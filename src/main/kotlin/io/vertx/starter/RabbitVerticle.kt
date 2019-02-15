package io.vertx.starter

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.rabbitmq.RabbitMQClient
import io.vertx.rabbitmq.RabbitMQOptions


class RabbitVerticle : AbstractVerticle() {

  override fun start(startFuture: Future<Void>) {

    val config = RabbitMQOptions()
// Each parameter is optional
// The default parameter with be used if the parameter is not set
    config.user = "rabbitmq"
    config.password = "rabbitmq"
    config.host = "myrabbit"
    config.port = 5672
    config.virtualHost = "/"
    config.connectionTimeout = 6000 // in milliseconds
    config.requestedHeartbeat = 60 // in seconds
    config.handshakeTimeout = 6000 // in milliseconds
    config.requestedChannelMax = 5
    config.networkRecoveryInterval = 500 // in milliseconds
    config.isAutomaticRecoveryEnabled = true


    val client = RabbitMQClient.create(vertx, config)
    client.start { ar ->
      if (ar.succeeded()) {
        if (client.isConnected) {
          startFuture.complete()
          println("il est connecté!")
        } else {
          println("marche pas rabbit")
          startFuture.fail(Throwable("error dans le rabbit"))
        }
      } else {
        startFuture.fail(Throwable("error dans le rabbit"))
      }
    }

//    val config = RabbitMQOptions()
// full amqp uri
//    config.uri = "amqp://rabbitmq:rabbitmq@myrabbit/"
//    val client = RabbitMQClient.create(vertx, config)

  }
}
