package nightgames.characters;

import java.util.HashMap;
import java.util.Map;

public class Growth {
	public int arousal;
	public int stamina;
	public int mojo;
	public int bonusArousal;
	public int bonusStamina;
	public int bonusMojo;
	public int attributes[];
	public int bonusAttributes;
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
		attributes = new int[] {
			2,
			3,
			3,
		};
		traits = new HashMap<Integer, Trait>();
		actions = new HashMap<Integer, Runnable>();
	}
}
