package recommendations

import org.json4s._
import org.json4s.jackson.JsonMethods._

case class Attrs(a: Option[String], b: Option[String], c: Option[String], d: Option[String],
                 e: Option[String], f: Option[String], g: Option[String], h: Option[String],
                 i: Option[String], j: Option[String]) {

  def toSeq = Seq(a, b, c, d, e, f, g, h, i, j)

  def rank(attrs: Attrs): Double = {
    this.toSeq.zip(attrs.toSeq).zipWithIndex
      .filter({ case ((x, y), _) => x.nonEmpty && y.nonEmpty && x == y })
      .map(_._2)
      .foldLeft(0.0)((rank, index) => rank + 1.0 + Math.pow(0.5, index.toDouble + 1))
  }
}

case class Engine(data: Map[String, Attrs]) {
  def get(name: String, n: Int): Map[String, Double] = {
    data.get(name) match {
      case Some(attrs) =>
        data.filterNot(_._1 == name).map {
          case (sku, a) => (sku, attrs.rank(a))
        }.toSeq.sortBy(_._2).reverse.take(n).toMap
      case None =>
        Map.empty
    }
  }
}

object Engine {
  def apply(jsonStr: String) = {
    implicit val formats = org.json4s.DefaultFormats
    val data = parse(jsonStr).transformField {
      case ("att-a", x) => ("a", x)
      case ("att-b", x) => ("b", x)
      case ("att-c", x) => ("c", x)
      case ("att-d", x) => ("d", x)
      case ("att-e", x) => ("e", x)
      case ("att-f", x) => ("f", x)
      case ("att-g", x) => ("g", x)
      case ("att-h", x) => ("h", x)
      case ("att-i", x) => ("i", x)
      case ("att-j", x) => ("j", x)
    }.extractOpt[Map[String, Attrs]]
    new Engine(data.get)
  }
}
