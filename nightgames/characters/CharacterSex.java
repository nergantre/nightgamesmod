package nightgames.characters;

import nightgames.global.Global;

public enum CharacterSex {
	male("male"),
	female("female"),
	herm("hermaphrodite"),
	asexual("asexual (warning: untested)");

	private String desc;

	CharacterSex(String desc) {
		this.desc = desc;
	}
	public String toString() {
		return Global.capitalizeFirstLetter(desc);
	}
}
