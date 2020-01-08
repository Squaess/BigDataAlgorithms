import itertools

aaa = 'web-Stanford.txt.gz'

import gzip
with gzip.open(working_dir + aaa, 'rb') as f:
    file_content = f.read()

# edges = [('1','2'),('2','3'),('3','1'),('4','1'),('4','2')]
# edges = [('1','3'),('1','0'),('0','4'),('4','5'),('5','0'),('0','6'),('6','8'),('0', '8'),('2','0'),('2','9'),('6','7')]
edges = []
print(len(file_content.split(b'\n')))
n = 2312501
for connection in file_content.split(b'\n')[4:n]:
  edge = connection.decode("utf-8").strip('\r').split('\t')
  edges.append(edge)

words_rdd = sc.parallelize(edges)

mappedOut = words_rdd.flatMap(lambda record: ((record[0],record[1]), (record[1],record[0])))\
  .groupByKey()\
  .map(lambda x: [x[0], set(x[1])])\

neighbours = mappedOut.collectAsMap()
# print(neighbours)

def have_same_element(x, y):
  return len(neighbours[y].intersection(neighbours[x]))

# pairs = mappedOut.flatMap(lambda x :[(x[0], neighbour) for neighbour in itertools.combinations(neighbours[x[0]], 2)])\
#   .map(lambda x: (x[0], have_same_element(x[1][0], x[1][1])))\
#   .reduceByKey(lambda a, b: a + b)\
#   .map(lambda x: (x[0], x[1]/(len(neighbours[str(x[0])])) if len(neighbours[str(x[0])]) >= 1 else 0))

pairs = mappedOut.flatMap(lambda x :[(x[0], neighbour) for neighbour in neighbours[x[0]]])\
  .map(lambda x: (x[0], have_same_element(x[0], x[1])))\
  .reduceByKey(lambda a, b: a + b)\
  .map(lambda x: (x[0], x[1]/(len(neighbours[str(x[0])])*(len(neighbours[str(x[0])])-1)) if len(neighbours[str(x[0])]) > 1 else -1))

def addPositive(a, b):
  c = 0.0
  if a > 0:
    c+=a
  if b > 0:
    c+=b
  return c

# print(pairs.collectAsMap())
cc = pairs.reduce(lambda a, b: (0, addPositive(a[1], b[1])))
print(cc[1]/len([1 for a in neighbours.values() if len(a)])import itertools

aaa = 'web-Stanford.txt.gz'

import gzip
with gzip.open(working_dir + aaa, 'rb') as f:
    file_content = f.read()

# edges = [('1','2'),('2','3'),('3','1'),('4','1'),('4','2')]
# edges = [('1','3'),('1','0'),('0','4'),('4','5'),('5','0'),('0','6'),('6','8'),('0', '8'),('2','0'),('2','9'),('6','7')]
edges = []
print(len(file_content.split(b'\n')))
n = 2312501
for connection in file_content.split(b'\n')[4:n]:
  edge = connection.decode("utf-8").strip('\r').split('\t')
  edges.append(edge)

words_rdd = sc.parallelize(edges)

mappedOut = words_rdd.flatMap(lambda record: ((record[0],record[1]), (record[1],record[0])))\
  .groupByKey()\
  .map(lambda x: [x[0], set(x[1])])\

neighbours = mappedOut.collectAsMap()
# print(neighbours)

def have_same_element(x, y):
  return len(neighbours[y].intersection(neighbours[x]))

# pairs = mappedOut.flatMap(lambda x :[(x[0], neighbour) for neighbour in itertools.combinations(neighbours[x[0]], 2)])\
#   .map(lambda x: (x[0], have_same_element(x[1][0], x[1][1])))\
#   .reduceByKey(lambda a, b: a + b)\
#   .map(lambda x: (x[0], x[1]/(len(neighbours[str(x[0])])) if len(neighbours[str(x[0])]) >= 1 else 0))

pairs = mappedOut.flatMap(lambda x :[(x[0], neighbour) for neighbour in neighbours[x[0]]])\
  .map(lambda x: (x[0], have_same_element(x[0], x[1])))\
  .reduceByKey(lambda a, b: a + b)\
  .map(lambda x: (x[0], x[1]/(len(neighbours[str(x[0])])*(len(neighbours[str(x[0])])-1)) if len(neighbours[str(x[0])]) > 1 else -1))

def addPositive(a, b):
  c = 0.0
  if a > 0:
    c+=a
  if b > 0:
    c+=b
  return c

# print(pairs.collectAsMap())
cc = pairs.reduce(lambda a, b: (0, addPositive(a[1], b[1])))
print(cc[1]/len([1 for a in neighbours.values() if len(a)])