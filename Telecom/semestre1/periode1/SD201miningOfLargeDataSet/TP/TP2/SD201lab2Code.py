#Question 1

#lines = sc.textFile ("/FileStore/tables/steve.txt")
#lines.flatMap(lambda line: line.split(" ")).map(lambda word : (word,1)).reduceByKey(lambda a,b : a+b).take(20)

text_file=sc.textFile("/FileStore/tables/steve.txt")
words = text_file.flatMap(lambda line: line.split(" "))
pairs = words.map(lambda s: (s, 1))
counts = pairs.reduceByKey(lambda a, b: a + b)
for (count, word) in counts.take(20):
  print("{} {}".format(count, word))

# i actually had to make the following modifications:
    #1)mofified c into sc and defined the variable text_file which stores the lines of the text
    #2)modified the print format because the previous one was giving some errors
    #3)modified the map from (s,2) to (s,2)

#QUESTION 2

text_file=sc.textFile("/FileStore/tables/steve.txt")
words = text_file.flatMap(lambda line: line.split(" "))
pairs = words.map(lambda s: (s, 1))
counts = pairs.reduceByKey(lambda a, b: a + b).sortBy(lambda a: -a[1])
counts.take(5)

#Question 3

#read the file ams store it as an RDD in the variable text_file
text_file=sc.textFile("/FileStore/tables/steve.txt")
words = text_file.flatMap(lambda line: line.split(" "))
pairs = words.map(lambda s: (s, 1))
wordsWithAtLeastFiveCarachters = pairs.reduceByKey(lambda a, b: a + b).filter(lambda pair : len(pair[0])>=5)
counts = wordsWithAtLeastFiveCarachters.sortBy(lambda a: -a[1])
counts.take(5)

#Question4

#take as input the lines of the file and return a list of tuple(a bit like dictionaries) where each tuple is like (pageId-> value)
def mapIdTitle(lines):
  space = " "
  returnList = []
  i=0
  while i in range(len(lines)):
    line = lines[i]
    index = line.find(space)
   # print(index)
    pair = (line[:index], line[index+1:])
   # print(pair)
    i+=1
    returnList.append(pair)
  return returnList


text_file=sc.textFile("/FileStore/tables/edgelist.txt")
idsLabel = sc.textFile("/FileStore/tables/idslabels.txt")
lines = idsLabel.flatMap(lambda lines : lines.split("\n")).collect()
#contains the the mapping pageId->siteName
mappedIds = mapIdTitle(lines)

#now i have for each page the corresponding name in a tuple (pageId -> name), so i parallelize it to trnasform it like RDD
mappedIds= sc.parallelize(mappedIds)
#split the lines of the text using /n as delimiter
lines = text_file.flatMap(lambda line: line.split("\n"))

# the [1:] after split allows me to take only the elemtn from index 1 on
pairs = lines.flatMap(lambda line : line.split(" ")[1:]).map(lambda id : (id,1))
#collect the ordered list containing the top ten pages
topTenPagesList = pairs.reduceByKey(lambda a,b: a+b).sortBy(lambda pair : -pair[1]).take(10)
#now prallelize the list to transform it as RDD
topTenPages = sc.parallelize(topTenPagesList)
#join the 2 RDD, perfrom the sort again and collect everythong
mappedIds.join(topTenPages).map(lambda pair : pair[1]).sortBy(lambda pair : -pair[1]).collect()


