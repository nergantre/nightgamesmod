package nightgames.characters.body;

public class StraponPart extends GenericBodyPart {
	/**
	 * 
	 */

	public static StraponPart generic = new StraponPart();
	public StraponPart() {
		super("strap-on", 1, 1, -999, "strapon", "a ");
	}
	public static boolean isStrapon(BodyPart part) {
		return part != null && part.isType("strapon");
	}
}