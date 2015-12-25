package nightgames.characters.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nightgames.characters.Attribute;
import nightgames.characters.Trait;

public class Stats {
	public int						level;
	public Map<Attribute, Integer>	attributes;
	public float					stamina;
	public float					mojo;
	public float					arousal;
	public float					willpower;
	public List<Trait>				traits;

	public Stats() {
		level = 1;
		attributes = new HashMap<>();
		attributes.put(Attribute.Seduction, 5);
		attributes.put(Attribute.Cunning, 5);
		attributes.put(Attribute.Power, 5);
		attributes.put(Attribute.Speed, 5);
		attributes.put(Attribute.Perception, 5);
		stamina = 25;
		arousal = 50;
		mojo = 25;
		willpower = 50;
		traits = new ArrayList<Trait>();
	}
}
