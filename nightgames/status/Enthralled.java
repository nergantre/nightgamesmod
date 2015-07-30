package nightgames.status;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.State;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Enthralled extends Status {

	private int duration;
	private int timesRefreshed;
	public Character master;
	
	public Enthralled(Character self,Character master, int duration) {
		super("Enthralled", self);
		if (self.has(Trait.PersonalInertia)) {
			duration = duration * 3 / 2;
		}
		this.duration = duration;
		timesRefreshed = 0;
		this.master = master;
		flag(Stsflag.enthralled);
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
		this.duration += Math.max(1, other.duration - timesRefreshed);
		timesRefreshed += 1;
	}

	@Override
	public boolean mindgames(){
		return true;
	}
	
	@Override
	public float fitnessModifier () {
		return -duration * 5;
	}
	
	@Override
	public int mod(Attribute a) {
		if (a == Attribute.Perception)
			return -5;
		return -2;
	}

	@Override
	public int regen(Combat c) {
		duration--;
		affected.spendMojo(c, 5);
		if (duration <= 0|| affected.check(Attribute.Cunning, master.get(Attribute.Seduction)/2 +master.get(Attribute.Arcane)/2 + master.get(Attribute.Dark)/2 + 10+10*(duration- timesRefreshed))) {
			affected.removelist.add(this);
			affected.addlist.add(new Cynical(affected));
			if (affected.human() && affected.state != State.combat)
				Global.gui().message("Everything around you suddenly seems much clearer,"
						+ " like a lens snapped into focus. You don't really remember why"
						+ " you were heading in the direction you where...");
				
		}
		affected.emote(Emotion.horny,15);
		return 0;
	}

	@Override
	public int damage(Combat c, int paramInt) {
		return 0;
	}

	@Override
	public int pleasure(Combat c, int paramInt) {
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Status instance(Character newAffected, Character newOther) {
		return new Enthralled(newAffected, newOther, duration);
	}
}
