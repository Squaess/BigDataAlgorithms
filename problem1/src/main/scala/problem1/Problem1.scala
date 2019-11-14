package problem1

import java.io.{PrintWriter, File => JFile}
import better.files._
import better.files.Dsl._
import scala.io.Source
import scala.io.Codec
import stopwords.StopWords.stopwords_pl

trait SaveForWordCloud {
    def saveResult[T:Numeric](fileName:String, list:Seq[(String, T)]):Seq[(String, T)] = {
        val f:File = (cwd/fileName).createIfNotExists(asDirectory = false, createParents = true).clear()
        val printWriter = new PrintWriter(new JFile(f.path.toString), "UTF-8")
        import Numeric.Implicits._
        list.foreach(x => printWriter.println(s"${x._2.toInt} ${x._1}"))
        printWriter.close()
        list
    }
}

object Problem1 extends App with SaveForWordCloud {
    val bookArray:Array[String] = Source.fromResource("book.txt")(Codec("UTF-8"))
      .getLines
      .mkString
      .toLowerCase
      .replaceAll(",|\\.|!|\\?", " ")
      .split("\\s+")
      .filterNot(stopwords_pl.contains(_))

    val rank:Seq[(String, Int)] = bookArray.map(x => (x, 1))
      .toList.groupBy(x => x._1)
      .view.mapValues(x => x.length).toMap
      .toSeq.sortWith((x,y) => x._2 > y._2)

    saveResult("result_all.txt", rank)
    saveResult("result_first30.txt", rank.take(30)).foreach(println)
    saveResult("result_except30.txt", rank.drop(30))


}