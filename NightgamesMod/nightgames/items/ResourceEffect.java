package nightgames.items;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class ResourceEffect extends ItemEffect {
	private String type;
	private int amt;
	public ResourceEffect(String type, int amt) {
		super("","",true, true);
		this.amt = amt;
		this.type = type;
	}
	public boolean use(Combat c, Character user, Character opponent, Item item) {
		if (type.equals("arouse")) {
			user.arouse(amt, c);
		} else if (type.equals("calm")) {
			user.calm(c, amt);
		} else if (type.equals("weaken")) {
			user.weaken(c, amt);
		} else if (type.equals("heal")) {
			user.heal(c, amt);
		} else if (type.equals("build")) {
			user.buildMojo(c, amt);
		} else if (type.equals("lose")) {
			user.loseMojo(c, amt);
		} else if (type.equals("pain")) {
			user.pain(c, amt);
		} else if (type.equals("wprestore")) {
			user.restoreWillpower(c, amt);
		} else if (type.equals("wplose")) {
			user.loseWillpower(c, amt, false);
		} else {
			return false;
		}
		return true;
	}
}