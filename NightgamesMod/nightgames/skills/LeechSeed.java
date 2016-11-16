package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.Seeded;
import nightgames.status.Stsflag;

public class LeechSeed extends Skill {
    String lastPart;

    public LeechSeed(Character self) {
        super("Leech Seed", self, 3);
        lastPart = "none";
        addTag(SkillTag.drain);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.positioning);
        addTag(SkillTag.debuff);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && getSelf().body.has("tentacles") && !target.is(Stsflag.seeded)
                        && !(target.is(Stsflag.pegged) && c.getStance().penetrated(c, target))
                        && target.outfit.slotOpen(ClothingSlot.bottom);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 20;
    }
    
    @Override
    public boolean resolve(Combat c, Character target) {
        if (!target.canAct() || target.roll(getSelf(), c, accuracy(c))) {
            Result results = Result.anal;
            if (!target.is(Stsflag.fucked) && target.hasPussy()) {
                results = Result.normal;
            }
            writeOutput(c, results, target);
            if (results == Result.normal) {

                target.add(c, new Seeded(target, getSelf(), "pussy"));
            } else {
                target.add(c, new Seeded(target, getSelf(), "ass"));
            }
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().get(Attribute.Bio) >= 10;
    }

    @Override
    public Skill copy(Character user) {
        return new LeechSeed(user);
    }

    public int speed() {
        return 5;
    }

    public int accuracy(Combat c) {
        return 15;
    }

    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to plant a seed in " + target.directObject() + ", but she dodges out of the way.";
        }
        String hole = "pussy";
        if (modifier == Result.anal) {
            hole = "ass";
        }
        return Global.format(
                        "You sneak one of your thinner tentacles behind {other:name-do} and prepare one of your seeds. While {other:subject} is distracted, you slip the tentacle into {other:possessive} %s and plant a hard lemon-sized orb into {other:direct-object}. {other:SUBJECT} yelps in surprise, but by then it was too late. Your seed is planted firmly inside her %s.",
                        getSelf(), target, hole, hole);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to plant a seed in %s with %s tentacle, but %s %s out of the way.",
                            getSelf().subject(), target.nameDirectObject(), getSelf().possessivePronoun(),
                            target.pronoun(), target.action("dodge"));
        }
        String hole = "pussy";
        if (modifier == Result.anal) {
            hole = "ass";
        }
        return Global.format(
                        "{self:SUBJECT} flashes a brilliant smile at {other:name-do} and beckons {other:direct-object} forward. Against {other:possessive} better judgement, {other:subject-action:move|moves} closer to {self:direct-object}, hoping for an opening to attack. "
                                        + "Suddenly, {other:pronoun-action:feel|feels} a pressure at {ohter:possessive} %s. It was a trap! {self:SUBJECT} laughs at {other:name-do} and wiggles {self:possessive} tentacle burried inside {other:direct-object}. {other:NAME-POSSESSIVE} ordeal, however, is not over. {other:PRONOUN-ACTION:feel|feels} an "
                                        + "egg shaped object pushed through {self:possessive} tentacle and deposited inside {other:possessive} %s. With a final giggle, {self:subject} retracts {self:possessive} tentacle and {other:subject-action:get|gets} to see that "
                                        + "{self:subject} planted a fist sized seed inside {other:direct-object}!",
                        getSelf(), target, hole, hole);
    }

    @Override
    public String describe(Combat c) {
        return "Plants a seed inside your opponent";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
