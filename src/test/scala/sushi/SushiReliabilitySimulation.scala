package sushi

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import sushi.SushiMethods._

class SushiReliabilitySimulation extends Simulation with SushiInjects {

  def scenarioOne: ScenarioBuilder = scenario("open main page").exec(mainPage)
  def scenarioTwo: ScenarioBuilder = scenario("get orders list").exec(getOrders)
  def scenarioThree: ScenarioBuilder = scenario("create order").exec(takeOrder)

  constantLoad(scenarioOne, 100, scenarioTwo, 50, scenarioThree, 20)
}
