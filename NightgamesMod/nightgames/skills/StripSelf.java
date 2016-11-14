package nightgames.skills;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Decider;
import nightgames.characters.NPC;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.nskills.tags.SkillTag;

public class StripSelf extends Skill {
    public StripSelf(Character self) {
        super("Strip Self", self);
        addTag(SkillTag.suicidal);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 3;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        boolean hasClothes = subChoices().size() > 0;
        return hasClothes && getSelf().canAct() && c.getStance().mobile(getSelf()) && !getSelf().isPet();
    }

    @Override
    public Collection<String> subChoices() {
        return getSelf().getOutfit().getAllStrippable().stream().map(clothing -> clothing.getName())
                        .collect(Collectors.toList());
    }

    @Override
    public float priorityMod(Combat c) {
        return -4f;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Clothing clothing = null;
        if (getSelf().human()) {
            Optional<Clothing> stripped = getSelf().getOutfit().getEquipped().stream()
                            .filter(article -> article.getName().equals(choice)).findAny();
            if (stripped.isPresent()) {
                clothing = getSelf().getOutfit().unequip(stripped.get());
                c.getCombatantData(getSelf()).addToClothesPile(clothing);
            }
        } else if (getSelf() instanceof NPC) {
            NPC self = (NPC) getSelf();
            HashMap<Clothing, Double> checks = new HashMap<>();
            double selfFit = self.getFitness(c);
            double otherFit = self.getOtherFitness(c, target);
            getSelf().getOutfit().getAllStrippable().stream().forEach(article -> {
                double rating = Decider.rateAction(self, c, selfFit, otherFit, (newCombat, newSelf, newOther) -> {
                    newSelf.strip(article, newCombat);
                    return true;
                });
                checks.put(article, rating);
            });
            if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
                checks.entrySet().stream().forEach(entry -> {
                    System.out.println("Stripping " + entry.getKey() + ": " + entry.getValue());
                });
            }
            Clothing best = checks.entrySet().stream().max((first, second) -> {
                double test = second.getValue() - first.getValue();
                if (test < 0) {
                    return -1;
                }
                if (test > 0) {
                    return 1;
                }
                return 0;
            }).get().getKey();
            getSelf().strip(best, c);
            clothing = best;
        }
        if (clothing == null) {
            c.write(getSelf(), "Skill failed...");
        } else {
            c.write(getSelf(), Global.format(String.format("{self:SUBJECT-ACTION:strip|strips} off %s %s.",
                            getSelf().possessivePronoun(), clothing.getName()), getSelf(), target));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new StripSelf(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
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
        return "Strip yourself";
    }

    @Override
    public boolean makesContact() {
        return false;
    }
}
