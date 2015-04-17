package characters;

import global.Flag;
import global.Global;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

import characters.body.Body;

import skills.Skill;
import actions.Action;
import actions.Movement;

import combat.Combat;
import combat.Result;

public abstract class BasePersonality implements Personality {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2279220186754458082L;
	public NPC character;
	
	public BasePersonality() {
	}
	
	public Skill act(HashSet<Skill> available,Combat c) {
		HashSet<Skill> tactic = new HashSet<Skill>();	
		Skill chosen;
		ArrayList<WeightedSkill> priority = Decider.parseSkills(available, c, character);
		if(!Global.checkFlag(Flag.dumbmode)){
			chosen = character.prioritizeNew(priority,c);
		}
		else{
			chosen = character.prioritize(priority);
		}
		if(chosen==null){
			tactic=available;
			Skill[] actions = tactic.toArray(new Skill[tactic.size()]);
			return actions[Global.random(actions.length)];
		}
		else{
			return chosen;
		}
	}

	public Action move(HashSet<Action> available, HashSet<Movement> radar) {
		Action proposed = Decider.parseMoves(available, radar, character);
		return proposed;
	}

	@Override
	public void pickFeat() {
		ArrayList<Trait> available = new ArrayList<Trait>();
		for(Trait feat: Global.getFeats()){
			if(!character.has(feat)){
				available.add(feat);
			}
		}
		if (available.size() == 0) { return; }
		character.add((Trait) available.toArray()[Global.random(available.size())]);
	}

	@Override
	public String image() {
		String fname = "assets/" + character.name().toLowerCase() + "_" + character.mood.name()+".jpg";
		String fname_default = "assets/" + character.name().toLowerCase() + "_confident.jpg";
		if (Global.gui().getClass().getResource(fname) != null) {
			return fname;
		}
		if (Global.gui().getClass().getResource(fname_default) != null) {
			return fname_default;
		}
		return null;
	}

	@Override
	public void ding() {
		character.getStamina().gain(2);
		character.getArousal().gain(5);
		character.getMojo().gain(1);
	}
	
	public String describeAll() {
		StringBuilder b = new StringBuilder();
		b.append(describe());
		b.append("<br><br>");
		character.body.describe(b, character, " ");
		b.append("<br>");
		for (Trait t : character.traits) {
			t.describe(character, b);
			b.append(" ");
		}
		b.append("<br>");
		return b.toString();
	}

	@Override
	public NPC getCharacter() {
		return character;
	}
}