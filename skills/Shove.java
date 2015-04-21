package skills;

import stance.Mount;
import stance.Neutral;
import stance.ReverseMount;
import stance.StandingOver;
import status.Braced;
import status.Stsflag;
import global.Global;
import characters.Attribute;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Shove extends Skill {
	public Shove(Character self) {
		super("Shove", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		if (target.hasStatus(Stsflag.cockbound)) { return false; }
		return !c.getStance().dom(self)&&!c.getStance().prone(target)&&c.getStance().reachTop(self)&&self.canAct()&&!c.getStance().penetration(self);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.getPure(Attribute.Ki)>=1&&!target.top.isEmpty()&&self.canSpend(5)) {
			if(self.human()){
				c.write(self,deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.special, target));
			}
			target.shred(0);
			target.pain(c, Global.random(6)+2);
			if(self.check(Attribute.Power, target.knockdownDC()-self.get(Attribute.Ki))) {
				c.setStance(new Neutral(self,target));
			}
		} else if (c.getStance().getClass() == Mount.class || c.getStance().getClass()==ReverseMount.class) {
			if(self.check(Attribute.Power,target.knockdownDC()+5)){
				if(self.human()){
					c.write(self,"You shove "+target.name()+" off of you and get to your feet before she can retaliate.");
				} else if(target.human()) {
					c.write(self,self.name()+" shoves you hard enough to free herself and jump up.");
				}
				if(!self.is(Stsflag.braced)){
					self.add(new Braced(self));
				}
				c.setStance(new Neutral(self,target));
			} else {
				if(self.human()){
					c.write(self,"You push "+target.name()+", but you're unable to dislodge her.");
				} else if(target.human()) {
					c.write(self,self.name()+" shoves you weakly.");
				}
			}
			target.pain(c, Global.random(6)+2);
		}
		else{ 
			if(self.check(Attribute.Power,target.knockdownDC())){
				if(self.human()){
					c.write(self,"You shove "+target.name()+" hard enough to knock her flat on her back.");
				}
				else if(target.human()){
					c.write(self,self.name()+" knocks you off balance and you fall at her feet.");
				}
				c.setStance(new StandingOver(self,target));
			}
			else{
				if(self.human()){
					c.write(self,"You shove "+target.name()+" back a step, but she keeps her footing.");
				}
				else if(target.human()){
					c.write(self,self.name()+" pushes you back, but you're able to maintain your balance.");
				}
			}
			target.pain(c, Global.random(4)+self.get(Attribute.Power)/2);
		}

	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return true;	
	}

	@Override
	public Skill copy(Character user) {
		return new Shove(user);
	}
	public int speed(){
		return 7;
	}
	public Tactics type(Combat c) {
		return Tactics.damage;
	}
	public String toString(){
		if(self.get(Attribute.Ki)>=1){
			return "Shredding Palm";
		}
		else{
			return name;
		}			
	}
	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You channel your ki into your hands and strike "+target.name()+" in the chest, destroying her "+target.top.peek();
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return self.name()+" strikes you in the chest with her palm, staggering you a step. Suddenly your "+target.top.peek()+" tears and falls off you in pieces";
	}

	@Override
	public String describe() {
		return "Slightly damage opponent and try to knock her down";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
