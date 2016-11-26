package nightgames.skills;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.Decider;
import nightgames.characters.NPC;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.ItemEffect;
import nightgames.status.Stsflag;

public class UseDraft extends Skill {
    public UseDraft(Character self) {
        super("Use draft", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        boolean hasItems = subChoices().size() > 0;
        return hasItems && getSelf().canAct() && c.getStance().mobile(getSelf()) && !getSelf().isPet();
    }

    @Override
    public Collection<String> subChoices() {
        ArrayList<String> usables = new ArrayList<String>();
        for (Item i : getSelf().getInventory().keySet()) {
            if (getSelf().has(i) && i.getEffects().get(0).drinkable()) {
                usables.add(i.getName());
            }
        }
        return usables;
    }

    public Item pickBest(Combat c, NPC self, Character target, List<Item> usables) {
        HashMap<Item, Double> checks = new HashMap<>();
        double selfFitness = self.getFitness(c);
        double targetFitness = self.getOtherFitness(c, target);
        usables.stream().forEach(item -> {
            double rating = Decider.rateAction(self, c, selfFitness, targetFitness, (newCombat, newSelf, newOther) -> {
                for (ItemEffect e : item.getEffects()) {
                    e.use(newCombat, newSelf, newOther, item);
                }
                return true;
            });
            checks.put(item, rating);
        });
        if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
            checks.entrySet().stream().forEach(entry -> {
                System.out.println("Item " + entry.getKey() + ": " + entry.getValue());
            });
        }
        Item best = checks.entrySet().stream().max((first, second) -> {
            double test = second.getValue() - first.getValue();
            if (test < 0) {
                return -1;
            }
            if (test > 0) {
                return 1;
            }
            return 0;
        }).get().getKey();
        return best;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Item used = null;
        if (getSelf().human()) {
            for (Item i : getSelf().getInventory().keySet()) {
                if (i.getName().equals(choice)) {
                    used = i;
                    break;
                }
            }
        } else {
            ArrayList<Item> usables = new ArrayList<Item>();
            for (Item i : getSelf().getInventory().keySet()) {
                if (i.getEffects().get(0).drinkable()) {
                    usables.add(i);
                }
            }
            if (usables.size() > 0 && getSelf() instanceof NPC) {
                used = pickBest(c, (NPC) getSelf(), target, usables);
            }
        }
        if (used == null) {
            c.write(getSelf(), "Skill failed...");
        } else {
            boolean eventful = false;
            if (shouldPrint(target))
                c.write(getSelf(), Global.format(
                            String.format("{self:SUBJECT-ACTION:%s|%ss} %s%s", used.getEffects().get(0).getSelfVerb(),
                                            used.getEffects().get(0).getSelfVerb(), used.pre(), used.getName()),
                            getSelf(), target));
            for (ItemEffect e : used.getEffects()) {
                eventful = e.use(c, getSelf(), target, used) || eventful;
            }
            if (!eventful && shouldPrint(target)) {
                c.write(getSelf(), "...But nothing happened.");
            }
            getSelf().consume(used, 1);
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new UseDraft(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return "";
    }

    @Override
    public String describe(Combat c) {
        return "Drink a draft";
    }

    @Override
    public boolean makesContact() {
        return false;
    }
    
    private boolean shouldPrint(Character target) {
        return !target.human() || !target.is(Stsflag.blinded);
    }
}
