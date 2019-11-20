import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object ReverseGraph {
    def go(): Unit = {
        val spark = SparkSession
            .builder
            .appName("Reverse Graph")
            .config("spark.master", "local")
            .getOrCreate()
        val sc = spark.sparkContext

        val lines = sc.textFile("graph.csv")
        val result = lines.filter(x => x.trim.length > 1)
          .flatMap(
              x => {
                  val y = x.replaceAll(",|\\[|\\]", " ")
                  .trim
                  .split("\\s+")
                  y.drop(1).flatMap( c => List((c, y(0))))
              }
          )
          .reduceByKey(_+" "+_)
        result.collect.map(println)
    }
}