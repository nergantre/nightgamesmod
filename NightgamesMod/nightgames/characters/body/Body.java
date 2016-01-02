package nightgames.characters.body;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.BodyFetish;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public class Body implements Cloneable {
	static class PartReplacement {
		public List<BodyPart>	added;
		public List<BodyPart>	removed;
		public int				duration;

		public PartReplacement(int duration) {
			added = new ArrayList<BodyPart>();
			removed = new ArrayList<BodyPart>();
			this.duration = duration;
		}

		public PartReplacement(PartReplacement original) {
			added = new ArrayList<BodyPart>(original.added);
			removed = new ArrayList<BodyPart>(original.removed);
			duration = original.duration;
		}
	}

	static private Map<String, BodyPart> prototypes;

	static {
		prototypes = new HashMap<String, BodyPart>();
		prototypes.put(PussyPart.class.getCanonicalName(), PussyPart.normal);
		prototypes.put(BreastsPart.class.getCanonicalName(), BreastsPart.c);
		prototypes.put(BasicCockPart.class.getCanonicalName(),
				BasicCockPart.average);
		// for compatibility with < v1.8.1
		prototypes.put(CockPart.class.getCanonicalName(),
				BasicCockPart.average);
		prototypes.put(ModdedCockPart.class.getCanonicalName(),
				new ModdedCockPart(BasicCockPart.average, CockMod.bionic));
		prototypes.put(WingsPart.class.getCanonicalName(), WingsPart.demonic);
		prototypes.put(TailPart.class.getCanonicalName(), TailPart.cat);
		prototypes.put(EarPart.class.getCanonicalName(), EarPart.normal);
		prototypes.put(StraponPart.class.getCanonicalName(),
				StraponPart.generic);
		prototypes.put(TentaclePart.class.getCanonicalName(),
				new TentaclePart("tentacles", "back", "semen", 0, 1, 1));
		prototypes.put(AssPart.class.getCanonicalName(),
				new AssPart("ass", 0, 1, 1));
		prototypes.put(MouthPart.class.getCanonicalName(),
				new MouthPart("mouth", 0, 1, 1));
		prototypes.put(AnalPussyPart.class.getCanonicalName(),
				new AnalPussyPart());
		prototypes.put(MouthPussyPart.class.getCanonicalName(),
				new MouthPussyPart());
		prototypes.put(GenericBodyPart.class.getCanonicalName(),
				new GenericBodyPart("", 0, 1, 1, "none", "none"));
	}

	// yeah i know :(
	public static BodyPart		nonePart		= new GenericBodyPart("none", 0,
			1, 1, "none", "");
	public static Set<String>	pluralParts		= new HashSet<String>(
			Arrays.asList("hands", "feet", "wings", "breasts", "balls"));
	final static BodyPart		requiredParts[]	= {
			new GenericBodyPart("hands", 0, 1, 1, "hands", ""),
			new GenericBodyPart("feet", 0, 1, 1, "feet", ""),
			new GenericBodyPart("skin", 0, 1, 1, "skin", ""), AssPart.generic,
			MouthPart.generic, BreastsPart.flat, EarPart.normal };
	final static String			fetishParts[]	= { "ass", "feet", "cock",
			"wings", "tail", "tentacles", "breasts" };

	Collection<BodyPart>		bodyParts;
	public double				hotness;
	Collection<PartReplacement>	replacements;
	public Character			character;
	public BodyPart				lastPleasuredBy;
	public BodyPart				lastPleasured;

	public Body() {
		bodyParts = new LinkedHashSet<BodyPart>();
		replacements = new ArrayList<PartReplacement>();
		lastPleasuredBy = nonePart;
		lastPleasured = nonePart;
		hotness = 1.0;
	}

	public Body(Character character) {
		this(character, 1);
	}

	public Body(Character character, double hotness) {
		this();
		this.character = character;
		this.hotness = hotness;

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
		if (character != null) {
			updateCharacter();
		}
	}

	public void temporaryRemovePart(BodyPart part, int duration) {
		PartReplacement replacement = new PartReplacement(duration);
		replacement.removed.add(part);
		replacements.add(replacement);
		if (character != null) {
			updateCharacter();
		}
	}

	public void temporaryAddOrReplacePartWithType(BodyPart part, int duration) {
		temporaryAddOrReplacePartWithType(part, getRandom(part.getType()),
				duration);
	}

	private BodyPart getPartIn(String type, Collection<BodyPart> parts) {
		for (BodyPart p : parts) {
			if (p.isType(type)) {
				return p;
			}
		}
		return null;
	}

	public boolean temporaryAddOrReplacePartWithType(BodyPart part,
			BodyPart removed, int duration) {
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
		if (character != null) {
			updateCharacter();
		}
		return true;
	}

	public void describe(StringBuilder b, Character c, String delimiter) {
		for (BodyPart part : getCurrentParts()) {
			if (part.isVisible(c) && part.isNotable()) {
				int prevLength = b.length();
				part.describeLong(b, c);
				if (prevLength != b.length()) {
					b.append(delimiter);
				}
			}
		}
	}

	public void describeBodyText(StringBuilder b, Character c,
			boolean notableOnly) {
		b.append(Global.format("{self:POSSESSIVE} body has ", c, null));
		BodyPart previous = null;
		for (BodyPart part : getCurrentParts()) {
			if (!notableOnly || part.isNotable()) {
				if (previous != null) {
					b.append(Global.prependPrefix(previous.prefix(),
							previous.fullDescribe(c)));
					b.append(", ");
				}
				previous = part;
			}
		}
		if (previous == null) {
			b.append("nothing notable.<br>");
		} else {
			b.append("and ");
			b.append(Global.prependPrefix(previous.prefix(),
					previous.fullDescribe(c)));
			b.append(".<br>");
		}
	}

	public void add(BodyPart part) {
		assert part != null;
		bodyParts.add(part);
		updateCharacter();
	}

	public void updateCharacter() {
		if (character != null) {
			character.update();
		}
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
		return (PussyPart) getRandom("pussy");

	}

	public WingsPart getRandomWings() {
		return (WingsPart) getRandom("wings");

	}

	public AssPart getRandomAss() {
		return (AssPart) getRandom("ass");
	}

	public BreastsPart getRandomBreasts() {
		return (BreastsPart) getRandom("breasts");
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
			if (b.getSize() < size) {
				upgradable.add(b);
			}
		}
		if (upgradable.size() == 0) {
			return null;
		}

		return upgradable.get(Global.random(upgradable.size()));
	}

	public CockPart getCockAbove(double size) {
		List<BodyPart> parts = get("cock");
		List<CockPart> upgradable = new ArrayList<CockPart>();
		for (BodyPart part : parts) {
			CockPart b = (CockPart) part;
			if (b.getSize() > size) {
				upgradable.add(b);
			}
		}
		if (upgradable.size() == 0) {
			return null;
		}

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
		if (upgradable.size() == 0) {
			return null;
		}

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
		if (upgradable.size() == 0) {
			return null;
		}

		return upgradable.get(Global.random(upgradable.size()));
	}

	public Optional<BodyFetish> getFetish(String part) {
		Optional<Status> fs = character.status.stream().filter(status -> {
			if (status.flags().contains(Stsflag.bodyfetish)) {
				BodyFetish fetish = (BodyFetish) status;
				if (fetish.part.equalsIgnoreCase(part)) {
					return true;
				}
			}
			return false;
		}).findFirst();
		if (fs.isPresent()) {
			return Optional.of((BodyFetish) fs.get());
		} else {
			return Optional.empty();
		}
	}

	public double getHotness(Character self, Character opponent) {
		// represents tempt damage
		double retval = hotness;
		for (BodyPart part : getCurrentParts()) {
			retval += part.getHotness(self, opponent)
					* (getFetish(part.getType()).isPresent() ? 2 : 0);
		}
		retval += self.getOutfit().getHotness();
		int seductionDiff = Math.max(0, self.get(Attribute.Seduction)
				- opponent.get(Attribute.Seduction));
		retval += seductionDiff / 10.0;
		retval *= self.getExposure();
		if (self.is(Stsflag.alluring)) {
			retval *= 1.5;
		}
		return retval;
	}

	public void remove(BodyPart part) {
		bodyParts.remove(part);

		if (character != null) {
			updateCharacter();
		}
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
			if (character != null) {
				updateCharacter();
			}
		}
	}

	// returns how many are removed
	public int removeAll(String type) {
		List<BodyPart> removed = new ArrayList<BodyPart>();
		for (BodyPart part : bodyParts) {
			assert part != null;
			if (part.isType(type)) {
				removed.add(part);
			}
		}
		for (BodyPart part : removed) {
			bodyParts.remove(part);
		}

		if (character != null) {
			updateCharacter();
		}
		return removed.size();
	}

	public int removeTemporaryParts(String type) {
		List<BodyPart> removed = new ArrayList<BodyPart>();
		replacements.removeIf(rep -> rep.added.stream().anyMatch(part -> part.getType().equals(type)));
		return removed.size();
	}
	
	public CockPart getRandomCock() {
		return (CockPart) getRandom("cock");
	}

	public BodyPart getRandomInsertable() {
		BodyPart part = getRandomCock();
		if (part == null && character.has(Trait.strapped)) {
			part = StraponPart.generic;
		}
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

	public int pleasure(Character opponent, BodyPart with, BodyPart target,
			int magnitude, Combat c) {
		return pleasure(opponent, with, target, magnitude, 0, c, false);
	}

	public int pleasure(Character opponent, BodyPart with, BodyPart target,
			int magnitude, int bonus, Combat c) {
		return pleasure(opponent, with, target, magnitude, bonus, c, false);
	}

	public int pleasure(Character opponent, BodyPart with, BodyPart target,
			int magnitude, int bonus, Combat c, boolean sub) {
		if (target == null) {
			target = nonePart;
		}
		if (with == null) {
			with = nonePart;
		}
		if (target.getType().equals("strapon")) {
			return 0;
		}

		double sensitivity = target.getSensitivity(with);
		if (character.has(Trait.desensitized)) {
			sensitivity -= .5;
		}
		if (character.has(Trait.desensitized2)) {
			sensitivity -= .5;
		}
		if (target.isErogenous() && opponent != null
				&& opponent.has(Trait.hairtrigger)) {
			sensitivity += 1;
		}
		double pleasure = 1;
		if (!with.isType("none")) {
			pleasure = with.getPleasure(opponent, target);
		}
		double perceptionBonus = 1.0;
		if (opponent != null) {
			perceptionBonus *= getCharismaBonus(opponent);
		}
		double bonusDamage = bonus;
		if (opponent != null) {
			bonusDamage += with.applyBonuses(opponent, character, target,
					magnitude, c);
			bonusDamage += target.applyReceiveBonuses(character, opponent, with,
					magnitude, c);
			if (!sub) {
				for (BodyPart p : opponent.body.getCurrentParts()) {
					bonusDamage += p.applySubBonuses(opponent, character, with,
							target, magnitude, c);
				}
			}
			// double the base damage if the opponent is submissive and in a
			// submissive stance
			if (c.getStance().sub(opponent) && opponent.has(Trait.submissive)) {
				bonusDamage += bonusDamage + magnitude;
			} else if (c.getStance().dom(opponent)
					&& opponent.has(Trait.submissive)) {
				bonusDamage += bonusDamage - magnitude * 2. / 3.;
			}
		}
		Optional<BodyFetish> fetish = getFetish(with.getType());
		if (fetish.isPresent()) {
			bonusDamage += magnitude * (1 + fetish.get().magnitude);
			character.add(c,
					new BodyFetish(character, opponent, with.getType(), .05));
		}
		double origBase = bonusDamage + magnitude;

		for (Status s : character.status) {
			bonusDamage += s.pleasure(c, origBase);
		}
		double base = magnitude + bonusDamage;
		double multiplier = Math.max(0,
				1 + (sensitivity - 1 + (pleasure - 1) + (perceptionBonus - 1)));
		double damage = base * multiplier;
		double perceptionlessDamage = base
				* (multiplier - (perceptionBonus - 1));

		int result = (int) Math.round(damage);
		if (character.is(Stsflag.rewired)) {
			character.pain(c, result, false, false);
			return 0;
		}
		if (opponent != null) {
			String pleasuredBy = opponent.nameOrPossessivePronoun() + " "
					+ with.describe(opponent);
			if (with == nonePart) {
				pleasuredBy = opponent.subject();
			}
			String firstColor = character.human()
					? "<font color='rgb(150,150,255)'>"
					: "<font color='rgb(255,150,150)'>";
			String secondColor = opponent.human()
					? "<font color='rgb(150,150,255)'>"
					: "<font color='rgb(255,150,150)'>";
			String bonusString = bonusDamage > 0 ? String.format(
					" + <font color='rgb(255,100,50)'>%.1f<font color='white'>",
					bonusDamage) : "";
			String battleString = String.format(
					"%s%s %s<font color='white'> was pleasured by %s%s<font color='white'> for <font color='rgb(255,50,200)'>%d<font color='white'> "
							+ "base:%.1f (%d%s) x multiplier: %.2f (1 + sen:%.1f + ple:%.1f + per:%.1f)\n",
					firstColor,
					Global.capitalizeFirstLetter(
							character.nameOrPossessivePronoun()),
					target.describe(character), secondColor, pleasuredBy,
					result, base, magnitude, bonusString, multiplier,
					sensitivity - 1, pleasure - 1, perceptionBonus - 1);
			if (c != null) {
				c.writeSystemMessage(battleString);
			}
			Optional<BodyFetish> otherFetish = opponent.body
					.getFetish(target.getType());
			if (otherFetish.isPresent()) {
				opponent.tempt(c, character, target,
						(int) Math.round(perceptionlessDamage));
			}
		} else {
			String firstColor = character.human()
					? "<font color='rgb(150,150,255)'>"
					: "<font color='rgb(255,150,150)'>";
			String bonusString = bonusDamage > 0 ? String.format(
					" + <font color='rgb(255,100,50)'>%.1f<font color='white'>",
					bonusDamage) : "";
			String battleString = String.format(
					"%s%s %s<font color='white'> was pleasured for <font color='rgb(255,50,200)'>%d<font color='white'> "
							+ "base:%.1f (%d%s) x multiplier: %.2f (sen:%.1f + ple:%.1f + per:%.1f)\n",
					firstColor,
					Global.capitalizeFirstLetter(
							character.nameOrPossessivePronoun()),
					target.describe(character), result, base, magnitude,
					bonusString, multiplier, sensitivity - 1, pleasure - 1,
					perceptionBonus - 1);
			if (c != null) {
				c.writeSystemMessage(battleString);
			}
		}
		character.pleasure(result, c);

		if (opponent != null
				&& Arrays.asList(fetishParts).contains(with.getType())) {
			if (opponent.has(Trait.fetishTrainer) && Global.random(100) < Math
					.min(opponent.get(Attribute.Fetish), 25)) {
				c.write(character,
						character.subjectAction("now have", "now has")
								+ " a new fetish, courtesy of "
								+ opponent.directObject() + ".");
				character.add(c, new BodyFetish(character, opponent,
						with.getType(), .25));
			}
		}
		lastPleasuredBy = with;
		lastPleasured = target;
		return result;
	}

	public double getCharismaBonus(Character opponent) {
		// you don't get turned on by yourself
		if (opponent == character) {
			return 1.0;
		} else {
			double perceptionBonus = Math.sqrt(opponent.body
					.getHotness(opponent, character)
					* (1.0 + (Math.max(0, character.get(Attribute.Perception))
							- 5) / 10.0));
			if (character.is(Stsflag.lovestruck)) {
				perceptionBonus += 1;
			}
			return perceptionBonus;
		}
	}

	public void addReplace(BodyPart part, int max) {
		int n = Math.min(Math.max(1, removeAll(part.getType())), max);
		for (int i = 0; i < n; i++) {
			add(part);
		}
	}

	public void finishBody(CharacterSex sex) {
		if (sex == CharacterSex.female || sex == CharacterSex.herm) {
			if (get("pussy").size() == 0) {
				add(PussyPart.normal);
			}
			if (get("breasts").size() == 0) {
				add(BreastsPart.b);
			}
		}
		if (sex == CharacterSex.male || sex == CharacterSex.herm) {
			if (get("cock").size() == 0) {
				add(BasicCockPart.average);
			}
		}
		if (sex == CharacterSex.male) {
			if (get("balls").size() == 0) {
				add(new GenericBodyPart("balls", 0, 1.0, 1.5, "balls", ""));
			}
		}
		for (BodyPart part : requiredParts) {
			if (get(part.getType()).size() == 0) {
				add(part);
			}
		}
	}

	@Override
	public Body clone() throws CloneNotSupportedException {
		Body newBody = (Body) super.clone();
		newBody.replacements = new ArrayList<PartReplacement>();
		replacements.forEach(
				rep -> newBody.replacements.add(new PartReplacement(rep)));
		newBody.bodyParts = new ArrayList<>(bodyParts);
		return newBody;
	}

	@SuppressWarnings("unchecked")
	public JSONObject save() {
		JSONObject bodyObj = new JSONObject();
		bodyObj.put("hotness", hotness);
		JSONArray partsArr = new JSONArray();
		for (BodyPart part : bodyParts) {
			JSONObject obj = part.save();
			obj.put("class", part.getClass().getCanonicalName());
			partsArr.add(obj);
		}
		bodyObj.put("parts", partsArr);
		return bodyObj;
	}

	public void loadParts(JSONArray partsArr) {
		for (Object part : partsArr) {
			JSONObject obj = (JSONObject) part;
			String classType = (String) obj.get("class");
			add(prototypes.get(classType).load(obj));
		}
	}

	public static Body load(JSONObject bodyObj, Character character) {
		double hotness = ((Number) bodyObj.get("hotness")).doubleValue();
		Body body = new Body(character, hotness);
		body.loadParts((JSONArray) bodyObj.get("parts"));
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
				sb.append(Global.format("{self:NAME-POSSESSIVE} ", character,
						character));
				for (BodyPart p : r.added.subList(0, r.added.size() - 1)) {
					sb.append(p.fullDescribe(character)).append(", ");
				}
				if (r.added.size() > 1) {
					sb.append(" and ");
				}
				sb.append(r.added.get(r.added.size() - 1)
						.fullDescribe(character));
				sb.append(" disappeared.");
			} else if (r.removed.size() > 0 && r.added.size() == 0) {
				sb.append(Global.format("{self:NAME-POSSESSIVE} ", character,
						character));
				for (BodyPart p : r.removed.subList(0, r.removed.size() - 1)) {
					sb.append(p.fullDescribe(character)).append(", ");
				}
				if (r.removed.size() > 1) {
					sb.append(" and ");
				}
				sb.append(r.removed.get(r.removed.size() - 1)
						.fullDescribe(character));
				sb.append(" reappeared.");
			} else if (r.removed.size() > 0 && r.added.size() > 0) {
				sb.append(Global.format("{self:NAME-POSSESSIVE} ", character,
						character));
				for (BodyPart p : r.added.subList(0, r.added.size() - 1)) {
					sb.append(p.fullDescribe(character)).append(", ");
				}
				if (r.added.size() > 1) {
					sb.append(" and ");
				}
				sb.append(r.added.get(r.added.size() - 1)
						.fullDescribe(character));
				sb.append(" turned back into ");
				for (BodyPart p : r.removed.subList(0, r.removed.size() - 1)) {
					sb.append(Global.prependPrefix(p.prefix(),
							p.fullDescribe(character))).append(", ");
				}
				if (r.removed.size() > 1) {
					sb.append(" and ");
				}
				BodyPart last = r.removed.get(r.removed.size() - 1);

				sb.append(Global.prependPrefix(last.prefix(),
						last.fullDescribe(character)));
				sb.append('.');
			}
			if (c != null) {
				c.writeSystemMessage(character, sb.toString());
			} else if (character.human()) {
				Global.gui().message(sb.toString());
			}
		}
		if (character != null) {
			updateCharacter();
		}
	}

	public BodyPart getRandomHole() {
		BodyPart part = getRandomPussy();
		if (part == null) {
			part = getRandom("ass");
		}
		return part;
	}

	public void clearReplacements() {
		replacements.clear();
		if (character != null) {
			updateCharacter();
		}
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
			c.write(Global.capitalizeFirstLetter(
					"<br><b>" + character.subjectAction("glow", "glows")
							+ " with power as the cum is absorbed by "
							+ character.possessivePronoun() + " "
							+ part.describe(character) + ".</b>"));
			character.add(c, new Abuff(character, Attribute.Power, 5, 10));
			character.add(c, new Abuff(character, Attribute.Seduction, 10, 10));
			character.add(c, new Abuff(character, Attribute.Cunning, 5, 10));
			character.buildMojo(c, 100);
		}
		if (opponent.has(Trait.hypnoticsemen)) {
			c.write(Global.format(
					"<br><b>{other:NAME-POSSESSIVE} hypnotic semen takes its toll on {self:name-possessive} willpower, rendering {self:direct-object} doe-eyed and compliant.</b>",
					character, opponent));
			character.loseWillpower(c, 10 + Global.random(10));
		}
	}

	public void tickHolding(Combat c, Character opponent, BodyPart selfOrgan,
			BodyPart otherOrgan) {
		if (selfOrgan != null && otherOrgan != null) {
			selfOrgan.tickHolding(c, character, opponent, otherOrgan);
			if (character.checkOrgasm()) {
				character.doOrgasm(c, opponent, selfOrgan, otherOrgan);
			}
			if (opponent.checkOrgasm()) {
				opponent.doOrgasm(c, character, otherOrgan, selfOrgan);
			}
		}
	}

	public float penetrationFitnessModifier(boolean pitcher, boolean anal,
			Body other) {
		int totalCounterValue = 0;

		if (anal) {
			if (!pitcher) {
				totalCounterValue += get("ass").stream()
						.flatMapToInt(ass -> other.get("cock").stream()
								.mapToInt(cock -> ass.counterValue(cock)))
						.sum();
			} else {
				totalCounterValue += get("cock").stream()
						.flatMapToInt(cock -> other.get("ass").stream()
								.mapToInt(ass -> cock.counterValue(ass)))
						.sum();
			}
		} else {
			if (!pitcher) {
				totalCounterValue += get("pussy").stream()
						.flatMapToInt(pussy -> other.get("cock").stream()
								.mapToInt(cock -> pussy.counterValue(cock)))
						.sum();
			} else {
				totalCounterValue += get("cock").stream()
						.flatMapToInt(cock -> other.get("pussy").stream()
								.mapToInt(pussy -> cock.counterValue(pussy)))
						.sum();
			}
		}
		return 20 * totalCounterValue;
	}

	public Body clone(Character other) throws CloneNotSupportedException {
		Body res = clone();
		res.character = other;
		return res;
	}

}