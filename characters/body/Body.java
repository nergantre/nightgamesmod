package characters.body;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import global.DebugFlags;
import global.Global;
import global.Match;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import status.Abuff;
import status.CockBound;
import status.Stsflag;

import combat.Combat;

public class Body implements Cloneable {
	class PartReplacement {
		public List<BodyPart> added;
		public List<BodyPart> removed;
		public int duration;
		public PartReplacement(int duration) {
			added = new ArrayList<BodyPart>();
			removed = new ArrayList<BodyPart>();
			this.duration = duration;
		}
	}

	Collection<BodyPart> bodyParts;
	Collection<PartReplacement> replacements;
	public double hotness;
	public Character character;
	public static BodyPart nonePart = new GenericBodyPart("none", 0, 1, 1, "none", "");
	// yeah i know :(
	public Set<String> pluralParts = new HashSet<String>(Arrays.asList(
		"hands", "feet", "wings", "breasts", "balls"
	));
	private Map<String, BodyPart> prototypes;
	final static BodyPart requiredParts[] = {
		new GenericBodyPart("hands",0,1,1,"hands", ""), 
		new GenericBodyPart("feet",0,1,1,"feet", ""), 
		new GenericBodyPart("skin",0,1,1,"skin", ""),
		AssPart.generic, MouthPart.generic, BreastsPart.flat,
		EarPart.normal
	};

	public Body(Character character) {
		this(character, 1);
	}

	public Body(Character character, double hotness) {
		this.character = character;
		bodyParts = new LinkedHashSet<BodyPart>();
		replacements = new ArrayList<PartReplacement>();
		this.hotness = hotness;
		this.prototypes = new HashMap<String, BodyPart>();
		prototypes.put(PussyPart.class.getCanonicalName(), PussyPart.normal);
		prototypes.put(BreastsPart.class.getCanonicalName(), BreastsPart.c);
		prototypes.put(CockPart.class.getCanonicalName(), CockPart.average);
		prototypes.put(WingsPart.class.getCanonicalName(), WingsPart.normal);
		prototypes.put(TailPart.class.getCanonicalName(), TailPart.normal);
		prototypes.put(EarPart.class.getCanonicalName(), EarPart.normal);
		prototypes.put(StraponPart.class.getCanonicalName(), StraponPart.generic);
		prototypes.put(TentaclePart.class.getCanonicalName(), new TentaclePart("tentacles", "back", "semen", 0, 1, 1));
		prototypes.put(AssPart.class.getCanonicalName(), new AssPart("ass", 0, 1, 1));
		prototypes.put(MouthPart.class.getCanonicalName(), new MouthPart("mouth", 0, 1, 1));
		prototypes.put(AnalPussyPart.class.getCanonicalName(), new AnalPussyPart());
		prototypes.put(GenericBodyPart.class.getCanonicalName(), new GenericBodyPart("", 0, 1, 1, "none", "none"));
	}

	public Collection<BodyPart> getCurrentParts() {
		Set<BodyPart> parts = new HashSet<BodyPart>();
		parts.addAll(bodyParts);
		for (PartReplacement r : replacements) {
			parts.removeAll(r.removed);
			parts.addAll(r.added);
		}
		return parts;
	}

	public void temporaryAddPart(BodyPart part, int duration) {
		PartReplacement replacement = new PartReplacement(duration);
		replacement.added.add(part);
		replacements.add(replacement);
	}

	public void temporaryRemovePart(BodyPart part, int duration) {
		PartReplacement replacement = new PartReplacement(duration);
		replacement.removed.add(part);
		replacements.add(replacement);
	}

	public void temporaryAddOrReplacePartWithType(BodyPart part, int duration) {
		temporaryAddOrReplacePartWithType(part, getRandom(part.getType()), duration);
	}

