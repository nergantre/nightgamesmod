package nightgames.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

import com.cedarsoftware.util.io.JsonWriter;

import nightgames.Resources.ResourceLoader;
import nightgames.actions.Action;
import nightgames.actions.Bathe;
import nightgames.actions.Craft;
import nightgames.actions.Energize;
import nightgames.actions.Hide;
import nightgames.actions.Locate;
import nightgames.actions.MasturbateAction;
import nightgames.actions.Movement;
import nightgames.actions.Recharge;
import nightgames.actions.Resupply;
import nightgames.actions.Scavenge;
import nightgames.actions.SetTrap;
import nightgames.actions.Use;
import nightgames.actions.Wait;
import nightgames.areas.Area;
import nightgames.characters.Airi;
import nightgames.characters.Angel;
import nightgames.characters.Attribute;
import nightgames.characters.Cassie;
import nightgames.characters.Character;
import nightgames.characters.Jewel;
import nightgames.characters.Kat;
import nightgames.characters.Mara;
import nightgames.characters.NPC;
import nightgames.characters.Personality;
import nightgames.characters.Player;
import nightgames.characters.Reyka;
import nightgames.characters.Trait;
import nightgames.characters.TraitTree;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.StraponPart;
import nightgames.characters.custom.CustomNPC;
import nightgames.characters.custom.JSONSourceNPCDataLoader;
import nightgames.daytime.Daytime;
import nightgames.gui.GUI;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.pet.Ptype;
import nightgames.skills.*;
import nightgames.stance.FlowerSex;
import nightgames.status.BodyFetish;
import nightgames.trap.Alarm;
import nightgames.trap.AphrodisiacTrap;
import nightgames.trap.Decoy;
import nightgames.trap.DissolvingTrap;
import nightgames.trap.EnthrallingTrap;
import nightgames.trap.IllusionTrap;
import nightgames.trap.Snare;
import nightgames.trap.Spiderweb;
import nightgames.trap.SpringTrap;
import nightgames.trap.StripMine;
import nightgames.trap.TentacleTrap;
import nightgames.trap.Trap;
import nightgames.trap.Tripline;

public class Global {
	private static Random rng;
	private static GUI gui;
	private static HashSet<Skill> skillPool = new HashSet<Skill>();

	private static HashSet<Action> actionPool;
	private static HashSet<Trap> trapPool;
	private static HashSet<Trait> featPool;
	private static HashSet<Character> players;
	private static HashSet<Character> resting;
	private static HashSet<String> flags;
	private static HashMap<String,Float> counters;
	private static Player human;
	private static Match match;
	private static Daytime day;
	private static int date;
	private Date jdate;
	private static TraitTree traitRequirements;
	public static Scene current;
	public static boolean debug[] = new boolean[DebugFlags.values().length];
	public static boolean debugSimulation = false;
	public static double moneyRate = 1.0;
	public static double xpRate = 1.0;
	public static ContextFactory factory;
	public static Context cx;

