package nightgames.actions;

import nightgames.areas.Area;
import nightgames.characters.Attribute;
import nightgames.characters.Character;

public class Shortcut extends Move {

    /**
     * 
     */
    private static final long serialVersionUID = -1296906689064842089L;

    public Shortcut(Area area) {
        super(area);
        name = "Take shortcut to " + area.name;
    }

    @Override
    public boolean usable(Character user) {
        return user.getPure(Attribute.Cunning) >= 28 && !user.bound();
    }

}
