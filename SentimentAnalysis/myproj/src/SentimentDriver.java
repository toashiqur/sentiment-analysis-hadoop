/* This SetimentDriver code was developed by
 * Ashiqur Rahman, School of Computing, Queen's University
 * for doing sentiment analysis
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class SentimentDriver extends Configured implements Tool
{
       @Override
	public int run(String[] args) throws Exception {

              //get the configuration parameters and assigns a job name
              JobConf conf = new JobConf(getConf(), SentimentDriver.class);
              conf.setJobName("Sentiment Analysis");
              
            //specifying the reducer class              
              conf.setMapperClass(SentimentMapper.class);

              //setting key value types for mapper and reducer outputs
              conf.setMapOutputKeyClass(Text.class);
              conf.setMapOutputValueClass(IntWritable.class);
              
              //Final output by the Reducer
              conf.setOutputKeyClass(Text.class);
              conf.setOutputValueClass(Text.class);

              //specifying the reducer class
              conf.setReducerClass(SentimentReducer.class);

              //List of positive words
              conf.set("positive_file", args[0]);    
              //List of negative words
              conf.set("negative_file", args[1]);  
              
              FileInputFormat.addInputPath(conf, new Path(args[2]));
             
              //Specifying the output directory @ runtime
              FileOutputFormat.setOutputPath(conf, new Path(args[3]));
              
              JobClient.runJob(conf);
              return 0;
       }

       public static void main(String[] args) throws Exception {
              int res = ToolRunner.run(new Configuration(), new SentimentDriver(),
                           args);
              System.exit(res);
       }
}