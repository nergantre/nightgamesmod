package nightgames.start;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.CharacterSex;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.global.JSONUtils;
import nightgames.items.clothing.Clothing;

import static nightgames.start.ConfigurationUtils.mergeOptionals;

public abstract class CharacterConfiguration {

    protected Optional<String> name;
    protected Optional<CharacterSex> gender;
    protected Map<Attribute, Integer> attributes;
    protected Optional<Integer> money;
    protected Optional<Integer> level;
    protected Optional<Integer> xp;
    protected Optional<List<Trait>> traits;
    protected Optional<BodyConfiguration> body;
    protected Optional<List<String>> clothing;

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

    /** Merges the fields of two CharacterConfigurations into the a new CharacterConfiguration.
     * @param primaryConfig The primary configuration.
     * @param secondaryConfig The secondary configuration. Field values will be overridden by values in primaryConfig.
     * return
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
        traits = mergeOptionals(primaryConfig.traits, secondaryConfig.traits);
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
        name.ifPresent(n -> base.name = n);
        base.att.putAll(attributes);
        money.ifPresent(m -> base.money = m);
        level.ifPresent(l -> base.level = l);
        xp.ifPresent(x -> base.gainXP(x));
        traits.ifPresent(t -> base.traits = new CopyOnWriteArrayList<Trait>(t));
        if (clothing.isPresent()) {
            List<Clothing> clothes = clothing.get()
                            .stream()
                            .map(Clothing::getByID)
                            .collect(Collectors.toList());
            base.outfitPlan = new ArrayList<>(clothes);
            base.closet = new HashSet<>(clothes);
            base.change();
        }
        body.ifPresent(b -> b.apply(base.body));
        base.levelUpIfPossible();
    }

    public Optional<CharacterSex> getGender() {
        return this.gender;
    }

    /** Parses fields common to PlayerConfiguration and NpcConfigurations.
     * @param obj The configuration read from the JSON config file.
     */
    protected void parseCommon(JSONObject obj) {
        name = JSONUtils.getIfExists(obj, "name", Object::toString);
        gender = JSONUtils.getIfExists(obj, "gender", o -> CharacterSex.valueOf(o.toString()
                                                                       .toLowerCase()));
        traits = JSONUtils.getIfExists(obj, "traits", o -> parseTraits((JSONArray) o));
        body = JSONUtils.getIfExists(obj, "body", o -> BodyConfiguration.parse((JSONObject) o));
        clothing = JSONUtils.getIfExists(obj, "clothing", o -> parseClothing((JSONArray) o));
        if (obj.containsKey("money"))
            money = Optional.of(JSONUtils.readInteger(obj, "money"));
        if (obj.containsKey("level"))
            level = Optional.of(JSONUtils.readInteger(obj, "level"));
        if (obj.containsKey("xp"))
            xp = Optional.of(JSONUtils.readInteger(obj, "xp"));
        if (obj.containsKey("attributes")) {
            JSONObject attrs = (JSONObject) obj.get("attributes");
            for (Object a : attrs.keySet()) {
                Attribute att = Attribute.valueOf(a.toString());
                attributes.put(att, JSONUtils.readInteger(attrs, att.name()));
            }
        }

    }




    private static List<Trait> parseTraits(JSONArray arr) {
        List<Trait> traits = new ArrayList<>();
        for (Object o : arr) {
            String name = o.toString();
            traits.add(Trait.valueOf(name));
        }
        return traits;
    }

    @SuppressWarnings("unchecked")
    private static List<String> parseClothing(JSONArray arr) {
        return ((Stream<Object>) arr.stream()).map(Object::toString)
                                              .collect(Collectors.toList());
    }

}
