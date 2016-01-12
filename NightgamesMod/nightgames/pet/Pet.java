package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;

public abstract class Pet {
	private String name;
	private Character owner;
	private Ptype type;
	protected int power;
	protected int ac;
	
	public Pet(String name,Character owner,Ptype type, int power, int ac){
		this.owner=owner;
		this.name=name;
		this.type=type;
		this.power=power;
		this.ac=ac;
	}
	public String toString(){
		return name;
	}
	public Character owner(){
		return owner;
	}
	public String own(){
		if(owner.human()){
			return "Your ";
		}
		else{
			return owner.name()+"'s ";
		}
	}
	public abstract String describe();
	public abstract void act(Combat c, Character target);
	public abstract void vanquish(Combat c,Pet opponent);
	public abstract void caught(Combat c,Character captor);
	public void remove(){
		owner.pet=null;
	}
	public Ptype type(){
		return type;
	}
	public int power(){
		return power;
	}
	public int ac(){
		return ac;
	}
	public boolean hasDick() {
		return false;
	}
	public boolean hasPussy() {
		return false;
	}

}
