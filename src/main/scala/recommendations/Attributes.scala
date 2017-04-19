package recommendations

/**
  * Stores a set of optional attributes for one SKU.
  */
case class Attributes(a: Option[String], b: Option[String], c: Option[String], d: Option[String],
                      e: Option[String], f: Option[String], g: Option[String], h: Option[String],
                      i: Option[String], j: Option[String]) {

  /**
    * Returns the attributes as an sequence
    * @return
    */
  def toSeq = Seq(a, b, c, d, e, f, g, h, i, j)

  /**
    * Calculates the similarity between this and any other Attributes.
    * @param attrs the attributes class to compare with
    * @return a similarity score
    */
  def rank(attrs: Attributes): Double = {
    this.toSeq.zip(attrs.toSeq).zipWithIndex
      .filter({ case ((x, y), _) => x.nonEmpty && y.nonEmpty && x == y })
      .map(_._2)
      .foldLeft(0.0)((rank, index) => rank + 1.0 + Math.pow(0.5, index.toDouble + 1))
  }
}
