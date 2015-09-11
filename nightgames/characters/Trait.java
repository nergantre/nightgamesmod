package nightgames.characters;

import java.util.HashMap;
import java.util.Map;

import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.status.Lethargic;
import nightgames.status.Resistance;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public enum Trait {
	//Physical
	vaginaltongue("Vaginal Tongue", "Have a second tongue", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
			if (c.crotchAvailable()) {
				b.append("Occasionally, a pink tongue slides out of her pussy and licks her lower lips.");
			}
		}
	}),

	//Perks
	smqueen("SM Queen","Skilled at providing pleasure alongside pain", new TraitDescription() {
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
	
	magicEyeArousal("Magic Eyes: Arouse", "Eyes have a chance to arouse"),
	magicEyeEnthrall("Magic Eyes: Enthrall", "Eyes have a chance to enthrall"),
	magicEyeTrance("Magic Eyes: Trance", "Eyes have a chance to entrance"),
	soulsucker("Soulsucker", "Soul sucking lips"),
	entrallingjuices("Enthralling cum", "Enthralling juices"),
	lacedjuices("Laced Juices", "Intoxicating bodily fluids"), //opponents take temptation when using oral skills
	addictivefluids("Addictive Fluids", "Addictive bodily fluids"), //opponents can only use oral skills if available
	lactating("Lactating","Breasts produces milk", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
			if (!c.human()) {
				if (c.breastsAvailable()) {
					b.append("You occasionally see milk dribbling down her breasts. Is she lactating?");
				} else {
					b.append("You notice a damp spot on her " + c.getOutfit().getTopOfSlot(ClothingSlot.top).getName() + ".");
				}
			} else {
				b.append("Your nipples ache from the milk building up in your mammaries.");
			}
		}
	}),

	polecontrol("Pole Control","Always hit the right spots"), //Dick damage upgrade
	hypnoticsemen("Hypnotic Semen","Cum drains willpower"), //Semen willpower damage trait
	testosterone("Testosterone","More powerful muscles"), //Having a cock gives + to power
	pussyhandler("Pussy Handler","Expert at pleasing the pussy"), //Bonus damage to pussies
	dickhandler("Dick Handler","Expert at pleasing cocks"), //Bonus damage to cocks
	
	darkpromises("Dark Promises","Can enthrall with the right words"), //whisper upgrade, can enthrall
	energydrain("Energy Drain", "Drains energy during intercourse"),
	objectOfWorship("Object Of Worship", "Opponents is periodically forced to worship your body.", new TraitDescription() {
		public void describe(StringBuilder b, Character c, Trait t) {
				b.append("A divine aura surrounds " + c.nameDirectObject() + ".");
		}
	}),
	spiritphage("Semenphage", "Feeds on semen"),
	erophage("Erophage", "Feeds on sexuality"),
	tight("Tight", "Powerful musculature and exquisite tightness makes for quick orgasms."),
	holecontrol("Pussy Control", "Dexterous internal muscle control."),
	oiledass("Oiled Ass", "Natural oils makes her ass always ready."),
	autonomousAss("Autonomous Ass", "Asshole instinctively forces anything inside of it to cum."),
	fetishTrainer("Fetish Trainer", "Capable of developing other's fetishes."),
	insertion("Insertion Master","More pleasure on insertion"), //more damage on insertion.
	proheels("Heels Pro", "Pro at walking around in heels"), //no speed penalty from heels
	masterheels("Heels Master", "Master at moving in heels, resists knockdowns"), //graceful when wearing heels

	//training perks
	analTraining1("Anal Training 1", "Refined ass control."),
	analTraining2("Anal Training 2", "Perfected ass control."),
	analTraining3("Anal Training 3", "Godly ass control."),
	pussyTraining1("Pussy Training 1", "Refined vaginal control."),
	pussyTraining2("Pussy Training 2", "Perfected vaginal control."),
	pussyTraining3("Pussy Training 3", "Godly vaginal control."),
	cockTraining1("Cock Training 1", "Refined cock control."),
	cockTraining2("Cock Training 2", "Perfected cock control."),
	cockTraining3("Cock Training 3", "Godly cock control."),	
	tongueTraining1("Tongue Training 1", "Refined tongue control."),
	tongueTraining2("Tongue Training 2", "Perfected tongue control."),
	tongueTraining3("Tongue Training 3", "Godly tongue control."),
	limbTraining1("Hands&Feet Training 1", "Refined hands and feet control."),
	limbTraining2("Hands&Feet Training 2", "Perfected hands and feet control."),
	limbTraining3("Hands&Feet Training 3", "Godly hands and feet control."),

	//resistances
	freeSpirit("Free Spirit","Better at escaping pins and resisting mind control"),
	calm("Calm","Chance at resisting arousal over time"),
	skeptical("Skeptical","Chance at resisting mental statuses"),
	graceful("Graceful","Chance at resisting knockdowns"),
	steady("Steady","Cannot be knocked down"),
	shameless("Shameless","Impossible to embarrass"), //Eve

	cute("Innocent Appearance", "Reduction to opponent's power"),

	autonomousPussy("Autonomous Pussy", "Her pussy instinctively forces anything inside of it to cum."),
	//AI traits
	submissive("Submissive", "Enjoys being the sub."),
	
	//Weaknesses
	ticklish("Ticklish","Can be easily tickled into submission"), 	//more weaken damage and arousal from tickle
	insatiable("Insatiable","One orgasm is never enough"), //arousal doesn't completely clear at end of match
	lickable("Lickable","Weak against oral sex"), 	//more arousal from oral attacks
	imagination("Active Imagination","More easily swayed by pillow talk"),//more temptation damage from indirect skills
	achilles("Achilles Jewels","Delicate parts are somehow even more delicate"),	//more pain from groin attacks
	naive("Naive", "Chance to not get cynical after mindgames"), //Chance to not get cynical after recovering from mindgames
	footfetishist("Foot Fetishist", "Loves those feet"), //Starts off each match with a foot fetish
	immobile("Immobile", "Unable to move"), //Cannot move
	lethargic("Lethargic", "Very low mojo gain from normal methods.", new Lethargic(null)), //25% mojo gain

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
	demigoddess("Demigoddess","Blessed by a deity of sexual pleasure, and on the road to ascension herself."),
	fighter("Fighter","A combination of martial arts and ki"),
	slime("Slime","An accident in the biology labs made the body a bit more... malleable."),
	dryad("Dryad","Part girl, part tree."),
	temptress("Temptress", "Well versed in the carnal arts."),
	
	//Class subtrait
	divinity("Divinity","Has aspects of divinity."),

	//Strength
	dexterous("Dexterous","Limbs and fingers. Underwear is not an obstacle."), //digital stimulation through underwear
	romantic("Romantic","Every kiss is as good as the first"), //bonus to kiss
	experienced("Experienced Lover","Skilled at pacing yourself when thrusting"), //reduced recoil from penetration
	wrassler("Wrassler","A talent for fighting dirty"), //squeeze, knee, kick reduce arousal less
	pimphand("Pimp Hand","A devastating slap and a bonus to hands"),
	stableform("Stable Form","Cannot be affected by unexpected transformations"), //ignores thrown transformative drafts from the opponent

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
			if (!c.hasDick() && c.crotchAvailable()) {
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
	none("",""),
	;
	private String desc;
	private TraitDescription longDesc;
	private String name;
	public Trait parent;
	public Status status;

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

	private Trait(String name, String description, Status status) {
		this.name=name;
		this.desc=description;
		this.status = status;
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
	public static Map<Trait, Resistance> resistances;
	public static Resistance nullResistance;
	static {
		nullResistance = (c, s) -> "";
		resistances = new HashMap<>();
		resistances.put(shameless, (c, s) -> {
			if (s.flags().contains(Stsflag.shamed) || s.flags().contains(Stsflag.distracted)) {
				return "Shameless";
			}
			return "";
		});
		resistances.put(Trait.freeSpirit, (c, s) -> {
			// 30% to resist enthrall and bound
			if ((s.flags().contains(Stsflag.enthralled) || s.flags().contains(Stsflag.bound)) && Global.random(100) < 30) {
				return "Free Spirit";
			}
			return "";
		});
		resistances.put(Trait.calm, (c, s) -> {
			// 50% to resist horny and hypersensitive
			if ((s.flags().contains(Stsflag.horny) || s.flags().contains(Stsflag.hypersensitive)) && Global.random(100) < 50) {
				return "Calm";
			}
			return "";
		});
		resistances.put(Trait.skeptical, (c, s) -> {
			// 30% to resist mindgames
			if (s.mindgames() && Global.random(100) < 30) {
				return "Skeptical";
			}
			return "";
		});
		resistances.put(Trait.masterheels, (c, s) -> {
			// 33% to resist falling wearing heels
			if (c.has(ClothingTrait.heels) && s.flags().contains(Stsflag.falling) && Global.random(100) < 33) {
				return "Heels Master";
			}
			return "";
		});
		resistances.put(Trait.graceful, (c, s) -> {
			// 25% to resist falling
			if (s.flags().contains(Stsflag.falling) && Global.random(100) < 25) {
				return "Graceful";
			}
			return "";
		});
		resistances.put(Trait.steady, (c, s) -> {
			// 100% to resist falling
			if (s.flags().contains(Stsflag.falling)) {
				return "Steady";
			}
			return "";
		});
		resistances.put(Trait.naive, (c, s) -> {
			// 50% to resist Cynical
			if (s.flags().contains(Stsflag.cynical) && Global.random(100) < 50) {
				return "Naive";
			}
			return "";
		});
	}
	public static Resistance getResistance(Trait t) {
		if (resistances.containsKey(t)) {
			return resistances.get(t);
		} else {
			return nullResistance;
		}
	}
}
