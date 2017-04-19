package recommendations

import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success}

class EngineSpec extends FlatSpec with Matchers {
  ".build" should "return a new instance of the Engine wraped in a Try" in {
    Engine.build("""{}""") shouldBe Success(Engine(Map.empty))
  }

  it should "handle json parse failures" in {
    Engine.build("""{not and valid json]""") shouldBe a[Failure[_]]
  }

  "#request" should "weight the count of attribute matches more than its position" in {
    val json =
      """
        {
          "sku-1": {"att-a": "a1", "att-b": "b1", "att-c": "c1"},
          "sku-2": {"att-a": "a2", "att-b": "b1", "att-c": "c1"},
          "sku-3": {"att-a": "a1", "att-b": "b3", "att-c": "c3"}
        }
      """

    Engine.build(json).get.request("sku-1", 10) shouldBe Seq("sku-2" -> 2.375, "sku-3" -> 1.5)
  }

  it should "weight the attributes position if matches are the same" in {
    val json =
      """
        {
          "sku-1": {"att-a": "a1", "att-b": "b1"},
          "sku-2": {"att-a": "a1", "att-b": "b2"},
          "sku-3": {"att-a": "a2", "att-b": "b1"}
        }
      """

    Engine.build(json).get.request("sku-1", 10) shouldBe Seq("sku-2" -> 1.5, "sku-3" -> 1.25)
  }
}
