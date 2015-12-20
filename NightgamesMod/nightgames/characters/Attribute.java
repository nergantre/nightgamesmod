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
	Willpower,
	Technique, 
	Submissive, 
	Hypnosis;

	public static boolean isBasic(Attribute a) {
		return a == Power || a == Seduction || a == Perception;
	}

	public static boolean isTrainable(Attribute a, Character self) {
		return a != Speed && a != Perception && (self.has(Trait.divinity) || a != Divinity);
	}
}
