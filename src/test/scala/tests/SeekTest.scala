package tests

import java.nio.file.Paths
import com.mle.audio.javasound.FileJavaSoundPlayer
import scala.concurrent.duration.{Duration, DurationInt}

/**
 *
 * @author mle
 */
class SeekTest extends TestBase {
  val file = Paths get ""
  test("seeking a variable bitrate file is accurate") {
    val player = new FileJavaSoundPlayer(file)
    player.play()
    sleep(100 millis)
    player seek 100.seconds
    sleep(100 millis)
//    assertPosition(player.position, 98, 102)
    player seek 10.seconds
    sleep(100 millis)
    assertPosition(player.position, 8, 12)
  }

  def assertPosition(pos: Duration, min: Long, max: Long) = {
    val seconds = pos.toSeconds
    assert(seconds >= min && seconds <= max,s"$seconds must be within [$min, $max]")
  }
}
