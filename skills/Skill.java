package skills;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

import status.Stsflag;

import combat.Combat;
import combat.Result;

import characters.Character;


public abstract class Skill{
	/**
	 * 
	 */
	protected String name;
	protected Character self;
	protected String image;
	protected String artist;
	public Skill(String name, Character self){
		this.name=name;
		this.self=self;
		this.image=null;
		this.artist=null;
	}
	public abstract boolean requirements();
	public abstract boolean requirements(Character user);
	public static boolean skillIsUsable(Combat c, Skill s, Character target) {
		boolean charmRestricted = s.self.is(Stsflag.charmed) && (s.type(c) != Tactics.fucking && s.type(c) != Tactics.pleasure && s.type(c) != Tactics.misc);
		boolean allureRestricted = target.is(Stsflag.alluring) && (s.type(c) == Tactics.damage || s.type(c) == Tactics.debuff);
		return s.usable(c, target) && !charmRestricted && !allureRestricted;
	}

	public abstract boolean usable(Combat c, Character target);
	public abstract String describe();
	public abstract void resolve(Combat c, Character target);
	public abstract Skill copy(Character user);
	public abstract Tactics type(Combat c);
	public abstract String deal(Combat c,int damage,Result modifier, Character target);
	public abstract String receive(Combat c, int damage,Result modifier, Character target);
	
	public boolean isReverseFuck(Character target) {
		return (target.hasDick() && self.hasPussy());
	}

	public float priorityMod(Combat c) {
		return 0.0f;
	}
	public int accuracy(){
		return 5;
	}
	public int speed(){
		return 5;
	}
	public String toString(){
		return name;
	}
	public Character user(){
		return self;
	}
	public void setSelf(Character self){
		this.self=self;
	}
	public void setName(String name){
		this.name=name;
	}
	@Override
	public boolean equals(Object other){
		return this.toString()==other.toString();
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
}
