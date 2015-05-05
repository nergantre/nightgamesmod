package skills;

import global.Global;
import status.Stsflag;
import combat.Combat;
import combat.Result;

import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;
import characters.body.BodyPart;

public class Frottage extends Skill{

	public Frottage(Character self) {
		super("Frottage", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Seduction)>=26;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().mobile(self)&&!c.getStance().sub(self)&&!c.getStance().penetration(self)&&target.pantsless()&&((self.hasDick()&&self.pantsless())||self.has(Trait.strapped));
	}

	@Override
	public String describe() {
		return "Rub yourself against your opponent";
	}

	@Override
	public void resolve(Combat c, Character target) {
		int m = 6 + Global.random(8);
		BodyPart receiver = target.hasDick() ? target.body.getRandomCock() : target.body.getRandomPussy(); 
		BodyPart dealer = self.hasDick() ? self.body.getRandomCock() : self.body.getRandomPussy(); 
		if(self.human()){
			if(target.hasDick()){
				c.write(self,deal(c,m,Result.special, target));
			}
			else{
				c.write(self,deal(c,m,Result.normal, target));
			}
		}
		else if(self.has(Trait.strapped)){
			if(target.human()){
				c.write(self,receive(c,m,Result.special, target));
			}
			target.buildMojo(c, -10);
			dealer = null;
		} else {
			if(target.human()){
				c.write(self,receive(c,m,Result.normal, target));
			}
		}

		if (dealer != null) {
			self.body.pleasure(target, receiver, dealer, m / 2, c);
		}
		target.body.pleasure(self, dealer, receiver, m, c);

		self.buildMojo(c, 20);
		self.emote(Emotion.horny, 15);
		target.emote(Emotion.horny, 15);
	}

	@Override
	public Skill copy(Character user) {
		return new Frottage(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.special){
			return "You tease "+target.name()+"'s penis with your own, dueling her like a pair of fencers.";
		}
		else{
			return "You press your hips against "+target.name()+"'s legs, rubbing her nether lips and clit with your dick.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.special){
			return self.name()+" thrusts her hips to prod your delicate jewels with her strapon dildo. As you flinch and pull your hips back, she presses the toy against your cock, teasing your sensitive parts.";
		}
		else{
			return self.name()+" pushes her girl-cock against your the sensitive head of you member, dominating your manhood.";
		}
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
