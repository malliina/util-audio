package com.mle.audio.javasound

import java.io.InputStream
import java.net.URI
import java.nio.file.Path

import com.mle.audio.javasound.JavaSoundPlayer.DEFAULT_RW_BUFFER_SIZE
import com.mle.audio.meta.StreamSource
import com.mle.storage.StorageSize

import scala.concurrent.duration.Duration

/**
 *
 * @author mle
 */
class BasicJavaSoundPlayer(media: StreamSource, readWriteBufferSize: StorageSize = DEFAULT_RW_BUFFER_SIZE)
  extends JavaSoundPlayer(media.toOneShot, readWriteBufferSize) with SourceClosing {

  override def resetStream(oldStream: InputStream): InputStream = {
    oldStream.close()
    media.openStream
  }

  override def seekProblem: Option[String] = None
}

object BasicJavaSoundPlayer {
  def fromFile(file: Path) =
    new BasicJavaSoundPlayer(StreamSource.fromFile(file))

  def fromUri(uri: URI, duration: Duration, size: StorageSize) =
    new BasicJavaSoundPlayer(StreamSource.fromURI(uri, duration, size))
}
