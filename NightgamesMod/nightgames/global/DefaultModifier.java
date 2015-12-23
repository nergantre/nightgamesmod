package nightgames.global;

import nightgames.modifier.Modifier;
import nightgames.modifier.clothing.NudeModifier;
import nightgames.modifier.clothing.UnderwearOnlyModifier;
import nightgames.status.Hypersensitive;
import nightgames.characters.Character;
public enum DefaultModifier implements Modifier {
	normal	(0),		//No challenge
	underwearonly (100),//Only get boxers when resupplying
	nudist	(125),		//No clothes
	norecovery (100),	//Arousal only empties on loss or draw, no masturbation
	vibration (75), 	//Small arousal gain in upkeep
	vulnerable (75),	//Permanent Hypersensitive effect
	pacifist (100),		//Unable to use pain attacks
	notoys (50),		//Unable to use toys
	noitems (50),		//Unable to use consumables
	ftc (0),			//Fuck the Carrier
	maya(0),			
	;
	
	//private static final ItemModifier NO_TOYS_MODIFIER = new BanToysModifier();
	//private static final ItemModifier NO_ITEMS_MODIFIER = new BanConsumablesModifier();
	
	private int bonus;
	private DefaultModifier(int bonus){
		this.bonus=bonus;
	}
	public int bonus(){
		return this.bonus;
	}
	
	@Override
	public void handleOutfit(Character c) {
		if (this == underwearonly && c.human()) {
			new UnderwearOnlyModifier().apply(c.outfit);
		} else if (this == nudist && c.human()) {
			new NudeModifier().apply(c.outfit);
		}
	}
	
	@Override
	public void handleTurn(Character c) {
		if (this == vibration && c.human()) {
			c.tempt(5);
		}
	}
	
	@Override
	public void handleStatus(Character c) {
		if (this == vulnerable && c.human()) {
			c.add(new Hypersensitive(c));
		}
	}
}
