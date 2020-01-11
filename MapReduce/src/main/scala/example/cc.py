import findspark
findspark.init("/opt/apache-spark")

from pyspark import SparkContext
sc = SparkContext("local")

FILE_NAME = "test-graph.txt"
FILE_NAME = "web-Stanford.txt"

file_content = sc.textFile(FILE_NAME) \
                .filter(lambda x: not x.startswith("#")) \
                .map(lambda x: x.split())

adj_rdd = file_content.flatMap(lambda x: [(x[0], {x[1]}), (x[1], {x[0]})]) \
        .reduceByKey(set.union)

adj = adj_rdd.collectAsMap()

def calc_cc(node, value):
  deg = len(adj[node])
  if deg > 1:
    return value/(deg*(deg-1))
  else:
    return 0

cc = adj_rdd.flatMap(lambda x: [(x[0], v) for v in adj[x[0]]]) \
  .map(
      lambda x: (x[0], len(adj[x[0]].intersection(adj[x[1]])))
  ).reduceByKey(lambda v1, v2: v1 + v2) \
  .map(lambda x: (x[0], calc_cc(*x))) \

result = cc.collect()
result[:5]
