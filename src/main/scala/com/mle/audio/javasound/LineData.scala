package com.mle.audio.javasound

import java.net.URL
import com.mle.util.Log
import javax.sound.sampled.{LineEvent, AudioSystem}
import com.mle.audio.PlayerStates
import com.mle.audio.PlayerStates._

class LineData(url: URL, onLineEvent: LineEvent => Unit = e => ())
  extends JavaSoundBase with Log {
  private val urlIn = AudioSystem.getAudioInputStream(url)
  private val baseFormat = urlIn.getFormat
  val decodedFormat = toDecodedFormat(baseFormat)
  // this is read
  val decodedIn = AudioSystem.getAudioInputStream(decodedFormat, urlIn)
  // this is written to during playback
  val audioLine = openLine(decodedFormat, onLineEvent)

  def skip(bytes: Long) = {
    val skipped = decodedIn skip bytes
    log debug s"Attempted to skip $bytes bytes, skipped $skipped bytes"
    skipped
  }

  def state = {
    import PlayerStates._
    if (audioLine.isOpen) {
      if (audioLine.isActive) {
        Started
      } else {
        Stopped
      }
    } else {
      Closed
    }
  }

  def close() {
    audioLine.drain()
    audioLine.stop()
    audioLine.close()
    decodedIn.close()
  }
}