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
        // ignore target for now... because yup I don't want to refactor subchoices just yet.
        List<Clothing> strippable = getStrippableArticles(c);
        return getSelf().canAct() && !strippable.isEmpty();
    }

    @Override
    public int getMojoCost(Combat c) {
        return c.getStance().dom(getSelf()) ? 0 : 8;
    }

    @Override
    public String describe(Combat c) {
        return "Attempt to remove a minor article of clothing from your opponent.";
    }

    private static final List<ClothingSlot> TOP_SLOTS = Arrays.asList(ClothingSlot.head, ClothingSlot.neck, ClothingSlot.arms, ClothingSlot.hands);
    private static final List<ClothingSlot> BOTTOM_SLOTS = Arrays.asList(ClothingSlot.legs, ClothingSlot.feet);

    private boolean canStripArticle(Combat c, Clothing article) {
        // if it contains top or bottom, don't let character use strip minor. That's in strip top/bottom
        if (Collections.disjoint(article.getSlots(), Arrays.asList(ClothingSlot.top, ClothingSlot.bottom))) {
            // if you can reach top and there's something to strip from the top, then do that
            if (!Collections.disjoint(article.getSlots(), TOP_SLOTS) && c.getStance().reachTop(getSelf())) {
                return true;
            }
            // if you can reach bottom and there's something to strip from the bottom, then do that
            if (!Collections.disjoint(article.getSlots(), BOTTOM_SLOTS) && c.getStance().reachBottom(getSelf())) {
                return true;
            }
        }
        return false;
    }

    private List<Clothing> getStrippableArticles(Combat c) {
        return c.getOpponent(getSelf()).getOutfit()
                        .getAllStrippable()
                        .stream()
                        .filter(article -> canStripArticle(c, article))
                        .collect(Collectors.toList());
    }

    @Override
    public Collection<String> subChoices(Combat c) {
        return getStrippableArticles(c).stream().map(Clothing::getName).map(Global::capitalizeFirstLetter).collect(Collectors.toList());
    }

    @Override
    public float priorityMod(Combat c) {
        return -2f; // minor clothing is usually not important, don't waste a turn
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Clothing clothing;
        Optional<Clothing> articleToStrip;
        if (getSelf().human()) {
            articleToStrip = target.getOutfit().getEquipped()
                                                   .stream()
                                                   .filter(article -> article.getName().toLowerCase().equals(choice.toLowerCase()))
                                                   .findAny();
        } else {
            articleToStrip = Global.pickRandom(getStrippableArticles(c));
        }
        if (!articleToStrip.isPresent()) {
            c.write(getSelf(), Global.format("{self:SUBJECT} tried to go after {other:name-possessive} clothing, "
                            + "but found that the intended piece is already gone.", getSelf(), target));
            return false;
        }
        clothing = articleToStrip.get();
        int difficulty = clothing.dc() 
                        + target.getLevel()
                        + (target.getStamina().percent() / 4
                        - target.getArousal().percent()) / 5
                        - (!target.canAct() || c.getStance().sub(target) ? 20 : 0);
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
}
