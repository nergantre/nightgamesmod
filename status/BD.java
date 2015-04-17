package status;

import combat.Combat;

import characters.Attribute;
import characters.Character;
import characters.Trait;

public class BD extends Status{
	private int duration;
	public BD(Character affected) {
		super("Bondage", affected);
		flag(Stsflag.bondage);
		if(affected.has(Trait.PersonalInertia)){
			duration = 15;
		}else{
			duration = 10;
		}
	}

	@Override
	public String describe() {
		if(affected.human()){
			return "Fantasies of being tied up continue to dance through your head.";
		}
		else{
			return affected.name()+" is affected by a brief bondage fetish.";
		}
	}
	
	@Override
	public float fitnessModifier () {
		return -1;
	}

	@Override
	public int mod(Attribute a) {
		return 0;
	}

	@Override
	public int regen(Combat c) {
		if(affected.bound()){
			affected.tempt(7);
		}
		duration--;
		if(duration<0){
			affected.removelist.remove(this);
		}
		return 0;
		
	}

	@Override
	public int damage(Combat c, int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int pleasure(Combat c, int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int weakened(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int tempted(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int evade() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int escape() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int gainmojo(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int spendmojo(int x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int counter() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int value() {
		// TODO Auto-generated method stub
		return 0;
	}

}
