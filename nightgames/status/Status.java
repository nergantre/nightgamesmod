package nightgames.status;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.skills.Skill;

public abstract class Status implements Cloneable {
	public String name;
	public Character affected;
	protected HashSet<Stsflag> flags;
	public Status(String name, Character affected){
		this.name=name;
		this.affected=affected;
		flags = new HashSet<Stsflag>();
	}
	public String toString(){
		return name;
	}
	
	public Collection<Skill> allowedSkills(){
		return Collections.emptySet();
	}

	public abstract String describe();
	public abstract int mod(Attribute a);
	public abstract int regen(Combat c);
	public abstract int damage(Combat c, int x);
	public abstract int pleasure(Combat c, int x);
	public abstract int weakened(int x);
	public abstract int tempted(int x);
	public abstract int evade();
	public abstract int escape();
	public abstract int gainmojo(int x);
	public abstract int spendmojo(int x);
	public abstract int counter();
	public abstract int value();
	public float fitnessModifier () {
		return 0;
	}
	public boolean lingering(){
		return false;
	}
	public void flag(Stsflag status){
		flags.add(status);
	}
	public HashSet<Stsflag> flags(){
		return this.flags;
	}
	public boolean overrides(Status s) {
		return s.getClass() == this.getClass();
	}

	public void replace(Status newStatus) {
	}

	public boolean mindgames(){
		return false;
	}

	public Object clone () {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract Status instance(Character newAffected, Character newOther);
	public String getVariant() {
		return toString();
	}
	public void eot(Combat c) {}
	public void struggle(Character character) {}
}
