package com.malliina.audio

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

/**
 *
 * @author mle
 */
object ExecutionContexts {
  implicit val defaultPlaybackContext: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
}
