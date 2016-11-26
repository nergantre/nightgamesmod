package nightgames.skills.petskills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.skills.Skill;

public abstract class SimpleEnemySkill extends Skill {
    private int levelReq;
    public SimpleEnemySkill (String name, Character self) {
        this(name, self, 0);
    }
    public SimpleEnemySkill (String name, Character self, int levelReq) {
        super(name, self);
        this.levelReq = levelReq;
    }

    protected boolean gendersMatch(Character other) {
        if (other.useFemalePronouns() && getSelf().useFemalePronouns() && Global.checkFlag(Flag.skipFF)) {
            return false;
        }
        if (!other.useFemalePronouns() && !getSelf().useFemalePronouns() && Global.checkFlag(Flag.skipMM)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !getSelf().isPetOf(target);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getLevel() >= levelReq;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "<ERROR>";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return "<ERROR>";
    }

    @Override
    public String describe(Combat c) {
        return "";
    }
}
