package skills;

import stance.Stance;
import items.Clothing;
import items.Item;
import global.Global;
import global.Modifier;
import characters.Character;
import characters.Emotion;

import combat.Combat;
import combat.Result;

public class Strapon extends Skill {

	public Strapon(Character self) {
		super("Strap On", self);
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().mobile(self)&&!c.getStance().prone(self)&&self.pantsless()&&(self.has(Item.Strapon)||self.has(Item.Strapon2))
				&&!self.hasDick()&&!c.getStance().penetration(self)&&!c.getStance().penetration(target)&&(!self.human()||Global.getMatch().condition!=Modifier.notoys)
				&&c.getStance().enumerate()!=Stance.facesitting;
	}

	@Override
	public String describe() {
		return "Put on the strapon dildo";
	}

	@Override
	public void resolve(Combat c, Character target) {
		self.bottom.push(Clothing.strapon);
		if(self.human()){
			c.write(self,deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		self.buildMojo(c, 10);
		target.loseMojo(c, 10);
		target.emote(Emotion.nervous, 10);
		self.emote(Emotion.confident, 30);
		self.emote(Emotion.dominant, 40);
	}

	@Override
	public Skill copy(Character user) {
		return new Strapon(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You put on a strap on dildo.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" straps on a thick rubber cock and grins at you in a way that makes you feel a bit nervous.";
	}

}
