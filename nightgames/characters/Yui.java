package nightgames.characters;

import nightgames.actions.Action;
import nightgames.actions.Movement;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Modifier;
import nightgames.items.Clothing;
import nightgames.items.Item;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

import java.util.HashSet;

public class Yui extends BasePersonality {
	public Yui(){
		super();
		character = new NPC("Yui",1,this);
		character.outfit[0].add(Clothing.bra);
		character.outfit[0].add(Clothing.Tshirt);
		character.outfit[1].add(Clothing.panties);
		character.outfit[1].add(Clothing.skirt);
		character.closet.add(Clothing.bra);
		character.closet.add(Clothing.Tshirt);
		character.closet.add(Clothing.panties);
		character.closet.add(Clothing.skirt);
		character.change(Modifier.normal);
		character.setTrophy(Item.YuiTrophy);
		character.plan = Tactics.hunting;
		character.mood = Emotion.confident;
		character.body.add(BreastsPart.c);
		character.body.add(PussyPart.normal);
		character.body.finishBody("female");
		}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Action move(HashSet<Action> available, HashSet<Movement> radar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rest() {
		// TODO Auto-generated method stub

	}

	@Override
	public String bbLiner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String nakedLiner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stunLiner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String winningLiner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String taunt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String victory(Combat c, Result flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String defeat(Combat c, Result flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String victory3p(Combat c, Character target, Character assist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String intervene3p(Combat c, Character target, Character assist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String describe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String draw(Combat c, Result flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean fightFlight(Character opponent) {
		return !character.nude()||opponent.nude();
	}

	@Override
	public boolean attack(Character opponent) {
		return true;
	}

	@Override
	public String startBattle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean fit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String night() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void advance() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean checkMood(Emotion mood, int value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String temptLiner(Character target) {
		// TODO Auto-generated method stub
		return "";
	}
	@Override
	public String orgasmLiner() {
		return "Reyka shudders, <i>\"Mmm it's been a while since I've felt that. Here, I'll return the favor\"</i>";
	}

	@Override
	public String makeOrgasmLiner() {
		return "With a devilish smile, Reyka brings her face close to yours <i>\"Mmmmm that smells great! Too bad I'm still pretty hungry.\"</i>";
	}
}
