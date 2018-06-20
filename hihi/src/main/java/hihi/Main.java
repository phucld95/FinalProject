package hihi;

import java.io.File;
import java.net.URI;

public class Main {
	public static void main(String[] args) throws Exception {
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "writeHdfs-0.0.1-SNAPSHOT-jar-with-dependencies.jar", "arg1", "arg2");
		pb.directory(new File("/home/lephuc/eclipse-workspace/writeHdfs/target"));
		Process p = pb.start();
		System.out.println("Done baby");
	  }
}