	private BodyPart getPartIn(String type, Collection<BodyPart> parts) {
		for (BodyPart p : parts) {
			if (p.isType(type)) {
				return p;
			}
		}
		return null;
	}
	public boolean temporaryAddOrReplacePartWithType(BodyPart part, BodyPart removed, int duration) {
		PartReplacement replacement = null;
		for (PartReplacement r : replacements) {
			BodyPart other = null;
			if (r.added.contains(removed)) {
				other = removed;
			} else {
				other = getPartIn(removed.getType(), r.added);
			}
			if (other != null) {
				replacement = r;
				r.added.remove(other);
				r.added.add(part);
				replacement.duration = Math.max(duration, replacement.duration);
				break;
			}
		}
		if (replacement == null) {
			replacement = new PartReplacement(duration);
			replacement.removed.add(removed);
			replacement.added.add(part);
			replacements.add(replacement);
		}
		return true;
	}

	public void describe(StringBuilder b, Character c, String delimiter)
	{
		for (BodyPart part : getCurrentParts()) {
			if (part.isVisible(c) && part.isNotable()) {
				int prevLength = b.length();
				part.describeLong(b, c);
				if (prevLength != b.length())
					b.append(delimiter);
			}
		}
	}

	public void describeNotable(StringBuilder b, Character c)
	{		
		b.append(Global.format("{self:POSSESSIVE} body has ", c, null));
		BodyPart previous = null;
		for (BodyPart part : getCurrentParts()) {
			if (part.isNotable()) {
				if (previous != null) {
					b.append(previous.prefix());
					b.append(previous.describe(c));
					b.append(", ");
				}
				previous = part;
			}
		}
		if (previous == null) {
			b.append("nothing notable.<br>");
		} else {
			b.append("and ");
			if (!pluralParts.contains(previous.getType())) {
				b.append("a ");
			}
			b.append(previous.describe(c));
			b.append(".<br>");
		}
	}

	public void add(BodyPart part) {
		assert(part!=null);
		bodyParts.add(part);
	}

	public boolean contains(BodyPart part) {
		return getCurrentParts().contains(part);
	}

	public List<BodyPart> get(String type) {
		List<BodyPart> parts = new ArrayList<BodyPart>(); 
		for (BodyPart part : getCurrentParts()) {
			if (part.isType(type)) {
				parts.add(part);
			}
		}
		return parts;
	}

	public PussyPart getRandomPussy() {
		return (PussyPart)getRandom("pussy");

	}

	public WingsPart getRandomWings() {
		return (WingsPart)getRandom("wings");

	}

	public BreastsPart getRandomBreasts() {
		return (BreastsPart)getRandom("breasts");
	}

	public BreastsPart getLargestBreasts() {
		List<BodyPart> parts = get("breasts");
		BreastsPart breasts = BreastsPart.flat;
		for (BodyPart part : parts) {
			BreastsPart b = (BreastsPart) part;
			if (b.size > breasts.size) {
				breasts = b;
			}
		}
		return breasts;
	}

	public CockPart getCockBelow(double size) {
		List<BodyPart> parts = get("cock");
		List<CockPart> upgradable = new ArrayList<CockPart>();
		for (BodyPart part : parts) {
			CockPart b = (CockPart) part;
			if (b.size < size) {
				upgradable.add(b);
			}
		}
		if (upgradable.size() == 0) { return null; }

		return upgradable.get(Global.random(upgradable.size()));
	}

	public CockPart getCockAbove(double size) {
		List<BodyPart> parts = get("cock");
		List<CockPart> upgradable = new ArrayList<CockPart>();
		for (BodyPart part : parts) {
			CockPart b = (CockPart) part;
			if (b.size > size) {
				upgradable.add(b);
			}
		}
		if (upgradable.size() == 0) { return null; }

		return upgradable.get(Global.random(upgradable.size()));
	}

	public BreastsPart getBreastsBelow(double size) {
		List<BodyPart> parts = get("breasts");
		List<BreastsPart> upgradable = new ArrayList<BreastsPart>();
		for (BodyPart part : parts) {
			BreastsPart b = (BreastsPart) part;
			if (b.size < size) {
				upgradable.add(b);
			}
		}
		if (upgradable.size() == 0) { return null; }

		return upgradable.get(Global.random(upgradable.size()));
	}

