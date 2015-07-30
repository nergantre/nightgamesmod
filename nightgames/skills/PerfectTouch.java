package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;

public class PerfectTouch extends Skill {

	public PerfectTouch(Character self) {
		super("Sleight of Hand", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().mobile(getSelf())&&(!target.nude())&&!c.getStance().prone(getSelf())&&getSelf().canAct()&&!c.getStance().penetration(getSelf());
	}

	@Override
	public int getMojoCost(Combat c) {
		return 25;
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(target.roll(this, c, accuracy())){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.normal, target));
				c.write(target,target.nakedLiner());
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.normal, target));
			}
			target.undress(c);
			target.emote(Emotion.nervous, 10);
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return user.get(Attribute.Cunning)>=18;
	}

	@Override
	public Skill copy(Character user) {
		return new PerfectTouch(user);
	}
	public int speed(){
		return 2;
	}
	public int accuracy(){
		return 3;
	}
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return "You try to steal "+target.name()+"'s clothes off of her, but she catches you.";
		}
		else{
			return "You feint to the left while your right hand makes quick work of "+target.name()+"'s clothes. By the time she realizes what's happening, you've " +
					"already stripped all her clothes off.";
		}
				
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.miss){
			return getSelf().name()+" lunges toward you, but you catch her hands before she can get ahold of your clothes.";
		}
		else{
			return getSelf().name()+" lunges towards you, but dodges away without hitting you. She tosses aside a handful of clothes, at which point you realize you're " +
					"naked. How the hell did she manage that?";
		}
				
	}

	@Override
	public String describe() {
		return "Strips opponent completely: 25 Mojo";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
