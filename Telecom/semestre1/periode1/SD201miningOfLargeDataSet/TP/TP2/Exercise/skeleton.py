c.textFile("/FileStore/tables/steve.txt")
words = text_file.flatMap(lambda line: line.split(" "))
pairs = words.map(lambda s: (s, 2))
counts = pairs.reduceByKey(lambda a, b: a + b)
for (count, word) in counts.take(5):
  print "%s: %i" % (count, word)
