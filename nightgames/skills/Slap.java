package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Shamed;

public class Slap extends Skill {

	public Slap(Character self) {
		super("Slap", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().reachTop(getSelf())&&getSelf().canAct()&&!getSelf().has(Trait.softheart)&&c.getStance().front(getSelf());
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return getSelf().has(Trait.pimphand) ? 15 : 5;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy(c))){
			if(getSelf().get(Attribute.Animism)>=8){
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.special, target));			
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.special, target));
				}
				if(getSelf().has(Trait.pimphand)){
					target.pain(c, Global.random((16*getSelf().getArousal().percent())/100)+getSelf().get(Attribute.Power)/2);
					target.emote(Emotion.nervous, 40);
					target.emote(Emotion.angry, 30);
				}
				else{
					target.pain(c, Global.random((12*getSelf().getArousal().percent())/100+1)+getSelf().get(Attribute.Power)/2);
					target.emote(Emotion.nervous, 25);
					target.emote(Emotion.angry, 30);
				}
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.normal, target));			
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.normal, target));
				}
				if(getSelf().has(Trait.pimphand)){
					target.pain(c, Global.random(8)+5+target.get(Attribute.Perception));
					target.emote(Emotion.nervous, 20);
					target.emote(Emotion.angry, 30);
				}
				else{
					target.pain(c, Global.random(5)+4);
					target.emote(Emotion.nervous, 10);
					target.emote(Emotion.angry, 30);
				}
			}
			
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return !user.has(Trait.softheart)&&user.get(Attribute.Power)>=7;
	}

	@Override
	public Skill copy(Character user) {
		return new Slap(user);
	}
	public int speed(){
		return 8;
	}
	public Tactics type(Combat c) {
		return Tactics.damage;
	}
	public String getLabel(Combat c){
		if(getSelf().get(Attribute.Animism)>=8){
			return "Tiger Claw";
		}
		else{
			return "Slap";
		}
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return target.name()+" avoids your slap.";
		}
		else if(modifier==Result.special){
			return "You channel your bestial power and strike"+target.name()+" with a solid open hand strike.";
		}
		else{
			return "You slap "+target.name()+"'s cheek; not hard enough to really hurt her, but enough to break her concentration.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return getSelf().name()+" tries to slap you but you catch her wrist.";
		}
		else if(modifier==Result.special){
			return getSelf().name()+"'s palm hits you in a savage strike that makes your head ring.";
		}
		else{
			return getSelf().name()+" slaps you across the face, leaving a stinging heat on your cheek.";
		}
	}

	@Override
	public String describe() {
		return "Slap opponent across the face";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
