package tpt.dk908a;

import java.io.IOException;
import java.util.regex.Matcher;
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



public class ComputeAverage {
	
       
   public static class Map extends Mapper<LongWritable, Text, Text, FloatWritable> {
    	
	  public void map(LongWritable offset, Text line, Context context)
	      throws IOException, InterruptedException {
		
		     private static final Pattern p = Pattern.compile("^([0-9]+),([0-9]+),([0-9]+),[0-9]+"); //pattern for parsing each input line  	
	    	 private final static IntWritable value = new FloatWritable(1);//number to put as a value
	         private Text key = new Text();//filed that will conteins the key of the map
	         
	         Matcher m = p.matcher(line.toString());
	         if (m.find()) { //for sure the header of the file will not be matched because contains non alphanumerical carachters
	             String movieId = m.group(2);
	             //should be converted to float
	             Float rating = Float.parseFloat(m.group(3));
	              //set the key as Text in the field key
                   key.set(movieId);
                   value.set(rating);
                 //create the mapping key value pair and send it on the output 
                   context.write(word,one);  
	         }
	  }
    }
    
    public static class Reduce extends Reducer<Text, FloatWritable, Text, FloatWritable> {

	   public void reduce(Text movie, Iterable<FloatWritable> values, Context context) 
	      throws IOException, InterruptedException {
	      
		float sum = 0;
		int size =0;
		
	        for(FloatWritable v : values) { 
	    	 
	         	sum+= v.get(); /* for the value */
	    	    l++;
	         }
	      context.write(movie,new FloatWritable(sum/l));
	}
    }
}


