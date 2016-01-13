package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.status.Stsflag;

public class StatusRequirement implements CustomRequirement {

    private final Stsflag flag;

    public StatusRequirement(String flag) {
        this.flag = Stsflag.valueOf(flag);
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        if (c == null || flag == null)
            return false;

        return self.getStatus(flag) != null;
    }

}
