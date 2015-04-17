package characters.body;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

import combat.Combat;

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
	public String getFluids(Character c);
	public boolean isErogenous();
	public boolean isNotable();
	double applyReceiveBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c);
}
