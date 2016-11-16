package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;
import nightgames.stance.Stance;

public class UseOnahole extends Skill {

    public UseOnahole(Character self) {
        super(Item.Onahole.getName(), self);
        addTag(SkillTag.usesToy);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public int accuracy(Combat c) {
        return c.getStance().en == Stance.neutral ? 35 : 100;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return (getSelf().has(Item.Onahole) || getSelf().has(Item.Onahole2)) && getSelf().canAct() && target.hasDick()
                        && c.getStance().reachBottom(getSelf()) && target.crotchAvailable()
                        && !c.getStance().inserted(target);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = 5 + Global.random(10);

        if (target.roll(getSelf(), c, accuracy(c))) {
            if (getSelf().has(Item.Onahole2)) {
                m += 5;
                if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.upgrade, target));
                } else {
                    c.write(getSelf(), deal(c, 0, Result.upgrade, target));
                }
            } else {
                if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.normal, target));
                } else {
                    c.write(getSelf(), deal(c, 0, Result.upgrade, target));
                }
            }
            m = (int)getSelf().modifyDamage(DamageType.gadgets, target, m);
            target.body.pleasure(getSelf(), null, target.body.getRandomCock(), m, c, this);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new UseOnahole(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to stick your onahole onto " + target.name() + "'s dick, but she manages to avoid it.";
        } else if (modifier == Result.upgrade) {
            return "You slide your onahole over " + target.name()
                            + "'s dick. The well-lubricated toy covers her member with minimal resistance. As you pump it, she moans in "
                            + "pleasure and her hips buck involuntarily.";
        } else {
            return "You stick your cocksleeve onto " + target.name()
                            + "'s erection and rapidly pump it. She squirms a bit at the sensation and can't quite stifle a moan.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to stick a cocksleeve on %s dick, but %s %s to avoid it.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            target.pronoun(), target.action("manage"));
        } else if (modifier == Result.upgrade) {
            return String.format("%s slides %s cocksleeve over %s dick and starts pumping it. "
                            + "The sensation is the same as if %s was riding %s, but %s %s the only "
                            + "one who's feeling anything.", getSelf().subject(), getSelf().possessivePronoun(),
                            target.nameOrPossessivePronoun(), getSelf().subject(), target.directObject(),
                            target.pronoun(), target.action("are", "is"));
        } else {
            return String.format("%s forces a cocksleeve over %s erection and begins to pump it. "
                            + "At first the feeling is strange and a little bit uncomfortable, but the "
                            + "discomfort gradually becomes pleasure.", getSelf().subject(),
                            target.nameOrPossessivePronoun());
        }

    }

    @Override
    public String describe(Combat c) {
        return "Pleasure opponent with an Onahole";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
