package nightgames.items.clothing;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import nightgames.Resources.ResourceLoader;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Trait;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.items.Loot;

public class Clothing implements Loot {
    public static final int N_LAYERS = 5;
    public static Map<String, Clothing> clothingTable;

    public static void buildClothingTable() {
        clothingTable = new HashMap<String, Clothing>();
        try {
            JSONArray value = (JSONArray) JSONValue.parseWithException(new InputStreamReader(
                            ResourceLoader.getFileResourceAsStream("data/clothing/defaults.json")));
            JSONClothingLoader.loadClothingListFromJSON(value).forEach(article -> {
                clothingTable.put(article.id, article);
                if (Global.isDebugOn(DebugFlags.DEBUG_LOADING)) {
                    System.out.println("Loaded " + article.id);
                }
            });
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResourceLoader.getFileResourcesFromDirectory("data/clothing").forEach(inputstream -> {
            try {
                JSONArray value = (JSONArray) JSONValue.parseWithException(new InputStreamReader(inputstream));
                JSONClothingLoader.loadClothingListFromJSON(value).forEach(article -> {
                    clothingTable.put(article.id, article);
                    if (Global.isDebugOn(DebugFlags.DEBUG_LOADING)) {
                        System.out.println("Loaded " + article.id);
                    }
                });
            } catch (ClassCastException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    String name;
    int dc;
    String prefix;
    List<ClothingTrait> attributes;
    List<String> stores;
    List<Trait> buffs;
    private List<ClothingSlot> slots;
    List<CharacterSex> sex;
    int price;
    double exposure;
    String id;
    double hotness;
    private int layer;

    Clothing() {}

    @Override
    public String getName() {
        return name;
    }

    public int dc(Character attacker) {
        if (attacker != null && attacker.has(Trait.bramaster) && layer == 0 && slots.contains(ClothingSlot.top)) {
            return dc / 2;
        }
        if (attacker != null && attacker.has(Trait.pantymaster) && layer == 0 && slots.contains(ClothingSlot.bottom)) {
            return dc / 2;
        }
        return dc;
    }

    public int dc() {
        return dc(null);
    }

    @Override
    public String pre() {
        return prefix;
    }

    public boolean buffs(Trait test) {
        return buffs.contains(test);
    }

    public List<Trait> buffs() {
        return buffs;
    }

    public List<ClothingTrait> attributes() {
        return attributes;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void pickup(Character owner) {
        if (!owner.has(this)) {
            owner.gain(this);
        }
    }

    public static Set<Clothing> femaleOnlyClothing;

    public static Clothing getByID(String key) {
        Clothing results = clothingTable.get(key);
        if (results == null) {
            throw new IllegalArgumentException(key + " is not a valid clothing key");
        }
        return results;
    }

    public boolean is(ClothingTrait trait) {
        return attributes.contains(trait);
    }

    public double getExposure() {
        return exposure;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public List<ClothingSlot> getSlots() {
        return slots;
    }

    public void setSlots(List<ClothingSlot> slots) {
        this.slots = slots;
    }

    public double getHotness() {
        return hotness;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getID() {
        return id;
    }

    public static List<Clothing> getAllBuyableFrom(String shopName) {
        return clothingTable.values().stream().filter(article -> {
            return article.stores.contains(shopName);
        }).collect(Collectors.toList());
    }

    public String getToolTip() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        DecimalFormat format = new DecimalFormat("#.##");
        sb.append(getName());
        if (!getSlots().isEmpty()) {
            sb.append("<br>Slots: [");
            sb.append(getSlots().stream().reduce((a, b) -> {
                sb.append(a.name());
                sb.append(", ");
                return b;
            }).get().name());
            sb.append(']');
        }
        sb.append("<br>Layer: ");
        sb.append(getLayer());
        sb.append("<br>Appearance: ");
        sb.append(format.format(getHotness()));
        sb.append("<br>Exposure: ");
        sb.append(format.format(getExposure()));
        if (!attributes().isEmpty()) {
            sb.append("<br>Attributes: [");
            sb.append(attributes().stream().reduce((a, b) -> {
                sb.append(a.getName());
                sb.append(", ");
                return b;
            }).get().name());
            sb.append(']');
        }
        if (!buffs().isEmpty()) {
            sb.append("<br>Buffs: [");
            sb.append(buffs().stream().reduce((a, b) -> {
                sb.append(a.toString());
                sb.append(", ");
                return b;
            }).get().name());
            sb.append(']');
        }
        sb.append("<br>Price: ");
        sb.append(getPrice());
        sb.append("</html>");
        return sb.toString();
    }
}
