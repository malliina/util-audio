package com.mle.audio

import scala.concurrent.duration.Duration

/**
 *
 * @author mle
 */
object PlaybackEvents {

  trait PlaybackEvent

  case object EndOfMedia extends PlaybackEvent

  case class TimeUpdated(position: Duration) extends PlaybackEvent

  case object Started extends PlaybackEvent

  case object Stopped extends PlaybackEvent

  case object Closed extends PlaybackEvent
}
