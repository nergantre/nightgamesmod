package nightgames.characters.custom;

import java.util.ArrayList;
import java.util.List;

import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.characters.Character;

public class CustomStringEntry {
	/**
	 * Lines that a character can say. Can have requirements attached 
	 */
	
	String line;
	List<CustomRequirement> requirements;
	public CustomStringEntry(String line) {
		this.line = line;
		requirements = new ArrayList<>();
	}

	public boolean meetsRequirements(Combat c, Character self, Character other) {
		for (CustomRequirement req : requirements)
			if (!req.meets(c, self, other))
				return false;
		return true;
	}

	public String getLine(Combat c, Character self, Character other) {
		return Global.format(line, self, other);
	}
}
