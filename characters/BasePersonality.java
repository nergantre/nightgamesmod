package characters;

import global.Flag;
import global.Global;

import items.Item;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import skills.Skill;
import actions.Action;
import actions.Movement;

import combat.Combat;

public abstract class BasePersonality implements Personality {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2279220186754458082L;
	public NPC character;
	protected Growth growth;
	protected List<Attribute> preferredAttributes;

	public BasePersonality() {
		growth = new Growth();
		preferredAttributes = new ArrayList<Attribute>();
		setGrowth();
	}

	public void setGrowth() {
	}
	
	public void buyUpTo(Item item, int number) {
		while (character.money>item.getPrice() && character.count(item) < 3) {
			character.money-=item.getPrice();
			character.gain(item);
		}
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
		for(Trait feat: Global.getFeats(character)){
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

	public Growth getGrowth() {
		return growth;
	}

	@Override
	public void ding() {
		character.getStamina().gain(growth.stamina);
		character.getArousal().gain(growth.arousal);
		character.getMojo().gain(growth.mojo);
		// get all the traits for the level up
		growth.traits.keySet().stream().filter(i -> i <= character.level).forEach(i -> character.add(growth.traits.get(i)));;
		growth.actions.keySet().stream().filter(i -> i <= character.level).forEach(i -> {
			growth.actions.get(i).run();
		});
		character.availableAttributePoints += growth.attributes[character.rank];
		if (Global.checkFlag(Flag.hardmode)) {
			hardmodeBonus();
		}
		distributePoints();
	}

	public String describeAll() {
		StringBuilder b = new StringBuilder();
		b.append(describe());
		b.append("<br><br>");
		character.body.describe(b, character, " ");
		b.append("<br>");
		for (Trait t : character.getTraits()) {
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

	public void hardmodeBonus() {
		character.getStamina().gain(growth.bonusStamina);
		character.getArousal().gain(growth.bonusArousal);
		character.getMojo().gain(growth.bonusMojo);
		character.availableAttributePoints += growth.bonusAttributes;
	}

	public void distributePoints() {
		if (character.availableAttributePoints <= 0) { return; }
		ArrayList<Attribute> avail = new ArrayList<Attribute>();
		ArrayDeque<Attribute> preferred = new ArrayDeque<Attribute>(preferredAttributes);
		for (Attribute a : character.att.keySet()) {
			if (Attribute.isTrainable(a) && (character.getPure(a) > 0 || Attribute.isBasic(a))) {
				avail.add(a);
			}
		}
		if (avail.size() == 0) {
			avail.add(Attribute.Cunning);
			avail.add(Attribute.Power);
			avail.add(Attribute.Seduction);
		}
		for (; character.availableAttributePoints > 0; character.availableAttributePoints--) {
			Attribute selected;
			while (preferred.size() > 0 && !avail.contains(preferred.peekFirst())) {
				preferred.removeFirst();
			}
			if (preferred.size() > 0) {
				selected = preferred.removeFirst();
			} else {
				selected = avail.get(Global.random(avail.size()));
			}
			character.mod(selected, 1);
		}
	}
}