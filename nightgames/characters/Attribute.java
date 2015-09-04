package nightgames.characters;

public enum Attribute {
	Power,
	Seduction,
	Cunning,
	Perception,
	Speed,
	Arcane,
	Science,
	Dark,
	Fetish,
	Animism,
	Ki,
	Bio,
	Divinity,
	Willpower;

	public static boolean isBasic(Attribute a) {
		return a == Power || a == Seduction || a == Perception;
	}

	public static boolean isTrainable(Attribute a) {
		return a != Speed && a != Perception;
	}
}
