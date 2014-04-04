package tests

import org.scalatest.FunSuite
import com.mle.util.{Util, FileUtilities}
import java.nio.file.{Files, Path}
import org.apache.commons.io.FileUtils
import com.mle.audio.javasound.JavaSoundPlayer
import scala.concurrent.duration.Duration

/**
 *
 * @author mle
 */
class TestBase extends FunSuite {
  val fileName = "mpthreetest.mp3"
  val tempFile = FileUtilities.tempDir resolve fileName

  def ensureTestMp3Exists(): Path = {
    if (!Files.exists(tempFile)) {
      val resourceURL = Util.resourceOpt(fileName)
      val url = resourceURL.getOrElse(throw new Exception(s"Resource not found: " + fileName))
      FileUtils.copyURLToFile(url, tempFile.toFile)
      if (!Files.exists(tempFile)) {
        throw new Exception(s"Unable to access $tempFile")
      }
    }
    tempFile
  }

  def withTestTrack[T](f: JavaSoundPlayer => T): T = {
    val file = ensureTestMp3Exists()
    val player = new JavaSoundPlayer(file)
    Util.using(player)(f)
  }

  def sleep(duration: Duration) = Thread.sleep(duration.toMillis)
}
