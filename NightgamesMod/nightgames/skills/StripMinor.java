package nightgames.skills;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;

public class StripMinor extends Skill {

    public StripMinor(Character self) {
        super("Strip Minor", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().get(Attribute.Cunning) >= 3;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        List<Clothing> strippable = target.outfit.getAllStrippable();
        boolean top = c.getStance()
                       .reachTop(getSelf())
                        && strippable.stream()
                                     .flatMap(a -> a.getSlots()
                                                    .stream())
                                     .anyMatch(StripMinor::isTop);
        boolean bottom = c.getStance()
                          .reachBottom(getSelf())
                        && strippable.stream()
                                     .flatMap(a -> a.getSlots()
                                                    .stream())
                                     .anyMatch(StripMinor::isBottom);
        return getSelf().canAct() && (top || bottom);
    }
    
    @Override
    public int getMojoCost(Combat c) {
        return c.getStance().dom(getSelf()) ? 0 : 8;
    }

    @Override
    public String describe(Combat c) {
        return "Attempt to remove a minor article of clothing from your opponent.";
    }

    @Override
    public Collection<String> subChoices(Combat c) {
        return c.getOpponent(getSelf()).getOutfit()
                        .getAllStrippable()
                        .stream()
                        .filter(a -> !(a.getSlots()
                                        .contains(ClothingSlot.top)
                                        || a.getSlots()
                                            .contains(ClothingSlot.bottom)))
                        .map(clothing -> clothing.getName())
                        .collect(Collectors.toList());
    }

    @Override
    public float priorityMod(Combat c) {
        return -2f; // minor clothing is usually not important, don't waste a turn
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Clothing clothing;
        ClothingSlot slot;
        if (getSelf().human()) {
            Optional<Clothing> stripped = target.getOutfit()
                                                   .getEquipped()
                                                   .stream()
                                                   .filter(article -> article.getName()
                                                                             .equals(choice))
                                                   .findAny();
            if (stripped.isPresent()) {
                clothing = stripped.get();
                slot = (ClothingSlot) clothing.getSlots()
                                              .toArray()[0];
            } else {
                c.writeSystemMessage("<b>Error: Tried to do StripMinor, but the sub choice disappeared?</b>");
                return false;
            }
        } else {
            slot = Global.pickRandom(target.outfit.getAllStrippable()
                                .stream()
                                .filter(a -> Collections.disjoint(a.getSlots(), Arrays.asList(ClothingSlot.top, ClothingSlot.bottom)))
                                .flatMap(a -> a.getSlots()
                                               .stream())
                                .collect(Collectors.toList())).orElse(null);
            if (slot == null) {
                c.write(getSelf(), getSelf().subject() + " tried to strip something minor, but nothing was there.");
                return false;
            }
            clothing = target.outfit.getTopOfSlot(slot);
        }
        int difficulty = target.getOutfit()
                               .getTopOfSlot(slot)
                               .dc()
                        + target.getLevel() + (target.getStamina()
                                                     .percent()
                                        / 4
                                        - target.getArousal()
                                                .percent())
                                        / 5
                        - (!target.canAct() || c.getStance()
                                                .sub(target) ? 20 : 0);
        difficulty -= 15;
        if (getSelf().check(Attribute.Cunning, difficulty) || !target.canAct()) {
            c.write(getSelf(),
                            Global.format("{self:SUBJECT-ACTION:reach|reaches} for"
                                            + " {other:name-possessive} %s and {self:action:pull|pulls} "
                                            + "it away from {other:direct-object}.", getSelf(), target,
                                            clothing.getName()));
            target.strip(clothing, c);
        } else {
            c.write(getSelf(),
                            Global.format("{self:SUBJECT-ACTION:try|tries} to remove"
                                            + " {other:name-possessive} %s, but {other:pronoun-action:keep|keeps}"
                                            + " it in place.", getSelf(), target, clothing.getName()));
            return false;
        }

        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new StripMinor(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    private static boolean isTop(ClothingSlot slot) {
        return slot == ClothingSlot.head || slot == ClothingSlot.neck || slot == ClothingSlot.arms
                        || slot == ClothingSlot.hands;
    }

    private static boolean isBottom(ClothingSlot slot) {
        return slot == ClothingSlot.legs || slot == ClothingSlot.feet;
    }

}
