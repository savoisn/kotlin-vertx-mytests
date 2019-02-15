package io.vertx.starter

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory


class EventBusVerticle : AbstractVerticle() {

  private val logger = LoggerFactory.getLogger(EventBusVerticle::class.java)

  override fun start(startFuture: Future<Void>){
    vertx.eventBus().consumer("test.nsa")
    { message: Message<Int> ->
      val input = message.body()
      logger.info { "Received: $input" }
      message.reply(JsonObject().put("result", "Nico"))
    }
    startFuture.complete()


  }

}
