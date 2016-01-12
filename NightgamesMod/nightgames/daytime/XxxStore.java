package nightgames.daytime;

import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import java.util.Map;

public class XxxStore extends Store{
	
	public XxxStore(Character player) {
		super("XXX Store", player);
		Clothing.getAllBuyableFrom("XxxStore").forEach(
				article -> {
					add(article);
				}
		);
		add(Item.Dildo);
		add(Item.Onahole);
		add(Item.Lubricant);
		add(Item.Crop);
		add(Item.Tickler);
		add(Item.Strapon);
	}

	@Override
	public boolean known() {
		return Global.checkFlag(Flag.basicStores);
	}

	@Override
	public void visit(String choice) {
		Map<Item, Integer> MyInventory = this.player.getInventory();
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
					if (MyInventory.get(i) == null || MyInventory.get(i) == 0) {
						Global.gui().message(i.getName()+": $"+i.getPrice());
					}
					else {
						Global.gui().message(i.getName()+": $"+i.getPrice() + " (you have: " + MyInventory.get(i) + ")");
					}
				}
			Global.gui().message("You have :$"+player.money+" to spend.");
			Global.gui().sale(this,Item.Lubricant);
			
			if(player.has(Item.Dildo2)){
				Global.gui().message("You already have a much better dildo. You don't need an average one.");
				if (!player.has(Item.Dildo)) {
					Global.gui().sale(this,Item.Dildo);
				}
			}
			else if(player.has(Item.Dildo)){
				Global.gui().message("You already have a perfectly serviceable dildo. You don't need another.");
			}
			else{
				Global.gui().sale(this,Item.Dildo);
			}
			
			if(player.has(Item.Onahole)){
				Global.gui().message("You already have the best onahole in stock. You don't need another.");
			}
			else if(player.has(Item.Onahole2)){
				Global.gui().message("You already have the best onahole you can dream of.");
			}
			else{
				Global.gui().sale(this,Item.Onahole);
			}
			
			if(player.has(Item.Crop)){
				Global.gui().message("You already have a riding crop. You don't need two.");
			}
			else if(player.has(Item.Crop2)){
				Global.gui().message("Your current riding crop is already overkill.");
			}
			else{
				Global.gui().sale(this,Item.Crop);
			}
			
			if(player.has(Item.Tickler)){
				Global.gui().message("Your current tickler is at least as good as anything they are selling.");
			}
			else if(player.has(Item.Tickler2)){
				Global.gui().message("Nothing on sale is half as good as your current tickler.");
			}
			else{
				Global.gui().sale(this,Item.Tickler);
			}
			
			if (player.hasDick()) {
				Global.gui().message("You see a strap-on dildo for sale. It's no use to you since you have "
						+ "the real thing, but you should watch your ass if the girls start buying these.");
			}
			else if(player.has(Item.Strapon)){
				Global.gui().message("You are plenty satisfied with the strap-on you already have.");
			}
			else if(player.has(Item.Strapon2)){
				Global.gui().message("Your strapon is even better than the real thing already.");
			}
			else {				
				Global.gui().sale(this,Item.Strapon);
			}
			
			displayClothes();
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
