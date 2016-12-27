package nightgames.actions;

import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.global.Global;

public class PassAmbush extends Action {

    private static final long serialVersionUID = -1745311550506911281L;

    public PassAmbush() {
        super("Try Ambush");
    }

    @Override
    public boolean usable(Character user) {
        return user.location().id() == Movement.ftcPass && user.state != State.inPass && !user.bound();
    }

    @Override
    public Movement execute(Character user) {
        if (user.human()) {
            Global.gui().message(
                            "You try to find a decent hiding place in the irregular" + " rock faces lining the pass.");
        }
        user.state = State.inPass;
        return Movement.ftcPassAmbush;
    }

    @Override
    public Movement consider() {
        return Movement.ftcPassAmbush;
    }

}
