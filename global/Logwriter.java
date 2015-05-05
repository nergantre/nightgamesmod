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
	            System.err.println(stacktrace);
				} });
	}
}
