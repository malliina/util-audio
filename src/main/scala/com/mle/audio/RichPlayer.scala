package com.mle.audio

import scala.concurrent.duration.Duration

/**
 * @author Michael
 */
trait RichPlayer extends IPlayer {
  def duration: Duration

  def position: Duration

  def gain: Float

  def mute: Boolean
}
