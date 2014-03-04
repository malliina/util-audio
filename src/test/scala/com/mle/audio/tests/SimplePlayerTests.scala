package com.mle.audio.tests

import org.scalatest.FunSuite
import java.nio.file.Paths
import com.mle.audio.javasound.JavaSoundPlayer
import concurrent.duration._

/**
 *
 * @author mle
 */
class SimplePlayerTests extends FunSuite {
  test("sample code to play mp3") {
    val mp3 = Paths get ""
    val player = new JavaSoundPlayer(mp3)
    player.play()
    player.seek(25 seconds)
    player.play()

    Thread.sleep(5000)
  }
}
