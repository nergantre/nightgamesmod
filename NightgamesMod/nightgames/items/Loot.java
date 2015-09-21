package nightgames.items;

import nightgames.characters.Character;

public interface Loot {
	public String getName();
	public String pre();
	public int getPrice();
	public void pickup(Character owner);
	public String getID();
}
