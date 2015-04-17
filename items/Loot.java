package items;

import characters.Character;

public interface Loot {
	public String getName();
	public String pre();
	public int getPrice();
	public void pickup(Character owner);
}
