package nightgames.items;

import nightgames.characters.Character;

public interface Loot {
    String getName();

    String pre();

    int getPrice();

    void pickup(Character owner);

    String getID();
}
