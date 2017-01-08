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
import nightgames.pet.arms.ArmManager;
import nightgames.skills.Skill;
import nightgames.skills.damage.Staleness;
import nightgames.skills.strategy.CombatStrategy;
import nightgames.skills.strategy.DefaultStrategy;

public class CombatantData implements Cloneable {
    private List<Clothing> clothespile;
    private Map<String, Number> flags;
    private String lastUsedSkillName;
    private List<Item> removedItems;
    private Optional<CombatStrategy> strategy;
    private int strategyDuration;
    private Map<Skill, Double> moveModifiers;
    private ArmManager manager;

    public CombatantData() {
        clothespile = new ArrayList<>();
        flags = new HashMap<String, Number>();
        setLastUsedSkillName("None");
        removedItems = new ArrayList<>();
        strategy = Optional.empty();
        strategyDuration = 0;
        moveModifiers = new HashMap<>();
        manager = new ArmManager();
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
        // strategies should always be stateless.
        newData.strategy = strategy;
        newData.moveModifiers = new HashMap<>(moveModifiers);
        return newData;
    }

    public void addToClothesPile(Character self, Clothing article) {
        if (article != null && self.outfitPlan.contains(article)) {
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

    public void setDoubleFlag(String key, double d) {
        flags.put(key, d);
    }

    public int getIntegerFlag(String key) {
        return flags.containsKey(key) ? flags.get(key).intValue() : 0;
    }

    public boolean getBooleanFlag(String key) {
        return flags.containsKey(key) ? flags.get(key).intValue() != 0 : false;
    }

    public void setBooleanFlag(String key, boolean val) {
        flags.put(key, val ? Integer.valueOf(1) : Integer.valueOf(0));
    }

    public double getDoubleFlag(String key) {
        return flags.getOrDefault(key, Double.valueOf(0)).doubleValue();
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
            System.out.printf("%s is now using %s\n", self.getTrueName(), strategy.getClass().getSimpleName());
        }
    }

    public void tick(Combat c) {
        strategyDuration -= 1;
        if (strategyDuration <= 0) {
            strategyDuration = 0;
            strategy = Optional.empty();
        }
        if (Global.isDebugOn(DebugFlags.DEBUG_STRATEGIES)) {
            System.out.printf("%s is now at %s\n", strategy.orElse(new DefaultStrategy()).getClass().getSimpleName(), String.valueOf(strategyDuration));
        }
        for (Skill skill : moveModifiers.keySet()) {
            if (!lastUsedSkillName.equals(skill.getName())) {
                double staleness = moveModifiers.get(skill);
                if (staleness < .999) {
                    moveModifiers.put(skill, Math.min(1.0, staleness + skill.getStaleness().recovery));
                }
                if (staleness > 1.01) {
                    moveModifiers.put(skill, Math.max(1.0, staleness - Math.abs(skill.getStaleness().recovery)));
                }
            }
        }
    }

    public double getMoveModifier(Skill skill) {
        return moveModifiers.getOrDefault(skill, skill.getStaleness().defaultAmount);
    }

    public void decreaseMoveModifier(Combat c, Skill skill) {
        double currentAmount = moveModifiers.getOrDefault(skill, 1.0);
        Staleness staleness = skill.getStaleness();
        double amount = Math.max(currentAmount - staleness.decay, Math.min(staleness.floor, currentAmount));
        moveModifiers.put(skill, amount);
    }

    public void increaseIntegerFlag(String key, int i) {
        setIntegerFlag(key, getIntegerFlag(key) + i);
    }

    public ArmManager getManager() {
        return manager;
    }

    protected void setManager(ArmManager manager) {
        this.manager = manager.instance();
    }
}
