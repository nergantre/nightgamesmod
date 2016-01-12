package nightgames.global;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Logwriter {
	public Logwriter() {
		Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			System.err.println(stacktrace);
		});
	}
}
