package nightgames.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.global.Global;
import nightgames.requirements.Requirement;

public class CombatScene {
    private String message;
    private List<CombatSceneChoice> choices;
    private Requirement requirement;

    public CombatScene(Requirement requirement, String message, Collection<CombatSceneChoice> choices) {
        this.choices = new ArrayList<>(choices);
        this.message = message;
        this.requirement = requirement;
    }

    public void visit(Combat c, Character npc) {
        Global.gui().message(message);
        choices.forEach(choice -> Global.gui().choose(c, npc, choice.getChoice(), choice));
    }

    public boolean meetsRequirements(Combat c, NPC npc) {
        return requirement.meets(c, npc, c.getOther(npc));
    }
}