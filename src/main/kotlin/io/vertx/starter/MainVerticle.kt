package io.vertx.starter

import io.vertx.core.AbstractVerticle
import io.vertx.core.CompositeFuture
import io.vertx.core.Future
import org.slf4j.LoggerFactory

class MainVerticle : AbstractVerticle() {

  override fun start(startFuture: Future<Void>) {
    val logger = LoggerFactory.getLogger("MainVerticle");

    logger.info("something happened");

    val futureHttpServer8080Startup = Future.future<Void>()
    val futureHttpServer8081Startup = Future.future<Void>()
    val futureRabbitStartup = Future.future<Void>()
    val httpServer8080 = HttpServerVerticuleVerticle(8080)
    val httpServer8081 = HttpServerVerticuleVerticle(8081)
    val rabbit = RabbitVerticle()

    vertx.deployVerticle(httpServer8080) { res ->
      if (res.succeeded()) {
        futureHttpServer8080Startup.complete()
      } else {
        futureHttpServer8080Startup.fail("")
      }
    }

    vertx.deployVerticle(httpServer8081) { res ->
      if (res.succeeded()) {
        futureHttpServer8081Startup.complete()
      } else {
        futureHttpServer8081Startup.fail("")
      }
    }

    vertx.deployVerticle(rabbit) { res ->
      if (res.succeeded()) {
        futureRabbitStartup.complete()
      } else {
        futureRabbitStartup.fail("")
      }
    }

    futureRabbitStartup.setHandler { res ->
      if (res.succeeded()) {
        startFuture.complete()
      } else {
        startFuture.fail("")
      }
    }

    val res = CompositeFuture.all(futureHttpServer8080Startup,
      futureHttpServer8081Startup, futureRabbitStartup)
    res.setHandler {
      res ->
      if (res.succeeded()){
        startFuture.complete()
      }else{
        startFuture.fail("Fuck")
      }
    }


  }
}
