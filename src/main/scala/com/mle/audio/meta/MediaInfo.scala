package com.mle.audio.meta

import java.net.URI
import java.nio.file.{Files, Path}
import scala.concurrent.duration.Duration

/**
 * @author Michael
 */

case class MediaInfo(uri: URI, duration: Duration, bytes: Long)

object MediaInfo {
  def fromPath(path: Path) = MediaInfo(
    path.toUri,
    MediaTags.audioDuration(path),
    Files size path
  )
}
