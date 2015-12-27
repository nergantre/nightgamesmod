package nightgames.combat;

import nightgames.characters.Character;
import nightgames.global.Encs;
import nightgames.trap.Trap;

public class FTCEncounter implements IEncounter {

	private static final long serialVersionUID = 5190164935968044626L;

	@Override
	public boolean battle() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void engage(Combat c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Combat getCombat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkIntrude(Character c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void intrude(Character intruder, Character assist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trap(Character opportunist, Character target, Trap trap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean spotCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Character getPlayer(int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parse(Encs choice, Character primary, Character opponent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parse(Encs choice, Character primary, Character opponent,
			Trap trap) {
		// TODO Auto-generated method stub
		
	}

	
}
