package tests

import scala.concurrent.duration.DurationInt
import java.io._
import com.mle.audio.javasound.JavaSoundPlayer
import com.mle.audio.meta.{OneShotStream, StreamSource}
import com.mle.storage.StorageInt
import scala.concurrent.{Await, Future}

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
  test("can seek and get position afterwards") {
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
    val stream = StreamSource.fromFile(file).toOneShot
    val player = new JavaSoundPlayer(stream)
    player.play()
    sleep(4 seconds)
    player.stop()
    player.close()
    stream.stream.close()
  }
  test("initialize player with closed stream throws IOException") {
    val file = ensureTestMp3Exists()
    val stream = StreamSource.fromFile(file).toOneShot
    stream.stream.close()
    intercept[IOException] {
      new JavaSoundPlayer(stream)
    }
  }
  test("playing an empty PipedInputStream blocks, and throws 'IOException: mark/reset not supported' when its PipedOutputStream is closed") {
    import com.mle.audio.ExecutionContexts.defaultPlaybackContext
    val dur = 1.minute
    val size = 100.megs
    val out = new PipedOutputStream()
    val in = new PipedInputStream(out)
    val fut = Future {
      new JavaSoundPlayer(OneShotStream(in, dur, size))
    }
    Thread.sleep(1000)
    out.close()
    Thread.sleep(500)
    assert(fut.isCompleted)
    val booleanFuture = fut.map(_ => false).recover {
      case t: IOException if t.getMessage == "mark/reset not supported" => true
      case t: Throwable => false
    }
    val futureCompletesAsExpected = Await.result(booleanFuture, 1.second)
    assert(futureCompletesAsExpected)
    in.close()
  }
}
