package tests

import java.nio.file.{Files, Path}

import com.malliina.audio.javasound.{FileJavaSoundPlayer, JavaSoundPlayer}
import com.malliina.file.FileUtilities
import com.malliina.util.Util
import org.apache.commons.io.FileUtils
import org.scalatest.FunSuite

import scala.concurrent.duration.Duration

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
    val player = new FileJavaSoundPlayer(file)
    Util.using(player)(f)
  }

  def assertPosition(pos: Duration, min: Long, max: Long) = {
    val seconds = pos.toSeconds
    assert(seconds >= min && seconds <= max, s"$seconds must be within [$min, $max]")
  }

  def sleep(duration: Duration) = Thread.sleep(duration.toMillis)
}
