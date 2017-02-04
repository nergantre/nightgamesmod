package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.Staleness;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class Thrust extends Skill {
    public Thrust(String name, Character self) {
        // thrust skills become stale very slowly and recovers pretty fast
        this(name, self, Staleness.build().withDecay(.05).withDefault(1.0).withRecovery(.10).withFloor(.5));
    }
    public Thrust(String name, Character self, Staleness staleness) {
        super(name, self, 0 , staleness);
        addTag(SkillTag.fucking);
        addTag(SkillTag.thrusting);
        addTag(SkillTag.pleasureSelf);
    }

    public Thrust(Character self) {
        this("Thrust", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return !user.has(Trait.temptress) || user.get(Attribute.Technique) < 11;
    }

    protected boolean havingSex(Combat c, Character target) {
        return getSelfOrgan(c, target) != null && getTargetOrgan(c, target) != null && getSelf().canRespond()
                        && (c.getStance().havingSexOtherNoStrapped(c, getSelf())
                                        || c.getStance().partsForStanceOnly(c, getSelf(), target).stream().anyMatch(part -> part.isType("cock")));
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return havingSex(c, target) && c.getStance().canthrust(c, getSelf());
    }

    public BodyPart getSelfOrgan(Combat c, Character target) {
        if (c.getStance().penetratedBy(c, target, getSelf())) {
            return getSelf().body.getRandomInsertable();
        } else if (c.getStance().anallyPenetratedBy(c, getSelf(), target)) {
            return getSelf().body.getRandom("ass");
        } else if (c.getStance().vaginallyPenetratedBy(c, getSelf(), target)) {
            return getSelf().body.getRandomPussy();
        } else {
            return null;
        }
    }

    public BodyPart getTargetOrgan(Combat c, Character target) {
        if (c.getStance().penetratedBy(c, getSelf(), target)) {
            return target.body.getRandomInsertable();
        } else if (c.getStance().anallyPenetratedBy(c, target, getSelf())) {
            return target.body.getRandom("ass");
        } else if (c.getStance().vaginallyPenetratedBy(c, target, getSelf())) {
            return target.body.getRandomPussy();
        }
        return null;
    }

    public int[] getDamage(Combat c, Character target) {
        int results[] = new int[2];

        int m = 8 + Global.random(11);
        if (c.getStance().anallyPenetrated(c, target) && getSelf().has(Trait.assmaster)) {
            m *= 1.5;
        }

        float mt = Math.max(1, m / 3.f);

        if (getSelf().has(Trait.experienced)) {
            mt = Math.max(1, mt * .66f);
        }
        mt = target.modRecoilPleasure(c, mt);

        if (getSelf().checkAddiction(AddictionType.BREEDER, target)) {
            float bonus = .3f * getSelf().getAddiction(AddictionType.BREEDER).map(Addiction::getCombatSeverity)
                            .map(Enum::ordinal).orElse(0);
            mt += mt * bonus;
        }
        if (target.checkAddiction(AddictionType.BREEDER, getSelf())) {
            float bonus = .3f * target.getAddiction(AddictionType.BREEDER).map(Addiction::getCombatSeverity)
                            .map(Enum::ordinal).orElse(0);
            m += m * bonus;
        }
        results[0] = m;
        results[1] = (int) mt;

        return results;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        BodyPart selfO = getSelfOrgan(c, target);
        BodyPart targetO = getTargetOrgan(c, target);
        if (selfO == null || targetO == null) {
        	System.err.println("Something very odd happened during " + getClass().getSimpleName() + ", stance is " + c.getStance());
        	System.err.println(getSelf().save().toString());
        	System.err.println(target.save().toString());
        	c.write("Something very weird happened, please make a bug report with the logs.");
        	return false;
        }
        Result result;
        if (c.getStance().penetratedBy(c, getSelf(), c.getStance().getPartner(c, getSelf()))) {
            result = Result.reverse;
        } else if (c.getStance().en == Stance.anal) {
            result = Result.anal;
        } else {
            result = Result.normal;
        }

        writeOutput(c, result, target);

        int[] m = getDamage(c, target);
        assert m.length >= 2;

        if (m[0] != 0) {
            target.body.pleasure(getSelf(), selfO, targetO, m[0], c, this);
        }
        if (m[1] != 0) {
            getSelf().body.pleasure(target, targetO, selfO, m[1], c, this);
        }
        if (selfO.isType("ass") && Global.random(100) < 2 + getSelf().get(Attribute.Fetish)) {
            target.add(c, new BodyFetish(target, getSelf(), "ass", .25));
        }
        return true;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 0;
    }

    @Override
    public Skill copy(Character user) {
        return new Thrust(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.anal) {
            return "You thrust steadily into " + target.getName() + "'s ass, eliciting soft groans of pleasure.";
        } else if (modifier == Result.reverse) {
            return Global.format(
                            "You rock your hips against {other:direct-object}, riding {other:direct-object} smoothly. "
                                            + "Despite the slow pace, {other:subject} soon starts gasping and mewing with pleasure.",
                            getSelf(), target);
        } else {
            return "You thrust into " + target.getName()
                            + " in a slow, steady rhythm. She lets out soft breathy moans in time with your lovemaking. You can't deny you're feeling "
                            + "it too, but by controlling the pace, you can hopefully last longer than she can.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.anal) {
            String res;
            if (getSelf().has(Trait.strapped)) {
                res = String.format("%s thrusts her hips, pumping her artificial cock in and out"
                                + " of %s ass and pushing on %s %s.", getSelf().subject(),
                                target.nameOrPossessivePronoun(), target.possessiveAdjective(),
                                target.hasBalls() ? "prostate" : "innermost parts");
                
            } else {
                res = String.format("%s cock slowly pumps the inside of %s rectum.",
                                getSelf().nameOrPossessivePronoun(), target.nameOrPossessivePronoun());
            }
            if (getSelf().has(Trait.assmaster)) {
                res += String.format(" %s penchant for fucking people in the ass makes "
                                + "%s thrusting that much more powerful, and that much more "
                                + "intense for the both of %s.", getSelf().nameOrPossessivePronoun(),
                                getSelf().possessiveAdjective(),
                                c.bothDirectObject(target));
            }
            return res;
        } else if (modifier == Result.reverse) {
            return String.format("%s rocks %s hips against %s, riding %s smoothly and deliberately. "
                            + "Despite the slow pace, the sensation of %s hot %s surrounding "
                            + "%s dick is gradually driving %s to %s limit.", getSelf().subject(),
                            getSelf().possessiveAdjective(), target.nameDirectObject(),
                            target.directObject(), getSelf().nameOrPossessivePronoun(),
                            getSelfOrgan(c, target).describe(getSelf()),
                            target.nameOrPossessivePronoun(), target.directObject(),
                            target.possessiveAdjective());
        } else {
            return Global.format(
                            "{self:subject} thrusts into {other:name-possessive} {other:body-part:pussy} in a slow steady rhythm, leaving {other:direct-object} gasping.",
                            getSelf(), target);
        }
    }

    @Override
    public String describe(Combat c) {
        return "Slow fuck, minimizes own pleasure";
    }

    @Override
    public String getName(Combat c) {
        if (c.getStance().penetratedBy(c, c.getStance().getPartner(c, getSelf()), getSelf())) {
            return "Thrust";
        } else {
            return "Ride";
        }
    }

    @Override
    public Character getDefaultTarget(Combat c) {
        return c.getStance().getPartner(c, getSelf());
    }

    @Override
    public boolean makesContact() {
        return true;
    }

    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
