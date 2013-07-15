package com.mle.audio.javasound

import javax.sound.sampled.{LineEvent, AudioSystem, SourceDataLine, AudioFormat}
import javax.sound.sampled.DataLine.Info
import com.mle.util.Log
import com.mle.audio.AudioImplicits._

/**
 * @author Michael
 */
trait JavaSoundBase extends Log {
  protected def toDecodedFormat(audioFormat: AudioFormat) = new AudioFormat(
    AudioFormat.Encoding.PCM_SIGNED,
    audioFormat.getSampleRate,
    16,
    audioFormat.getChannels,
    audioFormat.getChannels * 2,
    audioFormat.getSampleRate,
    false
  )

  protected def openLine(format: AudioFormat, onLineEvent: LineEvent => Unit) = {
    val info = new Info(classOf[SourceDataLine], format)
    val line = AudioSystem.getLine(info).asInstanceOf[SourceDataLine]
    // Careful to add line listeners before opening the line.
    line.addLineListener((e: LineEvent) => log debug s"Line event: $e")
    line addLineListener onLineEvent
    line open format
    line
  }
}