	public BreastsPart getBreastsAbove(double size) {
		List<BodyPart> parts = get("breasts");
		List<BreastsPart> upgradable = new ArrayList<BreastsPart>();
		for (BodyPart part : parts) {
			BreastsPart b = (BreastsPart) part;
			if (b.size > size) {
				upgradable.add(b);
			}
		}
		if (upgradable.size() == 0) { return null; }

		return upgradable.get(Global.random(upgradable.size()));
	}

	public double getHotness(Character self, Character opponent) {
		//represents tempt damage
		double retval = hotness;
		for (BodyPart part : getCurrentParts()) {
			retval += part.isVisible(self) ? part.getHotness(self, opponent) : 0;
		}
		int seductionDiff = Math.max(0, self.get(Attribute.Seduction) - opponent.get(Attribute.Seduction));
		retval += seductionDiff / 10.0;
		return retval;
	}

	public void remove(BodyPart part) {
		bodyParts.remove(part);
	}

	public void removeOne(String type) {
		BodyPart removed = null;
		for (BodyPart part : bodyParts) {
			if (part.isType(type)) {
				removed = part;
				break;
			}
		}
		if (removed != null) {
			bodyParts.remove(removed);
		}
	}

	// returns how many are removed
	public int removeAll(String type) {
		List<BodyPart> removed = new ArrayList<BodyPart>();
		for (BodyPart part : bodyParts) {
			assert(part != null);
			if (part.isType(type)) {
				removed.add(part);
			}
		}
		for (BodyPart part : removed) {
			bodyParts.remove(part);
		}
		return removed.size();
	}

	public CockPart getRandomCock() {
		return (CockPart)getRandom("cock");
	}
	
	public BodyPart getRandomInsertable() {
		BodyPart part = getRandomCock();
		if (part == null && character.has(Trait.strapped))
			part = StraponPart.generic;
		return part;
	}

	public boolean has(String type) {
		return get(type).size() > 0;
	}

	
	public BodyPart getRandom(String type) {
		List<BodyPart> parts = get(type);
		BodyPart part = null;
		if (parts.size() > 0) {
			part = parts.get(Global.random(parts.size()));
		}
		return part;
	}

	public int pleasure(Character opponent, BodyPart with, BodyPart target, int magnitude, Combat c) {
		return pleasure(opponent, with, target, magnitude, 0, c, false);
	}
	public int pleasure(Character opponent, BodyPart with, BodyPart target, int magnitude, int bonus, Combat c) {
		return pleasure(opponent, with, target, magnitude, bonus, c, false);		
	}

