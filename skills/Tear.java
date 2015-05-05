package skills;

import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Tear extends Skill {

	public Tear(Character self) {
		super("Tear Clothes", self);
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Power)>=32 || user.get(Attribute.Animism)>=12;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return ((c.getStance().reachTop(self)&&!target.topless())||((c.getStance().reachBottom(self)&&!target.pantsless())))&&self.canAct();
	}

	@Override
	public String describe() {
		return "Rip off your opponents clothes";
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(c.getStance().reachTop(self)&&!target.top.empty()){
			if(target.top.peek().attribute()!=Trait.indestructible
			&&self.get(Attribute.Animism)>=12
			&&(self.check(Attribute.Power, target.top.peek().dc()+(target.getStamina().percent()-(target.getArousal().percent())/4)+self.get(Attribute.Animism)*self.getArousal().percent()/100)
			||!target.canAct())){
				if(self.human()){
					c.write(self,"You channel your animal spirit and shred "+target.name()+"'s "+target.top.peek().getName()+" with claws you don't actually have.");
				}
				else if(target.human()){
					c.write(self,self.name()+" lunges toward you and rakes her nails across your "+target.top.peek().getName()+", shredding the garment. That shouldn't be possible. Her " +
							"nails are not that sharp, and if they were, you surely wouldn't have gotten away unscathed.");
				}
				target.shred(0);
				if(self.human()&&target.nude()){
					c.write(target.nakedLiner());
				}
			}
			else if(target.top.peek().attribute()!=Trait.indestructible
			&&self.check(Attribute.Power, target.top.peek().dc()+(target.getStamina().percent()-target.getArousal().percent())/4)||!target.canAct()){
				if(self.human()){
					c.write(self,target.name()+" yelps in surprise as you rip her "+target.top.peek().getName()+" apart.");
				}
				else if(target.human()){
					c.write(self,self.name()+" violently rips your "+target.top.peek().getName()+" off.");
				}
				target.shred(0);
				if(self.human()&&target.nude()){
					c.write(target,target.nakedLiner());
				}
			}
			else{
				if(self.human()){
					c.write(self,"You try to tear apart "+target.name()+"'s "+target.top.peek().getName()+", but the material is more durable than you expected.");
				}
				else if(target.human()){
					c.write(self,self.name()+" yanks on your "+target.top.peek().getName()+", but fails to remove it.");
				}
			}
		}
		else{
			if(target.bottom.peek().attribute()!=Trait.indestructible
			&&self.get(Attribute.Animism)>=12
			&&self.check(Attribute.Power, target.bottom.peek().dc()+(target.getStamina().percent()-(target.getArousal().percent())/4)+self.get(Attribute.Animism)*self.getArousal().percent()/100)
			||!target.canAct()){
				if(self.human()){
					c.write(self,"You channel your animal spirit and shred "+target.name()+"'s "+target.bottom.peek().getName()+" with claws you don't actually have.");
				}
				else if(target.human()){
					c.write(self,self.name()+" lunges toward you and rakes her nails across your "+target.bottom.peek().getName()+", shredding the garment. That shouldn't be possible. Her " +
							"nails are not that sharp, and if they were, you surely wouldn't have gotten away unscathed.");
				}
				target.shred(1);
				if(self.human()&&target.nude()){
					c.write(target,target.nakedLiner());
				}
				if(target.human()&&target.pantsless()){
					if(target.getArousal().get()>=15){
						c.write("Your boner springs out, no longer restrained by your pants.");
					}
					else{
						c.write(self.name()+" giggles as your flacid dick is exposed");
					}
				}
				target.emote(Emotion.nervous, 10);
			}		
			else if(target.bottom.peek().attribute()!=Trait.indestructible
			&&self.check(Attribute.Power, target.bottom.peek().dc()+(target.getStamina().percent()-target.getArousal().percent())/4)||!target.canAct()){
				if(self.human()){
					c.write(self,target.name()+" yelps in surprise as you rip her "+target.bottom.peek().getName()+" apart.");
				}
				else if(target.human()){
					c.write(self,self.name()+" violently rips your "+target.bottom.peek().getName()+" off.");
				}
				target.shred(1);
				if(self.human()&&target.nude()){
					c.write(target,target.nakedLiner());
				}
				if(target.human()&&target.pantsless()){
					if(target.getArousal().get()>=15){
						c.write("Your boner springs out, no longer restrained by your pants.");
					}
					else{
						c.write(self.name()+" giggles as your flacid dick is exposed");
					}
				}
				target.emote(Emotion.nervous, 10);
			}
			else{
				if(self.human()){
					c.write(self,"You try to tear apart "+target.name()+"'s "+target.bottom.peek().getName()+", but the material is more durable than you expected.");
				}
				else if(target.human()){
					c.write(self,self.name()+" yanks on your "+target.bottom.peek().getName()+", but fails to remove them.");
				}
			}
		}
	}

	@Override
	public Skill copy(Character user) {
		return new Tear(user);
	}

	public String getLabel(Combat c){
		if(self.get(Attribute.Animism)>=12){
			return "Shred Clothes";
		}
		else{
			return "Tear Clothes";
		}
	}
	@Override
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