	public Global(){
		rng=new Random();
		flags = new HashSet<String>();
		players = new HashSet<Character>();
		resting = new HashSet<Character>();
		counters = new HashMap<String,Float>();
		jdate = new Date();
		counters.put(Flag.malePref.name(), 0.f);
		Clothing.buildClothingTable();
		PrintStream fstream;
		try {
			File logfile = new File("nightgames_log.txt");
			//append the log if it's less than 2 megs in size.
			fstream = new PrintStream(new FileOutputStream(logfile, logfile.length() < (2L * 1024L * 1024L)));
			OutputStream estream = new TeeStream(System.err, fstream);
			OutputStream ostream = new TeeStream(System.out, fstream);
			System.setErr(new PrintStream(estream));
			System.setOut(new PrintStream(ostream));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("=============================================");
		System.out.println("Night games");
		System.out.println(new Timestamp(jdate.getTime()));

		debug[DebugFlags.DEBUG_SCENE.ordinal()] = true;
		debug[DebugFlags.DEBUG_LOADING.ordinal()] = true;
//		debug[DebugFlags.DEBUG_DAMAGE.ordinal()] = true;
//		debug[DebugFlags.DEBUG_SKILLS.ordinal()] = true;
//		debug[DebugFlags.DEBUG_SKILLS_RATING.ordinal()] = true;
//		debug[DebugFlags.DEBUG_PLANNING.ordinal()] = true;
//		debug[DebugFlags.DEBUG_SKILL_CHOICES.ordinal()] = true;
		traitRequirements = new TraitTree(ResourceLoader.getFileResourceAsStream("data/TraitRequirements.xml"));
		current=null;
		factory = new ContextFactory();
		cx = factory.enterContext();
		gui=new GUI();
		buildActionPool();
		buildFeatPool();
		buildParser();
	}

	public static boolean meetsRequirements(Character c, Trait t)
	{
		return traitRequirements.meetsRequirements(c, t);
	}
	
	public static boolean isDebugOn(DebugFlags flag) {
		return debug[flag.ordinal()] && !debugSimulation;
	}

	public static void newGame(Player one){
		human = one;
		players.add(human);
		gui.populatePlayer(human);
		buildSkillPool(human);
		Clothing.buildClothingTable();
		Global.learnSkills(human);
		rebuildCharacterPool();
		date=0;
		flag(Flag.systemMessages);
		players.add(getNPC("Jewel"));
		players.add(getNPC("Cassie"));
		players.add(getNPC("Angel"));
		players.add(getNPC("Mara"));
		match = new Match(players,Modifier.normal);
		match.round();
	}
	
	public static int random(int d){
		if (d <= 0) { return 0; }
		return rng.nextInt(d);
	}

	// finds a centered random number from [0, d] (inclusive)
	public static int centeredrandom(int d, double center, double sigma){
		int val = 0; 
		center = Math.max(0, Math.min(d, center));
		for (int i = 0; i < 10; i++) {
			double f = rng.nextGaussian() * sigma + center;
			val = (int)Math.round(f);
			if (val >= 0 && val <= d) {
				return val;
			}
		}
		return Math.max(0, Math.min(d, val));
	}

	public static GUI gui(){
		return gui;
	}

	public static Player getPlayer(){
		return human;
	}

	public static void buildSkillPool(Player p){
		skillPool.clear();
		skillPool.add(new Slap(p));
		skillPool.add(new ArmBar(p));
		skillPool.add(new Blowjob(p));
		skillPool.add(new Cunnilingus(p));
		skillPool.add(new Escape(p));
		skillPool.add(new Flick(p));
		skillPool.add(new LivingClothing(p));
		skillPool.add(new LivingClothingOther(p));
		skillPool.add(new Engulf(p));
		skillPool.add(new CounterFlower(p));
		skillPool.add(new Knee(p));
		skillPool.add(new LegLock(p));
		skillPool.add(new LickNipples(p));
		skillPool.add(new Maneuver(p));
		skillPool.add(new Paizuri(p));
		skillPool.add(new PerfectTouch(p));
		skillPool.add(new Restrain(p));
		skillPool.add(new Reversal(p));
		skillPool.add(new LeechEnergy(p));
		skillPool.add(new SweetScent(p));
		skillPool.add(new Spank(p));
		skillPool.add(new Stomp(p));
		skillPool.add(new StandUp(p));
		skillPool.add(new WildThrust(p));
		skillPool.add(new SuckNeck(p));
		skillPool.add(new Tackle(p));
		skillPool.add(new Taunt(p));
		skillPool.add(new Trip(p));
		skillPool.add(new Whisper(p));
		skillPool.add(new Kick(p));
		skillPool.add(new PinAndBlow(p));
		skillPool.add(new Footjob(p));
		skillPool.add(new FootPump(p));
		skillPool.add(new HeelGrind(p));
		skillPool.add(new Handjob(p));
		skillPool.add(new Squeeze(p));
		skillPool.add(new Nurple(p));
		skillPool.add(new Finger(p));
		skillPool.add(new Aphrodisiac(p));
		skillPool.add(new Lubricate(p));
		skillPool.add(new Dissolve(p));
		skillPool.add(new Sedate(p));
		skillPool.add(new Tie(p));
		skillPool.add(new Masturbate(p));
		skillPool.add(new Piston(p));
		skillPool.add(new Grind(p));
		skillPool.add(new Thrust(p));
		skillPool.add(new UseDildo(p));
		skillPool.add(new UseOnahole(p));
		skillPool.add(new UseCrop(p));
		skillPool.add(new Carry(p));
		skillPool.add(new Tighten(p));
		skillPool.add(new HipThrow(p));
		skillPool.add(new SpiralThrust(p));
		skillPool.add(new Bravado(p));
		skillPool.add(new Diversion(p));
		skillPool.add(new Undress(p));
		skillPool.add(new StripSelf(p));
		skillPool.add(new StripTease(p));
		skillPool.add(new Sensitize(p));
		skillPool.add(new EnergyDrink(p));
		skillPool.add(new Strapon(p));
		skillPool.add(new AssFuck(p));
		skillPool.add(new Turnover(p));
		skillPool.add(new Tear(p));
		skillPool.add(new Binding(p));
		skillPool.add(new Bondage(p));
		skillPool.add(new WaterForm(p));
		skillPool.add(new DarkTendrils(p));
		skillPool.add(new Dominate(p));
		skillPool.add(new FlashStep(p));
		skillPool.add(new FlyCatcher(p));
		skillPool.add(new Illusions(p));
		skillPool.add(new LustAura(p));
		skillPool.add(new MagicMissile(p));
		skillPool.add(new Masochism(p));
		skillPool.add(new NakedBloom(p));
		skillPool.add(new ShrinkRay(p));
		skillPool.add(new SpawnFaerie(p,Ptype.fairyfem));
		skillPool.add(new SpawnImp(p,Ptype.impfem));
		skillPool.add(new SpawnFaerie(p,Ptype.fairymale));
		skillPool.add(new SpawnImp(p,Ptype.impmale));
		skillPool.add(new SpawnSlime(p));
		skillPool.add(new StunBlast(p));
		skillPool.add(new Fly(p));
		skillPool.add(new Command(p));
		skillPool.add(new Obey(p));
		skillPool.add(new OrgasmSeal(p));
		skillPool.add(new DenyOrgasm(p));
		skillPool.add(new Drain(p));
		skillPool.add(new LevelDrain(p));
		skillPool.add(new StoneForm(p));
		skillPool.add(new FireForm(p));
		skillPool.add(new Defabricator(p));
		skillPool.add(new TentaclePorn(p));
		skillPool.add(new Sacrifice(p));
		skillPool.add(new Frottage(p));
		skillPool.add(new FaceFuck(p));
		skillPool.add(new VibroTease(p));
		skillPool.add(new TailPeg(p));
		skillPool.add(new CommandDismiss(p));
		skillPool.add(new CommandDown(p));
		skillPool.add(new CommandGive(p));
		skillPool.add(new CommandHurt(p));
		skillPool.add(new CommandInsult(p));
		skillPool.add(new CommandMasturbate(p));
		skillPool.add(new CommandOral(p));
		skillPool.add(new CommandStrip(p));
		skillPool.add(new CommandStripPlayer(p));
		skillPool.add(new CommandUse(p));
		skillPool.add(new ShortCircuit(p));
		skillPool.add(new IceForm(p));
		skillPool.add(new Barrier(p));
		skillPool.add(new CatsGrace(p));
		skillPool.add(new Charm(p));
		skillPool.add(new Tempt(p));
		skillPool.add(new EyesOfTemptation(p));
		skillPool.add(new TailJob(p));
		skillPool.add(new FaceSit(p));
		skillPool.add(new Purr(p));
		skillPool.add(new MutualUndress(p));
		skillPool.add(new Submit(p));
		skillPool.add(new Surrender(p));
		skillPool.add(new ReverseFuck(p));
		skillPool.add(new ReverseCarry(p));
		skillPool.add(new ReverseFly(p));
		skillPool.add(new CounterDrain(p));
		skillPool.add(new CounterRide(p));
		skillPool.add(new CounterPin(p));
		skillPool.add(new ReverseAssFuck(p));
		skillPool.add(new Nurse(p));
		skillPool.add(new Suckle(p));
		skillPool.add(new UseDraft(p));
		skillPool.add(new ThrowDraft(p));
		skillPool.add(new ReverseAssFuck(p));
		skillPool.add(new FondleBreasts(p));
		skillPool.add(new Fuck(p));
		skillPool.add(new Kiss(p));
		skillPool.add(new Struggle(p));
		skillPool.add(new Tickle(p));
		skillPool.add(new nightgames.skills.Wait(p));
		skillPool.add(new StripTop(p));
		skillPool.add(new StripBottom(p));
		skillPool.add(new Shove(p));
		skillPool.add(new Recover(p));
		skillPool.add(new Straddle(p));
		skillPool.add(new ReverseStraddle(p));
		skillPool.add(new Stunned(p));
		skillPool.add(new Distracted(p));
		skillPool.add(new PullOut(p));
		skillPool.add(new ThrowDraft(p));
		skillPool.add(new UseDraft(p));
		skillPool.add(new TentacleRape(p));
		skillPool.add(new Anilingus(p));
		skillPool.add(new UseSemen(p));
		skillPool.add(new Invitation(p));
		skillPool.add(new SubmissiveHold(p));
		skillPool.add(new BreastGrowth(p));
		skillPool.add(new CockGrowth(p));
		skillPool.add(new BreastRay(p));
		skillPool.add(new FootSmother(p));
		skillPool.add(new FootWorship(p));
		skillPool.add(new BreastWorship(p));
		skillPool.add(new CockWorship(p));
		skillPool.add(new PussyWorship(p));
		if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
			skillPool.add(new SelfStun(p));	
		}
	}

