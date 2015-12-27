package nightgames.characters;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import nightgames.actions.Move;
import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.PussyPart;
import nightgames.characters.custom.AiModifiers;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.combat.Result;
import nightgames.ftc.FTCMatch;
import nightgames.global.Challenge;
import nightgames.global.DebugFlags;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.items.Item;
import nightgames.items.Loot;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.items.clothing.Outfit;
import nightgames.pet.Pet;
import nightgames.skills.Nothing;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.stance.Position;
import nightgames.stance.Stance;
import nightgames.status.Alluring;
import nightgames.status.DivineCharge;
import nightgames.status.Enthralled;
import nightgames.status.Resistance;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.status.Trance;
import nightgames.trap.Trap;

public abstract class Character extends Observable implements Cloneable {
	/**
	 *
	 */
	protected String						name;
	protected int							level;
	protected int							xp;
	protected int							rank;
	public int								money;
	public HashMap<Attribute, Integer>		att;
	protected Meter							stamina;
	protected Meter							arousal;
	protected Meter							mojo;
	protected Meter							willpower;
	public Outfit							outfit;
	public List<Clothing>					outfitPlan;
	protected Area							location;
	protected HashSet<Skill>				skills;
	public HashSet<Status>					status;
	public Set<Trait>						traits;
	protected HashMap<Trait, Integer>		temporaryAddedTraits;
	protected HashMap<Trait, Integer>		temporaryRemovedTraits;
	public HashSet<Status>					removelist;
	public HashSet<Status>					addlist;
	public HashMap<String, Integer>			cooldowns;
	private HashSet<Character>				mercy;
	protected Map<Item, Integer>			inventory;
	private HashMap<String, Integer>		flags;
	protected Item							trophy;
	public State							state;
	protected int							busy;
	protected HashMap<Character, Integer>	attractions;
	protected HashMap<Character, Integer>	affections;
	public HashSet<Clothing>				closet;
	public Pet								pet;
	public ArrayList<Challenge>				challenges;
	public Body								body;
	public int								availableAttributePoints;
	public boolean							orgasmed;
	public boolean							custom;
	private boolean							pleasured;
	public int								orgasms;
	public int								cloned;

	public Character(String name, int level) {
		this.name = name;
		this.level = level;
		cloned = 0;
		custom = false;
		body = new Body(this);
		att = new HashMap<Attribute, Integer>();
		cooldowns = new HashMap<String, Integer>();
		flags = new HashMap<String, Integer>();
		att.put(Attribute.Power, 5);
		att.put(Attribute.Cunning, 5);
		att.put(Attribute.Seduction, 5);
		att.put(Attribute.Perception, 5);
		att.put(Attribute.Speed, 5);
		money = 0;
		stamina = new Meter(22 + 3 * level);
		stamina.fill();
		arousal = new Meter(90 + 10 * level);
		mojo = new Meter(33 + 2 * level);
		willpower = new Meter(40);
		orgasmed = false;
		pleasured = false;

		outfit = new Outfit();
		outfitPlan = new ArrayList<Clothing>();

		closet = new HashSet<Clothing>();
		skills = new HashSet<Skill>();
		status = new HashSet<Status>();
		traits = new TreeSet<Trait>(
				(a, b) -> a.toString().compareTo(b.toString()));
		temporaryAddedTraits = new HashMap<Trait, Integer>();
		temporaryRemovedTraits = new HashMap<Trait, Integer>();
		removelist = new HashSet<Status>();
		addlist = new HashSet<Status>();
		mercy = new HashSet<Character>();
		inventory = new HashMap<Item, Integer>();
		attractions = new HashMap<Character, Integer>();
		affections = new HashMap<Character, Integer>();
		challenges = new ArrayList<Challenge>();
		location = new Area("", "", null);
		state = State.ready;
		busy = 0;
		setRank(0);
		pet = null;
		Global.learnSkills(this);
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public Character clone() throws CloneNotSupportedException {
		Character c = (Character) super.clone();
		c.att = (HashMap<Attribute, Integer>) att.clone();
		c.stamina = stamina.clone();
		c.cloned = cloned + 1;
		c.arousal = arousal.clone();
		c.mojo = mojo.clone();
		c.willpower = willpower.clone();
		c.outfitPlan = new ArrayList<Clothing>(outfitPlan);
		c.outfit = new Outfit(outfit);
		c.skills = (HashSet<Skill>) skills.clone();
		c.status = new HashSet<Status>();
		c.flags = new HashMap<>(flags);
		for (Status s : status) {
			Status clone = s.instance(c, c);
			c.status.add(clone);
		}
		c.traits = new TreeSet<Trait>(traits);
		c.temporaryAddedTraits = new HashMap<Trait, Integer>(
				temporaryAddedTraits);
		c.temporaryRemovedTraits = new HashMap<Trait, Integer>(
				temporaryRemovedTraits);
		c.removelist = (HashSet<Status>) removelist.clone();
		c.addlist = (HashSet<Status>) addlist.clone();
		c.mercy = (HashSet<Character>) mercy.clone();
		c.inventory = (Map<Item, Integer>) ((HashMap<Item, Integer>) inventory)
				.clone();
		c.attractions = (HashMap<Character, Integer>) attractions.clone();
		c.affections = (HashMap<Character, Integer>) affections.clone();
		c.skills = (HashSet<Skill>) skills.clone();
		c.body = body.clone();
		c.body.character = c;
		c.orgasmed = orgasmed;
		return c;
	}

	public void finishClone(Character other) {
		HashSet<Status> oldstatus = status;
		status = new HashSet<Status>();
		for (Status s : oldstatus) {
			status.add(s.instance(this, other));
		}
	}

	public String name() {
		return name;
	}

	public List<Resistance> getResistances() {
		List<Resistance> resists = new ArrayList<>();
		for (Trait t : traits) {
			resists.add(Trait.getResistance(t));
		}
		return resists;
	}

	public int getXPReqToNextLevel() {
		return Math.min(45 + 5 * getLevel(), 100);
	}

	public int get(Attribute a) {
		int total = getPure(a);
		for (Status s : getStatuses()) {
			total += s.mod(a);
		}
		total += body.mod(a, total);
		switch (a) {
			case Arcane:
				if (outfit.has(ClothingTrait.mystic)) {
					total += 2;
				}
				break;
			case Dark:
				if (outfit.has(ClothingTrait.broody)) {
					total += 2;
				}
				break;
			case Ki:
				if (outfit.has(ClothingTrait.martial)) {
					total += 2;
				}
				break;
			case Fetish:
				if (outfit.has(ClothingTrait.kinky)) {
					total += 2;
				}
				break;
			case Power:
				if (has(Trait.testosterone) && hasDick()) {
					total += Math.min(20, 10 + getLevel() / 4);
				}
				break;
			case Science:
				if (has(ClothingTrait.geeky)) {
					total += 2;
				}
				break;
			case Speed:
				if (has(ClothingTrait.bulky)) {
					total -= 1;
				}
				if (has(ClothingTrait.shoes)) {
					total += 1;
				}
				if (has(ClothingTrait.heels) && !has(Trait.proheels)) {
					total -= 2;
				}
				if (has(ClothingTrait.highheels) && !has(Trait.proheels)) {
					total -= 1;
				}
				if (has(ClothingTrait.higherheels) && !has(Trait.proheels)) {
					total -= 1;
				}
			default:
				break;
		}
		return total;
	}

	public boolean has(ClothingTrait attribute) {
		return outfit.has(attribute);
	}

	public int getPure(Attribute a) {
		int total = 0;
		if (att.containsKey(a) && !a.equals(Attribute.Willpower)) {
			total = att.get(a);
		}
		return total;
	}

	public void mod(Attribute a, int i) {
		if (a.equals(Attribute.Willpower)) {
			getWillpower().gain(i * 3);
			return;
		}
		if (att.containsKey(a)) {
			att.put(a, att.get(a) + i);
		} else {
			set(a, i);
		}
	}

	public void set(Attribute a, int i) {
		att.put(a, i);
	}

	public boolean check(Attribute a, int dc) {
		int rand = Global.random(20);
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
			System.out.println("Checked " + a + " = " + get(a) + " against "
					+ dc + ", rolled " + rand);
		}
		if (rand == 0) {
			// critical hit, don't check
			return true;
		}
		if (get(a) == 0) {
			return false;
		} else {
			return get(a) + rand >= dc;
		}
	}

	public int getLevel() {
		return level;
	}

	public void gainXP(int i) {
		assert i >= 0;
		double rate = 1.0;
		if (has(Trait.fastLearner)) {
			rate += .2;
		}
		rate *= Global.xpRate;
		xp += Math.round(i * rate);
		update();
	}

