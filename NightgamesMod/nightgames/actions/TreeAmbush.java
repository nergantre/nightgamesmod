package nightgames.actions;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.global.Global;

public class TreeAmbush extends Action {

    private static final long serialVersionUID = -8503564080765172483L;

    public TreeAmbush() {
        super("Climb a Tree");
    }

    @Override
    public boolean usable(Character user) {
        return user.location().id() == Movement.ftcTrail
                        && (user.get(Attribute.Power) >= 20 || user.get(Attribute.Animism) >= 10)
                        && user.state != State.inTree
                        && !user.bound();
    }

    @Override
    public Movement execute(Character user) {
        if (user.human()) {
            if (user.get(Attribute.Animism) >= 10) {
                Global.gui().message(
                                "Following your instincts, you clamber up a tree" + " to await an unwitting passerby.");
            } else {
                Global.gui().message("You climb up a tree that has a branch hanging over"
                                + " the trail. It's hidden in the leaves, so you should be"
                                + " able to surprise someone passing underneath.");
            }
        }
        user.state = State.inTree;
        return Movement.ftcTreeAmbush;
    }

    @Override
    public Movement consider() {
        return Movement.ftcTreeAmbush;
    }

}
