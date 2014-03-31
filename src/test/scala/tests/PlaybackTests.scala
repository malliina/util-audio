package tests

import com.mle.audio.javasound.JavaSoundPlayer

/**
 *
 * @author mle
 */
class PlaybackTests extends TestBase {
  test("can play mp3 and can get duration, position") {
    val file = ensureTestMp3Exists()
    val player = new JavaSoundPlayer(file)
    assert(player.duration.toSeconds === 12)
    player.play()
    Thread.sleep(4000)
    assert(player.position.toSeconds > 2)
  }
}
