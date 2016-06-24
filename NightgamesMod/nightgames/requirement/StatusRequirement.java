package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.status.Stsflag;

public class StatusRequirement implements Requirement {
    private final Stsflag flag;

    public StatusRequirement(String flag) {
        this.flag = Stsflag.valueOf(flag);
    }

    public StatusRequirement(Stsflag flag) {
        this.flag = flag;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return !(c == null || flag == null) && self.getStatus(flag) != null;

    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        StatusRequirement that = (StatusRequirement) o;

        return flag == that.flag;

    }

    @Override public int hashCode() {
        return flag.hashCode();
    }
}
