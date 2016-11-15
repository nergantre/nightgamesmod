package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.stance.Position;
import nightgames.stance.Stance;
import nightgames.status.ArmLocked;
import nightgames.status.LegLocked;
import nightgames.status.Stsflag;

public class SuccubusSurprise extends Skill {

    public SuccubusSurprise(Character self) {
        super("Succubus Surprise", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 15 || user.get(Attribute.Cunning) >= 15;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && !getSelf().has(Trait.succubus) && getSelf().has(Item.SuccubusDraft)
                        && c.getStance().inserted(target) && !c.getStance().anallyPenetrated(c)
                        && !BodyPart.hasOnlyType(c.getStance().topParts(c), "strapon") && c.getStance().sub(getSelf())
                        && getSelf().canSpend(getMojoCost(c)) && !target.is(Stsflag.armlocked)
                        && !target.is(Stsflag.leglocked);
    }

    @Override
    public String describe(Combat c) {
        return "Use a Succubus Draft and latch unto your opponent.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean oppHasBlessed = c.getStance().insertedPartFor(c, target).getMod(target).countsAs(target, CockMod.blessed);
        if (getSelf().human()) {
            if (oppHasBlessed) {
                c.write(getSelf(), deal(c, 0, Result.weak, target));
            } else {
                c.write(getSelf(), deal(c, 0, Result.normal, target));
            }
        } else {
            if (oppHasBlessed) {
                c.write(getSelf(), receive(c, 0, Result.weak, target));
            } else {
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            }
        }
        getSelf().remove(Item.SuccubusDraft);
        Item.SuccubusDraft.getEffects().forEach(e -> e.use(c, getSelf(), target, Item.SuccubusDraft));
        if (isArmLock(c.getStance())) {
            target.add(c, new ArmLocked(target, 4 * getSelf().get(Attribute.Power)));
        } else {
            target.add(c, new LegLocked(target, 4 * getSelf().get(Attribute.Power)));
        }
        new Grind(getSelf()).resolve(c, target);

        if (!getSelf().human() && target.human() && !oppHasBlessed
                        && getSelf().getType().equals("CUSTOM_NPCSamantha")) {
            c.write(getSelf(), "<br><br>\"<i>Do you like your surprise, " + target.name() + "? I do.\"</i>");
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new SuccubusSurprise(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        String result = String.format(
                        "You might be on the receiving end here, but that"
                                        + " doesn't mean you should just give up! You distract %s for a moment,"
                                        + " just long enough to bring a very special bottle to your lips. When %s"
                                        + " notices, %s tries and snatch it away, but you already had swallowed"
                                        + " enough. A sultry wave washes over you as the draft takes effect, and you ",
                        target.name(), target.pronoun(), target.pronoun());
        if (isArmLock(c.getStance())) {
            result += String.format("grab %s hands and pull %s deeper into you. ", target.possessivePronoun(),
                            target.directObject());
        } else {
            result += String.format("wrap your legs around %s, trapping %s within.", target.directObject(),
                            target.directObject());
        }
        if (modifier == Result.weak) {
            result += String.format(
                            "%s does not seem too worried, and you can see why when"
                                            + " your new succubus pussy fails to steal even the faintest wisp of energy.",
                            target.name());
        } else {
            result += String.format("Realizing what is going on, %s frantically tries to pull out, "
                            + "but your hold is unrelenting. You grind against %s, and soon the "
                            + "energy starts flowing.", target.name(), target.directObject());
        }
        return result;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        String result = String.format("Despite %s dominant position, %s seems unfazed."
                        + " %s twists %s head to the side and %s %s gaze, fearing"
                        + " another competitor may be about to crash %s party. There's no one"
                        + " there, though, and when %s back at %s, %s has already downed"
                        + " a draft of some kind. %s grin widens as black wings and a tail form on %s back."
                        + " %s to pull out, but ", target.nameOrPossessivePronoun(),
                        getSelf().name(), getSelf().subject(), getSelf().possessivePronoun(),
                        target.subjectAction("follow"), getSelf().possessivePronoun(), 
                        target.subjectAction("look"), getSelf().name(), getSelf().directObject(),
                        getSelf().pronoun(), Global.capitalizeFirstLetter(getSelf().possessivePronoun()),
                        getSelf().possessivePronoun(), 
                        Global.capitalizeFirstLetter(target.subjectAction("try", "tries")));
        if (isArmLock(c.getStance())) {
            result += String.format("%s grabs %s hands tightly to %s body, holding %s in place. ",
                            getSelf().subject(), target.possessivePronoun(), 
                            getSelf().possessivePronoun(), target.directObject());
        } else {
            result += String.format("%s wraps %s lithe legs around %s waist, keeping %s inside.",
                            getSelf().subject(), getSelf().possessivePronoun(),
                            target.possessivePronoun(), target.directObject());
        }
        if (modifier == Result.weak) {
            result += String.format(" Luckily%s, the blessings on %s cock prevent any serious damage.",
                            target.human() ? "" : " for " + target.directObject(), 
                                            target.nameOrPossessivePronoun());
        } else {
            result += String.format(" %s fears are confirmed as %s %s a terrible suction starting "
                            + "on %s cock, drawing out %s strength.",
                            Global.capitalizeFirstLetter(target.nameOrPossessivePronoun()),
                            target.pronoun(), target.action("feel"),
                            target.possessivePronoun(), target.possessivePronoun());
        }
        return result;
    }

    private boolean isArmLock(Position p) {
        if (p.en == Stance.missionary) {
            return false;
        }
        return true;
    }

    @Override
    public int getMojoCost(Combat c) {
        return Math.max(10, 50 - getSelf().get(Attribute.Technique));
    }

    @Override
    public float priorityMod(Combat c) {
        return 0f;
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