	public void setXP(int i) {
		xp = i;
		update();
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void rankup() {
		rank++;
	}

	public abstract void ding();

	public String dong() {
		level--;
		String message = loseRandomAttributes(Global.random(3) + 2);
		if (countFeats() > level / 4) {
			message += loseFeat();
		}

		getStamina().gain(-2);
		getArousal().gain(-4);
		getMojo().gain(-1);
		return message + Global.gainSkills(this);
	}

	public String loseRandomAttributes(int number) {
		ArrayList<Attribute> avail = new ArrayList<Attribute>();
		String message = "";
		while (number > 0) {
			avail.clear();
			for (Attribute a : att.keySet()) {
				if (getPure(a) > 1 && a != Attribute.Speed
						&& a != Attribute.Perception) {
					avail.add(a);
				}
			}
			if (avail.size() == 0) {
				break;
			}
			Attribute removed = avail.get(Global.random(avail.size()));
			mod(removed, -1);
			if (human()) {
				message += "You've lost a point in " + removed.toString()
						+ ".<br>";
			}
			number -= 1;
		}
		return message;
	}

	public String loseFeat() {
		String string = "";
		ArrayList<Trait> available = new ArrayList<Trait>();
		for (Trait feat : Global.getFeats(this)) {
			if (has(feat)) {
				available.add(feat);
			}
		}
		Trait removed = available.get(Global.random(available.size()));
		if (human()) {
			string += "You've lost " + removed.toString() + ".<br>";
		}
		remove(removed);
		return string;
	}

	public int getXP() {
		return xp;
	}

	public void pain(Combat c, int i) {
		pain(c, i, true, true);
	}

	public void pain(Combat c, int i, boolean primary) {
		pain(c, i, primary, true);
	}

	public void pain(Combat c, int i, boolean primary, boolean physical) {
		int pain = i;
		int bonus = 0;
		if (is(Stsflag.rewired) && physical) {
			String message = String.format(
					"%s pleasured for <font color='rgb(255,50,200)'>%d<font color='white'>\n",
					Global.capitalizeFirstLetter(subjectWas()), pain);
			if (c != null) {
				c.writeSystemMessage(message);
			}
			pleasure(pain, c);
			return;
		}
		if (has(Trait.slime)) {
			bonus -= pain / 2;
			if (c != null) {
				c.write(this, "The blow glances off "
						+ nameOrPossessivePronoun() + " slimy body.");
			}
		}
		if (c != null) {
			Character other = c.getOther(this);
			if (has(Trait.cute) && other != null && primary && physical) {
				bonus -= Math.min(get(Attribute.Seduction), 50) * pain / 100;
				c.write(this,
						Global.format(
								"{self:NAME-POSSESSIVE} innocent appearance throws {other:direct-object} off and {other:subject-action:use|uses} much less strength than intended.",
								this, other));
			}

			for (Status s : getStatuses()) {
				bonus += s.damage(c, pain);
			}
		}
		pain += bonus;
		pain = Math.max(1, pain);
		emote(Emotion.angry, pain / 3);

		// threshold at which pain calms you down
		int painAllowance = Math.max(10, getStamina().max() / 25);
		if (c != null && c.getOther(this).has(Trait.wrassler)) {
			painAllowance *= 1.5;
		}
		int difference = pain - painAllowance;
		// if the pain exceeds the threshold and you aren't a masochist
		// calm down by the overflow

		if (c != null) {
			c.writeSystemMessage(String.format(
					"%s hurt for <font color='rgb(250,10,10)'>%d<font color='white'>",
					subjectWas(), pain));
		}
		if (difference > 0 && !is(Stsflag.masochism)) {
			if (c != null && c.getOther(this).has(Trait.wrassler)) {
				calm(c, difference / 2);
			} else {
				calm(c, difference);
			}
		}
		// if you are a masochist, arouse by pain up to the threshold.
		if (is(Stsflag.masochism) && physical) {
			this.arouse(Math.max(i, painAllowance), c);
		}
		stamina.reduce(pain);
	}

	public void drain(Combat c, Character drainer, int i) {
		int drained = i;
		int bonus = 0;

		for (Status s : getStatuses()) {
			bonus += s.drained(drained);
		}
		drained += bonus;
		if (drained >= stamina.get()) {
			drained = stamina.get();
		}
		drained = Math.max(drained, i);
		if (c != null) {
			c.writeSystemMessage(String.format(
					"%s drained of <font color='rgb(200,200,200)'>%d<font color='white'> stamina by %s",
					subjectWas(), drained, drainer.subject()));
		}
		stamina.reduce(drained);
		drainer.stamina.restore(drained);
	}

	public void weaken(Combat c, int i) {
		int weak = i;
		int bonus = 0;
		for (Status s : getStatuses()) {
			bonus += s.weakened(i);
		}
		weak += bonus;
		if (weak >= stamina.get()) {
			weak = stamina.get();
		}
		i = Math.max(1, i);
		if (c != null) {
			c.writeSystemMessage(String.format(
					"%s weaked by <font color='rgb(200,200,200)'>%d<font color='white'>",
					subjectWas(), i));
		}
		stamina.reduce(weak);
	}

	public void heal(Combat c, int i) {
		i = Math.max(1, i);
		if (c != null) {
			c.writeSystemMessage(String.format(
					"%s healed for <font color='rgb(100,240,30)'>%d<font color='white'>",
					subjectWas(), i));
		}
		stamina.restore(i);
	}

	public String subject() {
		return name();
	}

	public void pleasure(int i, Combat c) {
		int pleasure = i;

		emote(Emotion.horny, i / 4 + 1);
		if (pleasure < 1) {
			pleasure = 1;
		}
		pleasured = true;
		// pleasure = 0;
		arousal.restoreNoLimit(Math.round(pleasure));
	}

	public void tempt(Combat c, int i) {
		tempt(c, null, i);
	}

	public void tempt(Combat c, Character tempter, BodyPart with, int i) {
		if (tempter != null && with != null) {
			// triple multiplier for the body part
			double temptMultiplier = body.getCharismaBonus(tempter)
					+ with.getHotness(tempter, this) * 2;
			int dmg = (int) Math.round(i * temptMultiplier);
			tempt(dmg);
			String message = String.format(
					"%s tempted by %s %s for <font color='rgb(240,100,100)'>%d<font color='white'> (base:%d, charisma:%.1f)\n",
					Global.capitalizeFirstLetter(subjectWas()),
					tempter.nameOrPossessivePronoun(), with.describe(tempter),
					dmg, i, temptMultiplier);
			if (Global.isDebugOn(DebugFlags.DEBUG_DAMAGE)) {
				System.out.printf(message);
			}
			if (c != null) {
				c.writeSystemMessage(message);
			}
		}
	}

	public void tempt(Combat c, Character tempter, int i) {
		if (tempter != null) {
			double temptMultiplier = body.getCharismaBonus(tempter);
			int dmg = (int) Math.round(i * temptMultiplier);
			tempt(dmg);
			String message = String.format(
					"%s tempted %s for <font color='rgb(240,100,100)'>%d<font color='white'> (base:%d, charisma:%.1f)\n",
					Global.capitalizeFirstLetter(tempter.subject()),
					tempter == this ? reflectivePronoun() : directObject(), dmg,
					i, temptMultiplier);
			if (Global.isDebugOn(DebugFlags.DEBUG_DAMAGE)) {
				System.out.printf(message);
			}
			if (c != null) {
				c.writeSystemMessage(message);
			}
		} else {
			if (c != null) {
				c.writeSystemMessage(String.format(
						"%s tempted for <font color='rgb(240,100,100)'>%d<font color='white'>\n",
						subjectWas(), i));
			}
			tempt(i);
		}
	}

	public void arouse(int i, Combat c) {
		arouse(i, c, "");
	}

	public void arouse(int i, Combat c, String source) {
		String message = String.format(
				"%s aroused for <font color='rgb(240,100,100)'>%d<font color='white'>%s\n",
				Global.capitalizeFirstLetter(subjectWas()), i, source);
		if (c != null) {
			c.writeSystemMessage(message);
		}
		tempt(i);
	}

	public String subjectAction(String verb, String pluralverb) {
		return subject() + " " + pluralverb;
	}

	public String subjectWas() {
		return subject() + " was";
	}

	public void tempt(int i) {
		int temptation = i;
		int bonus = 0;

		for (Status s : getStatuses()) {
			bonus += s.tempted(i);
		}
		temptation += bonus;
		if (has(Trait.desensitized2)) {
			temptation /= 2;
		}
		emote(Emotion.horny, i / 4);
		arousal.restoreNoLimit(temptation);
	}

	public void calm(Combat c, int i) {
		if (c != null) {
			String message = String.format(
					"%s calmed down by <font color='rgb(80,145,200)'>%d<font color='white'>\n",
					Global.capitalizeFirstLetter(subjectAction("have", "has")),
					i);
			c.writeSystemMessage(message);
		}
		arousal.reduce(i);
	}

	public Meter getStamina() {
		return stamina;
	}

	public Meter getArousal() {
		return arousal;
	}

	public Meter getMojo() {
		return mojo;
	}

	public Meter getWillpower() {
		return willpower;
	}

	public void buildMojo(Combat c, int percent) {
		buildMojo(c, percent, "");
	}

	public void buildMojo(Combat c, int percent, String source) {
		int x = percent * Math.min(mojo.max(), 200) / 100;
		int bonus = 0;
		for (Status s : getStatuses()) {
			bonus += s.gainmojo(x);
		}
		x += bonus;
		if (x > 0) {
			mojo.restore(x);
			if (c != null) {
				c.writeSystemMessage(Global.capitalizeFirstLetter(String.format(
						"%s <font color='rgb(100,200,255)'>%d<font color='white'> mojo%s.",
						subjectAction("built", "built"), x, source)));
			}
		} else if (x < 0) {
			loseMojo(c, x);
		}
	}

	public void spendMojo(Combat c, int i) {
		spendMojo(c, i, "");
	}

	public void spendMojo(Combat c, int i, String source) {
		int cost = i;
		int bonus = 0;
		for (Status s : getStatuses()) {
			bonus += s.spendmojo(i);
		}
		cost += bonus;
		mojo.reduce(cost);
		if (mojo.get() < 0) {
			mojo.set(0);
		}
		if (c != null && i != 0) {
			c.writeSystemMessage(Global.capitalizeFirstLetter(String.format(
					"%s <font color='rgb(150,150,250)'>%d<font color='white'> mojo%s.",
					subjectAction("spent", "spent"), cost, source)));
		}
	}

	public int loseMojo(Combat c, int i) {
		return loseMojo(c, i, "");
	}

	public int loseMojo(Combat c, int i, String source) {
		int amt = Math.min(mojo.get(), i);
		mojo.reduce(amt);
		if (mojo.get() < 0) {
			mojo.set(0);
		}
		if (c != null) {
			c.writeSystemMessage(Global.capitalizeFirstLetter(String.format(
					"%s <font color='rgb(150,150,250)'>%d<font color='white'> mojo.",
					subjectAction("lost", "lost"), amt)));
		}
		return amt;
	}

	public Area location() {
		return location;
	}

	public int init() {
		return att.get(Attribute.Speed) + Global.random(10);
	}

	public boolean reallyNude() {
		return topless() && pantsless();
	}

	public boolean torsoNude() {
		return topless() && pantsless();
	}

	public boolean mostlyNude() {
		return breastsAvailable() && crotchAvailable();
	}

	public boolean breastsAvailable() {
		return outfit.slotOpen(ClothingSlot.top);
	}

	public boolean crotchAvailable() {
		return outfit.slotOpen(ClothingSlot.bottom);
	}

	public void dress(Combat c) {
		outfit.dress(c.getCombatantData(this).getClothespile());
	}

	public void change() {
		// List<Clothing> plan = outfitPlan;
		// if (rule == Modifier.underwearonly) {
		// plan = outfitPlan.stream().filter(article -> article.getLayer() ==
		// 0).collect(Collectors.toList());
		// } else if (rule == Modifier.nudist) {
		// plan = Collections.emptyList();
		// }
		// outfit.change(rule, plan);
		outfit.dress(outfitPlan);
		if (Global.getMatch() != null) {
			Global.getMatch().condition.handleOutfit(this);
		}
	}

	public String getName() {
		return name;
	}

	/* undress without any modifiers */
	public void undress(Combat c) {
		if (!breastsAvailable() || !crotchAvailable()) {
			// first time only strips down to what blocks fucking
			outfit.strip().forEach(article -> c.getCombatantData(this)
					.addToClothesPile(article));
		} else {
			// second time strips down everything
			outfit.undress().forEach(article -> c.getCombatantData(this)
					.addToClothesPile(article));
		}
	}

	/* undress non indestructibles */
	public boolean nudify() {
		if (!breastsAvailable() || !crotchAvailable()) {
			// first time only strips down to what blocks fucking
			outfit.forcedstrip();
		} else {
			// second time strips down everything
			outfit.undressOnly(c -> !c.is(ClothingTrait.indestructible));
		}
		return mostlyNude();
	}

	public Clothing strip(Clothing article, Combat c) {
		if (article == null) {
			return null;
		}
		Clothing res = outfit.unequip(article);
		c.getCombatantData(this).addToClothesPile(res);
		return res;
	}

	public Clothing strip(ClothingSlot slot, Combat c) {
		return strip(outfit.getTopOfSlot(slot), c);
	}

	public Clothing stripRandom(Combat c) {
		return stripRandom(c, false);
	}

	public void gainTrophy(Combat c, Character target) {
		Optional<Clothing> underwear = target.outfitPlan.stream()
				.filter(article -> article.getSlots().contains(
						ClothingSlot.bottom) && article.getLayer() == 0)
				.findFirst();
		if (!underwear.isPresent() || c.getCombatantData(target)
				.getClothespile().contains(underwear.get())) {
			this.gain(target.getTrophy());
		}
	}

	public Clothing shredRandom() {
		ClothingSlot slot = outfit.getRandomShreddableSlot();
		if (slot != null) {
			return shred(slot);
		}
		return null;
	}

	public boolean topless() {
		return outfit.slotEmpty(ClothingSlot.top);
	}

	public boolean pantsless() {
		return outfit.slotEmpty(ClothingSlot.bottom);
	}

	public Clothing stripRandom(Combat c, boolean force) {
		return strip(force ? outfit.getRandomEquippedSlot()
				: outfit.getRandomNakedSlot(), c);
	}

	public Clothing getRandomStrippable() {
		ClothingSlot slot = getOutfit().getRandomEquippedSlot();
		return slot == null ? null : getOutfit().getTopOfSlot(slot);
	}

	public Clothing shred(ClothingSlot slot) {
		Clothing article = outfit.getTopOfSlot(slot);
		if (article == null || article.is(ClothingTrait.indestructible)) {
			System.err.println(
					"Tried to shred clothing that doesn't exist at slot "
							+ slot.name() + " at clone " + cloned);
			System.err.println(outfit.toString());
			Thread.dumpStack();
			return null;
		} else {
			// don't add it to the pile
			return outfit.unequip(article);
		}
	}

	private void countdown(HashMap<Trait, Integer> counterSet) {
		Set<Trait> keys = new HashSet<Trait>(counterSet.keySet());
		for (Trait t : keys) {
			counterSet.put(t, counterSet.get(t) - 1);
			if (counterSet.get(t) <= 0) {
				counterSet.remove(t);
			}
		}
	}

	public void tick(Combat c) {
		body.tick(c);
		status.forEach(s -> s.tick(c));
		countdown(temporaryAddedTraits);
		countdown(temporaryRemovedTraits);
	}

	public Collection<Trait> getTraits() {
		Collection<Trait> allTraits = new HashSet<Trait>();
		allTraits.addAll(traits);
		allTraits.addAll(temporaryAddedTraits.keySet());
		allTraits.removeAll(temporaryRemovedTraits.keySet());
		return allTraits;
	}

	public boolean addTemporaryTrait(Trait t, int duration) {
		if (!getTraits().contains(t)) {
			temporaryAddedTraits.put(t, duration);
			return true;
		} else if (temporaryAddedTraits.containsKey(t)) {
			temporaryAddedTraits.put(t,
					Math.max(duration, temporaryAddedTraits.get(t)));
			return true;
		}
		return false;
	}

	public boolean removeTemporaryTrait(Trait t, int duration) {
		if (temporaryRemovedTraits.containsKey(t)) {
			temporaryRemovedTraits.put(t,
					Math.max(duration, temporaryRemovedTraits.get(t)));
			return true;
		} else if (traits.contains(t)) {
			temporaryRemovedTraits.put(t, duration);
			return true;
		}
		return false;
	}

	public void add(Trait t) {
		traits.add(t);
	}

	public void remove(Trait t) {
		traits.remove(t);
	}

	public boolean hasPure(Trait t) {
		return getTraits().contains(t);
	}

	public boolean has(Trait t) {
		boolean hasTrait = false;
		if (t.parent != null) {
			hasTrait = hasTrait || getTraits().contains(t.parent);
		}
		if (outfit.has(t)) {
			return true;
		}
		hasTrait = hasTrait || hasPure(t);
		return hasTrait;
	}

	public boolean hasDick() {
		return body.get("cock").size() > 0;
	}

	public boolean hasBalls() {
		return body.get("balls").size() > 0;
	}

	public boolean hasPussy() {
		return body.get("pussy").size() > 0;
	}

	public boolean hasBreasts() {
		return body.get("breasts").size() > 0;
	}

	public int countFeats() {
		int count = 0;
		for (Trait t : traits) {
			if (t.isFeat()) {
				count++;
			}
		}
		return count;
	}

	public void regen() {
		regen(null, false);
	}

	public void regen(Combat c) {
		regen(c, true);
	}

	public void regen(Combat c, boolean combat) {
		int regen = 1;
		// TODO can't find the concurrent modification error, just use a copy
		// for now I guess...
		for (Status s : new HashSet<Status>(getStatuses())) {
			regen += s.regen(c);
		}
		if (has(Trait.BoundlessEnergy)) {
			regen += 1;
		}
		if (regen > 0) {
			heal(c, regen);
		} else {
			weaken(c, regen);
		}
		if (combat) {
			if (has(Trait.exhibitionist) && mostlyNude()) {
				buildMojo(c, 5);
			}
			if (outfit.has(ClothingTrait.stylish)) {
				buildMojo(c, 3);
			}
			if (has(Trait.SexualGroove)) {
				buildMojo(c, 2);
			}
			if (outfit.has(ClothingTrait.lame)) {
				buildMojo(c, -2);
			}
		}
		orgasmed = false;
	}

	public void add(Status status) {
		add(null, status);
	}

	public boolean has(Status status) {
		return this.status.stream().anyMatch(s -> {
			return s.flags().containsAll(status.flags())
					&& status.flags().containsAll(status.flags())
					&& s.getVariant().equals(status.getVariant());
		});
	}

	public void add(Combat c, Status status) {
		boolean cynical = false;
		String message = "";
		for (Status s : getStatuses()) {
			if (s.flags().contains(Stsflag.cynical)) {
				cynical = true;
			}
		}
		if (cynical && status.mindgames()) {
			message = subjectAction("resist", "resists") + " " + status.name
					+ " (Cynical).";
		} else {
			for (Resistance r : getResistances()) {
				String resistReason = "";
				resistReason = r.resisted(this, status);
				if (!resistReason.isEmpty()) {
					message = subjectAction("resist", "resists") + " "
							+ status.name + " (" + resistReason + ").";
					break;
				}
			}
		}
		if (message.isEmpty()) {
			boolean unique = true;
			for (Status s : this.status) {
				if (s.getVariant().equals(status.getVariant())) {
					s.replace(status);
					message = s.initialMessage(c, false);
					break;
				}
				if (s.overrides(status)) {
					unique = false;
				}
			}
			if (message.isEmpty() && unique) {
				this.status.add(status);
				message = status.initialMessage(c, false);
			}
		}
		if (!message.isEmpty()) {
			message = Global.capitalizeFirstLetter(message);
			if (c != null) {
				c.write(this, "<b>" + message + "</b>");
			} else if (human()
					|| location() != null && location().humanPresent()) {
				Global.gui().message("<b>" + message + "</b>");
			}
			if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
				System.out.println(message);
			}
		}
	}

