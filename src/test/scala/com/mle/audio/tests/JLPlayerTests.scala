package com.mle.audio.tests

import org.scalatest.FunSuite
import javazoom.jl.player.Player
import java.io.{BufferedInputStream, FileInputStream}
import actors.Futures

/**
 * @author Michael
 */
class JLPlayerTests extends FunSuite {
  test("can play easy song") {
    play(0)
  }
  test("can play difficult song") {
    play(9)
  }

  private def play(index: Int, sleepDuration: Int = 2000) {
    val path = TestTracks.difficultSongs(index)
    val bis = new BufferedInputStream(new FileInputStream(path.toFile))
    val p = new Player(bis)
    Futures.future(p.play())
    Thread sleep sleepDuration
  }
}
