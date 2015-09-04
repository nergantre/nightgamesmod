package nightgames.skills;

import java.util.ArrayList;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.GenericBodyPart;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.global.Modifier;

public class Masturbate extends Skill {
	public Masturbate(Character self) {
		super("Masturbate", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().penetration(getSelf())&&Global.getMatch().condition!=Modifier.norecovery;
	}
	
	@Override
	public float priorityMod(Combat c) {
		return 0;
	}

	public BodyPart getSelfOrgan() {
		return getSelf().body.getRandom("hands");
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

	private BodyPart withO = Body.nonePart;
	private BodyPart targetO = Body.nonePart;

	@Override
	public int getMojoBuilt(Combat c) {
		return 25;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		withO = getSelfOrgan();
		targetO = getTargetOrgan(getSelf());
		
		if(getSelf().human()){
			if(getSelf().getArousal().get()<=15){
				c.write(getSelf(),deal(c,0,Result.weak, target));
			}
			else{
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		int pleasure;

		pleasure = getSelf().body.pleasure(getSelf(), withO, targetO, 25, c);
		getSelf().emote(Emotion.horny, pleasure);
		return true;
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
			return "You tease your own asshole.";
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
	public String describe(Combat c) {
		return "Raise your own arousal and boosts your mojo";
	}

	public String getTargetOrganType(Combat c, Character target) {
		return targetO.getType();
	}
	public String getWithOrganType(Combat c, Character target) {
		return "hands";
	}
}
