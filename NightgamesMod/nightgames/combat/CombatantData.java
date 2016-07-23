package nightgames.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nightgames.items.Item;
import nightgames.items.clothing.Clothing;

public class CombatantData implements Cloneable {
    private List<Clothing> clothespile;
    private Map<String, Number> flags;
    private String lastUsedSkillName;
    private List<Item> removedItems;
    
    public CombatantData() {
        clothespile = new ArrayList<>();
        flags = new HashMap<String, Number>();
        setLastUsedSkillName("None");
        removedItems = new ArrayList<>();
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
}