	public static void buildActionPool(){
		actionPool=new HashSet<Action>();
		actionPool.add(new Resupply());
		actionPool.add(new Wait());
		actionPool.add(new Hide());
		actionPool.add(new Bathe());
		actionPool.add(new Scavenge());
		actionPool.add(new Craft());
		actionPool.add(new Use(Item.Lubricant));
		actionPool.add(new Use(Item.EnergyDrink));
		actionPool.add(new Use(Item.Beer));
		actionPool.add(new Recharge());
		actionPool.add(new Locate());
		actionPool.add(new MasturbateAction());
		actionPool.add(new Energize());
		buildTrapPool();
		for(Trap t:trapPool){
			actionPool.add(new SetTrap(t));
		}
	}

	public static void buildTrapPool(){
		trapPool=new HashSet<Trap>();
		trapPool.add(new Alarm());
		trapPool.add(new Tripline());
		trapPool.add(new Snare());
		trapPool.add(new SpringTrap());
		trapPool.add(new AphrodisiacTrap());
		trapPool.add(new DissolvingTrap());
		trapPool.add(new Decoy());
		trapPool.add(new Spiderweb());
		trapPool.add(new EnthrallingTrap());
		trapPool.add(new IllusionTrap());
		trapPool.add(new StripMine());
		trapPool.add(new TentacleTrap());
	}
	public static void buildFeatPool(){
		featPool = new HashSet<Trait>();
		for (Trait trait : Trait.values()) {
			if (trait.isFeat()) {
				featPool.add(trait);
			}
		}
	}

