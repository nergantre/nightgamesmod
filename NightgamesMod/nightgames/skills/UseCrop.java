package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;

public class UseCrop extends Skill {

	public UseCrop(Character self) {
		super(Item.Crop.getName(), self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return (getSelf().has(Item.Crop)||getSelf().has(Item.Crop2))&&getSelf().canAct()&&c.getStance().mobile(getSelf())&&(c.getStance().reachTop(getSelf())||c.getStance().reachBottom(getSelf()));
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 10;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy(c))){
			if(target.crotchAvailable()&&c.getStance().reachBottom(getSelf())){
				if(getSelf().has(Item.Crop2)&&Global.random(10)>7 && !target.has(Trait.brassballs)){
					if(getSelf().human()){
						c.write(getSelf(),deal(c,0,Result.critical, target));
					}
					else if(target.human()){
						c.write(getSelf(),receive(c,0,Result.critical, target));
					}
					if(target.has(Trait.achilles)){
						target.pain(c, 6);
					}
					target.emote(Emotion.angry,10);
					target.pain(c, 8+Global.random(14)+target.get(Attribute.Perception));					
				}
				else{		
					if(getSelf().human()){
						c.write(getSelf(),deal(c,0,Result.normal, target));
					}
					else if(target.human()){
						c.write(getSelf(),receive(c,0,Result.normal, target));
					}
					target.pain(c, 5+Global.random(12)+target.get(Attribute.Perception)/2);
				}
			}
			else{
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.weak, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.weak, target));
				}
				target.pain(c, 5+Global.random(12));
			}
			target.emote(Emotion.angry,15);
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new UseCrop(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.damage;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			if(!target.has(Item.Crop)){
				return "You lash out with your riding crop, but it fails to connect.";
			}
			else{
				return "You try to hit "+target.name()+" with your riding crop, but she deflects it with her own.";
			}
		}
		else if(modifier==Result.critical){
			if(target.hasBalls()){
				return "You strike "+target.name()+"'s bare ass with your crop and the 'Treasure Hunter' attachment slips between her legs, hitting one of her hanging testicles " +
						"squarely. She lets out a shriek and clutches her sore nut";
			}
			else{
				return "You strike "+target.name()+"'s bare ass with your crop and the 'Treasure Hunter' attachment slips between her legs, impacting on her sensitive pearl. She " +
					"lets out a high pitched yelp and clutches her injured anatomy.";
			}
		}
		else if(modifier==Result.weak){
			return "You hit "+target.name()+" with your riding crop.";
		}
		else{
			return "You strike "+target.name()+"'s soft, bare skin with your riding crop, leaving a visible red mark.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			if(!target.has(Item.Crop)){
				return "You duck out of the way, as "+getSelf().name()+" swings her riding crop at you.";
			}
			else{
				return getSelf().name()+" swings her riding crop, but you draw your own crop and parry it.";
			}
		}
		else if(modifier==Result.critical){
			return getSelf().name()+" hits you on the ass with her riding crop. The attachment on the end delivers a painful sting to your jewels. You groan in pain and fight the urge to " +
					"curl up in the fetal position.";
		}
		else if(modifier==Result.weak){
			return getSelf().name()+" strikes you with a riding crop.";
		}
		else{
			return getSelf().name()+" hits your bare ass with a riding crop hard enough to leave a painful welt.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Strike your opponent with riding crop. More effective if she's naked";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
