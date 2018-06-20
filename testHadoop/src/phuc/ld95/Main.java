package phuc.ld95;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.util.logging.Logger;

public class Main {
   public static void main(String[] args) throws Exception {
      //HDFS URI
      String hdfsuri = "hdfs://localhost:9000";

      //Source path.
      String path="/user/hdfs/example/hdfs/";
      String fileName="hello.csv";
      String fileContent="hello;world";

      //Init HDFS File System Object
      Configuration conf = new Configuration();
      // Set FileSystem URI
      conf.set("fs.defaultFS", hdfsuri);
      // Because of Maven
      conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
      conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
      // Set HADOOP user
//      System.setProperty("HADOOP_USER_NAME", "hdfs");
      System.setProperty("hadoop.home.dir", "/");
      //Get the filesystem - HDFS
      FileSystem fs = FileSystem.get(URI.create(hdfsuri), conf);

      //==== Create folder if not exists
      Path workingDir=fs.getWorkingDirectory();
      Path newFolderPath= new Path(path);
      if(!fs.exists(newFolderPath)) {
         // Create new Directory
         fs.mkdirs(newFolderPath);
         System.out.println("Create new directory sucess.");
      }

      //==== Write file
      System.out.println("Begin Write file into hdfs");
      //Create a path
      Path hdfswritepath = new Path(newFolderPath + "/" + fileName);
      //Init output stream
      FSDataOutputStream outputStream=fs.create(hdfswritepath);
      //Cassical output stream usage
      outputStream.writeBytes(fileContent);
      outputStream.close();
      System.out.println("End Write file into hdfs");

      //==== Read file
      System.out.println("Read file into hdfs");
      //Create a path
      Path hdfsreadpath = new Path(newFolderPath + "/" + fileName);
      //Init input stream
      FSDataInputStream inputStream = fs.open(hdfsreadpath);
      //Classical input stream usage
      String out= IOUtils.toString(inputStream, "UTF-8");
      System.out.println("OUT");
      inputStream.close();
      fs.close();

   }
}
