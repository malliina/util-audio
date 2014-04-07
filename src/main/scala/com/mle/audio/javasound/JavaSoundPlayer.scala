package com.mle.audio.javasound

import com.mle.util.Log
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.Duration
import com.mle.audio._
import com.mle.audio.meta.OneShotStream
import java.io.InputStream
import com.mle.storage.StorageSize
import com.mle.storage.StorageLong

/**
 * A music player. Plays one media source. To change source, for example to change track, create a new player.
 *
 * The user needs to provide the media length and size to enable seek functionality. Seeking streams which cannot be
 * reopened is only supported if InputStream.markSupported() of `media.stream` is true, and even then the support is
 * buggy. markSupported() is true at least for [[java.io.BufferedInputStream]]s.
 *
 * The stream provided in `media` is not by default closed when the player is closed, but if you wish to do so,
 * subclass this player and override `close()` accordingly or mix in trait [[SourceClosing]].
 *
 * @see [[FileJavaSoundPlayer]]
 * @see [[UriJavaSoundPlayer]]
 * @param media media info to play
 */
class JavaSoundPlayer(val media: OneShotStream)(implicit val ec: ExecutionContext = ExecutionContexts.defaultPlaybackContext)
  extends IPlayer
  with JavaSoundPlayerBase
  with StateAwarePlayer
  with AutoCloseable
  with Log {

  def this(stream: InputStream, duration: Duration, size: StorageSize) = this(OneShotStream(stream, duration, size))

  protected var stream = media.stream
  tryMarkStream()
  protected var lineData: LineData = newLine(stream)
  private var active = false
  private var playThread: Option[Future[Unit]] = None

  def audioLine = lineData.audioLine

  def controlDescriptions = audioLine.getControls.map(_.toString)

  def newLine(source: InputStream): LineData = LineData fromStream source

  def supportsSeek = stream.markSupported()

  def play() {
    lineData.state match {
      case PlayerStates.Started =>
        log info "Start playback issued but playback already started: doing nothing"
      case PlayerStates.Closed =>
        log warn "Cannot start playback of a closed track."
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

  /**
   * Regardless of whether the user seeks backwards or forwards, here is what we do:
   *
   * Reset the stream to its initial position. Skip bytes from the beginning. (Optionally continue playback.)
   *
   * The stream needs to support mark so that we can mark the initial position (constructor). Subsequent calls to
   * reset will therefore go to the initial position. Then we can skip the sufficient amount of bytes and arrive at
   * the correct position. Otherwise seeking would just skip bytes forward every time, relative to the current
   * position.
   *
   * This can still be spectacularly inaccurate if a VBR file is seeked but that is a secondary problem.
   *
   * @param pos position to seek to
   */
  def seek(pos: Duration): Unit = {
    seekProblem.map(problem => log.warn(problem)).getOrElse {
      val bytes = timeToBytes(pos)
      val skippedBytes = seekBytes(bytes)
      startedFromMicros = bytesToTime(skippedBytes).toMicros
    }
  }

  def seekProblem: Option[String] =
    if (lineData.state == PlayerStates.Closed) Some(s"Cannot seek a stream of a closed track.")
    else if (!stream.markSupported()) Some("Cannot seek because the media stream does not support marking; see InputStream.markSupported() for more details")
    else None

  def close(): Unit = closeLine()

  def onPlaybackException() = onEndOfMedia()

  def reset() {
    closeLine()
    stream = resetStream(stream)
    lineData = newLine(stream)
  }

  /**
   * Returns a stream of the media reset to its initial read position. Helper method for seeking.
   *
   * The default implementation merely calls `reset()` on the [[InputStream]] and returns the same instance. If
   * possible, override this method, close and open a new stream instead.
   *
   * @see [[BasicJavaSoundPlayer]]
   * @return a stream of the media reset to its initial read position
   */
  protected def resetStream(oldStream: InputStream): InputStream = {
    oldStream.reset()
    oldStream
  }

  private def closeLine() {
    active = false
    lineData.close()
    startedFromMicros = 0L
  }

  /**
   * Closes the current line, starts from the beginning and then skips to the specified byte count.
   *
   * @param byteCount bytes to skip from start of track
   * @return actual bytes skipped from the beginning of the media
   */
  private def seekBytes(byteCount: StorageSize): StorageSize = {
    // saves state
    val wasPlaying = lineData.state == PlayerStates.Started
    val wasMute = mute
    // seeks
    reset()
    val bytesSkipped = (lineData skip byteCount.toBytes).bytes
    // restores state
    if (wasPlaying) {
      play()
    }
    mute(wasMute)
    bytesSkipped
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
    val data = new Array[Byte](4096 * 4)
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

  private def tryMarkStream() {
    if (stream.markSupported()) {
      val markLimit = math.min(Integer.MAX_VALUE.toLong, 2 * media.size.toBytes).toInt
      stream mark markLimit
      //      log.info(s"Mark limit is: $markLimit")
    }
  }
}

