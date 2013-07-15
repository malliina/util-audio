package com.mle.audio.tests

import org.scalatest.FunSuite
import com.mle.util.{FileUtilities, Log}
import java.nio.file.{Files, Paths}
import javax.sound.sampled.{UnsupportedAudioFileException, AudioSystem}
import java.io.IOException
import com.mle.audio.clip.ClipPlayer


/**
 * @author Michael
 */
class Mp3Tests extends FunSuite with Log {
  test("mp3 playback") {
    val musicFolder = Paths.get( "")
    val files = FileUtilities.listPaths(musicFolder)
    val mp3files = for (file <- files
                        if (Files.isRegularFile(file))
                        if (file.getFileName.toString.endsWith(".mp3"))
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
  }
}
