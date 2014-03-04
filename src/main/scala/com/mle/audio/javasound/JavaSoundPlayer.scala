package com.mle.audio.javasound

import com.mle.util.Log
import concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.nio.file.Path
import java.net.URL
import scala.concurrent.duration.Duration
import com.mle.audio._
import scala.Some
import com.mle.audio.meta.MediaInfo


/**
 * The user needs to provide the media length and size to enable seek functionality.
 *
 * TODO this needs work when it comes to concurrent operations
 *
 * @param media
 */
class JavaSoundPlayer(val media: MediaInfo)
  extends JavaSoundBase
  with IPlayer
  with JavaSoundRichPlayer
  with Seekable
  with StateAwarePlayer
  with AutoCloseable
  with Log {
  def this(media: Path) = this(MediaInfo fromPath media)

  private val url = media.uri.toURL
  var lineData = newLine(url)
  private var active = true
  private var playThread: Option[Future[Unit]] = None

  def audioLine = lineData.audioLine

  def newLine(source: URL): LineData = new LineData(source)

  def stop() {
    active = false
    audioLine.stop()
  }

  // should no-op if already playing
  def play() {
    lineData.state match {
      case PlayerStates.Started =>
        log info "Start playback issued but playback already started: doing nothing"
      case PlayerStates.Closed =>
        log info "Starting playback of closed track"
        resetLine(newLine(url))
        startPlayback()
      case anythingElse =>
        startPlayback()
    }
  }

  def seek(pos: Duration) {
    val bytesPos = timeToBytes(pos)
    val skippedBytes = seekBytes(bytesPos)
    startedFromNanos = bytesToTime(skippedBytes).toNanos
  }

  def close() {
    closeLine()
  }

  def onPlaybackException() {
    onEndOfMedia()
  }

  private def closeLine() {
    active = false
    lineData.close()
    startedFromNanos = 0L
  }

  /**
   * Closes the current line, starts from the beginning
   * and then skips to the specified byte count.
   *
   * @param byteCount bytes to skip from start of track
   * @return actual bytes skipped
   */
  private def seekBytes(byteCount: Long) = {
    val wasPlaying = lineData.state == PlayerStates.Started
    val wasMute = mute
    //    val previousGain = gain
    val seekedLine = newLine(url)
    val bytesSkipped = seekedLine skip byteCount
    resetLine(seekedLine)
    if (wasPlaying) {
      play()
    }
    mute(wasMute)
    //    gain(previousGain)
    bytesSkipped
  }

  private def resetLine(newLineData: LineData) {
    closeLine()
    lineData = newLineData
  }

  private def startPlayback() {
    active = true
    audioLine.start()
    playThread = Some(Future(startPlayThread()).recover({
      // javazoom lib may throw at arbitrary playback moments
      case e: ArrayIndexOutOfBoundsException =>
        log warn("Boom!", e)
        closeLine()
        onPlaybackException()
    }))
  }

  private def startPlayThread() {
    val data = new Array[Byte](16384)
    var bytesRead = 0
    while (bytesRead != -1 && active) {
      // this is blocking, i guess
      bytesRead = lineData.decodedIn.read(data)
      if (bytesRead != -1) {
        audioLine.write(data, 0, bytesRead)
      } else {
        // cleanup
        closeLine()
        // -1 bytes read means "end of stream has been reached"
        onEndOfMedia()
      }
    }
  }

  def state = lineData.state
}
