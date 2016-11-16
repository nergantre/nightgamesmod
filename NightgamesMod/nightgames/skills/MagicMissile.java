package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;

public class MagicMissile extends Skill {

    public MagicMissile(Character self) {
        super("Magic Missile", self);
        addTag(SkillTag.hurt);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Arcane) >= 1;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf());
    }

    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    @Override
    public String describe(Combat c) {
        return "Fires a small magic projectile: 5 Mojo";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            double m = Global.random(10, 20);
            if (target.mostlyNude() && Global.random(3) == 2) {
                writeOutput(c, Result.critical, target);
                m *= 2;
                target.emote(Emotion.angry, 10);
            } else {
                writeOutput(c, Result.normal, target);
                target.emote(Emotion.angry, 5);
            }
            target.pain(c, getSelf(), (int) getSelf().modifyDamage(DamageType.arcane, target, m));
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new MagicMissile(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.damage;
    }

    @Override
    public int accuracy(Combat c) {
        return 80;
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You fire a bolt of magical energy, but " + target.name() + " narrowly dodges out of the way.";
        } else if (modifier == Result.critical) {
            if (target.hasBalls()) {
                return "You cast and fire a magic missile at " + target.name()
                                + ". Just by luck, it hits her directly in the jewels. She cringes in pain, cradling her bruised parts.";
            } else {
                return "You cast and fire a magic missile at " + target.name()
                                + ". By chance, it flies under her guard and hits her solidly in the pussy. She doubles over "
                                + "with a whimper, holding her bruised parts.";
            }
        } else {
            return "You hurl a magic missile at " + target.name() + ", hitting and staggering her a step.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s %s start to cast a spell and %s %s to"
                            + " the left, just in time to avoid the missile.",
                            target.subjectAction("see"), getSelf().subject(),
                            target.pronoun(), target.action("jump"));
        } else if (modifier == Result.critical) {
            return String.format("%s casts a quick spell and fires a bolt of magic into %s vulnerable "
                            + "groin. %s %s injured plums as pain saps the strength from %s "
                            + "legs.", getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.subjectAction("cup"), target.possessivePronoun(),
                            target.possessivePronoun());
        } else {
            return String.format("%s hand glows as %s casts a spell. Before %s can react, %s "
                            + "struck with an impact like a punch in the gut.", getSelf().subject(),
                            getSelf().pronoun(),
                            target.pronoun(), target.subjectAction("are", "is"));
        }
    }

}
