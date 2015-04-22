package skills;

import items.Item;
import global.Global;
import status.Shamed;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class FaceFuck extends Skill {

	public FaceFuck(Character self) {
		super("Face Fuck", self);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Fetish)>=15;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Fetish)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().dom(self)&&c.getStance().reachTop(self)&&((self.bottom.isEmpty()&&self.hasDick())||self.has(Trait.strapped))&&
				!c.getStance().penetration(self)&&!c.getStance().penetration(target)&&!c.getStance().behind(self)&&!c.getStance().behind(target);
	}

	@Override
	public String describe() {
		return "Force your opponent to orally pleasure you.";
	}

	@Override
	public void resolve(Combat c, Character target) {
		int strength = 4 + (target.has(Trait.silvertongue) ? 4 : 0);
		if(self.human()){
			if (target.has(Trait.silvertongue)) {
				c.write(self,deal(c, 0, Result.strong, target));
			} else {
				c.write(self,deal(c, 0, Result.normal, target));
			}
			target.add(new Shamed(target));
			self.body.pleasure(target, target.body.getRandom("mouth"), self.body.getRandom("cock"), strength, c);
		} else {
			if(self.has(Trait.strapped)){
				if(self.has(Item.Strapon2)){
					if(target.human()){
						c.write(self,receive(c, 0,Result.upgrade, target));
					}
					target.tempt(c, self, Global.random(3));
				}
				else{
					c.write(self,receive(c,0,Result.special, target));
				}
			} else {
				self.body.pleasure(target, target.body.getRandom("mouth"), self.body.getRandom("pussy"), strength, c);

			}
			target.add(new Shamed(target));
		}
		self.buildMojo(c, 15);
		target.loseMojo(c, 2*self.get(Attribute.Seduction));
	}

	@Override
	public Skill copy(Character user) {
		return new FaceFuck(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.debuff;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		String m = "You grab hold of "+target.name()+"'s head and push your cock into her mouth. She flushes in shamed and anger, but still dutifully services you with her lips " +
				"and tongue while you thrust your hips.";
		if (modifier == Result.strong) {
			m += "<br>Her skillful tongue works its magic on your cock though, and you find yourself on the verge of organism way quicker than you would like.";
		}
		return m;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		String m;
		if(modifier==Result.special){
			m = self.name()+" forces her strapon cock into your mouth and fucks your face with it. It's only rubber, but your position is still humiliating. You stuggle not " +
					"to gag on the artificial member while "+self.name()+" revels in her dominance.";
		}
		else if(modifier==Result.upgrade){
			m = self.name()+" slightly moves forward on you, pushing her Strap-On against your lips. You try to keep your mouth closed but "+self.name()+" holds your nose together, " +
					"forcing you to eventually part your lips and suck on the rubbery invader. After a few sucks, you manage to push it out, although you're still shivering " +
					"with a mix of arousal and humiliation.";
		}
		else{
			m = self.name()+" forces your mouth open and shoves her sizable girl-cock into it. You're momentarily overwhelmed by the strong, musky smell and the taste, but " +
					"she quickly starts moving her hips, fucking your mouth like a pussy. You feel your cheeks redden in shame, but you still do what you can to pleasure her. " +
					"She may be using you like a sex toy, but you're going to try to scrounge whatever advantage you can get.";
		}
		return m;
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
