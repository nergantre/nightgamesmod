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

public class Braced extends DurationStatus {

    public Braced(Character affected, int duration) {
        super("Braced", affected, duration);
        flag(Stsflag.braced);
    }

    public Braced(Character affected) {
        this(affected, 4);
        flag(Stsflag.braced);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return String.format("%s now braced.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public String describe(Combat c) {
        return "";
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
        List<String> possibleStrings = Arrays.asList("Since {self:subject-action:are} already down, it doesn't make much of a difference.");
        if (affected.canRespond()) {
            possibleStrings = Arrays.asList(
                        "Prepared for the blow, {self:subject-action:manage} to avoid taking most of the damage.",
                        "Being wary now, {self:subject-action:avoid} most of the attack.",
                        "Once bitten twice shy, {self:subject} only {self:action:take} a glancing blow."
                        );
        }
        c.write(affected, Global.format(Global.pickRandom(possibleStrings).get(), affected, affected));
        return -x * 3 / 4;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }

    @Override
    public int weakened(Combat c, int x) {
        List<String> possibleStrings = Arrays.asList("Since {self:subject-action:are} already down, there's not much to weaken.");
        if (affected.canRespond()) {
            possibleStrings = Arrays.asList(
                        "Being wary now, {self:subject-action:manage} to conserve most of {self:possessive} stamina.",
                        "Being more careful now, {self:subject-action:avoid} manages to conserve most of {self:possessive} stamina."
                        );
        }
        c.write(affected, Global.format(Global.pickRandom(possibleStrings).get(), affected, affected));
        return -x * 3 / 4;
    }

    @Override
    public int drained(Combat c, int x) {
        List<String> possibleStrings = Arrays.asList("Since {self:subject-action:are} already down, there's not much to drain.");
        if (affected.canRespond()) {
            possibleStrings = Arrays.asList(
                            "Being wary now, {self:subject-action:avoid} manages to hold on to most of {self:possessive} stamina.",
                            "Being more careful now, {self:subject-action:avoid} manages to prevent most of the theft of {self:possessive} stamina."
                            );
        }
        c.write(affected, Global.format(Global.pickRandom(possibleStrings).get(), affected, affected));
        return -x * 3 / 4;
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
        return new Braced(newAffected);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Braced(null);
    }
}
