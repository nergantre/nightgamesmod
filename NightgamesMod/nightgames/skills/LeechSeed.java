package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Seeded;
import nightgames.status.Stsflag;

public class LeechSeed extends Skill {
    String lastPart;

    public LeechSeed(Character self) {
        super("Leech Seed", self, 3);
        lastPart = "none";
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && getSelf().body.has("tentacles") && !target.is(Stsflag.seeded)
                        && !(target.is(Stsflag.pegged) && target.is(Stsflag.fucked));
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 0;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (!target.canAct() || target.roll(this, c, accuracy(c))) {
            Result results = Result.anal;
            if (!target.is(Stsflag.fucked) && target.hasPussy()) {
                results = Result.normal;
            }
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, results, target));
            } else if (target.human()) {
                c.write(getSelf(), receive(c, 0, results, target));
            }
            if (results == Result.normal) {

                target.add(c, new Seeded(target, getSelf(), "pussy"));
            } else {
                target.add(c, new Seeded(target, getSelf(), "ass"));
            }
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), deal(c, 0, Result.miss, target));
            } else if (target.human()) {
                c.write(getSelf(), receive(c, 0, Result.miss, target));
            }
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
        return 5;
    }

    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You try to plant a seed in " + target.directObject() + ", but she dodges out of the way";
        }
        String hole = "pussy";
        if (modifier == Result.anal) {
            hole = "ass";
        }
        return Global.format(
                        "You sneak one of your thinner tentacles behind {other:name-do} and prepare one of your seeds. While {other:subject} is distracted, you slip the tentacle into {other:possessive} %s and plant a hard lemon-sized into {other:direct-object}. {other:SUBJECT} yelps in surprise, but by then it was too late. Your seed is planted firmly inside her %s.",
                        getSelf(), target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        BodyPart selfPart = getSelf().body.getRandom("tentacles");

        if (modifier == Result.miss) {
            return getSelf().subject() + " tries to plant a seed in you with " + getSelf().possessivePronoun()
                            + " tentacle, but you dodges out of the way";
        }
        String hole = "pussy";
        if (modifier == Result.anal) {
            hole = "ass";
        }
        return Global.format(
                        "{self:SUBJECT} flashes a brilliant smile at you and beckons you forward. Against your better judgement, you move closer to {self:direct-object}, hoping for an opening to attack. "
                                        + "Suddenly, you feel a pressure at your %s. It was a trap! {self:SUBJECT} laughs at you and wiggles {self:possessive} tentacle burried inside you. Your ordeal however is not over. You feel an "
                                        + "egg shaped object pushed through {self:possessive} tentacle and deposited inside your %s. With a final giggle, {self:subject} retracts {self:possessive} tentacle and you get to see that "
                                        + "{self:pronoun} planted a fist sized seed inside you!",
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
