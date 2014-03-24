package com.mle.audio.javasound

import javax.sound.sampled.{Control, FloatControl, BooleanControl, SourceDataLine}
import com.mle.util.Log
import scala.concurrent.duration._
import com.mle.audio.{RichPlayer}

/**
 * @author Michael
 */
trait JavaSoundRichPlayer extends RichPlayer with Seekable with Log {
  protected def audioLine: SourceDataLine

  private val zeroGain = 0.4f
  private val maxGain = 1.0f

  private def microsSinceLineOpened = audioLine.getMicrosecondPosition

  def position = {
    val ret = (startedFromMicros + microsSinceLineOpened).micros
    log.info(s"microsSinceLineOpened: $microsSinceLineOpened, as a Duration: ${microsSinceLineOpened.micros}, startedFromMicros: $startedFromMicros, as a Duration: ${startedFromMicros.micros}, position as micros: ${startedFromMicros + microsSinceLineOpened}, as a Duration: $ret, in seconds: ${ret.toSeconds}")
    ret
  }

  def gainControl = control[FloatControl](FloatControl.Type.MASTER_GAIN)

  /**
   * Adjusts the volume
   * @param level [0.0F, 1.0F]
   */
  def gain(level: Float) {
    gainControl map (c => c.setValue(dbValue(level, c)))
  }

  def hasGainControl = Option(audioLine)
    .exists(_.isControlSupported(FloatControl.Type.MASTER_GAIN))

  def gain = gainControl.map(gainValue).getOrElse(0F)

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
