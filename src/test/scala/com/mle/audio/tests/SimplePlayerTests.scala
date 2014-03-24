package com.mle.audio.tests

import org.scalatest.FunSuite
import java.nio.file.Paths
import com.mle.audio.javasound.JavaSoundPlayer
import concurrent.duration._
import com.mle.util.Scheduling

/**
 *
 * @author mle
 */
class SimplePlayerTests extends FunSuite {
  val mp3 = Paths get ""

  test("sample code to play mp3") {
    val player = new JavaSoundPlayer(mp3)
    player.play()
    player.seek(25 seconds)
    player.play()
    Thread.sleep(5000)
  }
  test("JavaSoundPlayer.position") {
    val player = new JavaSoundPlayer(mp3)
    Scheduling.every(1.second) {
      println(player.position.toSeconds)
    }
    player.play()
    Thread.sleep(5000)
  }
}
