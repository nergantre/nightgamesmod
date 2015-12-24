package nightgames.global;

import java.io.PrintWriter;
import java.io.StringWriter;

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
