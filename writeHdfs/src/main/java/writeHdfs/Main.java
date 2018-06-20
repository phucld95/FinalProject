package writeHdfs;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.net.URI;
import java.util.logging.Logger;

public class Main {

   private static final Logger logger = Logger.getLogger("io.saagie.example.hdfs.Main");

   public static void main(String[] args) throws Exception {
	  if(args.length <= 1){
	       System.out.println("Filename and content cant found");
	  }

      String hdfsuri = "hdfs://localhost:9000";
      String path = "/Lung_system_records";
      String fileName = args[0];
      String fileContent = args[1];

      Configuration conf = new Configuration();
      conf.set("fs.defaultFS", hdfsuri);
      conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
      conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
      System.setProperty("hadoop.home.dir", "/");
      FileSystem fs = FileSystem.get(URI.create(hdfsuri), conf);

      Path workingDir=fs.getWorkingDirectory();
      Path newFolderPath= new Path(path);
      if(!fs.exists(newFolderPath)) {
         // Create new Directory
         fs.mkdirs(newFolderPath);
      }

      logger.info("Begin Write file into hdfs");
      Path hdfswritepath = new Path(newFolderPath + "/" + fileName);
      FSDataOutputStream outputStream=fs.create(hdfswritepath);
      outputStream.writeBytes(fileContent);
      outputStream.close();
      logger.info("End Write file into hdfs");

      logger.info("Read file into hdfs");
      Path hdfsreadpath = new Path(newFolderPath + "/" + fileName);
      FSDataInputStream inputStream = fs.open(hdfsreadpath);
      String out= IOUtils.toString(inputStream, "UTF-8");
      logger.info(out);
      inputStream.close();
      fs.close();
   }
}