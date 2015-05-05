package characters.body;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

import combat.Combat;

import characters.Attribute;
import characters.Character;

public interface BodyPart extends Serializable {
	public void describeLong(StringBuilder b, Character c);
	public boolean isType(String type);
	public String getType();
	public String describe(Character c);
	public double getHotness(Character self, Character opponent);
	public double getPleasure(BodyPart target);
	public double getSensitivity(BodyPart target);
	public String toString();
	public boolean isReady(Character self);
	public void save(PrintWriter saver);
	public double applyBonuses(Character self, Character opponent, BodyPart target, double damage, Combat c);
	public double applySubBonuses(Character self, Character opponent, BodyPart with, BodyPart target, double damage, Combat c);
	public String getFluids(Character c);
	public boolean isVisible(Character c);
	public boolean isErogenous();
	public boolean isNotable();
	public BodyPart upgrade();
	public int compare(BodyPart other);
	public BodyPart downgrade();
	double applyReceiveBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c);
	public String prefix();
	public String fullDescribe(Character c);
	double priority(Character c);
	public int mod(Attribute a, int total);
	public BodyPart load(Scanner scanner);
}
