package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;
import nightgames.status.Bound;
import nightgames.status.Falling;
import nightgames.status.Flatfooted;
import nightgames.status.Frenzied;
import nightgames.status.Parasited;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.status.Trance;
import nightgames.status.Wary;

public class ThrowSlime extends Skill {

    public ThrowSlime(Character self) {
        super("Throw Slime", self, 4);
        addTag(SkillTag.knockdown);
        if (self.get(Attribute.Slime) >= 12) {
            addTag(SkillTag.mental);
        }
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Slime) > 0;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && (c.getStance().en == Stance.neutral 
                        || (c.getStance().en == Stance.standingover && c.getStance().dom(getSelf())))
                        && !getSelf().is(Stsflag.charmed);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 9 + getSelf().get(Attribute.Slime);
    }

    @Override
    public String describe(Combat c) {
        return "Throw some globs of slime at your opponent. Unlocks more effects with higher Slime attribute.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.has(Trait.slime)) {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:throw|throws} a glob of slime at"
                            + " {other:name-do}, but it is simply absorbed into {other:possessive}"
                            + " equally slimy body. That was rather underwhelming.", getSelf(), target));
        } else {
            HitType type = decideEffect(c, target);
            type.message(c, getSelf(), target);
            if (type != HitType.NONE) {
                target.add(type.build(getSelf(), target));
            }
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ThrowSlime(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    /*
     * Slime level:     Possible effects (cumulative): 
     * 1                Flatfooted (1 turn) 
     * 4                Weak Bound 
     * 8                Falling (if in neutral stance) OR 3-turn Flatfooted (if already standing over) 
     * 12               Low chance of Trance 
     * 16               Strong Bound 
     * 20               Somewhat higher chance of Trance OR Frenzied (50/50) 
     * 24               VERY low chance of Parasited (critical hit-style)
     * 
     * Accuracy increases with attribute level
     */

    private enum HitType {
        FLAT_1,
        BOUND_W,
        FALL,
        FLAT_3,
        TRANCE,
        BOUND_S,
        FRENZIED,
        PARASITED,
        NONE;

        Status build(Character user, Character target) {
            switch (this) {
                case BOUND_S:
                    return new Bound(target, 70, "slime");
                case BOUND_W:
                    return new Bound(target, 35, "slime");
                case FALL:
                    return new Falling(target);
                case FLAT_1:
                    return new Flatfooted(target, 1);
                case FLAT_3:
                    return new Flatfooted(target, 3);
                case FRENZIED:
                    return new Frenzied(target, 3);
                case PARASITED:
                    return new Parasited(target, user);
                case TRANCE:
                    return new Trance(target, 3);
                default: // NONE or a stupid mistake
                    Global.gui()
                          .message("ERROR: Half-implemented HitType for ThrowSlime; "
                                          + "applying 1-turn Wary instead. Please report this."
                                          + " And be sure to laugh at my stupidity. (DNDW)");
                    return new Wary(target, 1);
            }
        }

        void message(Combat c, Character self, Character target) {
            String msg = Global.format("With a large movement of {self:possessive} arms, {self:subject-action:throw|throws}"
                            + " a big glob of viscous slime at {other:name-do}. ", self, target);
            switch (this) {
                case BOUND_S:
                    msg += Global.format("While in the air, the mass of slime splits in two, and the remaining projectiles"
                                    + " impact both of {other:possessive} hands, binding them solidly to "
                                    + (c.getStance().en == Stance.neutral ? "one another." : "the ground.")
                                    , self, target);
                    break;
                case BOUND_W:
                    msg += Global.format("The slime impacts one of {other:possessive} hands, encasing it in a slimy"
                                    + " mitten. {other:PRONOUN-ACTION:are|is} going to have to get that off before"
                                    + " continuing.", self, target);
                    break;
                case FALL:
                    msg += Global.format("The glob impacts with a powerful <i>thud</i>, and it knocks"
                                    + " {other:subject} off {other:possessive} feet.", self, target);
                    break;
                case FLAT_1:
                    msg += Global.format("The slimy ball connects soundly with {other:possessive} head,"
                                    + " dazing {other:direct-object}.", self, target);
                    break;
                case FLAT_3:
                    msg += Global.format("The slime hits {other:possessive} already prone body with"
                                    + " substantial force, knocking the wind solidly out of {other:direct-object}."
                                    , self, target);
                    break;
                case TRANCE:
                    msg += Global.format("The glob catches on {other:possessive} arm, seemingly harmless. Then,"
                                    + " however, a flush spreads across {other:possessive} skin, radiating outward"
                                    + " from the slime. When the flush reaches {other:name-possessive} head,"
                                    + " {other:pronoun-action:fall|falls} straight into a deep trance."
                                    , self, target);
                    break;
                case NONE:
                    msg += Global.format("{other:PRONOUN}, however, "
                                    + "{other:action:manage|manages} to evade the onrushing slime.", self, target);
                    break;
                case PARASITED:
                    if (target.human()) {
                        msg += "You panic as the slime wraps itself around your head, completely engulfing it."
                                + " Your mood does not improve when you feel it seep into your ears, and"
                                + " beyond. It doesn't hurt, but the shock and the general weirdness of the"
                                + " situation get to you. You try to shake the slime off, but it just worms around"
                                + " inside of you while " + self.name() + " observes, giggling."
                                + " When it's job - whatever it is - is finished, the slime dislodges from"
                                + " your head and falls to the ground in a harmless puddle. Not nearly as"
                                + " harmless, however, is the sensation of some left-over slime still "
                                + " working its magic inside your skull...";
                    } else {
                        msg += Global.format("You can't help but feel a little giddy as your risky move succeeds,"
                                        + " and the slime wraps around {other:name-possessive} head."
                                        + " You know it won't be long now. {other:PRONOUN} tries to throw and"
                                        + " claw the slime off, but you can already feel the connection forming."
                                        + " After only a few seconds, the slime falls away. Most of it, anyway."
                                        + " Time for a test run...", self, target);
                    }
                    break;
                case FRENZIED:
                    msg += Global.format("The glob catches on {other:possessive} arm, seemingly harmless. Then,"
                                    + " however, a flush spreads across {other:possessive} skin, radiating outward"
                                    + " from the slime. "
                                    + (target.human() ? "It feels warm, and when it reaches your head it fills your"
                                                    + " mind with an unquenchable thirst for sex. And you"
                                                    + " know just where to get some..."
                                                    : "When the flush reaches {other:name-possessive} head, {other:pronoun}"
                                                    + " suddenly stares straight at you, focussed and intense"
                                                    + " with a not-so-subtle hint of sheer insanity.")
                                    , self, target);
                    break;
                default:
                    msg += "ERROR: Half-implemented HitType for ThrowSlime; "
                                    + "applying 1-turn Wary instead. Please report this."
                                    + " And be sure to laugh at my stupidity. (DNDW)";

            }
            c.write(self, msg);
        }
    }

    private int random(Combat c, Character target, int skill, int diff) {
        int r = Global.random(150);
        if (!c.getStance()
              .mobile(target) || !target.canRespond()) {
            r -= 50;
        } else {
            r += target.evasionBonus();
        }
       
        r -= Math.min(skill - diff, 20);

        return r;
    }

    private HitType decideEffect(Combat c, Character target) {
        int slime = getSelf().get(Attribute.Slime);
        int bonus = Math.min(slime, 40) - 20;

        if (!c.getStance()
              .mobile(target) || !target.canRespond()) {
            bonus *= 2;
        }

        if (slime >= 24 && random(c, target, slime, 24) <= 2) {
            return HitType.PARASITED;
        }
        if (slime >= 20 && random(c, target, slime, 20) <= 15 + bonus) {
            return Global.random(2) == 0 ? HitType.TRANCE : HitType.FRENZIED;
        }
        if (slime >= 16 && random(c, target, slime, 16) <= 20 + bonus) {
            return HitType.BOUND_S;
        }
        if (slime >= 12 && random(c, target, slime, 12) <= 10 + bonus) {
            return HitType.TRANCE;
        }
        if (slime >= 8 && random(c, target, slime, 8) <= 25 + bonus) {
            return c.getStance().en == Stance.neutral ? HitType.FALL : HitType.FLAT_3;
        }
        if (slime >= 4 && random(c, target, slime, 4) <= 30 + bonus) {
            return HitType.BOUND_W;
        }
        if (random(c, target, slime, 1) <= 50 + bonus) {
            return HitType.FLAT_1;
        }
        return HitType.NONE;
    }
}
