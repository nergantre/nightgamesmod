package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.StandingOver;
import nightgames.status.Bound;
import nightgames.status.Oiled;
import nightgames.status.Stsflag;

public class TentacleRape extends Skill {

	public TentacleRape(Character self) {
		super("Tentacle Rape", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && !c.getStance().sub(getSelf())&&!c.getStance().prone(getSelf())&&!c.getStance().prone(target)&&getSelf().canAct()&&getSelf().body.has("tentacles");
	}

	@Override
	public int getMojoCost(Combat c) {
		return 10;
	}

	@Override
	public String describe(Combat c) {
		return "Violate your opponent with your tentacles.";
	}
	BodyPart tentacles = null;
	@Override
	public boolean resolve(Combat c, Character target) {
		tentacles = getSelf().body.getRandom("tentacles");
		if(target.roll(this, c, accuracy(c))){
			if(target.mostlyNude()){
				int m = 2 + Global.random(4);
				if(target.bound()){
					if(getSelf().human()){
						c.write(getSelf(),deal(c,0,Result.special, target));
					}
					else if(target.human()){
						c.write(getSelf(),receive(c,0,Result.special, target));
					}
					if (target.hasDick()) {
						target.body.pleasure(getSelf(), tentacles, target.body.getRandom("cock"), m, c);
						m = 2 + Global.random(4);
					}
					if (target.hasPussy()) {
						target.body.pleasure(getSelf(), tentacles, target.body.getRandom("pussy"), m, c);
						m = 2 + Global.random(4);
					}
					if (target.hasBreasts()) {
						target.body.pleasure(getSelf(), tentacles, target.body.getRandom("breasts"), m, c);
						m = 2 + Global.random(4);
					}
					if (target.body.has("ass")) {
						target.body.pleasure(getSelf(), tentacles, target.body.getRandom("ass"), m, c);
						target.emote(Emotion.horny, 10);
					}
				} else if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.normal, target));
					target.body.pleasure(getSelf(), tentacles, target.body.getRandom("skin"), m, c);
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.normal, target));
					target.body.pleasure(getSelf(), tentacles, target.body.getRandom("skin"), m, c);
				}
				if(!target.is(Stsflag.oiled)){
					target.add(c, new Oiled(target));
				}
				target.emote(Emotion.horny, 20);
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.weak, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.weak, target));
				}
			}
			target.add(c, new Bound(target,Math.min(10+3*getSelf().get(Attribute.Fetish), 50),"tentacles"));
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
			return "You use your " +tentacles.describe(getSelf()) + " to snare "+target.name()+", but she nimbly dodges them.";
		}
		else if(modifier == Result.weak){
			return "You use your " +tentacles.describe(getSelf()) + " to wrap around "+target.name()+"'s arms, holding her in place.";
		}
		else if(modifier == Result.normal){
			return "You use your " +tentacles.describe(getSelf()) + " to wrap around "+target.name()+"'s naked body. They squirm against her and squirt slimy fluids on her body.";
		}
		else{
			return "You use your " +tentacles.describe(getSelf()) + " to toy with "+target.name()+"'s helpless form. The tentacles toy with her breasts and penetrate her genitals and ass.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return getSelf().name()+" shoots her " +tentacles.describe(getSelf()) + " forward at you. You're barely able to avoid them.";
		}
		else if(modifier == Result.weak){
			return getSelf().name()+" shoots her " +tentacles.describe(getSelf()) + " forward at you, entangling your arms and legs.";
		}
		else if(modifier == Result.normal){
			return getSelf().name()+" shoots her " +tentacles.describe(getSelf()) + " forward at you, entangling your arms and legs. The slimy appendages " +
					"wriggle over your body and coat you in the slippery liquid.";
		}
		else{
			return getSelf().name()+"'s " +tentacles.describe(getSelf()) + " cover your helpless body, tease your genitals, and probe your ass.";
		}
	}

}
