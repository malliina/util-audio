package com.mle.audio

/**
 * @author Michael
 */
trait StateAwarePlayer extends IPlayer {
  def state: PlayerStates.PlayerState

  def onEndOfMedia()
}
