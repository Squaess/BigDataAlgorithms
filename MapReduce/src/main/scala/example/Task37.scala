import org.apache.spark._
import org.apache.spark.sql.SparkSession

object Task37 {
    def go(): Unit = {
        // Initialize spark
        val spark = SparkSession
            .builder
            .appName("Task36")
            .config("spark.master", "local")
            .getOrCreate()
        val sc = spark.sparkContext
        // Read file, filter commented lines and split
        val rdd = sc.textFile("web-Stanford.txt")
            .filter(x => ! x.startsWith("#"))
            .map(line => line.split("\\s+"))
        // create adjacency rdd, emmit all possible pairs,
        // then groupByKey and convert values to Set
        val adj_rdd = rdd.flatMap(
                line => {
                    List((line(0), line(1)), (line(1), line(0)))
                }
            )
            .groupByKey()
            .map(x => (x._1, x._2.toSet))
        // collect adjacency map locally
        // this will be used in futher comutations
        val adj = adj_rdd.collectAsMap()
        // for all vertices pairs (v1,v2) compute
        // how many common neighbours they have
        // then compute the clustering coefficient
        val result = adj_rdd.flatMap({
                case (key, v_set) => for ( a <- v_set) yield (key, a)
            })
            .map({
                case (v1, v2) => (v1, adj(v1).intersect(adj(v2)).size)
            })
            .reduceByKey({case (v1, v2) => v1+v2})
            .map({
                case (key, value) => { 
                    val deg = adj(key).size
                    val cc = if (deg > 1) {
                        // we don't multiply by 2 because be already counted
                        // the edges twice
                        value/(deg * (deg-1)).toDouble
                    } else { 0.0 }
                    (key, cc, deg)
                }
            })

        val avg_cc = result.map(x => (x._2, 1))
            .reduce({
                case ((value1, count1), (value2, count2)) => {
                    val count = count1 + count2
                    (value1 * count1/count + value2* count2/count, count)
                }
            })._1
        println(s"Average cc: $avg_cc")
    }
}

