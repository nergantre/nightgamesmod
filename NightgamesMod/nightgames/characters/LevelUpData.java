package nightgames.characters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class LevelUpData implements Cloneable {
    private Set<Trait> traitsAdded;
    private Set<Trait> traitsRemoved;
    private Map<Attribute, Integer> attributes;

    protected LevelUpData() {
        traitsAdded = new HashSet<>();
        traitsRemoved = new HashSet<>();
        attributes = new HashMap<>();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void unapply(Character self) {
        for (Entry<Attribute, Integer> entry : attributes.entrySet()) {
            self.modAttributeDontSaveData(entry.getKey(), -entry.getValue().intValue());
        }
        for (Trait lostTrait : traitsRemoved) {
            self.traits.addIfAbsent(lostTrait);
        }
        for (Trait newTrait : traitsAdded) {
            self.traits.remove(newTrait);
        }
    }

    public void apply(Character self) {
        for (Entry<Attribute, Integer> entry : attributes.entrySet()) {
            self.modAttributeDontSaveData(entry.getKey(), entry.getValue().intValue());
        }
        for (Trait lostTrait : traitsRemoved) {
            self.traits.remove(lostTrait);
        }
        for (Trait newTrait : traitsAdded) {
            self.traits.addIfAbsent(newTrait);
        }
    }

    public void addTrait(Trait t) {
        if (traitsRemoved.contains(t)) {
            traitsRemoved.remove(t);
        } else {
            traitsAdded.add(t);
        }
    }

    public void removeTrait(Trait t) {
        if (traitsAdded.contains(t)) {
            traitsAdded.remove(t);
        } else {
            traitsRemoved.add(t);
        }        
    }

    public void modAttribute(Attribute a, int i) {
        attributes.put(a, attributes.getOrDefault(a, 0) + i);
    }
}
