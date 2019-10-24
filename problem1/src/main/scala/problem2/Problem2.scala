package problem2

import scala.io.Source
import scala.io.Codec
import stopwords.StopWords.stopwords_pl

object Problem2 extends App {
    val documents:Array[Array[String]] = Source.fromResource("book.txt")(Codec("UTF-8"))
      .getLines
      .mkString
      .toLowerCase
      .replaceAll(",|\\.|!|\\?", " ")
      .split("rozdział")
      .drop(1)
      .map(_
        .strip
        .split("\\s+")
        .filterNot(stopwords_pl.contains(_))
      )

    documents.head.take(10).foreach(println)
}