package tests

import org.scalatest.FunSuite
import com.mle.audio.javasound.JavaSoundPlayer
import java.nio.file.{Path, Files}
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
    val file = ensureTestMp3Exists()
    val player = new JavaSoundPlayer(file)
    assert(player.canAdjustVolume)
  }
  test("volume conversions") {
    val file = ensureTestMp3Exists()
    val player = new JavaSoundPlayer(file)
    val e1 = player.externalVolumeValue(25000, 0, 65663)
    assert(e1 === (25000f / 65663f * 100f).toInt)
    val e2 = player.externalVolumeValue(54000, 0, 54000)
    assert(e2 === 100)
    val e3 = player.externalVolumeValue(0, 0, 65000)
    assert(e3 === 0)
    val i1 = player.internalVolumeValue(50, -100, 100)
    assert(i1 === 0)
    val i2 = player.internalVolumeValue(40, 0, 65536)
    assert(i2.toInt === (0.40 * 65536).toInt)
  }

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
}
