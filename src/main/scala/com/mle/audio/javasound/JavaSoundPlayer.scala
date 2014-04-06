package com.mle.audio.javasound

import com.mle.util.Log
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.Duration
import com.mle.audio._
import scala.Some
import com.mle.audio.meta.StreamInfo
import java.io.InputStream

/**
 * A music player. Plays one media source. To change source, for example to change track, create a new player.
 *
 * The user needs to provide the media length and size to enable seek functionality, however, seeking is only
 * supported if InputStream.markSupported() of `media.stream` is true. It tends to be true at least for
 * [[java.io.FileInputStream]]s.
 *
 * The stream provided in `media` is not by default closed when the player is closed, but if you wish to do so,
 * subclass this player and override `close()` accordingly or mix in trait [[SourceClosing]].
 *
 * @see [[FileJavaSoundPlayer]]
 * @see [[UriJavaSoundPlayer]]
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

  private val stream = media.stream
  if (stream.markSupported()) {
    val markLimit = math.min(Integer.MAX_VALUE, media.size.toBytes).toInt
    stream mark markLimit
  }
  protected var lineData: LineData = newLine(stream)
  private var active = true
  private var playThread: Option[Future[Unit]] = None

  def audioLine = lineData.audioLine

  def controlDescriptions = audioLine.getControls.map(_.toString)

  def newLine(source: InputStream): LineData = LineData fromStream source

  def play() {
    lineData.state match {
      case PlayerStates.Started =>
        log info "Start playback issued but playback already started: doing nothing"
      case PlayerStates.Closed =>
        log warn "Attempting to start playback of a closed player. This is incorrect."
      // After end of media, the InputStream is closed and cannot be reused. Therefore this player cannot be used.
      // It's incorrect to call methods on a closed player. In principle we should throw an exception here, but I try
      // to resist the path of the IllegalStateException.
      case anythingElse =>
        startPlayback()
    }
  }

  def stop() {
    active = false
    audioLine.stop()
  }

  def seek(pos: Duration) {
    if (stream.markSupported()) {
      val bytesPos = timeToBytes(pos)
      val skippedBytes = seekBytes(bytesPos)
      startedFromMicros = bytesToTime(skippedBytes).toMicros
    } else {
      log.warn("Cannot seek because the media stream does not support marking; see InputStream.markSupported() for more details")
    }
  }

  def close(): Unit = closeLine()

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
    stream.reset()
    val seekedLine = newLine(stream)
    val bytesSkipped = seekedLine skip byteCount
    resetLine(seekedLine)
    if (wasPlaying) {
      play()
    }
    mute(wasMute)
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
    log.info("Stopped playback")
  }

  def state = lineData.state
}