	public static HashSet<Action> getActions(){
		return actionPool;
	}
	public static List<Trait> getFeats(Character c) {
		List<Trait> a = traitRequirements.availTraits(c);
		a.sort((first,second) -> first.toString().compareTo(second.toString()));
		return a;
	}
	public static Character lookup(String name){
		for(Character player:players){
			if(player.name().equalsIgnoreCase(name)){
				return player;
			}
		}
		return null;
	}

	public static Match getMatch(){
		return match;
	}

	public static Daytime getDay(){
		return day;
	}

	public static void dawn(){
		match=null;
		double level = 0;
		for(Character player: players){
			player.getStamina().fill();
			player.getArousal().empty();
			player.getMojo().empty();
			player.change(Modifier.normal);
			level += player.getLevel();
		}
		level /= players.size();
		for(Character rested: resting){
			rested.gainXP(100+Math.max(0, (int)Math.round(10*(level-rested.getLevel()))));
		}
		date++;
		day=new Daytime(human);
	}

	public static void dusk(Modifier matchmod){
		HashSet<Character> lineup = new HashSet<Character>();
		Character lover=null;
		int maxaffection=0;
		day=null;
		for(Character player: players){
			player.getStamina().fill();
			player.getArousal().empty();
			player.getMojo().empty();
			player.getWillpower().fill();
			if(player.getPure(Attribute.Science)>0){
				player.chargeBattery();
			}
			if(human.getAffection(player)>maxaffection){
				maxaffection=human.getAffection(player);
				lover=player;
			}
			if (player.has(Trait.footfetishist)) {
				player.add(new BodyFetish(player, null, "feet", .25));
			}
		}
//		if (true) {
//			lineup.add(human);
//			for (Character c : players) {
//				if (c.name().equals("Kat")) {
//					lineup.add(c);
//				}
//			}
//			match=new Match(lineup,matchmod);
//		} else
		List<Character> participants = new ArrayList<Character>();
		for (Character c : players) {
			Flag disabledFlag = null;
			try {
				disabledFlag = Flag.valueOf(c.getName() + "Disabled");
			}
			catch (IllegalArgumentException e) {}
			if (disabledFlag == null || !Global.checkFlag(disabledFlag)) {
				participants.add(c);
			}
		}
		if(participants.size()>5){
			ArrayList<Character> randomizer = new ArrayList<Character>();
			if(lover!=null){
				lineup.add(lover);
			}
			lineup.add(human);
			randomizer.addAll(participants);
			Collections.shuffle(randomizer);
			for(Character player: randomizer){
				if(!lineup.contains(player)&&!player.human()&&lineup.size()<5){
					lineup.add(player);
				}
				else if(lineup.size()>=5){
					resting.add(player);
				}
			}
			match=new Match(lineup,matchmod);
		} else {
			match=new Match(participants,matchmod);
		}
		match.round();
	}

	public static String gainSkills(Character c){
		String message = "";
		if(c.getPure(Attribute.Dark)>=6&&!c.has(Trait.darkpromises)){
			c.add(Trait.darkpromises);
		} else if (!(c.getPure(Attribute.Dark)>=6)&&c.has(Trait.darkpromises)){
			c.remove(Trait.darkpromises);
		}
		boolean pheromonesRequirements = (c.getPure(Attribute.Animism)>=2||c.has(Trait.augmentedPheromones));
		if(pheromonesRequirements&&!c.has(Trait.pheromones)) {
			c.add(Trait.pheromones);
		} else if(!pheromonesRequirements&&c.has(Trait.pheromones)){
			c.remove(Trait.pheromones);
		}
		return message;
	}

	public static void learnSkills(Character c) {
		for(Skill skill:skillPool){
			c.learn(skill);
		}
	}
	
	public static String capitalizeFirstLetter(String original){
		if(original == null)
			return "";
	    if(original.length() == 0)
	        return original;
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}

