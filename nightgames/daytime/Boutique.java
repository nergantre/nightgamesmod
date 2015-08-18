package nightgames.daytime;

import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Clothing;
import nightgames.items.Item;

public class Boutique extends Store {
	public Boutique(Character player) {
		super("Boutique", player);
		add(Clothing.blouse);
		add(Clothing.bra);
		add(Clothing.skirt);
		add(Clothing.panties);
		add(Clothing.thong);
		add(Clothing.tanktop);
		add(Clothing.miniskirt);
		add(Clothing.bikinitop);
		add(Clothing.bikinibottoms);
		add(Clothing.crotchlesspanties);
		add(Clothing.opencupbra);
		add(Clothing.latextop);
		add(Clothing.latexpants);
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
