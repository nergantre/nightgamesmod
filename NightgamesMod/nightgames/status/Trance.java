package nightgames.status;

import java.util.Arrays;
import java.util.Collection;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.skills.Masturbate;
import nightgames.skills.Piston;
import nightgames.skills.Skill;
import nightgames.skills.Thrust;

public class Trance extends DurationStatus {
	public Trance(Character affected, int duration) {
		super("Trance", affected, duration);
		flag(Stsflag.trance);
	}
	public Trance(Character affected) {
		this(affected, 3);
	}
	@Override
	public String describe(Combat c) {
		if(affected.human()){
			return "You know that you should be fighting back, but it's so much easier to just surrender.";
		}
		else{
			return affected.name()+" is flush with desire and doesn't seem interested in fighting back.";
		}
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		if (!replaced) {
			return String.format("%s now entranced.\n", affected.subjectAction("are", "is"));
		} else {
			return String.format("%s already entranced.\n", affected.subjectAction("are", "is"));
		}
	}

	@Override
	public boolean mindgames(){
		return true;
	}
	
	@Override
	public float fitnessModifier () {
		return - (2 + getDuration() / 2.0f);
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public void tick(Combat c) {
		affected.loseWillpower(c, 1, 0, false, " (Trance)");
		affected.emote(Emotion.horny,15);
	}

	@Override
	public void onRemove(Combat c, Character other) {
		affected.addlist.add(new Cynical(affected));
	}

	@Override
	public boolean overrides(Status s) {
		return false;
	}

	@Override
	public Collection<Skill> allowedSkills(Combat c){
		return Arrays.asList((Skill)new Masturbate(affected),
				new Thrust(affected),
				new Piston(affected));
	}

	@Override
	public int damage(Combat c, int x) {
		affected.removelist.add(this);
		return 0;
	}

	@Override
	public double pleasure(Combat c, double x) {
		return 0;
	}

	@Override
	public int weakened(int x) {
		return 0;
	}

	@Override
	public int tempted(int x) {
		return 3;
	}

	@Override
	public int evade() {
		return 0;
	}

	@Override
	public int escape() {
		return -10;
	}

	@Override
	public int gainmojo(int x) {
		return 0;
	}

	@Override
	public int spendmojo(int x) {
		return 0;
	}
	@Override
	public int counter() {
		return -10;
	}

	@Override
	public int value() {
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Trance(newAffected);
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new Trance(null);
	}
}
