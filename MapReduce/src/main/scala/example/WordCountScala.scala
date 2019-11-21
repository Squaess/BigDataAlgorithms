import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import scala.io.Source
import scala.io.Codec

class StopWords {
  val stopwords_pl = getStopWords("stopwords_pl.txt")

  private def getStopWords(fileName:String):Array[String] ={
    Source.fromFile("/home/silentnauscopy/Documents/University/BigDataAlgorithms/MapReduce/src/main/resources/stopwords/".concat(fileName))(Codec("UTF-8"))
      .mkString
      .split("\\s+")
  }
}

object WordCountScala {
    def go(): Unit = {
        val spark = SparkSession
            .builder
            .appName("Word Count")
            .config("spark.master", "local")
            .getOrCreate()
        val sc = spark.sparkContext
        val rdd = sc.textFile("/home/silentnauscopy/Documents/University/BigDataAlgorithms/MapReduce/src/main/scala/example/book.txt", 4)
        val stopwords_pl = new StopWords().stopwords_pl

        val result = rdd.flatMap(
            line => line
              .toLowerCase
              .replaceAll(",|\\.|!|\\?", " ")
              .trim
              .split("\\s+")
              .filterNot(x => stopwords_pl.contains(x) || x == "")
        ).countByValue()
        .toList.sortWith((x,y) => x._2 > y._2)

        result.take(10).foreach(println)
    }
} 