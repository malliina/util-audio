package com.mle.audio.javasound

import java.net.URL
import com.mle.util.Log
import javax.sound.sampled.{AudioInputStream, LineEvent, AudioSystem}
import com.mle.audio.PlayerStates
import java.io.ByteArrayInputStream
import scala.util.Try

class LineData(inStream: AudioInputStream, onLineEvent: LineEvent => Unit = _ => ())
  extends JavaSoundBase
  with Log {
  def this(url: URL) = this(AudioSystem.getAudioInputStream(url))

  def this(bytes: Array[Byte]) = this(AudioSystem.getAudioInputStream(new ByteArrayInputStream(bytes)))

  private val baseFormat = inStream.getFormat
  val decodedFormat = toDecodedFormat(baseFormat)
  // this is read
  val decodedIn = AudioSystem.getAudioInputStream(decodedFormat, inStream)
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
    Try(inStream.close())
  }
}