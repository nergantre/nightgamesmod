package nightgames.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import nightgames.characters.Character;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.skills.strategy.CombatStrategy;

public class CombatantData implements Cloneable {
    private List<Clothing> clothespile;
    private Map<String, Number> flags;
    private String lastUsedSkillName;
    private List<Item> removedItems;
    private Optional<CombatStrategy> strategy;
    private int strategyDuration;
    
    public CombatantData() {
        clothespile = new ArrayList<>();
        flags = new HashMap<String, Number>();
        setLastUsedSkillName("None");
        removedItems = new ArrayList<>();
        strategy = Optional.empty();
        strategyDuration = 0;
    }

    @Override
    public Object clone() {
        CombatantData newData;
        try {
            newData = (CombatantData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
        newData.clothespile = new ArrayList<>(clothespile);
        newData.flags = new HashMap<>(flags);
        newData.setLastUsedSkillName(lastUsedSkillName);
        newData.removedItems = new ArrayList<>(removedItems);
        if (strategy.isPresent()) {
            newData.strategy = Optional.of(strategy.get().instance());
        }
        return newData;
    }

    public void addToClothesPile(Clothing article) {
        if (article != null) {
            clothespile.add(article);
        }
    }

    public List<Clothing> getClothespile() {
        return clothespile;
    }

    public void toggleFlagOn(String key, boolean val) {
        flags.put(key, val ? 1 : 0);
    }

    public void setIntegerFlag(String key, int val) {
        flags.put(key, val);
    }

    public void setFloatFlag(String key, int val) {
        flags.put(key, val);
    }

    public int getIntegerFlag(String key) {
        return flags.containsKey(key) ? flags.get(key).intValue() : 0;
    }

    public boolean getBooleanFlag(String key) {
        return flags.containsKey(key) ? flags.get(key).intValue() != 0 : false;
    }

    public float getFloatFlag(String key) {
        return flags.containsKey(key) ? flags.get(key).floatValue() : 0f;
    }

    public String getLastUsedSkillName() {
        return lastUsedSkillName;
    }

    public void setLastUsedSkillName(String lastUsedSkillName) {
        this.lastUsedSkillName = lastUsedSkillName;
    }
    
    public void loseItem(Item item) {
        removedItems.add(item);
    }
    
    public List<Item> getRemovedItems() {
        return Collections.unmodifiableList(removedItems);
    }
    
    public Optional<CombatStrategy> getStrategy() {
        return strategy;
    }

    public void setStrategy(Combat c, Character self, CombatStrategy strategy) {
        this.strategy = Optional.ofNullable(strategy);
        this.strategyDuration = strategy.initialDuration(c, self);
        if (Global.isDebugOn(DebugFlags.DEBUG_STRATEGIES)) {
            System.out.printf("%s is now using %s\n", self.getName(), strategy.getClass().getSimpleName());
        }
    }

    public void tick() {
        strategyDuration -= 1;
        if (strategyDuration <= 0) {
            strategyDuration = 0;
            strategy = Optional.empty();
        }
        if (Global.isDebugOn(DebugFlags.DEBUG_STRATEGIES)) {
            System.out.printf("%s is now at %s\n", strategy.getClass().getSimpleName(), String.valueOf(strategyDuration));
        }
    }
}
