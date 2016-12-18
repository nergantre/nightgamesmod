package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;

public class TakeOffShoes extends Skill {

    public TakeOffShoes(Character self) {
        super("Remove Shoes", self);
        addTag(SkillTag.undressing);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return (user.get(Attribute.Cunning) >= 5 && !user.human()) || target.body.getFetish("feet").isPresent() && getSelf().has(Trait.direct);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !getSelf().outfit.hasNoShoes();
    }

    @Override
    public String describe(Combat c) {
        return "Remove your own shoes";
    }

    @Override
    public float priorityMod(Combat c) {
        return c.getOpponent(getSelf()).body.getFetish("feet").isPresent() && c.getOpponent(getSelf()).body.getFetish("feet").get().magnitude > .25 ? 1.0f : -5.0f;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().strip(ClothingSlot.feet, c);
        if (target.body.getFetish("feet").isPresent() && target.body.getFetish("feet").get().magnitude > .25) {
            writeOutput(c, Result.special, target);
            target.temptWithSkill(c, getSelf(), getSelf().body.getRandom("feet"), Global.random(17, 26), this);
        } else {
            writeOutput(c, Result.normal, target);
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TakeOffShoes(user);
    }

    @Override
    public Tactics type(Combat c) {
        Character target = c.getOpponent(getSelf());
        return target.body.getFetish("feet").isPresent() && target.body.getFetish("feet").get().magnitude > .25 ? Tactics.pleasure : Tactics.misc;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return Global.format("{self:SUBJECT} take a moment to slide off {self:possessive} footwear with slow exaggerated motions. {other:SUBJECT-ACTION:gulp|gulps}. "
                            + "While {other:pronoun-action:know|knows} what {self:pronoun} are doing, it changes nothing as desire fills {other:possessive} eyes.", getSelf(), target);
        }
        return "You take a moment to kick off your footwear.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return Global.format("{self:SUBJECT} takes a moment to slide off {self:possessive} footwear with slow exaggerated motions. {other:SUBJECT-ACTION:gulp|gulps}. "
                            + "While {other:pronoun-action:know|knows} what {self:pronoun} is doing, it changes nothing as desire fills {other:possessive} eyes.", getSelf(), target);
        }
        return getSelf().subject() + " takes a moment to kick off " + getSelf().possessivePronoun() + " footwear.";
    }
}
