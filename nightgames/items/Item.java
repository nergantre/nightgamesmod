package nightgames.items;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.TentaclePart;
import nightgames.characters.body.WingsPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.Alluring;
import nightgames.status.Distorted;
import nightgames.status.Shamed;
import nightgames.status.Shield;
import nightgames.status.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public enum Item implements Loot {
	
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
	AiriTrophy	("A piece of hardened gel",0,"Not sure what else to take",""),
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
	semen		( "Semen",100 ,"A small bottle filled with cum. Kinda gross", "a bottle of ",
			Arrays.asList((ItemEffect)
					new ConditionalEffect(new GroupEffect(Arrays.asList((ItemEffect)
						new BuffEffect("drink", "throw", new Abuff(Global.noneCharacter(), Attribute.Dark, 2, 15)),
						new BuffEffect("drink", "throw", new Abuff(Global.noneCharacter(), Attribute.Seduction, 2, 15)),
						new BuffEffect("drink", "throw", new Alluring(Global.noneCharacter(), 5)),
						new ResourceEffect("heal", 30),
						new ResourceEffect("build", 30),
						new ResourceEffect("arouse", 10)
						)), new ConditionalEffect.EffectCondition() {
							@Override
							public boolean operation(Combat c, Character user, Character opponent,
									Item item) {
								return user.has(Trait.succubus);
							}
						}),
					new ConditionalEffect(new GroupEffect(Arrays.asList((ItemEffect)
						new BuffEffect("drink", "throw", new Shamed(Global.noneCharacter())),
						new ResourceEffect("arouse", 10)
						)), new ConditionalEffect.EffectCondition() {
							@Override
							public boolean operation(Combat c, Character user, Character opponent,
									Item item) {
								return !user.has(Trait.succubus);
							}
						})),
					15),
	Ward		( "Dark Ward",100,"","a "),
	FaeScroll	( "Summoning Scroll",150,"","a "),
	Totem		( "Fetish Totem",150,"A small penis shaped totem that can summon tentacles","a "),
	Capacitor	( "Capacitor",30,"","a "),
	TinyDraft	( "Tiny Draft", 100, "Temporarily shrink a penis", "a ", Collections.singleton((ItemEffect)new BodyModEffect("drink", "throw", CockPart.average, BodyModEffect.Effect.downgrade)), 15),
	PriapusDraft( "Priapus Draft", 150, "Temporarily grow a penis", "a ", Collections.singleton((ItemEffect)new BodyModEffect("drink", "throw", CockPart.average, BodyModEffect.Effect.growplus)), 15),
	BustDraft	( "Bust Draft", 80, "Temporarily grow breasts", "a ", Collections.singleton((ItemEffect)new BodyModEffect("drink", "throw", BreastsPart.c, BodyModEffect.Effect.growplus)), 15),
	FemDraft	( "Fem Draft", 150, "Temporarily grow a pussy", "a ", Collections.singleton((ItemEffect)new BodyModEffect("drink", "throw", PussyPart.normal, BodyModEffect.Effect.replace)), 15),
	Lactaid	( "Lactaid", 100, "Temporarily start lactating", "", Arrays.asList((ItemEffect)
			new AddTraitEffect("drink", "throw", Trait.lactating)), 15),
	SuccubusDraft	( "Succubus Draft", 300, "Temporarily turn into a succubus", "a ", Arrays.asList((ItemEffect)
			new BuffEffect("drink", "throw", new Abuff(Global.noneCharacter(), Attribute.Dark, 10, 15)),
			new BuffEffect("drink", "throw", new Abuff(Global.noneCharacter(), Attribute.Seduction, 5, 15)),
			new AddTraitEffect("drink", "throw", Trait.addictivefluids),
			new AddTraitEffect("drink", "throw", Trait.succubus),
			new BodyModEffect("drink", "throw", PussyPart.succubus, BodyModEffect.Effect.replace),
			new BodyModEffect("drink", "throw", EarPart.pointed, BodyModEffect.Effect.replace),
			new BodyModEffect("drink", "throw", WingsPart.demonic, BodyModEffect.Effect.replace),
			new BodyModEffect("drink", "throw", TailPart.demonic, BodyModEffect.Effect.replace),
			new BodyModEffect("drink", "throw", BreastsPart.dd, BodyModEffect.Effect.growplus)
			), 15),
	TentacleTonic	( "Tentacle Tonic", 250, "Temporarily grow tentacles", "a ", Arrays.asList((ItemEffect)
			new GrowTentaclesEffect("drink", "throw", 15),
			new MaybeEffect(new GrowTentaclesEffect("drink", "throw", 15), .5),
			new MaybeEffect(new GrowTentaclesEffect("drink", "throw", 15), .25),
			new MaybeEffect(new GrowTentaclesEffect("drink", "throw", 15), .1),
			new MaybeEffect(new BodyModEffect("drink", "throw", PussyPart.tentacled, BodyModEffect.Effect.replace, 15), .3)
			), 15),
	JuggernautJuice
				( "Juggernaut Juice", 250, "Makes you nigh invulnerable.", "a ", Arrays.asList((ItemEffect)
						new BuffEffect("drink", "throw", new Shield(Global.noneCharacter(), .5, 10)),
						new BuffEffect("drink", "throw", new Abuff(Global.noneCharacter(), Attribute.Power, 5, 10)),
						new BuffEffect("drink", "throw", new Abuff(Global.noneCharacter(), Attribute.Cunning, -5, 10)),
						new RemoveTraitEffect("drink", "throw", Trait.achilles)), 10),
	BewitchingDraught
				( "Bewitching Draught", 250, "Makes you inhumanly alluring.", "a ", Arrays.asList((ItemEffect)
						new BuffEffect("drink", "throw", new Alluring(Global.noneCharacter(), 10)),
						new BuffEffect("drink", "throw", new Abuff(Global.noneCharacter(), Attribute.Seduction, 5, 10)),
						new BuffEffect("drink", "throw", new Abuff(Global.noneCharacter(), Attribute.Power, -5, 10)),
						new AddTraitEffect("drink", "throw", Trait.RawSexuality),
						new AddTraitEffect("drink", "throw", Trait.augmentedPheromones)), 10),
	TinkersMix
				( "TinkersMix", 250, "Not sure if it's a good idea to drink this...", "a ", Arrays.asList((ItemEffect)
						new BuffEffect("drink", "throw", new Distorted(Global.noneCharacter(), 10)),
						new BuffEffect("drink", "throw", new Abuff(Global.noneCharacter(), Attribute.Cunning, 5, 10)),
						new BuffEffect("drink", "throw", new Abuff(Global.noneCharacter(), Attribute.Seduction, -5, 10)),
						new AddTraitEffect("drink", "throw", Trait.lacedjuices),
						new AddTraitEffect("drink", "throw", Trait.aikidoNovice)), 10);

	/**
	 * The Item's display name.
	 */
	private String desc;
	private String name;
	private String prefix;
	private int price;
	private List<ItemEffect> effect;
	int duration;

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
		this(name, price, desc, prefix, Collections.singleton(new ItemEffect()), 0);
	}

	private Item( String name, int price, String desc,String prefix, Collection<ItemEffect> effect, int duration)
	{
		this.name = name;
		this.price = price;
		this.desc = desc;
		this.prefix = prefix;
		this.effect = new ArrayList<ItemEffect>(effect);
		this.duration = duration;
	}

	public List<ItemEffect> getEffects() {
		return effect;
	}
}
