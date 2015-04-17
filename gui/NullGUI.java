package gui;

import java.util.Observable;

import skills.Skill;

import combat.Combat;
import combat.Encounter;

import characters.Character;
import characters.Player;

public class NullGUI extends GUI {
	public NullGUI(){
		setVisible(false);
	}
	public Combat beginCombat(Character p1,Character p2){
		combat = new Combat(p1,p2,p1.location());
		combat.addObserver(this);
		return combat;
	}
	public void populatePlayer(Player player){
		
	}
	public void clearText(){
	}
	public void message(String text){
	}
	public void clearCommand(){
	}
	public void addSkill(Skill action,Combat com){
	}
	public void setPlayer(Player player){
	}
	public void next(Combat combat){
	}
	public void promptFF(Encounter enc){
	}
	public void promptAmbush(Encounter enc,Character target){
	}
	public void update(Observable arg0, Object arg1) {
		if(combat!=null){
			if(combat.phase==0||combat.phase==2){
				combat.next();
			}
		}
	}
	public void endCombat(){
		combat=null;
	}
}
