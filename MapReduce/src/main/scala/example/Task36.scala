import org.apache.spark.sql.SparkSession

object Task36 {
  def go(): Unit = {
    val spark = SparkSession
        .builder
        .appName("Task36")
        .config("spark.master", "local")
        .getOrCreate()

    val sc = spark.sparkContext

    val rdd = sc.textFile("web-Stanford.txt", 4)
    val result = rdd.filter(line => !line.startsWith("#"))
      .flatMap(
          line => {
              val arr = line.split("\\s+")
              List((arr(0), (0, 1)), (arr(1), (1, 0)))
          }
      ).reduceByKey((acc, next) => (acc._1 + next._1, acc._2 + next._2))

    result.collect().take(10).foreach(println)

    val res2 = result.aggregate((0,0,0))(
        (acc, value) => (
            acc._1 + value._2._1,
            acc._2 + value._2._2,
            acc._3 + 1
        ),
        (acc1, acc2) => (
            acc1._1 + acc2._1,
            acc1._2 + acc2._2,
            acc1._3 + acc2._3
        )
    )
    val avgIn = res2._1 / res2._3.toDouble
    val avgOut = res2._2 / res2._3.toDouble
    println(s"Average In degree: $avgIn, Average Out degree: $avgOut")
    println(s"Sum: IN --> ${res2._1}, OUT --> ${res2._2}, |V| = ${res2._3}")

  }
}