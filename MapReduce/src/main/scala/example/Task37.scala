import org.apache.spark._
import org.apache.spark.sql.SparkSession

object Task37 {
    def go(): Unit = {
        val spark = SparkSession
            .builder
            .appName("Task36")
            .config("spark.master", "local")
            .getOrCreate()

        val sc = spark.sparkContext

        val rdd = sc.textFile("web-Stanford.txt").filter(x => ! x.startsWith("#"))
        val neigh = rdd.flatMap(
            line => {
                val arr = line.split("\\s+")
                List((arr(0), Set(arr(1))), (arr(1), Set(arr(0))))
            }
        ).reduceByKey((acc, n) => {
            acc.union(n)
        })

        val local_cc = neigh.flatMap({
            case (id, xs) => {
                xs.toList.flatMap(x => List((x, (id, xs.filterNot(_ == x)) )))
            }
        })
        local_cc.reduceByKey((v1, v2) => v1)
        // .take(5).foreach(println)
        // .aggregateByKey((0, List[String](), List[String]()))(
        //     (acc, second) => {
        //         val m = acc._2.map(
        //             node => {
        //                 if (second._2 contains node) {
        //                     1
        //                 } else 0
        //             }
        //         ).sum
        //         (acc._1 + m, second._1::acc._2, second._2.toList++acc._3)
        //     },
        //     (acc1, acc2) => {
        //         val m = acc1._3.map(
        //             node => {
        //                 if (acc2._2 contains node) 1
        //                 else 0
        //             }
        //         ).sum

        //         (acc1._1 + m, acc2._2++acc1._2, acc1._3++acc2._3)
        //     }
        // )

        // val result = local_cc.map({
        //     case (node, (m, xs, _)) =>{
        //         val deg = xs.length
        //         val cc = {
        //             if (deg > 1) {
        //                 (2*m/(xs.length * (xs.length-1)).toDouble)
        //             } else {
        //                 0
        //             }
        //         }
        //         (node, cc)
        //     }
        // })
        // result.collect().take(5).foreach(println)

        // val average_cc = local_cc.aggregate((0.0,0))(
        //     (acc, value) => (
        //         acc._1 + value._3,
        //         acc._2 + 1
        //     ),
        //     (acc1, acc2) => (
        //         acc1._1 + acc2._1,
        //         acc1._2 + acc2._2
        //     )
        // )
        // println(s"Average cc = ${average_cc._1/average_cc._2}")
        // val users: RDD[(VertexId, (String, String))] = 
        //     sc.parallelize(Array((3L, ("rxin", "student")), (7L, ("jgonzal", "postdoc")),
        //                          (5L, ("franklin", "prof")), (2L, ("istoica", "prof"))))
        // // Create an RDD for edges
        // val relationships: RDD[Edge[String]] =
        // sc.parallelize(Array(Edge(3L, 7L, "collab"),    Edge(5L, 3L, "advisor"),
        //                     Edge(2L, 5L, "colleague"), Edge(5L, 7L, "pi")))
        // // Define a default user in case there are relationship with missing user
        // val defaultUser = ("John Doe", "Missing")
        // // Build the initial Graph
        // val graph = Graph(users, relationships, defaultUser)
        // // Count all users which are postdocs
        // println(graph.vertices.filter { case (id, (name, pos)) => pos == "postdoc" }.count)
        // // Count all the edges where src > dst
        // println(graph.edges.filter(e => e.srcId > e.dstId).count)

        // val facts: RDD[String] =
        // graph.triplets.map(triplet =>
        //     triplet.srcAttr._1 + " is the " + triplet.attr + " of " + triplet.dstAttr._1)
        // facts.collect.foreach(println(_))
    }
}

