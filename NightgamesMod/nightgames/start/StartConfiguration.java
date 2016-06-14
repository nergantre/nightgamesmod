package nightgames.start;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import nightgames.characters.Attribute;
import nightgames.characters.CharacterSex;
import nightgames.characters.NPC;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.items.clothing.Clothing;

public class StartConfiguration {

    private String name, summary;
    private boolean enabled;
    public PlayerConfiguration player;
    private List<NpcConfiguration> npcs;
    public NpcConfiguration npcCommon;
    private List<Flag> flags;

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

    public List<Flag> getFlags() {
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

    /**
     * Sets the configured basic attributes to the specified values. Useful for applying spent attribute points after creation.
     *
     * @param power Value to apply to the player's Power attribute.
     * @param seduction Value to apply to the player's Seduction attribute.
     * @param cunning Value to apply to the player's Cunning attribute.
     */
    public void setPlayerAttributePoints(int power, int seduction, int cunning) {
        player.attributes.put(Attribute.Power, power);
        player.attributes.put(Attribute.Seduction, seduction);
        player.attributes.put(Attribute.Cunning, cunning);
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

    @SuppressWarnings("unchecked")
    public static StartConfiguration parse(JSONObject root) throws ParseException {
        StartConfiguration cfg = new StartConfiguration();

        cfg.name = JSONUtils.readString(root, "name");
        cfg.summary = JSONUtils.readString(root, "summary");
        cfg.enabled = JSONUtils.readBoolean(root, "enabled");
        cfg.player = PlayerConfiguration.parse((JSONObject) root.get("player"));
        cfg.npcCommon = new NpcConfiguration();
        cfg.npcCommon.parseCommon((JSONObject) root.get("all_npcs"));

        JSONArray npcs = (JSONArray) root.get("npcs");
        cfg.npcs = ((Stream<Object>) npcs.stream()).map(o -> (JSONObject) o)
                                                   .map(NpcConfiguration::parse)
                                                   .collect(Collectors.toList());
        JSONArray flags = (JSONArray) root.get("flags");
        cfg.flags = ((Stream<Object>) flags.stream()).map(Object::toString)
                                                     .map(Flag::valueOf)
                                                     .collect(Collectors.toList());
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
                        res.add(parse(JSONUtils.rootFromFile(file)));
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

    public static void main(String[] args) throws IOException, ParseException {
        Clothing.buildClothingTable();
        Path path = new File("starts/Male Start.json").toPath();
        StartConfiguration cfg = parse(JSONUtils.rootFromFile(path));
        System.out.println(cfg);
    }
}
