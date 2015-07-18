package characters;

import items.Clothing;
import global.Global;

public enum Trait {
	//Physical
	vaginaltongue("Vaginal Tongue", "Have a second tongue", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
			if (c.pantsless()) {
				b.append("Occasionally, a pink tongue slides out of her pussy and licks her lower lips.");
			}
		}
	}),

	//Perks
	smqueen("Tickle Monster","Skilled at providing pleasure alongside pain", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
			b.append(Global.capitalizeFirstLetter(
					String.format("%s sneers in a way like an SM queen.", c.subject())));
		}
	}),
	ticklemonster("Tickle Monster","Skilled at tickling in unconventional areas"), //Mara Sex perk, increases pleasure from tickling if target is nude
	heeldrop("Heeldrop","A wrestling move feared by men and women alike"), //Mara Sparring perk, increases damage from stomp
	spider("Spider","Elaborate rope traps come naturally"), //Mara Gaming perk, Spiderweb
	experttongue("Expert Tonguework","Can charm with oral pleasure"), //Angel Sex perk, kiss has chance to inflict Charm
	disciplinarian("Disciplinarian","Frighteningly skilled at spanking"), //Angel Sparring perk, spank has a chance to inflict Shame
	pokerface("Poker Face","Bluff like a champion"), //Angel Gaming perk, Bluff
	silvertongue("Silvertongue","Terrific tongue talent"), //Cassie Sex perk, increases pleasure from oral attacks
	judonovice("Judo Novice","Basic understanding of judo"), //Cassie Sparring perk, Hip Throw
	misdirection("Misdirection","They look left, you go right"), //Cassie Gaming perk, Diversion
	dirtyfighter("Dirty Fighter","Down, but not out"), //Jewel Sparring perk, kick can be used from prone
	spiral("Spiral","Who the hell do you think I am?"), //Jewel Sex perk, Spiral Thrust
	fearless("Fearless","Leeroy Jenkins"), //Jewel Gaming perk, Bravado
	clairvoyance("Clairvoyance",""), //Reyka Sparring perk evasion bonus
	locator("Locator","Like a bloodhound"), //Reyka Gaming perk out of combat action
	desensitized("Desensitized","Sex is old hat now"), //Reyka Sex perk slight pleasure reduction
	desensitized2("Desensitized 2","Only the strongest stimulation gets you off"), //Reyka Sex perk slight pleasure reduction
	shameless("Shameless","Impossible to embarrass"), //Eve
	RawSexuality("Raw Sexuality","Constant lust boost for you and your opponent in battle", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
			if (c.human())
				b.append("You");
			else
				b.append(c.name());
			b.append(" exudes a aura of pure eros, making both of you flush with excitement.");
		}
	}), //Eve
	affectionate("Affectionate","Increased affection gain from draws"), //Kat Sex perk
	aikidoNovice("Aikido Novice","Improved counterattack rate"), //Kat Sparring perk
	
	//Passive Skills
	exhibitionist("Exhibitionist","More effective without any clothes"), //Passively builds mojo while nude
	pheromones("Pheromones","Scent can drive people wild", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
			b.append("A primal musk surrounds ");
			if (c.human())
				b.append("your");
			else
				b.append(c.name() + "'s");
			b.append(" body.");
		}
	}), //causes horny in opponents if aroused	
	augmentedPheromones("Augmented Pheromones", "Artificially enhanced pheromones", null, pheromones),

	soulsucker("Soulsucker", "Soul sucking lips"),
	entrallingjuices("Enthralling cum", "Enthralling juices"),
	lacedjuices("Laced Juices", "Intoxicating bodily fluids"), //opponents take temptation when using oral skills
	addictivefluids("Additive Fluids", "Addictive bodily fluids"), //opponents can only use oral skills if available
	lactating("Lactating","Breasts produces milk", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
			if (!c.human()) {
				if (c.topless()) {
					b.append("You occasionally see milk dribbling down her breasts. Is she lactating?");
				} else {
					b.append("You notice a damp spot on her " + c.top.lastElement().getName() + ".");
				}
			} else {
				b.append("Your nipples ache from the milk building up in your mammaries.");
			}
		}
	}),

	darkpromises("Dark Promises","Can enthrall with the right words"), //whisper upgrade, can enthrall
	energydrain("Energy Drain", "Drains energy during intercourse"),
	spiritphage("Semenphage", "Feeds on semen"),
	tight("Tight", "Powerful musculature and exquisite tightness makes for quick orgasms."),
	holecontrol("Control", "Dexterous internal muscle control."),
	oiledass("Oiled Ass", "Natural oils makes her ass always ready."),
	autonomousAss("Autonomous Ass", "Her asshole instinctively forces anything inside of it to cum."),
	cute("Innocent Appearance", "Hard to muster the will to hurt her"),

	autonomousPussy("Autonomous Pussy", "Her pussy instinctively forces anything inside of it to cum."),
	//AI traits
	submissive("Submissive", "Enjoys being the sub."),
	
	//Weaknesses
	ticklish("Ticklish","Can be easily tickled into submission"), 	//more weaken damage and arousal from tickle
	insatiable("Insatiable","One orgasm is never enough"), //arousal doesn't completely clear at end of match
	lickable("Lickable","Weak against oral sex"), 	//more arousal from oral attacks
	imagination("Active Imagination","More easily swayed by pillow talk"),//more temptation damage from indirect skills
	achilles("Achilles Jewels","Delicate parts are somehow even more delicate"),	//more pain from groin attacks
	
	//Restrictions
	softheart("Soft Hearted","Incapable of being mean"), //restricts slap, stomp, flick
	petite("Petite","Small body, small breasts"), //restricts carry, tackle, paizuri
	undisciplined("Undisciplined","Lover, not a fighter"), //restricts manuever, focus, armbar
	direct("Direct","Patience is overrated"), //restricts whisper, dissolving trap, aphrodisiac trap, decoy, strip tease
	shy("Shy", "", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
			if (c.human())
				b.append("You shy away from your opponent's gaze.");
			else
				b.append(c.name + " quickly avoids your gaze.");
		}
	}), //restricts striptease, flick, facesit, taunt, squeeze
	
	//Class
	madscientist("Mad Scientist","May have gone overboard with her projects"),
	witch("Witch","Learned to wield traditional arcane magic"),
	succubus("Succubus","Embraced the dark powers that feed on mortal lust"),
	fighter("Fighter","A combination of martial arts and ki"),
	slime("Slime","An accident in the biology labs made the body a bit more... malleable."),

	//Strength
	dexterous("Dexterous","Underwear is no obstacle for nimble fingers"), //digital stimulation through underwear
	romantic("Romantic","Every kiss is as good as the first"), //bonus to kiss
	experienced("Experienced Lover","Skilled at pacing yourself when thrusting"), //reduced recoil from penetration
	wrassler("Wrassler","A talent for fighting dirty"), //squeeze, knee, kick reduce arousal less
	pimphand("Pimp Hand","A devastating slap"),

	//unimplemented
	Clingy("Clingy","can do the 'glomp' attack - weak standing grapple hug, probably something Cassie would take right away"),
	cautious("Cautious","Better chance of avoiding traps"),

	//Feats
	sprinter("Sprinter","Better at escaping combat"),
	QuickRecovery("Quick Recovery","Regain stamina rapidly out of combat"),
	Sneaky("Sneaky","Easier time hiding and ambushing competitors"),
	PersonalInertia("Personal Inertia","status effects (positive and negative) last 50% longer"),
	Confident("Confident","Mojo decays slower out of combat"),
	SexualGroove("Sexual Groove","Passive mojo gain every turn in combat"),
	BoundlessEnergy("Boundless Energy","Increased passive stamina gain in battle"),
	Unflappable("Unflappable","Not distracted by being fucked from behind"),
	freeSpirit("Free Spirit","Better as escaping pins"),
	resourceful("Resourceful","Chance to not consume an item on use"),
	treasureSeeker("Treasure Seeker","Improved chance of opening item caches"),
	sympathetic("Sympathetic","Intervening opponents are more likely to side with you"),
	fastLearner("Fast Learner","Improved experience gain"),
	leadership("Leadership","Summoned pets are more powerful"),
	fitnessNut("Fitness Nut","More efficient at exercising"),
	expertGoogler("Expert Googler","More efficient at finding porn"),
	mojoMaster("Mojo Master","Max Mojo increases faster"),
	powerfulhips("Powerful Hips","Can grind from submissive positions"),
	strongwilled("Strong Willed","Halves willpower loss"),
	alwaysready("Always Ready","Always ready for penetration", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
			if (!c.hasDick() && c.pantsless()) {
				b.append("Juices constainly drool from ");
				if (c.human())
					b.append("your slobbering pussy.");
				else
					b.append(c.name +"'s slobbering pussy.");
			}
		}
	}),

	//Item
	strapped("Strapped","Penis envy", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
			if (c.human())
				b.append("A large black strap-on dildo adorns your waist.");
			else
				b.append("A large black strap-on dildo adorns " + c.name() + "'s waists.");
		}
	}), //currently wearing a strapon
	ineffective("Ineffective","Provides no protection"),
	armored("Armored","Protects the delicate bits"),
	stylish("Stylish","Better mojo gain"),
	lame("Lame","Small mojo penalty"),
	skimpy("Skimpy","Better temptation daamage"),
	flexible("Flexible","Can fuck by pulling it aside"),
	indestructible("Indestructible","Cannot be destroyed"),
	bulky("Bulky","Speed penalty"),
	geeky("Geeky","Science bonus"),
	mystic("Mystic","Arcane bonus"),
	martial("Martial","Ki bonus"),
	broody("Broody","Dark bonus"),
	kinky("Kinky","Fetish bonus"),
	
	none("",""),
	;
	private String desc;
	private TraitDescription longDesc;
	private String name;
	public Trait parent;
	public String getDesc()
	{
		return desc;
	}
	public String toString(){
		return name;
	}
	private Trait(String name, String description) {
		this.name=name;
		this.desc=description;
	}
	private Trait(String name, String description, TraitDescription longDesc) {
		this.name=name;
		this.desc=description;
		this.longDesc = longDesc;
	}
	private Trait(String name, String description, TraitDescription longDesc, Trait parent) {
		this.name=name;
		this.desc=description;
		this.longDesc = longDesc;
		this.parent = parent;
	}

	public boolean isFeat(){
		return this.compareTo(sprinter)>=0 && this.compareTo(strapped)<0;
	}

	public void describe(Character c, StringBuilder b){
		if (longDesc != null) {
			longDesc.describe(b, c, this);
			b.append(" ");
		}
	}
}
