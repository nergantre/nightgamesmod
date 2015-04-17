package skills;

import java.util.ArrayList;

import global.Global;
import global.Modifier;
import characters.Character;
import characters.Emotion;
import characters.body.BodyPart;
import characters.body.PussyPart;

import combat.Combat;
import combat.Result;

public class Masturbate extends Skill {

	public Masturbate(Character self) {
		super("Masturbate", self);
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return self.canAct()&&c.getStance().mobile(self)&&!c.getStance().penetration(self)&&Global.getMatch().condition!=Modifier.norecovery;
	}
	
	@Override
	public float priorityMod(Combat c) {
		return 0;
	}

	public BodyPart getSelfOrgan() {
		return self.body.getRandom("hands");
	}

	public BodyPart getTargetOrgan(Character target) {
		ArrayList<BodyPart> parts = new ArrayList<BodyPart>();
		BodyPart cock = target.body.getRandomCock();
		BodyPart pussy = target.body.getRandomPussy();
		BodyPart ass = null;
		if (cock == null || pussy != null) {
			ass = target.body.getRandom("ass");
		}
		if (cock != null) {parts.add(cock);}
		if (pussy != null) {parts.add(pussy);}
		if (ass != null) {parts.add(ass);}

		return parts.get(Global.random(parts.size()));
	}

	private BodyPart withO = null;
	private BodyPart targetO = null;

	@Override
	public void resolve(Combat c, Character target) {
		withO = getSelfOrgan();
		targetO = getTargetOrgan(self);
		
		if(self.human()){
			if(self.getArousal().get()<=15){
				c.write(self,deal(c,0,Result.weak, target));
			}
			else{
				c.write(self,deal(c,0,Result.normal, target));
			}
		}
		else if(target.human()){
			c.write(self,receive(c,0,Result.normal, target));
		}
		int mojo;

		mojo = self.body.pleasure(self, withO, targetO, 25, c);
		self.buildMojo(mojo);
		self.emote(Emotion.horny, mojo);
	}

	@Override
	public Skill copy(Character user) {
		return new Masturbate(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.misc;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (targetO == null) {
			return "You play with yourself, building up your own arousal.";			
		} if (targetO.isType("cock")) {
			if(modifier == Result.weak){
				return "You take hold of your flaccid dick, tugging and rubbing it into a full erection.";
			}
			else{
				return "You jerk off, building up your own arousal.";
			}
		} else if (targetO.isType("pussy")) {
			return "You tease your own labia and finger yourself.";
		} else if (targetO.isType("ass")) {
			return "You tease your own asshole and rim yourself.";
		} else {
			return "You play with yourself, building up your own arousal.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if (targetO == null) {
			return "She starts playing with herself, building up her own arousal.";
		} if (targetO.isType("cock")) {
			if(modifier == Result.weak){
				return "She takes hold of her flaccid dick, tugging and rubbing it into a full erection.";
			}
			else{
				return "She jerks off, building up your own arousal.";
			}
		} else if (targetO.isType("pussy")) {
			return "She slowly teases her own labia and starts playing with herself.";
		} else if (targetO.isType("ass")) {
			return "She teases her own asshole and sticks a finger in.";
		} else {
			return "She starts playing with herself, building up her own arousal.";
		}
	}

	@Override
	public String describe() {
		return "Raise your own arousal and boosts your mojo";
	}

}
