package nightgames.skills;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.global.DefaultModifier;
import nightgames.items.Item;
import nightgames.status.Bound;
import nightgames.status.Stsflag;

public class Tie extends Skill {

	public Tie(Character self) {
		super("Bind", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && getSelf().canAct()&&c.getStance().reachTop(getSelf())&&!c.getStance().reachTop(target)&&(getSelf().has(Item.ZipTie)||getSelf().has(Item.Handcuffs))&&c.getStance().dom(getSelf())&&!target.is(Stsflag.bound)
				&&(!getSelf().human()||Global.getMatch().condition!=DefaultModifier.noitems);
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		if(getSelf().has(Item.Handcuffs,1)){
			getSelf().consume(Item.Handcuffs,1);
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.special, target));
			}
			target.add(c, new Bound(target,75,"handcuffs"));		
		}
		else{
			getSelf().consume(Item.ZipTie, 1);
			if(target.roll(this, c, accuracy(c))){
				if(getSelf().human()){
					c.write(getSelf(),deal(c,0,Result.normal, target));
				}
				else if(target.human()){
					c.write(getSelf(),receive(c,0,Result.normal, target));
				}
				target.add(c, new Bound(target,50,"ziptie"));		
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
		}
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Tie(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}
	public int speed(){
		return 2;
	}
	public int accuracy(Combat c){
		return 80;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You try to catch "+target.name()+"'s hands, but she squirms too much to keep your grip on her.";
		}
		else if(modifier == Result.special){
			return "You catch "+target.name()+"'s wrists and slap a pair of cuffs on her.";
		}
		else{
			return "You catch both of "+target.name()+" hands and wrap a ziptie around her wrists.";
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return getSelf().name()+" tries to tie you down, but you keep your arms free.";
		}
		else if(modifier == Result.special){
			return getSelf().name()+" restrains you with a pair of handcuffs.";
		}
		else{
			return getSelf().name()+" secures your hands with a ziptie.";
		}
	}

	@Override
	public String describe(Combat c) {
		return "Tie up your opponent's hands with a ziptie";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
