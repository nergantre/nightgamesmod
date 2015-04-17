package skills;

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
		super("Ass Fuck", self);
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
				&&(getTargetOrgan(target).isReady(target) || self.has(Item.Lubricant) || self.getArousal().percent()>50 || self.has(Trait.alwaysready));
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(!target.hasStatus(Stsflag.oiled)&&self.getArousal().percent()>50 || self.has(Trait.alwaysready)) {
			String fluids = target.hasDick() ? "copious pre-cum" : "own juices";
			c.write(self, Global.capitalizeFirstLetter(self.subject()) + " lubes up your ass with " + self.possessivePronoun() + " " + fluids + ".");
			target.add(new Oiled(target));
		}
		if(!target.hasStatus(Stsflag.oiled)&&self.has(Item.Lubricant)) {
			c.write(self, Global.capitalizeFirstLetter(self.subject()) + " lubes up your ass.");
			target.add(new Oiled(target));
			self.consume(Item.Lubricant, 1);
		}
	
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
		self.buildMojo(25);
		self.emote(Emotion.dominant, 100);
		target.emote(Emotion.desperate,50);
		if(!target.has(Trait.Unflappable)) {
			target.add(new Flatfooted(target,1));
		}
	}

	@Override
	public boolean requirements() {
		return self.getPure(Attribute.Seduction)>=20;
	}

	@Override
	public boolean requirements(Character user) {
		return user.getPure(Attribute.Seduction)>=20;
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
}
