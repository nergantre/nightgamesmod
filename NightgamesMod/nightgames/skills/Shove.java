package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.stance.Mount;
import nightgames.stance.Neutral;
import nightgames.stance.ReverseMount;
import nightgames.status.Falling;
import nightgames.status.Stsflag;

public class Shove extends Skill {
	public Shove(Character self) {
		super("Shove", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		if (target.hasStatus(Stsflag.cockbound)) { return false; }
		return !c.getStance().dom(getSelf())&&!c.getStance().prone(target)&&c.getStance().reachTop(getSelf())&&getSelf().canAct()&&!c.getStance().havingSex();
	}

	@Override
	public int getMojoCost(Combat c) {
		return 10;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		boolean success = true;
		if(getSelf().get(Attribute.Ki)>=1&&
				!target.getOutfit().slotUnshreddable(ClothingSlot.top)
				&&getSelf().canSpend(5)) {
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.special, target));
			}
			target.shred(ClothingSlot.top);
			target.pain(c, Global.random(10)+15 + (getSelf().get(Attribute.Power)+  getSelf().get(Attribute.Ki)) / 4);
			if(getSelf().check(Attribute.Power, target.knockdownDC()-getSelf().get(Attribute.Ki))) {
				c.setStance(new Neutral(getSelf(),target));
			}
		} else if (c.getStance().getClass() == Mount.class || c.getStance().getClass()==ReverseMount.class) {
			if(getSelf().check(Attribute.Power,target.knockdownDC()+5)){
				if(getSelf().human()){
					c.write(getSelf(),"You shove "+target.name()+" off of you and get to your feet before she can retaliate.");
				} else if(target.human()) {
					c.write(getSelf(),getSelf().name()+" shoves you hard enough to free herself and jump up.");
				}
				c.setStance(new Neutral(getSelf(),target));
			} else {
				if(getSelf().human()){
					c.write(getSelf(),"You push "+target.name()+", but you're unable to dislodge her.");
				} else if(target.human()) {
					c.write(getSelf(),getSelf().name()+" shoves you weakly.");
				}
				success = false;
			}
			target.pain(c, Global.random(10)+10 + (getSelf().get(Attribute.Power)+  getSelf().get(Attribute.Ki)) / 4);
		}
		else{ 
			if(getSelf().check(Attribute.Power,target.knockdownDC())){
				if(getSelf().human()){
					c.write(getSelf(),"You shove "+target.name()+" hard enough to knock her flat on her back.");
				}
				else if(target.human()){
					c.write(getSelf(),getSelf().name()+" knocks you off balance and you fall at her feet.");
				}
				target.add(c, new Falling(target));
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),"You shove "+target.name()+" back a step, but she keeps her footing.");
				}
				else if(target.human()){
					c.write(getSelf(),getSelf().name()+" pushes you back, but you're able to maintain your balance.");
				}
				success = false;
			}
			target.pain(c, Global.random(10)+10 + (getSelf().get(Attribute.Power)+  getSelf().get(Attribute.Ki)) / 4);
		}
		return success;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return user.get(Attribute.Power) >= 5;
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
	@Override
	public String getLabel(Combat c){
		if(getSelf().get(Attribute.Ki)>=1){
			return "Shredding Palm";
		}
		else{
			return getName(c);
		}			
	}
	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You channel your ki into your hands and strike "+target.name()+" in the chest, destroying her " + target.getOutfit().getTopOfSlot(ClothingSlot.top).getName() + ".";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" strikes you in the chest with her palm, staggering your footing. Suddenly your "+target.getOutfit().getTopOfSlot(ClothingSlot.top).getName()+" tears and falls off you in tatters.";
	}

	@Override
	public String describe(Combat c) {
		return "Slightly damage opponent and try to knock her down";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
