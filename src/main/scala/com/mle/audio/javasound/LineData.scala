package com.mle.audio.javasound

import com.mle.util.Log
import javax.sound.sampled.{AudioInputStream, LineEvent, AudioSystem}
import com.mle.audio.PlayerStates
import java.io.InputStream

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
  def fromStream(stream: InputStream) = new LineData(AudioSystem.getAudioInputStream(stream))
}

class LineData(inStream: AudioInputStream, onLineEvent: LineEvent => Unit = _ => ())
  extends JavaSoundBase
  with Log {

  private val baseFormat = inStream.getFormat
  val decodedFormat = toDecodedFormat(baseFormat)
  // this is read
  val decodedIn = AudioSystem.getAudioInputStream(decodedFormat, inStream)
  private val line = buildLine(decodedFormat, onLineEvent)
  //  private val subject = Subject[LineEvent.Type]()
  //  audioLine.addLineListener((e: LineEvent) => subject.onNext(e.getType))
  //  val events: Observable[LineEvent.Type] = subject
  line open decodedFormat
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
    // drain may block and I believe it has caused some issues; it is even necessary to call it here?
    //    audioLine.drain()
    audioLine.stop()
    audioLine.flush()
    audioLine.close()
    //    decodedIn.close()
    //    Try(inStream.close())
    //    subject.onCompleted()
  }
}