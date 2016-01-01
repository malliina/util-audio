package com.malliina.audio.javasound

import com.malliina.audio.meta.OneShotStream
import com.malliina.storage.{StorageLong, StorageSize}
import com.malliina.util.Log

import scala.concurrent.duration._

/**
 * @author Michael
 */
trait Seekable extends Log {
  // Helper variable for seeking, needed because java sound getters return the time since the line was opened,
  // which is not equivalent to the track position if the user has seeked.
  var startedFromMicros = 0L

  def media: OneShotStream

  def duration = media.duration

  /**
   * Inaccurate. VBR etc.
   */
  protected def timeToBytes(pos: Duration): StorageSize = {
    val ret = (1.0 * pos.toMicros / media.duration.toMicros * media.size.toBytes).toLong.bytes
    log debug s"Seeking to position: ${pos.toSeconds} seconds which corresponds to $ret bytes out of ${media.size}"
    ret
  }

  protected def bytesToTime(bytes: StorageSize): Duration = {
    (1.0 * bytes.toBytes / media.size.toBytes * media.duration.toMicros).micros
  }
}
