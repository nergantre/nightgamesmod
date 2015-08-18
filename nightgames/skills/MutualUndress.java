package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;

public class MutualUndress extends Skill {

	public MutualUndress(Character self) {
		super("Tempt Undress", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Seduction) > 50;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		if(getSelf().canAct()&&c.getStance().mobile(getSelf())&&!getSelf().nude()&&!target.nude()) {
			return true;
		}
		return false;
	}

	@Override
	public int getMojoCost(Combat c) {
		return 30;
	}

	@Override
	public float priorityMod(Combat c) {
		return c.getStance().dom(getSelf()) ? 2.0f : 0.0f;
	}
	
	@Override
	public String describe() {
		return "Tempt opponent to remove clothes by removing your own";
	}

	@Override
	public boolean resolve(Combat c, Character target) {	
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.strong, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.strong, target));
		}
		getSelf().undress(c);
		target.undress(c);
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new MutualUndress(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "During a brief respite in the fight as "+target.name()+" is catching her breath, you ask if we can finish the fight naked. " +
				"Without waiting for an answer, you slowly strip off all your clothing." + 
				"By the time you finish, you find that she has also stripped naked while panting with arousal.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" asks for a quick time out and starts sexily slipping her her clothes off. Although there are no time outs in the rules, you can't help staring " +
				"at the seductive display until she finishes with a cute wiggle of her naked ass. She asks you if you want to join her in feeling good, and before you realize it " +
				"she's got you naked as well.";
	}
}
