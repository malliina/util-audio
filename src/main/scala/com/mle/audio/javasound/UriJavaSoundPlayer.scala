package com.mle.audio.javasound

import com.mle.storage.StorageSize
import scala.concurrent.duration.Duration
import java.net.URI
import com.mle.audio.meta.{StreamSource, OneShotStream}

/**
 *
 * @author mle
 */
class UriJavaSoundPlayer(uri: URI, duration: Duration, size: StorageSize)
  extends BasicJavaSoundPlayer(StreamSource.fromURI(uri, duration, size))
  with SourceClosing