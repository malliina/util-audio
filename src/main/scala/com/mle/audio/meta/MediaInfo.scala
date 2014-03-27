package com.mle.audio.meta

import java.net.URI
import java.nio.file.{Files, Path}
import scala.concurrent.duration.Duration
import com.mle.storage.{StorageSize, StorageLong}

/**
 * @author Michael
 */

case class MediaInfo(uri: URI, duration: Duration, size: StorageSize)

object MediaInfo {
  def fromPath(path: Path) = MediaInfo(
    path.toUri,
    MediaTags.audioDuration(path),
    (Files size path).bytes
  )
}
