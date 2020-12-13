package sushi

import sushi.SushiMethods._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class SushiStairSimulation extends Simulation with SushiInjects {

  def scenarioOne: ScenarioBuilder = scenario("open main page").exec(mainPage)
  def scenarioTwo: ScenarioBuilder = scenario("get orders list").exec(getOrders)
  def scenarioThree: ScenarioBuilder = scenario("create order").exec(takeOrder)

  stairLoad(scenarioOne, 100, scenarioTwo, 50, scenarioThree, 20)
}
