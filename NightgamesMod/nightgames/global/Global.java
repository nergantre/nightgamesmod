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
import java.text.DecimalFormat;
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
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

import com.cedarsoftware.util.io.JsonWriter;

import nightgames.Resources.ResourceLoader;
import nightgames.actions.Action;
import nightgames.actions.Bathe;
import nightgames.actions.BushAmbush;
import nightgames.actions.Craft;
import nightgames.actions.Energize;
import nightgames.actions.Hide;
import nightgames.actions.Locate;
import nightgames.actions.MasturbateAction;
import nightgames.actions.Movement;
import nightgames.actions.PassAmbush;
import nightgames.actions.Recharge;
import nightgames.actions.Resupply;
import nightgames.actions.Scavenge;
import nightgames.actions.SetTrap;
import nightgames.actions.TreeAmbush;
import nightgames.actions.Use;
import nightgames.actions.Wait;
import nightgames.areas.Area;
import nightgames.characters.Airi;
import nightgames.characters.Angel;
import nightgames.characters.Attribute;
import nightgames.characters.Cassie;
import nightgames.characters.Character;
import nightgames.characters.Eve;
import nightgames.characters.Jewel;
import nightgames.characters.Kat;
import nightgames.characters.Mara;
import nightgames.characters.Maya;
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
import nightgames.combat.Combat;
import nightgames.daytime.Daytime;
import nightgames.ftc.FTCMatch;
import nightgames.gui.GUI;
import nightgames.gui.NullGUI;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.modifier.Modifier;
import nightgames.modifier.standard.FTCModifier;
import nightgames.modifier.standard.NoItemsModifier;
import nightgames.modifier.standard.NoModifier;
import nightgames.modifier.standard.NoRecoveryModifier;
import nightgames.modifier.standard.NoToysModifier;
import nightgames.modifier.standard.NudistModifier;
import nightgames.modifier.standard.PacifistModifier;
import nightgames.modifier.standard.UnderwearOnlyModifier;
import nightgames.modifier.standard.VibrationModifier;
import nightgames.modifier.standard.VulnerableModifier;
import nightgames.pet.Ptype;
import nightgames.skills.*;
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
	private static HashSet<Modifier> modifierPool;
	private static HashSet<Character> players;
	private static HashSet<Character> resting;
	private static HashSet<String> flags;
	private static HashMap<String, Float> counters;
	public static Player human;
	private static Match match;
	public static Daytime day;
	private static int date;
	private Date jdate;
	private static TraitTree traitRequirements;
	public static Scene current;
	public static boolean debug[] = new boolean[DebugFlags.values().length];
	public static int debugSimulation = 0;
	public static double moneyRate = 1.0;
	public static double xpRate = .75;
	public static ContextFactory factory;
	public static Context cx;

	public Global() {
		this(false);
	}

	public Global(boolean headless) {
		rng = new Random();
		flags = new HashSet<String>();
		players = new HashSet<Character>();
		resting = new HashSet<Character>();
		counters = new HashMap<String, Float>();
		jdate = new Date();
		counters.put(Flag.malePref.name(), 0.f);
		Clothing.buildClothingTable();
		PrintStream fstream;
		try {
			File logfile = new File("nightgames_log.txt");
			// append the log if it's less than 2 megs in size.
			fstream = new PrintStream(new FileOutputStream(logfile, logfile.length() < 2L * 1024L * 1024L));
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
		// debug[DebugFlags.DEBUG_FTC.ordinal()] = true;
		// debug[DebugFlags.DEBUG_DAMAGE.ordinal()] = true;
		// debug[DebugFlags.DEBUG_SKILLS.ordinal()] = true;
		// debug[DebugFlags.DEBUG_SKILLS_RATING.ordinal()] = true;
		// debug[DebugFlags.DEBUG_PLANNING.ordinal()] = true;
		// debug[DebugFlags.DEBUG_SKILL_CHOICES.ordinal()] = true;
		traitRequirements = new TraitTree(ResourceLoader.getFileResourceAsStream("data/TraitRequirements.xml"));
		current = null;
		factory = new ContextFactory();
		cx = factory.enterContext();
		buildParser();
		buildActionPool();
		buildFeatPool();
		buildModifierPool();
		flag(Flag.AiriEnabled);
		if (headless) {
			gui = new NullGUI();
		} else {
			gui = new GUI();
		}
	}

	public static boolean meetsRequirements(Character c, Trait t) {
		return traitRequirements.meetsRequirements(c, t);
	}

	public static boolean isDebugOn(DebugFlags flag) {
		return debug[flag.ordinal()] && debugSimulation == 0;
	}

	public static void newGame(Player one) {
		human = one;
		players.add(human);
		if (gui != null) {
			gui.populatePlayer(human);
		}
		buildSkillPool(human);
		Clothing.buildClothingTable();
		Global.learnSkills(human);
		rebuildCharacterPool();
		date = 0;
		flag(Flag.systemMessages);
		players.add(getNPC("Jewel"));
		players.add(getNPC("Cassie"));
		players.add(getNPC("Angel"));
		players.add(getNPC("Mara"));
		match = new Match(players, new NoModifier());
	}

	public static int random(int d) {
		if (d <= 0) {
			return 0;
		}
		return rng.nextInt(d);
	}

	// finds a centered random number from [0, d] (inclusive)
	public static int centeredrandom(int d, double center, double sigma) {
		int val = 0;
		center = Math.max(0, Math.min(d, center));
		for (int i = 0; i < 10; i++) {
			double f = rng.nextGaussian() * sigma + center;
			val = (int) Math.round(f);
			if (val >= 0 && val <= d) {
				return val;
			}
		}
		return Math.max(0, Math.min(d, val));
	}

	public static GUI gui() {
		return gui;
	}

	public static Player getPlayer() {
		return human;
	}

	public static void buildSkillPool(Player p) {
		getSkillPool().clear();
		getSkillPool().add(new Slap(p));
		getSkillPool().add(new Tribadism(p));
		getSkillPool().add(new PussyGrind(p));
		getSkillPool().add(new Slap(p));
		getSkillPool().add(new ArmBar(p));
		getSkillPool().add(new Blowjob(p));
		getSkillPool().add(new Cunnilingus(p));
		getSkillPool().add(new Escape(p));
		getSkillPool().add(new Flick(p));
		getSkillPool().add(new ToggleKnot(p));
		getSkillPool().add(new LivingClothing(p));
		getSkillPool().add(new LivingClothingOther(p));
		getSkillPool().add(new Engulf(p));
		getSkillPool().add(new CounterFlower(p));
		getSkillPool().add(new Knee(p));
		getSkillPool().add(new LegLock(p));
		getSkillPool().add(new LickNipples(p));
		getSkillPool().add(new Maneuver(p));
		getSkillPool().add(new Paizuri(p));
		getSkillPool().add(new PerfectTouch(p));
		getSkillPool().add(new Restrain(p));
		getSkillPool().add(new Reversal(p));
		getSkillPool().add(new LeechEnergy(p));
		getSkillPool().add(new SweetScent(p));
		getSkillPool().add(new Spank(p));
		getSkillPool().add(new Stomp(p));
		getSkillPool().add(new StandUp(p));
		getSkillPool().add(new WildThrust(p));
		getSkillPool().add(new SuckNeck(p));
		getSkillPool().add(new Tackle(p));
		getSkillPool().add(new Taunt(p));
		getSkillPool().add(new Trip(p));
		getSkillPool().add(new Whisper(p));
		getSkillPool().add(new Kick(p));
		getSkillPool().add(new PinAndBlow(p));
		getSkillPool().add(new Footjob(p));
		getSkillPool().add(new FootPump(p));
		getSkillPool().add(new HeelGrind(p));
		getSkillPool().add(new Handjob(p));
		getSkillPool().add(new Squeeze(p));
		getSkillPool().add(new Nurple(p));
		getSkillPool().add(new Finger(p));
		getSkillPool().add(new Aphrodisiac(p));
		getSkillPool().add(new Lubricate(p));
		getSkillPool().add(new Dissolve(p));
		getSkillPool().add(new Sedate(p));
		getSkillPool().add(new Tie(p));
		getSkillPool().add(new Masturbate(p));
		getSkillPool().add(new Piston(p));
		getSkillPool().add(new Grind(p));
		getSkillPool().add(new Thrust(p));
		getSkillPool().add(new UseDildo(p));
		getSkillPool().add(new UseOnahole(p));
		getSkillPool().add(new UseCrop(p));
		getSkillPool().add(new Carry(p));
		getSkillPool().add(new Tighten(p));
		getSkillPool().add(new HipThrow(p));
		getSkillPool().add(new SpiralThrust(p));
		getSkillPool().add(new Bravado(p));
		getSkillPool().add(new Diversion(p));
		getSkillPool().add(new Undress(p));
		getSkillPool().add(new StripSelf(p));
		getSkillPool().add(new StripTease(p));
		getSkillPool().add(new Sensitize(p));
		getSkillPool().add(new EnergyDrink(p));
		getSkillPool().add(new Strapon(p));
		getSkillPool().add(new AssFuck(p));
		getSkillPool().add(new Turnover(p));
		getSkillPool().add(new Tear(p));
		getSkillPool().add(new Binding(p));
		getSkillPool().add(new Bondage(p));
		getSkillPool().add(new WaterForm(p));
		getSkillPool().add(new DarkTendrils(p));
		getSkillPool().add(new Dominate(p));
		getSkillPool().add(new FlashStep(p));
		getSkillPool().add(new FlyCatcher(p));
		getSkillPool().add(new Illusions(p));
		getSkillPool().add(new LustAura(p));
		getSkillPool().add(new MagicMissile(p));
		getSkillPool().add(new Masochism(p));
		getSkillPool().add(new NakedBloom(p));
		getSkillPool().add(new ShrinkRay(p));
		getSkillPool().add(new SpawnFaerie(p, Ptype.fairyfem));
		getSkillPool().add(new SpawnImp(p, Ptype.impfem));
		getSkillPool().add(new SpawnFaerie(p, Ptype.fairymale));
		getSkillPool().add(new SpawnImp(p, Ptype.impmale));
		getSkillPool().add(new SpawnSlime(p));
		getSkillPool().add(new StunBlast(p));
		getSkillPool().add(new Fly(p));
		getSkillPool().add(new Command(p));
		getSkillPool().add(new Obey(p));
		getSkillPool().add(new OrgasmSeal(p));
		getSkillPool().add(new DenyOrgasm(p));
		getSkillPool().add(new Drain(p));
		getSkillPool().add(new LevelDrain(p));
		getSkillPool().add(new StoneForm(p));
		getSkillPool().add(new FireForm(p));
		getSkillPool().add(new Defabricator(p));
		getSkillPool().add(new TentaclePorn(p));
		getSkillPool().add(new Sacrifice(p));
		getSkillPool().add(new Frottage(p));
		getSkillPool().add(new FaceFuck(p));
		getSkillPool().add(new VibroTease(p));
		getSkillPool().add(new TailPeg(p));
		getSkillPool().add(new CommandDismiss(p));
		getSkillPool().add(new CommandDown(p));
		getSkillPool().add(new CommandGive(p));
		getSkillPool().add(new CommandHurt(p));
		getSkillPool().add(new CommandInsult(p));
		getSkillPool().add(new CommandMasturbate(p));
		getSkillPool().add(new CommandOral(p));
		getSkillPool().add(new CommandStrip(p));
		getSkillPool().add(new CommandStripPlayer(p));
		getSkillPool().add(new CommandUse(p));
		getSkillPool().add(new ShortCircuit(p));
		getSkillPool().add(new IceForm(p));
		getSkillPool().add(new Barrier(p));
		getSkillPool().add(new CatsGrace(p));
		getSkillPool().add(new Charm(p));
		getSkillPool().add(new Tempt(p));
		getSkillPool().add(new EyesOfTemptation(p));
		getSkillPool().add(new TailJob(p));
		getSkillPool().add(new FaceSit(p));
		getSkillPool().add(new Purr(p));
		getSkillPool().add(new MutualUndress(p));
		getSkillPool().add(new Submit(p));
		getSkillPool().add(new Surrender(p));
		getSkillPool().add(new ReverseFuck(p));
		getSkillPool().add(new ReverseCarry(p));
		getSkillPool().add(new ReverseFly(p));
		getSkillPool().add(new CounterDrain(p));
		getSkillPool().add(new CounterRide(p));
		getSkillPool().add(new CounterPin(p));
		getSkillPool().add(new ReverseAssFuck(p));
		getSkillPool().add(new Nurse(p));
		getSkillPool().add(new Suckle(p));
		getSkillPool().add(new UseDraft(p));
		getSkillPool().add(new ThrowDraft(p));
		getSkillPool().add(new ReverseAssFuck(p));
		getSkillPool().add(new FondleBreasts(p));
		getSkillPool().add(new Fuck(p));
		getSkillPool().add(new Kiss(p));
		getSkillPool().add(new Struggle(p));
		getSkillPool().add(new Tickle(p));
		getSkillPool().add(new nightgames.skills.Wait(p));
		getSkillPool().add(new Bluff(p));
		getSkillPool().add(new StripTop(p));
		getSkillPool().add(new StripBottom(p));
		getSkillPool().add(new Shove(p));
		getSkillPool().add(new Recover(p));
		getSkillPool().add(new Straddle(p));
		getSkillPool().add(new ReverseStraddle(p));
		getSkillPool().add(new Stunned(p));
		getSkillPool().add(new Distracted(p));
		getSkillPool().add(new PullOut(p));
		getSkillPool().add(new ThrowDraft(p));
		getSkillPool().add(new UseDraft(p));
		getSkillPool().add(new TentacleRape(p));
		getSkillPool().add(new Anilingus(p));
		getSkillPool().add(new UseSemen(p));
		getSkillPool().add(new Invitation(p));
		getSkillPool().add(new SubmissiveHold(p));
		getSkillPool().add(new BreastGrowth(p));
		getSkillPool().add(new CockGrowth(p));
		getSkillPool().add(new BreastRay(p));
		getSkillPool().add(new FootSmother(p));
		getSkillPool().add(new FootWorship(p));
		getSkillPool().add(new BreastWorship(p));
		getSkillPool().add(new CockWorship(p));
		getSkillPool().add(new PussyWorship(p));
		getSkillPool().add(new SuccubusSurprise(p));
		getSkillPool().add(new TemptressHandjob(p));
		getSkillPool().add(new TemptressBlowjob(p));
		getSkillPool().add(new TemptressRide(p));
		getSkillPool().add(new TemptressStripTease(p));
		getSkillPool().add(new Blindside(p));
		getSkillPool().add(new Beg(p));
		getSkillPool().add(new Cowardice(p));
		getSkillPool().add(new Dive(p));
		getSkillPool().add(new Offer(p));
		getSkillPool().add(new ShamefulDisplay(p));
		getSkillPool().add(new Stumble(p));
		getSkillPool().add(new TortoiseWrap(p));
		getSkillPool().add(new FaerieSwarm(p));
		getSkillPool().add(new DarkTalisman(p));
		getSkillPool().add(new HeightenSenses(p));
		getSkillPool().add(new LewdSuggestion(p));
		getSkillPool().add(new Suggestion(p));
		getSkillPool().add(new ImbueFetish(p));
		getSkillPool().add(new AssJob(p));
		getSkillPool().add(new TailSuck(p));
		getSkillPool().add(new ToggleSlimeCock(p));
		getSkillPool().add(new ToggleSlimePussy(p));
		getSkillPool().add(new Spores(p));
		getSkillPool().add(new EngulfedFuck(p));

		if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
			getSkillPool().add(new SelfStun(p));
		}
	}

	public static void buildActionPool() {
		actionPool = new HashSet<Action>();
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
		actionPool.add(new BushAmbush());
		actionPool.add(new PassAmbush());
		actionPool.add(new TreeAmbush());
		buildTrapPool();
		for (Trap t : trapPool) {
			actionPool.add(new SetTrap(t));
		}
	}

	public static void buildTrapPool() {
		trapPool = new HashSet<Trap>();
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

	public static void buildFeatPool() {
		featPool = new HashSet<Trait>();
		for (Trait trait : Trait.values()) {
			if (trait.isFeat()) {
				featPool.add(trait);
			}
		}
	}

	public static void buildModifierPool() {
		modifierPool = new HashSet<>();
		modifierPool.add(new NoModifier());
		modifierPool.add(new NoItemsModifier());
		modifierPool.add(new NoToysModifier());
		modifierPool.add(new NoRecoveryModifier());
		modifierPool.add(new NudistModifier());
		modifierPool.add(new PacifistModifier());
		modifierPool.add(new UnderwearOnlyModifier());
		modifierPool.add(new VibrationModifier());
		modifierPool.add(new VulnerableModifier());
	}

	public static HashSet<Action> getActions() {
		return actionPool;
	}

	public static List<Trait> getFeats(Character c) {
		List<Trait> a = traitRequirements.availTraits(c);
		a.sort((first, second) -> first.toString().compareTo(second.toString()));
		return a;
	}

	public static Character lookup(String name) {
		for (Character player : players) {
			if (player.name().equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}

	public static Match getMatch() {
		return match;
	}

	public static Daytime getDay() {
		return day;
	}

	public static void dawn() {
		match = null;
		double level = 0;
		for (Character player : players) {
			player.getStamina().fill();
			player.getArousal().empty();
			player.getMojo().empty();
			player.change();
			level += player.getLevel();
		}
		level /= players.size();
		for (Character rested : resting) {
			rested.gainXP(100 + Math.max(0, (int) Math.round(10 * (level - rested.getLevel()))));
		}
		date++;
		day = new Daytime(human);
		day.plan();
	}

	public static void dusk(Modifier matchmod) {
		HashSet<Character> lineup = new HashSet<Character>();
		Character lover = null;
		int maxaffection = 0;
		day = null;
		unflag(Flag.FTC);
		for (Character player : players) {
			player.getStamina().fill();
			player.getArousal().empty();
			player.getMojo().empty();
			player.getWillpower().fill();
			if (player.getPure(Attribute.Science) > 0) {
				player.chargeBattery();
			}
			if (human.getAffection(player) > maxaffection) {
				maxaffection = human.getAffection(player);
				lover = player;
			}
		}
		// if (true) {
		// lineup.add(human);
		// for (Character c : players) {
		// if (c.name().equals("Kat")) {
		// lineup.add(c);
		// }
		// }
		// match=new Match(lineup,matchmod);
		// } else
		List<Character> participants = new ArrayList<Character>();
		for (Character c : players) {
			Flag disabledFlag = null;
			try {
				disabledFlag = Flag.valueOf(c.getName() + "Disabled");
			} catch (IllegalArgumentException e) {
			}
			if (disabledFlag == null || !Global.checkFlag(disabledFlag)) {
				participants.add(c);
			}
		}
		if (matchmod.name().equals("maya")) {
			ArrayList<Character> randomizer = new ArrayList<>();
			if (lover != null) {
				lineup.add(lover);
			}
			lineup.add(human);
			randomizer.addAll(players);
			Collections.shuffle(randomizer);
			for (Character player : randomizer) {
				if (!lineup.contains(player) && !player.human() && lineup.size() < 4 && !player.has(Trait.event)) {
					lineup.add(player);
				} else if (lineup.size() >= 4 || player.has(Trait.event)) {
					resting.add(player);
				}
			}
			if (!checkFlag(Flag.Maya)) {
				newChallenger(new Maya(human.getLevel()));
				flag(Flag.Maya);
			}
			NPC maya = getNPC("Maya");
			lineup.add(maya);
			maya.gain(Item.Aphrodisiac, 10);
			maya.gain(Item.DisSol, 10);
			maya.gain(Item.Sedative, 10);
			maya.gain(Item.Lubricant, 10);
			maya.gain(Item.BewitchingDraught, 5);
			maya.gain(Item.FeralMusk, 10);
			maya.gain(Item.ExtremeAphrodisiac, 5);
			maya.gain(Item.ZipTie, 10);
			maya.gain(Item.SuccubusDraft, 10);
			maya.gain(Item.Lactaid, 5);
			maya.gain(Item.Handcuffs, 5);
			maya.gain(Item.Onahole2);
			maya.gain(Item.Dildo2);
			maya.gain(Item.Strapon2);
			match = new Match(lineup, matchmod);
		} else if (matchmod.name().equals("ftc")) {
			Character prey = ((FTCModifier) matchmod).getPrey();
			lineup.add(prey);
			if (!prey.human())
				lineup.add(human);
			while (lineup.size() < 5)
				lineup.add((Character) pickRandom(participants.toArray()));
			resting.addAll(participants);
			resting.removeIf(lineup::contains);
			match = buildMatch(lineup, matchmod);
		} else if (participants.size() > 5) {
			ArrayList<Character> randomizer = new ArrayList<Character>();
			if (lover != null) {
				lineup.add(lover);
			}
			lineup.add(human);
			randomizer.addAll(participants);
			Collections.shuffle(randomizer);
			for (Character player : randomizer) {
				if (!lineup.contains(player) && !player.human() && lineup.size() < 5) {
					lineup.add(player);
				} else if (lineup.size() >= 5) {
					resting.add(player);
				}
			}
			match = buildMatch(lineup, matchmod);
		} else {
			match = buildMatch(participants, matchmod);
		}
		startMatch();
	}

	public static void startMatch() {
		match.round();
	}

	public static String gainSkills(Character c) {
		String message = "";
		if (c.getPure(Attribute.Dark) >= 6 && !c.has(Trait.darkpromises)) {
			c.add(Trait.darkpromises);
		} else if (!(c.getPure(Attribute.Dark) >= 6) && c.has(Trait.darkpromises)) {
			c.remove(Trait.darkpromises);
		}
		boolean pheromonesRequirements = c.getPure(Attribute.Animism) >= 2 || c.has(Trait.augmentedPheromones);
		if (pheromonesRequirements && !c.has(Trait.pheromones)) {
			c.add(Trait.pheromones);
		} else if (!pheromonesRequirements && c.has(Trait.pheromones)) {
			c.remove(Trait.pheromones);
		}
		return message;
	}

	public static void learnSkills(Character c) {
		for (Skill skill : getSkillPool()) {
			c.learn(skill);
		}
	}

	public static String capitalizeFirstLetter(String original) {
		if (original == null) {
			return "";
		}
		if (original.length() == 0) {
			return original;
		}
		return original.substring(0, 1).toUpperCase() + original.substring(1);
	}

	private static Map<String, NPC> characterPool;

	public static NPC getNPCByType(String type) {
		NPC results = characterPool.get(type);
		if (results == null) {
			System.err.println("failed to find NPC for type " + type);
		}
		return results;
	}

	public static Character getCharacterByType(String type) {
		if (type.equals(human.getType())) {
			return human;
		}
		return getNPCByType(type);
	}

	public static HashMap<String, Area> buildMap() {
		Area quad = new Area("Quad",
				"You are in the <b>Quad</b> that sits in the center of the Dorm, the Dining Hall, the Engineering Building, and the Liberal Arts Building. There's "
						+ "no one around at this time of night, but the Quad is well-lit and has no real cover. You can probably be spotted from any of the surrounding buildings, it may "
						+ "not be a good idea to hang out here for long.",
				Movement.quad);
		Area dorm = new Area("Dorm",
				"You are in the <b>Dorm</b>. Everything is quieter than it would be in any other dorm this time of night. You've been told the entire first floor "
						+ "is empty during match hours, but you wouldn't be surprised if a few of the residents are hiding in their rooms, peeking at the fights. You've stashed some clothes "
						+ "in one of the rooms you're sure is empty, which is common practice for most of the competitors.",
				Movement.dorm);
		Area shower = new Area("Showers",
				"You are in the first floor <b>Showers</b>. There are a half-dozen stalls shared by the residents on this floor. They aren't very big, but there's "
						+ "room to hide if need be. A hot shower would help you recover after a tough fight, but you'd be vulnerable if someone finds you.",
				Movement.shower);
		Area laundry = new Area("Laundry Room",
				"You are in the <b>Laundry Room</b> in the basement of the Dorm. Late night is prime laundry time in your dorm, but none of these machines "
						+ "are running. You're a bit jealous when you notice that the machines here are free, while yours are coin-op. There's a tunnel here that connects to the basement of the "
						+ "Dining Hall.",
				Movement.laundry);
		Area engineering = new Area("Engineering Building",
				"You are in the Science and <b>Engineering Building</b>. Most of the lecture rooms are in other buildings; this one is mostly "
						+ "for specialized rooms and labs. The first floor contains workshops mostly used by the Mechanical and Electrical Engineering classes. The second floor has "
						+ "the Biology and Chemistry Labs. There's a third floor, but that's considered out of bounds.",
				Movement.engineering);
		Area lab = new Area("Chemistry Lab",
				"You are in the <b>Chemistry Lab</b>. The shelves and cabinets are full of all manner of dangerous and/or interesting chemicals. A clever enough "
						+ "person could combine some of the safer ones into something useful. Just outside the lab is a bridge connecting to the library.",
				Movement.lab);
		Area workshop = new Area("Workshop",
				"You are in the Mechanical Engineering <b>Workshop</b>. There are shelves of various mechanical components and the back table is covered "
						+ "with half-finished projects. A few dozen Mechanical Engineering students use this workshop each week, but it's well stocked enough that no one would miss "
						+ "some materials that might be of use to you.",
				Movement.workshop);
		Area libarts = new Area("Liberal Arts Building",
				"You are in the <b>Liberal Arts Building</b>. There are three floors of lecture halls and traditional classrooms, but only "
						+ "the first floor is in bounds. The Library is located directly out back, and the side door is just a short walk from the pool.",
				Movement.la);
		Area pool = new Area("Pool",
				"You are by the indoor <b>Pool</b>, which is connected to the Student Union for reasons that no one has ever really explained. There pool is quite "
						+ "large and there is even a jacuzzi. A quick soak would feel good, but the lack of privacy is a concern. The side doors are locked at this time of night, but the "
						+ "door to the Student Union is open and there's a back door that exits near the Liberal Arts building.",
				Movement.pool);
		Area library = new Area("Library",
				"You are in the <b>Library</b>. It's a two floor building with an open staircase connecting the first and second floors. The front entrance leads to "
						+ "the Liberal Arts building. The second floor has a Bridge connecting to the Chemistry Lab in the Science and Engineering building.",
				Movement.library);
		Area dining = new Area("Dining Hall",
				"You are in the <b>Dining Hall</b>. Most students get their meals here, though some feel it's worth the extra money to eat out. The "
						+ "dining hall is quite large and your steps echo on the linoleum, but you could probably find someplace to hide if you need to.",
				Movement.dining);
		Area kitchen = new Area("Kitchen",
				"You are in the <b>Kitchen</b> where student meals are prepared each day. The industrial fridge and surrounding cabinets are full of the "
						+ "ingredients for any sort of bland cafeteria food you can imagine. Fortunately, you aren't very hungry. There's a chance you might be able to cook up some "
						+ "of the more obscure items into something useful.",
				Movement.kitchen);
		Area storage = new Area("Storage Room",
				"You are in a <b>Storage Room</b> under the Dining Hall. It's always unlocked and receives a fair bit of foot traffic from students "
						+ "using the tunnel to and from the Dorm, so no one keeps anything important in here. There's enough junk down here to provide some hiding places and there's a chance "
						+ "you could find something useable in one of these boxes.",
				Movement.storage);
		Area tunnel = new Area("Tunnel",
				"You are in the <b>Tunnel</b> connecting the dorm to the dining hall. It doesn't get a lot of use during the day and most of the freshmen "
						+ "aren't even aware of its existence, but many upperclassmen have been thankful for it on cold winter days and it's proven to be a major tactical asset. The "
						+ "tunnel is well-lit and doesn't offer any hiding places.",
				Movement.tunnel);
		Area bridge = new Area("Bridge",
				"You are on the <b>Bridge</b> connecting the second floors of the Science and Engineering Building and the Library. It's essentially just a "
						+ "corridor, so there's no place for anyone to hide.",
				Movement.bridge);
		Area sau = new Area("Student Union",
				"You are in the <b>Student Union</b>, which doubles as base of operations during match hours. You and the other competitors can pick up "
						+ "a change of clothing here.",
				Movement.union);
		Area courtyard = new Area("Courtyard",
				"You are in the <b>Courtyard</b>. "
						+ "It's a small opening between four buildings. There's not much to see here except a tidy garden maintained by the botany department.",
				Movement.courtyard);
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
		workshop.shortcut(pool);
		pool.shortcut(workshop);
		library.shortcut(tunnel);
		tunnel.shortcut(library);
		HashMap<String, Area> map = new HashMap<String, Area>();
		map.put("Quad", quad);
		map.put("Dorm", dorm);
		map.put("Shower", shower);
		map.put("Laundry", laundry);
		map.put("Engineering", engineering);
		map.put("Workshop", workshop);
		map.put("Lab", lab);
		map.put("Liberal Arts", libarts);
		map.put("Pool", pool);
		map.put("Library", library);
		map.put("Dining", dining);
		map.put("Kitchen", kitchen);
		map.put("Storage", storage);
		map.put("Tunnel", tunnel);
		map.put("Bridge", bridge);
		map.put("Union", sau);
		map.put("Courtyard", courtyard);
		return map;
	}

	public static void flag(String f) {
		flags.add(f);
	}

	public static void unflag(String f) {
		flags.remove(f);
	}

	public static void flag(Flag f) {
		flags.add(f.name());
	}

	public static void unflag(Flag f) {
		flags.remove(f.name());
	}

	public static boolean checkFlag(Flag f) {
		return flags.contains(f.name());
	}

	public static boolean checkFlag(String key) {
		return flags.contains(key);
	}

	public static float getValue(Flag f) {
		if (!counters.containsKey(f.name())) {
			return 0;
		} else {
			return counters.get(f.name());
		}
	}

	public static void modCounter(Flag f, float inc) {
		counters.put(f.name(), getValue(f) + inc);
	}

	public static void setCounter(Flag f, float val) {
		counters.put(f.name(), val);
	}

	@SuppressWarnings("unchecked")
	public static void save(boolean auto) {
		JSONObject obj = new JSONObject();
		JSONArray characterArr = new JSONArray();
		for (Character c : players) {
			characterArr.add(c.save());
		}
		obj.put("characters", characterArr);
		JSONArray flagsArr = new JSONArray();
		for (String flag : flags) {
			flagsArr.add(flag);
		}
		obj.put("flags", flagsArr);
		JSONObject countersObj = new JSONObject();
		for (String counter : counters.keySet()) {
			countersObj.put(counter, counters.get(counter));
		}
		obj.put("counters", countersObj);
		obj.put("time", match == null ? "dusk" : "dawn");
		obj.put("date", date);

		try {
			FileWriter file;
			if (auto) {
				file = new FileWriter("./auto.ngs");
			} else {
				JFileChooser dialog = new JFileChooser("./");
				FileFilter savesFilter = new FileNameExtensionFilter("Nightgame Saves", "ngs");
				dialog.addChoosableFileFilter(savesFilter);
				dialog.setFileFilter(savesFilter);
				dialog.setMultiSelectionEnabled(false);
				int rv = dialog.showSaveDialog(gui);

				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				File saveFile = dialog.getSelectedFile();
				String fname = saveFile.getAbsolutePath();
				if (!fname.endsWith(".ngs")) {
					fname += ".ngs";
				}
				file = new FileWriter(new File(fname));
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

		try {
			JSONArray characterSet = (JSONArray) JSONValue.parseWithException(
					new InputStreamReader(ResourceLoader.getFileResourceAsStream("characters/included.json")));
			for (Object obj : characterSet) {
				String name = (String) obj;
				try {
					Personality npc = new CustomNPC(
							JSONSourceNPCDataLoader.load(ResourceLoader.getFileResourceAsStream("characters/" + name)));
					characterPool.put(npc.getCharacter().getType(), npc.getCharacter());
					System.out.println("Loaded " + name);
				} catch (ParseException | IOException e1) {
					System.err.println("Failed to load NPC " + name);
					e1.printStackTrace();
				}
			}
		} catch (ParseException | IOException e1) {
			System.err.println("Failed to load character set");
			e1.printStackTrace();
		}

		Personality cassie = new Cassie();
		Personality angel = new Angel();
		Personality reyka = new Reyka();
		Personality kat = new Kat();
		Personality mara = new Mara();
		Personality jewel = new Jewel();
		Personality airi = new Airi();
		Personality eve = new Eve();
		Personality maya = new Maya(1);
		characterPool.put(cassie.getCharacter().getType(), cassie.getCharacter());
		characterPool.put(angel.getCharacter().getType(), angel.getCharacter());
		characterPool.put(reyka.getCharacter().getType(), reyka.getCharacter());
		characterPool.put(kat.getCharacter().getType(), kat.getCharacter());
		characterPool.put(mara.getCharacter().getType(), mara.getCharacter());
		characterPool.put(jewel.getCharacter().getType(), jewel.getCharacter());
		characterPool.put(airi.getCharacter().getType(), airi.getCharacter());
		characterPool.put(eve.getCharacter().getType(), eve.getCharacter());
		characterPool.put(maya.getCharacter().getType(), maya.getCharacter());
	}

	public static void load() {
		JFileChooser dialog = new JFileChooser("./");
		FileFilter savesFilter = new FileNameExtensionFilter("Nightgame Saves", "ngs");
		dialog.addChoosableFileFilter(savesFilter);
		dialog.setFileFilter(savesFilter);
		dialog.setMultiSelectionEnabled(false);
		int rv = dialog.showOpenDialog(gui);
		if (rv != JFileChooser.APPROVE_OPTION) {
			return;
		}
		FileInputStream fileIS;
		players.clear();
		flags.clear();
		gui.clearText();
		human = new Player("Dummy");
		gui.purgePlayer();
		buildSkillPool(human);
		Clothing.buildClothingTable();
		rebuildCharacterPool();

		boolean dawn = false;
		try {
			File file = dialog.getSelectedFile();
			if (!file.isFile()) {
				file = new File(dialog.getSelectedFile().getAbsolutePath() + ".ngs");
				if (!file.isFile()) {
					// not a valid save, abort
					JOptionPane.showMessageDialog(gui, "Nightgames save file not found", "File not found",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			fileIS = new FileInputStream(file);
			Reader loader = new InputStreamReader(fileIS);
			JSONObject object = (JSONObject) JSONValue.parseWithException(loader);
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
			for (Object flag : flagsArr) {
				flags.add((String) flag);
			}

			JSONObject countersObj = (JSONObject) object.get("counters");
			for (Object counter : countersObj.keySet()) {
				counters.put((String) counter, JSONUtils.readFloat(countersObj, (String) counter));
			}
			date = JSONUtils.readInteger(object, "date");
			String time = JSONUtils.readString(object, "time");
			dawn = time.equals("dawn");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		gui.populatePlayer(human);
		if (dawn) {
			dawn();
		} else {
			decideMatchType().buildPrematch(human);
		}
	}

	public static HashSet<Character> everyone() {
		return players;
	}

	public static boolean newChallenger(Personality challenger) {
		if (!players.contains(challenger.getCharacter())) {
			while (challenger.getCharacter().getLevel() <= human.getLevel()) {
				challenger.getCharacter().ding();
			}
			players.add(challenger.getCharacter());
			return true;
		} else {
			return false;
		}
	}

	public static NPC getNPC(String name) {
		for (Character c : allNPCs()) {
			if (c.getName().equalsIgnoreCase(name)) {
				return (NPC) c;
			}
		}
		System.err.println("NPC \"" + name + "\" is not loaded.");
		return null;
	}

	public static void main(String[] args) {
		new Logwriter();
		for (String arg : args) {
			try {
				DebugFlags flag = DebugFlags.valueOf(arg);
				debug[flag.ordinal()] = true;
			} catch (IllegalArgumentException e) {
				// pass
			}
		}
		new Global();
	}

	public static String getIntro() {
		return "You don't really know why you're going to the Student Union in the middle of the night. You'd have to be insane to accept the invitation you received this afternoon. Seriously, someone "
				+ "is offering you money to sexfight a bunch of girls? You're more likely to get mugged (though you're not carrying any money) or murdered if you show up. Best case scenario, it's probably a prank "
				+ "for gullible freshmen. You have no good reason to believe the invitation is on the level, but here you are, walking into the empty Student Union.\n\nNot quite empty, it turns out. The same "
				+ "woman who approached you this afternoon greets you and brings you to a room near the back of the building. Inside, you're surprised to find three quite attractive girls. After comparing notes, "
				+ "you confirm they're all freshmen like you and received the same invitation today. You're surprised, both that these girls would agree to such an invitation, and that you're the only guy here. For "
				+ "the first time, you start to believe that this might actually happen. After a few minutes of awkward small talk (though none of these girls seem self-conscious about being here), the woman walks "
				+ "in again leading another girl. Embarrassingly you recognize the girl, named Cassie, who is a classmate of yours, and who you've become friends with over the past couple weeks. She blushes when she "
				+ "sees you and the two of you consciously avoid eye contact while the woman explains the rules of the competition.\n\nThere are a lot of specific points, but the rules basically boil down to this: "
				+ "competitors move around the empty areas of the campus and engage each other in sexfights. When one competitor orgasms and can't go on, the other gets a point and can claim the loser's clothes. "
				+ "Those two players are forbidden to engage again until the loser gets a replacement set of clothes at either the Student Union or the first floor of the dorm building. It seems to be customary, but not "
				+ "required, for the loser to get the winner off after a fight, when it doesn't count. After three hours, the match ends and each player is paid for each opponent they defeat, each set of clothes turned in, "
				+ "and a bonus for whoever scores the most points.\n\nAfter the explanation, she confirms with each participant whether they are still interested in participating. Everyone agrees. The first match "
				+ "starts at exactly 10:00.";
	}

	public static void reset() {
		players.clear();
		flags.clear();
		human = new Player("Dummy");
		gui.purgePlayer();
		gui.createCharacter();
	}

	public static boolean inGame() {
		return !players.isEmpty();
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

	public static int getDate() {
		return date;
	}

	interface MatchAction {
		String replace(Character self, String first, String second, String third);
	}

	private static HashMap<String, MatchAction> matchActions = null;

	public static void buildParser() {
		matchActions = new HashMap<String, Global.MatchAction>();
		matchActions.put("possessive", (self, first, second, third) -> {
			if (self != null) {
				return self.possessivePronoun();
			}
			return "";
		});
		matchActions.put("name-possessive", (self, first, second, third) -> {
			if (self != null) {
				return self.nameOrPossessivePronoun();
			}
			return "";
		});
		matchActions.put("name", (self, first, second, third) -> {
			if (self != null) {
				return self.name();
			}
			return "";
		});
		matchActions.put("subject-action", (self, first, second, third) -> {
			if (self != null && third != null) {
				String verbs[] = third.split("\\|");
				return self.subjectAction(verbs[0], verbs[1]);
			}
			return "";
		});
		matchActions.put("pronoun-action", (self, first, second, third) -> {
			if (self != null && third != null) {
				String verbs[] = third.split("\\|");
				return self.pronoun() + " " + self.action(verbs[0], verbs[1]);
			}
			return "";
		});
		matchActions.put("action", (self, first, second, third) -> {
			if (self != null && third != null) {
				String verbs[] = third.split("\\|");
				return self.action(verbs[0], verbs[1]);
			}
			return "";
		});
		matchActions.put("subject", (self, first, second, third) -> {
			if (self != null) {
				return self.subject();
			}
			return "";
		});
		matchActions.put("direct-object", (self, first, second, third) -> {
			if (self != null) {
				return self.directObject();
			}
			return "";
		});
		matchActions.put("name-do", (self, first, second, third) -> {
			if (self != null) {
				return self.nameDirectObject();
			}
			return "";
		});
		matchActions.put("body-part", (self, first, second, third) -> {
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
		});
		matchActions.put("pronoun", (self, first, second, third) -> {
			if (self != null) {
				return self.pronoun();
			}
			return "";
		});
		matchActions.put("reflective", (self, first, second, third) -> {
			if (self != null) {
				return self.reflectivePronoun();
			}
			return "";
		});
	}

	public static String format(String format, Character self, Character target) {
		// pattern to find stuff like {word:otherword:finalword} in strings
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
		if (prefix.equals("a ") && "aeiou".contains(fullDescribe.substring(0, 1).toLowerCase())) {
			return "an " + fullDescribe;
		}
		return prefix + fullDescribe;
	}

	public static Collection<NPC> allNPCs() {
		return characterPool.values();
	}

	private static DecimalFormat formatter = new DecimalFormat("#.##");

	public static String formatDecimal(double val) {
		return formatter.format(val);
	}

	public static HashSet<Skill> getSkillPool() {
		return skillPool;
	}

	public static HashSet<Modifier> getModifierPool() {
		return modifierPool;
	}

	public static HashSet<Skill> getByTactics(Combat c, Tactics tact) {
		HashSet<Skill> result = new HashSet<>(skillPool);
		result.removeIf(skill -> skill.type(c) != tact);
		return result;
	}

	public static MatchType decideMatchType() {
		if (human.getLevel() < 15)
			return MatchType.NORMAL;
		if (!checkFlag(Flag.didFTC))
			return MatchType.FTC;
		return isDebugOn(DebugFlags.DEBUG_FTC) || Global.random(10) == 0 ? MatchType.FTC : MatchType.NORMAL;
	}

	private static Match buildMatch(Collection<Character> combatants, Modifier mod) {
		if (mod.name().equals("ftc")) {
			flag(Flag.FTC);
			return new FTCMatch(combatants, ((FTCModifier) mod).getPrey());
		} else {
			return new Match(combatants, mod);
		}
	}

	public static HashSet<Character> getCharacters() {
		return new HashSet<>(players);
	}
}