	private double getPheromonesChance(Combat c) {
		double baseChance = .1 + getExposure() / 2
				+ (arousal.getOverflow() + arousal.get())
						/ (float) arousal.max();
		double mod = c.getStance().pheromoneMod(this);
		double chance = Math.min(1, baseChance * mod);
		return chance;
	}

	public boolean rollPheromones(Combat c) {
		double chance = getPheromonesChance(c);
		double roll = Global.randomdouble();
		System.out.println("Pheromones: rolled " + Global.formatDecimal(roll)
				+ " vs " + chance + ".");
		return roll < chance;
	}

	public int getPheromonePower() {
		return 1 + get(Attribute.Animism) / 10;
	}

	public void dropStatus(Combat c, Character opponent) {
		Set<Status> removedStatuses = status.stream()
				.filter(s -> !s.meetsRequirements(c, this, opponent))
				.collect(Collectors.toSet());
		removedStatuses.addAll(removelist);
		removedStatuses.stream().forEach(s -> {
			if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
				System.out.println(s.name + " removed from " + name());
			}
			s.onRemove(c, opponent);
		});
		status.removeAll(removedStatuses);
		for (Status s : addlist) {
			add(c, s);
		}
		removelist.clear();
		addlist.clear();
	}

	public boolean is(Stsflag sts) {
		for (Status s : getStatuses()) {
			if (s.flags().contains(sts)) {
				return true;
			}
		}
		return false;
	}

	public boolean is(Stsflag sts, String variant) {
		for (Status s : getStatuses()) {
			if (s.flags().contains(sts) && s.getVariant().equals(variant)) {
				return true;
			}
		}
		return false;
	}

	public boolean stunned() {
		for (Status s : getStatuses()) {
			if (s.flags().contains(Stsflag.stunned)) {
				return true;
			}
		}
		return false;
	}

	public boolean distracted() {
		for (Status s : getStatuses()) {
			if (s.flags().contains(Stsflag.distracted)
					|| s.flags().contains(Stsflag.trance)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasStatus(Stsflag flag) {
		for (Status s : getStatuses()) {
			if (s.flags().contains(flag)) {
				return true;
			}
		}
		return false;
	}

	public void removeStatus(Stsflag flag) {
		for (Status s : getStatuses()) {
			if (s.flags().contains(flag)) {
				removelist.add(s);
			}
		}
	}

	public boolean bound() {
		for (Status s : getStatuses()) {
			if (s.flags().contains(Stsflag.bound)) {
				return true;
			}
		}
		return false;
	}

	public void free() {
		for (Status s : getStatuses()) {
			if (s.flags().contains(Stsflag.bound)) {
				removelist.add(s);
			}
		}
	}

	public void struggle() {
		for (Status s : getStatuses()) {
			s.struggle(this);
		}
	}

	public int getEscape(Combat c) {
		int total = 0;
		for (Status s : getStatuses()) {
			total += s.escape();
		}
		if (has(Trait.freeSpirit)) {
			total += 5;
		}
		int stanceMod = c.getStance().escape(c, this);
		if (stanceMod < 0) {
			total += stanceMod;
		}
		return total;
	}

	public int escape(Combat c) {
		int total = getEscape(c);
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
			System.out.println("Escape: " + total);
		}
		if (c.getStance().escape(c, this) < 0) {
			c.getStance().struggle();
		}
		return total;
	}

	public boolean canMasturbate() {
		return !(stunned() || bound() || is(Stsflag.distracted)
				|| is(Stsflag.enthralled));
	}

	public boolean canAct() {
		return !(stunned() || distracted() || bound()
				|| is(Stsflag.enthralled));
	}

	public boolean canRespond() {
		return !(stunned() || distracted() || is(Stsflag.enthralled));
	}

	public abstract void detect();

	public abstract void faceOff(Character opponent, IEncounter enc);

	public abstract void spy(Character opponent, IEncounter enc);

	public abstract String describe(int per, Combat c);

	public abstract void victory(Combat c, Result flag);

	public abstract void defeat(Combat c, Result flag);

	public abstract void intervene3p(Combat c, Character target,
			Character assist);

	public abstract void victory3p(Combat c, Character target,
			Character assist);

	public abstract boolean resist3p(Combat c, Character target,
			Character assist);

	public abstract void act(Combat c);

	public abstract void move();

	public abstract void draw(Combat c, Result flag);

	public abstract boolean human();

	public abstract String bbLiner(Combat c);

	public abstract String nakedLiner(Combat c);

	public abstract String stunLiner(Combat c);

	public abstract String taunt(Combat c);

	public abstract void intervene(IEncounter fight, Character p1, Character p2);

	public abstract void showerScene(Character target, IEncounter encounter);

	public boolean humanControlled(Combat c) {
		return human() || Global.isDebugOn(DebugFlags.DEBUG_SKILL_CHOICES)
				&& c.getOther(this).human();
	}

	@SuppressWarnings({ "unchecked" })
	private static void saveLoot(JSONObject obj, Collection<? extends Loot> arr,
			String name) {
		JSONArray array = new JSONArray();
		for (Loot e : arr) {
			array.add(e.getID());
		}
		obj.put(name, array);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void saveEnums(JSONObject obj,
			Collection<? extends Enum> arr, String name) {
		JSONArray array = new JSONArray();
		for (Enum e : arr) {
			array.add(e.name());
		}
		obj.put(name, array);
	}

	@SuppressWarnings("unchecked")
	private static void saveCharIntMap(JSONObject obj,
			Map<Character, Integer> map, String name) {
		JSONObject objMap = new JSONObject();
		for (Character key : map.keySet()) {
			if (key != null) {
				objMap.put(key.getType(), map.get(key));
			}
		}
		obj.put(name, objMap);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void saveEnumIntMap(JSONObject obj,
			Map<? extends Enum, Integer> map, String name) {
		JSONObject objMap = new JSONObject();
		for (Enum key : map.keySet()) {
			objMap.put(key.name(), map.get(key));
		}
		obj.put(name, objMap);
	}

	private static HashMap<Character, Integer> loadCharIntMap(
			JSONObject parentObj, String name) {
		JSONObject obj = (JSONObject) parentObj.get(name);
		HashMap<Character, Integer> map = new HashMap<>();
		for (Object key : obj.keySet()) {
			String keyString = (String) key;
			Character character = Global.getCharacterByType(keyString);
			if (character == null) {
				System.err.println("Failed loading character: " + keyString);
			} else {
				map.put(character, JSONUtils.readInteger(obj, keyString));
			}
		}
		return map;
	}

	private static void loadClothingFromArr(JSONObject obj,
			Collection<Clothing> arr, String name) {
		arr.clear();
		JSONArray savedArr = (JSONArray) obj.get(name);
		for (Object elem : savedArr) {
			String key = (String) elem;
			try {
				arr.add(Clothing.getByID(key));
			} catch (IllegalArgumentException e) {
				// If we find a piece of clothing that isn't actually available,
				// log it and ignore it.
				System.err.println(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public JSONObject save() {
		JSONObject saveObj = new JSONObject();
		saveObj.put("name", name);
		saveObj.put("type", getType());
		saveObj.put("level", level);
		saveObj.put("rank", getRank());
		saveObj.put("xp", xp);
		saveObj.put("money", money);
		{
			JSONObject resources = new JSONObject();
			resources.put("stamina", stamina.maxFull());
			resources.put("arousal", arousal.maxFull());
			resources.put("mojo", mojo.maxFull());
			resources.put("willpower", willpower.maxFull());
			saveObj.put("resources", resources);
		}
		saveCharIntMap(saveObj, affections, "affections");
		saveCharIntMap(saveObj, attractions, "attractions");
		saveEnumIntMap(saveObj, att, "attributes");
		saveLoot(saveObj, outfitPlan, "outfit");
		saveLoot(saveObj, closet, "closet");
		saveEnums(saveObj, traits, "traits");
		saveObj.put("body", body.save());
		saveEnumIntMap(saveObj, inventory, "inventory");
		saveObj.put("human", human());
		JSONObject flagsObj = new JSONObject();
		saveObj.put("flags", flagsObj);
		flags.entrySet().stream().forEach(
				entry -> flagsObj.put(entry.getKey(), entry.getValue()));
		return saveObj;
	}

	public abstract String getType();

	public void load(JSONObject obj) {
		name = JSONUtils.readString(obj, "name");
		level = JSONUtils.readInteger(obj, "level");
		rank = JSONUtils.readInteger(obj, "rank");
		xp = JSONUtils.readInteger(obj, "xp");
		money = JSONUtils.readInteger(obj, "money");
		{
			JSONObject resources = (JSONObject) obj.get("resources");
			stamina.setMax(JSONUtils.readFloat(resources, "stamina"));
			arousal.setMax(JSONUtils.readFloat(resources, "arousal"));
			mojo.setMax(JSONUtils.readFloat(resources, "mojo"));
			willpower.setMax(JSONUtils.readFloat(resources, "willpower"));
		}
		affections = loadCharIntMap(obj, "affections");
		attractions = loadCharIntMap(obj, "attractions");

		// TODO Clothing loading, this is for compatibility, remove this later.
		if (obj.containsKey("outfit")) {
			loadClothingFromArr(obj, outfitPlan, "outfit");
		} else {
			outfitPlan.clear();
		}
		if (obj.containsKey("top") && obj.containsKey("bottom")) {
			List<Clothing> temp = new ArrayList<>();
			loadClothingFromArr(obj, temp, "top");
			outfitPlan.addAll(temp);
			loadClothingFromArr(obj, temp, "bottom");
			outfitPlan.addAll(temp);
		}
		// End Clothing loading

		loadClothingFromArr(obj, closet, "closet");
		traits = new HashSet<>(
				JSONUtils.loadEnumsFromArr(obj, "traits", Trait.class));
		body = Body.load((JSONObject) obj.get("body"), this);
		{
			JSONObject attObj = (JSONObject) obj.get("attributes");
			att = new HashMap<>();
			for (Object key : attObj.keySet()) {
				String keyString = (String) key;
				Attribute attribute = Attribute.valueOf(keyString);
				att.put(attribute, JSONUtils.readInteger(attObj, keyString));
			}
		}
		{
			JSONObject invenObj = (JSONObject) obj.get("inventory");
			inventory = new HashMap<>();
			for (Object key : invenObj.keySet()) {
				String keyString = (String) key;
				Item item = Item.valueOf(keyString);
				inventory.put(item, JSONUtils.readInteger(invenObj, keyString));
			}
		}
		if (obj.containsKey("flags")) {
			JSONObject flagsObj = (JSONObject) obj.get("flags");
			flags.clear();
			for (Object key : flagsObj.keySet()) {
				String keyString = (String) key;
				flags.put(keyString,
						JSONUtils.readInteger(flagsObj, keyString));
			}
		}
		change();
		Global.gainSkills(this);
		Global.learnSkills(this);
	}

	public abstract void afterParty();

	public boolean checkOrgasm() {
		return getArousal().isFull() && !is(Stsflag.orgasmseal) && pleasured;
	}

	public CharacterSex getSex() {
		// it's oversimplified I know. So sue me :(
		if (hasDick() && hasPussy()) {
			return CharacterSex.herm;
		}
		if (hasDick()) {
			return CharacterSex.male;
		}
		if (hasPussy()) {
			return CharacterSex.female;
		}
		return CharacterSex.asexual;
	}

	public void doOrgasm(Combat c, Character opponent, BodyPart selfPart,
			BodyPart opponentPart) {
		orgasmed = true;
		c.write(this, "<br>");
		if (c.getStance().inserted(this) && !has(Trait.strapped)) {
			c.write(this,
					Global.format(
							"<b>{self:SUBJECT-ACTION:tense|tenses} up as {self:possessive} hips wildly buck against {other:direct-object}. In no time, {self:possessive} hot seed spills into {other:possessive} pulsing hole.</b>",
							this, opponent));
			if (c.getStance().en == Stance.anal) {
				opponent.body.receiveCum(c, this,
						opponent.body.getRandom("ass"));
			} else {
				opponent.body.receiveCum(c, this,
						opponent.body.getRandom("pussy"));
			}
		} else if (selfPart != null && selfPart.isType("cock")
				&& opponentPart != null && !opponentPart.isType("none")) {
			c.write(this,
					Global.format(
							"<b>{self:NAME-POSSESSIVE} back arches as thick ropes of jizz fire from {self:possessive} dick and land on {other:name-possessive} "
									+ opponentPart.describe(opponent) + ".</b>",
							this, opponent));
			opponent.body.receiveCum(c, this, opponentPart);
		} else {
			c.write(this,
					Global.format(
							"<b>{self:SUBJECT-ACTION:shudder|shudders} as {other:subject-action:bring|brings} {self:direct-object} to a toe-curling climax.</b>",
							this, opponent));
		}
		c.write(this, "<b>" + orgasmLiner(c) + "</b>");
		c.write(opponent, opponent.makeOrgasmLiner(c));
		int overflow = arousal.getOverflow();
		c.write(this,
				String.format(
						"<br><font color='rgb(255,50,200)'>%s<font color='white'> arousal overflow",
						overflow));

		if (selfPart != null && opponentPart != null) {
			selfPart.onOrgasm(c, this, opponent, opponentPart, true);
			opponentPart.onOrgasm(c, opponent, this, selfPart, false);
		} else if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
			System.out.printf(
					"Could not process %s's orgasm against %s: self=%s, opp=%s, pos=%s",
					this, opponent, selfPart, opponentPart, c.getStance());
		}

		if (opponent.has(Trait.erophage)) {
			c.write(Global.capitalizeFirstLetter("<br><b>"
					+ opponent.subjectAction("flush", "flushes")
					+ " as the feedback from " + nameOrPossessivePronoun()
					+ " orgasm feeds " + opponent.possessivePronoun()
					+ " divine power.</b>"));
			opponent.add(c, new Alluring(opponent, 5));
			opponent.buildMojo(c, 100);
			if (c.getStance().inserted(this) && opponent.has(Trait.divinity)) {
				opponent.add(c, new DivineCharge(opponent, 1));
			}
		}

		getArousal().empty();
		if (has(Trait.insatiable)) {
			arousal.restore((int) (arousal.max() * .2));
		}
		if (is(Stsflag.feral)) {
			arousal.restore(arousal.max() / 2);
		}
		float extra = 25.0f * overflow / (arousal.max() / 2.0f);

		loseWillpower(c, getOrgasmWillpowerLoss(), Math.round(extra), true, "");
		orgasms += 1;
	}

	public void doOrgasm(Combat c, Character opponent, Skill last) {
		doOrgasm(c, opponent, body.lastPleasured, body.lastPleasuredBy);
	}

	public void loseWillpower(Combat c, int i) {
		loseWillpower(c, i, 0, false, "");
	}

	public void loseWillpower(Combat c, int i, boolean primary) {
		loseWillpower(c, i, 0, primary, "");
	}

	public void loseWillpower(Combat c, int i, int extra, boolean primary,
			String source) {
		int amt = i + extra;
		String reduced = "";
		if (has(Trait.strongwilled) && primary) {
			amt = amt * 2 / 3 + 1;
			reduced = " (Strong-willed)";
		}
		if (is(Stsflag.feral) && primary) {
			amt = amt / 3;
			reduced = " (Feral)";
		}
		int old = willpower.get();
		willpower.reduce(amt);
		if (c != null) {
			c.writeSystemMessage(
					String.format(
							"%s lost <font color='rgb(220,130,40)'>%s<font color='white'> willpower"
									+ reduced + "%s.",
							subject(),
							extra == 0 ? Integer.toString(amt)
									: i + "+" + extra + " (" + amt + ")",
							source));
		} else {
			Global.gui()
					.systemMessage(
							String.format(
									"%s lost <font color='rgb(220,130,40)'>%d<font color='white'> willpower"
											+ reduced + "%s.",
									subject(), amt, source));
		}
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
			System.out.printf("will power reduced from %d to %d\n", old,
					willpower.get());
		}
	}

	public void restoreWillpower(Combat c, int i) {
		willpower.restore(i);
		c.writeSystemMessage(String.format(
				"%s regained <font color='rgb(181,230,30)'>%d<font color='white'> willpower.",
				subject(), i));
	}

	public void eot(Combat c, Character opponent, Skill last) {
		dropStatus(c, opponent);
		tick(c);
		List<String> removed = new ArrayList<String>();
		for (String s : cooldowns.keySet()) {
			if (cooldowns.get(s) <= 1) {
				removed.add(s);
			} else {
				cooldowns.put(s, cooldowns.get(s) - 1);
			}
		}
		for (String s : removed) {
			cooldowns.remove(s);
		}
		if (c.getStance().inserted()) {
			BodyPart selfOrgan;
			BodyPart otherOrgan;
			if (c.getStance().inserted(this)) {
				selfOrgan = body.getRandomCock();
				if (c.getStance().en == Stance.anal) {
					otherOrgan = opponent.body.getRandom("ass");
				} else {
					otherOrgan = opponent.body.getRandomPussy();
				}
			} else {
				otherOrgan = opponent.body.getRandomCock();
				if (c.getStance().en == Stance.anal) {
					selfOrgan = body.getRandom("ass");
				} else {
					selfOrgan = body.getRandomPussy();
				}
			}
			if (has(Trait.energydrain) && selfOrgan != null
					&& otherOrgan != null) {
				c.write(this,
						Global.format(
								"{self:NAME-POSSESSIVE} body glows purple as {other:subject-action:feel|feels} {other:possessive} very spirit drained into {self:possessive} "
										+ selfOrgan.describe(this)
										+ " through your connection.",
								this, opponent));
				int m = Global.random(5) + 5;
				opponent.drain(c, this, m);
			}
			// TODO this works weirdly when both have both organs.
			body.tickHolding(c, opponent, selfOrgan, otherOrgan);
		}
		if (outfit.has(ClothingTrait.tentacleSuit)) {
			c.write(this,
					Global.format(
							"The tentacle suit squirms against {self:name-possessive} body.",
							this, opponent));
			if (hasBreasts()) {
				body.pleasure(null, null, body.getRandom("breasts"), 5, c);
			}
			body.pleasure(null, null, body.getRandom("skin"), 5, c);
		}
		if (outfit.has(ClothingTrait.tentacleUnderwear)) {
			String undieName = "underwear";
			if (hasPussy()) {
				undieName = "panties";
			}
			c.write(this,
					Global.format(
							"The tentacle " + undieName
									+ " squirms against {self:name-possessive} crotch.",
							this, opponent));
			if (hasDick()) {
				body.pleasure(null, null, body.getRandom("cock"), 5, c);
			}
			if (hasBalls()) {
				body.pleasure(null, null, body.getRandom("balls"), 5, c);
			}
			if (hasPussy()) {
				body.pleasure(null, null, body.getRandom("pussy"), 5, c);
			}
			body.pleasure(null, null, body.getRandom("ass"), 5, c);
		}
		if (checkOrgasm()) {
			doOrgasm(c, opponent, null, null);
		}
		if (opponent.checkOrgasm()) {
			opponent.doOrgasm(c, this, null, null);
		}
		if (opponent.has(Trait.magicEyeEnthrall) && getArousal().percent() >= 50
				&& c.getStance().facing() && Global.random(20) == 0) {
			c.write(opponent,
					Global.format(
							"<br>{other:NAME-POSSESSIVE} eyes start glowing and captures both {self:name-possessive} gaze and consciousness.",
							this, opponent));
			add(c, new Enthralled(this, opponent, 2));
		}
		if (opponent.has(Trait.magicEyeTrance) && getArousal().percent() >= 50
				&& c.getStance().facing() && Global.random(10) == 0) {
			c.write(opponent,
					Global.format(
							"<br>{other:NAME-POSSESSIVE} eyes start glowing and sends {self:subject} straight into a trance.",
							this, opponent));
			add(c, new Trance(this));
		}
		if (opponent.has(Trait.magicEyeArousal) && getArousal().percent() >= 50
				&& c.getStance().facing() && Global.random(5) == 0) {
			c.write(opponent,
					Global.format(
							"<br>{other:NAME-POSSESSIVE} eyes start glowing and {self:subject-action:feel|feels} a strong pleasure wherever {other:possessive} gaze lands. {self:SUBJECT-ACTION:are|is} literally being raped by {other:name-possessive} eyes!",
							this, opponent));
			tempt(c, opponent, opponent.get(Attribute.Seduction) / 2);
		}
		pleasured = false;
	}

	public String orgasmLiner(Combat c) {
		return "";
	}

	public String makeOrgasmLiner(Combat c) {
		return "";
	}

	private int getOrgasmWillpowerLoss() {
		return 25;
	}

	public abstract void emote(Emotion emo, int amt);

	public void learn(Skill copy) {
		skills.add(copy.copy(this));
	}

	public void forget(Skill copy) {
		skills.remove(copy);
	}

	public boolean stealthCheck(int perception) {
		return check(Attribute.Cunning, Global.random(20) + perception)
				|| state == State.hidden;
	}

	public boolean spotCheck(int perception) {
		if (state == State.hidden) {
			int dc = perception + 10;
			if (has(Trait.Sneaky)) {
				dc -= 5;
			}
			return check(Attribute.Cunning, dc);
		} else {
			return true;
		}
	}

	public void travel(Area dest) {
		state = State.ready;
		location.exit(this);
		location = dest;
		dest.enter(this);
		if (dest.name.isEmpty()) {
			throw new RuntimeException("empty location");
		}
	}

	public void flee(Area location2) {
		Area[] adjacent = location2.adjacent
				.toArray(new Area[location2.adjacent.size()]);
		travel(adjacent[Global.random(adjacent.length)]);
		location2.endEncounter();
	}

	public void upkeep() {
		getTraits().forEach(trait -> {
			if (trait.status != null) {
				Status newStatus = trait.status.instance(this, null);
				if (!has(newStatus)) {
					add(newStatus);
				}
			}
		});
		regen();
		tick(null);
		if (has(Trait.Confident)) {
			willpower.restore(10);
			mojo.reduce(5);
		} else {
			willpower.restore(5);
			mojo.reduce(10);
		}
		if (bound()) {
			free();
		}
		dropStatus(null, null);
		if (has(Trait.QuickRecovery)) {
			heal(null, 4);
		}
		update();
		notifyObservers();
	}

	public String debugMessage(Combat c, Position p) {
		String mood;
		if (this instanceof NPC) { // $codepro.audit.disable
									// useOfInstanceOfWithThis
			mood = "mood: " + ((NPC) this).mood.toString();
		} else {
			mood = "";
		}
		return String.format("[%s] %s s: %d/%d a: %d/%d m: %d/%d c:%d f:%f",
				name, mood, stamina.get(), stamina.max(), arousal.get(),
				arousal.max(), mojo.get(), mojo.max(),
				outfit.getEquipped().size(), getFitness(c));
	}

	public void gain(Item item) {
		gain(item, 1);
	}

	public void remove(Item item) {
		gain(item, -1);
	}

	public void gain(Clothing item) {
		closet.add(item);
		setChanged();
	}

	public void gain(Item item, int q) {
		int amt = 0;
		if (inventory.containsKey(item)) {
			amt = count(item);
		}
		inventory.put(item, Math.max(0, amt + q));
		setChanged();
	}

	public boolean has(Item item) {
		return has(item, 1);
	}

	public boolean has(Item item, int quantity) {
		if (inventory.containsKey(item)) {
			return inventory.get(item) >= quantity;
		}
		return false;
	}

	public void unequipAllClothing() {
		closet.addAll(outfitPlan);
		outfitPlan.clear();
		change();
	}

	public boolean has(Clothing item) {
		return closet.contains(item) || outfit.getEquipped().contains(item);
	}

	public void consume(Item item, int quantity) {
		consume(item, quantity, true);
	}

	public void consume(Item item, int quantity, boolean canBeResourceful) {
		if (canBeResourceful && has(Trait.resourceful)
				&& Global.random(5) == 0) {
			quantity--;
		}
		if (inventory.containsKey(item)) {
			gain(item, -quantity);
		}
	}

	public int count(Item item) {
		if (inventory.containsKey(item)) {
			return inventory.get(item);
		}
		return 0;
	}

	public void chargeBattery() {
		int power = count(Item.Battery);
		if (power < 20) {
			gain(Item.Battery, 20 - power);
		}
	}

	public void defeated(Character victor) {
		mercy.add(victor);
	}

	public void resupply() {
		for (Character victor : mercy) {
			victor.bounty(has(Trait.event) ? 5 : 1);
		}
		mercy.clear();
		change();
		state = State.ready;
		getWillpower().fill();
		if (location().present.size() > 1) {
			if (location().id() == Movement.dorm) {
				if (Global.getMatch().gps("Quad").present.isEmpty()) {
					if (human()) {
						Global.gui().message(
								"You hear your opponents searching around the dorm, so once you finish changing, you hop out the window and head to the quad.");
					}
					travel(Global.getMatch().gps("Quad"));
				} else {
					if (human()) {
						Global.gui().message(
								"You hear your opponents searching around the dorm, so once you finish changing, you quietly move downstairs to the laundry room.");
					}
					travel(Global.getMatch().gps("Laundry"));
				}
			}
			if (location().id() == Movement.union) {
				if (Global.getMatch().gps("Quad").present.isEmpty()) {
					if (human()) {
						Global.gui().message(
								"You don't want to be ambushed leaving the student union, so once you finish changing, you hop out the window and head to the quad.");
					}
					travel(Global.getMatch().gps("Quad"));
				} else {
					if (human()) {
						Global.gui().message(
								"You don't want to be ambushed leaving the student union, so once you finish changing, you sneak out the back door and head to the pool.");
					}
					travel(Global.getMatch().gps("Pool"));
				}
			}
		}
	}

	public void finishMatch() {
		for (Character victor : mercy) {
			victor.bounty(has(Trait.event) ? 5 : 1);
		}
		Global.gui().clearImage();
		mercy.clear();
		change();
		clearStatus();
		temporaryAddedTraits.clear();
		temporaryRemovedTraits.clear();
		body.clearReplacements();
		getStamina().fill();
		getArousal().empty();
		getMojo().empty();
	}

	public void place(Area loc) {
		location = loc;
		loc.present.add(this);
		if (loc.name.isEmpty()) {
			throw new RuntimeException("empty location");
		}
	}

	public void bounty(int points) {
		Global.getMatch().score(this, points);
	}

	public boolean eligible(Character p2) {
		boolean ftc = true;
		if (Global.checkFlag(Flag.FTC)) {
			FTCMatch match = (FTCMatch) Global.getMatch();
			ftc = !match.inGracePeriod() || (!match.isPrey(this) && !match.isPrey(p2));
		}
		return ftc && !mercy.contains(p2) && state != State.resupplying;
	}

	public void setTrophy(Item trophy) {
		this.trophy = trophy;
	}

	public Item getTrophy() {
		return trophy;
	}

	public void bathe() {
		status.clear();
		stamina.fill();
		state = State.ready;
		setChanged();
	}

	public void masturbate() {
		arousal.empty();
		state = State.ready;
		setChanged();
	}

	public void craft() {
		int roll = Global.random(15);
		if (check(Attribute.Cunning, 25)) {
			if (roll == 9) {
				gain(Item.Aphrodisiac);
				gain(Item.DisSol);
			} else if (roll >= 5) {
				gain(Item.Aphrodisiac);
			} else {
				gain(Item.Lubricant);
				gain(Item.Sedative);
			}
		} else if (check(Attribute.Cunning, 20)) {
			if (roll == 9) {
				gain(Item.Aphrodisiac);
			} else if (roll >= 7) {
				gain(Item.DisSol);
			} else if (roll >= 5) {
				gain(Item.Lubricant);
			} else if (roll >= 3) {
				gain(Item.Sedative);
			} else {
				gain(Item.EnergyDrink);
			}
		} else if (check(Attribute.Cunning, 15)) {
			if (roll == 9) {
				gain(Item.Aphrodisiac);
			} else if (roll >= 8) {
				gain(Item.DisSol);
			} else if (roll >= 7) {
				gain(Item.Lubricant);
			} else if (roll >= 6) {
				gain(Item.EnergyDrink);
			}
		} else {
			if (roll >= 7) {
				gain(Item.Lubricant);
			} else if (roll >= 5) {
				gain(Item.Sedative);
			}
		}
		state = State.ready;
		setChanged();
	}

	public void search() {
		int roll = Global.random(15);
		switch (roll) {
			case 9:
				gain(Item.Tripwire);
				gain(Item.Tripwire);
				break;
			case 8:
				gain(Item.ZipTie);
				gain(Item.ZipTie);
				gain(Item.ZipTie);
				break;
			case 7:
				gain(Item.Phone);
				break;
			case 6:
				gain(Item.Rope);
				break;
			case 5:
				gain(Item.Spring);
		}
		state = State.ready;

	}

	public abstract String challenge(Character other);

	public void delay(int i) {
		busy += i;
	}

	public abstract void promptTrap(IEncounter fight, Character target, Trap trap);

	public int lvlBonus(Character opponent) {
		if (opponent.getLevel() > getLevel()) {
			return 10 * (opponent.getLevel() - getLevel());
		} else {
			return 0;
		}
	}

	public int getVictoryXP(Character opponent) {
		return 20 + lvlBonus(opponent);
	}

	public int getAssistXP(Character opponent) {
		return 10 + lvlBonus(opponent);
	}

	public int getDefeatXP(Character opponent) {
		return 20 + lvlBonus(opponent);
	}

	public int getAttraction(Character other) {
		if (other == null) {
			System.err.println("Other is null");
			Thread.dumpStack();
			return 0;
		}
		if (attractions.containsKey(other)) {
			return attractions.get(other);
		} else {
			return 0;
		}
	}

	public void gainAttraction(Character other, int x) {
		if (other == null) {
			System.err.println("Other is null");
			Thread.dumpStack();
		}
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
			System.out.printf("%s gained attraction for %s\n", name(),
					other.name());
		}
		if (attractions.containsKey(other)) {
			attractions.put(other, attractions.get(other) + x);
		} else {
			attractions.put(other, x);
		}
	}

	public int getAffection(Character other) {
		if (other == null) {
			System.err.println("Other is null");
			Thread.dumpStack();
			return 0;
		}

		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
			System.out.printf("%s gained affection for %s\n", name(),
					other.name());
		}
		if (affections.containsKey(other)) {
			return affections.get(other);
		} else {
			return 0;
		}
	}

	public void gainAffection(Character other, int x) {
		if (other == null) {
			System.err.println("Other is null");
			Thread.dumpStack();
		}
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
			System.out.printf("%s gained affection for %s\n", name(),
					other.name());
		}
		if (affections.containsKey(other)) {
			affections.put(other, affections.get(other) + x);
		} else {
			affections.put(other, x);
		}
	}

	public int evasionBonus() {
		int ac = 0;
		for (Status s : getStatuses()) {
			ac += s.evade();
		}
		if (has(Trait.clairvoyance)) {
			ac += 5;
		}
		return ac;
	}

	private Collection<Status> getStatuses() {
		return status;
	}

	public int counterChance(Combat c, Character opponent, Skill skill) {
		int counter = 0;
		counter += Math.max(0,
				get(Attribute.Cunning) - opponent.get(Attribute.Cunning)) / 2;
		counter += get(Attribute.Perception);
		counter += (get(Attribute.Speed) - opponent.get(Attribute.Speed)) / 2;
		counter += 5 - skill.accuracy(c);
		for (Status s : getStatuses()) {
			counter += s.counter();
		}
		if (has(Trait.clairvoyance)) {
			counter += 3;
		}
		if (has(Trait.aikidoNovice)) {
			counter += 3;
		}
		if (opponent.is(Stsflag.countered)) {
			counter -= 10;
		}
		return Math.max(0, counter);
	}

	public boolean roll(Skill attack, Combat c, int accuracy) {
		int hitDiff = attack.user().get(Attribute.Speed)
				+ attack.user().get(Attribute.Perception)
				- (get(Attribute.Perception) + get(Attribute.Speed));
		int levelDiff = Math.min(attack.user().level - level, 5);
		levelDiff = Math.max(attack.user().level - level, -5);
		int attackroll = Global.random(100);

		// with no level or hit differences and an default accuracy of 80, 80%
		// hit rate
		// each level the attacker is below the target will reduce this by 5%,
		// to a maximum of 25%
		// each point in accuracy of skill affects changes the hit chance by 1%
		// each point in speed and perception will increase hit by 5%
		int chanceToHit = 5 * levelDiff + accuracy
				+ 5 * (hitDiff - evasionBonus());
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
			System.out.printf(
					"Rolled %s against %s, base accuracy: %s, hit difference: %s, level difference: %s\n",
					attackroll, chanceToHit, accuracy, hitDiff, levelDiff);
		}

		return attackroll < chanceToHit;
	}

	public int knockdownDC() {
		int dc = 10 + getStamina().get() / 2;
		if (is(Stsflag.braced)) {
			dc += getStatus(Stsflag.braced).value();
		}
		if (has(ClothingTrait.heels) && !has(Trait.proheels)) {
			dc -= 7;
		}
		if (has(ClothingTrait.highheels) && !has(Trait.proheels)) {
			dc -= 8;
		}
		if (has(ClothingTrait.higherheels) && !has(Trait.proheels)) {
			dc -= 10;
		}
		return dc;
	}

	public abstract void counterattack(Character target, Tactics type,
			Combat c);

	public void clearStatus() {
		status.clear();
	}

	public Status getStatus(Stsflag flag) {
		for (Status s : getStatuses()) {
			if (s.flags().contains(flag)) {
				return s;
			}
		}
		return null;
	}

	public Integer prize() {
		if (getRank() == 1) {
			return 200;
		} else {
			return 50;
		}
	}

	public Move findPath(Area target) {
		if (location.name.equals(target.name)) {
			return null;
		}
		ArrayDeque<Area> queue = new ArrayDeque<Area>();
		Vector<Area> vector = new Vector<Area>();
		HashMap<Area, Area> parents = new HashMap<Area, Area>();
		queue.push(location);
		vector.add(location);
		Area last = null;
		while (!queue.isEmpty()) {
			Area t = queue.pop();
			parents.put(t, last);
			if (t.name.equals(target.name)) {
				while (!location.adjacent.contains(t)) {
					t = parents.get(t);
				}
				return new Move(t);
			}
			for (Area area : t.adjacent) {
				if (!vector.contains(area)) {
					vector.add(area);
					queue.push(area);
				}
			}
			last = t;
		}
		return null;
	}

	public boolean knows(Skill skill) {
		for (Skill s : skills) {
			if (s.equals(skill)) {
				return true;
			}
		}
		return false;
	}

	public void endofbattle() {
		for (Status s : status) {
			if (!s.lingering()) {
				removelist.add(s);
			}
		}
		if (pet != null) {
			pet.remove();
		}
		cooldowns.clear();
		dropStatus(null, null);
		orgasms = 0;
		setChanged();
		if (has(ClothingTrait.heels)) {
			setFlag("heelsTraining", getFlag("heelsTraining") + 1);
		}
		if (has(ClothingTrait.highheels)) {
			setFlag("heelsTraining", getFlag("heelsTraining") + 1);
		}
		if (has(ClothingTrait.higherheels)) {
			setFlag("heelsTraining", getFlag("heelsTraining") + 1);
		}
	}

	public void setFlag(String string, int i) {
		flags.put(string, i);
	}

	public int getFlag(String string) {
		if (flags.containsKey(string)) {
			return flags.get(string);
		}
		return 0;
	}

	public boolean canSpend(int mojo) {
		int cost = mojo;
		for (Status s : getStatuses()) {
			cost += s.spendmojo(mojo);
		}
		return getMojo().get() >= cost;
	}

	public Map<Item, Integer> getInventory() {
		return inventory;
	}

	public ArrayList<String> listStatus() {
		ArrayList<String> result = new ArrayList<String>();
		for (Status s : getStatuses()) {
			result.add(s.toString());
		}
		return result;
	}

	public String dumpstats(boolean notableOnly) {
		StringBuilder b = new StringBuilder();
		b.append("<b>");
		b.append(name() + ": Level " + getLevel() + "; ");
		for (Attribute a : att.keySet()) {
			b.append(a.name() + " " + att.get(a) + ", ");
		}
		b.append("</b>");
		b.append("<br>Max Stamina " + stamina.max() + ", Max Arousal "
				+ arousal.max() + ", Max Mojo " + mojo.max()
				+ ", Max Willpower " + willpower.max() + ".");
		b.append("<br>");
		body.describeBodyText(b, this, notableOnly);
		if (getTraits().size() > 0) {
			b.append("<br>Traits:<br>");
			List<Trait> traits = new ArrayList<>(getTraits());
			traits.sort((first, second) -> first.toString()
					.compareTo(second.toString()));
			for (Trait t : traits) {
				b.append(t + ": " + t.getDesc());
				b.append("<br>");
			}
		}
		b.append("</p>");

		return b.toString();
	}

	public void accept(Challenge c) {
		challenges.add(c);
	}

	public void evalChallenges(Combat c, Character victor) {
		for (Challenge chal : challenges) {
			chal.check(c, victor);
		}
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean taintedFluids() {
		return Global.random(get(Attribute.Dark) / 4 + 5) >= 4;
	}

	protected void pickSkillsWithGUI(Combat c, Character target) {
		if (Global.isDebugOn(DebugFlags.DEBUG_SKILL_CHOICES)) {
			c.write(this, nameOrPossessivePronoun() + " turn...");
			c.updateAndClearMessage();
		}
		HashSet<Skill> available = new HashSet<Skill>();
		HashSet<Skill> cds = new HashSet<Skill>();
		for (Skill a : skills) {
			if (Skill.skillIsUsable(c, a, target)) {
				if (cooldownAvailable(a)) {
					available.add(a);
				} else {
					cds.add(a);
				}
			}
		}
		HashSet<Skill> damage = new HashSet<Skill>();
		HashSet<Skill> pleasure = new HashSet<Skill>();
		HashSet<Skill> fucking = new HashSet<Skill>();
		HashSet<Skill> position = new HashSet<Skill>();
		HashSet<Skill> debuff = new HashSet<Skill>();
		HashSet<Skill> recovery = new HashSet<Skill>();
		HashSet<Skill> summoning = new HashSet<Skill>();
		HashSet<Skill> stripping = new HashSet<Skill>();
		HashSet<Skill> misc = new HashSet<Skill>();
		Skill.filterAllowedSkills(c, available, this, target);
		if (available.size() == 0) {
			available.add(new Nothing(this));
		}
		available.addAll(cds);
		for (Skill a : available) {
			if (a.type(c) == Tactics.damage) {
				damage.add(a);
			} else if (a.type(c) == Tactics.pleasure) {
				pleasure.add(a);
			} else if (a.type(c) == Tactics.fucking) {
				fucking.add(a);
			} else if (a.type(c) == Tactics.positioning) {
				position.add(a);
			} else if (a.type(c) == Tactics.debuff) {
				debuff.add(a);
			} else if (a.type(c) == Tactics.recovery
					|| a.type(c) == Tactics.calming) {
				recovery.add(a);
			} else if (a.type(c) == Tactics.summoning) {
				summoning.add(a);
			} else if (a.type(c) == Tactics.stripping) {
				stripping.add(a);
			} else {
				misc.add(a);
			}
		}
		Global.gui().clearCommand();
		for (Skill a : stripping) {
			Global.gui().addSkill(a, c);
		}
		for (Skill a : position) {
			Global.gui().addSkill(a, c);
		}
		for (Skill a : fucking) {
			Global.gui().addSkill(a, c);
		}
		for (Skill a : pleasure) {
			Global.gui().addSkill(a, c);
		}
		for (Skill a : damage) {
			Global.gui().addSkill(a, c);
		}
		for (Skill a : debuff) {
			Global.gui().addSkill(a, c);
		}
		for (Skill a : summoning) {
			Global.gui().addSkill(a, c);
		}
		for (Skill a : recovery) {
			Global.gui().addSkill(a, c);
		}
		for (Skill a : misc) {
			Global.gui().addSkill(a, c);
		}
		Global.gui().showSkills(0);
	}

	public float getOtherFitness(Combat c, Character other) {
		float fit = 0;
		// Urgency marks
		float arousalMod = 1.0f;
		float staminaMod = 1.0f;
		float mojoMod = 1.0f;
		float usum = arousalMod + staminaMod + mojoMod;
		int escape = other.getEscape(c);
		if (escape > 1) {
			fit += 8 * Math.log(escape);
		} else if (escape < -1) {
			fit += -8 * Math.log(-escape);
		}
		int totalAtts = 0;
		for (Attribute attribute : att.keySet()) {
			totalAtts += att.get(attribute);
		}
		fit += Math.sqrt(totalAtts) * 5;

		// what an average piece of clothing should be worth in fitness
		double topFitness = 4.0;
		double bottomFitness = 4.0;
		// If I'm horny, I want the other guy's clothing off, so I put more
		// fitness in them
		if (getMood() == Emotion.horny) {
			topFitness = 8;
			topFitness = 6;
			// If I'm horny, I want to make the opponent cum asap, put more
			// emphasis on arousal
			arousalMod = 2.0f;
		}

		// check body parts based on my preferences
		if (other.hasDick()) {
			fit -= (dickPreference() - 3) * 4;
		}
		if (other.hasPussy()) {
			fit -= (pussyPreference() - 3) * 4;
		}

		fit += other.outfit.getFitness(c, bottomFitness, topFitness);
		fit += other.body.getHotness(other, this);
		// Extreme situations
		if (other.arousal.isFull()) {
			fit -= 50;
		}
		// will power empty is a loss waiting to happen
		if (other.willpower.isEmpty()) {
			fit -= 100;
		}
		if (other.stamina.isEmpty()) {
			fit -= staminaMod * 3;
		}
		fit += 100.0f
				* (other.getWillpower().max() - other.getWillpower().get())
				/ Math.min(100, other.getWillpower().max());
		// Short-term: Arousal
		fit += arousalMod / usum * 100.0f
				* (other.getArousal().max() - other.getArousal().get())
				/ Math.min(100, other.getArousal().max());
		// Mid-term: Stamina
		fit += staminaMod / usum * 50.0f
				* (1 - Math.exp(-((float) other.getStamina().get())
						/ Math.min(other.getStamina().max(), 100.0f)));
		// Long term: Mojo
		fit += mojoMod / usum * 50.0f
				* (1 - Math.exp(-((float) other.getMojo().get())
						/ Math.min(other.getMojo().max(), 40.0f)));
		for (Status status : other.getStatuses()) {
			fit += status.fitnessModifier();
		}
		return fit;
	}

	public float getFitness(Combat c) {
		float fit = 0;
		// Urgency marks
		float arousalMod = 1.0f;
		float staminaMod = 1.0f;
		float mojoMod = 1.0f;
		float usum = arousalMod + staminaMod + mojoMod;
		Character other = c.getOther(this);

		int totalAtts = 0;
		for (Attribute attribute : att.keySet()) {
			totalAtts += att.get(attribute);
		}
		fit += Math.sqrt(totalAtts) * 5;
		// Always important: Position
		fit += c.getStance().priorityMod(this) * 6;
		int escape = getEscape(c);
		if (escape > 1) {
			fit += 8 * Math.log(escape);
		} else if (escape < -1) {
			fit += -8 * Math.log(-escape);
		}
		// what an average piece of clothing should be worth in fitness
		double topFitness = 4.0;
		double bottomFitness = 4.0;
		// If I'm horny, I don't care about my clothing, so I put more less
		// fitness in them
		if (getMood() == Emotion.horny) {
			topFitness = 1;
			topFitness = 1;
			// If I'm horny, I put less importance on my own arousal
			arousalMod = .7f;
		}
		fit += outfit.getFitness(c, bottomFitness, topFitness);
		fit += body.getHotness(this, other);
		if (c.getStance().inserted()) { // If we are fucking...
			// ...we need to see if that's beneficial to us.
			fit += body.penetrationFitnessModifier(c.getStance().inserted(this),
					c.getStance().anallyPenetrated(), other.body);
		}
		if (hasDick()) {
			fit += (dickPreference() - 3) * 4;
		}

		if (hasPussy()) {
			fit += (pussyPreference() - 3) * 4;
		}
		if (has(Trait.pheromones)) {
			fit += 5 * getPheromonePower();
			fit += 15 * getPheromonesChance(c) * (2 + getPheromonePower());
		}

		// Also somewhat of a factor: Inventory (so we don't
		// just use it without thinking)
		for (Item item : inventory.keySet()) {
			fit += (float) item.getPrice() / 10;
		}
		// Extreme situations
		if (arousal.isFull()) {
			fit -= 100;
		}
		if (stamina.isEmpty()) {
			fit -= staminaMod * 3;
		}
		fit += 100.0f * (getWillpower().max() - getWillpower().get())
				/ Math.min(100, getWillpower().max());
		// Short-term: Arousal
		fit += arousalMod / usum * 100.0f
				* (getArousal().max() - getArousal().get())
				/ Math.min(100, getArousal().max());
		// Mid-term: Stamina
		fit += staminaMod / usum * 50.0f
				* (1 - Math.exp(-((float) getStamina().get())
						/ Math.min(getStamina().max(), 100.0f)));
		// Long term: Mojo
		fit += mojoMod / usum * 50.0f * (1 - Math.exp(
				-((float) getMojo().get()) / Math.min(getMojo().max(), 40.0f)));
		for (Status status : getStatuses()) {
			fit += status.fitnessModifier();
		}

		if (!human()) {
			NPC me = (NPC) this;
			AiModifiers mods = me.ai.getAiModifiers();
			fit += mods.modPosition(c.getStance().enumerate()) * 6;
			fit += status.stream().flatMap(s -> s.flags().stream())
					.mapToDouble(f -> mods.modSelfStatus(f)).sum();
			fit += c.getOther(this).status.stream()
					.flatMap(s -> s.flags().stream())
					.mapToDouble(f -> mods.modOpponentStatus(f)).sum();
		}
		return fit;
	}

	public String nameOrPossessivePronoun() {
		return name + "'s";
	}

	public double getExposure(ClothingSlot slot) {
		return outfit.getExposure(slot);
	}

	public double getExposure() {
		return outfit.getExposure();
	}

	public abstract String getPortrait(Combat c);

	public void modMoney(int i) {
		if (human() && i > 0) {
			Global.gui().message(
					"You've gained $" + Math.round(i * Global.moneyRate) + ".");
		}
		setMoney((int) (money + Math.round(i * Global.moneyRate)));
	}

	public void setMoney(int i) {
		money = i;
		update();
	}

	public void loseXP(int i) {
		assert i >= 0;
		xp -= i;
		update();
	}

	public String pronoun() {
		if (useFemalePronouns()) {
			return "she";
		} else {
			return "he";
		}
	}

	public Emotion getMood() {
		return Emotion.confident;
	}

	public String possessivePronoun() {
		if (useFemalePronouns()) {
			return "her";
		} else {
			return "his";
		}
	}

	public String directObject() {
		if (useFemalePronouns()) {
			return "her";
		} else {
			return "him";
		}
	}

	private boolean useFemalePronouns() {
		return hasPussy();
	}

	public String nameDirectObject() {
		return name();
	}

	public String reflectivePronoun() {
		return possessivePronoun() + "self";
	}

	public boolean clothingFuckable(BodyPart part) {
		if (part.isType("strapon")) {
			return true;
		}
		if (part.isType("cock")) {
			return outfit.slotEmptyOrMeetsCondition(ClothingSlot.bottom,
					(article) -> (!article.is(ClothingTrait.armored)
							&& !article.is(ClothingTrait.bulky)));
		} else if (part.isType("pussy") || part.isType("ass")) {
			return outfit.slotEmptyOrMeetsCondition(ClothingSlot.bottom,
					(article) -> {
						return article.is(ClothingTrait.skimpy)
								|| article.is(ClothingTrait.open)
								|| article.is(ClothingTrait.flexible);
					});
		} else {
			return false;
		}
	}

	public double pussyPreference() {
		return 11 - Global.getValue(Flag.malePref);
	}

	public double dickPreference() {
		return Global.getValue(Flag.malePref);
	}

	public boolean wary() {
		return hasStatus(Stsflag.wary);
	}

	public void gain(Combat c, Item item) {
		if (c != null) {
			c.write(Global.format("<b>{self:subject-action:have|has} gained "
					+ item.pre() + item.getName() + "</b>", this, this));
		}
		gain(item, 1);
	}

	public String temptLiner(Combat c) {
		Character target = c.getOther(this);
		return Global.format(
				"{self:SUBJECT-ACTION:tempt|tempts} {other:direct-object}.",
				this, target);
	}

	public String action(String firstPerson, String thirdPerson) {
		return thirdPerson;
	}

	public void addCooldown(Skill skill) {
		if (skill.getCooldown() <= 0) {
			return;
		}
		if (cooldowns.containsKey(skill.toString())) {
			cooldowns.put(skill.toString(),
					cooldowns.get(skill.toString()) + skill.getCooldown());
		} else {
			cooldowns.put(skill.toString(), skill.getCooldown());
		}
	}

	public boolean cooldownAvailable(Skill s) {
		boolean cooledDown = true;
		if (cooldowns.containsKey(s.toString())
				&& cooldowns.get(s.toString()) > 0) {
			cooledDown = false;
		}
		return cooledDown;
	}

	public Object getCooldown(Skill s) {
		if (cooldowns.containsKey(s.toString())
				&& cooldowns.get(s.toString()) > 0) {
			return cooldowns.get(s.toString());
		} else {
			return 0;
		}
	}

	public boolean checkLoss() {
		return orgasmed && willpower.isEmpty();
	}

	public boolean isCustomNPC() {
		return custom;
	}

	public String recruitLiner() {
		return "";
	}

	public int stripDifficulty(Character other) {
		if (outfit.has(ClothingTrait.tentacleSuit)
				|| outfit.has(ClothingTrait.tentacleUnderwear)) {
			return other.get(Attribute.Science) + 20;
		}
		return 0;
	}

	public void startBattle(Combat combat) {
		orgasms = 0;
	}

	public void drainStaminaAsMojo(Combat c, Character drainer, int i,
			float efficiency) {
		int drained = i;
		int bonus = 0;

		for (Status s : getStatuses()) {
			bonus += s.drained(drained);
		}
		drained += bonus;
		if (drained >= stamina.get()) {
			drained = stamina.get();
		}
		drained = Math.max(1, drained);
		int restored = Math.round(drained * efficiency);
		if (c != null) {
			c.writeSystemMessage(String.format(
					"%s drained of <font color='rgb(240,162,100)'>%d<font color='white'> stamina as <font color='rgb(100,162,240)'>%d<font color='white'> mojo by %s",
					subjectWas(), drained, restored, drainer.subject()));
		}
		stamina.reduce(drained);
		drainer.mojo.restore(restored);
	}

	public void drainMojo(Combat c, Character drainer, int i) {
		int drained = i;
		int bonus = 0;

		for (Status s : getStatuses()) {
			bonus += s.drained(drained);
		}
		drained += bonus;
		if (drained >= mojo.get()) {
			drained = mojo.get();
		}
		drained = Math.max(1, drained);
		if (c != null) {
			c.writeSystemMessage(String.format(
					"%s drained of <font color='rgb(0,162,240)'>%d<font color='white'> mojo by %s",
					subjectWas(), drained, drainer.subject()));
		}
		mojo.reduce(drained);
		drainer.mojo.restore(drained);
	}

	public void update() {
		setChanged();
		notifyObservers();
	}

	public Outfit getOutfit() {
		return outfit;
	}

	public boolean footAvailable() {
		Clothing article = outfit.getTopOfSlot(ClothingSlot.feet);
		return article == null || article.getLayer() < 2;
	}

	public boolean hasInsertable() {
		return hasDick() || has(Trait.strapped);
	}

	public String guyOrGirl() {
		return useFemalePronouns() ? "girl" : "guy";
	}

	public String boyOrGirl() {
		return useFemalePronouns() ? "girl" : "boy";
	}

	public boolean isDemonic() {
		return has(Trait.succubus)
				|| body.get("cock").stream()
						.anyMatch(part -> part.getMod() == PussyPart.succubus)
				|| body.get("cock").stream()
						.anyMatch(part -> part.getMod() == CockMod.incubus);
	}

	public int baseDisarm() {
		int disarm = 0;
		if (has(Trait.cautious)) {
			disarm += 5;
		}
		return disarm;
	}

	public int baseRecoilPleasure() {
		int total = get(Attribute.Submissive) / 2;
		if (has(Trait.responsive)) {
			total += 3;
		}
		return total;
	}
}