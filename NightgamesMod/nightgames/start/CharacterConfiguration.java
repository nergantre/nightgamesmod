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

public abstract class CharacterConfiguration {

    protected Optional<String> name;
    protected Optional<CharacterSex> gender;
    protected Map<Attribute, Integer> attributes;
    protected OptionalInt money;
    protected OptionalInt level;
    protected OptionalInt xp;
    protected Optional<List<Trait>> traits;
    protected Optional<BodyConfiguration> body;
    protected Optional<List<String>> clothing;

    public CharacterConfiguration() {
        name = Optional.empty();
        gender = Optional.empty();
        attributes = new HashMap<>();
        money = OptionalInt.empty();
        level = OptionalInt.empty();
        xp = OptionalInt.empty();
        traits = Optional.empty();
        body = Optional.empty();
        clothing = Optional.empty();
    }

    protected final void apply(Character base) {
        name.ifPresent(n -> base.name = n);
        base.att.putAll(attributes);
        money.ifPresent(m -> base.money = m);
        level.ifPresent(l -> base.money = l);
        xp.ifPresent(x -> base.xp = x);
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
    }

    public Optional<CharacterSex> getGender() {
        return this.gender;
    }

    protected void parseCommon(JSONObject obj) {
        name = JSONUtils.getIfExists(obj, "name", Object::toString);
        gender = JSONUtils.getIfExists(obj, "gender", o -> CharacterSex.valueOf(o.toString()
                                                                       .toLowerCase()));
        traits = JSONUtils.getIfExists(obj, "traits", o -> parseTraits((JSONArray) o));
        body = JSONUtils.getIfExists(obj, "body", o -> BodyConfiguration.parse((JSONObject) o));
        clothing = JSONUtils.getIfExists(obj, "clothing", o -> parseClothing((JSONArray) o));
        if (obj.containsKey("money"))
            money = OptionalInt.of(JSONUtils.readInteger(obj, "money"));
        if (obj.containsKey("level"))
            level = OptionalInt.of(JSONUtils.readInteger(obj, "level"));
        if (obj.containsKey("xp"))
            xp = OptionalInt.of(JSONUtils.readInteger(obj, "xp"));
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
