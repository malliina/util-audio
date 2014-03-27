package com.mle.audio

import scala.concurrent.duration.Duration

/**
 * @author Michael
 */
trait IPlayer extends AutoCloseable {
  /**
   * Starts or resumes playback, whichever makes sense.
   */
  def play()

  /**
   * Pauses playback.
   */
  def stop()

  //  def duration: Time
  //
  //  def position: Time

  /**
   * Skips to the given position, TODO type of parameter
   * @param pos
   */
  def seek(pos: Duration)

  /**
   * Adjusts the volume.
   *
   * @param level [0, 100]
   */
  def volume(level: Int)

  /**
   * Mutes/unmutes the player.
   *
   * @param mute true to mute, false to unmute
   */
  def mute(mute: Boolean)

  def toggleMute()

  //  def state: PlayerStates.PlayerState

  /**
   * Releases any player resources (input streams, ...). Playback is stopped.
   */
  def close()
}
