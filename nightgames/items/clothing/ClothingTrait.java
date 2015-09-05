package nightgames.items.clothing;

public enum ClothingTrait {
	open("Open", "Shows what's beneath"),
	bulky("Bulky","Speed penalty"),
	skimpy("Skimpy","Better temptation daamage"),
	flexible("Flexible","Can fuck by pulling it aside"),
	indestructible("Indestructible","Cannot be destroyed"),
	geeky("Geeky","Science bonus"),
	armored("Armored","Protects the delicate bits"),
	mystic("Mystic","Arcane bonus"),
	martial("Martial","Ki bonus"),
	broody("Broody","Dark bonus"),
	kinky("Kinky","Fetish bonus"),
	tentacleUnderwear("Tentacle Underwear","Wearing tentacle underwear"),
	tentacleSuit("Tentacle Suit","Wearing a tentacle suit"),
	stylish("Stylish","Better mojo gain"),
	lame("Lame","Small mojo penalty"),
	none("","");
	
	private String name;
	private String desc;

	ClothingTrait(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}
}
