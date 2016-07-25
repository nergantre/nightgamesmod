package nightgames.start;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nightgames.characters.Attribute;
import nightgames.characters.CharacterSex;
import nightgames.global.Flag;
import nightgames.json.JsonUtils;
import nightgames.items.clothing.Clothing;

public class StartConfiguration {

    private String name, summary;
    private boolean enabled;
    public PlayerConfiguration player;
    private Collection<NpcConfiguration> npcs;
    public NpcConfiguration npcCommon;
    private Collection<Flag> flags;

    private StartConfiguration() {
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Collection<Flag> getFlags() {
        return flags;
    }

    public boolean playerCanChooseGender() {
        return !player.gender.isPresent();
    }

    public boolean playerCanChooseTraits() {
        return player.allowsMoreTraits();
    }

    public int availableAttributePoints() {
        return player.getAttributePoints();
    }

    public Map<Attribute, Integer> playerAttributes() {
        return new HashMap<>(player.attributes);
    }
    
    public CharacterSex chosenPlayerGender() {
        return player.gender.orElseThrow(() -> new RuntimeException("No gender specified in this configuration"));
    }

    @Override
    public String toString() {
        return name;
    }

    @SuppressWarnings("unchecked") public static StartConfiguration parse(JsonObject root) throws JsonParseException {
        StartConfiguration cfg = new StartConfiguration();

        cfg.name = root.get("name").getAsString();
        cfg.summary = root.get("summary").getAsString();
        cfg.enabled = root.get("enabled").getAsBoolean();
        cfg.player = PlayerConfiguration.parse(root.getAsJsonObject("player"));
        cfg.npcCommon = NpcConfiguration.parseAllNpcs(root.getAsJsonObject("all_npcs"));

        cfg.npcs = new HashSet<>();
        JsonArray npcs = root.getAsJsonArray("npcs");
        npcs.forEach(element -> cfg.npcs.add(NpcConfiguration.parse(element.getAsJsonObject())));
        JsonArray flags = root.getAsJsonArray("flags");
        cfg.flags = JsonUtils.collectionFromJson(flags, Flag.class);
        return cfg;
    }

    public Optional<NpcConfiguration> findNpcConfig(String type) {
        return npcs.stream().filter(npc -> type.equals(npc.type)).findAny();
    }

    public static Collection<StartConfiguration> loadConfigurations() {
        Path dir = new File("starts/").toPath();
        Collection<StartConfiguration> res = new ArrayList<>();
        try {
            for (Path file : Files.newDirectoryStream(dir)) {
                if (file.toString()
                        .endsWith(".json")) {
                    try {
                        res.add(parse(JsonUtils.rootJson(file).getAsJsonObject()));
                    } catch (Exception e) {
                        System.out.println("Failed to load configuration from " + file.toString() + ": ");
                        System.out.flush();
                        e.printStackTrace();
                        System.err.flush();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public static void main(String[] args) throws IOException, JsonParseException {
        Clothing.buildClothingTable();
        Path path = new File("starts/Male Start.json").toPath();
        StartConfiguration cfg = parse(JsonUtils.rootJson(path).getAsJsonObject());
        System.out.println(cfg);
    }
}
