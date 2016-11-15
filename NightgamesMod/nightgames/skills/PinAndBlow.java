package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.HeldOral;
import nightgames.stance.Stance;

public class PinAndBlow extends Skill {
    public PinAndBlow(Character self) {
        super("Oral Pin", self);
        addTag(SkillTag.positioning);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.oral);
        addTag(SkillTag.foreplay);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 22 && user.get(Attribute.Power) >= 15;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance()
                .mobile(getSelf())
                        && c.getStance()
                            .prone(target)
                        && target.crotchAvailable() && getSelf().canAct() && !c.getStance()
                                                                               .connected(c)
                        && c.getStance().en != Stance.oralpin;
    }

    @Override
    public float priorityMod(Combat c) {
        return 0;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        c.setStance(new HeldOral(getSelf(), target));
        if (target.hasDick()) {
            new Blowjob(getSelf()).resolve(c, target);
        } else if (target.hasPussy()) {
            new Cunnilingus(getSelf()).resolve(c, target);
        } else if (target.body.has("ass")) {
            new Anilingus(getSelf()).resolve(c, target);
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new PinAndBlow(user);
    }

    @Override
    public int speed() {
        return 5;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return receive(c, damage, modifier, target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format(
                        "{self:SUBJECT-ACTION:bow|bows} {other:name-do} over, and {self:action:settle|settles} {self:possessive} head between {other:possessive} legs.",
                        getSelf(), target);
    }

    @Override
    public String describe(Combat c) {
        return "Holds your opponent down and use your mouth";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
