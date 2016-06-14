package nightgames.start;

import nightgames.characters.Player;
import nightgames.global.JSONUtils;
import org.json.simple.JSONObject;

public class PlayerConfiguration extends CharacterConfiguration {

    private boolean allowsMoreTraits;
    private int attributePoints;
    
    public PlayerConfiguration() {}

    boolean allowsMoreTraits() {
        return allowsMoreTraits;
    }
    
    int getAttributePoints() {
        return attributePoints;
    }

    public void apply(Player player) {
        super.apply(player);
    }

    public static PlayerConfiguration parse(JSONObject obj) {
        PlayerConfiguration cfg = new PlayerConfiguration();
        cfg.parseCommon(obj);
        cfg.allowsMoreTraits = JSONUtils.getIfExists(obj, "trait_choice", o -> (boolean) o).orElse(true);
        cfg.attributePoints = JSONUtils.getIfExists(obj, "attribute_points", o -> ((Long) o).intValue()).orElse(11);
        return cfg;
    }

}
