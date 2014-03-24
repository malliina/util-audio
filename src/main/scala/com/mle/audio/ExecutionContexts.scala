package com.mle.audio

import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors

/**
 *
 * @author mle
 */
object ExecutionContexts {
  implicit val defaultPlaybackContext: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
}
