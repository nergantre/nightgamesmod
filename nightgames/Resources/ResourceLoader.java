package nightgames.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import nightgames.global.DebugFlags;
import nightgames.global.Global;

public class ResourceLoader {
	public static InputStream getFileResourceAsStream(String path) {
		// first check in the working directory
		File f = new File(path);
		try {
			InputStream res = new FileInputStream(f);
			return res;
		} catch (FileNotFoundException e) {}
		// then check in the class directory		
		return ResourceLoader.class.getClassLoader().getResourceAsStream("resources/" + path);
	}
}
