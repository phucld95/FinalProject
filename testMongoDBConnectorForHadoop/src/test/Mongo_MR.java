package test;


import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.mongodb.BasicDBObject;
import com.mongodb.hadoop.util.MongoConfigUtil;
 
 
public class Mongo_MR {
 
  public static class TokenizerMapper
       extends Mapper<Object, BasicDBObject, Text, Text>{
 
    public void map(Object key, BasicDBObject value, Context context
                    ) throws IOException, InterruptedException {
    
        String id = value.get("_id").toString();
        String device = value.get("created_at").toString();
        
        context.write(new Text(id.toString()),new Text(device.toString()));
      
    }
  }
 
  public static class IntSumReducer
       extends Reducer<Text,Text,Text,Text> {
 
    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
        
        Text result =  null;
    
      for (Text val : values) {
          result = val;
      }
     
      context.write(key, result);
    }
  }
 
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Lungfunc");
    MongoConfigUtil.setInputURI(job.getConfiguration(), "mongodb://localhost:12345/YEUHIEN.User");
    job.setJarByClass(Mongo_MR.class);
    job.setNumReduceTasks(0);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);
    job.setInputFormatClass(com.mongodb.hadoop.MongoInputFormat.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileOutputFormat.setOutputPath(job, new Path(args[0]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
