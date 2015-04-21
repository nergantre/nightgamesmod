package skills;

import items.Item;
import global.Global;
import global.Modifier;
import status.Bound;
import status.Stsflag;
import characters.Character;

import combat.Combat;
import combat.Result;

public class Tie extends Skill {

	public Tie(Character self) {
		super("Bind", self);
	}

	@Override
	public boolean requirements() {
		return true;
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return !target.wary() && self.canAct()&&c.getStance().reachTop(self)&&!c.getStance().reachTop(target)&&(self.has(Item.ZipTie)||self.has(Item.Handcuffs))&&c.getStance().dom(self)&&!target.is(Stsflag.bound)
				&&(!self.human()||Global.getMatch().condition!=Modifier.noitems);
	}

	@Override
	public void resolve(Combat c, Character target) {
		if(self.has(Item.Handcuffs,1)){
			self.consume(Item.Handcuffs,1);
			if(self.human()){
				c.write(self,deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(self,receive(c,0,Result.special, target));
			}
			target.add(new Bound(target,40,"handcuffs"));		
		}
		else{
			self.consume(Item.ZipTie, 1);
			if(target.roll(this, c, accuracy()+self.tohit())){
				if(self.human()){
					c.write(self,deal(c,0,Result.normal, target));
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.normal, target));
				}
				target.add(new Bound(target,20,"ziptie"));		
			}
			else{
				if(self.human()){
					c.write(self,deal(c,0,Result.miss, target));
				}
				else if(target.human()){
					c.write(self,receive(c,0,Result.miss, target));
				}
			}
		}
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
	public int accuracy(){
		return 1;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier == Result.miss){
			return "You try to catch "+target.name()+"'s hands, but she squirms to much to keep your grip on her.";
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
			return self.name()+" tries to tie you down, but you keep your arms free.";
		}
		else if(modifier == Result.special){
			return self.name()+" restrains you with a pair of handcuffs.";
		}
		else{
			return self.name()+" secures your hands with a ziptie.";
		}
	}

	@Override
	public String describe() {
		return "Tie up your opponent's hands with a ziptie";
	}

	@Override
	public boolean makesContact() {
		return true;
	}
}
