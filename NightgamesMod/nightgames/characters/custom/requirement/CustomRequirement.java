package nightgames.characters.custom.requirement;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public interface CustomRequirement {
	boolean meets(Combat c, Character self, Character other);
}
