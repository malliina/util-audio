package tests

import org.scalatest.FunSuite
import com.mle.util.Log

//import javax.media.Time

/**
 *
 * @author mle
 */
class Tests extends FunSuite with Log {
  test("tests work") {}

  test("time formatting") {
    import com.mle.audio.AudioImplicits._
    import concurrent.duration._
    def assertSeconds(secs: Int, expected: String) =
      assert(secs.seconds.readable === expected)
    assertSeconds(0, "00:00")
    assertSeconds(5, "00:05")
    assertSeconds(60, "01:00")
    assertSeconds(100, "01:40")
    assertSeconds(1000, "16:40")
    assertSeconds(3600, "01:00:00")
    assertSeconds(10000, "02:46:40")
  }
  test("uri tests") {
    TestTracks.difficultSongs(0).toUri
  }
}