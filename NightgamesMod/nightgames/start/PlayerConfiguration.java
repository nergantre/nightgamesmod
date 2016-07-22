package nightgames.start;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nightgames.characters.Player;
import nightgames.json.JsonUtils;

public class PlayerConfiguration extends CharacterConfiguration {

    private boolean allowsMoreTraits;
    private int attributePoints;

    boolean allowsMoreTraits() {
        return allowsMoreTraits;
    }

    int getAttributePoints() {
        return attributePoints;
    }

    public void apply(Player player) {
        super.apply(player);
    }

    public static PlayerConfiguration parse(JsonObject obj) {
        PlayerConfiguration cfg = new PlayerConfiguration();
        cfg.parseCommon(obj);
        cfg.allowsMoreTraits = JsonUtils.getOptional(obj, "trait_choice").map(JsonElement::getAsBoolean).orElse(true);
        cfg.attributePoints = JsonUtils.getOptional(obj, "attribute_points").map(JsonElement::getAsInt).orElse(11);
        return cfg;
    }

}
