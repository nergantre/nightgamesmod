package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.skills.CounterBase;

public class CounterStatus extends DurationStatus {
    private CounterBase skill;
    private String desc;

    public CounterStatus(Character affected, CounterBase skill, String description) {
        this(affected, skill, description, 0);
    }

    public CounterStatus(Character affected, CounterBase skill, String description, int duration) {
        super("Counter", affected, duration);
        this.skill = skill;
        desc = description;
        flag(Stsflag.counter);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return String.format("%s ready for a counter.\n", affected.subjectAction("get", "gets"));
    }

    @Override
    public String describe(Combat c) {
        return desc;
    }

    @Override
    public float fitnessModifier() {
        return .5f;
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
        return -100;
    }

    @Override
    public int value() {
        return 0;
    }

    public void resolveSkill(Combat c, Character target) {
        affected.removelist.add(this);
        skill.resolveCounter(c, target);
    }

    public CounterBase getCounterSkill() {
        return skill;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new CounterStatus(newAffected, skill, desc, getDuration());
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        // TODO Support this once skill loading is in the game
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        // TODO Support this once skill loading is in the game
        throw new UnsupportedOperationException();
    }
}
