#/bin/bash

#export HADOOP_CLASSPATH="$(yarn classpath)"
mkdir classes
javac -classpath $HADOOP_CLASSPATH -d classes tpt/dk908a/ExtractUserMovies.java tpt/dk908a/CountPairs.java tpt/dk908a/SelectPairs.java tpt/dk908a/NameMovie.java tpt/dk908a/MovieRecommendation.java tpt/dk908a/RunAverage.java  tpt/dk908a/ComputeAverage.java ;
jar -cvf code.jar -C classes/ . ;
rm -r classes ;

#hadoop jar code.jar tpt.dk908a.RunAverage /datasets/movie_small /tmp/$USER
#hadoop jar code.jar tpt.dk908a.MovieRecommendation /datasets/movie_small /tmp/$USER

