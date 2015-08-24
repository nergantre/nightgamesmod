package nightgames.cmatch.prepare;

import java.util.List;

import nightgames.characters.Character;
import nightgames.cmatch.CustomMatch;
import nightgames.items.Item;

/**
 * Change to ensure a character holds at most X copies of an item.
 */
public class TakeItemChange implements PrematchChange {

	private Item item;
	private int count;
	private int startCount;
	
	public TakeItemChange(Item item, int count) {
		this.item = item;
		this.count = count;
	}

	@Override
	public void apply(CustomMatch match, Character ch, List<PrematchChange> changeList) {
		int current = ch.getInventory().getOrDefault(item, 0);
		if (current > count) {
			ch.getInventory().put(item, count);
			startCount = current;
			changeList.add(this);
		} else {
			startCount = -1;
		}
	}

	@Override
	public void revert(CustomMatch match, Character ch) {
		if (startCount >= 0)
			ch.getInventory().put(item, startCount);
	}

	@Override
	public PrematchChange copy() {
		return new TakeItemChange(item, count);
	}


}
