package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;

public class StripBottom extends Skill {

	public StripBottom(Character self) {
		super("Strip Bottoms", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (c.getStance().oral(getSelf()) || c.getStance().reachBottom(getSelf()))&&!target.crotchAvailable()&&getSelf().canAct();
	}

	@Override
	public int getMojoCost(Combat c) {
		return 10;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		int difficulty = target.getOutfit().getTopOfSlot(ClothingSlot.bottom).dc()
				+(target.getLevel())
				+(target.getStamina().percent() / 4
				- target.getArousal().percent()) / 5
				- (!target.canAct()||c.getStance().sub(target) ? 20 : 0);
		if (getSelf().check(Attribute.Cunning, difficulty)||!target.canAct()) {
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			target.strip(ClothingSlot.bottom, c);
			if(getSelf().human()&&target.mostlyNude()){
				c.write(target,target.nakedLiner(c));
			}
			if(target.human()&&target.crotchAvailable()&&target.hasDick()) {
				if(target.getArousal().get()>=15){
					c.write("Your boner springs out, no longer restrained by your pants.");
				}
				else{
					c.write(getSelf().name()+" giggles as your flaccid dick is exposed");
				}
			}
			target.emote(Emotion.nervous, 10);
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
			target.weaken(c, Global.random(6)+getSelf().get(Attribute.Power)/4);
			return false;
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new StripBottom(user);
	}
	public int speed(){
		return 3;
	}
	public Tactics type(Combat c) {
		return Tactics.stripping;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You grab "+target.name()+"'s "+target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()+", but she scrambles away before you can strip her.";
		}
		else{
			return "After a brief struggle, you manage to pull off "+target.name()+"'s "+target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()+".";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return getSelf().name()+" tries to pull down your "+target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()+", but you hold them up.";
		}
		else{
			return getSelf().name()+" grabs the waistband of your "+target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()+" and pulls them down.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Attempt to remove opponent's pants. More likely to succeed if she's weakened and aroused";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
