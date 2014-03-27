package com.mle.audio.clip

import com.mle.util.Log
import java.net.URI
import javax.sound.sampled._
import scala.concurrent.duration._
import com.mle.audio.{PlayerStates, GainHack, StateAwarePlayer, RichPlayer}
import com.mle.audio.AudioImplicits._

/**
 * @author Michael
 */
class ClipPlayer(media: URI)
  extends ClipBase
  with RichPlayer
  with StateAwarePlayer
  with GainHack
  with AutoCloseable
  with Log {
  val stream = toDecodedStream(media)
  val clip = AudioSystem.getClip
  private var playerState: PlayerStates.PlayerState = PlayerStates.NoMedia
  clip.addLineListener((e: LineEvent) => {
    if (e.getType == LineEvent.Type.STOP && clip.getFramePosition == clip.getFrameLength) {
      onEndOfMedia()
    }
    playerState = e.getType match {
      case LineEvent.Type.OPEN => PlayerStates.Open
      case LineEvent.Type.START => PlayerStates.Started
      case LineEvent.Type.STOP => PlayerStates.Stopped
      case LineEvent.Type.CLOSE => PlayerStates.Closed
      case anythingElse => {
        log warn "Unknown line event: " + anythingElse
        PlayerStates.NoMedia
      }
    }
  })
  clip open stream
  private val supportedControls = clip.getControls.map(_.getType)
  require(supportedControls contains BooleanControl.Type.MUTE, "media does not support mute")
  require(supportedControls contains FloatControl.Type.MASTER_GAIN, "media does not support gain")

  val muteControl = clip.getControl(BooleanControl.Type.MUTE)
    .asInstanceOf[BooleanControl]
  val gainControl = clip.getControl(FloatControl.Type.MASTER_GAIN)
    .asInstanceOf[FloatControl]

  def play(): Unit = clip.start()

  def duration = clip.getMicrosecondLength.microseconds

  def position = clip.getMicrosecondPosition.microseconds

  def stop(): Unit = clip.stop()

  def seek(pos: Duration): Unit = clip.setMicrosecondPosition(pos.toNanos / 1000)

  def gain = gainValue(gainControl.getValue)

  override def volume = (gain * 100).toInt

  def gain(level: Float): Unit = gainControl setValue dbValue(level)

  override def volume(level: Int): Unit = gain(1.0F * level / 100)

  def mute = muteControl.getValue

  def mute(mute: Boolean): Unit = muteControl setValue mute

  def toggleMute(): Unit = mute(!muteControl.getValue)

  def state = playerState

  def close(): Unit = clip.close()
}
