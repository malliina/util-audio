package com.mle.audio.javasound

/**
 * Mix this in to [[JavaSoundPlayer]]s when you want to close the audio stream
 * when the player is closed. Whether you want to do that depends on who creates
 * the stream.
 *
 * @author mle
 */
trait SourceClosing extends JavaSoundPlayer {
  abstract override def close() {
    super.close()
    media.stream.close()
  }
}
