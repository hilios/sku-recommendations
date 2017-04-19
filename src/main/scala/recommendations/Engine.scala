package recommendations

import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.util.Try

case class Engine(data: Map[String, Attributes]) {

  /**
    * Returns the recommendations from the given SKUs
    * @param name the sku
    * @param n the amount of recommendations to return
    * @return the ranked list
    */
  def request(name: String, n: Int): Seq[(String, Double)] = {
    data.get(name) match {
      case Some(attrs) =>
        data.filterNot(_._1 == name).map {
          case (sku, a) => (sku, attrs.rank(a))
        }.toSeq.sortBy(_._2).reverse.take(n)
      case None =>
        Seq.empty
    }
  }
}

object Engine {

  /**
    * Parses a json string and returns an recommendation engine
    */
  def build(jsonStr: String) = {
    implicit val formats = org.json4s.DefaultFormats
    Try {
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
      }.extract[Map[String, Attributes]]
      Engine(data)
    }
  }
}
