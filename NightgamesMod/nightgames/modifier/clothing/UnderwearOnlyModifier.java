package nightgames.modifier.clothing;

import java.util.Collections;
import java.util.Set;

import org.json.simple.JSONObject;

import nightgames.modifier.ModifierComponent;

public class UnderwearOnlyModifier extends ClothingModifier implements ModifierComponent<UnderwearOnlyModifier>{

	@Override
	public Set<Integer> allowedLayers() {
		return Collections.singleton(0);
	}

	@Override
	public String name() {
		return "underwear-only";
	}

	@Override
	public UnderwearOnlyModifier instance(JSONObject obj) {
		return new UnderwearOnlyModifier();
	}
	
	@Override
	public String toString() {
		return name();
	}
	
}
