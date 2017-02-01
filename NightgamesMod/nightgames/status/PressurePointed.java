package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class PressurePointed extends DurationStatus {
    public PressurePointed(Character affected) {
        super("Pressure Point", affected, 5);
        flag(Stsflag.orgasmPurged);
        flag(Stsflag.pressurepoint);
        flag(Stsflag.purgable);
    }

    @Override
    public float fitnessModifier() {
        return -15;
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return Global.format("An indescribable feeling settles inside {self:name-possessive} crotch. "
                        + "While {self:pronoun-action:aren't|isn't} necessarily aroused, "
                        + "it's all {self:pronoun} can do to tighten up {self:possessive} "
                        + "body to avoid cumming instantly. {self:pronoun-action:know} that "
                        + "if {self:pronoun} relaxed for even a second, it'll be all over.", affected, c.getOpponent(affected));
    }

    @Override
    public String describe(Combat c) {
        return Global.format("{self:SUBJECT-ACTION:are} clenching {self:possessive} lower muscles to prevent {self:reflective} from cumming instantly.", affected, c.getOpponent(affected));
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public void tick(Combat c) {
        if (getDuration() <= 0) {
            affected.removelist.add(this);
            c.write(Global.format("The feeling in {self:name-possessive} crotch finally passes and {self:pronoun} can finally relax; whatever {other:SUBJECT} did to {self:direct-object} seems to have subsided.", affected,
                            c.getOpponent(affected)));
        } else if (!affected.canRespond()) {
            c.write(Global.format("<b>Without {self:NAME-DO} actively holding it in, the artifically induced orgasm rips through {self:possessive} body.</b>", affected, c.getOpponent(affected)));
            affected.removelist.add(this);
            affected.doOrgasm(c, c.getOpponent(affected), null, null);
        }
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
    public int weakened(Combat c, int x) {
        return 0;
    }

    @Override
    public int tempted(Combat c, int x) {
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
        return -5;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new PressurePointed(newAffected);
    }

    @Override
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return new PressurePointed(Global.noneCharacter());
    }

}