	private static Map<String, NPC> characterPool;
	public static NPC getNPCByType (String type) {
		NPC results = characterPool.get(type);
		if (results == null) {
			System.err.println("failed to find NPC for type " + type);
		}
		return results;
	}
	public static Character getCharacterByType (String type) {
		if (type.equals(human.getType())) {
			return human;
		}
		return getNPCByType(type);
	}
	public static HashMap<String,Area> buildMap(){
		Area quad = new Area("Quad","You are in the <b>Quad</b> that sits in the center of the Dorm, the Dining Hall, the Engineering Building, and the Liberal Arts Building. There's " +
				"no one around at this time of night, but the Quad is well-lit and has no real cover. You can probably be spotted from any of the surrounding buildings, it may " +
				"not be a good idea to hang out here for long.",Movement.quad);
		Area dorm = new Area("Dorm","You are in the <b>Dorm</b>. Everything is quieter than it would be in any other dorm this time of night. You've been told the entire first floor " +
				"is empty during match hours, but you wouldn't be surprised if a few of the residents are hiding in their rooms, peeking at the fights. You've stashed some clothes " +
				"in one of the rooms you're sure is empty, which is common practice for most of the competitors.",Movement.dorm);
		Area shower = new Area("Showers","You are in the first floor <b>Showers</b>. There are a half-dozen stalls shared by the residents on this floor. They aren't very big, but there's " +
				"room to hide if need be. A hot shower would help you recover after a tough fight, but you'd be vulnerable if someone finds you.",Movement.shower);
		Area laundry = new Area("Laundry Room","You are in the <b>Laundry Room</b> in the basement of the Dorm. Late night is prime laundry time in your dorm, but none of these machines " +
				"are running. You're a bit jealous when you notice that the machines here are free, while yours are coin-op. There's a tunnel here that connects to the basement of the " +
				"Dining Hall.",Movement.laundry);
		Area engineering = new Area("Engineering Building","You are in the Science and <b>Engineering Building</b>. Most of the lecture rooms are in other buildings; this one is mostly " +
				"for specialized rooms and labs. The first floor contains workshops mostly used by the Mechanical and Electical Engineering classes. The second floor has " +
				"the Biology and Chemistry Labs. There's a third floor, but that's considered out of bounds.",Movement.engineering);
		Area lab = new Area("Chemistry Lab","You are in the <b>Chemistry Lab</b>. The shelves and cabinets are full of all manner of dangerous and/or interesting chemicals. A clever enough " +
				"person could combine some of the safer ones into something useful. Just outside the lab is a bridge connecting to the library.",Movement.lab);
		Area workshop = new Area("Workshop","You are in the Mechanical Engineering <b>Workshop</b>. There are shelves of various mechanical components and the back table is covered " +
				"with half-finished projects. A few dozen Mechanical Engineering students use this workshop each week, but it's well stocked enough that no one would miss " +
				"some materials that might be of use to you.",Movement.workshop);
		Area libarts = new Area("Liberal Arts Building","You are in the <b>Liberal Arts Building</b>. There are three floors of lecture halls and traditional classrooms, but only " +
				"the first floor is in bounds. The Library is located directly out back, and the side door is just a short walk from the pool.",Movement.la);
		Area pool = new Area("Pool","You are by the indoor <b>Pool</b>, which is connected to the Student Union for reasons that no one has ever really explained. There pool is quite " +
				"large and there is even a jaccuzi. A quick soak would feel good, but the lack of privacy is a concern. The side doors are locked at this time of night, but the " +
				"door to the Student Union is open and there's a back door that exits near the Liberal Arts building.",Movement.pool);
		Area library = new Area("Library","You are in the <b>Library</b>. It's a two floor building with an open staircase connecting the first and second floors. The front entrance leads to " +
				"the Liberal Arts building. The second floor has a Bridge connecting to the Chemistry Lab in the Science and Engineering building.",Movement.library);
		Area dining = new Area("Dining Hall","You are in the <b>Dining Hall</b>. Most students get their meals here, though some feel it's worth the extra money to eat out. The " +
				"dining hall is quite large and your steps echo on the linoleum, but you could probably find someplace to hide if you need to.",Movement.dining);
		Area kitchen = new Area("Kitchen","You are in the <b>Kitchen</b> where student meals are prepared each day. The industrial fridge and surrounding cabinets are full of the " +
				"ingredients for any sort of bland cafeteria food you can imagine. Fortunately, you aren't very hungry. There's a chance you might be able to cook up some " +
				"of the more obscure items into something useful.",Movement.kitchen);
		Area storage = new Area("Storage Room","You are in a <b>Storage Room</b> under the Dining Hall. It's always unlocked and receives a fair bit of foot traffic from students " +
				"using the tunnel to and from the Dorm, so no one keeps anything important in here. There's enough junk down here to provide some hiding places and there's a chance " +
				"you could find something useable in one of these boxes.",Movement.storage);
		Area tunnel = new Area("Tunnel","You are in the <b>Tunnel</b> connecting the dorm to the dining hall. It doesn't get a lot of use during the day and most of the freshmen " +
				"aren't even aware of its existance, but many upperclassmen have been thankful for it on cold winter days and it's proven to be a major tactical asset. The " +
				"tunnel is well-lit and doesn't offer any hiding places.",Movement.tunnel);
		Area bridge = new Area("Bridge","You are on the <b>Bridge</b> connecting the second floors of the Science and Engineering Building and the Library. It's essentially just a " +
				"corridor, so there's no place for anyone to hide.",Movement.bridge);
		Area sau = new Area("Student Union","You are in the <b>Student Union</b>, which doubles as base of operations during match hours. You and the other competitors can pick up " +
				"a change of clothing here.",Movement.union);
		Area courtyard = new Area("Courtyard","You are in the <b>Courtyard</b>. "
				+ "It's a small opening between four buildings. There's not much to see here except a tidy garden maintained by the botany department.",Movement.courtyard);
		quad.link(dorm);
		quad.link(engineering);
		quad.link(libarts);
		quad.link(dining);
		quad.link(sau);
		dorm.link(shower);
		dorm.link(laundry);
		dorm.link(quad);
		shower.link(dorm);
		laundry.link(dorm);
		laundry.link(tunnel);
		engineering.link(quad);
		engineering.link(lab);
		engineering.link(workshop);
		workshop.link(engineering);
		lab.link(engineering);
		lab.link(bridge);
		libarts.link(quad);
		libarts.link(library);
		libarts.link(pool);
		pool.link(libarts);
		pool.link(sau);
		pool.link(courtyard);
		courtyard.link(pool);
		library.link(libarts);
		library.link(bridge);
		dining.link(quad);
		dining.link(storage);
		dining.link(kitchen);
		kitchen.link(dining);
		storage.link(dining);
		storage.link(tunnel);
		tunnel.link(storage);
		tunnel.link(laundry);
		bridge.link(lab);
		bridge.link(library);
		sau.link(pool);
		sau.link(quad);
		HashMap<String,Area> map = new HashMap<String, Area>();
		map.put("Quad",quad);
		map.put("Dorm",dorm);
		map.put("Shower",shower);
		map.put("Laundry", laundry);
		map.put("Engineering", engineering);
		map.put("Workshop",workshop);
		map.put("Lab",lab);
		map.put("Liberal Arts",libarts);
		map.put("Pool",pool);
		map.put("Library",library);
		map.put("Dining",dining);
		map.put("Kitchen",kitchen);
		map.put("Storage",storage);
		map.put("Tunnel",tunnel);
		map.put("Bridge",bridge);
		map.put("Union", sau);
		map.put("Courtyard", courtyard);
		return map;
	}

