import org.apache.spark.sql.SparkSession
import scala.io.Source
import scala.io.Codec
import example.StopWords

object Task35 {
    def go(): Unit = {
       val spark = SparkSession
          .builder()
          .appName("Important words")
          .config("spark.master", "local")
          .getOrCreate()
        val sc = spark.sparkContext

        val rdd = sc.textFile("book.txt", 4)
        val stopWords = new StopWords().stopwords_pl
        stopWords.foreach(println)

        spark.stop()
    }
}