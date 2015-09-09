package nightgames.daytime;

import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;

public class XxxStore extends Store{
	
	public XxxStore(Character player) {
		super("XXX Store", player);
		add(Item.Lubricant);
		add(Item.Dildo);
		add(Item.Onahole);
		add(Item.Crop);
		add(Item.Tickler);
		Clothing.getAllBuyableFrom("XxxStore");
	}

	@Override
	public boolean known() {
		return Global.checkFlag(Flag.basicStores);
	}

	@Override
	public void visit(String choice) {
		if(choice=="Start"){
			acted=false;
		}
		Global.gui().clearText();
		Global.gui().clearCommand();
		if(choice=="Leave"){
			done(acted);
			return;
		}
		checkSale(choice);
		if(player.human()){
			Global.gui().message("The adult specialty store stocks several items that could be useful during a match.");
			for(Item i: stock.keySet()){
				Global.gui().message(i.getName()+": "+i.getPrice());
			}
			Global.gui().message("You have :$"+player.money+" to spend.");
			Global.gui().sale(this,Item.Lubricant);
			if(player.has(Item.Dildo)||player.has(Item.Dildo2)){
				Global.gui().message("You already have a perfectly servicable dildo. You don't need another.");
			}
			else{
				Global.gui().sale(this,Item.Dildo);
			}
			if(player.has(Item.Onahole)||player.has(Item.Onahole2)){
				Global.gui().message("You already have the best onahole in stock. You don't need another.");
			}
			else{
				Global.gui().sale(this,Item.Onahole);
			}
			if(player.has(Item.Crop)||player.has(Item.Crop2)){
				Global.gui().message("You already have a riding crop. You don't need two.");
			}
			else{
				Global.gui().sale(this,Item.Crop);
			}
			if(player.has(Item.Tickler)||player.has(Item.Tickler2)){
				Global.gui().message("Your current tickler is at least as good as anything they are selling.");
			}
			else{
				Global.gui().sale(this,Item.Tickler);
			}
			Global.gui().message("You see a strapon dildo for sale. It's no use to you since you have the real thing, but you should watch your ass if the girls start " +
					"buying these.");
			Global.gui().choose(this,"Leave");
		}
	}

	@Override
	public void shop(Character npc, int budget) {
		if(!npc.has(Item.Lubricant, 10)){
			int i = Math.min(budget/Item.Lubricant.getPrice(), 10 - npc.count(Item.Lubricant));
			npc.gain(Item.Lubricant, i);
			budget -= i*Item.Lubricant.getPrice();
			npc.money-=i*Item.Lubricant.getPrice();
		}
	}
}
