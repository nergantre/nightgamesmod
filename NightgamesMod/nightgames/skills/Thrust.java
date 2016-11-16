package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class Thrust extends Skill {
    public Thrust(String name, Character self) {
        super(name, self);
    }

    public Thrust(Character self) {
        super("Thrust", self);
        addTag(SkillTag.pleasureSelf);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return !user.has(Trait.temptress) || user.get(Attribute.Technique) < 11;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelfOrgan(c) != null && getTargetOrgan(c, target) != null && getSelf().canAct()
                        && c.getStance().canthrust(c, getSelf()) && c.getStance().havingSexOtherNoStrapped(c, getSelf());
    }

    public BodyPart getSelfOrgan(Combat c) {
        if (c.getStance().inserted(getSelf())) {
            return getSelf().body.getRandomInsertable();
        } else if (c.getStance().anallyPenetratedBy(c, getSelf(), c.getOpponent(getSelf()))) {
            return getSelf().body.getRandom("ass");
        } else {
            return getSelf().body.getRandomPussy();
        }
    }

    public BodyPart getTargetOrgan(Combat c, Character target) {
        if (c.getStance().inserted(target)) {
            return target.body.getRandomInsertable();
        } else if (c.getStance().anallyPenetratedBy(c, c.getOpponent(getSelf()), getSelf())) {
            return target.body.getRandom("ass");
        } else {
            return target.body.getRandomPussy();
        }
    }

    public int[] getDamage(Combat c, Character target) {
        int results[] = new int[2];

        int m = 5 + Global.random(14);
        if (c.getStance().anallyPenetrated(c, target) && getSelf().has(Trait.assmaster)) {
            m *= 1.5;
        }
        
        float mt = Math.max(1, m / 3.f);

        if (getSelf().has(Trait.experienced)) {
            mt = Math.max(1, mt * .66f);
        }
        mt = target.modRecoilPleasure(c, mt);

        if (getSelf().human() || target.human()) {
            Player p = Global.getPlayer();
            Character npc = c.getOpponent(p);
            if (p.checkAddiction(AddictionType.BREEDER, npc)) {
                float bonus = .3f * p.getAddiction(AddictionType.BREEDER).map(Addiction::getCombatSeverity)
                                .map(Enum::ordinal).orElse(0);
                if (p == getSelf()) {
                    mt += mt * bonus;
                } else {
                    m += m * bonus;                    
                }
            }
        }
        
        results[0] = m;
        results[1] = (int) mt;

        return results;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        BodyPart selfO = getSelfOrgan(c);
        BodyPart targetO = getTargetOrgan(c, target);
        Result result;
        if (c.getStance().inserted(target)) {
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
            return "You thrust steadily into " + target.name() + "'s ass, eliciting soft groans of pleasure.";
        } else if (modifier == Result.reverse) {
            return Global.format(
                            "You rock your hips against {other:direct-object}, riding her smoothly. "
                                            + "Despite the slow pace, {other:subject} soon starts gasping and mewing with pleasure.",
                            getSelf(), target);
        } else {
            return "You thrust into " + target.name()
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
                                target.nameOrPossessivePronoun(), target.possessivePronoun(),
                                target.hasBalls() ? "prostate" : "innermost parts");
                
            } else {
                res = String.format("%s cock slowly pumps the inside of %s rectum.",
                                getSelf().nameOrPossessivePronoun(), target.nameOrPossessivePronoun());
            }
            if (getSelf().has(Trait.assmaster)) {
                res += String.format(" %s penchant for fucking people in the ass makes "
                                + "%s thrusting that much more powerful, and that much more "
                                + "intense for the both of %s.", getSelf().nameOrPossessivePronoun(),
                                getSelf().possessivePronoun(),
                                c.bothDirectObject(target));
            }
            return res;
        } else if (modifier == Result.reverse) {
            return String.format("%s rocks %s hips against %s, riding %s smoothly and deliberately. "
                            + "Despite the slow pace, the sensation of %s hot %s surrounding "
                            + "%s dick is gradually driving %s to %s limit.", getSelf().subject(),
                            getSelf().possessivePronoun(), target.nameDirectObject(),
                            target.directObject(), getSelf().nameOrPossessivePronoun(),
                            getSelfOrgan(c).fullDescribe(getSelf()),
                            target.nameOrPossessivePronoun(), target.directObject(),
                            target.possessivePronoun());
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
        if (c.getStance().inserted(getSelf())) {
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
