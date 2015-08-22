package nightgames.characters.body;
import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public interface BodyPart {
	public void describeLong(StringBuilder b, Character c);
	public boolean isType(String type);
	public String getType();
	public String describe(Character c);
	public double getHotness(Character self, Character opponent);
	public double getPleasure(Character self, BodyPart target);
	public double getSensitivity(BodyPart target);
	public String toString();
	public boolean isReady(Character self);
	public JSONObject save();
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
	public BodyPart load(JSONObject obj);
	public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan);
}
