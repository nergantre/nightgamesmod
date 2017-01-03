package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.Staleness;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.Charmed;
import nightgames.status.Status;

public class SuccubusNurse extends Skill {

    public SuccubusNurse(Character self) {
        super("Succubus Nurse", self);
        addTag(SkillTag.breastfeed);
        addTag(SkillTag.usesBreasts);
        addTag(SkillTag.perfectAccuracy);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.SuccubusWarmth);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && getSelf().body.getLargestBreasts() != BreastsPart.flat
                        && c.getStance().en == Stance.succubusembrace;
    }

    @Override
    public int getMojoCost(Combat c) {
        return getSelf().has(Trait.Pacification) ? 15 : 0;
    }

    @Override
    public String describe(Combat c) {
        return "Let your opponent drink a bit of milk";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        c.write(getSelf(), Global.format(
                        "{self:SUBJECT-ACTION:shift|shifts}, pulling {other:name-possessive} head down "
                                        + "towards one of {self:possessive} puffy nipples. %s. {self:POSSESSIVE} milk"
                                        + " slides smoothly down {other:possessive} throat, %s.",
                        getSelf(), target,
                        alreadyInfluenced(target)
                                        ? "{other:PRONOUN-ACTION:don't|doesn't} even try to"
                                                        + " resist as {self:pronoun-action:place|places} {other:possessive}"
                                                        + " mouth around it"
                                        : "{other:PRONOUN-ACTION:clamp|clamps}"
                                                        + " {other:possessive} lips shut, but it's no use as"
                                                        + " {self:subject-action:pry|pries} open {other:possessive} mouth"
                                                        + " and {self:action:insert|inserts} {self:possessive} nipple.",
                        getSelf().has(Trait.Pacification)
                                        ? "making {other:direct-object} feel strangely" + " calm and passive inside"
                                        : "feeling strangely erotic"));
        new Suckle(target).resolve(c, getSelf(), true);
        if (getSelf().has(Trait.Pacification)) {
            target.add(c, new Charmed(target, 2));
        }
        if (Global.random(100) < 5 + 2 * getSelf().get(Attribute.Fetish)) {
            target.add(c, new BodyFetish(target, getSelf(), BreastsPart.a.getType(), .25));
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new SuccubusNurse(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    private boolean alreadyInfluenced(Character target) {
        return target.status.stream()
                            .anyMatch(Status::mindgames);
    }

}
