package skills;

import global.Global;
import stance.StandingOver;
import status.Bound;
import status.Oiled;
import status.Stsflag;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.body.BodyPart;

import combat.Combat;
import combat.Result;

public class TentacleRape extends Skill {

	public TentacleRape(Character self) {
		super("Tentacle Rape", self);
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && !c.getStance().sub(self)&&!c.getStance().prone(self)&&!c.getStance().prone(target)&&self.canAct()&&self.body.has("tentacles")&&self.canSpend(10);
	}

	@Override
	public String describe() {
		return "Violate your opponent with your tentacles.";
	}
	BodyPart tentacles = null;
	@Override
	public void resolve(Combat c, Character target) {
		tentacles = self.body.getRandom("tentacles");
		self.spendMojo(c, 10);
		if(target.roll(this, c, accuracy())){
			if(target.nude()){
				int m = 2 + Global.random(4);
				if(target.bound()){
					if(self.human()){
						c.write(self,deal(c,0,Result.special, target));
					}
					else if(target.human()){
						c.write(self,receive(c,0,Result.special, target));
					}
					if (target.hasDick()) {
						target.body.pleasure(self, tentacles, target.body.getRandom("cock"), m, c);
						m = 2 + Global.random(4);
					}
					if (target.hasPussy()) {
						target.body.pleasure(self, tentacles, target.body.getRandom("pussy"), m, c);
						m = 2 + Global.random(4);
					}
					if (target.hasBreasts()) {
						target.body.pleasure(self, tentacles, target.body.getRandom("breasts"), m, c);
						m = 2 + Global.random(4);
					}
					if (target.body.has("ass")) {
						target.body.pleasure(self, tentacles, target.body.getRandom("ass"), m, c);
						target.emote(Emotion.horny, 10);
					}
				} else if(self.human()){
					c.write(self,deal(c,0,Result.normal, target));
					target.body.pleasure(self, tentacles, target.body.getRandom("skin"), m, c);
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.normal, target));
					target.body.pleasure(self, tentacles, target.body.getRandom("skin"), m, c);
				}
				if(!target.is(Stsflag.oiled)){
					target.add(new Oiled(target));
				}
				target.emote(Emotion.horny, 20);
			}
			else{
				if(self.human()){
					c.write(self,deal(c,0,Result.weak, target));
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.weak, target));
				}
			}
			target.add(new Bound(target,Math.min(10+3*self.get(Attribute.Fetish), 30),"tentacles"));
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.miss, target));
			}
		}
	}

	@Override
	public Skill copy(Character user) {
		return new TentacleRape(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You use your " +tentacles.describe(self) + " to snare "+target.name()+", but she nimbly dodges them.";
		}
		else if(modifier == Result.weak){
			return "You use your " +tentacles.describe(self) + " to wrap around "+target.name()+"'s arms, holding her in place.";
		}
		else if(modifier == Result.normal){
			return "You use your " +tentacles.describe(self) + " to wrap around "+target.name()+"'s naked body. They squirm against her and squirt slimy fluids on her body.";
		}
		else{
			return "You use your " +tentacles.describe(self) + " to toy with "+target.name()+"'s helpless form. The tentacles toy with her breasts and pentrate her genitals and ass.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return self.name()+" shoots her " +tentacles.describe(self) + " forward at you. You're barely able to avoid them.";
		}
		else if(modifier == Result.weak){
			return self.name()+" shoots her " +tentacles.describe(self) + " forward at you, entangling your arms and legs.";
		}
		else if(modifier == Result.normal){
			return self.name()+" shoots her " +tentacles.describe(self) + " forward at you, entangling your arms and legs. The slimy appendages " +
					"wriggle over your body and coat you in the slippery liquid.";
		}
		else{
			return self.name()+"'s " +tentacles.describe(self) + " cover your helpless body, tease your genitals, and probe your ass.";
		}
	}

}
