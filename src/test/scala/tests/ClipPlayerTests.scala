package tests

import org.scalatest.FunSuite
import com.mle.audio.clip.ClipPlayer

/**
 * @author Michael
 */
class ClipPlayerTests extends FunSuite {
  test("play easy song") {
    play(0)
  }
  test("play difficult song") {
    play(1, 2000)
  }
  test("play difficult song 2") {
    play(9, 2000)
  }
  test("play TRANCEPORT") {
    play(10, 2000)
  }

  private def play(index: Int, sleepDuration: Int = 2000) {
    val uri = TestTracks.difficultSongs(index).toUri
    val player = new ClipPlayer(uri)
    player.play()
    Thread sleep sleepDuration
    player.stop()
  }
}
