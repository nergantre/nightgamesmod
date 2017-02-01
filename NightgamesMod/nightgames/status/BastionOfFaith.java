package nightgames.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class BastionOfFaith extends DurationStatus {

    public BastionOfFaith(Character affected) {
        this(affected, 6);
    }

    public BastionOfFaith(Character affected, int duration) {
        super("Bastion of Faith", affected, duration);
        flag(Stsflag.braced);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return String.format("%s divine protection.\n", affected.subjectAction("have", "has"));
    }

    @Override
    public String describe(Combat c) {
        return String.format("%s protected by a divine aura.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public float fitnessModifier() {
        return (10.0f + 10.0f * getDuration()) / 40.f;
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        List<String> possibleStrings = Arrays.asList(
                        "{self:NAME-POSSESSIVE} holy barrier makes it impossible to damage {self:direct-object}.",
                        "{self:NAME-POSSESSIVE} holy barrier is making it impossible to damage {self:direct-object}.",
                        "A golden barrier surrounding {self:name-do} is making it impossible to damage {self:direct-object}."
                        );
        Global.writeIfCombat(c, affected, Global.format(Global.pickRandom(possibleStrings).get(), affected, affected));
        return -x;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }

    @Override
    public int weakened(Combat c, int x) {
        List<String> possibleStrings = Arrays.asList(
                        "{self:NAME-POSSESSIVE} holy barrier is reenergizing {self:direct-object}.",
                        "{self:NAME-POSSESSIVE} holy barrier is buoying up {self:possessive} stamina."
                        );
        Global.writeIfCombat(c, affected, Global.format(Global.pickRandom(possibleStrings).get(), affected, affected));
        return -x;
    }

    @Override
    public int drained(Combat c, int x) {
        List<String> possibleStrings = Arrays.asList(
                        "{self:NAME-POSSESSIVE} holy barrier is preventing {self:direct-object} from being drained.",
                        "{self:NAME-POSSESSIVE} holy barrier prevents {self:direct-object} draining effects.",
                        "A golden barrier surrounding {self:name-do} stops the theft of {self:possessive} stamina."
                        );
        Global.writeIfCombat(c, affected, Global.format(Global.pickRandom(possibleStrings).get(), affected, affected));
        return -x;
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
        return 30 + 30 * getDuration();
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new BastionOfFaith(newAffected);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new BastionOfFaith(null);
    }
}
