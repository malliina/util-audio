package com.malliina.audio.javasound

import java.net.URI

import com.malliina.audio.javasound.JavaSoundPlayer.DEFAULT_RW_BUFFER_SIZE
import com.malliina.audio.meta.StreamSource
import com.malliina.storage.StorageSize

import scala.concurrent.duration.Duration

class UriJavaSoundPlayer(uri: URI,
                         duration: Duration,
                         size: StorageSize,
                         readWriteBufferSize: StorageSize = DEFAULT_RW_BUFFER_SIZE)
  extends BasicJavaSoundPlayer(StreamSource.fromURI(uri, duration, size), readWriteBufferSize)
    with SourceClosing
