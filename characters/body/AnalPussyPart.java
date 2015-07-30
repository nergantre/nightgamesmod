package characters.body;

import java.util.Map;
import java.util.Scanner;

import characters.Character;
import characters.Trait;

public class AnalPussyPart extends AssPart {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7999826317978553116L;
	public static AnalPussyPart generic = new AnalPussyPart();

	public AnalPussyPart() {
		super("anal pussy", "Instead of a normal sphincter, {self:possessive} round butt is crowned by a slobbering second pussy.", .5, 2.2, 1.3);
	}

	@Override
	public boolean isReady(Character c) {
		return c.getArousal().percent() > 15  || c.has(Trait.alwaysready) || super.isReady(c);
	}

	@Override
	public String getFluids(Character c) {
		if (super.getFluids(c).isEmpty())
			return "juices";
		else
			return super.getFluids(c);
	}

	@Override
	public BodyPart loadFromDict(Map<String,Object> dict) {
		return new AnalPussyPart();
	}
}
