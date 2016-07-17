package nightgames.skills;

import java.util.List;

import nightgames.characters.custom.CustomStringEntry;
import nightgames.requirements.Requirement;

public class LoadedSkillData {
    String name;
    float priorityMod;
    public List<Requirement> usableRequirements;
    public List<Requirement> skillRequirements;
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
