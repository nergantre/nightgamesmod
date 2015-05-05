package items;

import global.Global;
import combat.Combat;
import characters.Character;
import status.Status;

public class BuffEffect extends ItemEffect {
	private Status applied;
	public BuffEffect(String verb, String otherverb, Status status) {
		super(verb, otherverb, true, true);
		this.applied = status;
	}
	public boolean use(Combat c, Character user, Character opponent, Item item) {
		if (c != null) {
			c.write(user, Global.format(String.format("{self:SUBJECT-ACTION:are|is} now %s", applied.name), user, opponent));
		} else if (user.human()) {
			Global.gui().message(Global.format(String.format("{self:SUBJECT-ACTION:are|is} now %s", applied.name), user, opponent));
		}
		user.add(applied.instance(user, opponent));
		return true;
	}
}