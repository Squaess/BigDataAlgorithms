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

        val rdd = sc.textFile("test-graph.txt").filter(x => ! x.startsWith("#"))
        val neigh = rdd.flatMap(
            line => {
                val arr = line.split("\\s+")
                List((arr(0), Set(arr(1))), (arr(1), Set(arr(0))))
            }
        ).reduceByKey((acc, n) => {
            acc.union(n)
        })

        val local_cc = neigh.map({
            case (id, xs) => {
                var nC = 0
                for ( i <- 0 until (xs.size - 1)){
                    for (j <- i+1 until xs.size){
                        // if (neigh.filter(_._1 == xs.toList(j)).first()._2 contains(xs.toList(j))){
                        // val arr = neigh.filter(_._1 == xs.toList(j)).collect()
                        // if (neigh.getOrElse(xs(j), List()).contains(xs(i))){
                        //     nC += 1
                        // }
                    }
                }
                // var cc = 2 * nC /(xs.length * (xs.length - 1) ).toDouble
                // if (cc.isNaN) {
                //     cc = 0
                // }
                // (id, xs.length, cc)
                (id, xs.size)
            }
        }).take(5).foreach(println)

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

