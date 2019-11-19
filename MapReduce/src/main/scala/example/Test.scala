
import scala.math.random
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._


object Test {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession
            .builder
            .appName("Spark Pi")
            .config("spark.master", "local")
            .getOrCreate()
        // val spark = new SparkContext(conf)
        // val slices = if (args.length > 0) args(0).toInt else 2
        // val n = math.min(100000L * slices, Int.MaxValue).toInt
        // val count = spark.sparkContext.parallelize(1 until n, slices).map { i =>
        //     val x = random * 2 - 1
        //     val y = random * 2 - 1
        //     if (x*x + y*y <= 1) 1 else 0
        // }.reduce(_ + _)
        // println(s"Pi is roughly ${4.0 * count / (n - 1)}")
        // spark.stop()

        // val conf = new SparkConf().setMaster("local").setAppName("My App")
        // val sc = new SparkContext(conf)
        val sc = spark.sparkContext

        val number = sc.parallelize((1 to 1000))
        val noRep = number.distinct

        val result = number.aggregate((0, 0, scala.Int.MaxValue))(
            (acc, value) => (acc._1 + value, acc._2 + 1, if (value < acc._3) value else acc._3),
            (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2, if (acc1._3 < acc2._3) acc1._3 else acc2._3))
        val avg = result._1 / result._2.toDouble
        val min = result._3
        println(s"Average: $avg")
        println(s"Min value: $min")

    }
}