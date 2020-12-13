package sushi

import io.gatling.core.Predef._
import io.gatling.core.controller.inject.open.IncreasingUsersPerSecCompositeStep
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

trait SushiInjects extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("http://185.177.93.224:3000")

  def constantLoad(
                 first: ScenarioBuilder, firstRps: Int,
                 second: ScenarioBuilder, secondRps: Int,
                 third: ScenarioBuilder, thirdRps: Int
               ): SetUp = {
    val loading = 0.8
    setUp(
      first.inject(constantUsersPerSec((loading * firstRps).toInt) during (70 minutes)).protocols(httpProtocol),
      second.inject(constantUsersPerSec((loading * secondRps).toInt) during (70 minutes)).protocols(httpProtocol),
      third.inject(constantUsersPerSec((loading * thirdRps).toInt) during (70 minutes)).protocols(httpProtocol)
    ).maxDuration(FiniteDuration(80, MINUTES))
  }

  def stairLoad(
       first: ScenarioBuilder, firstRps: Int,
       second: ScenarioBuilder, secondRps: Int,
       third: ScenarioBuilder, thirdRps: Int
     ): SetUp = {
    val stepSize = 0.2
    val steps = 6
    val stepDuration = FiniteDuration(4, MINUTES)
    setUp(
      first.inject(stairSteps(steps, stepSize, firstRps, stepDuration)).protocols(httpProtocol),
      second.inject(stairSteps(steps, stepSize, secondRps, stepDuration)).protocols(httpProtocol),
      third.inject(stairSteps(steps, stepSize, thirdRps, stepDuration)).protocols(httpProtocol)
    ).maxDuration(FiniteDuration(24, MINUTES))
  }

  def stairSteps(steps: Int, stepSize: Double, baseRps: Int, stepDur: FiniteDuration) : IncreasingUsersPerSecCompositeStep = {
    incrementUsersPerSec(baseRps * stepSize)
      .times(steps)
      .eachLevelLasting(stepDur)
      .startingFrom(baseRps)
  }
}