	public static void flag(String f){
		flags.add(f);
	}

	public static void unflag(String f){
		flags.remove(f);
	}

	public static void flag(Flag f){
		flags.add(f.name());
	}

	public static void unflag(Flag f){
		flags.remove(f.name());
	}

	public static boolean checkFlag(Flag f){
		return flags.contains(f.name());
	}

	public static float getValue(Flag f){
		if(!counters.containsKey(f.name())){
			return 0;
		}
		else{
			return counters.get(f.name());
		}
	}
	public static void modCounter(Flag f, float inc){
		counters.put(f.name(), getValue(f)+inc);
	}
	public static void setCounter(Flag f, float val){
		counters.put(f.name(),val);
	}
	
	@SuppressWarnings("unchecked")
	public static void save(boolean auto){
		JSONObject obj = new JSONObject();
		JSONArray characterArr = new JSONArray();
		for(Character c: players) {
			characterArr.add(c.save());
		}
		obj.put("characters", characterArr);
		JSONArray flagsArr = new JSONArray();
		for(String flag : flags) {
			flagsArr.add(flag);
		}
		obj.put("flags", flagsArr);
		JSONObject countersObj = new JSONObject();
		for(String counter: counters.keySet()){
			countersObj.put(counter, counters.get(counter));
		}
		obj.put("counters", countersObj);
		obj.put("time", match == null ? "dusk" : "dawn");
		obj.put("date", date);

		try {
			FileWriter file;
			if(auto){
				file = new FileWriter("./auto.sav");
			}
			else{
				JFileChooser dialog = new JFileChooser("./");            
	            dialog.setMultiSelectionEnabled(false);
	            int rv = dialog.showSaveDialog(gui);
	            
	            if (rv != JFileChooser.APPROVE_OPTION)
	            {
	            	return;
	            }
				file = new FileWriter(dialog.getSelectedFile());
			}
			PrintWriter saver = new PrintWriter(file);
			saver.write(JsonWriter.formatJson(obj.toJSONString()));
			saver.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void rebuildCharacterPool() {
		characterPool = new HashMap<>();

		JSONArray characterSet = (JSONArray) JSONValue.parse(new InputStreamReader(ResourceLoader.getFileResourceAsStream("characters/included.json")));
		for (Object obj : characterSet) {
			String name = (String) obj;
			try {
				Personality npc = new CustomNPC(JSONSourceNPCDataLoader.load(ResourceLoader.getFileResourceAsStream("characters/" + name)));
				characterPool.put(npc.getCharacter().getType(), npc.getCharacter());
			} catch (ParseException | IOException e1) {
				System.err.println("Failed to load NPC");
				e1.printStackTrace();
			}
		}

		Personality cassie = new Cassie();
		Personality angel = new Angel();
		Personality reyka = new Reyka();
		Personality kat = new Kat();
		Personality mara = new Mara();
		Personality jewel = new Jewel();
		Personality airi = new Airi();
		characterPool.put(cassie.getCharacter().getType(), cassie.getCharacter());
		characterPool.put(angel.getCharacter().getType(), angel.getCharacter());
		characterPool.put(reyka.getCharacter().getType(), reyka.getCharacter());
		characterPool.put(kat.getCharacter().getType(), kat.getCharacter());
		characterPool.put(mara.getCharacter().getType(), mara.getCharacter());
		characterPool.put(jewel.getCharacter().getType(), jewel.getCharacter());
		characterPool.put(airi.getCharacter().getType(), airi.getCharacter());
	}
	
	public static void load(){
		JFileChooser dialog = new JFileChooser("./");
        dialog.setMultiSelectionEnabled(false);
        int rv = dialog.showOpenDialog(gui);      
        if (rv != JFileChooser.APPROVE_OPTION)
        {
        	return;
        }
		FileInputStream file;
		players.clear();
		flags.clear();
		gui.clearText();
		human=new Player("Dummy");
		gui.purgePlayer();
		buildSkillPool(human);
		Clothing.buildClothingTable();
		rebuildCharacterPool();

		boolean dawn = false;
		try {
			file = new FileInputStream(dialog.getSelectedFile());
			Reader loader = new InputStreamReader(file);
			JSONObject object = (JSONObject) JSONValue.parse(loader);
			loader.close();

			JSONArray characters = (JSONArray) object.get("characters");
			for (Object obj : characters) {
				JSONObject character = (JSONObject) obj;
				String type = JSONUtils.readString(character, "type");
				if (Boolean.TRUE.equals(character.get("human"))) {
					human.load(character);
					players.add(human);
				} else if (characterPool.containsKey(type)) {
					characterPool.get(type).load(character);
					players.add(characterPool.get(type));
				}
			}
			
			JSONArray flagsArr = (JSONArray) object.get("flags");
			for(Object flag : flagsArr) {
				flags.add((String)flag);
			}
			
			JSONObject countersObj = (JSONObject) object.get("counters");
			for(Object counter : countersObj.keySet()) {
				counters.put((String)counter, JSONUtils.readFloat(countersObj, (String) counter));
			}
			date = JSONUtils.readInteger(object, "date");
			String time = JSONUtils.readString(object, "time");
			dawn = time.equals("dawn");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gui.populatePlayer(human);
		if (dawn) {
			dawn();
		} else {
			new Prematch(human);
		}
	}

	public static HashSet<Character> everyone(){
		return players;
	}

	public static boolean newChallenger(Personality challenger){
		if(!players.contains(challenger.getCharacter())){
			while(challenger.getCharacter().getLevel()<=human.getLevel()){
				challenger.getCharacter().ding();
			}
			players.add(challenger.getCharacter());
			return true;
		}else{
			return false;
		}
	}

	public static NPC getNPC(String name){
		for(Character c : allNPCs()) {
			if(c.getName().equalsIgnoreCase(name)){
				return (NPC)c;
			}
		}
		System.err.println("NPC \""+ name + "\" is not loaded.");
		return null;
	}

	public static void main(String[] args){
		new Logwriter();
		for (String arg : args) {
			try {
				DebugFlags flag = DebugFlags.valueOf(arg);
				debug[flag.ordinal()] = true;
			} catch (IllegalArgumentException e) {
				//pass
			}
		}
		new Global();
	}

	public static String getIntro() {
		return "You don't really know why you're going to the Student Union in the middle of the night. You'd have to be insane to accept the invitation you received this afternoon. Seriously, someone " +
			"is offering you money to sexfight a bunch girls? You're more likely to get mugged (though you're not carrying any money) or murdered if you show up. Best case scenerio, it's probably a prank " +
			"for gullible freshmen. You have no good reason to believe the invitation is on the level, but here you are, walking into the empty Student Union.\n\nNot quite empty, it turns out. The same " +
			"woman who approached you this afternoon greets you and brings you to a room near the back of the building. Inside, you're surprised to find three quite attractive girls. After comparing notes, " +
			"you confirm they're all freshmen like you and received the same invitation today. You're surprised, both that these girls would agree to such an invitation, and that you're the only guy here. For " +
			"the first time, you start to believe that this might actually happen. After a few minutes of awkward small talk (though none of these girls seem self-conscious about being here), the woman walks " +
			"in again leading another girl. Embarrassingly you recognize the girl, named Cassie, who is a classmate of yours, and who you've become friends with over the past couple weeks. She blushes when she " +
			"sees you and the two of you consciously avoid eye contact while the woman explains the rules of the competition.\n\nThere are a lot of specific points, but the rules basically boil down to this: " +
			"competitors move around the empty areas of the campus and engage each other in sexfights. When one competitor orgasms, the other gets a point and can claim their clothes. Additional orgasms between " +
			"those two players are not worth any points until the loser gets a replacement set of clothes at either the Student Union or the first floor of the dorm building. It seems to be customary, but not " +
			"required, for the loser to get the winner off after a fight, when it doesn't count. After three hours, the match ends and each player is paid for each opponent they defeat, each set of clothes taken, " +
			"and a bonus for whoever scores the most points.\n\nAfter the explanation, she confirms with each participant whether they are still interested in participating. Everyone agrees. The first match " +
			"starts at exactly 10:00.";
	}

	public static void reset(){
		players.clear();
		flags.clear();
		human=new Player("Dummy");
		gui.purgePlayer();
		gui.createCharacter();
	}
	public static float randomfloat() {
		return (float) rng.nextDouble();
	}

	public static String maybeString(String string) {
		if (Global.random(2) == 0) {
			return string;
		} else {
			return "";
		}
	}

	public static <T> T pickRandom(T[] arr) {
		return arr[Global.random(arr.length)];
	}

	interface MatchAction {
		String replace(Character self, String first, String second, String third);
	}
	private static HashMap<String, MatchAction> matchActions = null;
	
	public static void buildParser() {
		matchActions = new HashMap<String, Global.MatchAction>();
		matchActions.put("possessive", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null) {
					return self.possessivePronoun();
				}
				return "";
			}
		});
		matchActions.put("name-possessive", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null) {
					return self.nameOrPossessivePronoun();
				}
				return "";
			}
		});
		matchActions.put("name", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null) {
					return self.name();
				}
				return "";
			}
		});
		matchActions.put("subject-action", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null && third != null) {
					String verbs[] = third.split("\\|");
					return self.subjectAction(verbs[0], verbs[1]);
				}
				return "";
			}
		});
		matchActions.put("pronoun-action", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null && third != null) {
					String verbs[] = third.split("\\|");
					return self.pronoun() + " " + self.action(verbs[0], verbs[1]);
				}
				return "";
			}
		});
		matchActions.put("action", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null && third != null) {
					String verbs[] = third.split("\\|");
					return self.action(verbs[0], verbs[1]);
				}
				return "";
			}
		});
		matchActions.put("subject", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null) {
					return self.subject();
				}
				return "";
			}
		});
		matchActions.put("direct-object", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null) {
					return self.directObject();
				}
				return "";
			}
		});
		matchActions.put("name-do", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null) {
					return self.nameDirectObject();
				}
				return "";
			}
		});
		matchActions.put("body-part", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null && third != null) {
					BodyPart part = self.body.getRandom(third);
					if (part == null && third.equals("cock") && self.has(Trait.strapped)) {
						part = StraponPart.generic;
					}
					if (part != null) {
						return part.describe(self);
					}
				}
				return "";
			}
		});
		matchActions.put("pronoun", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null) {
					return self.pronoun();
				}
				return "";
			}
		});
		matchActions.put("reflective", new MatchAction() {
			@Override
			public String replace(Character self, String first, String second, String third) {
				if (self != null) {
					return self.reflectivePronoun();
				}
				return "";
			}
		});
	}
	
	public static String format(String format, Character self, Character target) {
		//pattern to find stuff like {word:otherword:finalword} in strings
		Pattern p = Pattern.compile("\\{((?:self)|(?:other))(?::([^:}]+))?(?::([^:}]+))?\\}");
		Matcher matcher = p.matcher(format);
		StringBuffer b = new StringBuffer();
		while (matcher.find()) {
			String first = matcher.group(1);
			String second = matcher.group(2);
			if (second == null) {
				second = "";
			}
			String third = matcher.group(3);
			Character character = null;
			if (first.equals("self")) {
				character = self;
			} else if (first.equals("other")) {
				character = target;
			}
			String replacement = matcher.group(0);
			boolean caps = false;
			if (second.toUpperCase().equals(second)) {
				second = second.toLowerCase();
				caps = true;
			}
			MatchAction action = matchActions.get(second);

			if (action == null) {
				System.out.println(second);
			}
			if (matchActions != null && second != null && action != null) {
				replacement = action.replace(character, first, second, third);
				if (caps) {
					replacement = Global.capitalizeFirstLetter(replacement);
				}
			}
			matcher.appendReplacement(b, replacement);
		}
		matcher.appendTail(b);
		return b.toString();
	}

	private static Character noneCharacter = new NPC("none", 1, null);
	public static Character noneCharacter() {
		return noneCharacter;
	}

	public static double randomdouble() {
		return rng.nextDouble();
	}

	public static String prependPrefix(String prefix, String fullDescribe) {
		if (prefix.equals("a ") && "aeiou".contains(fullDescribe.substring(0,1).toLowerCase()))
			return "an " + fullDescribe;
		return prefix + fullDescribe;
	}

	public static Collection<NPC> allNPCs() {
		return characterPool.values();
	}
}