package nightgames.global;

import nightgames.characters.Character;
/**
 * Some static mechanic configuration. This should probably go in a json somewhere, but for now, it'll be here.
 */
public class Configuration {
    public static int getMaximumStaminaPossible(Character c) {
        return (int) (100 + c.getLevel() * (3 + c.getGrowth().stamina));
    }
    public static int getMaximumArousalPossible(Character c) {
        return (int) (100 + c.getLevel() * (4 + c.getGrowth().arousal));
    }
}
