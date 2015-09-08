package nightgames.items.clothing;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import nightgames.Resources.ResourceLoader;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Trait;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.items.Loot;

public class Clothing implements Loot{
	public static final int N_LAYERS = 5;
	public static Map<String, Clothing> clothingTable;
	public static void buildClothingTable() {
		clothingTable = new HashMap<String, Clothing>();
		ResourceLoader.getFileResourcesFromDirectory("data/clothing").forEach(inputstream -> {
			try {
				JSONArray value = (JSONArray) JSONValue.parse(new InputStreamReader(inputstream));
				JSONClothingLoader.loadClothingListFromJSON(value).forEach(article -> {
					clothingTable.put(article.id, article);
					if (Global.isDebugOn(DebugFlags.DEBUG_LOADING)) {
						System.out.println("Loaded " + article.id);
					}
				});
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		});
	}

	String name;
	int dc;
	String prefix;
	List<ClothingTrait> attributes;
	List<Trait> buffs;
	private List<ClothingSlot> slots;
	List<CharacterSex> sex;
	int price;
	double exposure;
	String id;
	double hotness;
	private int layer;
	Clothing() {}
	public String getName(){
		return name;
	}
	public int dc(){
		return dc;
	}
	public String pre(){
		return prefix;
	}
	public boolean buffs(Trait test){
		return buffs.contains(test);
	}
	public List<Trait> buffs() {
		return buffs;
	}
	public List<ClothingTrait> attributes(){
		return attributes;
	}
	public int getPrice(){
		return this.price;
	}

	@Override
	public void pickup(Character owner) {
		if(!owner.has(this)){
			owner.gain(this);
		}
	}

	public static Set<Clothing> femaleOnlyClothing; 

	public static Clothing getByName(String key) {
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
		return this.id;
	}
}
