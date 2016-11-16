package nightgames.status;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;

public class Aggressive extends DurationStatus {

    private static final Collection<Skill> CONTACT_SKILLS = Collections.unmodifiableSet(
                    Global.getSkillPool().stream().filter(Skill::makesContact).collect(Collectors.toSet()));

    private String cause;

    public Aggressive(Character affected, String cause, int duration) {
        super("Aggressive", affected, duration);
        this.cause = cause;
        flag(Stsflag.aggressive);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s now aggressive, and cannot use non-physical skills.",
                        affected.subjectAction("are", "is"));
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "Affected by " + cause + ", you are incapable of anything but an all-out assault.";
        }
        return String.format("%s has an aggressive look on %s face: eyes wide open, teeth bared.", affected.name(),
                        affected.possessivePronoun());
    }

    @Override
    public Collection<Skill> allowedSkills(Combat c) {
        return CONTACT_SKILLS.stream()
                        .filter(s -> s.requirements(c, affected, c.getOpponent(affected))
                                        && Skill.skillIsUsable(c, s))
                        .map(s -> s.copy(affected)).collect(Collectors.toSet());
    }

    @Override
    public int mod(Attribute a) {
        if (a == Attribute.Cunning)
            return -3;
        if (a == Attribute.Power)
            return 3;
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
        return -5;
    }

    @Override
    public int escape() {
        return 5;
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
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Aggressive(newAffected, cause, getDuration());
    }

     @Override public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("duration", getDuration());
        obj.addProperty("cause", cause);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Aggressive(null, obj.get("cause").getAsString(), obj.get("duration").getAsInt());
    }
}
