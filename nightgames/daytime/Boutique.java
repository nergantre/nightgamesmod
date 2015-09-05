package nightgames.daytime;

import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;

public class Boutique extends Store {
	public Boutique(Character player) {
		super("Boutique", player);
		add(Clothing.getByName("blouse"));
		add(Clothing.getByName("bra"));
		add(Clothing.getByName("skirt"));
		add(Clothing.getByName("panties"));
		add(Clothing.getByName("thong"));
		add(Clothing.getByName("tanktop"));
		add(Clothing.getByName("miniskirt"));
		add(Clothing.getByName("bikinitop"));
		add(Clothing.getByName("bikinibottoms"));
		add(Clothing.getByName("crotchlesspanties"));
		add(Clothing.getByName("opencupbra"));
	}

	@Override
	public boolean known() {
		if (player.hasPussy())
			return Global.checkFlag(Flag.basicStores);
		return false;
	}

	@Override
	public void visit(String choice) {
		Global.gui().clearText();
		Global.gui().clearCommand();
		if(choice=="Start"){
			acted=false;
		}
		if(choice=="Leave"){
			done(acted);
			return;
		}
		checkSale(choice);
		if(player.human()){
			Global.gui().message("This is a store for women's clothing. Things may get a bit expensive here.");
			for(Clothing i: clothing().keySet()){
				Global.gui().message(i.getName()+": "+i.getPrice() +(player.has(i) ? " (Owned)":""));
			}
			Global.gui().message("You have: $"+player.money+" available to spend.");
			displayGoods();
			Global.gui().choose(this,"Leave");
		}
	}

	@Override
	public void shop(Character npc, int budget) {
	}

}
