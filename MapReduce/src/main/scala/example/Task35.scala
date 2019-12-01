import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.rdd.RDDFunctions._

import scala.math.random
import scala.io.Source
import scala.io.Codec

class StopWords {
  val stopwords_pl = getStopWords("stopwords_pl.txt")

  private def getStopWords(fileName:String):Array[String] ={
    Source.fromFile("/home/silentnauscopy/Documents/University/BigDataAlgorithms/MapReduce/src/main/resources/stopwords/stopwords_pl.txt")(Codec("UTF-8"))
      .mkString
      .split("\\s+")
  }
}

object Task35 {
    def go(): Map[String, Set[String]] = {
       val spark = SparkSession
          .builder()
          .appName("Important words")
          .config("spark.master", "local")
          .getOrCreate()
        val sc = spark.sparkContext

        val rdd = sc.textFile("book.txt", 4)
        val stopWords = new StopWords().stopwords_pl
        val noStopWords = rdd.flatMap(
            line => line
              .toLowerCase
              .replaceAll(",|\\.|!|\\?", " ")
              .trim
              .split("\\s+")
              .filterNot(x => stopWords.contains(x) || x == "")
        )
        val result = noStopWords
          .sliding(2)
          .flatMap({case Array(x,y) => List((x,Set(y)), (y, Set(x)))})
          .reduceByKey((acc, n) => acc.union(n))
          .sortBy({case (k, xs) => xs.size}, ascending = false)
          .collect()
        //   .collect({case (k, xs) => (k, xs.size)})
        result.take(5).foreach({ case (k, xs) => println(k, xs.size)})
        val c = result.toMap

        val r = new scala.util.Random(100)
        var firstWord = result(0)._1
        var s = firstWord
        for (_ <- 0 to 5) {
            val choices = c.getOrElse(firstWord, result(0)._2).toList
            val nextId = r.nextInt(choices.size)
            val nextWord = choices(nextId)
            println(s"$firstWord --> $nextWord, $nextId")
            firstWord = nextWord
            s += " "+nextWord
        }
        println(s)

        return c
    }
}