package nightgames.characters;

import java.util.HashMap;
import java.util.Map;

public class Growth {
	public float arousal;
	public float stamina;
	public float mojo;
	public float bonusArousal;
	public float bonusStamina;
	public float bonusMojo;
	public int attributes[];
	public int bonusAttributes;
	public float willpower;
	public float bonusWillpower;
	public Map<Integer, Trait> traits;
	public Map<Integer, Runnable> actions;
	public Growth() {
		stamina = 2;
		arousal = 4;
		mojo = 1;
		bonusStamina = 2;
		bonusArousal = 3;
		bonusMojo = 1;
		bonusAttributes = 1;
		willpower = .5f;
		bonusWillpower = .25f;
		attributes = new int[] {
			2,
			3,
			3,
		};
		traits = new HashMap<Integer, Trait>();
		actions = new HashMap<Integer, Runnable>();
	}
}
