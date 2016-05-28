package nightgames.start;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONObject;

import nightgames.characters.CharacterSex;
import nightgames.characters.Player;
import nightgames.characters.Trait;

class PlayerConfiguration extends CharacterConfiguration {

    private boolean allowsMoreTraits;
    private int attributePoints;
    
    private PlayerConfiguration() {}

    boolean allowsMoreTraits() {
        return allowsMoreTraits;
    }
    
    int getAttributePoints() {
        return attributePoints;
    }

    Player build(String name, Optional<CharacterSex> gender, Optional<List<Trait>> traits) {
        Player p = new Player(name, gender.orElse(CharacterSex.male));
        if (!gender.isPresent()) {
            if (gender.isPresent()) {
                super.gender = gender;
            } else {
                super.gender = Optional.of(CharacterSex.male);
            }
        }
        if (allowsMoreTraits && traits.isPresent()) {
            List<Trait> extraTraits = traits.get();
            if (super.traits.isPresent())
                super.traits.get().addAll(extraTraits);
            else
                super.traits = Optional.of(extraTraits);
        }
        processCommon(p);
        return p;
    }

    public static PlayerConfiguration parse(JSONObject obj) {
        PlayerConfiguration cfg = new PlayerConfiguration();
        cfg.parseCommon(obj);
        cfg.allowsMoreTraits = getIfExists(obj, "trait_choice", o -> (boolean) o).orElse(true);
        cfg.attributePoints = getIfExists(obj, "attribute_points", o -> ((Long) o).intValue()).orElse(11);
        return cfg;
    }

}
