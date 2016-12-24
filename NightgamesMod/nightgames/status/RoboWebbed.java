package nightgames.status;

import java.util.Collection;
import java.util.Collections;

import com.google.gson.JsonObject;

import nightgames.actions.Action;
import nightgames.actions.Wait;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Nothing;
import nightgames.skills.Skill;
import nightgames.trap.RoboWeb;

public class RoboWebbed extends DurationStatus {

    private final RoboWeb trap;
    
    public RoboWebbed(Character affected, RoboWeb roboWeb) {
        super("RoboWebbed", affected, 10);
        trap = roboWeb;
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return ""; // done in trap
    }

    @Override
    public String describe(Combat c) {
        return Global.format("{self:SUBJECT-ACTION:are|is} hopelessly tangled up in"
                        + " synthetic webbing, which is sending pleasurable sensations"
                        + " through {self:possessive} entire body.", affected, Global.noneCharacter());
    }
    
    @Override
    public boolean lingering() {
        return true;
    }
    
    @Override
    public void tick(Combat c) {
        int dmg = (int) (affected.getArousal().max() * .25);
        // Message handled in describe
        if (c == null) {
            affected.tempt(dmg);
            affected.location().opportunity(affected, trap);
        } else {
            affected.temptNoSkillNoTempter(c, dmg);
        }
    }
    
    @Override
    public Collection<Action> allowedActions() {
        return Collections.singleton(new Wait());
    }

    @Override
    public Collection<Skill> allowedSkills(Combat c) {
        return Collections.singleton(new Nothing(affected));
    }
    
    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        return 0;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }

    @Override
    public int weakened(int x) {
        return 0;
    }

    @Override
    public int tempted(int x) {
        return 0;
    }

    @Override
    public int evade() {
        return 0;
    }

    @Override
    public int escape() {
        return 0;
    }

    @Override
    public int gainmojo(int x) {
        return 0;
    }

    @Override
    public int spendmojo(int x) {
        return 0;
    }

    @Override
    public int counter() {
        return 0;
    }

    @Override
    public int value() {
        return -10;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new RoboWebbed(newAffected, trap);
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
