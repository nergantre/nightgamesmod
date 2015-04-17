package characters.body;

import java.io.PrintWriter;
import java.util.Scanner;

import combat.Combat;

import characters.Character;

public enum TailPart implements BodyPart {
	demonic("demonic ", 0, 1.2),
	cat("cat's ", 0, 1.5), 
	normal("", 0, 1);

	public String desc;
	public double hotness;
	public double pleasure;
	TailPart(String desc, double hotness, double pleasure) {
		this.desc = desc;
		this.hotness = hotness;
		this.pleasure = pleasure;
	}

	@Override
	public void describeLong(StringBuilder b, Character c) {
		b.append("A lithe " + describe(c) + " swings lazily behind " + c.nameOrPossessivePronoun() + " back.");
	}

	@Override
	public String describe(Character c) {
		return desc + "tail";
	}
	
	@Override
	public String toString() {
		return desc + "tail";
	}
	
	public boolean isType(String type) {
		return type.equalsIgnoreCase("tail");
	}

	@Override
	public String getType() {
		return "tail";
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
		return TailPart.valueOf(loader.nextLine());
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		return damage;
	}

	@Override
	public String getFluids(Character c) {
		return this == TailPart.demonic ? "tail-cum" : "";
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
