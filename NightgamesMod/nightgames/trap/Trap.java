package nightgames.trap;

import nightgames.areas.Deployable;
import nightgames.characters.Character;
import nightgames.combat.IEncounter;

public interface Trap extends Deployable {
	public void trigger(Character target);

	public boolean decoy();

	public boolean recipe(Character owner);

	public boolean requirements(Character owner);

	public String setup(Character owner);

	@Override
	public Character owner();

	@Override
	public String toString();

	public void capitalize(Character attacker, Character victim, IEncounter enc);
}
