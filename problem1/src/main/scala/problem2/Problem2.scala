package problem2

import scala.io.Source
import scala.io.Codec
import stopwords.StopWords.stopwords_pl

object Problem2 extends App {
    val documents:Array[String] = Source.fromResource("book.txt")(Codec("UTF-8"))
      .getLines
      .mkString
      .toLowerCase
      .replaceAll(",|\\.|!|\\?", " ")
      .split("rozdział")
      .drop(1)
    println(documents.head)
}