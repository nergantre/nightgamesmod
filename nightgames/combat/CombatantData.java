package nightgames.combat;

import java.util.ArrayList;
import java.util.List;

import nightgames.items.clothing.Clothing;

public class CombatantData implements Cloneable {
	private List<Clothing> clothespile;
	public CombatantData() {
		clothespile = new ArrayList<>();
	}
	
	@Override
	public Object clone() {
		CombatantData newData = new CombatantData();
		newData.clothespile = new ArrayList<>(clothespile);
		return newData;
	}
	
	public void addToClothesPile(Clothing article) {
		if (article != null)
			clothespile.add(article);
	}
	
	public List<Clothing> getClothespile() {
		return clothespile;
	}
}
