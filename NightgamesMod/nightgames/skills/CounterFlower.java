package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.FlowerSex;

public class CounterFlower extends CounterBase {
    public CounterFlower(Character self) {
        super("Flower Counter", self, 5,
                        Global.format("<b>The giant flower at the base of {self:name-possessive} legs are open, with the petals waving invitingly.",
                                        self, self),
                        2);
        addTag(SkillTag.fucking);
        addTag(SkillTag.positioning);
    }

    @Override
    public float priorityMod(Combat c) {
        return Global.randomfloat() * 2;
    }

    @Override
    public int speed() {
        return -20;
    }

    @Override
    public String getBlockedString(Combat c, Character target) {
        return Global.format(
                        "{self:SUBJECT-ACTION:block|blocks} {other:name-possessive} assault with a vine and {self:action:shoot|shoots} out {self:possessive} vines to drag {other:direct-object} into {self:possessive} flower. "
                                        + "However, {other:subject-action:were|was} wary of {self:direct-object} and {other:action:jump|jumps} back before {self:subject} can catch {other:direct-object}.",
                        getSelf(), target);
    }

    @Override
    public void resolveCounter(Combat c, Character target) {
        target.nudify();
        if (target.hasDick() && getSelf().hasPussy()) {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            }
            c.setStance(new FlowerSex(getSelf(), target), getSelf(), true);
            new Thrust(getSelf()).resolve(c, target);
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.miss, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.miss, target));
            }
        }
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Bio) >= 15;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().has(Trait.dryad) && !c.getStance().dom(getSelf()) && !c.getStance().dom(target) && getSelf().canAct() && getSelf().hasPussy()
                        && target.hasDick();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 40;
    }

    @Override
    public String describe(Combat c) {
        return "Counters with vines, trapping them in your flower.";
    }

    @Override
    public Skill copy(Character user) {
        return new CounterFlower(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.setup) {
            return Global.format("You open up the flower at the base of your legs and get ready for a counter.",
                            getSelf(), target);
        } else if (modifier == Result.miss) {
            return Global.format(
                            "You shoot out your vines and drag {other:name-do} into your flower. You urge {other:possessive} hips forward into yours, but "
                                            + "you discover that you do not have the right equipment for the job. Whoops!",
                            getSelf(), target);
        } else {
            return Global.format(
                            "You shoot out your vines and drag {other:name-do} into your flower. You shove {other:possessive} face between your breasts "
                                            + "and {other:possessive} cock inside your drenched flower cunt. "
                                            + "With a quick flick of your mind, you close the petals of your outer flower around yourselves, trapping {other:name} and you inside."
                                            + "",
                            getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.setup) {
            return Global.format(
                            "{self:SUBJECT} giggles softly and opens the flower at the base of {self:possessive} legs invitingly.",
                            getSelf(), target);
        } else if (modifier == Result.miss) {
            return Global.format(
                            "Numerous vines shoot out of her flower, entangling your body and stopping you in your tracks. "
                            + "With a salacious smile, {self:subject} uses her vines and drags {other:name-do} into {self:possessive} flower and deposits you in {self:possessive} arms. "
                            + " {self:PRONOUN} forces {other:possessive} hips forward before frowning"
                            + " when she discovers {other:pronoun-action:don't|doesn't} have the right equipment.",
                            getSelf(), target);
        } else {
            return Global.format(
                            "Numerous vines shoot out of her flower, entangling your body and stopping you in your tracks. "
                            + "With a salacious smile, {self:subject} uses her vines and drags {other:name-do} into {self:possessive} flower and deposits you in {self:possessive} arms. "
                            + "{self:PRONOUN} coils her limbs around {other:possessive}s, forcing {other:possessive}"
                            + " face inside her fragrant cleavage and {other:possessive} cock inside her warm sticky flower cunt.",
                            getSelf(), target);
        }
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
