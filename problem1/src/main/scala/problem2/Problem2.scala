package problem2

import scala.io.Source
import scala.io.Codec
import stopwords.StopWords.stopwords_pl

object Problem2 extends App {
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
        .mapValues(y => y.length/x._2.toDouble),
    )

    val inverse:Map[String, Double] = frequency
      .flatMap(_.toList)
      .groupBy(x => x._1)
      .mapValues(x => x.length/documents.length.toDouble)

    frequency.map(
        x => x.map(
            y => (y._1, y._2 * inverse.getOrElse(y._1, 0))
        )
    ).head.take(10).foreach(println)
}