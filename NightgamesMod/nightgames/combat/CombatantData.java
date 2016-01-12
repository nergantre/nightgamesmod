package nightgames.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nightgames.items.clothing.Clothing;

public class CombatantData implements Cloneable {
	private List<Clothing> clothespile;
	private Map<String, Number> flags;

	public CombatantData() {
		clothespile = new ArrayList<>();
		flags = new HashMap<String, Number>();
	}

	@Override
	public Object clone() {
		CombatantData newData = new CombatantData();
		newData.clothespile = new ArrayList<>(clothespile);
		newData.flags = new HashMap<>(flags);
		return newData;
	}

	public void addToClothesPile(Clothing article) {
		if (article != null) {
			clothespile.add(article);
		}
	}

	public List<Clothing> getClothespile() {
		return clothespile;
	}

	public void toggleFlagOn(String key, boolean val) {
		flags.put(key, new Integer(val ? 1 : 0));
	}

	public void setIntegerFlag(String key, int val) {
		flags.put(key, new Integer(val));
	}

	public void setFloatFlag(String key, int val) {
		flags.put(key, new Integer(val));
	}

	public int getIntegerFlag(String key) {
		return flags.containsKey(key) ? flags.get(key).intValue() : 0;
	}

	public boolean getBooleanFlag(String key) {
		return flags.containsKey(key) ? flags.get(key).intValue() != 0 : false;
	}

	public float getFloatFlag(String key) {
		return flags.containsKey(key) ? flags.get(key).floatValue() : 0f;
	}
}
