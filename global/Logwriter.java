package global;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import characters.Character;

public class Logwriter {
	public Logwriter(){
		Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { 
			public void uncaughtException(Thread t, Throwable e) { 
				StringWriter sw = new StringWriter();
	            e.printStackTrace(new PrintWriter(sw));
	            String stacktrace = sw.toString();
	            System.out.println(stacktrace);
				Logwriter.error(stacktrace); 
				} });

	}
	public static void error(String error){
		FileWriter file;
		try {
			file = new FileWriter("nightgames.log");
			
			PrintWriter logger = new PrintWriter(file);
			logger.write(error);
			for(Character c: Global.everyone()){
				c.save(logger);
			}
			logger.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
}
