package io.vertx.starter

import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeUnit


@ExtendWith(VertxExtension::class)
class TestMainVerticle {

  @BeforeEach
  fun deploy_verticle(vertx: Vertx, testContext: VertxTestContext) {
    vertx.deployVerticle(MainVerticle(), testContext.succeeding<String> { _ -> testContext.completeNow() })
  }

  @Test
  @DisplayName("Should start a Web Server on port 8080")
  @Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun start_http_server(vertx: Vertx, testContext: VertxTestContext) {
    vertx.createHttpClient().getNow(8080, "localhost", "/") { response ->
      assertTrue(response.statusCode() == 200)
      testContext.verify {
        response.handler { body ->
          assertTrue(body.toString() == "Hello from Vert.x!")
          println("assertTrue seems ok!")
          testContext.completeNow()
        }
      }

    }
  }

  @Test
  @DisplayName("should test response")
  @Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
  fun http_server_check_response(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    client.get(808, "localhost", "/")
      .`as`(BodyCodec.string())
      .send(testContext.succeeding { response ->
        testContext.verify {
          assertTrue(response.body() == "Hello from Vert.x!")
          testContext.completeNow()
        }
      })
  }


  @Test
  @DisplayName("should publish message to event bus")
  @Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
  fun http_server_check_eventbus(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    client.get(8080, "localhost", "/")
      .`as`(BodyCodec.string())
      .send(testContext.succeeding { response ->
        println(response)
      })
    vertx.eventBus().consumer<String>("test.nsa") { message ->
      testContext.verify {
        assertTrue(message.body() == "Bisounours for the win")
        testContext.completeNow()
      }
    }
  }
}
