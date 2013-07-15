package com.mle.audio.tests

import org.scalatest.FunSuite
import com.mle.util.Log
import javax.sound.sampled.FloatControl
import com.mle.audio.clip.ClipPlayer

/**
 * @author Michael
 */
class ClipTest extends FunSuite with Log {
  test("java sound: clip") {
    val uri = TestTracks.difficultSongs(6).toUri
    val player = new ClipPlayer(uri)
    val gainControl = player.gainControl
    val maxGain = player.gainControl.getMaximum
    val minGain = player.gainControl.getMinimum
    val maxLabel = player.gainControl.getMaxLabel
    val gain = player.gainControl.getValue
    log info s"Gain: [$minGain,$maxGain] $maxLabel $gain $gainControl"
  }
  test("clip: hacked gain levels") {
    val zeroGain = 0.4f
    val minGain = 0.0f
    val maxGain = 1.0f
    val minDbGain = -80f
    val maxDbGain = 6f
    val posDbFactor = maxDbGain / (maxGain - zeroGain)
    val negDbFactor = zeroGain / -minDbGain
    def dbValue(gainLevel: Float) = {
      if (gainLevel >= zeroGain) {
        (gainLevel - zeroGain) * posDbFactor
      } else {
        (zeroGain - gainLevel) * minDbGain / zeroGain
      }
    }

    def gainValue(dbLevel: Float) = {
      if (dbLevel >= 0f) {
        zeroGain + dbLevel / posDbFactor
      } else {
        (-minDbGain + dbLevel) * negDbFactor
      }
    }
    val dbValues = Seq(0f, 0.1f, 0.4f, 0.8f, 1.0f).map(g => (g, dbValue(g)))
    val gainValues = Seq(-80f, -30f, -10f, 0f, 1f, 3f, 4f, 6f).map(db => (db, gainValue(db)))
    log info "Decibels: " + dbValues
    log info "Gains: " + gainValues

  }
}
