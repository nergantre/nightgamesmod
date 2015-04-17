package characters.body;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Scanner;

import combat.Combat;

import characters.Character;

public class GenericBodyPart implements BodyPart {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6412667696235300087L;
	public String type;
	public String desc;
	public double hotness;
	public double sensitivity;
	public double pleasure;
	public String descLong;
	private boolean notable;

	public GenericBodyPart(String desc, String descLong, double hotness, double pleasure, double sensitivity, boolean notable, String type) {
		this.desc = desc;
		this.descLong = descLong;
		this.hotness = hotness;
		this.pleasure = pleasure;
		this.sensitivity = sensitivity;
		this.type = type;
		this.notable = notable;
	}

	public GenericBodyPart(String desc, double hotness, double pleasure, double sensitivity, String type) {
		this(desc, "", hotness, pleasure, sensitivity, false, type);
	}

	public GenericBodyPart(String desc, double hotness, double pleasure, double sensitivity, boolean notable, String type) {
		this(desc, "", hotness, pleasure, sensitivity, notable, type);
	}

	@Override
	public void describeLong(StringBuilder b, Character c) {
		String parsedDesc = descLong.replace("{psv-pronoun}", c.nameOrPossessivePronoun());
		b.append(parsedDesc);
	}

	@Override
	public boolean isType(String type) {
		return this.type.equalsIgnoreCase(type);
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String describe(Character c) {
		return desc;
	}
	
	@Override
	public String toString() {
		return desc;
	}

	@Override
	public double getHotness(Character self, Character opponent) {
		return 1 + hotness;
	}

	@Override
	public double getPleasure(BodyPart target) {
		return pleasure;
	}

	@Override
	public double getSensitivity(BodyPart target) {
		return sensitivity;
	}

	@Override
	public boolean isReady(Character self) {
		return true;
	}

	@Override
	public boolean equals(Object other)
	{
		return this.toString().equals(other.toString());
	}

	@Override
	public int hashCode() {
		return (this.type + ":" +this.toString()).hashCode();
	}

	@Override
	public void save(PrintWriter saver) {
		saver.write(desc);
		saver.write(", ");
		saver.write(descLong);
		saver.write(", ");
		saver.write(Double.toString(hotness));
		saver.write(", ");
		saver.write(Double.toString(pleasure));
		saver.write(", ");
		saver.write(Double.toString(sensitivity));
		saver.write(", ");
		saver.write(Boolean.toString(notable));
		saver.write(", ");
		saver.write(type);
	}

	public static BodyPart load(Scanner loader) {
		String line = loader.nextLine();
		String vals[] = line.split(",");
		assert(vals.length >= 6);
		boolean notable = false;
		String type = vals[5];
		if (vals.length >= 7) {
			notable = Boolean.valueOf(vals[5].trim());
			type = vals[6].trim();
		}
		return new GenericBodyPart(vals[0].trim(), vals[1].trim(), Double.valueOf(vals[2].trim()), Double.valueOf(vals[3].trim()), Double.valueOf(vals[4].trim()), notable, type);
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
		return notable;
	}
	@Override
	public double applyReceiveBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		return damage;
	}
}
