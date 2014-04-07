package com.mle.audio.javasound

import java.nio.file.Path
import com.mle.audio.meta.{StreamSource, OneShotStream}

/**
 * Use for audio files. Since this constructor opens an InputStream, trait SourceClosing is mixed in so that when this
 * player is closed, so is the InputStream.
 *
 * @param file file to play
 */
class FileJavaSoundPlayer(file: Path)
  extends BasicJavaSoundPlayer(StreamSource.fromFile(file))


