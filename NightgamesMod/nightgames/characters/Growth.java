package nightgames.characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nightgames.characters.body.BodyPart;
import nightgames.global.Flag;
import nightgames.global.Global;

public class Growth implements Cloneable {
    public float arousal;
    public float stamina;
    public float bonusArousal;
    public float bonusStamina;
    public int attributes[];
    public int bonusAttributes;
    public float willpower;
    public float bonusWillpower;
    private Map<Integer, List<Trait>> traits;
    public Map<Integer, List<BodyPart>> bodyParts;

    public Growth() {
        stamina = 2;
        arousal = 4;
        bonusStamina = 2;
        bonusArousal = 3;
        bonusAttributes = 1;
        willpower = .5f;
        bonusWillpower = .25f;
        attributes = new int[10];
        Arrays.fill(attributes, 4);
        attributes[0] = 3;
        traits = new HashMap<>();
        bodyParts = new HashMap<>();
    }

    public void addTrait(int level, Trait trait) {
        if (!traits.containsKey(level)) {
            traits.put(level, new ArrayList<Trait>());
        }
        traits.get(level).add(trait);
    }

    public void addBodyPart(int level, BodyPart part) {
        if (!bodyParts.containsKey(level)) {
            bodyParts.put(level, new ArrayList<BodyPart>());
        }
        bodyParts.get(level).add(part);
    }

    public void addOrRemoveTraits(Character character) {
        traits.keySet().stream().filter(i -> i > character.level).forEach(i -> {
            traits.get(i).forEach(character::remove);
        });
        traits.keySet().stream().filter(i -> i <= character.level).forEach(i -> {
            traits.get(i).forEach(character::add);
        });
        bodyParts.forEach((level, parts) ->  {
            parts.forEach(part -> {
                BodyPart existingPart = character.body.getRandom(part.getType());
                String existingPartDesc = existingPart == null ? "NO_EXISTING_PART" : existingPart.canonicalDescription();
                String loadedPartDesc = part == null ? "NO_LOADED_PART" : part.canonicalDescription();
                // only add parts if the level matches
                if (level <= character.getLevel()) {
                    if (existingPart == null || !existingPartDesc.equals(loadedPartDesc)) {
                        character.body.addReplace(part, 1);
                    }
                }
            });
        });
    }

    public void levelUp(Character character) {
        character.getStamina().gain(stamina);
        character.getArousal().gain(arousal);
        character.getWillpower().gain(willpower);

        character.availableAttributePoints += attributes[Math.min(character.rank, attributes.length-1)];

        if (Global.checkFlag(Flag.hardmode)) {
            character.getStamina().gain(bonusStamina);
            character.getArousal().gain(bonusArousal);
            character.getWillpower().gain(bonusWillpower);
            character.availableAttributePoints += bonusAttributes;
        }
        addOrRemoveTraits(character);
    }

    /**
     * Note: only affects meters, not traits.
     *
     * @param character
     */
    public void levelDown(Character character) {
        character.getStamina().gain(-stamina);
        character.getArousal().gain(-arousal);
        character.getWillpower().gain(-willpower);
        if (Global.checkFlag(Flag.hardmode)) {
            character.getStamina().gain(-bonusStamina);
            character.getArousal().gain(-bonusArousal);
            character.getWillpower().gain(-bonusWillpower);
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
        // TODO, growth should NEVER be modified as a cloned version. if this is true, we need to revisit this.
        Growth clone = (Growth) super.clone();
        clone.traits = Collections.unmodifiableMap(clone.traits);
        clone.bodyParts = Collections.unmodifiableMap(clone.bodyParts);
        return clone;
    }
}
