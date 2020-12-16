package sushi

import io.gatling.core.Predef._
import io.gatling.core.controller.inject.open.IncreasingUsersPerSecCompositeStep
import io.gatling.http.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}

import scala.concurrent.duration._

object SushiMethods {

  val mainPage: ChainBuilder = pace(1 seconds)
    .exec(http("mainPage")
      .get("/"))

  val getOrders: ChainBuilder = pace(1 seconds)
    .exec(http("get orders")
      .get("/api/orders"))

  val takeOrder: ChainBuilder = pace(1 seconds)
    .exec(http("take order")
      .post("/api/orders")
      .body(RawFileBody("orderDetails.json")).asJson)
}
