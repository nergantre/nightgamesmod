package items;

import characters.Character;

public enum Item implements Loot{
	
	Tripwire	( "Trip Wire",10, "A strong wire used to trigger traps","a "	),
	Spring		( "Spring",20,	"A component for traps","a "	),
	Rope		( "Rope",15,"A component for traps","a "	),
	Phone		( "Phone",30,"A cheap disposable phone with a programable alarm","a "		),
	ZipTie		( "Heavy Zip Tie",5,"A thick heavy tie suitable for binding someone's hands","a "),
	Handcuffs	( "Handcuffs",200,"Strong steel restraints, hard to escape from",""),
	Lubricant	( "Lubricant",20,"Helps you pleasure your opponent, but makes her hard to hang on to","some "	),
	EnergyDrink ( "Energy Drink",20,"It'll either kill you or restore your stamina","an "),
	Aphrodisiac	( "Aphrodisiac",40,"Can be thrown like a 'horny bomb'","an "	),
	Beer		( "Beer",10,"Tastes like horsepiss, but it'll numb your senses","a can of "	),
	Sedative	( "Sedative",25,"Tires out your opponent, but can also make her numb","a "	),
	DisSol		( "Dissolving Solution",30,"Destroys clothes, but completely non-toxic","a "),
	Dildo		( "Dildo",250,"Big rubber cock: not a weapon","a "	),
	Crop		( "Riding Crop",200,"Delivers a painful sting to instill discipline","a "	),
	Onahole		( "Onahole",300,"An artificial vagina, but you can probably find some real ones pretty easily","an "	),
	Tickler		( "Tickler",300,"Tickles and pleasures your opponent's sensitive areas","a "	),
	Clothing	( "Set of Clothes",0,"A trophy of your victory","a " ),
	CassieTrophy	("Cassie's Panties",0,"Cute and simple panties",""),
	MaraTrophy	("Mara's Underwear",0,"She wears boys underwear?",""),
	AngelTrophy	("Angel's Thong",0,"There's barely anything here",""),
	JewelTrophy	("Jewel's Panties",0,"Surprisingly lacy",""),
	ReykaTrophy	("Reyka's Clit Ring",0,"What else can you take from someone who goes commando?",""),
	PlayerTrophy("Your Boxers",0,"How did you end up with these?",""),
	EveTrophy	("Eve's 'Panties'",0,"Crotchless and of no practical use",""),
	KatTrophy	("Kat's Panties",0,"Cute pink panties",""),
	YuiTrophy	("Yui's Panties",0,"",""),
	Sprayer		( "Sprayer",30,	"Necessary for making traps that use liquids","a "	),
	SPotion		( "Sensitivity Potion",25,"Who knows whats in this stuff, but it makes any skin it touches tingle","a "),
	Strapon		( "Strap-on Dildo",600,"Penis envy much?","a "),
	Dildo2		( "Sonic Dildo",2000,"Apparently vibrates at the ideal frequency to produce pleasure","a "	),
	Crop2		( "Hunting Crop",1500,"Equipped with the fearsome Treasure Hunter attachment","a "	),
	Onahole2	( "Wet Onahole",3000,"As hot and wet as the real thing","an "	),
	Tickler2	( "Enhanced Tickler",3000,"Coated with a substance that can increase sensitivity","an "	),
	Strapon2	( "Flex-O-Peg",2500,"A more flexible and versatile strapon with a built in vibrator","the patented "),
	ShockGlove	( "Shock Glove",800,"Delivers a safe, but painful electric shock", "a " ),
	Aersolizer	( "Aerosolizer",500, "Turns a liquid into an unavoidable cloud of mist", "an "),
	Battery		( "Battery",0,"Available energy to power electronic equipment","a "),
	Semen		( "Semen",0 ,"A small bottle filled with cum. Kinda gross", "a bottle of "),
	Ward		( "Dark Ward",100,"","a "),
	FaeScroll	( "Summoning Scroll",150,"","a "),
	Totem		( "Fetish Totem",150,"A small penis shaped totem that can summon tentacles","a "),
	Capacitor	( "Capacitor",30,"","a ");
	
	/**
	 * The Item's display name.
	 */
	private String desc;
	private String name;
	private String prefix;
	private int price;
	/**
	 * @return the Item name
	 */
	public String getDesc()
	{
		return desc;
	}
	public int getPrice(){
		return price;
	}
	public String getName(){
		return name;
	}
	public String pre(){
		return prefix;
	}
	@Override
	public void pickup(Character owner) {
		owner.gain(this);
	}
	private Item( String name, int price, String desc,String prefix )
	{
		this.name = name;
		this.price = price;
		this.desc = desc;
		this.prefix = prefix;
	}
}
