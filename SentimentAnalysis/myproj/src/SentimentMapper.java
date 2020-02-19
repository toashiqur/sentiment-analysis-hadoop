/* This Mapper code was developed by
 * Ashiqur Rahman, School of Computing, Queen's University
 * It reads input from hdfs files and produces (key,sentimentValue) for the Reducer
 */

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class SentimentMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>
{
    private String positive_file_path, negative_file_path;
    private List<String> positiveList;
	private List<String> negativeList;
   
    
    public void configure(JobConf job) {
        super.configure(job);
        positive_file_path = job.get("positive_file");
        negative_file_path = job.get("negative_file");
        try{
        	positiveList = read_words(positive_file_path);
        	negativeList = read_words(negative_file_path);
        }
        catch(Exception e)
        {
        }
    }
    

    public List<String> read_words(String hdfs_file_path) throws IOException{
        Path pt=new Path(hdfs_file_path);//Location of file in HDFS
        FileSystem fs = FileSystem.get(new Configuration());
        BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
        List<String> myList = new ArrayList<String>();
        String word;
        word = br.readLine();
        while (word != null){
        	myList.add(word.trim());
            word=br.readLine();
        }
        return myList;
    }
    
    
    public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException
    {
    	int pos=0, neg=0, sentimentInt =0;
    	String productId, reviewBody, searchWord = "";
 
       //taking one line/record at a time and parsing them into key value pairs
        String line = value.toString();
        String splitline[] = line.split("\t");
        
       //Check whether a line has the actual number of cells:: should be 8 cells
        if (splitline.length == 8)
        {
            productId = splitline[1];
            reviewBody = splitline[7];
            
            String reviewBodyArray[] = reviewBody.split(("[ |\\,|\\.]"));
            
            for( int i=0; i<reviewBodyArray.length; i++)
            {
            	searchWord = reviewBodyArray[i];
            	
            	if (positiveList.contains(searchWord))
            		pos++;
            	else if(negativeList.contains(searchWord))
            		neg++;
            }
            
            //Determine overall sentiment of one particular review
            sentimentInt = pos - neg;

          //sending the key value pair out of Sentiment mapper
            output.collect(new Text(productId), new IntWritable(sentimentInt));
        }
     }
}