package nightgames.items;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class TextEffect extends ItemEffect {
	private String format;

	public TextEffect(String verb, String otherverb, String format) {
		super(verb, otherverb, true, true);
		this.format = format;
	}

	@Override
	public boolean use(Combat c, Character user, Character opponent, Item item) {
		c.write(user, Global.format(format, user, opponent));
		return true;
	}
}