package characters.body;

import java.util.Scanner;

import characters.Character;

public class AnalPussyPart extends GenericBodyPart {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7999826317978553116L;
	public static AnalPussyPart generic = new AnalPussyPart();

	public AnalPussyPart() {
		super("anal pussy", .5, 2.5, 1.3, true, "ass");
	}
	
	@Override
	public boolean isReady(Character c) {
		return c.getArousal().percent() > 15 || super.isReady(c);
	}

	@Override
	public String getFluids(Character c) {
		return "juices";
	}
	
	public static BodyPart load(Scanner loader) {
		return new AnalPussyPart();
	}
}
