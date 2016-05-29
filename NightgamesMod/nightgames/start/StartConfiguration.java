package nightgames.start;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
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
    private PlayerConfiguration player;
    private List<NpcConfiguration> npcs;
    private NpcConfiguration npcCommon;
    private List<Flag> flags;

    private StartConfiguration() {

    }

    public Player buildPlayer(String name) {
        return buildPlayer(name, null, null);
    }

    public Player buildPlayer(String name, List<Trait> traits, CharacterSex gender) {
        return player.build(name, Optional.ofNullable(gender), Optional.ofNullable(traits));
    }

    public List<NPC> buildNpcs() {
        return npcs.stream()
                   .map(n -> n.build(Optional.ofNullable(npcCommon)))
                   .collect(Collectors.toList());
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
    public static StartConfiguration parse(Reader read) throws ParseException {
        StartConfiguration cfg = new StartConfiguration();
        JSONObject root;
        try {
            root = (JSONObject) JSONValue.parseWithException(read);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file.");
        }

        cfg.name = JSONUtils.readString(root, "name");
        cfg.summary = JSONUtils.readString(root, "summary");
        cfg.enabled = JSONUtils.readBoolean(root, "enabled");
        cfg.player = PlayerConfiguration.parse((JSONObject) root.get("player"));
        cfg.npcCommon = NpcConfiguration.parse((JSONObject) root.get("all_npcs"));

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

    public static Collection<StartConfiguration> loadConfigurations() {
        Path dir = new File("starts/").toPath();
        Collection<StartConfiguration> res = new ArrayList<>();
        try {
            for (Path file : Files.newDirectoryStream(dir)) {
                if (file.toString()
                        .endsWith(".json")) {
                    try {
                        res.add(parse(Files.newBufferedReader(file)));
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
        Reader reader = Files.newBufferedReader(new File("starts/Male Start.json").toPath());
        StartConfiguration cfg = parse(reader);
        reader.close();
        System.out.println(cfg);
    }
}
