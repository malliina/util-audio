package com.malliina.audio

import scala.concurrent.duration.Duration

/**
 * @author Michael
 */
trait RichPlayer extends IPlayer {
  def duration: Duration

  def position: Duration

  def volume: Int

  def mute: Boolean
}
