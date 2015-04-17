package characters;

import java.io.Serializable;
import java.util.HashSet;

import javax.swing.Icon;

import skills.Skill;

import combat.Combat;
import combat.Result;

import actions.Action;
import actions.Movement;
import areas.Area;

public interface Personality extends Serializable{
	public Skill act(HashSet<Skill> available,Combat c);
	public Action move(HashSet<Action> available,HashSet<Movement> radar);
	public NPC getCharacter();
	public void rest();
	public String bbLiner();
	public String nakedLiner();
	public String stunLiner();
	public String winningLiner();
	public String taunt();
	public String victory(Combat c,Result flag);
	public String defeat(Combat c,Result flag);
	public String victory3p(Combat c,Character target, Character assist);
	public String intervene3p(Combat c,Character target, Character assist);
	public String describe();
	public String draw(Combat c,Result flag);
	public boolean fightFlight(Character opponent);
	public boolean attack(Character opponent);
	public void ding();
	public String startBattle();
	public boolean fit();
	public String night();
	public void advance();
	public boolean checkMood(Emotion mood, int value);
	public String image();
	public void pickFeat();
	public String describeAll();
	public String temptLiner(Character target);
}
