package skills;

import stance.StandingOver;
import characters.Attribute;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class Dominate extends Skill {

	public Dominate(Character self) {
		super("Dominate", self, 3);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Dark)>=9;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Dark)>=9;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && !c.getStance().sub(self)&&!c.getStance().prone(self)&&!c.getStance().prone(target)&&self.canAct();
	}

	@Override
	public String describe() {
		return "Overwhelm your opponent to force her to lie down: 10 Arousal";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.arouse(10, c);
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		c.setStance(new StandingOver(self,target));
		self.emote(Emotion.dominant, 20);
		target.emote(Emotion.nervous,20);
	}

	@Override
	public Skill copy(Character user) {
		return new Dominate(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You take a deep breathe, gathering dark energy into your lungs. You expend the power to command "+target.name()+" to submit. The demonic command renders her " +
				"unable to resist and she drops to floor, spreading her legs open to you. As you approach, she comes to her senses and quickly closes her legs. Looks like her " +
				"will is still intact.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" forcefully orders you to \"Kneel!\" Your body complies without waiting for your brain and you drop to your knees in front of her. She smiles and " +
				"pushes you onto your back. By the time you break free of her suggestion, you're flat on the floor with her foot planted on your chest.";
	}

}
