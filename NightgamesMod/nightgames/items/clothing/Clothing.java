package nightgames.items.clothing;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;

import nightgames.Resources.ResourceLoader;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Trait;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.json.JsonUtils;
import nightgames.items.Loot;

public class Clothing implements Loot {
    public static final int N_LAYERS = 5;
    public static Map<String, Clothing> clothingTable;

    public static void buildClothingTable() {
        clothingTable = new HashMap<>();
        try (InputStreamReader inputstreamreader = new InputStreamReader(
                        ResourceLoader.getFileResourceAsStream("data/clothing/defaults.json"))) {
            JsonArray defaultClothesJson = JsonUtils.rootJson(inputstreamreader).getAsJsonArray();
            JsonClothingLoader.loadClothingListFromJson(defaultClothesJson)
                            .forEach(article -> {
                clothingTable.put(article.id, article);
                if (Global.isDebugOn(DebugFlags.DEBUG_LOADING)) {
                    System.out.println("Loaded " + article.id);
                }
            });
        } catch (ClassCastException | JsonParseException | IOException e) {
            e.printStackTrace();
        }
        ResourceLoader.getFileResourcesFromDirectory("data/clothing")
                      .forEach(inputstream -> {
                          try (InputStreamReader inputstreamreader = new InputStreamReader(inputstream)) {
                              JsonArray clothesJson = new JsonParser().parse(inputstreamreader).getAsJsonArray();
                              JsonClothingLoader.loadClothingListFromJson(clothesJson).forEach(article -> {
                                  clothingTable.put(article.id, article);
                                  if (Global.isDebugOn(DebugFlags.DEBUG_LOADING)) {
                                      System.out.println("Loaded " + article.id);
                                  }
                              });
                          } catch (ClassCastException | JsonParseException | IOException e) {
                              e.printStackTrace();
                          }
                      });
    }

    String name;
    int dc;
    String prefix;
    Set<ClothingTrait> attributes;
    Set<String> stores;
    Set<Trait> buffs;
    Set<ClothingSlot> slots;
    Set<CharacterSex> sex;
    int price;
    double exposure;
    String id;
    double hotness;
    int layer;

    Clothing() {}

    @Override
    public String getName() {
        return name;
    }

    public int dc(Character attacker) {
        if (attacker != null && attacker.has(Trait.bramaster) && layer <= 1 && slots.contains(ClothingSlot.top)) {
            return dc / 4;
        }
        if (attacker != null && attacker.has(Trait.pantymaster) && layer <= 1 && slots.contains(ClothingSlot.bottom)) {
            return dc / 4;
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

    public Set<Trait> buffs() {
        return buffs;
    }

    public Set<ClothingTrait> attributes() {
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

    public Set<ClothingSlot> getSlots() {
        return slots;
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
        return clothingTable.values()
                            .stream()
                            .filter(article -> {
                                return article.stores.contains(shopName);
                            })
                            .collect(Collectors.toList());
    }

    public String getToolTip() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        DecimalFormat format = new DecimalFormat("#.##");
        sb.append(getName());
        if (!getSlots().isEmpty()) {
            sb.append("<br>Slots: [");
            sb.append(slots.stream().map(ClothingSlot::name).collect(Collectors.joining(", ")));
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
            sb.append(attributes.stream().map(ClothingTrait::name).collect(Collectors.joining(", ")));
            sb.append(']');
        }
        if (!buffs().isEmpty()) {
            sb.append("<br>Buffs: [");
            sb.append(buffs.stream().map(Trait::name).collect(Collectors.joining(", ")));
            sb.append(']');
        }
        sb.append("<br>Price: ");
        sb.append(getPrice());
        sb.append("</html>");
        return sb.toString();
    }

    // TODO: Replace small floating point values with integer representations
    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Clothing clothing = (Clothing) o;

        if (dc != clothing.dc)
            return false;
        if (price != clothing.price)
            return false;
        if (!(Math.abs(clothing.exposure - exposure) < 1e-6))
            return false;
        if (!(Math.abs(clothing.hotness - hotness) < 1e-6))
            return false;
        if (layer != clothing.layer)
            return false;
        if (!name.equals(clothing.name))
            return false;
        if (!prefix.equals(clothing.prefix))
            return false;
        if (!attributes.equals(clothing.attributes))
            return false;
        if (!stores.equals(clothing.stores))
            return false;
        if (!buffs.equals(clothing.buffs))
            return false;
        if (!slots.equals(clothing.slots))
            return false;
        if (!sex.equals(clothing.sex))
            return false;
        return id.equals(clothing.id);

    }

    @Override public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        result = 31 * result + dc;
        result = 31 * result + prefix.hashCode();
        result = 31 * result + attributes.hashCode();
        result = 31 * result + stores.hashCode();
        result = 31 * result + buffs.hashCode();
        result = 31 * result + slots.hashCode();
        result = 31 * result + sex.hashCode();
        result = 31 * result + price;
        temp = Double.doubleToLongBits(Double.valueOf(String.format("%.6f", exposure)));
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + id.hashCode();
        temp = Double.doubleToLongBits(Double.valueOf(String.format("%.6f", hotness)));
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + layer;
        return result;
    }
}
