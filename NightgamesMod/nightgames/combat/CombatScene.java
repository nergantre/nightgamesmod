package nightgames.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.global.Global;
import nightgames.requirements.Requirement;

public class CombatScene {
    public static interface StringProvider {
        String provide(Combat c, Character self, Character other);
    }
    private StringProvider message;
    private List<CombatSceneChoice> choices;
    private Requirement requirement;

    public CombatScene(Requirement requirement, StringProvider message, Collection<CombatSceneChoice> choices) {
        this.choices = new ArrayList<>(choices);
        this.message = message;
        this.requirement = requirement;
    }

    public void visit(Combat c, Character npc) {
        c.write("<br/>");
        c.write(message.provide(c, npc, c.getOpponent(npc)));
        c.updateAndClearMessage();
        choices.forEach(choice -> Global.gui().choose(c, npc, choice.getChoice(), choice));
    }

    public boolean meetsRequirements(Combat c, NPC npc) {
        return requirement.meets(c, npc, c.getOpponent(npc));
    }
}