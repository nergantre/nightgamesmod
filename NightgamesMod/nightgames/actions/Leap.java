package nightgames.actions;

import nightgames.areas.Area;
import nightgames.characters.Attribute;
import nightgames.characters.Character;

public class Leap extends Move {
    private static final long serialVersionUID = -6383146631175015529L;
    
    public Leap(Area destination) {
        super(destination);
        name = "Ninja Leap("+destination.name+")";
    }
    
    public boolean usable(Character user) {
        return user.getPure(Attribute.Ninjutsu)>=5;
    }

}
