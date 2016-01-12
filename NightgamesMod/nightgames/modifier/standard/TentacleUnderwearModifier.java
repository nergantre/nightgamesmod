package nightgames.modifier.standard;

import nightgames.modifier.BaseModifier;
import nightgames.modifier.clothing.ForceClothingModifier;

public class TentacleUnderwearModifier extends BaseModifier {

	public TentacleUnderwearModifier() {
		this.clothing = new ForceClothingModifier("tentaclebottom");
	}

	@Override
	public int bonus() {
		return 75;
	}

	@Override
	public String name() {
		return "tentacle-underwear";
	}

	@Override
	public String intro() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String acceptance() {
		// TODO Auto-generated method stub
		return null;
	}

}
