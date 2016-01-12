package nightgames.items.clothing;

import java.util.Comparator;

public class ClothingSorter implements Comparator<Clothing> {
	private ClothingSlot getTopSlot(Clothing article) {
		ClothingSlot topSlot = ClothingSlot.feet;
		for(ClothingSlot slot :article.getSlots()) {
			if (slot.ordinal() < topSlot.ordinal()) {
				topSlot = slot;
			}
		}
		return topSlot;
	}
	@Override
	public int compare(Clothing a, Clothing b) {
		ClothingSlot aSlot = getTopSlot(a);
		ClothingSlot bSlot = getTopSlot(b);
		if (aSlot == bSlot) {
			if (a.getLayer() == b.getLayer()) {
				return a.getName().compareTo(b.getName());
			}
			return b.getLayer() - a.getLayer();
		}
		return aSlot.ordinal() - bSlot.ordinal();
	}

}
