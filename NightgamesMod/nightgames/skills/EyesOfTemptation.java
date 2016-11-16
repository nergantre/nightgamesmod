package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Enthralled;
import nightgames.status.Stsflag;

public class EyesOfTemptation extends Skill {

    public EyesOfTemptation(Character self) {
        super("Eyes of Temptation", self, 5);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 45 || user.get(Attribute.Dark) >= 20
                        || user.get(Attribute.Arcane) >= 15;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && c.getStance()
                                          .facing(getSelf(), target)
                        && !getSelf().is(Stsflag.blinded) && !target.wary();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 40;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        Result result = target.is(Stsflag.blinded) ? Result.special
                        : target.roll(getSelf(), c, accuracy(c)) ? Result.normal : Result.miss;
        writeOutput(c, result, target);
        if (result == Result.normal) {
            target.add(c, new Enthralled(target, getSelf(), 5));
            getSelf().emote(Emotion.dominant, 50);
        }
        return result != Result.miss;
    }

    @Override
    public Skill copy(Character user) {
        return new EyesOfTemptation(user);
    }

    @Override
    public int speed() {
        return 9;
    }

    @Override
    public int accuracy(Combat c) {
        return 100;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.normal) {
            return Global.format(
                            "As {other:subject-action:gaze|gazes} into {self:name-possessive} eyes, {other:subject-action:feel|feels} {other:possessive} will slipping into the abyss.",
                            getSelf(), target);
        } else if (modifier == Result.special) {
            if (getSelf().human()) {
                return Global.format(
                                "You focus your eyes on {other:name}, but with {other:possessive} eyesight blocked the power just seeps away uselessly.",
                                getSelf(), target);
            } else {
                return Global.format(
                                "There seems to be a bit of a lull in the fight. {self:SUBJECT-ACTION:are|is} not sure what {other:name} is doing, but it isn't having any effect on {self:direct-object}.",
                                getSelf(), target);
            }
        } else {
            return Global.format(
                            "{other:SUBJECT-ACTION:look|looks} away as soon as {self:subject-action:focus|focuses} {self:possessive} eyes on {other:direct-object}.",
                            getSelf(), target);
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return deal(c, damage, modifier, target);
    }

    @Override
    public String describe(Combat c) {
        return "Enthralls your opponent with a single gaze.";
    }
}
