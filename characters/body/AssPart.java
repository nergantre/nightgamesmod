package characters.body;

import java.util.Scanner;

import status.Stsflag;
import characters.Character;

public class AssPart extends GenericBodyPart {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1767949507600318064L;
	public static AssPart generic = new AssPart("ass", 0, 1, 1);

	public AssPart(String desc, double hotness, double pleasure, double sensitivity) {
		super(desc, hotness, pleasure, sensitivity, "ass");
	}

	@Override
	public boolean isReady(Character c) {
		return c.hasStatus(Stsflag.oiled);
	}

	@Override
	public String getFluids(Character c) {
		return "";
	}
	
	public static BodyPart load(Scanner loader) {
		String line = loader.nextLine();
		String vals[] = line.split(",");
		assert(vals.length >= 6);
		return new AssPart(vals[0].trim(), Double.valueOf(vals[2].trim()), Double.valueOf(vals[3].trim()), Double.valueOf(vals[4].trim()));
	}
}
