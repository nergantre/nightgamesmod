package nightgames.characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.GenericBodyPart;
import nightgames.characters.body.mods.PartMod;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.utilities.DebugHelper;

public class Growth implements Cloneable {
    public class PartModApplication {
        private final PartMod mod;
        private final String bodyPartType;
        public PartModApplication(String bodyPartType, PartMod mod) {
            this.bodyPartType = bodyPartType;
            this.mod = mod;
        }
        
        public PartMod getMod() {
            return mod;
        }
        public String getBodyPartType() {
            return bodyPartType;
        }
    }
    public float arousal;
    public float stamina;
    public float bonusArousal;
    public float bonusStamina;
    public int attributes[];
    public int bonusAttributes;
    public int extraAttributes;
    public float willpower;
    public float bonusWillpower;
    private Map<Integer, List<Trait>> traits;
    private Map<Integer, Integer> traitPoints;
    public Map<Integer, List<BodyPart>> bodyParts;
    public Map<Integer, List<PartModApplication>> bodyPartMods;
    private Map<Integer, Clothing> clothing;

    public Growth() {
        stamina = 2;
        arousal = 4;
        bonusStamina = 2;
        bonusArousal = 3;
        bonusAttributes = 1;
        extraAttributes = 0;
        willpower = 1.0f;
        bonusWillpower = .25f;
        attributes = new int[10];
        Arrays.fill(attributes, 4);
        attributes[0] = 3;
        traits = new HashMap<>();
        bodyParts = new HashMap<>();
        bodyPartMods = new HashMap<>();
        traitPoints = new HashMap<>();
        clothing = new HashMap<>();
    }

    public void addTrait(int level, Trait trait) {
        if (trait == null) {
            System.err.println("Tried to add a null trait to a growth.");
            DebugHelper.printStackFrame(4, 1);
            return;
        }
        if (!traits.containsKey(level)) {
            traits.put(level, new ArrayList<Trait>());
        }
        traits.get(level).add(trait);
    }

    public Map<Integer, List<Trait>> getTraits() {
        return Collections.unmodifiableMap(new HashMap<>(traits));
    }
    
    public void addTraitPoints(int[] levels, Character charfor) {
        if (!(charfor instanceof Player)) return;
        for (int level : levels) {
            if (!(traitPoints.containsKey(level))) traitPoints.put(level, 0);
            traitPoints.put(level,traitPoints.get(level)+1);
            if (charfor.level <= level) ((Player)charfor).traitPoints+=1;
        }
    }

    public void addBodyPart(int level, BodyPart part) {
        if (!bodyParts.containsKey(level)) {
            bodyParts.put(level, new ArrayList<>());
        }
        bodyParts.get(level).add(part);
    }

    public void addBodyPartMod(int level, String type, PartMod mod) {
        if (!bodyPartMods.containsKey(level)) {
            bodyPartMods.put(level, new ArrayList<>());
        }
        bodyPartMods.get(level).add(new PartModApplication(type, mod));
    }

    public void addClothing(int level, Clothing c) {
        clothing.putIfAbsent(level, c);
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
        bodyPartMods.forEach((level, mods) ->  {
            mods.forEach(mod -> {
                // only add parts if the level matches
                if (level <= character.getLevel()) {
                    BodyPart existingPart = character.body.getRandom(mod.getBodyPartType());
                    String existingPartDesc = existingPart == null ? "NO_EXISTING_PART" : existingPart.canonicalDescription();
                    if (existingPart != null && existingPart instanceof GenericBodyPart) {
                        GenericBodyPart part = (GenericBodyPart) existingPart;
                        GenericBodyPart newPart = (GenericBodyPart) part.applyMod(mod.getMod());
                        if (newPart.canonicalDescription().equals(existingPartDesc)) {
                            character.body.addReplace(newPart, 1);
                        }
                    }
                }
            });
        });
        clothing.forEach((level, c) -> {
           if (character.getLevel() >= level) {
               character.outfitPlan.add(c);
           } else {
               character.outfitPlan.remove(c);
           }
        });
    }

    public void levelUp(Character character) {
        character.getStamina().gain(stamina);
        character.getArousal().gain(arousal);
        character.getWillpower().gain(willpower);
        if (traitPoints.containsKey(character.level) && character instanceof Player) ((Player)character).traitPoints+=traitPoints.get(character.level);

        character.availableAttributePoints += attributes[Math.min(character.rank, attributes.length-1)] + extraAttributes;

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
        clone.bodyPartMods = Collections.unmodifiableMap(clone.bodyPartMods);
        clone.clothing = Collections.unmodifiableMap(clone.clothing);
        return clone;
    }

    public void removeNullTraits() {
        traits.forEach((i, l) -> l.removeIf(t -> t == null));
    }
}
