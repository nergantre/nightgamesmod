package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;
import nightgames.status.ArmLocked;
import nightgames.status.LegLocked;
import nightgames.status.addiction.AddictionType;

public class Invitation extends Skill {
    private static final String divineStringFemale = "Goddess's Invitation";
    private static final String divineStringMale = "God's Invitation";

    public Invitation(Character self) {
        super("Invitation", self, 6);
        addTag(SkillTag.fucking);
        addTag(SkillTag.positioning);
    }

    @Override
    public float priorityMod(Combat c) {
        return getSelf().has(Trait.submissive) ? 2 : 0;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) > 25 || user.has(Trait.submissive);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        boolean insertable = c.getStance().insert(c, getSelf(), getSelf()) != c.getStance()
                        || c.getStance().insert(c, target, getSelf()) != c.getStance();
        return insertable && getSelf().canRespond() && getSelf().crotchAvailable() && target.crotchAvailable()
                        && (getSelf().hasDick() && target.hasPussy() || getSelf().hasPussy() && target.hasDick());
    }

    @Override
    public int getMojoCost(Combat c) {
        //Free if user is Kat and player has Breeder
        if (!c.getOpponent(getSelf()).human() || !Global.getPlayer().checkAddiction(AddictionType.BREEDER, getSelf()))
            return 50;
        return 0;
    }

    @Override
    public String describe(Combat c) {
        return "Invites opponent into your embrace";
    }

    @Override
    public Skill copy(Character user) {
        return new Invitation(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            if (hasDivinity()) {
                return Global.format(
                                "You command {other:name} to embrace you. {other:SUBJECT} moves to walk towards you for a second before snapping out of it.",
                                getSelf(), target);
            }
            return Global.format("You try to hug {other:name} and pull her down, but she twists out of your grasp.\n",
                            getSelf(), target);
        } else if (!c.getStance().inserted(getSelf())) {
            if (hasDivinity()) {
                return Global.format(
                                "You command {other:name} to embrace you. {other:SUBJECT} obeys and hugs you close to {other:direct-object}. You follow up on your earlier command and tell her to fuck you, which she promptly lovingly complies.",
                                getSelf(), target);
            }
            return Global.format(
                            "You embrace {other:name} and smoothly slide her cock into your folds while she's distracted. You then pull her to the ground on top of you and softly wrap your legs around her waist",
                            getSelf(), target);
        } else {
            if (hasDivinity()) {
                return Global.format(
                                "You command {other:name} to embrace you. {other:SUBJECT} obeys and hugs you close to {other:direct-object}. You follow up on your earlier command and tell her to fuck you, which she promptly lovingly complies.",
                                getSelf(), target);
            }
            return Global.format(
                            "You embrace {other:name} and pull her on top of you. Taking advantage of her distraction, you push her on top of you while you are fucking her from beneath.",
                            getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            if (hasDivinity()) {
                return Global.format(
                                "{self:SUBJECT} commands {other:direct-object} to embrace {self:direct-object}. {other:SUBJECT} move to walk towards {self:direct-object} for a brief second before snapping out of it.",
                                getSelf(), target);
            }
            return Global.format(
                            "{self:NAME} hugs {other:name-do} softly and tries to pull {other:direct-object} into {self:direct-object}, but {other:pronoun-action:come|comes} to {other:possessive} senses in the nick of time and manage to twist out of {self:possessive} grasp, causing {self:NAME} to pout at {other:direct-object} cutely.\n",
                            getSelf(), target);
        } else if (!c.getStance().inserted(getSelf())) {
            if (hasDivinity()) {
                return Global.format(
                                "{self:SUBJECT} commands {other:name-do} to embrace {self:direct-object}. {other:SUBJECT-ACTION:obey|obeys} and {other:action:hug|hugs} {self:direct-object} close to {other:reflective}. {self:NAME} follows up on {self:possessive} earlier command and tells {other:name-do} to fuck {self:direct-object}, to which {other:pronoun} promptly, lovingly {other:action:comply|complies}.",
                                getSelf(), target);
            }
            return Global.format(
                            "{self:NAME} embraces {other:name-do} and smoothly slides {other:possessive} cock into {self:possessive} folds while {other:pronoun-action:are|is} distracted. {self:PRONOUN} then pulls {other:direct-object} to the ground on top of {self:direct-object} and softly wraps {self:possessive} legs around {other:possessive} waist preventing {other:possessive} escape.",
                            getSelf(), target);
        } else {
            if (hasDivinity()) {
                return Global.format(
                                "{self:SUBJECT} commands {other:direct-object} to embrace {self:direct-object}. {other:SUBJECT-ACTION:obey|obeys} and {other:action:hug|hugs} {self:direct-action} close to {other:reflective}. {self:NAME} follows up on {self:possessive} earlier command and tells {other:name-do} to fuck {self:direct-object}, to which {other:pronoun} promptly, lovingly {other:action:comply|complies}.",
                                getSelf(), target);
            }
            return Global.format(
                            "{self:NAME} embraces {other:name-do} and pulls {other:direct-object} on top of {self:direct-object}. Taking advantage of {other:possessive} distraction, {self:subject} pushes {other:name-do} above {self:direct-object} with {self:direct-object} fucking {other:direct-object} from underneath.",
                            getSelf(), target);
        }
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int difficulty = target.getLevel() - target.getArousal().get() * 10 / target.getArousal().max()
                        + target.get(Attribute.Seduction);
        int strength = getSelf().getLevel() + getSelf().get(Attribute.Seduction)
                        * (getSelf().has(Trait.submissive) ? 2 : 1) * (hasDivinity() ? 2 : 1);

        boolean success = Global.random(Math.min(Math.max(difficulty - strength, 1), 10)) == 0;
        Result result = Result.normal;
        if (!success) {
            result = Result.miss;
        } else if (hasDivinity()) {
            result = Result.divine;
        }

        if (success) {
            c.setStance(c.getStance().insertRandomDom(c, target), getSelf(), getSelf().canMakeOwnDecision());
        }
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, result, target));
        } else {
            c.write(getSelf(), receive(c, 0, result, target));
        }
        if (success) {
            if (c.getStance().en == Stance.missionary) {
                target.add(c, new LegLocked(target, 4 * getSelf().get(Attribute.Power)));
            } else {
                target.add(c, new ArmLocked(target, 4 * getSelf().get(Attribute.Power)));
            }
            new Thrust(target).resolve(c, getSelf());
            if (hasDivinity()) {
                getSelf().usedAttribute(Attribute.Divinity, c, .5);
            }
        }
        return success;
    }

    public boolean hasDivinity() {
        return getSelf().get(Attribute.Divinity) >= 25;
    }

    @Override
    public String getLabel(Combat c) {
        if (hasDivinity()) {
            return getSelf().hasPussy() ? divineStringFemale : divineStringMale;
        } else {
            return "Invitation";
        }
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
