package com.mle.audio.javasound

import com.mle.util.Log
import scala.concurrent.duration._
import com.mle.audio.meta.MediaInfo

/**
 * @author Michael
 */
trait Seekable extends Log {
  // Helper variable for seeking, needed because java sound getters return the time since the line was opened,
  // which is not equivalent to the track position if the user has seeked.
  var startedFromMicros = 0L

  def media: MediaInfo

  def duration = media.duration

  /**
   * Inaccurate. VBR etc.
   */
  protected def timeToBytes(pos: Duration): Long = {
    val ret = (1.0 * pos.toMicros / media.duration.toMicros * media.bytes).toLong
    log debug s"Seeking to position: ${pos.toSeconds} seconds which corresponds to $ret bytes out of ${media.bytes} total bytes"
    ret
  }

  protected def bytesToTime(bytes: Long): Duration = {
    (1.0 * bytes / media.bytes * media.duration.toMicros).micros
  }
}
