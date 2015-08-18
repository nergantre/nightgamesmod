package nightgames.characters.body;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class GenericBodyPart implements BodyPart {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6412667696235300087L;
	public String type;
	public String desc;
	public String prefix;
	public double hotness;
	public double sensitivity;
	public double pleasure;
	public String descLong;
	private boolean notable;

	public GenericBodyPart(String desc, String descLong, double hotness, double pleasure, double sensitivity, boolean notable, String type, String prefix) {
		this.desc = desc;
		this.descLong = descLong;
		this.hotness = hotness;
		this.pleasure = pleasure;
		this.sensitivity = sensitivity;
		this.type = type;
		this.notable = notable;
		this.prefix = prefix;
	}

	public GenericBodyPart(String desc, double hotness, double pleasure, double sensitivity, String type, String prefix) {
		this(desc, "", hotness, pleasure, sensitivity, false, type, prefix);
	}

	public GenericBodyPart(String desc, double hotness, double pleasure, double sensitivity, boolean notable, String type, String prefix) {
		this(desc, "", hotness, pleasure, sensitivity, notable, type, prefix);
	}

	@Override
	public void describeLong(StringBuilder b, Character c) {
		String parsedDesc = Global.format(descLong, c, c);
		b.append(parsedDesc);
	}

	@Override
	public boolean isType(String type) {
		return this.type.equalsIgnoreCase(type);
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String describe(Character c) {
		return desc;
	}

	@Override
	public double priority(Character c) {
		return (this.getPleasure(c, null) - 1) * 3;
	}

	@Override
	public String fullDescribe(Character c) {
		if (notable)
			return desc;
		else
			return "normal " +desc;
	}
	
	@Override
	public String toString() {
		return fullDescribe(null);
	}

	@Override
	public double getHotness(Character self, Character opponent) {
		return hotness;
	}

	@Override
	public double getPleasure(Character self, BodyPart target) {
		double pleasureMod = pleasure;
		if (type.equals("hands") || type.equals("feet")) {
			pleasureMod += self.has(Trait.limbTraining1) ? .5 : 0;
			pleasureMod += self.has(Trait.limbTraining2) ? .7 : 0;
			pleasureMod += self.has(Trait.limbTraining3) ? .7 : 0;
		}
		return pleasureMod;
	}

	@Override
	public double getSensitivity(BodyPart target) {
		return sensitivity;
	}

	@Override
	public boolean isReady(Character self) {
		return true;
	}

	@Override
	public boolean equals(Object other)
	{
		return this.toString().equals(other.toString());
	}

	@Override
	public int hashCode() {
		return (this.type + ":" +this.toString()).hashCode();
	}

	public Map<String,Object> saveToDict() {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("desc",			desc);
		res.put("descLong",		descLong);
		res.put("hotness",		hotness);
		res.put("pleasure",		pleasure);
		res.put("sensitivity",	sensitivity);
		res.put("notable",		notable);
		res.put("type",			type);
		res.put("prefix",		prefix);

		return res;
	}

	public BodyPart loadFromDict(Map<String,Object> dict) {
		try {
		GenericBodyPart part = new GenericBodyPart(
									(String)dict.get("desc"),
									(String)dict.get("descLong"),
									(Double)dict.get("hotness"),
									(Double)dict.get("pleasure"),
									(Double)dict.get("sensitivity"),
									(Boolean)dict.get("notable"),
									(String)dict.get("type"),
									(String)dict.get("prefix"));
			return part;
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	@Override
	public void save(PrintWriter saver) {
		Map<String, Object> dict = saveToDict();
		saver.write("{\n");
		for (String key: dict.keySet()) {
			Object value = dict.get(key);
			if (value instanceof String) {

				saver.write(value.getClass().getCanonicalName()+":" + key + ":\"" + value.toString()+"\"\n");
			} else {
				saver.write(value.getClass().getCanonicalName()+":" + key + ":" + value.toString()+"\n");
			}
		}
		saver.write("}");
	}

	public BodyPart load(Scanner loader) {
		Map<String, Object> dict = new HashMap<String, Object>();
		String line = loader.nextLine(); //trash the first line because it's a '{'
		do {
			line = loader.nextLine().trim();
			String[] params = line.split(":");
			if (params.length < 3) {
				continue;
			}
			String value = line.substring(line.indexOf(":", line.indexOf(":") + 1)+1);
			String key = params[1];
			String type = params[0];
			if (type.equals(String.class.getCanonicalName())) {
				dict.put(key, value.substring(value.indexOf('"')+1, value.lastIndexOf('"')));
			} else {
				try {
					Class<?> c = Class.forName(type);
					Method m = c.getMethod("valueOf", String.class);
					Object result = m.invoke(null, value);
					dict.put(key, result);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		while (!line.equals("}"));
		return loadFromDict(dict);
	}

	@Override
	public double applyBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		return 0;
	}

	@Override
	public String getFluids(Character c) {
		return "";
	}

	@Override
	public boolean isErogenous() {
		return false;
	}

	@Override
	public boolean isNotable() {
		return notable;
	}
	@Override
	public double applyReceiveBonuses(Character self, Character opponent,
			BodyPart target, double damage, Combat c) {
		return 0;
	}

	@Override
	public BodyPart upgrade() {
		return this;
	}

	@Override
	public BodyPart downgrade() {
		return this;
	}

	@Override
	public String prefix() {
		return prefix;
	}
	
	@Override
	public int compare(BodyPart other) {
		return 0;
	}

	@Override
	public boolean isVisible(Character c) {
		return true;
	}

	@Override
	public double applySubBonuses(Character self, Character opponent,
			BodyPart with, BodyPart target, double damage, Combat c) {
		return 0;
	}

	@Override
	public int mod(Attribute a, int total) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void tickHolding(Combat c, Character self, Character opponent, BodyPart otherOrgan) {

	}
}
