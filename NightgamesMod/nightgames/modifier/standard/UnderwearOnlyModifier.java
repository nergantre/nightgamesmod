package nightgames.modifier.standard;

import nightgames.modifier.BaseModifier;

public class UnderwearOnlyModifier extends BaseModifier {

	public UnderwearOnlyModifier() {
		this.clothing = new nightgames.modifier.clothing.UnderwearOnlyModifier();
	}
	
	@Override
	public int bonus() {
		return 100;
	}

	@Override
	public String name() {
		return "underwearonly";
	}

}
