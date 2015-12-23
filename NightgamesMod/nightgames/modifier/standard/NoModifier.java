package nightgames.modifier.standard;

import nightgames.modifier.BaseModifier;

public class NoModifier extends BaseModifier {

	@Override
	public int bonus() {
		return 0;
	}

	@Override
	public String name() {
		return "normal";
	}

	@Override
	public String intro() {
		return "";
	}

	@Override
	public String acceptance() {
		return "";
	}

}
