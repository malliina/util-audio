package com.mle.audio.javasound

import javax.sound.sampled.{Control, FloatControl, BooleanControl, SourceDataLine}
import com.mle.util.Log
import scala.concurrent.duration._
import com.mle.audio.RichPlayer

/**
 * @author Michael
 */
trait JavaSoundRichPlayer extends RichPlayer with Seekable with Log {
  protected def audioLine: SourceDataLine

  private val zeroGain = 0.4f
  private val maxGain = 1.0f

  /**
   * Bug: DataLine.getMicrosecondPosition returns milliseconds on Oracle's ARM JVM.
   *
   * To workaround the bug, this method conditionally converts what may be milliseconds
   * to microseconds. However, that's based on guessing, so when the position is incorrectly
   * reported as milliseconds and the position is over 1000 seconds, no conversion takes
   * place and milliseconds are incorrectly returned.
   *
   * Better implementations are welcome.
   *
   * @return microseconds since the line was opened
   */
  private def microsSinceLineOpened4 = {
    val microsOrMillis = audioLine.getMicrosecondPosition
    val multiplier = if (microsOrMillis > 1000 && microsOrMillis < 1000000) 1000L else 1L
    multiplier * microsOrMillis
  }

  private def microsSinceLineOpened = framesToMicroseconds(audioLine.getLongFramePosition)

  private def microsSinceLineOpened2 = {
    val frameSizeBytes = audioLine.getFormat.getFrameSize
    val positionInBytes = audioLine.getLongFramePosition * frameSizeBytes
    (1.0 * positionInBytes / media.bytes) * media.duration.toMicros
  }

  // http://stackoverflow.com/questions/9470148/how-do-you-play-a-long-audioclip see if this formula works better
  private def framesToMicroseconds(frames: Long): Long =
    (frames / audioLine.getFormat.getSampleRate.toLong) * 1000000L

  def position: Duration = {
    val ret = (startedFromMicros + microsSinceLineOpened).micros
    log.info(s"microsSinceLineOpened: $microsSinceLineOpened, position as micros: ${startedFromMicros + microsSinceLineOpened}, as a Duration: $ret, in seconds: ${ret.toSeconds}")
    ret
  }

  def gainControl = control[FloatControl](FloatControl.Type.MASTER_GAIN)

  /**
   * Adjusts the volume
   * @param level [0.0F, 1.0F]
   */
  def gain(level: Float): Unit =
    gainControl foreach (c => c.setValue(dbValue(level, c)))

  def hasGainControl = Option(audioLine)
    .exists(_.isControlSupported(FloatControl.Type.MASTER_GAIN))

  def gain = gainControl.map(gainValue).getOrElse {
    log.info(s"Unable to find gain control; returning 0f as gain.")
    0F
  }

  def muteControl = control[BooleanControl](BooleanControl.Type.MUTE)

  def mute(shouldMute: Boolean) {
    muteControl.foreach(c => c.setValue(shouldMute))
  }

  def mute =
    muteControl.exists(_.getValue)

  def toggleMute() {
    mute(!mute)
  }

  // blaaaaaaaaaaaaaaaaah
  private def control[T](controlType: Control.Type) = Option(audioLine)
    .filter(_.isControlSupported(controlType))
    .map(_.getControl(controlType).asInstanceOf[T])

  /**
   *
   * @param gainControl
   * @return [0.0F, 1.0F]
   */
  private def gainValue(gainControl: FloatControl) = {
    val maxDbGain = gainControl.getMaximum
    val minDbGain = gainControl.getMinimum
    val posDbFactor = maxDbGain / (maxGain - zeroGain)
    val negDbFactor = zeroGain / -minDbGain
    val dbLevel = gainControl.getValue
    if (dbLevel >= 0f) {
      zeroGain + dbLevel / posDbFactor
    } else {
      (-minDbGain + dbLevel) * negDbFactor
    }
  }

  /**
   *
   * @param normalizedValue [0.0F, 1.0F]
   * @param gainControl
   * @return db value
   */
  private def dbValue(normalizedValue: Float, gainControl: FloatControl) = {
    val maxDbGain = gainControl.getMaximum
    val minDbGain = gainControl.getMinimum
    val posDbFactor = maxDbGain / (maxGain - zeroGain)
    if (normalizedValue >= zeroGain) {
      (normalizedValue - zeroGain) * posDbFactor
    } else {
      (zeroGain - normalizedValue) * minDbGain / zeroGain
    }
  }


}
