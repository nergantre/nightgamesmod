package daytime;

import items.Clothing;
import items.Item;
import global.Flag;
import global.Global;
import characters.Character;

public class ClothingStore extends Store {

	public ClothingStore(Character player) {
		super("Clothing Store", player);
		add(Clothing.Tshirt);
		add(Clothing.shirt);
		add(Clothing.sweatshirt);
		add(Clothing.sweater);
		add(Clothing.silkShirt);
		add(Clothing.jeans);
		add(Clothing.shorts);
		add(Clothing.sweatpants);
		add(Clothing.dresspants);
		add(Clothing.boxers);
		add(Clothing.briefs);
		add(Clothing.sweatshirt);
		add(Clothing.undershirt);
		add(Clothing.trenchcoat);
		add(Clothing.jacket);
		add(Clothing.windbreaker);
		add(Clothing.blazer);
		add(Clothing.gothshirt);
		add(Clothing.gothpants);
	}

	@Override
	public boolean known() {
		return Global.checkFlag(Flag.basicStores);
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
			Global.gui().message("This is a normal retail clothing outlet. For obvious reasons, you'll need to buy anything you want to wear at night in bulk.");
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
		// TODO Auto-generated method stub

	}

}
