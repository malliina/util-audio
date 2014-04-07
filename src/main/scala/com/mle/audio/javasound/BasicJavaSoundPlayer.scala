package com.mle.audio.javasound

import com.mle.audio.meta.StreamSource
import java.io.InputStream
import java.nio.file.Path
import java.net.URI
import com.mle.storage.StorageSize
import scala.concurrent.duration.Duration

/**
 *
 * @author mle
 */
class BasicJavaSoundPlayer(media: StreamSource) extends JavaSoundPlayer(media.toOneShot) with SourceClosing {
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
