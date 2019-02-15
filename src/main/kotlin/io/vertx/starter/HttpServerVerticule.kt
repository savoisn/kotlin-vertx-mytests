package io.vertx.starter

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future

class HttpServerVerticuleVerticle(val port: Int) : AbstractVerticle() {

  override fun start(startFuture: Future<Void>) {

    vertx
      .createHttpServer()
      .requestHandler { req ->
        vertx.eventBus().publish(
          "test.nsa",
          "Bisounours for the win")
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x!")
      }
      .listen(port) { http ->
        if (http.succeeded()) {
          startFuture.complete()
          println("HTTP server started on port "+port)
        } else {
          startFuture.fail(http.cause())
        }
      }
  }
}
