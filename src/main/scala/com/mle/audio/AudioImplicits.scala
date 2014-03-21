package com.mle.audio

import org.joda.time.Period
import javax.sound.sampled.{LineListener, LineEvent}
import scala.concurrent.duration.Duration

/**
 *
 * @author Michael
 */
object AudioImplicits {
  // TODO implicit classes yo scala 2.10
  implicit def lineEvent2listener(onEvent: LineEvent => Unit) = new LineListener {
    def update(event: LineEvent) {
      onEvent(event)
    }
  }

  implicit def dur2readable(t: Duration) = new {
    val inSeconds = t.toSeconds.toInt
    private val period = Period.seconds(inSeconds).normalizedStandard()
    private val stringified =
      if (inSeconds >= 3600) {
        "%02d:%02d:%02d".format(period.getHours, period.getMinutes, period.getSeconds)
      } else {
        "%02d:%02d".format(period.getMinutes, period.getSeconds)
      }

    def readable = stringified
  }
}
