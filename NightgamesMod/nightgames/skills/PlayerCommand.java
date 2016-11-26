package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.status.Enthralled;
import nightgames.status.Stsflag;

public abstract class PlayerCommand extends Skill {

    public PlayerCommand(String name, Character self) {
        super(name, self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.human();
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().human() && target.is(Stsflag.enthralled)
                        && ((Enthralled) target.getStatus(Stsflag.enthralled)).master.equals(getSelf())
                        && !c.getStance().havingSex(c) && getSelf().canRespond();
    }
}
