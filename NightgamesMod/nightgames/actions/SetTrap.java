package nightgames.actions;

import nightgames.characters.Character;
import nightgames.global.Global;
import nightgames.trap.Trap;

public class SetTrap extends Action {
    /**
     * 
     */
    private static final long serialVersionUID = 9194305067966782124L;
    private Trap trap;

    public SetTrap(Trap trap) {
        super("Set(" + trap.toString() + ")");
        this.trap = trap;
    }

    @Override
    public boolean usable(Character user) {
        return trap.recipe(user) && !user.location().open() && trap.requirements(user)
                        && user.location().env.size() < 5 && !user.bound();
    }

    @Override
    public Movement execute(Character user) {
        try {
            Trap newTrap = trap.getClass().newInstance();
            newTrap.setStrength(user);
            user.location().place(newTrap);
            String message = newTrap.setup(user);
            if (user.human()) {
                Global.gui().message(message);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Movement.trap;
    }

    @Override
    public Movement consider() {
        return Movement.trap;
    }

}
