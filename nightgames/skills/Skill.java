package nightgames.skills;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.status.Status;
import nightgames.status.Stsflag;


public abstract class Skill {
	/**
	 * 
	 */
	private String name;
	private Character self;
	protected String image;
	protected String artist;
	private int cooldown;
	public String choice;
	public Skill(String name, Character self){
		this(name, self, 0);
	}
	public Skill(String name, Character self, int cooldown){
		this.name=name;
		this.setSelf(self);
		this.image=null;
		this.artist=null;
		this.cooldown = cooldown;
		this.choice = "";
	}
	public final boolean requirements(Combat c, Character target) {
		return requirements(c, getSelf(), target);
	}
	public abstract boolean requirements(Combat c, Character user, Character target);
	
	public static void filterAllowedSkills(Combat c, Set<Skill> skills, Character user, Character target) {
		boolean filtered = false;
		Set<Skill> stanceSkills = new HashSet<Skill>(c.getStance().availSkills(user));
		
		if (stanceSkills.size() > 0) {
			skills.retainAll(stanceSkills);
			filtered = true;
		}
		Set<Skill> availSkills = new HashSet<Skill>();
		for (Status st : user.status) {
			for (Skill sk : st.allowedSkills(c)) {
				if (skillIsUsable(c, sk, target)) {
					availSkills.add(sk);
				}
			}
		}
		if (availSkills.size() > 0) {
			skills.retainAll(availSkills);
			filtered = true;
		}
		Set<Skill> noReqs = new HashSet<Skill>(); 
		if (!filtered) {
			// if the skill is restricted by status/stance, do not check for requirements
			for (Skill sk : skills) {
				if (!sk.requirements(c, target)) {
					noReqs.add(sk);
				}
			}
			skills.removeAll(noReqs);
		}
	}
	public static boolean skillIsUsable(Combat c, Skill s, Character target) {
		boolean charmRestricted = s.getSelf().is(Stsflag.charmed) && (s.type(c) != Tactics.fucking && s.type(c) != Tactics.pleasure && s.type(c) != Tactics.misc);
		boolean allureRestricted = target.is(Stsflag.alluring) && (s.type(c) == Tactics.damage || s.type(c) == Tactics.debuff);
		boolean usable = s.usable(c, target) && s.getSelf().canSpend(s.getMojoCost(c)) && !charmRestricted && !allureRestricted;
		return usable;
	}

	public int getMojoBuilt(Combat c) {
		return 0;
	}

	public int getMojoCost(Combat c) {
		return 0;
	}

	public abstract boolean usable(Combat c, Character target);
	public abstract String describe(Combat c);
	public abstract boolean resolve(Combat c, Character target);
	public abstract Skill copy(Character user);
	public abstract Tactics type(Combat c);
	public abstract String deal(Combat c,int damage,Result modifier, Character target);
	public abstract String receive(Combat c, int damage,Result modifier, Character target);
	
	public boolean isReverseFuck(Character target) {
		return (target.hasDick() && getSelf().hasPussy());
	}

	public float priorityMod(Combat c) {
		return 0.0f;
	}
	public int accuracy(Combat c){
		return 90;
	}
	public int speed(){
		return 5;
	}

	public String getLabel(Combat c) {
		return getName(c);
	}
	public String getName(){
		return name;
	}
	public String toString(){
		return getName();
	}
	public Character user(){
		return getSelf();
	}
	public void setSelf(Character self){
		this.self=self;
	}
	@Override
	public boolean equals(Object other){
		return this.toString()==other.toString();
	}
	@Override
	public int hashCode() {
		return ("Skill:" + this.toString()).hashCode();
	}
	public String getImage(){
		return image;
	}
	public String getArtist(String image){
		return artist;
	}
	public String getName(Combat c) {
		return toString();
	}

	public boolean makesContact() {
		return false;
	}

	public static void resolve(Skill skill, Combat c, Character target) {
		skill.user().addCooldown(skill);
		skill.user().spendMojo(c, skill.getMojoCost(c));
		//save the mojo built of the skill before resolving it (or the status may change)
		int generated = skill.getMojoBuilt(c);
		
		if (skill.resolve(c, target)) {
			skill.user().buildMojo(c, generated);
		}
	}
	public int getCooldown() {
		return cooldown;
	}
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	public Collection<String> subChoices() {
		return Collections.emptySet();
	}
	public String getTargetOrganType(Combat c, Character target) {
		return "none";
	}
	public String getWithOrganType(Combat c, Character target) {
		return "none";
	}
	public Character getSelf() {
		return self;
	}
}
