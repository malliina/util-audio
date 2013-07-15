package com.mle.audio.clip

import java.net.URI
import javax.sound.sampled.AudioSystem
import com.mle.audio.javasound.JavaSoundBase

/**
 * @author Michael
 */
trait ClipBase extends JavaSoundBase {
  protected def toDecodedStream(uri: URI) = {
    val urlIn = AudioSystem.getAudioInputStream(uri.toURL)
    val baseFormat = urlIn.getFormat
    val decodedFormat = toDecodedFormat(baseFormat)
    AudioSystem.getAudioInputStream(decodedFormat, urlIn)
  }

  protected def openClip(uri: URI) = {
    val stream = toDecodedStream(uri)
    val clip = AudioSystem.getClip
    clip open stream
    clip
  }
}
