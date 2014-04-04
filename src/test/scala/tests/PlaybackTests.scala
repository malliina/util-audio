package tests

import scala.concurrent.duration.DurationInt
import java.io._
import com.mle.audio.javasound.JavaSoundPlayer
import com.mle.audio.meta.StreamInfo
import com.mle.storage.StorageInt

/**
 *
 * @author mle
 */
class PlaybackTests extends TestBase {
  test("can play mp3 and can get duration, position") {
    withTestTrack(player => {
      assert(player.duration.toSeconds === 12)
      player.play()
      Thread.sleep(4000)
      assert(player.position.toSeconds > 2)
    })
  }
  test("can seek and get position duration afterwards") {
    withTestTrack(player => {
      player.play()
      sleep(100 millis)
      player seek 3.seconds
      sleep(500 millis)
      assert(player.position.toSeconds >= 2)
    })
  }
  test("can seek backwards - not sure what's up, this only succeeds when run individually, are my tests not isolated?") {
    withTestTrack(player => {
      player.play()
      sleep(10 millis)
      player seek 8.seconds
      sleep(100 millis)
      assert(player.position.toSeconds >= 7)
      player seek 3.seconds
      sleep(300 millis)
      val pos = player.position.toSeconds
      assert(pos >= 2 && pos <= 4)
    })
  }
  test("can stream") {
    val file = ensureTestMp3Exists()
    val stream = new BufferedInputStream(new FileInputStream(file.toFile))
    val dur = 1.minute
    val size = 10.megs
    val player = new JavaSoundPlayer(StreamInfo(stream, dur, size))
    player.play()
    sleep(4 seconds)
  }
  //  test("can open empty InputStream - no, this will block"){
  //    val dur = 1.minute
  //    val size = 100.megs
  //    val out = new PipedOutputStream()
  //    val in = new PipedInputStream(out)
  //    val player = new JavaSoundPlayer(StreamInfo(in, dur, size))
  //  }
}
