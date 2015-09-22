package nightgames.trap;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Encounter;
import nightgames.global.Global;

public class IllusionTrap implements Trap {
	private Character owner;
	@Override
	public void trigger(Character target) {
		if(target.human()){
			Global.gui().message("You run into a girl you don't recognize, but she's beautiful and completely naked. You don't have a chance to wonder where she came from, because " +
					"she immediately presses her warm, soft body against you and kisses you passionately. She slips a hand between you to grope your crotch and suddenly vanishes. " +
					"She was just an illusion, but your erection is very real.");
		}
		else if(target.location().humanPresent()){
			Global.gui().message("There's a flash of pink light and "+target.name()+" flushes with arousal");
		}
		if(target.has(Trait.imagination)){
			target.tempt(25);
		}
		target.tempt(25);
		target.location().opportunity(target,this);
	}

	@Override
	public boolean decoy() {
		return false;
	}

	@Override
	public boolean recipe(Character owner) {
		return owner.canSpend(15);
	}

	@Override
	public boolean requirements(Character owner) {
		return owner.get(Attribute.Arcane)>=5;
	}

	@Override
	public String setup(Character owner) {
		this.owner=owner;
		owner.spendMojo(null, 15);
		return "You cast a simple illusion that will trigger when someone approaches and seduce them.";
	}

	@Override
	public Character owner() {
		return this.owner;
	}
	public String toString(){
		return "Illusion Trap";
	}
	@Override
	public void capitalize(Character attacker, Character victim, Encounter enc) {
		victim.location().remove(this);
	}
	@Override
	public void resolve(Character active) {
		if(active!=owner){
			trigger(active);
		}
	}
}
