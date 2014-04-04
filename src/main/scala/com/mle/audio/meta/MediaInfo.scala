package com.mle.audio.meta

import java.net.URI
import java.nio.file.{Files, Path}
import scala.concurrent.duration.Duration
import com.mle.storage.{StorageSize, StorageLong}
import java.io.{BufferedInputStream, FileInputStream, InputStream}

/**
 * @author Michael
 */

trait SourceInfo {
  def duration: Duration

  def size: StorageSize
}

case class MediaInfo(uri: URI, duration: Duration, size: StorageSize) extends SourceInfo

object MediaInfo {
  def fromPath(path: Path) = MediaInfo(
    path.toUri,
    MediaTags.audioDuration(path),
    (Files size path).bytes
  )
}

case class StreamInfo(stream: InputStream, duration: Duration, size: StorageSize) extends SourceInfo

object StreamInfo {
  def fromFile(path: Path) = {
    val stream = new BufferedInputStream(new FileInputStream(path.toFile))
    StreamInfo(stream, MediaTags.audioDuration(path), (Files size path).bytes)
  }

  def fromURI(uri: URI, duration: Duration, size: StorageSize) =
    StreamInfo(uri.toURL.openStream(), duration, size)
}


