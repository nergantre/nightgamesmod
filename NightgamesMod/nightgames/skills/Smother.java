package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.DamageType;
import nightgames.stance.Smothering;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.Shamed;

public class Smother extends Skill {

    public Smother(Character self) {
        super("Smother", self);
        addTag(SkillTag.pleasureSelf);
        addTag(SkillTag.dominant);
        addTag(SkillTag.facesit);
        addTag(SkillTag.weaken);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Fetish) >= 5;
    }

    @Override
    public float priorityMod(Combat c) {
        return 6;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().crotchAvailable() && getSelf().canAct() && c.getStance().dom(getSelf())
                        && (c.getStance().isBeingFaceSatBy(c, target, getSelf()))
                        && !getSelf().has(Trait.shy);
    }

    @Override
    public String describe(Combat c) {
        return "Shove your ass into your opponent's face to demonstrate your superiority";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);

        int m = 10;
        if (target.has(Trait.silvertongue)) {
            m = m * 3 / 2;
        }
        getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandom("ass"), m, c, this);
        double n = 14 + Global.random(4);
        if (c.getStance().front(getSelf())) {
            // opponent can see self
            n += 3 * getSelf().body.getHotness(target);
        }
        if (target.has(Trait.imagination)) {
            n *= 1.5;
        }

        target.tempt(c, getSelf(), getSelf().body.getRandom("ass"), (int) Math.round(n / 2));
        target.weaken(c, (int) getSelf().modifyDamage(DamageType.physical, target, Global.random(10, 25)));

        target.loseWillpower(c, Math.max(10, target.getWillpower().max() * 10 / 100 ));
        target.add(c, new Shamed(target));
        if (c.getStance().enumerate() != Stance.smothering) {
            c.setStance(new Smothering(getSelf(), target));
        }
        if (Global.random(100) < 25 + 2 * getSelf().get(Attribute.Fetish)) {
            target.add(c, new BodyFetish(target, getSelf(), "ass", .35));
        }
        return true;
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 25;
    }

    @Override
    public Skill copy(Character user) {
        return new Smother(user);
    }

    @Override
    public Tactics type(Combat c) {
        if (c.getStance().enumerate() != Stance.smothering) {
            return Tactics.positioning;
        } else {
            return Tactics.pleasure;
        }
    }

    @Override
    public String getLabel(Combat c) {
        return "Smother";
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return Global.format("Enjoying your dominance over {other:name-do}, you experimentally scoot your legs forward so that your ass completely eclipses {other:possessive} face. {other:SUBJECT-ACTION:panic|panicks} as {other:pronoun} {other:action:realize|realizes} that {other:pronoun} cannot breathe!", getSelf(), target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format("Enjoying {self:possessive} dominance over {other:name-do}, {self:subject} experimentally scoots {self:possessive} legs forward so that {self:possessive} ass completely eclipses {other:possessive} face. {other:SUBJECT-ACTION:panic|panicks} as {other:pronoun} {other:action:realize|realizes} that {other:pronoun} cannot breathe!", getSelf(), target);
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
