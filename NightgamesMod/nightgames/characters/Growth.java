package nightgames.characters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nightgames.global.Flag;
import nightgames.global.Global;

public class Growth {
	public float arousal;
	public float stamina;
	public float mojo;
	public float bonusArousal;
	public float bonusStamina;
	public float bonusMojo;
	public int attributes[];
	public int bonusAttributes;
	public float willpower;
	public float bonusWillpower;
	private Map<Integer, List<Trait>> traits;
	public Map<Integer, Runnable> actions;
	public Growth() {
		stamina = 2;
		arousal = 4;
		mojo = 1;
		bonusStamina = 2;
		bonusArousal = 3;
		bonusMojo = 1;
		bonusAttributes = 1;
		willpower = .5f;
		bonusWillpower = .25f;
		attributes = new int[] {
			2,
			3,
			3,
		};
		traits = new HashMap<Integer, List<Trait>>();
		actions = new HashMap<Integer, Runnable>();
	}
	
	public void addTrait(int level, Trait trait) {
		if (!traits.containsKey(level)) {
			traits.put(level, new ArrayList<Trait>());
		}
		traits.get(level).add(trait);
	}
	
	public List<Trait> getTraits(int level) {
		return traits.get(level);
	}
	
	public void levelUp(Character character) {
		character.getStamina().gain(stamina);
		character.getArousal().gain(arousal);
		character.getMojo().gain(mojo);
		character.getWillpower().gain(willpower);

		if (character.rank < attributes.length)
			character.availableAttributePoints += attributes[character.rank];
		else
			character.availableAttributePoints += attributes[attributes.length - 1];

		if (Global.checkFlag(Flag.hardmode)) {
			character.getStamina().gain(bonusStamina);
			character.getArousal().gain(bonusArousal);
			character.getMojo().gain(bonusMojo);
			character.getWillpower().gain(bonusWillpower);
			character.availableAttributePoints += bonusAttributes;
		}
		traits.keySet().stream().filter(i -> i <= character.level).forEach(i -> {
			traits.get(i).forEach(trait -> character.add(trait));
		});
		actions.keySet().stream().filter(i -> i <= character.level).forEach(i -> {
			actions.get(i).run();
		});
	}
}
