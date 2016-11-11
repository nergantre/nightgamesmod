package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.status.BD;
import nightgames.status.Stsflag;

public class GoblinBondage extends SimpleEnemySkill {
    public GoblinBondage(Character self) {
        super("Goblin Bondage", self);
        addTag(SkillTag.debuff);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && !target.is(Stsflag.masochism);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        c.write(getSelf(), Global.format("{self:SUBJECT} pulls the bondage straps tighter around herself. You can see the leather and latex digging into her skin as "
                        + "her bondage fascinatation begins to affect both of you.", getSelf(), target));
        getSelf().pain(c, getSelf(), 10);
        c.p1.add(c, new BD(c.p1));
        c.p2.add(c, new BD(c.p2));
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new GoblinBondage(user);
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    @Override
    public boolean makesContact() {
        return false;
    }
}
