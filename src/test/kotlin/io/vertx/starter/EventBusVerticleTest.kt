package io.vertx.starter

import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
class EventBusVerticleTest{
  @BeforeEach
  fun deploy_verticle(vertx: Vertx, testContext: VertxTestContext) {
    vertx.deployVerticle(EventBusVerticle(), testContext.succeeding<String> { _ -> testContext.completeNow() })
  }


  @Test
  @Throws(Exception::class)
  @Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
  @DisplayName("Consume Message<Int> correctly")
  fun `consume message correctly`(vertx: Vertx, testContext: VertxTestContext) {
    vertx.eventBus().send<JsonObject>("test.nsa", 5)
    {
      testContext.verify {
        Assertions.assertTrue(it.succeeded())
        Assertions.assertTrue(it.result().body().getString("result") == "Nico")
        testContext.completeNow()
      }
    }
  }



}
