package characters.body;

import java.io.PrintWriter;
import java.util.Scanner;

import combat.Combat;

import characters.Character;

public enum WingsPart implements BodyPart {
	demonic("demonic ", 0),
	normal("", 0);
	public String desc;
	public int hotness;
	WingsPart(String desc, int hotness) {
		this.desc = desc;
		this.hotness = hotness;
	}

	@Override
	public void describeLong(StringBuilder b, Character c) {
		b.append("A pair of " + describe(c) + " sits gracefully between " + c.nameOrPossessivePronoun() + " shoulder blades.");
	}

	@Override
	public String describe(Character c) {
		return desc + "wings";
	}
	
	@Override
	public String toString() {
		return desc + "wings";
	}
	
	public boolean isType(String type) {
		return type.equalsIgnoreCase("wings");
	}

	@Override
	public String getType() {
		return "wings";
	}

	@Override
	public double getHotness(Character self, Character opponent) {
		return 1 + hotness;
	}

	@Override
	public double getPleasure(BodyPart target) {
		return 1;
	}

	@Override
	public double getSensitivity(BodyPart target) {
		return 1;
	}
	@Override
	public boolean isReady(Character self) {
		return true;
	}

	@Override
	public void save(PrintWriter saver) {
		saver.write(this.name());
	}

	public static BodyPart load(Scanner loader) {
		return WingsPart.valueOf(loader.nextLine());
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		return damage;
	}

	@Override
	public String getFluids(Character c) {
		return "";
	}

	@Override
	public boolean isErogenous() {
		return false;
	}

	@Override
	public boolean isNotable() {
		return true;
	}
	@Override
	public double applyReceiveBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		return damage;
	}
}
