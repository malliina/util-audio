package com.mle.audio.tests

import org.scalatest.FunSuite
import org.jaudiotagger.audio.AudioFileIO
import com.mle.util.Log

/**
 * @author Michael
 */
class TagTests extends FunSuite with Log {
  test("jaudiotagger") {
    val song = TestTracks.difficultSongs(10)
    val f = AudioFileIO.read(song.toFile)
    val length = f.getAudioHeader.getTrackLength
    log info s"track length is: $length"
  }
}
