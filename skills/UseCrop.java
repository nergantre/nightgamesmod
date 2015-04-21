package skills;

import items.Item;
import global.Global;
import global.Modifier;
import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class UseCrop extends Skill {

	public UseCrop(Character self) {
		super(Item.Crop.getName(), self);
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
	public boolean usable(Combat c, Character target) {
		return (self.has(Item.Crop)||self.has(Item.Crop2))&&self.canAct()&&c.getStance().mobile(self)&&(c.getStance().reachTop(self)||c.getStance().reachBottom(self))
				&&(!self.human()||Global.getMatch().condition!=Modifier.notoys);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy()+self.tohit())){
			if(target.pantsless()&&c.getStance().reachBottom(self)){
				if(self.has(Item.Crop2)&&Global.random(10)>7){
					if(self.human()){
						c.write(self,deal(c,0,Result.critical, target));
					}
					else if(target.human()){
						c.write(self,receive(c,0,Result.critical, target));
					}
					if(target.has(Trait.achilles)){
						target.pain(c, 6);
					}
					target.emote(Emotion.angry,10);
					target.pain(c, 8+Global.random(14)+target.get(Attribute.Perception));					
				}
				else{		
					if(self.human()){
						c.write(self,deal(c,0,Result.normal, target));
					}
					else if(target.human()){
						c.write(self,receive(c,0,Result.normal, target));
					}
					target.pain(c, 5+Global.random(12)+target.get(Attribute.Perception)/2);
				}
			}
			else{
				if(self.human()){
					c.write(self,deal(c,0,Result.weak, target));
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.weak, target));
				}
				target.pain(c, 5+Global.random(12));
			}
			target.emote(Emotion.angry,15);
		}
		else{
			if(self.human()){
				c.write(self,deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.miss, target));
			}
		}
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
				return "You strike "+target.name()+"'s bare ass with your crop and the  'Treasure Hunter' attachment slips between her legs, hitting one of her hanging testicles " +
						"squarely. She lets out a shriek and clutches her sore nut";
			}
			else{
				return "You strike "+target.name()+"'s bare ass with your crop and the  'Treasure Hunter' attachment slips between her legs, impacting on her sensitive pearl. She " +
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
				return "You duck out of the way, as "+self.name()+" swings her riding crop at you.";
			}
			else{
				return self.name()+" swings her riding crop, but you draw your own crop and parry it.";
			}
		}
		else if(modifier==Result.critical){
			return self.name()+" hits you on the ass with her riding crop. The attachment on the end delivers a painful sting to your jewels. You groan in pain and fight the urge to " +
					"curl up in the fetal position.";
		}
		else if(modifier==Result.weak){
			return self.name()+" strikes you with a riding crop.";
		}
		else{
			return self.name()+" hits your bare ass with a riding crop hard enough to leave a painful welt.";
		}
	}

	@Override
	public String describe() {
		return "Strike your opponent with riding crop. More effective if she's naked";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
