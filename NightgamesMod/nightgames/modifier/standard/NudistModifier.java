package nightgames.modifier.standard;

import nightgames.modifier.BaseModifier;
import nightgames.modifier.clothing.NudeModifier;

public class NudistModifier extends BaseModifier {

	public NudistModifier() {
		this.clothing = new NudeModifier();
	}
	
	@Override
	public int bonus() {
		return 125;
	}

	@Override
	public String name() {
		return "nudist";
	}

}
