package skills;

import items.Clothing;
import items.Item;
import stance.Anal;
import stance.AnalProne;
import status.Flatfooted;
import status.Oiled;
import status.Stsflag;
import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;
import characters.body.BodyPart;

import combat.Combat;
import combat.Result;

public class AssFuck extends Fuck {
	public AssFuck(Character self) {
		super("Ass Fuck", self, 0);
	}

	@Override
	public float priorityMod(Combat c) {
		return 0.0f + (self.getMood() == Emotion.dominant ? 1.0f : 0);
	}

	public BodyPart getTargetOrgan(Character target) {
		return target.body.getRandom("ass");
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return fuckable(c, target)
				&&c.getStance().mobile(self)
				&&(c.getStance().behind(self)||(c.getStance().prone(target)&&!c.getStance().mobile(target)))
				&&self.canAct()&&!c.getStance().penetration(self)
				&&!c.getStance().penetration(target)
				&&(getTargetOrgan(target).isReady(target) || self.has(Item.Lubricant) || self.getArousal().percent()>50 || self.has(Trait.alwaysready));
	}

	@Override
	public void resolve(Combat c, Character target) {
		String premessage = "";
		if(!self.bottom.empty() && getSelfOrgan().isType("cock")) {
			if (self.bottom.size() == 1) {
				premessage += String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s", self.bottom.get(0).name());
			} else if (self.bottom.size() == 2) {
				premessage += String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s and %s", self.bottom.get(0).name(), self.bottom.get(1).name());
			}
		}
		if(!target.hasStatus(Stsflag.oiled)&&self.getArousal().percent()>50 || self.has(Trait.alwaysready)) {
			String fluids = target.hasDick() ? "copious pre-cum" : "own juices";
			if (premessage.isEmpty()) {
				premessage = "{self:subject-action:lube|lubes}";
			} else {
				premessage += " and {self:action:lube|lubes}";
			}
			premessage += " up {other:possessive} ass with {self:possessive} " + fluids + ".";
			target.add(new Oiled(target));
		} else if(!target.hasStatus(Stsflag.oiled)&&self.has(Item.Lubricant)) {
			if (premessage.isEmpty()) {
				premessage = "{self:subject-action:lube|lubes}";
			} else {
				premessage += " and {self:action:lube|lubes}";
			}
			premessage += " up {other:possessive} ass.";
			target.add(new Oiled(target));
			self.consume(Item.Lubricant, 1);
		}
		c.write(self, Global.format(premessage, self, target));
	
		int m = Global.random(5);
		if(self.human()) {
			c.write(self,deal(c,m,Result.normal, target));
		}
		else if(target.human()){
			if (self.has(Trait.strapped)&&self.has(Item.Strapon2)){
				m+=3;
			}
			if (!c.getStance().behind(self) && self.has(Trait.strapped)){
				c.write(self,receive(c,m,Result.upgrade, target));
			} else {
				c.write(self,receive(c,m,Result.normal, target));
			}
		}
		if(c.getStance().behind(self)){
			c.setStance(new Anal(self,target));				
		} else {
			c.setStance(new AnalProne(self,target));
		}
		target.body.pleasure(self, getSelfOrgan(), getTargetOrgan(target), m, c);		
		if (!self.has(Trait.strapped)) {
			self.body.pleasure(target, getTargetOrgan(target), getSelfOrgan(), m / 2, c);
		}
		self.buildMojo(c, 25);
		self.emote(Emotion.dominant, 100);
		target.emote(Emotion.desperate,50);
		if(!target.has(Trait.Unflappable)) {
			target.add(new Flatfooted(target,1));
		}
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
					target.nameOrPossessivePronoun(), getSelfOrgan().describe(self), getTargetOrgan(target).describe(target));
		}
		else {
			return target.name()+"'s ass is oiled up and ready to go, but you're still too soft to penetrate her.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.upgrade){
			return self.name()+" spreads your legs apart and teasingly pokes the Strap-On against your anus. Your try to struggle away but "+self.name()
					+" pulls your hips closer and slowly pushes the dildo inside your ass.";
		}
		if(modifier == Result.normal){
			if(self.has(Trait.strapped)){
				if(self.has(Item.Strapon2)){
					
					return self.name()+" aligns her Strap-On behind you and pushes it into your lubricated ass. After pushing it in completely, "
							+self.name()+" pushes a button on a controller which causes the Dildo to vibrate in your ass, giving you a slight shiver.";
				}
				else{
					return self.name()+" lubes up her strapon, positions herself behind you, and shoves it into your ass.";
				}
			}
			else{
				return self.name()+" rubs her cock up and down your ass crack before thrusting her hips to penetrate you.";
			}
		} else {
			return self.name()+" rubs her dick against your ass, but she's still flacid and can't actually penetrate you.";
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
}
