package nightgames.characters.custom.effect;
import nightgames.characters.Character;
import nightgames.combat.Combat;

public interface CustomEffect {
	boolean execute(Combat c, Character self, Character other);
}
