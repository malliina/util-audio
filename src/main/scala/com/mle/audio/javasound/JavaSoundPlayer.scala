package com.mle.audio.javasound

import com.mle.util.Log
import scala.concurrent.{ExecutionContext, Future}
import java.nio.file.Path
import java.net.URI
import scala.concurrent.duration.Duration
import com.mle.audio._
import scala.Some
import com.mle.audio.meta.StreamInfo
import com.mle.storage.StorageSize
import java.io.InputStream

/**
 * The user needs to provide the media length and size to enable seek functionality.
 *
 * @param media media info to play
 */
class JavaSoundPlayer(val media: StreamInfo)(implicit val ec: ExecutionContext = ExecutionContexts.defaultPlaybackContext)
//  extends JavaSoundBase
  extends IPlayer
  with JavaSoundPlayerBase
  with Seekable
  with StateAwarePlayer
  with AutoCloseable
  with Log {

  def this(media: Path) = this(StreamInfo fromFile media)

  def this(uri: URI, duration: Duration, size: StorageSize) = this(StreamInfo(uri.toURL.openStream(), duration, size))

  //  private val url = media.uri.toURL
  private val stream = media.stream
  protected var lineData: LineData = newLine(stream)
  private var active = true
  private var playThread: Option[Future[Unit]] = None

  def audioLine = lineData.audioLine

  def controlDescriptions = audioLine.getControls.map(_.toString)

  def newLine(source: InputStream): LineData = LineData fromStream source

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
        resetLine(newLine(stream))
        startPlayback()
      case anythingElse =>
        startPlayback()
    }
  }

  def seek(pos: Duration) {
    val bytesPos = timeToBytes(pos)
    val skippedBytes = seekBytes(bytesPos)
    startedFromMicros = bytesToTime(skippedBytes).toMicros
  }

  def close(): Unit = {
    closeLine()
    stream.close()
  }

  def onPlaybackException() = onEndOfMedia()

  private def closeLine() {
    active = false
    lineData.close()
    startedFromMicros = 0L
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
    val seekedLine = newLine(stream)
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
    //    log.info(s"Starting playback of ${media.uri}")
    playThread = Some(Future(startPlayThread()).recover({
      // javazoom lib may throw at arbitrary playback moments
      case e: ArrayIndexOutOfBoundsException =>
        log warn(e.getClass.getName, e)
        closeLine()
        onPlaybackException()
    }))
  }

  private def startPlayThread(): Unit = {
    val data = new Array[Byte](16384)
    var bytesRead = 0
    var hasRead = false
    while (bytesRead != -1 && active) {
      // this is blocking, i guess
      bytesRead = lineData.decodedIn.read(data)
      if (bytesRead != -1) {
        if (!hasRead) {
          log.info(s"Now playing")
        }
        hasRead = true
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
