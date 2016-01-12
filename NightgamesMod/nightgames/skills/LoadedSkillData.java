package nightgames.skills;

import java.util.List;

import nightgames.characters.custom.CustomStringEntry;
import nightgames.characters.custom.requirement.CustomRequirement;

public class LoadedSkillData {
	String name;
	float priorityMod;
	public List<CustomRequirement> usableRequirements;
	public List<CustomRequirement> skillRequirements;
	public List<CustomStringEntry> labels;
	public String description;
	public Tactics tactics;
	public int mojoCost;
	public int mojoBuilt;
	public int cooldown;
	public boolean makesContact;
	public int accuracy;
	public int speed;
}
