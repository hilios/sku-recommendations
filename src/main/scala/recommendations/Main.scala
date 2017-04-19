package recommendations

import scala.annotation.tailrec
import scala.io.{Source, StdIn}
import scala.util.{Failure, Success, Try}

object Main extends App {

  /**
    * Recursively ask for a SKU and fetch the recommendations
    * @param engine the recommendation engine
    */
  @tailrec def ask(engine: Engine): Unit = {
    val sku = StdIn.readLine("Type a SKU: ")
    val recommendations = engine.request(sku, 10)

    if (recommendations.nonEmpty) {
      println(
        s"""
          #+-----------------+---------+
          #| SKU             | Rank    |
          #+-----------------+---------+
          #${recommendations.map {
            case (name, score) => f"| $name%-15s | $score%2.5f |"
          }.mkString("\n")}
          #+-----------------+---------+
        """.stripMargin('#'))
    } else {
      println("Not found")
    }

    ask(engine)
  }

 args match {
   case Array(path) =>
     val engine = for {
       f <- Try { Source.fromFile(path).getLines() }
       e <- Engine.build(f.mkString(""))
     } yield e

     engine match {
       case Success(engine) =>
         println("Press Ctrl+C to exit")
         ask(engine)

       case Failure(error) =>
         println(s"Error loading the file:\n${error.getMessage}")
     }
   case _ =>
     println("Usage: java -jar recommendations.jar path/to/data.json")
 }
}
