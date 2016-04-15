package tests

import java.io.IOException
import java.nio.file.{Files, Path}
import javax.sound.sampled.{AudioSystem, UnsupportedAudioFileException}

import com.malliina.file.FileUtilities
import org.scalatest.FunSuite
import org.slf4j.LoggerFactory

class Mp3Tests extends FunSuite {
  private val log = LoggerFactory.getLogger(getClass)

  test("mp3 playback") {
    val musicFolder: Option[Path] = None // Paths.get("")
    musicFolder.foreach(folder => {
      val files = FileUtilities.listPaths(folder)
      val mp3files = for (file <- files
                          if Files.isRegularFile(file)
                          if file.getFileName.toString.endsWith(".mp3")
      ) yield file
      var processed = 0
      val (succeeded, failed) = mp3files.par.partition(mp3 => {
        try {
          AudioSystem.getAudioInputStream(mp3.toUri.toURL)
          true
        } catch {
          case ue: UnsupportedAudioFileException =>
            //          log debug "Unsupported: " + mp3.getFileName
            false
          case ioe: IOException =>
            //          log debug "IO: " + mp3.getFileName
            false
        } finally {
          processed += 1
          if (processed % 100 == 0) {
            println(s"Processed: $processed")
          }
        }
      })
      log info failed.map(_.getFileName).mkString("\n")
      log info s"Files: ${files.size}, MP3s: ${mp3files.size}, working: ${succeeded.size}, failed: ${failed.size}, failure rate: ${1.0 * failed.size / mp3files.size}"
    })
  }
}
