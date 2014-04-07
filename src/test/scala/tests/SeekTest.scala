package tests

import java.nio.file.Paths
import com.mle.audio.javasound.FileJavaSoundPlayer
import scala.concurrent.duration.DurationInt

/**
 *
 * @author mle
 */
class SeekTest extends TestBase {
  val file = None // Paths get ""
  test("seeking a file is accurate") {
    file.foreach(f => {
      val player = new FileJavaSoundPlayer(f)
      player.play()
      sleep(100 millis)
      val first = 200
      player seek first.seconds
      sleep(3000 millis)
      assertPosition(player.position, first, first + 6)
      val second = 1
      player seek second.seconds
      sleep(3000 millis)
      assertPosition(player.position, second + 1, second + 5)
    })
  }
}
