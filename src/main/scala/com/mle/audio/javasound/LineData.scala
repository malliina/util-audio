package com.mle.audio.javasound

import com.mle.util.Log
import javax.sound.sampled._
import com.mle.audio.PlayerStates
import java.io.InputStream
import com.mle.audio.AudioImplicits._
import rx.lang.scala.Subject
import javax.sound.sampled.DataLine.Info

object LineData {
  /**
   * This factory method blocks as long as `stream` is empty, i.e. until an appropriate amount
   * of audio bytes has been made available to it.
   *
   * Therefore you must not, in the same thread, call this before bytes are made available to
   * the stream.
   *
   * @param stream
   * @return
   */
  def fromStream(stream: InputStream, subject: Subject[PlayerStates.PlayerState]) =
    new LineData(AudioSystem.getAudioInputStream(stream), subject)
}

class LineData(inStream: AudioInputStream, subject: Subject[PlayerStates.PlayerState])
  extends JavaSoundBase
  with Log {

  private val baseFormat = inStream.getFormat
  private val decodedFormat = toDecodedFormat(baseFormat)
  // this is read
  private val decodedIn = AudioSystem.getAudioInputStream(decodedFormat, inStream)
  // this is written to during playback
  val line = buildLine(decodedFormat)
  line.addLineListener((lineEvent: LineEvent) => subject.onNext(toPlayerEvent(lineEvent)))
  line open decodedFormat

  def toPlayerEvent(lineEvent: LineEvent): PlayerStates.PlayerState = {
    import LineEvent.Type._
    import PlayerStates._
    val eventType = lineEvent.getType
    if (eventType == OPEN) Open
    else if (eventType == CLOSE) Closed
    else if (eventType == START) Started
    else if (eventType == STOP) Stopped
    else Unknown
  }

  def read(buffer: Array[Byte]) = decodedIn.read(buffer)

  def skip(bytes: Long) = {
    val skipped = decodedIn skip bytes
    log debug s"Attempted to skip $bytes bytes, skipped $skipped bytes"
    skipped
  }

  def state = {
    import PlayerStates._
    if (line.isOpen) {
      if (line.isActive) {
        Started
      } else {
        Stopped
      }
    } else {
      Closed
    }
  }

  def close() {
    line.stop()
    line.flush()
    line.close()
  }

  private def buildLine(format: AudioFormat): SourceDataLine = {
    val info = new Info(classOf[SourceDataLine], format)
    val line = AudioSystem.getLine(info).asInstanceOf[SourceDataLine]
    // Add line listeners before opening the line.
    line.addLineListener((e: LineEvent) => log debug s"Line event: $e")
    line
  }

  protected def toDecodedFormat(audioFormat: AudioFormat) = new AudioFormat(
    AudioFormat.Encoding.PCM_SIGNED,
    audioFormat.getSampleRate,
    16,
    audioFormat.getChannels,
    audioFormat.getChannels * 2,
    audioFormat.getSampleRate,
    false
  )
}