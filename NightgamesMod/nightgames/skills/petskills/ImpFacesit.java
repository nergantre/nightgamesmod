package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.status.Horny;

public class ImpFacesit extends SimpleEnemySkill {
    public ImpFacesit(Character self) {
        super("Imp Facesit", self);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.debuff);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && c.getStance().prone(target) 
                        && getSelf().hasPussy() && c.getStance().faceAvailable(target) 
                        && gendersMatch(target);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = Global.random(3,6) + getSelf().getLevel() / 5;
        c.write(getSelf(), Global.format("{self:SUBJECT} straddles {other:name-possessive} face, forcing {self:possessive} wet pussy onto {other:possessive} nose and mouth. "
                        + "{self:POSSESSIVE} scent is unnaturally intoxicating and fires up {other:possessive} libido.", getSelf(), target));
        getSelf().body.pleasure(target, target.body.getRandom("mouth"), getSelf().body.getRandomPussy(), 10, c);
        target.add(c, new Horny(target, m, 5, "imp juices"));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ImpFacesit(user);
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
