package skills;

import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class NakedBloom extends Skill {

	public NakedBloom(Character self) {
		super("Naked Bloom", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Arcane)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().prone(getSelf())&&!target.nude()&&getSelf().canSpend(20);
	}

	@Override
	public String describe() {
		return "Cast a spell to transform your opponents clothes into flower petals: 20 Mojo";
	}

	@Override
	public void resolve(Combat c, Character target) {
		getSelf().spendMojo(c, 20);
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
			c.write(target,target.nakedLiner());
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		target.nudify();
		target.emote(Emotion.nervous, 10);
	}

	@Override
	public Skill copy(Character user) {
		return new NakedBloom(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You chant a short spell and turn "+target.name()+"'s clothes into a burst of flowers. The cloud of flower petals flutters to the ground, exposing her nude body.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" mumbles a spell and you're suddenly surrounded by an eruption of flower petals. As the petals settle, you realize you've been stripped completely " +
				"naked.";
	}

}
