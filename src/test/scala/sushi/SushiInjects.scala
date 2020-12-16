package sushi

import io.gatling.core.Predef._
import io.gatling.core.controller.inject.open.IncreasingUsersPerSecCompositeStep
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

trait SushiInjects extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("http://185.177.93.224:3000").shareConnections

  def constantLoad(
                 first: ScenarioBuilder, firstRps: Int,
                 second: ScenarioBuilder, secondRps: Int,
                 third: ScenarioBuilder, thirdRps: Int
               ): SetUp = {
    val loading = 0.8
    val loadDuration = FiniteDuration(70, MINUTES)
    setUp(
      first.inject(constantUsersPerSec((loading * firstRps).toInt) during loadDuration)
        .throttle(jumpToRps((loading * firstRps).toInt), holdFor(loadDuration))
        .protocols(httpProtocol),
      second.inject(constantUsersPerSec((loading * secondRps).toInt) during loadDuration)
        .throttle(jumpToRps((loading * secondRps).toInt),holdFor(loadDuration))
        .protocols(httpProtocol),
      third.inject(constantUsersPerSec((loading * thirdRps).toInt) during loadDuration)
        .throttle(jumpToRps((loading * thirdRps).toInt),holdFor(loadDuration))
        .protocols(httpProtocol)
    ).maxDuration(loadDuration)
  }

  def stairLoad(
       first: ScenarioBuilder, firstRps: Int,
       second: ScenarioBuilder, secondRps: Int,
       third: ScenarioBuilder, thirdRps: Int
     ): SetUp = {
    val stepDuration = FiniteDuration(4, MINUTES)
    setUp(
      stairThrottle(first.inject(constantUsersPerSec(firstRps * 2) during (stepDuration * 6)), firstRps, stepDuration)
        .protocols(httpProtocol),
      stairThrottle(second.inject(constantUsersPerSec(secondRps * 2) during (stepDuration * 6)), secondRps, stepDuration)
        .protocols(httpProtocol),
      stairThrottle(third.inject(constantUsersPerSec(thirdRps * 2) during (stepDuration * 6)), thirdRps, stepDuration)
        .protocols(httpProtocol)
    ).maxDuration(stepDuration * 6)
  }

  def stairThrottle(pb: PopulationBuilder, baseRps: Int, stepDuration: FiniteDuration) : PopulationBuilder = {
    pb.throttle(
      jumpToRps(baseRps),
      holdFor(stepDuration),
      jumpToRps((baseRps * 1.2).ceil.toInt),
      holdFor(stepDuration),
      jumpToRps((baseRps * 1.4).ceil.toInt),
      holdFor(stepDuration),
      jumpToRps((baseRps * 1.6).ceil.toInt),
      holdFor(stepDuration),
      jumpToRps((baseRps * 1.8).ceil.toInt),
      holdFor(stepDuration),
      jumpToRps(baseRps * 2),
      holdFor(stepDuration),
    )
  }
}
