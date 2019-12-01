# Databricks notebook source
#2.1
i=0
myList = range(3000)
L = sc.parallelize(myList)
#print(L.take(3000))

#2.2

C = L.map(lambda number : number*number*number)
print(C.take(10))
#C.take(300)

# COMMAND ----------

#2.2

C = L.map(lambda number : number*number*number)
print(C.take(10))
print(C.reduce(lambda number1,number2 : number1+number2))
#C.take(300)


# COMMAND ----------

#2.3
result = C.map(lambda number : (number%10,1)).reduceByKey(lambda a,b :a+b)
print(result.take(10))

# COMMAND ----------

def digits(i):
  return [e for e in str(i)]
#print(digits(323))
result = C.flatMap(lambda number : digits(number)).map(lambda number : (number,1)).reduceByKey(lambda number1,number2 : number1+number2 ).take(300)
print(result)

# COMMAND ----------

#3.1
myList = range(3000)
C = sc.parallelize(myList)
D = C.cartesian(C)
#D.take(3000)


# COMMAND ----------

#3.2
K = 3000
def filterFunction(x,y):
  result = (2*x+1)*(2*x+1) +(2*y+1)*(2*y+1)
  if result < (2*K)*(2*K) :
      return True
  return False
D.take(5)
D1 = D.filter(lambda pair : filterFunction(pair[0],pair[1]))
#print(result)


# COMMAND ----------

#3.3
result1 = D.count()
result2 = D1.count()
print(result1)
ratio = D1.count()/D.count()
print(ratio*4)

# COMMAND ----------

#4.2
import re
future_pattern = re.compile("""([^,"]+|"[^"]+")(?=,|$)""")
def parseCSV(line):
  return future_pattern.findall(line)

path_data = "/FileStore/tables/" # CHANGE ME
ratingsFile = sc.textFile(path_data+"/ratings.csv").map(parseCSV)
moviesFile = sc.textFile(path_data+"/movies.csv").map(parseCSV)

print(moviesFile.take(2))

# COMMAND ----------

#4.3
ratingHeader = ratingsFile.first()
moviesHeader = moviesFile.first() #extract header

ratingLines = ratingsFile.filter(lambda line : line != ratingHeader)
moviesLines = moviesFile.filter(lambda line : line != moviesHeader)

ratingLines=ratingLines.map(lambda l : (l[0],l[1],float(l[2]),l[3]))
ratingLines.take(10)

# COMMAND ----------

#4.4
#contains the the top 10 rated movies
ratingRdd = ratingLines.map(lambda line : (line[1],(line[2],1))).reduceByKey(lambda a,b : (a[0]+b[0],a[1]+b[1])).map(lambda p : (p[0],p[1][0]/p[1][1])).sortBy(lambda p : -p[1])
ratingRdd.take(10)



# COMMAND ----------

#4.5

mapMovieTitle=moviesLines.map(lambda m : (m[0],m[1]))
movieAverageRating=mapMovieTitle.join(ratingRdd).map(lambda pair : pair[1])
bestMovies = movieAverageRating.sortBy(lambda m : -m[1])
bestMovies.take(10)


# COMMAND ----------

#4.6
import math
ratingRdd = ratingLines.map(lambda line : (line[1],(line[2],1))).reduceByKey(lambda a,b : (a[0]+b[0],a[1]+b[1])).map(lambda p : (p[0],p[1][0]/p[1][1]+1)).sortBy(lambda p : -p[1])
print("****raking using the first metric is :****")
print(ratingRdd.take(10))
print("/n/n****raking using the second metric is :****")

ratingRdd = ratingLines.map(lambda line : (line[1],(line[2],1))).reduceByKey(lambda a,b : (a[0]+b[0],a[1]+b[1])).map(lambda p : (p[0],(p[1][0]/p[1][1])*math.log(p[1][1]))).sortBy(lambda p : -p[1])
 
print(ratingRdd.take(10))  

# COMMAND ----------

#5

user1RatedMovies = ratingLines.filter(lambda line : int(line[0]) == 1).map(lambda line : (line[1],line[2]))#.reduceByKey(lambda a,b : a+b)
mapMovieTitle.join(user1RatedMovies).map(lambda pair : pair[1]).take(10)


# COMMAND ----------


