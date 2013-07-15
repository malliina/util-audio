package com.mle.audio.tests

import org.scalatest.FunSuite
import com.mle.util.Log
import java.nio.file.Paths
import com.mle.audio.javasound.JavaSoundPlayer

/**
 * @author Michael
 */
//class JavaSoundTests extends FunSuite with Log {
//  test("MP3SPI with javasound: play difficult MP3s") {
//    val songPath = TestTracks.difficultSongs(9)
//    val player = new JavaSoundPlayer(songPath){
//      def onEndOfMedia() {
//        log info "EOM"
//      }
//    }
//    player.play()
//    Thread.sleep(3000)
//    player.seek(new Time(10d))
//    Thread.sleep(3000)
//    player.stop()
//    Thread.sleep(3000)
//    player.play()
//    player.seek(new Time(220d))
//    Thread.sleep(60000)
//    player.stop()
//  }
//
//}
