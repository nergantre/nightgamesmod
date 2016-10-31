package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Neutral;
import nightgames.status.Stsflag;

public class Escape extends Skill {
    public Escape(Character self) {
        super("Escape", self);
        addTag(SkillTag.positioning);
        addTag(SkillTag.escaping);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        if (target.hasStatus(Stsflag.cockbound)) {
            return false;
        }
        return (c.getStance()
                 .sub(getSelf())
                        && !c.getStance()
                             .mobile(getSelf())
                        || getSelf().bound()) && getSelf().canRespond();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().bound()) {
            if (getSelf().check(Attribute.Cunning, 5 - getSelf().escape(c))) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You slip your hands out of your restraints.");
                } else if (c.shouldPrintReceive(target)) {
                    c.write(getSelf(), getSelf().name() + " manages to free " + getSelf().reflectivePronoun() + ".");
                }
                getSelf().free();
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), "You try to slip your restraints, but can't get free.");
                } else if (c.shouldPrintReceive(target)) {
                    c.write(getSelf(), getSelf().name() + " squirms against " + getSelf().possessivePronoun()
                                    + " restraints fruitlessly.");
                    c.write(getSelf(), String.format("%s squirms against %s restraints fruitlessly.", getSelf().name(),
                                    getSelf().possessivePronoun()));
                }
                getSelf().struggle();
                return false;
            }
        } else if (getSelf().check(Attribute.Cunning, 10 + target.get(Attribute.Cunning) - getSelf().escape(c))) {
            if (getSelf().human()) {
                if (getSelf().hasStatus(Stsflag.cockbound)) {
                    c.write(getSelf(), "You somehow managed to wiggle out of " + target.name()
                                    + "'s iron grip on your dick.");
                    getSelf().removeStatus(Stsflag.cockbound);
                    return true;
                }
                c.write(getSelf(), "Your quick wits find a gap in " + target.name() + "'s hold and you slip away.");
            } else if (c.shouldPrintReceive(target)) {
                if (getSelf().hasStatus(Stsflag.cockbound)) {
                    c.write(getSelf(),
                                    String.format("%s somehow managed to wiggle out of %s iron grip on %s dick.",
                                                    getSelf().pronoun(), target.nameOrPossessivePronoun(),
                                                    getSelf().possessivePronoun()));
                    getSelf().removeStatus(Stsflag.cockbound);
                    return true;
                }
                c.write(getSelf(), String.format(
                                "%s goes limp and %s the opportunity to adjust %s grip on %s"
                                                + ". As soon as %s %s, %s bolts out of %s weakened hold. "
                                                + "It was a trick!",
                                getSelf().name(), target.subjectAction("take"),
                                target.possessivePronoun(), getSelf().directObject(),
                                target.pronoun(), target.action("move"), getSelf().pronoun(),
                                target.possessivePronoun()));
            }
            c.setStance(new Neutral(getSelf(), target));
        } else {
            if (getSelf().human()) {
                if (getSelf().hasStatus(Stsflag.cockbound)) {
                    c.write(getSelf(), "You try to escape " + target.name()
                                    + "'s iron grip on your dick. However, her pussy tongue has other ideas. She runs her tongue up and down your cock and leaves you gasping with pleasure.");
                    int m = 8;
                    getSelf().body.pleasure(target, target.body.getRandom("pussy"), getSelf().body.getRandom("cock"), m,
                                    c, this);
                } else if (getSelf().crotchAvailable()) {
                    c.write(getSelf(), "You try to take advantage of an opening in " + target.name()
                                    + "'s stance to slip away, but she catches you by your protruding penis and reasserts her position.");
                } else {
                    c.write(getSelf(), "You think you see an opening in " + target.name()
                                    + "'s stance, but she corrects it before you can take advantage.");
                }
            } else if (c.shouldPrintReceive(target)) {
                c.write(getSelf(),
                                String.format("%s manages to slip out of %s grip for a moment, but %s %s %s "
                                                + "before %s can get far and %s control.",
                                                getSelf().name(), target.nameOrPossessivePronoun(),
                                                target.pronoun(), target.action("tickle"), getSelf().directObject(),
                                                getSelf().pronoun(), getSelf().action("regain")));
            }
            getSelf().struggle();
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 8;
    }

    @Override
    public Skill copy(Character user) {
        return new Escape(user);
    }

    @Override
    public int speed() {
        return 1;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character attacker) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String describe(Combat c) {
        return "Uses Cunning to try to escape a submissive position";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
