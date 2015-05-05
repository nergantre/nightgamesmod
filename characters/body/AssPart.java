package characters.body;

import java.util.Map;
import java.util.Scanner;

import status.Stsflag;
import characters.Character;

public class AssPart extends GenericBodyPart {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1767949507600318064L;
	public static AssPart generic = new AssPart("ass", "", 0, 1, 1);

	public AssPart(String desc, String longDesc, double hotness,
			double pleasure, double sensitivity) {
		super(desc, longDesc, hotness, pleasure, sensitivity, true, "ass",
				"an ");
	}

	public AssPart(String desc, double hotness, double pleasure,
			double sensitivity) {
		super(desc, "", hotness, pleasure, sensitivity, false, "ass", "an ");
	}

	@Override
	public boolean isReady(Character c) {
		return true;
	}

	@Override
	public String getFluids(Character c) {
		return "";
	}

	@Override
	public boolean isErogenous() {
		return true;
	}

	@Override
	public BodyPart loadFromDict(Map<String, Object> dict) {
		try {
			GenericBodyPart part = new AssPart(
					(String) dict.get("desc"),
					(String) dict.get("descLong"),
					(Double) dict.get("hotness"),
					(Double) dict.get("pleasure"),
					(Double) dict.get("sensitivity"));
			return part;
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}
