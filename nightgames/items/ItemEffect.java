package nightgames.items;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class ItemEffect {
	private String selfVerb, otherVerb;
	private boolean drinkable, throwable;
	public ItemEffect() {
		this("", "", false, false);
	}
	public ItemEffect(String verb, String otherverb, boolean drinkable, boolean throwable) {
		this.selfVerb = verb;
		this.otherVerb = otherverb;
		this.drinkable = drinkable;
		this.throwable = throwable;
	}
	public String getSelfVerb() {
		return selfVerb;
	}
	public String getOtherVerb() {
		return otherVerb;
	}
	public boolean use(Combat c, Character user, Character opponent, Item item) {
		return false;
	}
	public boolean throwable() {
		return throwable;
	}
	public boolean drinkable() {
		return drinkable;
	}
}