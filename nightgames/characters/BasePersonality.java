package nightgames.characters;

import nightgames.actions.Action;
import nightgames.actions.Movement;
import nightgames.combat.Combat;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.skills.Skill;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BasePersonality implements Personality {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2279220186754458082L;
	public NPC character;
	protected Growth growth;
	protected List<PreferredAttribute> preferredAttributes;

	public interface PreferredAttribute {
		Optional<Attribute> getPreferred(Character c);
	}

	public BasePersonality() {
		growth = new Growth();
		preferredAttributes = new ArrayList<PreferredAttribute>();
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
		character.getWillpower().gain(growth.willpower);
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

	public String describeAll(Combat c) {
		StringBuilder b = new StringBuilder();
		b.append(describe(c));
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
		character.getWillpower().gain(growth.bonusWillpower);
		character.availableAttributePoints += growth.bonusAttributes;
	}

	public void distributePoints() {
		if (character.availableAttributePoints <= 0) { return; }
		ArrayList<Attribute> avail = new ArrayList<Attribute>();
		Deque<PreferredAttribute> preferred = new ArrayDeque<PreferredAttribute>(preferredAttributes);
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
			Attribute selected = null;
			//remove all the attributes that isn't in avail
			preferred = new ArrayDeque<>(preferred.stream().filter(p -> {
				Optional<Attribute> att = p.getPreferred(character);
				return att.isPresent() && avail.contains(att.get());
			}).collect(Collectors.toList()));
			if (preferred.size() > 0) {
				Optional<Attribute> pref = preferred.removeFirst().getPreferred(character);
				if (pref.isPresent()) {
					selected = pref.get();
				}
			}

			if (selected == null) {
				selected = avail.get(Global.random(avail.size()));
			}
			character.mod(selected, 1);
			selected = null;
		}
	}
}