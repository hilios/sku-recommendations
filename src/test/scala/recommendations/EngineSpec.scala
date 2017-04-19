package recommendations

import org.scalatest.{FlatSpec, Matchers}

class EngineSpec extends FlatSpec with Matchers {
  "Engine" should "weight the count of attribute matches more than its position" in {
    val json =
      """
        {
          "sku-1": {"att-a": "a1", "att-b": "b1", "att-c": "c1"},
          "sku-2": {"att-a": "a2", "att-b": "b1", "att-c": "c1"},
          "sku-3": {"att-a": "a1", "att-b": "b3", "att-c": "c3"}
        }
      """

    Engine(json).get("sku-1", 10) shouldBe Map("sku-2" -> 2.375, "sku-3" -> 1.5)
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

    Engine(json).get("sku-1", 10) shouldBe Map("sku-2" -> 1.5, "sku-3" -> 1.25)
  }
}
