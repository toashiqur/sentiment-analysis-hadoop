/* This Reducer code was developed by
 * Ashiqur Rahman, School of Computing, Queen's University
 * It takes the (key,value)=>(product ID, sentimentValue) as input from mapper
 * and produces (key,value)=>(product ID, Sentiment) as output
 */

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class SentimentReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, Text> {
      
       public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException
    {
        int sentiment_total = 0;
        // Find all values and do sum
        while (values.hasNext())
             sentiment_total += values.next().get();

     // Send Reducer final output to file
        if(sentiment_total>=0)
        	output.collect(key, new Text("Positive"));
        else
        	output.collect(key, new Text("Negative"));   
    }
}

