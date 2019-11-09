package problem3
import stopwords.StopWords.stopwords_pl

import scala.io.{Codec, Source}

object Problem3 {

  val documents:Array[Array[String]] = Source.fromResource("book.txt")(Codec("UTF-8"))
    .getLines
    .mkString
    .replaceAll(",|\\.|!|\\?", " ")
    .split("Rozdział (I|V|X|L|C|D|M)*")
    .drop(1)
    .map(_
      .strip
      .toLowerCase
      .split("\\s+")
      .filterNot(stopwords_pl.contains(_))
    )

  val frequency:Array[Map[String, Double]] = documents
    .map( x => (x, x.length))
    .map( x => x._1
      .toList
      .groupBy(x => x)
      .mapValues(y => y.length/x._2.toDouble)
    )

  val inverse:Map[String, Double] = frequency
    .flatMap(_.toList)
    .groupBy(x => x._1)
    .mapValues(x => math.log(documents.length/x.length.toDouble))

  val tf_idf:Array[Map[String, Double]] = frequency.map(
    x => x.map(y => (y._1, y._2*inverse.getOrElse(y._1, 0.0)))
  )

  def main(args: Array[String]): Unit = {
    var text = scala.io.StdIn.readLine("Provide a word, if want to exit write \":exit\": ")
    while(text != ":exit"){
      println(
        tf_idf.map(x => x.getOrElse(text, 0.0))
        .zipWithIndex
        .sortWith((x, y) => x._1 > y._1)
        .map(_._2)
        .toList
      )
      text = scala.io.StdIn.readLine
    }
  }

}
