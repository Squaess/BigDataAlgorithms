package example

import scala.math.random
import org.apache.spark.sql.SparkSession


object Test {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder
            .appName("Spark Pi")
            .config("spark.master", "local")
            .getOrCreate()
        // val spark = new SparkContext(conf)
        val slices = if (args.length > 0) args(0).toInt else 2
        val n = math.min(100000L * slices, Int.MaxValue).toInt
        val count = spark.sparkContext.parallelize(1 until n, slices).map { i =>
            val x = random * 2 - 1
            val y = random * 2 - 1
            if (x*x + y*y <= 1) 1 else 0
        }.reduce(_ + _)
        println(s"Pi is roughly ${4.0 * count / (n - 1)}")
        spark.stop()
    }
}