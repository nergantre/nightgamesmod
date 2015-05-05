package skills;

import stance.ReverseMount;
import stance.SixNine;
import stance.Stance;
import status.Enthralled;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;
import characters.body.PussyPart;

import combat.Combat;
import combat.Result;

public class Cunnilingus extends Skill {

	public Cunnilingus(Character self) {
		super("Lick Pussy", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return target.pantsless()&&target.hasPussy()&&c.getStance().oral(self)&&self.canAct()&&!c.getStance().penetration(self);
	}

	@Override
	public float priorityMod(Combat c) {
		return (self.has(Trait.silvertongue) ? 1 : 0);
	}

	@Override
	public void resolve(Combat c, Character target) {
		PussyPart targetPussy = target.body.getRandomPussy();
		Result results = Result.normal;
		int m = 4 + Global.random(8);
		if(self.has(Trait.silvertongue)) {
			m += 4;
		}
		int i = 0;
		if(c.getStance().enumerate()!= Stance.facesitting && !target.roll(this, c, accuracy())) {
			results = Result.miss;
		} else { 
			if (target.has(Trait.entrallingjuices) && Global.random(4) == 0 && !target.wary()) {
				i = -2;
				this.self.add(new Enthralled(self,target, 3));
			} else if (target.has(Trait.lacedjuices)){
				i = -1;
				this.self.tempt(c, target, 5);
			}
			if (c.getStance().enumerate()== Stance.facesitting) {
				results = Result.reverse;
			}
		}
		if(self.human()){
			c.write(self,deal(c,i,results, target));
		}
		else if(target.human()){
			c.write(self,receive(c,i,results, target));
		}
		if (results != Result.miss) {
			if (results == Result.reverse) {
				self.buildMojo(c, 5);
			} else {
				target.buildMojo(c, 10);
			}
			if(ReverseMount.class.isInstance(c.getStance())){
				c.setStance(new SixNine(self,target));
			}
			target.body.pleasure(self, self.body.getRandom("mouth"), target.body.getRandom("pussy"), m, c);
		}
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Seduction)>=10;
	}

	@Override
	public Skill copy(Character user) {
		return new Cunnilingus(user);
	}
	public int speed(){
		return 2;
	}
	public int accuracy(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You try to eat out "+target.name()+", but she pushes your head away.";
		}
		if(target.getArousal().get()<10){
			return "You run your tongue over "+target.name()+"'s dry vulva, lubricating it with your saliva.";
		}
		if (modifier == Result.special) {
			return "Your skilled tongue explores "
					+ target.name()
					+ "'s pussy, finding and pleasuring her more sensitive areas. You frequently tease her clitoris until she "
					+ "can't surpress her pleasured moans."
					+ (damage == -1 ? " Under your skilled minestrations, her juices flow freely, and they unmistakably"
							+ " have their effect on you"
							: "")
					+ (damage == -2 ? " You feel a strange pull on you mind,"
							+ " somehow she has managed to enthrall you with her juices"
							: "");
		}
		if (modifier == Result.reverse) {
			return "Your resign yourself to lapping at "
					+ target.nameOrPossessivePronoun()
					+ " pussy, as she dominates your face with her ass."
					+ (damage == -1 ? " Under your skilled minestrations, her juices flow freely, and they unmistakably"
							+ " have their effect on you"
							: "")
					+ (damage == -2 ? " You feel a strange pull on you mind,"
							+ " somehow she has managed to enthrall you with her juices"
							: "");
		}
		if (target.getArousal().percent() > 80) {
			return "You relentlessly lick and suck the lips of "
					+ target.name()
					+ "'s pussy as she squirms in pleasure. You let up just for a second before kissing her"
					+ " swollen clit, eliciting a cute gasp."
					+ (damage == -1 ? " The highly aroused succubus' vulva is dripping with her "
							+ "aphrodisiac juices and you consume generous amounts of them"
							: "")
					+ (damage == -2 ? " You feel a strange pull on you mind,"
							+ " somehow she has managed to enthrall you with her juices"
							: "");
		}
		int r = Global.random(3);
		if (r == 0) {
			return "You gently lick "
					+ target.name()
					+ "'s pussy and sensitive clit."
					+ (damage == -1 ? " As you drink down her juices, they seem to flow "
							+ "straight down to your crotch, lighting fires when they arrive"
							: "")
					+ (damage == -2 ? " You feel a strange pull on you mind,"
							+ " somehow she has managed to enthrall you with her juices"
							: "");
		}
		if (r == 1) {
			return "You thrust your tongue into "
					+ target.name()
					+ "'s hot vagina and lick the walls of her pussy."
					+ (damage == -1 ? " Your tongue tingles with her juices,"
							+ " clouding your mind with lust"
							: "")
					+ (damage == -2 ? " You feel a strange pull on you mind,"
							+ " somehow she has managed to enthrall you with her juices"
							: "");
		}
		return "You locate and capture "
				+ target.name()
				+ "'s clit between your lips and attack it with your tongue"
				+ (damage == -1 ? " Her juices taste wonderful and you cannot"
						+ " help but desire more" : "")
				+ (damage == -2 ? " You feel a strange pull on you mind,"
						+ " somehow she has managed to enthrall you with her juices"
						: "");
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character attacker) {
		if (modifier == Result.miss) {
			return self.name() + " tries to tease your cunt with her mouth, but you push her face away from your box.";
		} else if (modifier == Result.special) {
			return self.nameOrPossessivePronoun() + " skilled tongue explores your pussy, finding and pleasuring your more sensitive areas. " +
					"She repeatedly attacks your clitoris until you can't surpress your pleasured moans."
					+ (damage == -1 ? " Your aphrodisiac juices manages to arouse her as much as she aroused you." : "")
					+ (damage == -2 ? " Your tainted juices quickly reduces her into a willing thrall."
							: "");
		} else if (modifier == Result.reverse) {
			return self.name() + " obediently laps at your pussy as you sit on her face."
					+ (damage == -1 ? " Your aphrodisiac juices manages to arouse her as much as she aroused you." : "")
					+ (damage == -2 ? " Your tainted juices quickly reduces her into a willing thrall."
							: "");
		}
		return self.name() + " locates and captures your clit between her lips and attacks it with her tongue."
				+ (damage == -1 ? " Your aphrodisiac juices manages to arouse her as much as she aroused you." : "")
				+ (damage == -2 ? " Your tainted juices quickly reduces her into a willing thrall."
						: "");
	}

	@Override
	public String describe() {
		return "Perfom cunnilingus on opponent";
	}
}
