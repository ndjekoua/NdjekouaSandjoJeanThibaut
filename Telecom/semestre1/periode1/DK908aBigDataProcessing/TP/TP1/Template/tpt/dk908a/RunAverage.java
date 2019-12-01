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
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;
import org.apache.log4j.Logger;


public class RunAverage extends Configured implements Tool {

    private static final Logger LOG = Logger.getLogger(RunAverage.class);

    public static void main(String[] args) throws Exception {
	int res = ToolRunner.run(new RunAverage(), args);
	System.exit(res);
    }

    public int run(String[] args) throws Exception {
	Job job = Job.getInstance(getConf(), "Average movie rating");
	job.setJarByClass(this.getClass());
	FileInputFormat.addInputPath(job, new Path(args[0]+"/ratings.csv"));
	FileOutputFormat.setOutputPath(job, new Path(args[1]+".t1"));
	job.setMapperClass(ComputeAverage.Map.class);
	job.setReducerClass(ComputeAverage.Reduce.class);
	job.setOutputValueClass(FloatWritable.class);
	job.setOutputKeyClass(Text.class);
        return job.waitForCompletion(true) ? 0 : 1 ;
    } 
	    
}


