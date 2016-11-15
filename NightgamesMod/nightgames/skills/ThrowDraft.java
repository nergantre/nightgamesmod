package nightgames.skills;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nightgames.characters.Character;
import nightgames.characters.Decider;
import nightgames.characters.NPC;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.ItemEffect;

public class ThrowDraft extends Skill {
    private static final Set<Item> transformativeItems = new HashSet<>();

    {
        transformativeItems.add(Item.SuccubusDraft);
        transformativeItems.add(Item.BustDraft);
        transformativeItems.add(Item.TinyDraft);
        transformativeItems.add(Item.TentacleTonic);
        transformativeItems.add(Item.PriapusDraft);
        transformativeItems.add(Item.FemDraft);
    }

    public ThrowDraft(Character self) {
        super("Throw draft", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        boolean hasItems = subChoices().size() > 0;
        return hasItems && getSelf().canAct() && c.getStance().mobile(getSelf())
                        && (c.getStance().reachTop(getSelf()) || c.getStance().reachBottom(getSelf())) && !getSelf().isPet();
    }

    @Override
    public Collection<String> subChoices() {
        ArrayList<String> usables = new ArrayList<String>();
        for (Item i : getSelf().getInventory().keySet()) {
            if (getSelf().has(i) && i.getEffects().get(0).throwable()) {
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
                    e.use(newCombat, newOther, newSelf, item);
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
                if (i.getEffects().get(0).throwable()) {
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
            String verb = used.getEffects().get(0).getOtherVerb();
            if (verb.isEmpty()) {
                verb = "throw";
            }
            c.write(getSelf(), Global.format(
                            String.format("{self:SUBJECT-ACTION:%s|%ss} %s%s.", verb, verb, used.pre(), used.getName()),
                            getSelf(), target));
            if (transformativeItems.contains(used) && target.has(Trait.stableform)) {
                c.write(target, "...But nothing happened (Stable Form).");
            } else {
                boolean eventful = false;
                for (ItemEffect e : used.getEffects()) {
                    eventful = e.use(c, target, getSelf(), used) || eventful;
                }
                if (!eventful) {
                    c.write(getSelf(), "...But nothing happened.");
                }
            }
            getSelf().consume(used, 1);
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ThrowDraft(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
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
        return "Throw a draft at your opponent";
    }

    @Override
    public boolean makesContact() {
        return false;
    }
}
