package nightgames.characters.body;

import org.json.simple.JSONObject;

import nightgames.characters.Character;
import nightgames.characters.Trait;

public class AnalPussyPart extends AssPart {
	/**
	 *
	 */
	public static AnalPussyPart		generic			= new AnalPussyPart();
	public static final BodyPartMod	AnalPussyMod	= () -> "AnalPussy";

	public AnalPussyPart(double size) {
		super("anal pussy", "Instead of a normal sphincter, {self:possessive} round butt is crowned by a slobbering second pussy.", .5, 2.2, 1.3, size, true);
	}

	public AnalPussyPart() {
		this(AssPart.SIZE_NORMAL);
	}

	@Override
	public boolean isReady(Character c) {
		return c.getArousal().percent() > 15 || c.has(Trait.alwaysready)
				|| super.isReady(c);
	}

	@Override
	public String getFluids(Character c) {
		if (super.getFluids(c).isEmpty()) {
			return "juices";
		} else {
			return super.getFluids(c);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public BodyPart loadFromDict(JSONObject dict) {
		return new AnalPussyPart(((Number)dict.getOrDefault("size", Double.valueOf(AssPart.SIZE_NORMAL))).doubleValue());
	}

	@Override
	public BodyPartMod getMod() {
		return AnalPussyMod;
	}
}
