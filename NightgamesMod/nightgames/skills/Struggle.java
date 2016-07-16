package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Neutral;
import nightgames.stance.Position;
import nightgames.stance.Stance;
import nightgames.status.Bound;
import nightgames.status.CockBound;
import nightgames.status.Stsflag;

public class Struggle extends Skill {

    public Struggle(Character self) {
        super("Struggle", self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        if (target.hasStatus(Stsflag.cockbound) || target.hasStatus(Stsflag.knotted)) {
            return false;
        }
        if (getSelf().hasStatus(Stsflag.cockbound) || getSelf().hasStatus(Stsflag.knotted)) {
            return true;
        }
        return (!c.getStance().mobile(getSelf()) && !c.getStance().dom(getSelf()) || getSelf().bound())
                        && getSelf().canRespond();
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().bound()) {
            Bound status = (Bound) target.getStatus(Stsflag.bound);
            if (getSelf().check(Attribute.Power, -getSelf().escape(c))) {
                if (getSelf().human()) {
                    if (status != null) {
                        c.write(getSelf(), "You manage to break free from the " + status + ".");
                    } else {
                        c.write(getSelf(), "You manage to snap the restraints that are binding your hands.");
                    }
                } else if (target.human()) {
                    if (status != null) {
                        c.write(getSelf(), getSelf().name() + " slips free from the " + status + ".");
                    } else {
                        c.write(getSelf(), getSelf().name() + " breaks free.");
                    }
                }
                getSelf().free();
            } else {
                if (getSelf().human()) {
                    if (status != null) {
                        c.write(getSelf(), "You struggle against the " + status + ", but can't get free.");
                    } else {
                        c.write(getSelf(), "You struggle against your restraints, but can't get free.");
                    }
                } else if (target.human()) {
                    if (status != null) {
                        c.write(getSelf(), getSelf().name() + " struggles against the " + status
                                        + ", but can't free her hands.");
                    } else {
                        c.write(getSelf(), getSelf().name() + " struggles, but can't free her hands.");
                    }
                }
                getSelf().struggle();
                return false;
            }
        } else if (c.getStance().havingSex()) {
            boolean knotted = getSelf().hasStatus(Stsflag.knotted);
            if (c.getStance().enumerate() == Stance.anal) {
                int diffMod = knotted ? 50 : 0;
                if (getSelf().check(Attribute.Power,
                                target.getStamina().get() / 2 - getSelf().getStamina().get() / 2
                                                + target.get(Attribute.Power) - getSelf().get(Attribute.Power)
                                                - getSelf().escape(c) + diffMod)) {
                    if (getSelf().human()) {
                        if (knotted) {
                            c.write(getSelf(), "With a herculean effort, you painfully force "
                                            + target.possessivePronoun()
                                            + " knot through your asshole, and the rest of her dick soon follows.");
                            getSelf().removeStatus(Stsflag.knotted);
                            getSelf().pain(c, 10);
                        } else {
                            c.write(getSelf(), "You manage to break away from " + target.name() + ".");
                        }
                    } else if (target.human()) {
                        if (knotted) {
                            c.write(getSelf(), getSelf().name()
                                            + " roughly pulls away from you, groaning loudly as the knot in your dick pops free of her ass.");
                            getSelf().removeStatus(Stsflag.knotted);
                            getSelf().pain(c, 10);
                        } else {
                            c.write(getSelf(), getSelf().name()
                                            + " pulls away from you and your dick slides out of her butt.");
                        }
                    }
                    c.setStance(new Neutral(getSelf(), target));
                } else {
                    if (getSelf().human()) {
                        if (knotted) {
                            c.write(getSelf(), "You try to force " + target.possessivePronoun()
                                            + " dick out of your ass, but the knot at its base is utterly unyielding.");
                        } else {
                            c.write(getSelf(), "You try to pull free, but " + target.name()
                                            + " has a good grip on your waist.");
                        }
                    } else if (target.human()) {
                        if (knotted) {
                            c.write(getSelf(),
                                            " frantically attempts to get your cock out of her ass, but your knot is keeping it inside her warm depths.");
                        } else {
                            c.write(getSelf(),
                                            getSelf().name() + " tries to squirm away, but you have better leverage.");
                        }
                    }
                    getSelf().struggle();
                    return false;
                }
            } else {
                int diffMod = 0;
                if (c.getStance().insertedPartFor(target).moddedPartCountsAs(target, CockMod.enlightened)) {
                    diffMod = 15;
                } else if (c.getStance().insertedPartFor(getSelf()).moddedPartCountsAs(getSelf(), CockMod.enlightened)) {
                    diffMod = -15;
                }
                if (getSelf().check(Attribute.Power,
                                target.getStamina().get() / 2 - getSelf().getStamina().get() / 2
                                                + target.get(Attribute.Power) - getSelf().get(Attribute.Power)
                                                - getSelf().escape(c) + diffMod)) {
                    if (getSelf().hasStatus(Stsflag.cockbound)) {
                        CockBound s = (CockBound) getSelf().getStatus(Stsflag.cockbound);
                        c.write(getSelf(),
                                        Global.format("With a strong pull, {self:subject} somehow managed to wiggle out of {other:possessive} iron grip on {self:possessive} dick. "
                                                        + "However the sensations of " + s.binding
                                                        + " sliding against {self:possessive} cockskin leaves {self:direct-object} gasping.",
                                        getSelf(), target));
                        int m = 15;
                        getSelf().body.pleasure(target, target.body.getRandom("pussy"),
                                        getSelf().body.getRandom("cock"), m, c);
                        getSelf().removeStatus(Stsflag.cockbound);
                    }
                    if (knotted) {
                        c.write(getSelf(),
                                        Global.format("{self:subject} somehow {self:SUBJECT-ACTION:manage|manages} to force {other:possessive} knot through {self:possessive} tight opening, stretching it painfully in the process.",
                                                        getSelf(), target));
                        getSelf().removeStatus(Stsflag.knotted);
                        getSelf().pain(c, 10);
                    }
                    boolean reverseStrapped = BodyPart.hasOnlyType(c.getStance().partsFor(target), "strapon");
                    boolean reversedStance = false;
                    if (!reverseStrapped) {
                        Position reversed = c.getStance().reverse(c);
                        if (reversed != c.getStance()) {
                            c.setStance(reversed);
                            reversedStance = true;
                        }
                    }
                    if (!reversedStance) {
                        c.write(getSelf(),
                                        Global.format("{self:SUBJECT-ACTION:manage|manages} to shake {other:direct-object} off.",
                                                        getSelf(), target));
                        c.setStance(new Neutral(getSelf(), target));
                    }
                } else {
                    if (getSelf().hasStatus(Stsflag.cockbound)) {
                        CockBound s = (CockBound) getSelf().getStatus(Stsflag.cockbound);
                        c.write(getSelf(),
                                        Global.format("{self:SUBJECT-ACTION:try|tries} to escape {other:possessive} iron grip on {self:possessive} dick. However, {other:possessive} "
                                                        + s.binding
                                                        + " has other ideas. {other:SUBJECT-ACTION:run|runs} {other:possessive} "
                                                        + s.binding
                                                        + " up and down {self:possessive} cock and leaves {self:direct-object} gasping with pleasure.",
                                        getSelf(), target));
                        getSelf().body.pleasure(target, target.body.getRandom("pussy"),
                                        getSelf().body.getRandom("cock"), 8, c);
                    } else if (getSelf().human()) {
                        if (c.getStance().inserted(getSelf())) {
                            c.write(getSelf(), "You try to tip " + target.name()
                                            + " off balance, but she drops her hips firmly, pushing your cock deep inside her and pinning you to the floor.");
                        } else {
                            if (knotted) {
                                c.write(getSelf(), "You struggle fruitlessly against the lump of "
                                                + target.nameOrPossessivePronoun() + " knot.");
                            } else {
                                c.write(getSelf(), "You attempt to get away from " + target.name()
                                                + ", but she drives her cock into you to the hilt, pinning you down.");
                            }
                        }
                    } else if (target.human()) {
                        if (c.getStance().behind(target)) {
                            c.write(getSelf(), getSelf().name()
                                            + " struggles to gain a more dominant position, but with you behind her, holding her waist firmly, there is nothing she can do.");
                        } else {
                            c.write(getSelf(), getSelf().name()
                                            + " tries to roll on top of you, but you use you superior upper body strength to maintain your position.");
                        }
                    }
                    getSelf().struggle();
                    return false;
                }
            }
        } else {
            if (getSelf().check(Attribute.Power, target.getStamina().get() / 2 - getSelf().getStamina().get() / 2
                            + target.get(Attribute.Power) - getSelf().get(Attribute.Power) - getSelf().escape(c))) {
                if (getSelf().human()) {
                    c.write(getSelf(), "You manage to scrabble out of " + target.name() + "'s grip.");
                } else if (target.human()) {
                    c.write(getSelf(), getSelf().name() + " squirms out from under you.");
                }
                c.setStance(new Neutral(getSelf(), target));
            } else {
                if (c.getStance().enumerate() == Stance.facesitting) {
                    if (getSelf().human()) {
                        c.write(getSelf(), "You try to free yourself from " + target.name()
                                        + ", but she drops her ass over your face again, forcing you to service her.");
                    } else if (target.human()) {
                        c.write(getSelf(), getSelf().name()
                                        + " struggles against you, but you drop your ass over her face again, forcing her to service you.");
                    }
                    if (target.hasPussy()) {
                        new Cunnilingus(getSelf()).resolve(c, target);
                    } else {
                        new Anilingus(getSelf()).resolve(c, target);
                    }
                    target.weaken(c, 5 + Global.random(5) + getSelf().get(Attribute.Power) / 2);
                    getSelf().struggle();
                    return false;
                } else {
                    if (getSelf().human()) {
                        c.write(getSelf(), "You try to free yourself from " + target.name()
                                        + "'s grasp, but she has you pinned too well.");
                    } else if (target.human()) {
                        c.write(getSelf(),
                                        getSelf().name() + " struggles against you, but you maintain your position.");
                    }
                    target.weaken(c, 5 + Global.random(5) + getSelf().get(Attribute.Power) / 2);
                    getSelf().struggle();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Power) >= 3;
    }

    @Override
    public Skill copy(Character user) {
        return new Struggle(user);
    }

    @Override
    public int speed() {
        return 0;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String describe(Combat c) {
        return "Attempt to escape a submissive position using Power";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
