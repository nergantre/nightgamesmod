package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
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
	public boolean requirements(Character user) {
		return user.get(Attribute.Fetish)>=15;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().dom(getSelf())&&c.getStance().reachTop(getSelf())&&((getSelf().bottom.isEmpty()&&getSelf().hasDick())||getSelf().has(Trait.strapped))&&
				!c.getStance().penetration(getSelf())&&!c.getStance().penetration(target)&&c.getStance().front(getSelf())&&!c.getStance().behind(target);
	}

	@Override
	public String describe() {
		return "Force your opponent to orally pleasure you.";
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int strength = 4 + (target.has(Trait.silvertongue) ? 4 : 0);
		if(getSelf().human()){
			if (target.has(Trait.silvertongue)) {
				c.write(getSelf(),deal(c, 0, Result.strong, target));
			} else {
				c.write(getSelf(),deal(c, 0, Result.normal, target));
			}
			target.add(c, new Shamed(target));
			getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandom("cock"), strength, c);
		} else {
			if(getSelf().has(Trait.strapped)){
				if(getSelf().has(Item.Strapon2)){
					if(target.human()){
						c.write(getSelf(),receive(c, 0,Result.upgrade, target));
					}
					target.tempt(c, getSelf(), Global.random(3));
				}
				else{
					c.write(getSelf(),receive(c,0,Result.special, target));
				}
			} else {
				getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandom("pussy"), strength, c);

			}
			target.add(c, new Shamed(target));
		}
		if (Global.random(100) < 5 + 2 * getSelf().get(Attribute.Fetish)) {
			target.add(c, new BodyFetish(target, getSelf(), "cock", .25, 10));
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
		String m = "You grab hold of "+target.name()+"'s head and push your cock into her mouth. She flushes in shame and anger, but still dutifully services you with her lips " +
				"and tongue while you thrust your hips.";
		if (modifier == Result.strong) {
			m += "<br>Her skillful tongue works its magic on your cock though, and you find yourself on the verge of orgasm way quicker than you would like.";
		}
		return m;
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 25;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		String m;
		if(modifier==Result.special){
			m = getSelf().name()+" forces her strapon cock into your mouth and fucks your face with it. It's only rubber, but your position is still humiliating. You stuggle not " +
					"to gag on the artificial member while "+getSelf().name()+" revels in her dominance.";
		}
		else if(modifier==Result.upgrade){
			m = getSelf().name()+" slightly moves forward on you, pushing her Strap-On against your lips. You try to keep your mouth closed but "+getSelf().name()+" holds your nose together, " +
					"forcing you to eventually part your lips and suck on the rubbery invader. After a few sucks, you manage to push it out, although you're still shivering " +
					"with a mix of arousal and humiliation.";
		}
		else{
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
	public String getTargetOrganType(Combat c, Character target) {
		return "mouth";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "cock";
	}
}
