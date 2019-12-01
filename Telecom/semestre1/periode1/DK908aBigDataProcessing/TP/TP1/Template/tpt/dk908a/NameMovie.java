package tpt.dk908a;

import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.log4j.Logger;


public class NameMovie {
    
    public static class MapRatings extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable offset, Text moviePair, Context context)
	    throws IOException, InterruptedException {
	    //FILL HERE
	    //
	    // use
	    //context.write(key,value)
	}
    }

    public static class MapMovies extends Mapper<LongWritable, Text, Text, Text> {

        // this pattern allows you to split the CSV file movies.csv where
        // some movie titles include the ',' char
        private static final Pattern p = Pattern.compile("^([0-9]+),([^,\"]+|\"[^\"]+\"),(.+)$");
       
        
	public void map(LongWritable offset, Text moviePair, Context context)
	    throws IOException, InterruptedException {
	    //FILL HERE
	    //
	    // use
	    //context.write(key,value)
	}
    }
    
    public static class Reduce extends Reducer<Text, Text, Text, Text> {	
	@Override
	public void reduce(Text movie, Iterable<Text> values, Context context) 
	    throws IOException, InterruptedException {
	    // FILL HERE
	    //
	    // use
	    // context.write(key,value);
	}
    }
}


