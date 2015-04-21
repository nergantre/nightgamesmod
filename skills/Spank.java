package skills;

import status.Shamed;
import status.Stsflag;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Spank extends Skill {

	public Spank(Character self) {
		super("Spank", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.pantsless()&&!c.getStance().prone(target)&&c.getStance().reachBottom(self)&&self.canAct();
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.has(Trait.disciplinarian)){
			if(self.human()){
				c.write(self,deal(c,0,Result.special, target));
			}
			else if(target.human()){			
				c.write(self,receive(c,0,Result.special, target));
			}
			if(Global.random(10)>=5||!target.is(Stsflag.shamed)&&self.canSpend(5)){
				target.add(new Shamed(target));
				self.spendMojo(5);
				target.emote(Emotion.angry,10);
				target.emote(Emotion.nervous,15);
			}
			if(target.has(Trait.achilles)){
				target.pain(c, 4);
			}
			target.pain(c, Global.random(6+target.get(Attribute.Perception)/2)+3);
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.normal, target));
			}
			else if(target.human()){			
				c.write(self,receive(c,0,Result.normal, target));
			}
			target.pain(c, Global.random(6)+3);
		}
		target.emote(Emotion.angry,25);
		target.emote(Emotion.nervous,15);
		target.calm(6);
		self.buildMojo(10);
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Seduction)>=8;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Seduction)>=8;
	}

	@Override
	public Skill copy(Character user) {
		return new Spank(user);
	}
	public int speed(){
		return 8;
	}
	public int accuracy(){
		return 4;
	}
	public Tactics type(Combat c) {
		return Tactics.damage;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You try to spank "+target.name()+", but she dodges away.";
		}
		if(modifier==Result.special){
			return "You bend "+target.name()+" over your knee and spank her, alternating between hitting her soft butt check and her sensitive pussy.";
		}
		else{
			return "You spank "+target.name()+" on her naked butt cheek.";
		}
		
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return self.name()+" aims a slap at your ass, but you dodge it.";
		}
		if(modifier==Result.special){
			return self.name()+" bends you over like a misbehaving child and spanks your ass twice. The third spank aims lower and connects solidly with your ballsack, " +
					"injuring your manhood along with your pride.";
		}
		else{
			return self.name()+" lands a stinging slap on your bare ass.";
		}
		
	}

	@Override
	public String describe() {
		return "Slap opponent on the ass";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
