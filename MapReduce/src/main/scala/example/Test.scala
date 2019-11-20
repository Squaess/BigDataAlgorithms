
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
        val sc = spark.sparkContext

        val r = new scala.util.Random(100)

        // val number = sc.parallelize((1 to 1000).toList ::: (1 to 500).toList)
        val number = sc.parallelize((for (i <- 1 to 1000) yield r.nextInt(50)))

        val result = number.aggregate((0, 0, scala.Int.MaxValue, scala.Int.MinValue, List[Int]()))(
            (acc, value) => (
                acc._1 + value,
                acc._2 + 1,
                if (value < acc._3) value else acc._3,
                if (value > acc._4) value else acc._4,
                if (acc._5.contains(value)) acc._5 else value::acc._5
            ),
            (acc1, acc2) => (
                acc1._1 + acc2._1,
                acc1._2 + acc2._2,
                if (acc1._3 < acc2._3) acc1._3 else acc2._3,
                if (acc1._4 < acc2._4) acc2._4 else acc1._4,
                acc1._5:::(acc2._5.filterNot( x => acc1._5.contains(x)))
            )
        )
        val avg = result._1 / result._2.toDouble
        val min = result._3
        val max = result._4
        val distrinc = result._5
        println(s"Average: $avg")
        println(s"Min value: $min")
        println(s"Max value: $max")
        println(s"Distinct items: $distrinc")
        println(s"Number of Distinct items: ${distrinc.length}")

        // println(s"Distinct: ${noRep.collect}")

        // val bigMap = number.countByValue()
        // println(s"Distinct values: ${bigMap.keys.map(println)}")
        // println(s"Number of distinct values: ${bigMap.size}")
        // println(s"Largest:")
    }
}