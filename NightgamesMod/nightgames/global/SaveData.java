package nightgames.global;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nightgames.characters.Character;
import nightgames.json.JsonUtils;

import java.util.*;

/**
 * SaveData specifies a schema for data that will be saved and loaded.
 */
public class SaveData {
    public final Set<Character> players;
    public final Set<String> flags;
    public final Map<String, Float> counters;
    public Time time;
    public int date;


    private enum JSONKey {
        PLAYERS("characters"),
        FLAGS("flags"),
        COUNTERS("counters"),
        TIME("time"),
        DATE("date");

        final String key;

        JSONKey(String key) {
            this.key = key;
        }
    }


    public SaveData() {
        players = new HashSet<>();
        flags = new HashSet<>();
        counters = new HashMap<>();
    }

    public SaveData(JsonObject rootJSON) {
        this();

        JsonArray charactersJSON = rootJSON.getAsJsonArray(JSONKey.PLAYERS.key);
        for (JsonElement element : charactersJSON) {
            JsonObject characterJSON = element.getAsJsonObject();
            String type = characterJSON.get("type").getAsString();
            Character character = Global.getCharacterByType(type);
            character.load(characterJSON);

            players.add(character);
        }

        JsonArray flagsJSON = rootJSON.getAsJsonArray(JSONKey.FLAGS.key);
        for (JsonElement element : flagsJSON) {
            flags.add(element.getAsString());
        }

        JsonObject countersJSON = rootJSON.getAsJsonObject(JSONKey.COUNTERS.key);
        counters.putAll(JsonUtils.mapFromJson(countersJSON, String.class, Float.class));

        date = rootJSON.get(JSONKey.DATE.key).getAsInt();

        time = Time.fromDesc(rootJSON.get(JSONKey.TIME.key).getAsString());
    }

    public JsonObject toJson() {
        JsonObject rootJSON = new JsonObject();

        JsonArray characterJSON = new JsonArray();
        players.stream().map(Character::save).forEach(characterJSON::add);
        rootJSON.add(JSONKey.PLAYERS.key, characterJSON);

        JsonArray flagJSON = new JsonArray();
        flags.forEach(flagJSON::add);
        rootJSON.add(JSONKey.FLAGS.key, flagJSON);

        JsonObject counterJSON = new JsonObject();
        counters.forEach(counterJSON::addProperty);
        rootJSON.add(JSONKey.COUNTERS.key, counterJSON);

        rootJSON.addProperty(JSONKey.TIME.key, time.desc);

        rootJSON.addProperty(JSONKey.DATE.key, date);

        return rootJSON;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SaveData saveData = (SaveData) o;

        if (date != saveData.date)
            return false;
        if (!players.equals(saveData.players))
            return false;
        if (!flags.equals(saveData.flags))
            return false;
        if (!counters.equals(saveData.counters))
            return false;
        return time == saveData.time;

    }

    @Override public int hashCode() {
        int result = players.hashCode();
        result = 31 * result + flags.hashCode();
        result = 31 * result + counters.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + date;
        return result;
    }

    @Override public String toString() {
        return "SaveData{" +
                        "players=" + players +
                        ", flags=" + flags +
                        ", counters=" + counters +
                        ", time=" + time +
                        ", date=" + date +
                        '}';
    }
}
