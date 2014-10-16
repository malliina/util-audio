package com.mle.audio.javasound

import java.net.URI

import com.mle.audio.javasound.JavaSoundPlayer.DEFAULT_RW_BUFFER_SIZE
import com.mle.audio.meta.StreamSource
import com.mle.storage.StorageSize

import scala.concurrent.duration.Duration

/**
 *
 * @author mle
 */
class UriJavaSoundPlayer(uri: URI,
                         duration: Duration,
                         size: StorageSize,
                         readWriteBufferSize: StorageSize = DEFAULT_RW_BUFFER_SIZE)
  extends BasicJavaSoundPlayer(StreamSource.fromURI(uri, duration, size), readWriteBufferSize)
  with SourceClosing