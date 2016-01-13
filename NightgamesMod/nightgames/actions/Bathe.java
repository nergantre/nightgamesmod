package nightgames.actions;

import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.global.Global;

public class Bathe extends Action {

    /**
     * 
     */
    private static final long serialVersionUID = 4565550545479306251L;

    public Bathe() {
        super("Clean Up");
    }

    @Override
    public boolean usable(Character user) {
        return user.location().bath();
    }

    @Override
    public Movement execute(Character user) {
        if (user.human()) {
            if (user.location().name.equals("Showers")) {
                Global.gui().message("It's a bit dangerous, but a shower sounds especially inviting right now.");
            } else if (user.location().name.equals("Pool")) {
                Global.gui().message("There's a jacuzzi in the pool area and you decide to risk a quick soak.");
            }
        }
        user.state = State.shower;
        user.delay(1);
        return Movement.bathe;
    }

    @Override
    public Movement consider() {
        return Movement.bathe;
    }

}
