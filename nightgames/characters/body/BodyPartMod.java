package nightgames.characters.body;

public interface BodyPartMod {
	BodyPartMod noMod = () -> "none";
	String getModType();
}