	public int pleasure(Character opponent, BodyPart with, BodyPart target, int magnitude, int bonus, Combat c, boolean sub) {
		if (target == null) {
			target = nonePart; 
		}
		if (with == null) {
			with = nonePart;
		}

		double sensitivity = target.getSensitivity(with);
		if(character.has(Trait.desensitized)){
			sensitivity -= .5;
		}
		if(character.has(Trait.desensitized2)){
			sensitivity -= .5;
		}
		double pleasure = with.getPleasure(opponent, target);
		double perceptionBonus = 1.0;
		if (opponent != null) {
			perceptionBonus *= getCharismaBonus(opponent);
			if (opponent.is(Stsflag.alluring)) {
				perceptionBonus += .5;
			}
		}
		double bonusDamage = bonus;
		if (opponent != null) {
			bonusDamage += with.applyBonuses(opponent, character, target, magnitude, c);
			bonusDamage += target.applyReceiveBonuses(character, opponent, with, magnitude, c);
			if (!sub) {
				for (BodyPart p : opponent.body.getCurrentParts()) {
					bonusDamage += p.applySubBonuses(opponent, character, with, target, magnitude, c);
				}
			}
		}
		// double the base damage if the opponent is submissive and in a submissive stance
		if (c.getStance().sub(opponent) && opponent.has(Trait.submissive)) {
			bonusDamage += bonusDamage + magnitude;
		} else if (c.getStance().dom(opponent) && opponent.has(Trait.submissive)) {
			bonusDamage += bonusDamage - (magnitude * 2. / 3.);
		}
		double base = (magnitude + bonusDamage);
		double multiplier = 1 + ((sensitivity - 1) + (pleasure - 1) + (perceptionBonus - 1));
		double damage = base * multiplier;

		int result = (int) Math.round(damage);		
		if (opponent != null) {
			String pleasuredBy = opponent.nameOrPossessivePronoun() + " " + with.describe(opponent);
			if (with == nonePart) {
				pleasuredBy = opponent.subject();
			}
			String firstColor = character.human() ? "<font color='rgb(150,150,255)'>" : "<font color='rgb(255,150,150)'>";
			String secondColor = opponent.human() ? "<font color='rgb(150,150,255)'>" : "<font color='rgb(255,150,150)'>";
			String bonusString = bonusDamage > 0 ? String.format(" + <font color='rgb(255,100,50)'>%.1f<font color='white'>",bonusDamage) : "";
			String battleString = String.format("%s%s %s<font color='white'> was pleasured by %s%s<font color='white'> for <font color='rgb(255,50,200)'>%d<font color='white'> " +
					"base:%.1f (%d%s) x multiplier: %.2f (sen:%.1f + ple:%.1f + per:%.1f)\n",
					firstColor,
					Global.capitalizeFirstLetter(character.nameOrPossessivePronoun()), target.describe(character), secondColor,pleasuredBy, result, base, magnitude, bonusString,
					multiplier, sensitivity - 1, pleasure - 1, perceptionBonus - 1);
			if (c != null)
				c.write(battleString);
		}
		character.pleasure(result, c);
		return result;
	}

	public double getCharismaBonus(Character opponent) {
		// you don't get turned on by yourself
		if (opponent == character) {
			return 1.0;
		} else {
			double perceptionBonus = Math.sqrt(opponent.body.getHotness(opponent, this.character) * (1.0 + (Math.max(0,character.get(Attribute.Perception)) - 5) / 10.0));
			return perceptionBonus;
		}
	}

	public void addReplace(BodyPart part, int max) {
		int n = Math.min(Math.max(1, removeAll(part.getType())), max);
		for (int i = 0; i < n; i++) {
			character.body.add(part);
		}
	}

	public void finishBody(String sex) {
		for (BodyPart part: requiredParts) {
			if (get(part.getType()).size() == 0) {
				add(part);
			}
		}
		if (sex.equals("female") || sex.equals("herm")) {
			if (get("pussy").size() == 0) {
				add(PussyPart.normal);
			}
		}
		if (sex.equals("male") || sex.equals("herm")) {
			if (get("cock").size() == 0) {
				add(CockPart.average);
			}
		}
		if (sex.equals("male")) {
			if (get("balls").size() == 0) {
				add(new GenericBodyPart("balls", 0, 1.0, 1.5, "balls", ""));
			}
		}
	}

	public Body clone() throws CloneNotSupportedException {
		Body newBody = (Body) super.clone();
		return newBody;
	}

	public void save(PrintWriter saver) {
		saver.write("body:\n");
		saver.write(Double.toString(hotness));
		saver.write('\n');
		for (BodyPart part : bodyParts) {
			saver.write(part.getClass().getName());
			saver.write('\n');
			part.save(saver);
			saver.write('\n');
		}
		saver.write("endbody\n");
	}

	public void loadParts(Scanner scanner) {
		String type = scanner.nextLine().trim();
		while (!type.equals("endbody")) {
			BodyPart prototype = prototypes.get(type);
			if (prototype != null) {
				BodyPart part = prototype.load(scanner);
				if (part == null) {
					System.err.println("Failed to load " + type);					
				} else {
					add(part);
				}
			} else {
				System.err.println("Failed to find " + type);
			}
			type = scanner.nextLine().trim();
		}
	}

	public static Body load(Scanner scanner, Character character) {
		while (!scanner.nextLine().trim().equals("body:")) {}
		String line = scanner.nextLine().trim();
		double hotness = Double.valueOf(line);
		Body body = new Body(character, hotness);
		body.loadParts(scanner);
		return body;
	}

