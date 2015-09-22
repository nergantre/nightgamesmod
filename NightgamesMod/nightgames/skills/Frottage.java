package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.BodyFetish;
import nightgames.status.Stsflag;

public class Frottage extends Skill{

	public Frottage(Character self) {
		super("Frottage", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Seduction)>=26;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()&&c.getStance().mobile(getSelf())&&!c.getStance().sub(getSelf())&&!c.getStance().havingSex()&&target.crotchAvailable()&&((getSelf().hasDick()&&getSelf().crotchAvailable())||getSelf().has(Trait.strapped));
	}

	@Override
	public String describe(Combat c) {
		return "Rub yourself against your opponent";
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 15;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int m = 6 + Global.random(8);
		BodyPart receiver = target.hasDick() ? target.body.getRandomCock() : target.body.getRandomPussy(); 
		BodyPart dealer = getSelf().hasDick() ? getSelf().body.getRandomCock() : getSelf().body.getRandomPussy(); 
		if(getSelf().human()){
			if(target.hasDick()){
				c.write(getSelf(),deal(c,m,Result.special, target));
			}
			else{
				c.write(getSelf(),deal(c,m,Result.normal, target));
			}
		}
		else if(getSelf().has(Trait.strapped)){
			if(target.human()){
				c.write(getSelf(),receive(c,m,Result.special, target));
			}
			target.loseMojo(c, 10);
			dealer = null;
		} else {
			if(target.human()){
				c.write(getSelf(),receive(c,m,Result.normal, target));
			}
		}

		if (dealer != null) {
			getSelf().body.pleasure(target, receiver, dealer, m / 2, c);
		}
		target.body.pleasure(getSelf(), dealer, receiver, m, c);
		if (Global.random(100) < 15 + 2 * getSelf().get(Attribute.Fetish)) {
			target.add(c, new BodyFetish(target, getSelf(), "cock", .25));
		}
		getSelf().emote(Emotion.horny, 15);
		target.emote(Emotion.horny, 15);
		return true;
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
			return getSelf().name()+" thrusts her hips to prod your delicate jewels with her strapon dildo. As you flinch and pull your hips back, she presses the toy against your cock, teasing your sensitive parts.";
		} else if (getSelf().hasDick()){
			return getSelf().name()+" pushes her girl-cock against your the sensitive head of you member, dominating your manhood.";
		} else {
			return getSelf().name()+" pushes your cock against her soft thighs, rubbing your shaft up against her nether lips.";
		}
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
