package nightgames.actions;

import nightgames.characters.Character;

public class Wait extends Action {

    /**
     * 
     */
    private static final long serialVersionUID = -644996487174479671L;

    public Wait() {
        super("Wait");
    }

    @Override
    public boolean usable(Character user) {
        return true;
    }

    @Override
    public Movement execute(Character user) {
        return Movement.wait;
    }

    @Override
    public Movement consider() {
        return Movement.wait;
    }

}
