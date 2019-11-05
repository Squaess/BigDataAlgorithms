package problem2

import scala.io.Source
import scala.io.Codec
import stopwords.StopWords.stopwords_pl
import problem1.SaveForWordCloud

object Problem2 extends App with SaveForWordCloud {
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

    val tf_idf:Array[Seq[(String, Double)]] = frequency.map(
        x => x.map(y => (y._1, y._2*inverse.getOrElse(y._1, 0.0)))
          .toSeq.sortWith((x, y) => x._2 > y._2)
    )
    for (i <- tf_idf.indices) {
        saveResult(
            s"problem2/c$i.txt",
            tf_idf(i).take(100).map( x => (x._1, x._2*10e4))
        )
    }

    saveResult(
        "problem2/all.txt",
        tf_idf.flatMap(_.toList)
          .sortWith((x, y) => x._2 > y._2)
          .map(x => (x._1, x._2 * 10e4))
          .take(100)
    )
}