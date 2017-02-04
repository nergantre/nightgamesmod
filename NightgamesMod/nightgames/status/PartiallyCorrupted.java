package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class PartiallyCorrupted extends DurationStatus {
    private static final int THRESHOLD = 5;

    private int counter;
    private final Character cause;

    public PartiallyCorrupted(Character affected, Character cause) {
        super("Partially Corrupted", affected, cause.has(Trait.LastingCorruption) ? 6 : 4);
        counter = 1;
        this.cause = cause;
        flag(Stsflag.partiallyCorrupted);
        flag(Stsflag.debuff);
        flag(Stsflag.purgable);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        if (counter > THRESHOLD) {
            affected.addict(c, AddictionType.CORRUPTION,
                            cause, cause.has(Trait.Subversion) ? Addiction.HIGH_INCREASE : Addiction.MED_INCREASE);
            counter = 0;
            return Global.format("{other:NAME-POSSESSIVE} lips have finally broke through {self:possessive} resistance and planted a bit of {other:possessive} darkness inside {self:possessive} very soul!", affected, cause);
        } else {
            return Global.format("You {self:if-human:feel}{self:if-nonhuman:almost see} {other:NAME-POSSESSIVE} lips tug on {self:name-possessive} very soul. If this keeps up, {self:pronoun} could be in serious trouble!", affected, cause);
        }
    }

    public float fitnessModifier() {
        if (counter == 0) {
            //hack to get her to want to do the final kiss.
            return -300;
        }
        return -50 * counter;
    }

    @Override
    public String describe(Combat c) {
        if (counter > 0) {
            return Global.format("The barriers protecting {self:name-possessive} soul are temporarily weakened by {other:name-possessive} lips.", affected, cause);
        } else {
            return "";
        }
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }

    @Override
    public void replace(Status s) {
        assert s instanceof PartiallyCorrupted;
        PartiallyCorrupted other = (PartiallyCorrupted) s;
        setDuration(Math.max(other.getDuration(), getDuration()));
        counter += other.counter;
    }

    public void tick(Combat c) {
        if (counter <= 0) {
            affected.removelist.add(this);
        }
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int regen(Combat c) {
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
        return -3;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new PartiallyCorrupted(newAffected, newOther);
    }

    @Override
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("counter", counter);
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        PartiallyCorrupted pc = new PartiallyCorrupted(null, null);
        pc.counter = obj.get("counter")
                        .getAsInt();
        return pc;
    }

}
