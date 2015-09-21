package nightgames.daytime;

import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;


public class HWStore extends Store {
	public HWStore(Character player) {
		super("Hardware Store", player);
		add(Item.Tripwire);
		add(Item.Rope);
		add(Item.Spring);
		add(Item.Sprayer);
		add(Item.EmptyBottle);
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
			Global.gui().message("Nothing at the hardware store is designed for the sort of activities you have in mind, but there are components you could use to make some " +
					"effective traps.");
			for(Item i: stock.keySet()){
				Global.gui().message(i.getName()+": "+i.getPrice());
			}
			Global.gui().message("You have: $"+player.money+" available to spend.");
			displayGoods();
			Global.gui().choose(this,"Leave");
		}
	}

	@Override
	public void shop(Character npc, int budget) {
		int remaining = budget;
		int bored = 0;
		while(remaining>10&&bored<10){
			for(Item i:stock.keySet()){
				if(remaining>i.getPrice()&&!npc.has(i,20)){
					npc.gain(i);
					npc.money-=i.getPrice();
					remaining-=i.getPrice();
				}
				else{
					bored++;
				}
			}
		}
	}
}
