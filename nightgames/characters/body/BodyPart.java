package nightgames.characters.body;
import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.DebugFlags;
import nightgames.global.Global;

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
	
	/**-1 is weak, 0 is none, 1 is strong<br>
	  ex: <br><code>
	  	  PussyPart.fiery.counterValue(CockPart.primal) == -1   // fiery pussy builds mojo for primal cock<br>
	      CockPart.primal.counterValue(PussyPart.fiery) == 1    // the reverse case<br>
	      CockPart.primal.counterValue(PussyPart.succubus) == 0 // no effects: both parts function as normal<br>
	      </code>
	  It must always hold that:<br><code>
	   X.counterValue(Y) == -1 <=> Y.counterValue(X) == 1<br>
	   X.counterValue(Y) == 0  <=> Y.counterValue(X) == 0<br>
	   </code>
	*/
	public int counterValue(BodyPart other);
	
	// Should be called whenever a combatant is penetrated in any way
	public default void onStartPenetration(Combat c, Character self, Character opponent, BodyPart target) {
		//Do nothing, may be overridden in implementing classes.
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE))
			System.out.printf("Starting Penetration for %s -> (%s, %s, %s)", this, self, opponent, target);
	}
	
	// Should be called when penetration ends
	public default void onEndPenetration(Combat c, Character self, Character opponent, BodyPart target) {
		//Do nothing, may be overridden in implementing classes.
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE))
			System.out.printf("Ending Penetration for %s -> (%s, %s, %s)", this, self, opponent, target);
	}
	
	// Should be called when either combatant orgasms
	public default void onOrgasm(Combat c, Character self, Character opponent, BodyPart other, boolean selfCame) {
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE))
			System.out.printf("Processing Orgasm for %s -> (%s, %s, %s, %s)", this, self, opponent, other, selfCame+"");
	}
}
