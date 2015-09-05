package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.global.JSONUtils;

public class Enthralled extends DurationStatus {
	private int timesRefreshed;
	public Character master;
	
	public Enthralled(Character self,Character master, int duration) {
		super("Enthralled", self, duration);
		timesRefreshed = 0;
		this.master = master;
		flag(Stsflag.enthralled);
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		if (replaced) {
			return String.format("%s %s control of %s.\n", master.subjectAction("reinforce", "reinforces"), master.possessivePronoun(), affected.nameDirectObject());
		} else {	
			return String.format("%s now enthralled by %s.\n", affected.subjectAction("are", "is"), master.subject());
		}
	}

	@Override
	public String describe() {
		if(affected.human())
		  return "You feel a constant pull on your mind, forcing you to obey " + master.possessivePronoun() + " every command.";
		else{
			return affected.name()+" looks dazed and compliant, ready to follow your orders.";
		}
	}

	@Override
	public String getVariant() {
			return "enthralled by " + master.name();
	}

	@Override
	public boolean overrides(Status s) {
		return false;
	}

	@Override
	public void replace(Status s) {
		assert (s instanceof Enthralled);
		Enthralled other = (Enthralled)s;
		setDuration(Math.max(getDuration() + 1, other.getDuration() - timesRefreshed));
		timesRefreshed += 1;
	}

	@Override
	public boolean mindgames(){
		return true;
	}
	
	@Override
	public float fitnessModifier () {
		return -getDuration() * 5;
	}
	
	@Override
	public int mod(Attribute a) {
		if (a == Attribute.Perception)
			return -5;
		return -2;
	}


	@Override
	public void onRemove(Combat c, Character other) {
		affected.addlist.add(new Cynical(affected));
		Global.gui().message("Everything around you suddenly seems much clearer,"
				+ " like a lens snapped into focus. You don't really remember why"
				+ " you were heading in the direction you where...");
	}

	@Override
	public int regen(Combat c) {
		super.regen(c);
		if (affected.check(Attribute.Cunning, master.get(Attribute.Seduction)/2 +master.get(Attribute.Arcane)/2 + master.get(Attribute.Dark)/2 + 10+10*(getDuration() - timesRefreshed))) {
			if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
				System.out.println("Escaped from Enthralled");
			}
			setDuration(0);
		}
		affected.spendMojo(c, 5);
		affected.loseWillpower(c, 1);
		affected.emote(Emotion.horny,15);
		return 0;
	}

	@Override
	public int damage(Combat c, int paramInt) {
		return 0;
	}

	@Override
	public double pleasure(Combat c, double paramInt) {
		return paramInt/4;
	}

	@Override
	public int weakened(int paramInt) {
		return 0;
	}

	@Override
	public int tempted(int paramInt) {
		return paramInt/4;
	}

	@Override
	public int evade() {
		return -20;
	}

	@Override
	public int escape() {
		return -20;
	}

	@Override
	public int gainmojo(int paramInt) {
		return -paramInt;
	}

	@Override
	public int spendmojo(int paramInt) {
		return 0;
	}

	@Override
	public int counter() {
		return 0;
	}

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Enthralled(newAffected, newOther, getDuration());
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("duration", getDuration());
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new Enthralled(null, null, JSONUtils.readInteger(obj, "duration"));
	}
}
