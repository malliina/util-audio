package com.mle.audio.javasound

import java.nio.file.Path
import com.mle.audio.meta.StreamInfo

/**
 *
 * @author mle
 */
class FileJavaSoundPlayer(file: Path)
  extends JavaSoundPlayer(StreamInfo.fromFile(file))
  with SourceClosing


