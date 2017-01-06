package nightgames.start;

import static nightgames.start.ConfigurationUtils.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Growth;
import nightgames.characters.Trait;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.json.JsonUtils;

public abstract class CharacterConfiguration {

    protected Optional<String> name;
    protected Optional<CharacterSex> gender;
    protected Map<Attribute, Integer> attributes;
    protected Optional<Integer> money;
    protected Optional<Integer> level;
    protected Optional<Integer> xp;
    protected Optional<Collection<Trait>> traits;
    protected Optional<BodyConfiguration> body;
    protected Optional<Collection<String>> clothing;

    public CharacterConfiguration() {
        name = Optional.empty();
        gender = Optional.empty();
        attributes = new HashMap<>();
        money = Optional.empty();
        level = Optional.empty();
        xp = Optional.empty();
        traits = Optional.empty();
        body = Optional.empty();
        clothing = Optional.empty();
    }

    /**
     * Merges the fields of two CharacterConfigurations into the a new CharacterConfiguration.
     *
     * @param primaryConfig   The primary configuration.
     * @param secondaryConfig The secondary configuration. Field values will be overridden by values in primaryConfig. return
     */
    protected CharacterConfiguration(CharacterConfiguration primaryConfig, CharacterConfiguration secondaryConfig) {
        this();
        name = mergeOptionals(primaryConfig.name, secondaryConfig.name);
        gender = mergeOptionals(primaryConfig.gender, secondaryConfig.gender);
        attributes.putAll(secondaryConfig.attributes);
        attributes.putAll(primaryConfig.attributes);
        money = mergeOptionals(primaryConfig.money, secondaryConfig.money);
        level = mergeOptionals(primaryConfig.level, secondaryConfig.level);
        xp = mergeOptionals(primaryConfig.xp, secondaryConfig.xp);
        clothing = mergeOptionals(primaryConfig.clothing, secondaryConfig.clothing);
        traits = mergeCollections(primaryConfig.traits, secondaryConfig.traits);
        if (primaryConfig.body.isPresent()) {
            if (secondaryConfig.body.isPresent()) {
                body = Optional.of(new BodyConfiguration(primaryConfig.body.get(), secondaryConfig.body.get()));
            } else {
                body = primaryConfig.body;
            }
        } else {
            body = secondaryConfig.body;
        }
    }

    protected final void apply(Character base) {
        name.ifPresent(n -> base.setName(n));
        base.att.putAll(attributes);
        money.ifPresent(m -> base.money = m);
        level.ifPresent(l -> {
            base.level = l;
            modMeters(base, l * 2); // multiplication to compensate for missed daytime gains
        });
        xp.ifPresent(x -> base.gainXP(x));
        traits.ifPresent(t -> base.traits = new CopyOnWriteArrayList<>(t));
        if (clothing.isPresent()) {
            List<Clothing> clothes = clothing.get().stream().map(Clothing::getByID).collect(Collectors.toList());
            base.outfitPlan = new ArrayList<>(clothes);
            base.closet = new HashSet<>(clothes);
            base.change();
        }
        body.ifPresent(b -> b.apply(base.body));
        base.levelUpIfPossible();
    }

    /**
     * Parses fields common to PlayerConfiguration and NpcConfigurations.
     *
     * @param object The configuration read from the JSON config file.
     */
    protected void parseCommon(JsonObject object) {
        name = JsonUtils.getOptional(object, "name").map(JsonElement::getAsString);
        gender = JsonUtils.getOptional(object, "gender").map(JsonElement::getAsString).map(String::toLowerCase)
                        .map(CharacterSex::valueOf);
        traits = JsonUtils.getOptionalArray(object, "traits")
                        .map(array -> JsonUtils.collectionFromJson(array, Trait.class));
        body = JsonUtils.getOptionalObject(object, "body").map(BodyConfiguration::parse);
        clothing = JsonUtils.getOptionalArray(object, "clothing").map(JsonUtils::stringsFromJson);
        money = JsonUtils.getOptional(object, "money").map(JsonElement::getAsInt);
        level = JsonUtils.getOptional(object, "level").map(JsonElement::getAsInt);
        xp = JsonUtils.getOptional(object, "xp").map(JsonElement::getAsInt);
        attributes = JsonUtils.getOptionalObject(object, "attributes")
                        .map(obj -> JsonUtils.mapFromJson(obj, Attribute.class, Integer.class)).orElse(new HashMap<>());
    }

    private static void modMeters(Character character, int levels) {
        Growth growth = character.getGrowth();
        boolean hard = Global.checkFlag(Flag.hardmode);
        for (int i = 0; i < levels; i++) {
            character.getStamina().gain(growth.stamina);
            character.getArousal().gain(growth.arousal);
            character.getWillpower().gain(growth.willpower);
            if (hard) {
                character.getStamina().gain(growth.bonusStamina);
                character.getArousal().gain(growth.bonusArousal);
                character.getWillpower().gain(growth.bonusWillpower);
            }
        }
    }
}
