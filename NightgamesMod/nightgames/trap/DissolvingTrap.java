package nightgames.trap;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Encounter;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Flatfooted;

public class DissolvingTrap implements Trap {
	private Character owner;
	@Override
	public void trigger(Character target) {
		if(!target.check(Attribute.Perception, 20-target.get(Attribute.Perception) + target.baseDisarm())){
			if(target.human()){
				Global.gui().message("You spot a liquid spray trap in time to avoid setting it off. You carefully manage to disarm the trap and pocket the potion.");
				target.gain(Item.DisSol);
				target.location().remove(this);
			}
		}
		else{
			if(target.human()){
				if(target.reallyNude()){
					Global.gui().message("Your bare foot hits a tripwire and you brace yourself as liquid rains down on you. You hastely do your best to brush the liquid off, " +
							"but after about a minute you realize nothing has happened. Maybe the trap was a dud.");
				}
				else{
					Global.gui().message("You are sprayed with a clear liquid. Everywhere it lands on clothing, it immediately dissolves it, but it does nothing to your skin. " +
							"You try valiantly to save enough clothes to preserve your modesty, but you quickly end up naked.");
				}
			}
			else if(target.location().humanPresent()){
				if(target.reallyNude()){
					Global.gui().message(target.name()+" is caught in your clothes dissolving trap, but she was already naked. Oh well.");
				}
				else{
					Global.gui().message(target.name()+" is caught in your trap and is showered in dissolving solution. In seconds, her clothes vanish off her body, leaving her " +
							"completely nude.");
				}
			}
			target.nudify();
			target.location().opportunity(target,this);
		}
	}

	@Override
	public boolean decoy() {
		return false;
	}

	@Override
	public boolean recipe(Character owner) {
		return owner.has(Item.Tripwire)&&owner.has(Item.DisSol)&&owner.has(Item.Sprayer)&&!owner.has(Trait.direct);
	}

	@Override
	public String setup(Character owner) {
		this.owner=owner;
		owner.consume(Item.Tripwire, 1);
		owner.consume(Item.DisSol, 1);
		owner.consume(Item.Sprayer, 1);
		return "You rig up a trap to dissolve the clothes of whoever triggers it.";
	}

	@Override
	public Character owner() {
		return owner;
	}

	public String toString(){
		return "Dissolving Trap";
	}
	@Override
	public boolean requirements(Character owner) {
		return owner.get(Attribute.Cunning)>=11 && !owner.has(Trait.direct);
	}

	@Override
	public void capitalize(Character attacker, Character victim, Encounter enc) {
		victim.add(new Flatfooted(victim,1));
		enc.engage(new Combat(attacker,victim,attacker.location()));
		attacker.location().trap=null;	
	}
	@Override
	public void resolve(Character active) {
		if(active!=owner){
			trigger(active);
		}
	}
}
