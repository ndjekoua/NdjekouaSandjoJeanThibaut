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


public class MovieRecommendation extends Configured implements Tool {

    private static final Logger LOG = Logger.getLogger(MovieRecommendation.class);

    public static void main(String[] args) throws Exception {
	int res = ToolRunner.run(new MovieRecommendation(), args);
	System.exit(res);
    }

    public int run(String[] args) throws Exception {
      Job job1 = Job.getInstance(getConf(), "Extract User Movies");
      job1.setJarByClass(this.getClass());
      FileInputFormat.addInputPath(job1, new Path(args[0]+"/ratings.csv"));
      FileOutputFormat.setOutputPath(job1, new Path(args[1]+".eum"));
      job1.setMapperClass(ExtractUserMovies.Map.class);
      job1.setReducerClass(ExtractUserMovies.Reduce.class);
      job1.setOutputKeyClass(Text.class);
      job1.setOutputValueClass(Text.class);
      
      if(!job1.waitForCompletion(true)){
        System.exit(1);
      }
      
      Job job2 = Job.getInstance(getConf(), "Count Pairs");
      job2.setJarByClass(this.getClass());
      FileInputFormat.addInputPath(job2, new Path(args[1]+".eum/part*"));
      FileOutputFormat.setOutputPath(job2, new Path(args[1]+".cp"));
      job2.setMapperClass(CountPairs.Map.class);
     // job2.setCombinerClass(CountPairs.Reduce.class);
      job2.setReducerClass(CountPairs.Reduce.class);
      job2.setOutputKeyClass(Text.class);
      job2.setOutputValueClass(IntWritable.class);
      
      if(!job2.waitForCompletion(true)){
        System.exit(1);
      }
      
      Job job3 = Job.getInstance(getConf(), "Select Pairs");
      job3.setJarByClass(this.getClass());
      FileInputFormat.addInputPath(job3, new Path(args[1]+".cp/part*"));
      FileOutputFormat.setOutputPath(job3, new Path(args[1]+".sp"));
      job3.setMapperClass(SelectPairs.Map.class);
      job3.setReducerClass(SelectPairs.Reduce.class);
      job3.setOutputKeyClass(Text.class);
      job3.setOutputValueClass(Text.class);
      
      if(!job3.waitForCompletion(true)){
        System.exit(1);
      }

      Job job4 = Job.getInstance(getConf(), "Name Movie 1");
      job4.setJarByClass(this.getClass());
      MultipleInputs.addInputPath(job4, new Path(args[1]+".sp/part*"),
        TextInputFormat.class, NameMovie.MapRatings.class);
      MultipleInputs.addInputPath(job4, new Path(args[0]+"/movies.csv"),
        TextInputFormat.class, NameMovie.MapMovies.class);
      FileOutputFormat.setOutputPath(job4, new Path(args[1]+".nm1"));
      job4.setReducerClass(NameMovie.Reduce.class);
      job4.setOutputKeyClass(Text.class);
      job4.setOutputValueClass(Text.class);

      if(!job4.waitForCompletion(true)){
        System.exit(1);
      }

      Job job5 = Job.getInstance(getConf(), "Name Movie 2");
      job5.setJarByClass(this.getClass());
      MultipleInputs.addInputPath(job5, new Path(args[1]+".nm1/part*"),
        TextInputFormat.class, NameMovie.MapRatings.class);
      MultipleInputs.addInputPath(job5, new Path(args[0]+"/movies.csv"),
        TextInputFormat.class, NameMovie.MapMovies.class);
      FileOutputFormat.setOutputPath(job5, new Path(args[1]+".result"));
      job5.setReducerClass(NameMovie.Reduce.class);
      job5.setOutputKeyClass(Text.class);
      job5.setOutputValueClass(Text.class);

      return job5.waitForCompletion(true) ? 0 : 1 ;
    }

}
