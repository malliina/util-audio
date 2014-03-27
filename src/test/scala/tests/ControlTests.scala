package tests

import org.scalatest.FunSuite
import com.mle.audio.javasound.JavaSoundPlayer
import java.nio.file.Files
import com.mle.util.{FileUtilities, Util}
import org.apache.commons.io.FileUtils

/**
 *
 * @author mle
 */
class ControlTests extends FunSuite {
  val fileName = "mpthreetest.mp3"
  val tempFile = FileUtilities.tempDir resolve fileName

  test("supported controls") {
    if (!Files.exists(tempFile)) {
      val resourceURL = Util.resourceOpt("mpthreetest.mp3")
      val url = resourceURL.getOrElse(throw new Exception(s"Resource not found: " + fileName))
      FileUtils.copyURLToFile(url, tempFile.toFile)
      if (!Files.exists(tempFile)) {
        throw new Exception(s"Unable to access $tempFile")
      }
    }

    val player = new JavaSoundPlayer(tempFile)
    assert(player.canAdjustVolume)
  }
}
