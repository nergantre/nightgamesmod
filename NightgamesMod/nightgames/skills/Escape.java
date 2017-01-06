package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.pet.arms.skills.Grab;
import nightgames.stance.Neutral;
import nightgames.status.Collared;
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
                        || (getSelf().bound() && !getSelf().is(Stsflag.maglocked))) && getSelf().canRespond();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (blockedByCollar(c, target)) {
            return false;
        }
        if (getSelf().bound()) {
            if (getSelf().check(Attribute.Cunning, 5 - getSelf().escape(c, target))) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You slip your hands out of your restraints.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), getSelf().getName() + " manages to free " + getSelf().reflectivePronoun() + ".");
                }
                getSelf().free();
                c.getCombatantData(target).setIntegerFlag(Grab.FLAG, 0);
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), "You try to slip your restraints, but can't get free.");
                } else if (c.shouldPrintReceive(target, c)) {
                    c.write(getSelf(), getSelf().getName() + " squirms against " + getSelf().possessiveAdjective()
                                    + " restraints fruitlessly.");
                    c.write(getSelf(), String.format("%s squirms against %s restraints fruitlessly.", getSelf().getName(),
                                    getSelf().possessiveAdjective()));
                }
                getSelf().struggle();
                return false;
            }
        } else if (getSelf().check(Attribute.Cunning, 10 + target.get(Attribute.Cunning) - getSelf().escape(c, target))) {
            if (getSelf().human()) {
                if (getSelf().hasStatus(Stsflag.cockbound)) {
                    c.write(getSelf(), "You somehow managed to wiggle out of " + target.getName()
                                    + "'s iron grip on your dick.");
                    getSelf().removeStatus(Stsflag.cockbound);
                    return true;
                }
                c.write(getSelf(), "Your quick wits find a gap in " + target.getName() + "'s hold and you slip away.");
            } else if (c.shouldPrintReceive(target, c)) {
                if (getSelf().hasStatus(Stsflag.cockbound)) {
                    c.write(getSelf(),
                                    String.format("%s somehow managed to wiggle out of %s iron grip on %s dick.",
                                                    getSelf().pronoun(), target.nameOrPossessivePronoun(),
                                                    getSelf().possessiveAdjective()));
                    getSelf().removeStatus(Stsflag.cockbound);
                    return true;
                }
                c.write(getSelf(), String.format(
                                "%s goes limp and %s the opportunity to adjust %s grip on %s"
                                                + ". As soon as %s %s, %s bolts out of %s weakened hold. "
                                                + "It was a trick!",
                                getSelf().getName(), target.subjectAction("take"),
                                target.possessiveAdjective(), getSelf().directObject(),
                                target.pronoun(), target.action("move"), getSelf().pronoun(),
                                target.possessiveAdjective()));
            }
            c.setStance(new Neutral(getSelf(), target), getSelf(), true);
        } else {
            if (getSelf().human()) {
                if (getSelf().hasStatus(Stsflag.cockbound)) {
                    c.write(getSelf(), "You try to escape " + target.getName()
                                    + "'s iron grip on your dick. However, her pussy tongue has other ideas. She runs her tongue up and down your cock and leaves you gasping with pleasure.");
                    int m = 8;
                    getSelf().body.pleasure(target, target.body.getRandom("pussy"), getSelf().body.getRandom("cock"), m,
                                    c, this);
                } else if (getSelf().crotchAvailable()) {
                    c.write(getSelf(), "You try to take advantage of an opening in " + target.getName()
                                    + "'s stance to slip away, but she catches you by your protruding penis and reasserts her position.");
                } else {
                    c.write(getSelf(), "You think you see an opening in " + target.getName()
                                    + "'s stance, but she corrects it before you can take advantage.");
                }
            } else if (c.shouldPrintReceive(target, c)) {
                c.write(getSelf(),
                                String.format("%s manages to slip out of %s grip for a moment, but %s %s %s "
                                                + "before %s can get far and %s control.",
                                                getSelf().getName(), target.nameOrPossessivePronoun(),
                                                target.pronoun(), target.action("tickle"), getSelf().directObject(),
                                                getSelf().pronoun(), getSelf().action("regain")));
            }
            getSelf().struggle();
            return false;
        }
        return true;
    }

    private boolean blockedByCollar(Combat c, Character target) {
        Collared stat = (Collared) getSelf().getStatus(Stsflag.collared);
        if (stat != null) {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:try|tries} to struggle, but"
                            + " the collar is having none of it and shocks {self:direct-object}"
                            + " into submission.", getSelf(), target));
            getSelf().pain(c, null, Global.random(20, 50));
            stat.spendCharges(c, 2);
            return true;
        }
        return false;
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
