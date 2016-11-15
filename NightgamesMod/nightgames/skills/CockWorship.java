package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.BodyFetish;

public class CockWorship extends Skill {

    public CockWorship(Character self) {
        super("Cock Worship", self);
        addTag(SkillTag.pleasureSelf);
        addTag(SkillTag.usesMouth);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.suicidal);
        addTag(SkillTag.worship);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.crotchAvailable() && target.hasDick() && c.getStance().oral(getSelf(), target)
                        && c.getStance().front(getSelf()) && getSelf().canAct()
                        && !c.getStance().vaginallyPenetrated(c, target);
    }

    @Override
    public float priorityMod(Combat c) {
        return 0;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 0;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = 10 + Global.random(8);
        if (getSelf().has(Trait.silvertongue)) {
            m += 4;
        }
        writeOutput(c, Result.normal, target);
        BodyPart mouth = getSelf().body.getRandom("mouth");
        BodyPart cock = target.body.getRandom("cock");
        target.body.pleasure(getSelf(), mouth, cock, m, c, this);
        if (getSelf().hasDick() && (!getSelf().hasPussy() || Global.random(2) == 0)) {
            getSelf().body.pleasure(getSelf(), getSelf().body.getRandom("hands"), getSelf().body.getRandomCock(), m, c, this);
        } else if (getSelf().hasPussy()) {
            getSelf().body.pleasure(getSelf(), getSelf().body.getRandom("hands"), getSelf().body.getRandomPussy(), m,
                            c, this);
        } else {
            getSelf().body.pleasure(getSelf(), getSelf().body.getRandom("hands"), getSelf().body.getRandomHole(), m, c, this);
        }
        if (mouth.isErogenous()) {
            getSelf().body.pleasure(getSelf(), cock, mouth, m, c, this);
        }

        target.buildMojo(c, 20);
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        Optional<BodyFetish> fetish = getSelf().body.getFetish("cock");
        return fetish.isPresent() && fetish.get().magnitude >= .5;
    }

    @Override
    public int accuracy(Combat c) {
        return 150;
    }

    @Override
    public Skill copy(Character user) {
        return new CockWorship(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return Global.format(
                        "You ecstatically crawl towards {other:name-do} and reverently hold {other:possessive} {other:body-part:cock} with your hands. "
                                        + "You carefully take {other:possessive} member into your {self:body-part:mouth} and start blowing {other:direct-object} for all you are worth. "
                                        + "Minutes pass and you lose yourself in sucking {other:name-possessive} divine shaft while idly playing with yourself. Finally, {other:subject} "
                                        + "pushes your head away from {other:possessive} cock and you finally regain your senses.",
                        getSelf(), target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format(
                        "{self:subject} ecstatically crawls to {other:subject} on {self:possessive} knees and reverently cups {other:possessive} {other:body-part:cock}"
                                        + "with {self:possessive} hands. {self:PRONOUN} carefully takes {other:possessive} member into {self:possessive} {self:body-part:mouth} and starts sucking on it "
                                        + "like it was the most delicious popsicle made. Minutes pass and {self:subject} continues blowing {other:possessive} shaft while idly playing with "
                                        + "{self:reflective}. Feeling a bit too good, {other:subject-action:manage|manages} to push {self:name-do} away from {other:possessive} cock lest {self:pronoun} makes {other:direct-object} cum accidentally.",
                        getSelf(), target);
    }

    @Override
    public String describe(Combat c) {
        return "Worship your opponent's dick";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
