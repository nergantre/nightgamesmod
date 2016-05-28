package nightgames.start;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.CharacterSex;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.Body;
import nightgames.global.JSONUtils;
import nightgames.items.clothing.Clothing;

abstract class CharacterConfiguration {

    protected Optional<String> name;
    protected Optional<CharacterSex> gender;
    protected Map<Attribute, Integer> attributes;
    protected int money;
    protected int level;
    protected int xp;
    protected Optional<List<Trait>> traits;
    protected Optional<BodyConfiguration> body;
    protected Optional<List<String>> clothing;

    protected CharacterConfiguration() {
        name = Optional.empty();
        gender = Optional.empty();
        attributes = new HashMap<>();
        money = 0;
        level = 1;
        xp = 0;
        traits = Optional.empty();
        body = Optional.empty();
        clothing = Optional.empty();
    }

    protected final void processCommon(Character base) {
        if (name.isPresent())
            base.name = name.get();
        base.att = buildAttributes();
        base.money = money;
        base.level = level;
        base.xp = xp;
        if (traits.isPresent()) {
            base.traits = new CopyOnWriteArrayList<>(traits.get());
        }
        if (clothing.isPresent()) {
            base.outfitPlan = clothing.get()
                                      .stream()
                                      .map(Clothing::getByID)
                                      .collect(Collectors.toList());
        }
        Body body;
        if (this.body.isPresent()) {
            body = this.body.get()
                            .build();
        } else {
            body = new Body();
            body.finishBody(gender.orElse(CharacterSex.female));
        }
        base.body = body;
    }

    protected void parseCommon(JSONObject obj) {
        name = getIfExists(obj, "name", Object::toString);
        gender = getIfExists(obj, "gender", o -> CharacterSex.valueOf(o.toString()
                                                                       .toLowerCase()));
        traits = getIfExists(obj, "traits", o -> parseTraits((JSONArray) o));
        body = getIfExists(obj, "body", o -> BodyConfiguration.parse((JSONObject) o, gender));
        clothing = getIfExists(obj, "clothing", o -> parseClothing((JSONArray) o));
        if (obj.containsKey("money"))
            money = JSONUtils.readInteger(obj, "money");
        if (obj.containsKey("level"))
            level = JSONUtils.readInteger(obj, "level");
        if (obj.containsKey("xp"))
            xp = JSONUtils.readInteger(obj, "xp");
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

    protected static <T> Optional<T> getIfExists(JSONObject obj, String key, Function<Object, T> f) {
        if (!obj.containsKey(key))
            return Optional.empty();
        return Optional.ofNullable(f.apply(obj.get(key)));
    }

    private HashMap<Attribute, Integer> buildAttributes() {
        HashMap<Attribute, Integer> att = new HashMap<>();
        att.putAll(attributes);
        att.putIfAbsent(Attribute.Power, 5);
        att.putIfAbsent(Attribute.Seduction, 5);
        att.putIfAbsent(Attribute.Cunning, 5);
        att.putIfAbsent(Attribute.Speed, 5);
        att.putIfAbsent(Attribute.Perception, 5);
        return att;
    }
}
