package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class PartiallyCorrupted extends DurationStatus {

    private static final int THRESHOLD = 5;

    private int counter;
    private final Character cause;

    public PartiallyCorrupted(Player affected, Character cause) {
        super("Partially Corrupted", affected, cause.has(Trait.LastingCorruption) ? 6 : 4);
        counter = 1;
        this.cause = cause;
        flag(Stsflag.partiallyCorrupted);
        flag(Stsflag.debuff);
        flag(Stsflag.purgable);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return cause.nameOrPossessivePronoun() + " lips tug on your very soul."
                        + " If this keeps up, you could be in serious trouble!";
    }

    @Override
    public String describe(Combat c) {
        return "The barriers protecting your soul are temporarily weakened by " + cause.nameOrPossessivePronoun()
                        + " lips.";
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
        if (counter > THRESHOLD) {
            ((Player) affected).addict(AddictionType.CORRUPTION, cause,
                            cause.has(Trait.Subversion) ? Addiction.HIGH_INCREASE : Addiction.MED_INCREASE);
            // TODO: message?
            ((Player) affected).removelist.add(this);
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
        return -3;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new PartiallyCorrupted((Player) newAffected, newOther);
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
