package nightgames.actions;
import java.io.Serializable;

import nightgames.characters.Character;

public abstract class Action implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4981682001213276175L;
	protected String name;
	public Action(String name){
		this.name=name;
	}
	public abstract boolean usable(Character user);
	public abstract Movement execute(Character user);
	public String toString(){
		return name;
	}
	public abstract Movement consider();
	public boolean freeAction(){
		return false;
	}
}
