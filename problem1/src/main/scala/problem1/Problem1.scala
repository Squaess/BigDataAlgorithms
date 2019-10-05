package problem1

import java.io.{FileWriter, PrintWriter}

import scala.io.Source
import stopwords.StopWords.stopWords

object Problem1 extends App {
    val bookLocation:String = getClass.getResource("/book.txt").getPath
    val bufferedSource = Source.fromFile(bookLocation, "UTF-8")
    val bookArray:Array[String] = bufferedSource.mkString.toLowerCase
      .split("\\s+")
      .filterNot(stopWords.contains(_))
    bufferedSource.close

    val rank:Seq[(String, Int)] = (bookArray zip Array.fill(bookArray.length)(1))
      .toList.groupBy(x => x._1)
      .mapValues(x => x.length)
      .toSeq.sortWith((x,y) => x._2 > y._2)

    saveResult("result1.txt", rank)
    saveResult("result2.txt", rank.take(30)).foreach(println)
    saveResult("result3.txt", rank.drop(30))

    def saveResult(fileName:String, list:Seq[(String, Int)]):Seq[(String, Int)] = {
        val rankPaths:String = getClass.getResource("").getPath
        val fileWriter = new FileWriter(rankPaths.concat(fileName))
        val printWriter = new PrintWriter(fileWriter)
        list.foreach(x => printWriter.println(s"${x._2} ${x._1}"))
        printWriter.close()
        list
    }

}