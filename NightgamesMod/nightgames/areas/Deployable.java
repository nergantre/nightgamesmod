package nightgames.areas;

import nightgames.characters.Character;

public interface Deployable {
    public boolean resolve(Character active);

    public Character owner();
    
    default int priority() {return 5;}
}
