package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.status.Shamed;
import nightgames.status.Stsflag;

public class GoblinFaceFuck extends SimpleEnemySkill {
    public GoblinFaceFuck(Character self) {
        super("Goblin Face Fuck", self);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.debuff);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && c.getStance().prone(target) 
                        && getSelf().hasDick() && c.getStance().faceAvailable(target) && !target.is(Stsflag.shamed);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        c.write(getSelf(), Global.format("{self:SUBJECT} straddles {other:name-possessive} head, giving {other:direct-object} an eyefull of her assorted genitals. "
                        + "She pulls the vibrator out of her pussy, causing a rain of love juice to splash {other:possessive} face. "
                        + "{self:SUBJECT} then wipes her leaking cock on {other:name-possessive} forehead, smearing {other:direct-object} with precum. "
                        + "{other:NAME-POSSESSIVE} face flushes with shame as the goblin marks {other:direct-object} with her fluids.", getSelf(), target));
        getSelf().body.pleasure(target, target.body.getRandom("skin"), getSelf().body.getRandomCock(), 10, c);
        target.add(c, new Shamed(target));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new GoblinFaceFuck(user);
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
