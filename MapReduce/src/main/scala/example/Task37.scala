import org.apache.spark._
import org.apache.spark.sql.SparkSession
import spire.std.`package`.int

object Task37 {
    def go(): Unit = {

        // Initialize spark
        val spark = SparkSession
            .builder
            .appName("Task36")
            .config("spark.master", "local")
            .getOrCreate()

        val sc = spark.sparkContext

        val rdd = sc.textFile("web-Stanford.txt").filter(x => ! x.startsWith("#")).map(line => line.split("\\s+"))
        // val rdd = sc.textFile("test-graph.txt")
        //     .filter(x => ! x.startsWith("#"))
        //     .map(line => line.split("\\s+"))

        val adj_rdd = rdd.flatMap(
            line => {
                List((line(0), line(1)), (line(1), line(0)))
            }
        ).groupByKey().map(x => (x._1, x._2.toSet))

        val adj = adj_rdd.collectAsMap()

        adj.take(5).foreach(println)

        println("Start calculating local cc ...")
        val result = adj_rdd.flatMap({
            case (key, v_set) => {
                for ( a <- v_set) yield (key, a)
            }
        }).map({
            case (v1, v2) => (v1, adj(v1).intersect(adj(v2)).size)
        }).reduceByKey({case (v1, v2) => v1+v2})
        .map({
            case (key, value) => { 
                val deg = adj(key).size
                val cc = if (deg > 1) {
                    value/(deg * (deg-1)).toDouble
                } else { 0.0 }
                (key, cc, deg)
            }
        })

        println("Start calculating average cc ...")
        val average_cc = result.aggregate((0.0, 0))(
            (acc, value) => (
                acc._1 + value._2,
                acc._2 + 1
            ),
            (acc1, acc2) => (
                acc1._1 + acc2._1,
                acc1._2 + acc2._2
            )
        )
        // .foldLeft((0.0, 0))((acc, value) => (acc._1 + value._2, acc._2 + 1))
        println(s"Average clustering coefficient: ${average_cc._1/average_cc._2}")
    }
}

