package nightgames.status;

import java.util.Collection;
import java.util.Collections;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.JSONUtils;
import nightgames.skills.Skill;
import nightgames.skills.Suckle;

public class Suckling extends DurationStatus {
	private Suckle skill;

	public Suckling(Character affected, Character opponent, int duration) {
		super("Suckling", affected, duration);
		skill = new Suckle(opponent);
		flag(Stsflag.suckling);
	}

	@Override
	public Collection<Skill> allowedSkills(Combat c){
		return Collections.singleton((Skill)new Suckle(affected));
	}

	@Override
	public String initialMessage(Combat c, boolean replaced) {
		return String.format("%s fighting an urge to drink from %s nipples.\n", affected.subjectAction("are", "is"), skill.getSelf().nameOrPossessivePronoun());
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "You feel an irresistable urge to suck on her nipples.";
		}
		else{
			return affected.name()+" is looking intently at your breasts.";
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
	public int regen(Combat c) {
		super.regen(c);
		affected.emote(Emotion.horny,15);
		return 0;
	}

	@Override
	public int damage(Combat c, int x) {
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
		return 0;
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
	public void onRemove(Combat c, Character other) {
		affected.addlist.add(new Cynical(affected));
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Suckling(newAffected, newOther, getDuration());
	}

	@SuppressWarnings("unchecked")
	public JSONObject saveToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", getClass().getSimpleName());
		obj.put("duration", getDuration());
		return obj;
	}

	public Status loadFromJSON(JSONObject obj) {
		return new Suckling(null, null, JSONUtils.readInteger(obj, "duration"));
	}
}
