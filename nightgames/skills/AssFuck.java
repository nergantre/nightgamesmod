package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Clothing;
import nightgames.items.Item;
import nightgames.stance.Anal;
import nightgames.stance.AnalProne;
import nightgames.status.Flatfooted;
import nightgames.status.Oiled;
import nightgames.status.Stsflag;

public class AssFuck extends Fuck {
	public AssFuck(Character self) {
		super("Ass Fuck", self, 0);
	}

	@Override
	public float priorityMod(Combat c) {
		return 0.0f + (getSelf().getMood() == Emotion.dominant ? 1.0f : 0);
	}

	public BodyPart getTargetOrgan(Character target) {
		return target.body.getRandom("ass");
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return fuckable(c, target)
				&&c.getStance().mobile(getSelf())
				&&(c.getStance().behind(getSelf())||(c.getStance().prone(target)&&!c.getStance().mobile(target)))
				&&getSelf().canAct()&&!c.getStance().penetration(getSelf())
				&&!c.getStance().penetration(target)
				&&(getTargetOrgan(target).isReady(target) || getSelf().has(Item.Lubricant) || getSelf().getArousal().percent()>50 || getSelf().has(Trait.alwaysready));
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		String premessage = "";
		if(!getSelf().bottom.empty() && getSelfOrgan().isType("cock")) {
			if (getSelf().bottom.size() == 1) {
				premessage += String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s", getSelf().bottom.get(0).name());
			} else if (getSelf().bottom.size() == 2) {
				premessage += String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s and %s", getSelf().bottom.get(0).name(), getSelf().bottom.get(1).name());
			}
		}
		if(!target.hasStatus(Stsflag.oiled)&&getSelf().getArousal().percent()>50 || getSelf().has(Trait.alwaysready)) {
			String fluids = target.hasDick() ? "copious pre-cum" : "own juices";
			if (premessage.isEmpty()) {
				premessage = "{self:subject-action:lube|lubes}";
			} else {
				premessage += " and {self:action:lube|lubes}";
			}
			premessage += " up {other:possessive} ass with {self:possessive} " + fluids + ".";
			target.add(c, new Oiled(target));
		} else if(!target.hasStatus(Stsflag.oiled)&&getSelf().has(Item.Lubricant)) {
			if (premessage.isEmpty()) {
				premessage = "{self:subject-action:lube|lubes}";
			} else {
				premessage += " and {self:action:lube|lubes}";
			}
			premessage += " up {other:possessive} ass.";
			target.add(c, new Oiled(target));
			getSelf().consume(Item.Lubricant, 1);
		}
		c.write(getSelf(), Global.format(premessage, getSelf(), target));
	
		int m = Global.random(5);
		if(getSelf().human()) {
			c.write(getSelf(),deal(c,m,Result.normal, target));
		}
		else if(target.human()){
			if (getSelf().has(Trait.strapped)&&getSelf().has(Item.Strapon2)){
				m+=3;
			}
			if (!c.getStance().behind(getSelf()) && getSelf().has(Trait.strapped)){
				c.write(getSelf(),receive(c,m,Result.upgrade, target));
			} else {
				c.write(getSelf(),receive(c,m,Result.normal, target));
			}
		}
		if(c.getStance().behind(getSelf())){
			c.setStance(new Anal(getSelf(),target));				
		} else {
			c.setStance(new AnalProne(getSelf(),target));
		}
		target.body.pleasure(getSelf(), getSelfOrgan(), getTargetOrgan(target), m, c);		
		if (!getSelf().has(Trait.strapped)) {
			getSelf().body.pleasure(target, getTargetOrgan(target), getSelfOrgan(), m / 2, c);
		}
		getSelf().emote(Emotion.dominant, 100);
		target.emote(Emotion.desperate,50);
		if(!target.has(Trait.Unflappable)) {
			target.add(c, new Flatfooted(target,1));
		}
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Seduction)>=20;
	}

	@Override
	public Skill copy(Character user) {
		return new AssFuck(user);
	}
	public int speed(){
		return 2;
	}
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.normal){
			return String.format("You make sure %s ass is sufficiently lubricated and you push your %s into her %s.",
					target.nameOrPossessivePronoun(), getSelfOrgan().describe(getSelf()), getTargetOrgan(target).describe(target));
		}
		else {
			return target.name()+"'s ass is oiled up and ready to go, but you're still too soft to penetrate her.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.upgrade){
			return getSelf().name()+" spreads your legs apart and teasingly pokes the Strap-On against your anus. Your try to struggle away but "+getSelf().name()
					+" pulls your hips closer and slowly pushes the dildo inside your ass.";
		}
		if(modifier == Result.normal){
			if(getSelf().has(Trait.strapped)){
				if(getSelf().has(Item.Strapon2)){
					
					return getSelf().name()+" aligns her Strap-On behind you and pushes it into your lubricated ass. After pushing it in completely, "
							+getSelf().name()+" pushes a button on a controller which causes the Dildo to vibrate in your ass, giving you a slight shiver.";
				}
				else{
					return getSelf().name()+" lubes up her strapon, positions herself behind you, and shoves it into your ass.";
				}
			}
			else{
				return getSelf().name()+" rubs her cock up and down your ass crack before thrusting her hips to penetrate you.";
			}
		} else {
			return getSelf().name()+" rubs her dick against your ass, but she's still flacid and can't actually penetrate you.";
		}
	}

	@Override
	public String describe() {
		return "Penetrate your opponent's ass.";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		return "ass";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "cock";
	}
}
