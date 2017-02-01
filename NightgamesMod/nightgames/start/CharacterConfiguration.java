package nightgames.start;

import static nightgames.start.ConfigurationUtils.mergeCollections;
import static nightgames.start.ConfigurationUtils.mergeOptionals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
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

    private Map<Integer, Map<Attribute, Integer>> calculateAttributeLevelPlan(Character base, int desiredLevel, Map<Attribute, Integer> desiredFinalAttributes) {
        Map<Attribute, Integer> deltaAtts = desiredFinalAttributes.keySet()
                        .stream()
                        .collect(Collectors.toMap(Function.identity(), key -> desiredFinalAttributes.get(key) - base.att.getOrDefault(key, 0)));
        Map<Integer, Map<Attribute, Integer>> attributeLevelPlan = new HashMap<>();
        // k this is some terrible code but what it's doing is trying to simulate level ups for a character based on the number of levels
        // it gets and what final attributes it has
        for (int i = base.level + 1; i <= desiredLevel; i++) {
            // calculates how many more attributes it needs to add
            int attsLeftToAdd = deltaAtts.values().stream().mapToInt(Integer::intValue).sum();
            // calculates how many more levels left to distribute points (counting the current level)
            int levelsLeft = desiredLevel - i + 1;
            // calculates how many points to add for this particular level
            int attsToAdd = attsLeftToAdd / levelsLeft;
            Map<Attribute, Integer> attsForLevel = new HashMap<>();
            attributeLevelPlan.put(i, attsForLevel);
            for (int j = 0; j < attsToAdd; j++) {
                // randomly pick an attribute to train out of the ones that needs to be trained.
                List<Attribute> attsToTrain = deltaAtts.entrySet().stream()
                                .filter(entry -> entry.getValue() > 0)
                                .map(Entry::getKey)
                                .collect(Collectors.toList());
                Optional<Attribute> attToTrain = Global.pickRandom(attsToTrain);
                // put it into the level plan.
                attToTrain.ifPresent(att -> {
                    attsForLevel.compute(att, (key, old) -> old == null? 1 : old + 1);
                    deltaAtts.compute(att, (key, old) -> old - 1);
                });
            }
        }
        return attributeLevelPlan;
    }

    protected final void apply(Character base) {
        name.ifPresent(n -> base.setName(n));
        money.ifPresent(m -> base.money = m);
        traits.ifPresent(t -> {
            base.clearTraits();
            t.forEach(base::addTraitDontSaveData);
            t.forEach(trait -> base.getGrowth().addTrait(0, trait));
        });
        level.ifPresent(desiredLevel -> {
            Map<Integer, Map<Attribute, Integer>> attributeLevelPlan = calculateAttributeLevelPlan(base, desiredLevel, attributes);
            System.out.println(attributeLevelPlan);
            while (base.level < desiredLevel) {
                base.level += 1;
                modMeters(base, 1); // multiplication to compensate for missed daytime gains
                attributeLevelPlan.get(base.level).forEach((a, val) -> {
                    if (val > 0) {
                        base.mod(a, val, true);
                    }
                });
                base.getGrowth().addOrRemoveTraits(base);
            }
        });
        base.att.putAll(attributes);
        xp.ifPresent(x -> base.gainXPPure(x));
        if (clothing.isPresent()) {
            List<Clothing> clothes = clothing.get().stream().map(Clothing::getByID).collect(Collectors.toList());
            base.outfitPlan = new ArrayList<>(clothes);
            base.closet = new HashSet<>(clothes);
            base.change();
        }
        body.ifPresent(b -> b.apply(base.body));
        base.levelUpIfPossible(null);
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
