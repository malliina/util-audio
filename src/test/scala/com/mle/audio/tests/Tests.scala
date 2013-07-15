package com.mle.audio.tests

import org.scalatest.FunSuite
import com.mle.util.Log
import com.mle.audio.AudioImplicits._
//import javax.media.Time
import org.joda.time.format.PeriodFormatterBuilder
import org.joda.time.Period
import java.util.concurrent.TimeUnit

/**
 *
 * @author mle
 */
class Tests extends FunSuite with Log {
  test("tests work") {}

//  test("time formatting") {
//    val time: Time = 5.seconds
//    val timeMillis = time.getNanoseconds / 1000
//    val formatter = new PeriodFormatterBuilder()
//      .appendMinutes()
//      .appendSuffix("::")
//      .appendSeconds()
//      .toFormatter
//    val readable = "%d:%d".format(
//      TimeUnit.MINUTES.toMinutes(timeMillis),
//      TimeUnit.SECONDS.toSeconds(timeMillis)
//    )
//    log info readable
//    val period = Period.seconds(3781).normalizedStandard()
//    val readable2 = "%02d:%02d:%02d".format(
//      period.getHours, period.getMinutes, period.getSeconds
//    )
//    log info readable2
//  }
  test("uri tests") {
    TestTracks.difficultSongs(0).toUri
  }
}
