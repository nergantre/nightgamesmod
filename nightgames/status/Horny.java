package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public class Horny extends Status {
	private int duration;
	private int magnitude;
	private String source;

	public Horny(Character affected, int magnitude, int duration, String source) {
		super("Horny", affected);
		this.source = source;
		this.magnitude = magnitude;
		if(affected.has(Trait.PersonalInertia)){
			this.duration=3*duration/2;
		}
		else{
			this.duration=duration;
		}
		flag(Stsflag.horny);
	}

	public String toString() {
		return "Aroused from " + source + " ("+ magnitude +" x "+ duration+ ")";
	}
	@Override
	public String describe() {
		if(affected.human()){
			return "Your heart pounds in your chest as you try to surpress your arousal from contacting " + source + ".";
		}
		else{
			return affected.name()+" is flushed and her nipples are noticeably hard from contacting " + source + ".";
		}
	}

	@Override
	public float fitnessModifier () {
		return -Math.min(.5f, magnitude * duration / 3.0f);
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		affected.arouse(magnitude, c);
		duration--;
		if(duration==0){
			affected.removelist.add(this);
		}
		affected.emote(Emotion.horny,20);
		return 0;
	}

	@Override
	public String getVariant() {
			return source;
	}

	@Override
	public boolean overrides(Status s) {
		return false;
	}

	@Override
	public void replace(Status s) {
		assert (s instanceof Horny);
		Horny other = (Horny)s;
		assert (other.source.equals(source));
		this.duration = Math.max(other.duration, this.duration);
		this.magnitude += other.magnitude;
	}

	@Override
	public int damage(Combat c, int x) {
		return 0;
	}

	@Override
	public int pleasure(Combat c, int x) {
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
		return 0;
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
		return 0;
	}
	public boolean lingering(){
		return true;
	}

	@Override
	public int value() {
		return 0;
	}

	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Horny(newAffected, magnitude, duration, source);
	}
}
