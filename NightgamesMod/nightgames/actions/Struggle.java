package nightgames.actions;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.status.Bound;
import nightgames.status.Stsflag;

public class Struggle extends Action {
    private static final long serialVersionUID = -644996487174479671L;
    public Struggle() {
        super("Struggle");
    }

    @Override
    public boolean usable(Character user) {
        return user.bound();
    }

    @Override
    public Movement execute(Character user) {
        Bound status = (Bound) user.getStatus(Stsflag.bound);
        int difficulty = 20 - user.getEscape(null, null);
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.println(user.getName() + " struggles with difficulty " + difficulty);
        }
        if (user.check(Attribute.Power, difficulty)) {
            if (user.human()) {
                if (status != null) {
                    Global.gui().message("You manage to break free from the " + status.getVariant() + ".");
                } else {
                    Global.gui().message("You manage to snap the restraints that are binding your hands.");
                }
            }
            user.free();
        } else {
            if (user.human()) {
                if (status != null) {
                    Global.gui().message("You struggle against the " + status.getVariant() + ", but can't get free.");
                } else {
                    Global.gui().message("You struggle against your restraints, but can't get free.");
                }
            }
            user.struggle();
        }
        return Movement.struggle;
    }

    @Override
    public Movement consider() {
        return Movement.struggle;
    }

}
