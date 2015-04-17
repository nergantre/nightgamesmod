package characters.body;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import global.DebugFlags;
import global.Global;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import status.CockBound;
import status.Stsflag;

import combat.Combat;

public class Body implements Cloneable {
	Collection<BodyPart> bodyParts;
	public double hotness;
	public Character character;
	public static BodyPart nonePart = new GenericBodyPart("none", 0, 1, 1, "none");
	// yeah i know :(
	public Set<String> pluralParts = new HashSet<String>(Arrays.asList(
		"hands", "feet", "wings", "breasts", "balls"
	));
	final static BodyPart requiredParts[] = {
		new GenericBodyPart("hands",0,1,1,"hands"), 
		new GenericBodyPart("feet",0,1,1,"feet"), 
		new GenericBodyPart("skin",0,1,1,"skin"),
		AssPart.generic, MouthPart.generic, BreastsPart.flat
	};
	
	public Body(Character character) {
		this(character, 1);
	}

	public Body(Character character, double hotness) {
		this.character = character;
		bodyParts = new LinkedHashSet<BodyPart>();
		this.hotness = hotness;
	}

	public void describe(StringBuilder b, Character c, String delimiter)
	{
		for (BodyPart part : bodyParts) {
			int prevLength = b.length();
			part.describeLong(b, c);
			if (prevLength != b.length())
				b.append(delimiter);
		}
	}
	
	public void describeNotable(StringBuilder b, Character c)
	{
		b.append(Global.format("{self:POSSESSIVE} body has ", c, null));
		BodyPart previous = null;
		for (BodyPart part : bodyParts) {
			if (part.isNotable()) {
				if (previous != null) {
					if (!pluralParts.contains(previous.getType())) {
						b.append("a ");
					}
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
		return bodyParts.contains(part);
	}
	
	public List<BodyPart> get(String type) {
		List<BodyPart> parts = new ArrayList<BodyPart>(); 
		for (BodyPart part : bodyParts) {
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
		for (BodyPart part : bodyParts) {
			retval *= part.getHotness(self, opponent);
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
		if (target == null) {
			target = nonePart; 
		}
		if (with == null) {
			with = nonePart;
		}

		double sensitivity = target.getSensitivity(with);
		if(character.has(Trait.desensitized)){
			sensitivity *= .7;
		}
		if(character.has(Trait.desensitized2)){
			sensitivity *= .7;
		}
		double pleasure = with.getPleasure(target);
		double perceptionBonus = 1.0;
		if (opponent != null) {
			perceptionBonus *= getCharismaBonus(opponent);
			if (opponent.is(Stsflag.alluring)) {
				perceptionBonus += .5;
			}
		}
		double damage = magnitude * sensitivity * pleasure * perceptionBonus;
		if (opponent != null) {
			damage = with.applyBonuses(opponent, character, target, damage, c);
			damage = target.applyReceiveBonuses(character, opponent, with, damage, c);
		}

		int result = (int) Math.round(damage);		
		if (opponent != null) {
			String pleasuredBy = opponent.nameOrPossessivePronoun() + " " + with.describe(opponent);
			if (with == nonePart) {
				pleasuredBy = opponent.subject();
			}
			String battleString = String.format("%s %s was pleasured by %s for %d (base:%d, mutlipliers: sen:%.1f/ple:%.1f/per:%.1f)\n",
					Global.capitalizeFirstLetter(character.nameOrPossessivePronoun()), target.describe(character), pleasuredBy, result, magnitude,
					sensitivity, pleasure, perceptionBonus);
			if(Global.isDebugOn(DebugFlags.DEBUG_DAMAGE))
				System.out.println(battleString);
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
				add(new GenericBodyPart("balls", 0, 1.0, 1.5, "balls"));
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

	public static Body load(Scanner scanner, Character character) {
		while (!scanner.nextLine().trim().equals("body:")) {}
		String line = scanner.nextLine().trim();
		double hotness = Double.valueOf(line);
		Body body = new Body(character, hotness);
		while (true) {
			String type = scanner.nextLine().trim();
			try {
				Class<?> c = Class.forName(type);
				Method m = c.getMethod("load", Scanner.class);
				Object result = m.invoke(null, scanner);
				if (!(result instanceof BodyPart)) {
					break;
				}
				body.add((BodyPart) result);
			} catch (ClassNotFoundException e) {
				break;
			} catch (NoSuchMethodException e) {
				break;
			} catch (SecurityException e) {
				e.printStackTrace();
				assert(false);
			} catch (IllegalAccessException e) {
				break;
			} catch (IllegalArgumentException e) {
				break;
			} catch (InvocationTargetException e) {
				break;
			}
		}
		return body;
	}
}
