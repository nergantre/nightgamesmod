package nightgames.characters;

import java.util.ArrayList;
import java.util.HashSet;

import nightgames.actions.Action;
import nightgames.actions.Move;
import nightgames.actions.Movement;
import nightgames.combat.Combat;
import nightgames.daytime.Daytime;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class Decider {
	private static void addAllSkillsWithPriority(ArrayList<WeightedSkill> priority, HashSet<Skill> skills, float weight)
	{
		for (Skill s : skills) {
			priority.add(new WeightedSkill(weight, s));
		}
	}

	public static ArrayList<WeightedSkill> parseSkills(HashSet<Skill> available, Combat c, NPC character){
		HashSet<Skill> damage = new HashSet<Skill>();
		HashSet<Skill> pleasure = new HashSet<Skill>();
		HashSet<Skill> fucking = new HashSet<Skill>();
		HashSet<Skill> position = new HashSet<Skill>();
		HashSet<Skill> debuff = new HashSet<Skill>();
		HashSet<Skill> recovery = new HashSet<Skill>();
		HashSet<Skill> calming = new HashSet<Skill>();
		HashSet<Skill> summoning = new HashSet<Skill>();
		HashSet<Skill> stripping = new HashSet<Skill>();
		HashSet<Skill> misc = new HashSet<Skill>();
		ArrayList<WeightedSkill> priority = new ArrayList<WeightedSkill>();
		for(Skill a:available){
			if(a.type(c)==Tactics.damage){
				damage.add(a);
			}
			else if(a.type(c)==Tactics.pleasure){
				pleasure.add(a);
			}
			else if(a.type(c)==Tactics.fucking){
				fucking.add(a);
			}
			else if(a.type(c)==Tactics.positioning){
				position.add(a);
			}
			else if(a.type(c)==Tactics.debuff){
				debuff.add(a);
			}
			else if(a.type(c)==Tactics.recovery){
				recovery.add(a);
			}
			else if(a.type(c)==Tactics.calming){
				calming.add(a);
			}
			else if(a.type(c)==Tactics.summoning){
				summoning.add(a);
			}
			else if(a.type(c)==Tactics.stripping){
				stripping.add(a);
			}
			else if(a.type(c)==Tactics.misc){
				misc.add(a);
			}
		}
		switch(character.mood){
		case confident:
			//i can do whatever i want
			addAllSkillsWithPriority(priority, position, 1.0f);
			addAllSkillsWithPriority(priority, stripping, 1.0f);
			addAllSkillsWithPriority(priority, debuff, 1.0f);
			addAllSkillsWithPriority(priority, pleasure, 1.0f);
			addAllSkillsWithPriority(priority, fucking, 1.0f);
			addAllSkillsWithPriority(priority, damage, 1.0f);
			addAllSkillsWithPriority(priority, summoning, .5f);
			addAllSkillsWithPriority(priority, misc, 0f);
			break;
		case angry:
			addAllSkillsWithPriority(priority, damage, 2.5f);
			addAllSkillsWithPriority(priority, position, 2.0f);
			addAllSkillsWithPriority(priority, debuff, 2.0f);
			addAllSkillsWithPriority(priority, stripping, 1.0f);
			addAllSkillsWithPriority(priority, pleasure, 0.0f);
			addAllSkillsWithPriority(priority, misc, 0f);
			addAllSkillsWithPriority(priority, summoning, 0f);
			break;
		case nervous:
			addAllSkillsWithPriority(priority, summoning, 2.0f);
			addAllSkillsWithPriority(priority, debuff, 2.0f);
			addAllSkillsWithPriority(priority, calming, 2.0f);
			addAllSkillsWithPriority(priority, recovery, 2.0f);
			addAllSkillsWithPriority(priority, position, 1.0f);
			addAllSkillsWithPriority(priority, damage, .5f);
			addAllSkillsWithPriority(priority, pleasure, 0.0f);
			addAllSkillsWithPriority(priority, misc, 0f);
			break;
		case desperate:
			//and probably a bit confused
			addAllSkillsWithPriority(priority, calming, 4.0f);
			addAllSkillsWithPriority(priority, recovery, 4.0f);
			addAllSkillsWithPriority(priority, debuff, 4.0f);
			addAllSkillsWithPriority(priority, misc, 3.0f);
			addAllSkillsWithPriority(priority, position, 2.0f);
			addAllSkillsWithPriority(priority, damage, 2.0f);
			addAllSkillsWithPriority(priority, pleasure, 1.0f);
			addAllSkillsWithPriority(priority, fucking, 1.0f);
			break;
		case horny:
			addAllSkillsWithPriority(priority, fucking, 5.0f);
			addAllSkillsWithPriority(priority, stripping, 1.0f);
			addAllSkillsWithPriority(priority, pleasure, 1.0f);
			addAllSkillsWithPriority(priority, position, 1.0f);
			addAllSkillsWithPriority(priority, debuff, 0f);
			addAllSkillsWithPriority(priority, misc, 0f);
			break;
		case dominant:
			addAllSkillsWithPriority(priority, position, 3.0f);
			addAllSkillsWithPriority(priority, fucking, 2.0f);
			addAllSkillsWithPriority(priority, stripping, 2.0f);
			addAllSkillsWithPriority(priority, pleasure, 2.0f);
			addAllSkillsWithPriority(priority, debuff, 2.0f);
			addAllSkillsWithPriority(priority, summoning, 1.0f);
			addAllSkillsWithPriority(priority, damage, 1.0f);
			addAllSkillsWithPriority(priority, misc, 0f);
			break;
		}
/*	if(character.getArousal().percent()>85||character.getStamina().percent()<10){
			priority.add(recovery);
			priority.add(damage);
			priority.add(pleasure);
		}
		if((c.stance.penetration(character)&&c.stance.dom(character))||c.stance.enumerate()==Stance.sixnine||(c.stance.dom(character)&&c.stance.enumerate()==Stance.behind)){
			priority.add(pleasure);
			priority.add(pleasure);
			priority.add(damage);
			priority.add(recovery);
		}
		if(!target.canAct()){
			priority.add(stripping);
			priority.add(pleasure);
			priority.add(summoning);
			priority.add(position);
		}
		else if(!target.nude()&&(target.getArousal().percent()>60||target.getStamina().percent()<50)){
			priority.add(stripping);
			priority.add(pleasure);
			priority.add(damage);
			priority.add(position);		
		}
		else if(c.stance.dom(character)){
			priority.add(pleasure);
			priority.add(stripping);
			priority.add(summoning);
			priority.add(damage);
			priority.add(position);
			priority.add(debuff);
		}
		else{
			priority.add(summoning);
			priority.add(pleasure);
			priority.add(debuff);
			priority.add(damage);
			priority.add(position);
			priority.add(stripping);
			priority.add(recovery);
		}
*/		return priority;
	}
	
	public static Action parseMoves(HashSet<Action> available,HashSet<Movement> radar,NPC character){
		HashSet<Action> enemy = new HashSet<Action>();
		HashSet<Action> safe = new HashSet<Action>();
		HashSet<Action> utility = new HashSet<Action>();
		HashSet<Action> tactic = new HashSet<Action>();
		if(character.mostlyNude()){
			for(Action act: available){
				if(act.consider()==Movement.resupply){
					return act;
				}
				else if(act.getClass()==Move.class){
					Move movement = (Move)act;
					if(movement.consider()==Movement.union&&!radar.contains(Movement.union)||movement.consider()==Movement.dorm&&!radar.contains(Movement.dorm)){
						return act;
					}
				}
			}
		}
		if(character.getArousal().percent()>=40&&!character.location().humanPresent()&&radar.isEmpty()){
			for(Action act: available){
				if(act.consider()==Movement.masturbate){
					return act;
				}
			}
		}
		if(character.getStamina().percent()<=60||character.getArousal().percent()>=30){
			for(Action act: available){
				if(act.consider()==Movement.bathe){
					return act;
				}
				else if(act.getClass()==Move.class){
					Move movement = (Move)act;
					if(movement.consider()==Movement.pool||movement.consider()==Movement.shower){
						return act;
					}
				}
			}
		}
		if(character.get(Attribute.Science)>=1&&!character.has(Item.Battery,10)){
			for(Action act: available){
				if(act.consider()==Movement.recharge){
					return act;
				}
			}
			Move path = character.findPath(Global.getMatch().gps("Workshop"));
			if( path!=null){
				return path;
			}
		}
		for(Action act: available){
			if(radar.contains(act.consider())){
				enemy.add(act);
			}
			else if(act.consider()==Movement.bathe||act.consider()==Movement.craft||act.consider()==Movement.scavenge||act.consider()==Movement.hide||act.consider()==Movement.trap||act.consider()==Movement.wait||act.consider()==Movement.engineering||act.consider()==Movement.dining){
				utility.add(act);
			}
			else{
				safe.add(act);
			}
		}
		if(!character.location().humanPresent()){
			tactic.addAll(utility);
		}
		if(character.plan == Plan.hunting&&!enemy.isEmpty()){
			tactic.addAll(enemy);
		}

		else{
			tactic.addAll(safe);
		}
		if(tactic.isEmpty()){
			tactic.addAll(available);
		}
		Action[] actions = tactic.toArray(new Action[tactic.size()]);
		return actions[Global.random(actions.length)];
	}
	public static void visit(Character self){
		int max = 0;
		Character bff = null;
		if(!self.attractions.isEmpty()){
			for(Character friend: self.attractions.keySet()){
				if(self.getAttraction(friend)>max&&!friend.human()){
					max = self.getAttraction(friend);
					bff = friend;
				}
			}
			if(bff!=null){
				self.gainAffection(bff, Global.random(3)+1);
				bff.gainAffection(self, Global.random(3)+1);
				switch(Global.random(3)){
				case 0:
					Daytime.train(self, bff, Attribute.Power);
				case 1:
					Daytime.train(self, bff, Attribute.Cunning);
				default:
					Daytime.train(self, bff, Attribute.Seduction);
				}
			}
		}
	}
}
