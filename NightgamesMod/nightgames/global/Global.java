package nightgames.global;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
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
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

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
import nightgames.areas.MapDrawHint;
import nightgames.characters.Airi;
import nightgames.characters.Angel;
import nightgames.characters.Attribute;
import nightgames.characters.Cassie;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
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
import nightgames.characters.Yui;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.StraponPart;
import nightgames.characters.custom.CustomNPC;
import nightgames.characters.custom.JsonSourceNPCDataLoader;
import nightgames.characters.custom.NPCData;
import nightgames.combat.Combat;
import nightgames.daytime.Daytime;
import nightgames.ftc.FTCMatch;
import nightgames.gui.GUI;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.json.JsonUtils;
import nightgames.modifier.CustomModifierLoader;
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
import nightgames.start.NpcConfiguration;
import nightgames.start.PlayerConfiguration;
import nightgames.start.StartConfiguration;
import nightgames.status.Status;
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
    private static Set<Skill> skillPool = new HashSet<>();
    private static Map<String, NPC> characterPool;
    private static Set<Action> actionPool;
    private static Set<Trap> trapPool;
    private static Set<Trait> featPool;
    private static Set<Modifier> modifierPool;
    private static Set<Character> players;
    private static Set<Character> debugChars;
    private static Set<Character> resting;
    private static Set<String> flags;
    private static Map<String, Float> counters;
    public static Player human;
    private static Match match;
    public static Daytime day;
    protected static int date;
    private static Time time;
    private Date jdate;
    private static TraitTree traitRequirements;
    public static Scene current;
    public static boolean debug[] = new boolean[DebugFlags.values().length];
    public static int debugSimulation = 0;
    public static double moneyRate = 1.0;
    public static double xpRate = 1.0;
    public static ContextFactory factory;
    public static Context cx;

    public static final Path COMBAT_LOG_DIR = new File("combatlogs").toPath();

    public Global() {
        rng = new Random();
        flags = new HashSet<>();
        players = new HashSet<>();
        debugChars = new HashSet<>();
        resting = new HashSet<>();
        counters = new HashMap<>();
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

        // debug[DebugFlags.DEBUG_SCENE.ordinal()] = true;
        // debug[DebugFlags.DEBUG_LOADING.ordinal()] = true;
        // debug[DebugFlags.DEBUG_FTC.ordinal()] = true;
        // debug[DebugFlags.DEBUG_DAMAGE.ordinal()] = true;
        // debug[DebugFlags.DEBUG_SKILLS.ordinal()] = true;
        // debug[DebugFlags.DEBUG_SKILLS_RATING.ordinal()] = true;
        // debug[DebugFlags.DEBUG_PLANNING.ordinal()] = true;
        // debug[DebugFlags.DEBUG_SKILL_CHOICES.ordinal()] = true;
        // debug[DebugFlags.DEBUG_ADDICTION.ordinal()] = true;
        traitRequirements = new TraitTree(ResourceLoader.getFileResourceAsStream("data/TraitRequirements.xml"));
        current = null;
        factory = new ContextFactory();
        cx = factory.enterContext();
        buildParser();
        buildActionPool();
        buildFeatPool();
        buildSkillPool(noneCharacter);
        buildModifierPool();
        flag(Flag.AiriEnabled);
        gui = makeGUI();
    }

    protected GUI makeGUI() {
        return new GUI();
    }

    public static boolean meetsRequirements(Character c, Trait t) {
        return traitRequirements.meetsRequirements(c, t);
    }

    public static boolean isDebugOn(DebugFlags flag) {
        return debug[flag.ordinal()] && debugSimulation == 0;
    }

    public static void newGame(String playerName, Optional<StartConfiguration> config, List<Trait> pickedTraits,
                    CharacterSex pickedGender, Map<Attribute, Integer> selectedAttributes) {
        Optional<PlayerConfiguration> playerConfig = config.map(c -> c.player);
        Collection<Flag> cfgFlags = config.map(StartConfiguration::getFlags).orElse(new ArrayList<>());
        human = new Player(playerName, pickedGender, playerConfig, pickedTraits, selectedAttributes);
        players.add(human);
        if (gui != null) {
            gui.populatePlayer(human);
        }
        buildSkillPool(human);
        Clothing.buildClothingTable();
        learnSkills(human);
        rebuildCharacterPool(config);
        // Add starting characters to players
        players.addAll(characterPool.values().stream().filter(npc -> npc.isStartCharacter).collect(Collectors.toList()));
        if (!cfgFlags.isEmpty()) {
            flags = cfgFlags.stream().map(Flag::name).collect(Collectors.toSet());
        }
        Set<Character> lineup = pickCharacters(players, Collections.singleton(human), 4);
        match = new Match(lineup, new NoModifier());
        time = Time.NIGHT;
        saveWithDialog();
    }

    public static int random(int start, int end) {
        return rng.nextInt(end - start) + start;
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

    public static void buildSkillPool(Character ch) {
        getSkillPool().clear();
        getSkillPool().add(new Slap(ch));
        getSkillPool().add(new Tribadism(ch));
        getSkillPool().add(new PussyGrind(ch));
        getSkillPool().add(new Slap(ch));
        getSkillPool().add(new ArmBar(ch));
        getSkillPool().add(new Blowjob(ch));
        getSkillPool().add(new Cunnilingus(ch));
        getSkillPool().add(new Escape(ch));
        getSkillPool().add(new Flick(ch));
        getSkillPool().add(new ToggleKnot(ch));
        getSkillPool().add(new LivingClothing(ch));
        getSkillPool().add(new LivingClothingOther(ch));
        getSkillPool().add(new Engulf(ch));
        getSkillPool().add(new CounterFlower(ch));
        getSkillPool().add(new Knee(ch));
        getSkillPool().add(new LegLock(ch));
        getSkillPool().add(new LickNipples(ch));
        getSkillPool().add(new Maneuver(ch));
        getSkillPool().add(new Paizuri(ch));
        getSkillPool().add(new PerfectTouch(ch));
        getSkillPool().add(new Restrain(ch));
        getSkillPool().add(new Reversal(ch));
        getSkillPool().add(new LeechEnergy(ch));
        getSkillPool().add(new SweetScent(ch));
        getSkillPool().add(new Spank(ch));
        getSkillPool().add(new Stomp(ch));
        getSkillPool().add(new StandUp(ch));
        getSkillPool().add(new WildThrust(ch));
        getSkillPool().add(new SuckNeck(ch));
        getSkillPool().add(new Tackle(ch));
        getSkillPool().add(new Taunt(ch));
        getSkillPool().add(new Trip(ch));
        getSkillPool().add(new Whisper(ch));
        getSkillPool().add(new Kick(ch));
        getSkillPool().add(new PinAndBlow(ch));
        getSkillPool().add(new Footjob(ch));
        getSkillPool().add(new FootPump(ch));
        getSkillPool().add(new HeelGrind(ch));
        getSkillPool().add(new Handjob(ch));
        getSkillPool().add(new Squeeze(ch));
        getSkillPool().add(new Nurple(ch));
        getSkillPool().add(new Finger(ch));
        getSkillPool().add(new Aphrodisiac(ch));
        getSkillPool().add(new Lubricate(ch));
        getSkillPool().add(new Dissolve(ch));
        getSkillPool().add(new Sedate(ch));
        getSkillPool().add(new Tie(ch));
        getSkillPool().add(new Masturbate(ch));
        getSkillPool().add(new Piston(ch));
        getSkillPool().add(new Grind(ch));
        getSkillPool().add(new Thrust(ch));
        getSkillPool().add(new UseDildo(ch));
        getSkillPool().add(new UseOnahole(ch));
        getSkillPool().add(new UseCrop(ch));
        getSkillPool().add(new Carry(ch));
        getSkillPool().add(new Tighten(ch));
        getSkillPool().add(new HipThrow(ch));
        getSkillPool().add(new SpiralThrust(ch));
        getSkillPool().add(new Bravado(ch));
        getSkillPool().add(new Diversion(ch));
        getSkillPool().add(new Undress(ch));
        getSkillPool().add(new StripSelf(ch));
        getSkillPool().add(new StripTease(ch));
        getSkillPool().add(new Sensitize(ch));
        getSkillPool().add(new EnergyDrink(ch));
        getSkillPool().add(new Strapon(ch));
        getSkillPool().add(new AssFuck(ch));
        getSkillPool().add(new Turnover(ch));
        getSkillPool().add(new Tear(ch));
        getSkillPool().add(new Binding(ch));
        getSkillPool().add(new Bondage(ch));
        getSkillPool().add(new WaterForm(ch));
        getSkillPool().add(new DarkTendrils(ch));
        getSkillPool().add(new Dominate(ch));
        getSkillPool().add(new FlashStep(ch));
        getSkillPool().add(new FlyCatcher(ch));
        getSkillPool().add(new Illusions(ch));
        getSkillPool().add(new LustAura(ch));
        getSkillPool().add(new MagicMissile(ch));
        getSkillPool().add(new Masochism(ch));
        getSkillPool().add(new NakedBloom(ch));
        getSkillPool().add(new ShrinkRay(ch));
        getSkillPool().add(new SpawnFaerie(ch, Ptype.fairyfem));
        getSkillPool().add(new SpawnImp(ch, Ptype.impfem));
        getSkillPool().add(new SpawnFaerie(ch, Ptype.fairymale));
        getSkillPool().add(new SpawnImp(ch, Ptype.impmale));
        getSkillPool().add(new SpawnFGoblin(ch, Ptype.fgoblin));
        getSkillPool().add(new SpawnSlime(ch));
        getSkillPool().add(new StunBlast(ch));
        getSkillPool().add(new Fly(ch));
        getSkillPool().add(new Command(ch));
        getSkillPool().add(new Obey(ch));
        getSkillPool().add(new OrgasmSeal(ch));
        getSkillPool().add(new DenyOrgasm(ch));
        getSkillPool().add(new Drain(ch));
        getSkillPool().add(new LevelDrain(ch));
        getSkillPool().add(new StoneForm(ch));
        getSkillPool().add(new FireForm(ch));
        getSkillPool().add(new Defabricator(ch));
        getSkillPool().add(new TentaclePorn(ch));
        getSkillPool().add(new Sacrifice(ch));
        getSkillPool().add(new Frottage(ch));
        getSkillPool().add(new FaceFuck(ch));
        getSkillPool().add(new VibroTease(ch));
        getSkillPool().add(new TailPeg(ch));
        getSkillPool().add(new CommandDismiss(ch));
        getSkillPool().add(new CommandDown(ch));
        getSkillPool().add(new CommandGive(ch));
        getSkillPool().add(new CommandHurt(ch));
        getSkillPool().add(new CommandInsult(ch));
        getSkillPool().add(new CommandMasturbate(ch));
        getSkillPool().add(new CommandOral(ch));
        getSkillPool().add(new CommandStrip(ch));
        getSkillPool().add(new CommandStripPlayer(ch));
        getSkillPool().add(new CommandUse(ch));
        getSkillPool().add(new ShortCircuit(ch));
        getSkillPool().add(new IceForm(ch));
        getSkillPool().add(new Barrier(ch));
        getSkillPool().add(new CatsGrace(ch));
        getSkillPool().add(new Charm(ch));
        getSkillPool().add(new Tempt(ch));
        getSkillPool().add(new EyesOfTemptation(ch));
        getSkillPool().add(new TailJob(ch));
        getSkillPool().add(new FaceSit(ch));
        getSkillPool().add(new Purr(ch));
        getSkillPool().add(new MutualUndress(ch));
        getSkillPool().add(new Surrender(ch));
        getSkillPool().add(new ReverseFuck(ch));
        getSkillPool().add(new ReverseCarry(ch));
        getSkillPool().add(new ReverseFly(ch));
        getSkillPool().add(new CounterDrain(ch));
        getSkillPool().add(new CounterRide(ch));
        getSkillPool().add(new CounterPin(ch));
        getSkillPool().add(new ReverseAssFuck(ch));
        getSkillPool().add(new Nurse(ch));
        getSkillPool().add(new Suckle(ch));
        getSkillPool().add(new UseDraft(ch));
        getSkillPool().add(new ThrowDraft(ch));
        getSkillPool().add(new ReverseAssFuck(ch));
        getSkillPool().add(new FondleBreasts(ch));
        getSkillPool().add(new Fuck(ch));
        getSkillPool().add(new Kiss(ch));
        getSkillPool().add(new Struggle(ch));
        getSkillPool().add(new Tickle(ch));
        getSkillPool().add(new nightgames.skills.Wait(ch));
        getSkillPool().add(new Bluff(ch));
        getSkillPool().add(new StripTop(ch));
        getSkillPool().add(new StripBottom(ch));
        getSkillPool().add(new Shove(ch));
        getSkillPool().add(new Recover(ch));
        getSkillPool().add(new Straddle(ch));
        getSkillPool().add(new ReverseStraddle(ch));
        getSkillPool().add(new Stunned(ch));
        getSkillPool().add(new Distracted(ch));
        getSkillPool().add(new PullOut(ch));
        getSkillPool().add(new ThrowDraft(ch));
        getSkillPool().add(new UseDraft(ch));
        getSkillPool().add(new TentacleRape(ch));
        getSkillPool().add(new Anilingus(ch));
        getSkillPool().add(new UseSemen(ch));
        getSkillPool().add(new Invitation(ch));
        getSkillPool().add(new SubmissiveHold(ch));
        getSkillPool().add(new BreastGrowth(ch));
        getSkillPool().add(new CockGrowth(ch));
        getSkillPool().add(new BreastRay(ch));
        getSkillPool().add(new FootSmother(ch));
        getSkillPool().add(new FootWorship(ch));
        getSkillPool().add(new BreastWorship(ch));
        getSkillPool().add(new CockWorship(ch));
        getSkillPool().add(new PussyWorship(ch));
        getSkillPool().add(new SuccubusSurprise(ch));
        getSkillPool().add(new TemptressHandjob(ch));
        getSkillPool().add(new TemptressBlowjob(ch));
        getSkillPool().add(new TemptressRide(ch));
        getSkillPool().add(new TemptressStripTease(ch));
        getSkillPool().add(new Blindside(ch));
        getSkillPool().add(new LeechSeed(ch));
        getSkillPool().add(new Beg(ch));
        getSkillPool().add(new Cowardice(ch));
        getSkillPool().add(new Dive(ch));
        getSkillPool().add(new Offer(ch));
        getSkillPool().add(new ShamefulDisplay(ch));
        getSkillPool().add(new Stumble(ch));
        getSkillPool().add(new TortoiseWrap(ch));
        getSkillPool().add(new FaerieSwarm(ch));
        getSkillPool().add(new DarkTalisman(ch));
        getSkillPool().add(new HeightenSenses(ch));
        getSkillPool().add(new LewdSuggestion(ch));
        getSkillPool().add(new Suggestion(ch));
        getSkillPool().add(new ImbueFetish(ch));
        getSkillPool().add(new AssJob(ch));
        getSkillPool().add(new TailSuck(ch));
        getSkillPool().add(new ToggleSlimeCock(ch));
        getSkillPool().add(new ToggleSlimePussy(ch));
        getSkillPool().add(new Spores(ch));
        getSkillPool().add(new EngulfedFuck(ch));
        getSkillPool().add(new Pray(ch));
        getSkillPool().add(new Prostrate(ch));
        getSkillPool().add(new DarkKiss(ch));
        getSkillPool().add(new MimicAngel(ch));
        getSkillPool().add(new MimicCat(ch));
        getSkillPool().add(new MimicDryad(ch));
        getSkillPool().add(new MimicSuccubus(ch));
        getSkillPool().add(new MimicWitch(ch));
        getSkillPool().add(new Parasite(ch));
        getSkillPool().add(new Bite(ch));
        getSkillPool().add(new PlaceBlindfold(ch));
        getSkillPool().add(new RipBlindfold(ch));
        getSkillPool().add(new ToggleBlindfold(ch));
        getSkillPool().add(new BunshinAssault(ch));
        getSkillPool().add(new BunshinService(ch));
        getSkillPool().add(new GoodnightKiss(ch));
        getSkillPool().add(new NeedleThrow(ch));
        getSkillPool().add(new StealClothes(ch));
        getSkillPool().add(new Substitute(ch));
        getSkillPool().add(new AttireShift(ch));
        getSkillPool().add(new CheapShot(ch));
        getSkillPool().add(new EmergencyJump(ch));
        getSkillPool().add(new Haste(ch));
        getSkillPool().add(new Rewind(ch));
        getSkillPool().add(new Unstrip(ch));
        getSkillPool().add(new WindUp(ch));


        if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
            getSkillPool().add(new SelfStun(ch));
        }
    }

    public static void buildActionPool() {
        actionPool = new HashSet<>();
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
        trapPool = new HashSet<>();
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
        featPool = new HashSet<>();
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

        File customModFile = new File("data/customModifiers.json");
        if (customModFile.canRead()) {
            try {
                JsonArray array = JsonUtils.rootJson(Files.newBufferedReader(customModFile.toPath())).getAsJsonArray();
                for (JsonElement element : array) {
                    JsonObject object;
                    try {
                        object = element.getAsJsonObject();
                    } catch (Exception e) {
                        System.out.println("Error loading custom modifiers: Non-object element in root array");
                        continue;
                    }
                    Modifier mod = CustomModifierLoader.readModifier(object);
                    if (!mod.name().equals("DEMO"))
                        modifierPool.add(mod);
                    if (isDebugOn(DebugFlags.DEBUG_LOADING))
                        System.out.println("Loaded custom modifier: " + mod.name());
                }
            } catch (IOException e) {
                System.out.println("Error loading custom modifiers: " + e);
                e.printStackTrace();
            }
        }
        if (isDebugOn(DebugFlags.DEBUG_LOADING))
            System.out.println("Done loading modifiers");
    }

    public static Set<Action> getActions() {
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

    public static void startDay() {
        match = null;
        day = new Daytime(human);
        day.plan();
    }

    public static void endNight() {
        double level = 0;
        int maxLevelTracker = 0;

        for (Character player : players) {
            player.getStamina().fill();
            player.getArousal().empty();
            player.getMojo().empty();
            player.change();
            level += player.getLevel();
            if (!player.has(Trait.unnaturalgrowth) && !player.has(Trait.naturalgrowth)) {
                maxLevelTracker = Math.max(player.getLevel(), maxLevelTracker);
            }
        }
        final int maxLevel = maxLevelTracker / players.size();
        players.stream().filter(c -> c.has(Trait.naturalgrowth)).filter(c -> c.getLevel() < maxLevel + 2).forEach(c -> {
            while (c.getLevel() < maxLevel + 2) {
                c.ding();
            }
        });
        players.stream().filter(c -> c.has(Trait.unnaturalgrowth)).filter(c -> c.getLevel() < maxLevel + 5)
                        .forEach(c -> {
                            while (c.getLevel() < maxLevel + 5) {
                                c.ding();
                            }
                        });

        level /= players.size();

        for (Character rested : resting) {
            rested.gainXP(100 + Math.max(0, (int) Math.round(10 * (level - rested.getLevel()))));
        }
        date++;
        time = Time.DAY;
        if (Global.checkFlag(Flag.autosave)) {
            Global.autoSave();
        }
        startDay();
    }
    
    private static Set<Character> pickCharacters(Set<Character> avail, Set<Character> added, int size) {
        List<Character> randomizer = avail.stream()
                        .filter(c -> !c.human())
                        .filter(c -> !c.has(Trait.event))
                        .filter(c -> !added.contains(c))
                        .collect(Collectors.toList());
        Collections.shuffle(randomizer);
        Set<Character> results = new HashSet<>(added);
        results.addAll(randomizer.subList(0, Math.min(Math.max(0, size - results.size())+1, randomizer.size())));
        return results;
    }

    public static void endDay() {
        day = null;
        time = Time.NIGHT;
        if (checkFlag(Flag.autosave)) {
            autoSave();
        }
        startNight();
    }

    public static void startNight() {
        decideMatchType().buildPrematch(human);
    }

    public static void setUpMatch(Modifier matchmod) {
        assert day == null;
        Set<Character> lineup = new HashSet<>(debugChars);
        Character lover = null;
        int maxaffection = 0;
        unflag(Flag.FTC);
        for (Character player : players) {
            player.getStamina().fill();
            player.getArousal().empty();
            player.getMojo().empty();
            player.getWillpower().fill();
            if (player.getPure(Attribute.Science) > 0) {
                player.chargeBattery();
            }
            if (human.getAffection(player) > maxaffection && !player.has(Trait.event)) {
                maxaffection = human.getAffection(player);
                lover = player;
            }
        }
        List<Character> participants = new ArrayList<>();
        // Disable characters flagged as disabled
        for (Character c : players) {
            // Disabling the player wouldn't make much sense, and there's no PlayerDisabled flag.
            Flag disabledFlag = null;
            String flagName = c.getType() + "Disabled";
            if (!c.getType().equals("Player") && Flag.exists(flagName)) {
                disabledFlag = Flag.valueOf(flagName);
            }
            if (disabledFlag == null || !Global.checkFlag(disabledFlag)) {
                // TODO: DEBUG
               // if (c.getName().contains("Reyka") || c.human()) {
                    participants.add(c);
              //}
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
            lineup = pickCharacters(players, lineup, 4);
            if (!checkFlag(Flag.Maya)) {
                newChallenger(new Maya(human.getLevel()));
                flag(Flag.Maya);
            }
            NPC maya = Optional.ofNullable(getNPC("Maya")).orElseThrow(() -> new IllegalStateException(
                            "Maya data unavailable when attempting to add her to lineup."));
            lineup.add(maya);
            resting = new HashSet<>(players);
            resting.removeAll(lineup);
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
            lineup = pickCharacters(players, lineup, 4);
            resting = new HashSet<>(players);
            resting.removeAll(lineup);
            match = buildMatch(lineup, matchmod);
        } else if (participants.size() > 5) {
            if (lover != null) {
                lineup.add(lover);
            }
            lineup.add(human);
            lineup = pickCharacters(players, lineup, 4);
            resting = new HashSet<>(players);
            resting.removeAll(lineup);
            match = buildMatch(lineup, matchmod);
        } else {
            match = buildMatch(participants, matchmod);
        }
        startMatch();
    }

    public static void startMatch() {
        Global.getPlayer().getAddictions().forEach(a -> {
            Optional<Status> withEffect = a.startNight();
            withEffect.ifPresent(s -> Global.getPlayer().add(s));
        });
        Global.gui().startMatch();
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
                        Movement.quad, new MapDrawHint(new Rectangle(10, 3, 7, 9), "Quad", false));
        Area dorm = new Area("Dorm",
                        "You are in the <b>Dorm</b>. Everything is quieter than it would be in any other dorm this time of night. You've been told the entire first floor "
                                        + "is empty during match hours, but you wouldn't be surprised if a few of the residents are hiding in their rooms, peeking at the fights. You've stashed some clothes "
                                        + "in one of the rooms you're sure is empty, which is common practice for most of the competitors.",
                        Movement.dorm, new MapDrawHint(new Rectangle(14, 12, 3, 5), "Dorm", false));
        Area shower = new Area("Showers",
                        "You are in the first floor <b>Showers</b>. There are a half-dozen stalls shared by the residents on this floor. They aren't very big, but there's "
                                        + "room to hide if need be. A hot shower would help you recover after a tough fight, but you'd be vulnerable if someone finds you.",
                        Movement.shower, new MapDrawHint(new Rectangle(13, 17, 4, 2), "Showers", false));
        Area laundry = new Area("Laundry Room",
                        "You are in the <b>Laundry Room</b> in the basement of the Dorm. Late night is prime laundry time in your dorm, but none of these machines "
                                        + "are running. You're a bit jealous when you notice that the machines here are free, while yours are coin-op. There's a tunnel here that connects to the basement of the "
                                        + "Dining Hall.",
                        Movement.laundry, new MapDrawHint(new Rectangle(17, 15, 8, 2), "Laundry", false));
        Area engineering = new Area("Engineering Building",
                        "You are in the Science and <b>Engineering Building</b>. Most of the lecture rooms are in other buildings; this one is mostly "
                                        + "for specialized rooms and labs. The first floor contains workshops mostly used by the Mechanical and Electrical Engineering classes. The second floor has "
                                        + "the Biology and Chemistry Labs. There's a third floor, but that's considered out of bounds.",
                        Movement.engineering, new MapDrawHint(new Rectangle(10, 0, 7, 3), "Eng", false));
        Area lab = new Area("Chemistry Lab",
                        "You are in the <b>Chemistry Lab</b>. The shelves and cabinets are full of all manner of dangerous and/or interesting chemicals. A clever enough "
                                        + "person could combine some of the safer ones into something useful. Just outside the lab is a bridge connecting to the library.",
                        Movement.lab, new MapDrawHint(new Rectangle(0, 0, 10, 3), "Lab", false));
        Area workshop = new Area("Workshop",
                        "You are in the Mechanical Engineering <b>Workshop</b>. There are shelves of various mechanical components and the back table is covered "
                                        + "with half-finished projects. A few dozen Mechanical Engineering students use this workshop each week, but it's well stocked enough that no one would miss "
                                        + "some materials that might be of use to you.",
                        Movement.workshop, new MapDrawHint(new Rectangle(17, 0, 8, 3), "Workshop", false));
        Area libarts = new Area("Liberal Arts Building",
                        "You are in the <b>Liberal Arts Building</b>. There are three floors of lecture halls and traditional classrooms, but only "
                                        + "the first floor is in bounds. The Library is located directly out back, and the side door is just a short walk from the pool.",
                        Movement.la, new MapDrawHint(new Rectangle(5, 5, 5, 7), "L&A", false));
        Area pool = new Area("Pool",
                        "You are by the indoor <b>Pool</b>, which is connected to the Student Union for reasons that no one has ever really explained. The pool here is quite "
                                        + "large and there is even a jacuzzi. A quick soak would feel good, but the lack of privacy is a concern. The side doors are locked at this time of night, but the "
                                        + "door to the Student Union is open and there's a back door that exits near the Liberal Arts building. Across the water in the other direction is the Courtyard.",
                        Movement.pool, new MapDrawHint(new Rectangle(6, 12, 4, 2), "Pool", false));
        Area library = new Area("Library",
                        "You are in the <b>Library</b>. It's a two floor building with an open staircase connecting the first and second floors. The front entrance leads to "
                                        + "the Liberal Arts building. The second floor has a Bridge connecting to the Chemistry Lab in the Science and Engineering building.",
                        Movement.library, new MapDrawHint(new Rectangle(0, 8, 5, 12), "Library", false));
        Area dining = new Area("Dining Hall",
                        "You are in the <b>Dining Hall</b>. Most students get their meals here, though some feel it's worth the extra money to eat out. The "
                                        + "dining hall is quite large and your steps echo on the linoleum, but you could probably find someplace to hide if you need to.",
                        Movement.dining, new MapDrawHint(new Rectangle(17, 6, 4, 6), "Dining", false));
        Area kitchen = new Area("Kitchen",
                        "You are in the <b>Kitchen</b> where student meals are prepared each day. The industrial fridge and surrounding cabinets are full of the "
                                        + "ingredients for any sort of bland cafeteria food you can imagine. Fortunately, you aren't very hungry. There's a chance you might be able to cook up some "
                                        + "of the more obscure items into something useful.",
                        Movement.kitchen, new MapDrawHint(new Rectangle(18, 12, 4, 2), "Kitchen", false));
        Area storage = new Area("Storage Room",
                        "You are in a <b>Storage Room</b> under the Dining Hall. It's always unlocked and receives a fair bit of foot traffic from students "
                                        + "using the tunnel to and from the Dorm, so no one keeps anything important in here. There's enough junk down here to provide some hiding places and there's a chance "
                                        + "you could find something useable in one of these boxes.",
                        Movement.storage, new MapDrawHint(new Rectangle(21, 6, 4, 5), "Storage", false));
        Area tunnel = new Area("Tunnel",
                        "You are in the <b>Tunnel</b> connecting the dorm to the dining hall. It doesn't get a lot of use during the day and most of the freshmen "
                                        + "aren't even aware of its existence, but many upperclassmen have been thankful for it on cold winter days and it's proven to be a major tactical asset. The "
                                        + "tunnel is well-lit and doesn't offer any hiding places.",
                        Movement.tunnel, new MapDrawHint(new Rectangle(23, 11, 2, 4), "Tunnel", true));
        Area bridge = new Area("Bridge",
                        "You are on the <b>Bridge</b> connecting the second floors of the Science and Engineering Building and the Library. It's essentially just a "
                                        + "corridor, so there's no place for anyone to hide.",
                        Movement.bridge, new MapDrawHint(new Rectangle(0, 3, 2, 5), "Bridge", true));
        Area sau = new Area("Student Union",
                        "You are in the <b>Student Union</b>, which doubles as base of operations during match hours. You and the other competitors can pick up "
                                        + "a change of clothing here.",
                        Movement.union, new MapDrawHint(new Rectangle(10, 12, 3, 5), "S.Union", true));
        Area courtyard = new Area("Courtyard",
                        "You are in the <b>Courtyard</b>. "
                                        + "It's a small clearing behind the school pool. There's not much to see here except a tidy garden maintained by the botany department.",
                        Movement.courtyard, new MapDrawHint(new Rectangle(6, 14, 3, 6), "Courtyard", true));
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
        lab.jump(dining);
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
        bridge.jump(quad);
        sau.link(pool);
        sau.link(quad);
        workshop.shortcut(pool);
        pool.shortcut(workshop);
        library.shortcut(tunnel);
        tunnel.shortcut(library);
        HashMap<String, Area> map = new HashMap<>();
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

    public static void autoSave() {
        save(new File("./auto.ngs"));
    }

    public static void saveWithDialog() {
        Optional<File> file = gui().askForSaveFile();
        if (file.isPresent()) {
            save(file.get());
        }
    }

    protected static SaveData saveData() {
        SaveData data = new SaveData();
        data.players.addAll(players);
        data.flags.addAll(flags);
        data.counters.putAll(counters);
        data.time = time;
        data.date = date;
        return data;
    }

    public static void save(File file) {
        SaveData data = saveData();
        JsonObject saveJson = data.toJson();

        try (JsonWriter saver = new JsonWriter(new FileWriter(file))) {
            saver.setIndent("  ");
            JsonUtils.gson.toJson(saveJson, saver);
        } catch (IOException | JsonIOException e) {
            System.err.println("Could not save file " + file + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Optional<NpcConfiguration> findNpcConfig(String type, Optional<StartConfiguration> startConfig) {
        return startConfig.isPresent() ? startConfig.get().findNpcConfig(type) : Optional.empty();
    }

    public static void rebuildCharacterPool(Optional<StartConfiguration> startConfig) {
        characterPool = new HashMap<>();
        debugChars.clear();

        Optional<NpcConfiguration> commonConfig =
                        startConfig.isPresent() ? Optional.of(startConfig.get().npcCommon) : Optional.empty();

        try (InputStreamReader reader = new InputStreamReader(
                        ResourceLoader.getFileResourceAsStream("characters/included.json"))) {
            JsonArray characterSet = JsonUtils.rootJson(reader).getAsJsonArray();
            for (JsonElement element : characterSet) {
                String name = element.getAsString();
                try {
                    NPCData data = JsonSourceNPCDataLoader
                                    .load(ResourceLoader.getFileResourceAsStream("characters/" + name));
                    Optional<NpcConfiguration> npcConfig = findNpcConfig(CustomNPC.TYPE_PREFIX + data.getName(), startConfig);
                    Personality npc = new CustomNPC(data, npcConfig, commonConfig);
                    characterPool.put(npc.getCharacter().getType(), npc.getCharacter());
                    System.out.println("Loaded " + name);
                } catch (JsonParseException e1) {
                    System.err.println("Failed to load NPC " + name);
                    e1.printStackTrace();
                }
            }
        } catch (JsonParseException | IOException e1) {
            System.err.println("Failed to load character set");
            e1.printStackTrace();
        }

        // TODO: Refactor into function and unify with CustomNPC handling.
        Personality cassie = new Cassie(findNpcConfig("Cassie", startConfig), commonConfig);
        Personality angel = new Angel(findNpcConfig("Angel", startConfig), commonConfig);
        Personality reyka = new Reyka(findNpcConfig("Reyka", startConfig), commonConfig);
        Personality kat = new Kat(findNpcConfig("Kat", startConfig), commonConfig);
        Personality mara = new Mara(findNpcConfig("Mara", startConfig), commonConfig);
        Personality jewel = new Jewel(findNpcConfig("Jewel", startConfig), commonConfig);
        Personality airi = new Airi(findNpcConfig("Airi", startConfig), commonConfig);
        Personality eve = new Eve(findNpcConfig("Eve", startConfig), commonConfig);
        Personality maya = new Maya(1, findNpcConfig("Maya", startConfig), commonConfig);
        Personality yui = new Yui(findNpcConfig("Yui", startConfig), commonConfig);
        characterPool.put(cassie.getCharacter().getType(), cassie.getCharacter());
        characterPool.put(angel.getCharacter().getType(), angel.getCharacter());
        characterPool.put(reyka.getCharacter().getType(), reyka.getCharacter());
        characterPool.put(kat.getCharacter().getType(), kat.getCharacter());
        characterPool.put(mara.getCharacter().getType(), mara.getCharacter());
        characterPool.put(jewel.getCharacter().getType(), jewel.getCharacter());
        characterPool.put(airi.getCharacter().getType(), airi.getCharacter());
        characterPool.put(eve.getCharacter().getType(), eve.getCharacter());
        characterPool.put(maya.getCharacter().getType(), maya.getCharacter());
        characterPool.put(yui.getCharacter().getType(), yui.getCharacter());


        //debugChars.add(mara.getCharacter());
    }

    public static void loadWithDialog() {
        JFileChooser dialog = new JFileChooser("./");
        FileFilter savesFilter = new FileNameExtensionFilter("Nightgame Saves", "ngs");
        dialog.addChoosableFileFilter(savesFilter);
        dialog.setFileFilter(savesFilter);
        dialog.setMultiSelectionEnabled(false);
        int rv = dialog.showOpenDialog(gui);
        if (rv != JFileChooser.APPROVE_OPTION) {
            return;
        }
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
        load(file);
    }

    protected static void resetForLoad() {
        players.clear();
        flags.clear();
        gui.clearText();
        human = new Player("Dummy");
        gui.purgePlayer();
        buildSkillPool(human);
        Clothing.buildClothingTable();
        rebuildCharacterPool(Optional.empty());
        day = null;
    }

    public static void load(File file) {
        resetForLoad();

        JsonObject object;
        try (Reader loader = new InputStreamReader(new FileInputStream(file))) {
            object = new JsonParser().parse(loader).getAsJsonObject();

        } catch (IOException e) {
            e.printStackTrace();
            // Couldn't load data; just get out
            return;
        }
        SaveData data = new SaveData(object);
        loadData(data);
        gui.populatePlayer(human);
        if (time == Time.DAY) {
            startDay();
        } else {
            startNight();
        }
    }

    /**
     * Loads game state data into static fields from SaveData object.
     *
     * @param data A SaveData object, as loaded from save files.
     */
    protected static void loadData(SaveData data) {
        players.addAll(data.players);
        flags.addAll(data.flags);
        counters.putAll(data.counters);
        date = data.date;
        time = data.time;
    }

    public static Set<Character> everyone() {
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
            if (c.getType().equalsIgnoreCase(name)) {
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
        return "You don't really know why you're going to the Student Union in the middle of the night."
                        + " You'd have to be insane to accept the invitation you received this afternoon."
                        + " Seriously, someone is offering you money to sexfight a bunch of other students?"
                        + " You're more likely to get mugged (though you're not carrying any money) or murdered if you show up."
                        + " Best case scenario, it's probably a prank for gullible freshmen."
                        + " You have no good reason to believe the invitation is on the level, but here you are, walking into the empty Student Union."
                        + "\n\n" + "Not quite empty, it turns out."
                        + " The same woman who approached you this afternoon greets you and brings you to a room near the back of the building."
                        + " Inside, you're surprised to find three quite attractive girls."
                        + " After comparing notes, you confirm they're all freshmen like you and received the same invitation today."
                        + " You're surprised, both that these girls would agree to such an invitation."
                        + " For the first time, you start to believe that this might actually happen."
                        + " After a few minutes of awkward small talk (though none of these girls seem self-conscious about being here), the woman walks in again leading another girl."
                        + " Embarrassingly you recognize the girl, named Cassie, who is a classmate of yours, and who you've become friends with over the past couple weeks."
                        + " She blushes when she sees you and the two of you consciously avoid eye contact while the woman explains the rules of the competition."
                        + "\n\n" + "There are a lot of specific points, but the rules basically boil down to this: "
                        + " competitors move around the empty areas of the campus and engage each other in sexfights."
                        + " When one competitor orgasms and doesn't have the will to go on, the other gets a point and can claim the loser's clothes."
                        + " Those two players are forbidden to engage again until the loser gets a replacement set of clothes at either the Student Union or the first floor of the dorm building."
                        + " It seems to be customary, but not required, for the loser to get the winner off after a fight, when it doesn't count."
                        + " After three hours, the match ends and each player is paid for each opponent they defeat, each set of clothes turned in, and a bonus for whoever scores the most points."
                        + "\n\n"
                        + "After the explanation, she confirms with each participant whether they are still interested in participating."
                        + " Everyone agrees." + " The first match starts at exactly 10:00.";
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
        if (arr.length == 0) return null;
        return arr[Global.random(arr.length)];
    }
    
    public static <T> Optional<T> pickRandom(List<T> list) {
        if (list.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(list.get(random(list.size())));
        }
    }

    public static int getDate() {
        return date;
    }

    interface MatchAction {
        String replace(Character self, String first, String second, String third);
    }

    private static HashMap<String, MatchAction> matchActions = null;

    public static void buildParser() {
        matchActions = new HashMap<>();
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

        matchActions.put("main-genitals", (self, first, second, third) -> {
            if (self != null) {
                if (self.hasDick()) {
                    return "dick";
                } else if (self.hasPussy()) {
                    return "pussy";
                } else {
                    return "crotch";
                }
            }
            return "";
        });
    }

    public static String format(String format, Character self, Character target, Object... strings) {
        // pattern to find stuff like {word:otherword:finalword} in strings
        Pattern p = Pattern.compile("\\{((?:self)|(?:other))(?::([^:}]+))?(?::([^:}]+))?\\}");
        format = String.format(format, strings);

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
            if (action != null) {
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

    public static Set<Skill> getSkillPool() {
        return skillPool;
    }

    public static Set<Modifier> getModifierPool() {
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

    public static int clamp(int number, int min, int max) {
        return Math.min(Math.max(number, min), max);
    }

    public static double clamp(double number, double min, double max) {
        return Math.min(Math.max(number, min), max);
    }

    public static long randomlong() {
        return rng.nextLong();
    }
}