	public void tick(Combat c) {
		ArrayList<PartReplacement> expired = new ArrayList<Body.PartReplacement>();
		for (PartReplacement r : replacements) {
			r.duration -= 1;
			if (r.duration <= 0) {
				expired.add(r);
			}
		}
		for (PartReplacement r : expired) {
			replacements.remove(r);
			StringBuilder sb = new StringBuilder();
			if (r.added.size() > 0 && r.removed.size() == 0) {
				sb.append(Global.format("{self:NAME-POSSESSIVE} ", character, character));
				for (BodyPart p : r.added.subList(0, r.added.size() - 1)) {
					sb.append(p.fullDescribe(character)).append(", ");
				}
				if (r.added.size() > 1) {
					sb.append(" and ");
				}
				sb.append(r.added.get(r.added.size()-1).fullDescribe(character));
				sb.append(" disappeared.");
			} else if (r.removed.size() > 0 && r.added.size() == 0) {
				sb.append(Global.format("{self:NAME-POSSESSIVE} ", character, character));
				for (BodyPart p : r.removed.subList(0, r.removed.size() - 1)) {
					sb.append(p.fullDescribe(character)).append(", ");
				}
				if (r.removed.size() > 1) {
					sb.append(" and ");
				}
				sb.append(r.removed.get(r.removed.size()-1).fullDescribe(character));
				sb.append(" reappeared.");
			} else if (r.removed.size() > 0 && r.added.size() > 0) {
				sb.append(Global.format("{self:NAME-POSSESSIVE} ", character, character));
				for (BodyPart p : r.added.subList(0, r.added.size() - 1)) {
					sb.append(p.fullDescribe(character)).append(", ");
				}
				if (r.added.size() > 1) {
					sb.append(" and ");
				}
				sb.append(r.added.get(r.added.size()-1).fullDescribe(character));
				sb.append(" turned back into ");
				for (BodyPart p : r.removed.subList(0, r.removed.size() - 1)) {
					sb.append(p.prefix() + " " + p.fullDescribe(character)).append(", ");
				}
				if (r.removed.size() > 1) {
					sb.append(" and ");
				}
				BodyPart last = r.removed.get(r.removed.size()-1);
				sb.append(last.prefix() + " " + last.fullDescribe(character));
				sb.append(".");
			}
			if (c != null) {
				c.write(character, sb.toString());
			} else if (character.human()){
				Global.gui().message(sb.toString());
			}
		}
	}

	public BodyPart getRandomHole() {
		BodyPart part = getRandomPussy();
		if (part == null )
			part = getRandom("ass");
		return part;
	}

	public void clearReplacements() {
		replacements.clear();
	}

	public int mod(Attribute a, int total) {
		int res = 0;
		for (BodyPart p : getCurrentParts()) {
			total += p.mod(a, total);
		}
		return res;
	}

	public void receiveCum(Combat c, Character opponent, BodyPart part) {
		if (part == null) {
			part = character.body.getRandom("skin");
		}
		if (character.has(Trait.spiritphage)) {
			c.write("<br><b>"+character.subjectAction("glow", "glows") + " with power as the cum is absorbed by " + character.possessivePronoun() + " " + part.describe(character)+"</b>");
			character.add(c, new Abuff(character, Attribute.Power, 5, 10));
			character.add(c, new Abuff(character, Attribute.Seduction, 5, 10));
			character.add(c, new Abuff(character, Attribute.Cunning, 5, 10));
		}
		if (opponent.has(Trait.hypnoticsemen)) {
			c.write(Global.format("<br><b>{other:NAME-POSSESSIVE} hypnotic semen takes its toll on {self:name-possessive} willpower, rendering {self:direct-object} doe-eyed and compliant.</b>",character, opponent));
			character.loseWillpower(c, 10 + Global.random(10));
		}
	}

	public void tickHolding(Combat c, Character opponent, BodyPart selfOrgan,
			BodyPart otherOrgan) {
		selfOrgan.tickHolding(c, character, opponent, otherOrgan);
	}
}