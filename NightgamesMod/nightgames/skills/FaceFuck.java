package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.Shamed;

public class FaceFuck extends Skill {

	public FaceFuck(Character self) {
		super("Face Fuck", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Fetish)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().dom(getSelf())&&c.getStance().reachTop(getSelf())&&((getSelf().crotchAvailable()&&getSelf().hasDick())||getSelf().has(Trait.strapped))&&
				!c.getStance().inserted(getSelf())&&c.getStance().front(getSelf())&&!c.getStance().behind(target);
	}

	@Override
	public String describe(Combat c) {
		return "Force your opponent to orally pleasure you.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		Result res = Result.normal;
		int selfDamage = 4;
		int targetDamage = 0;
		BodyPart targetMouth = target.body.getRandom("mouth");
		if (target.has(Trait.silvertongue)) {
			res = Result.reverse;
			selfDamage *= 2;
		}
		if (getSelf().has(Trait.strapped)) {
			if(getSelf().has(Item.Strapon2)){
				res = Result.upgrade;
				targetDamage += 6;
			} else {
				res = Result.strapon;
			}
			selfDamage = 0;
		}
		if (targetMouth.isErogenous()) {
			targetDamage += 10;
		}
		selfDamage += Global.random(selfDamage * 2 / 3);
		targetDamage += Global.random(targetDamage * 2 / 3);

		if(getSelf().human()){
			c.write(getSelf(),deal(c, 0, res, target));			
		} else {
			c.write(getSelf(),receive(c,0,res, target));
		}
		target.add(c, new Shamed(target));

		if (selfDamage > 0) {
			getSelf().body.pleasure(target, targetMouth, getSelf().body.getRandom("cock"), selfDamage, c);
		}
		if (targetDamage > 0) {
			target.body.pleasure(target, getSelf().body.getRandomInsertable(), targetMouth, targetDamage, c);
		}
		if (Global.random(100) < 5 + 2 * getSelf().get(Attribute.Fetish) && !getSelf().has(Trait.strapped)) {
			target.add(c, new BodyFetish(target, getSelf(), "cock", .25));
		}
		target.loseMojo(c, 2*getSelf().get(Attribute.Seduction));
		target.loseWillpower(c, 5);
		return true;
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
		String m = "";
		if (modifier == Result.strapon || modifier == Result.upgrade) {
			m = "You grab hold of "+target.name()+"'s head and push your cock into her mouth. She flushes in shame and anger, but still dutifully services you with her lips " +
					"and tongue while you thrust your hips.";
			if (modifier == Result.upgrade) {
				m += "<br>Additionally, your upgraded vibrocock thoroughly stimulates her throat.";
			}
		} else {
			if (target.body.getRandom("mouth").isErogenous()) {
				m = "You grab hold of "+target.name()+"'s head and push your cock into her mouth. What you find inside is unexpected, though. " + target.name()
+					+ " has transformed her mouth into a second female genitalia; its soft hot walls, its ridges and folds slide across your dick delightfully as you thrust into her.";
				if (modifier == Result.reverse) {
					m += "<br>Her skillful tongue works its magic on your cock while you're fucking her mouth pussy, and you find yourself on the verge of orgasm way quicker than you would like.";
				}
			} else {
				m = "You grab hold of "+target.name()+"'s head and push your cock into her mouth. She flushes in shame and anger, but still dutifully services you with her lips " +
						"and tongue while you thrust your hips.";
				if (modifier == Result.reverse) {
					m += "<br>Her skillful tongue works its magic on your cock though, and you find yourself on the verge of orgasm way quicker than you would like.";
				}				
			}
		}
		return m;
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 50;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		String m;
		if(modifier==Result.strapon){
			m = getSelf().name()+" forces her strapon cock into your mouth and fucks your face with it. It's only rubber, but your position is still humiliating. You struggle not " +
					"to gag on the artificial member while "+getSelf().name()+" revels in her dominance.";
		}
		else if(modifier==Result.upgrade){
			m = getSelf().name()+" slightly moves forward on you, pushing her strapon against your lips. You try to keep your mouth closed but "+getSelf().name()+" pinches your nose together, " +
+					"and pushes in the rubbery invader as you gasp for air. After a few sucks, you manage to push it out, although you're still shivering " +
					"with a mix of arousal and humiliation.";
		} else if (target.body.getRandom("mouth").isErogenous()) {
			m = getSelf().name()+" forces your mouth open and shoves her sizable girl-cock into it. You're momentarily overwhelmed by the strong, musky smell and the taste, but " +
					"she quickly starts moving her hips, fucking your mouth. However, your modified oral orifice was literally designed to squeeze cum; soon " + getSelf().name()
					+ " finds herself ramming with little more than her own enjoyment in mind.";
		} else {
			m = getSelf().name()+" forces your mouth open and shoves her sizable girl-cock into it. You're momentarily overwhelmed by the strong, musky smell and the taste, but " +
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
