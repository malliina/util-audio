package com.mle.audio.javasound

import javax.sound.sampled.{LineEvent, AudioSystem, SourceDataLine, AudioFormat}
import javax.sound.sampled.DataLine.Info
import com.mle.util.Log
import com.mle.audio.AudioImplicits._

/**
 * @author Michael
 */
trait JavaSoundBase extends Log {




  //  protected def openLine(format: AudioFormat, onLineEvent: LineEvent => Unit): SourceDataLine = {
  //    val line = buildLine(format, onLineEvent)
  //    line open format
  //    line
  //  }
}
