package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.trap.Trap;

public class RoboWebbed extends Bound {
    public RoboWebbed(Character affected, double dc, Trap roboWeb) {
        super("RoboWebbed", affected, dc, "robo-web", roboWeb);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return "";
    }

    @Override
    public String describe(Combat c) {
        return Global.format("{self:SUBJECT-ACTION:are|is} hopelessly tangled up in"
                        + " synthetic webbing, which is sending pleasurable sensations"
                        + " through {self:possessive} entire body.", affected, Global.noneCharacter());
    }

    @Override
    public void tick(Combat c) {
        int dmg = (int) (affected.getArousal().max() * .25);
        // Message handled in describe
        if (c == null && trap.isPresent()) {
            if (affected.human()) {
                Global.gui().message(Global.format("{self:SUBJECT-ACTION:are|is} hopelessly tangled up in"
                                + " synthetic webbing, which is sending pleasurable sensations"
                                + " through {self:possessive} entire body.", affected, Global.noneCharacter()));
            }
            affected.tempt(dmg);
            affected.location().opportunity(affected, trap.get());
        } else {
            affected.temptNoSkillNoTempter(c, dmg);
        }
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new RoboWebbed(newAffected, toughness, trap.orElse(null));
    }

    @Override
    public JsonObject saveToJson() {
        return null;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return null;
    }
}
