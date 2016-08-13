package nightgames.combat;

import nightgames.characters.Character;
import nightgames.global.Encs;
import nightgames.trap.Trap;

// C# naming convention, I know, I know
public interface IEncounter {
    boolean battle();

    void engage(Combat c);

    Combat getCombat();

    boolean checkIntrude(Character c);

    void intrude(Character intruder, Character assist);

    void trap(Character opportunist, Character target, Trap trap);

    boolean spotCheck();

    Character getPlayer(int idx);

    void parse(Encs choice, Character primary, Character opponent);

    void parse(Encs choice, Character primary, Character opponent, Trap trap);

    void watch();
}
