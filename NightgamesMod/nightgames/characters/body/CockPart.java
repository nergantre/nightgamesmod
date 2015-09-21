package nightgames.characters.body;

public interface CockPart extends BodyPart {
	double getSize();
	BodyPart applyMod(CockMod mod);
}
