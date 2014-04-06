package tests

import java.nio.file.Paths
import com.mle.audio.javasound.FileJavaSoundPlayer
import scala.concurrent.duration.{Duration, DurationInt}

/**
 *
 * @author mle
 */
class SeekTest extends TestBase {
  val file = Paths get "F:\\musik\\NHL08 Soundtrack\\Paramore - Misery Business.mp3"
  test("seeking a file is accurate") {
    val player = new FileJavaSoundPlayer(file)
    player.play()
    sleep(100 millis)
    player seek 100.seconds
    sleep(3000 millis)
    assertPosition(player.position, 101, 105)
    player seek 1.seconds
    sleep(5000 millis)
    assertPosition(player.position, 2, 16)
  }

  def assertPosition(pos: Duration, min: Long, max: Long) = {
    val seconds = pos.toSeconds
    assert(seconds >= min && seconds <= max,s"$seconds must be within [$min, $max]")
  }
}
