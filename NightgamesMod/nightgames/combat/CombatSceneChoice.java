package nightgames.combat;

import nightgames.characters.Character;
import nightgames.characters.custom.effect.CustomEffect;

public class CombatSceneChoice {
    private final String message;
    private final CustomEffect effect;
    public CombatSceneChoice(String choice, CustomEffect effect) {
        this.message = choice;
        this.effect = effect;
    }
    
    public String getChoice() {
        return message;
    }

    public void choose(Combat c, Character npc) {
        effect.execute(c, npc, c.getOther(npc));
    }
}
