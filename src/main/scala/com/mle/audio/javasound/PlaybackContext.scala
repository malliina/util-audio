package com.mle.audio.javasound

import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors

/**
 *
 * @author mle
 */
object PlaybackContext {
  implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(3))
}
