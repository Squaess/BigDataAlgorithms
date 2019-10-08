package problem1

import java.io.{FileWriter, PrintWriter, File}

import scala.io.Source
import scala.io.Codec
import stopwords.StopWords.stopwords_pl

object Problem1 extends App {
    val bookArray:Array[String] = Source.fromResource("book.txt")(Codec("UTF-8"))
      .getLines
      .mkString
      .toLowerCase
      .replaceAll(",|\\.|!|\\?", " ")
      .split("\\s+")
      .filterNot(stopwords_pl.contains(_))

    val rank:Seq[(String, Int)] = bookArray.map(x => (x, 1))
      .toList.groupBy(x => x._1)
      .mapValues(x => x.length)
      .toSeq.sortWith((x,y) => x._2 > y._2)

    saveResult("result_all.txt", rank)
    saveResult("result_first30.txt", rank.take(30)).foreach(println)
    saveResult("result_except30.txt", rank.drop(30))

    def saveResult(fileName:String, list:Seq[(String, Int)]):Seq[(String, Int)] = {
        val printWriter = new PrintWriter(new File(fileName), "UTF-8")
        list.foreach(x => printWriter.println(s"${x._2} ${x._1}"))
        printWriter.close()
        list
    }

}