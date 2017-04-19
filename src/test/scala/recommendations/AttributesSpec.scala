package recommendations

import org.scalatest.{FlatSpec, Matchers}

class AttributesSpec extends FlatSpec with Matchers {
  "#toSeq" should "return all attributes in an ordered sequence" in {
    val a = Attributes(Some("a"), Some("b"), Some("c"), Some("d"), None, None, None, None, None, Some("j"))
    a.toSeq shouldBe Seq(Some("a"), Some("b"), Some("c"), Some("d"), None, None, None, None, None, Some("j"))
  }

  "#rank" should "return the similarity score between two attributes" in {
    val a = Attributes(Some("a1"), Some("b1"), Some("c1"), None, None, None, None, None, None, None)
    val b = Attributes(Some("a2"), Some("b1"), Some("c1"), None, None, None, None, None, None, None)
    val c = Attributes(Some("a1"), Some("b3"), Some("c3"), None, None, None, None, None, None, None)

    a.rank(b) shouldBe 2.375
    a.rank(c) shouldBe 1.5
  }

  it should "weight the position of the attribute and the amount of matches" in {
    val a = Attributes(Some("a1"), Some("b1"), None, None, None, None, None, None, None, None)
    val b = Attributes(Some("a1"), Some("b2"), None, None, None, None, None, None, None, None)
    val c = Attributes(Some("a2"), Some("b1"), None, None, None, None, None, None, None, None)

    a.rank(b) shouldBe 1.5
    a.rank(c) shouldBe 1.25
  }

  it should "weight and return zero properly" in {
    val a = Attributes(Some("a1"), Some("b1"), None, None, None, None, None, None, None, Some("j1"))
    val b = Attributes(Some("a2"), Some("b2"), None, None, None, None, None, None, None, Some("j1"))
    val c = Attributes(Some("a3"), Some("b3"), None, None, None, None, None, None, None, Some("j3"))

    a.rank(b) shouldBe 1.0009765625
    a.rank(c) shouldBe 0.0
  }
}
