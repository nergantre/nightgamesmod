package nightgames.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceLoader {
	public static InputStream getFileResourceAsStream(String path) {
		// first check in the working directory
		File f = new File(path);
		try {
			return new FileInputStream(f);
		} catch (FileNotFoundException e) {}
		// then check in the class directory
		return ResourceLoader.class.getClassLoader().getResourceAsStream("resources/" + path);
	}
}
