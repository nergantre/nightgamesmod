package nightgames.status;

import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class PartiallyCorrupted extends DurationStatus {

    private static final int THRESHOLD = 3;

    private int counter;
    private final Character cause;

    public PartiallyCorrupted(Character cause) {
        super("Partially Corrupted", Global.getPlayer(), 4);
        counter = 1;
        this.cause = cause;
        flag(Stsflag.partiallyCorrupted);
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
            Global.getPlayer()
                  .addict(AddictionType.CORRUPTION, cause, Addiction.MED_INCREASE);
            // TODO: message?
            Global.getPlayer().removelist.add(this);
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
        return new PartiallyCorrupted(newOther);
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject saveToJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", getClass().getSimpleName());
        obj.put("counter", counter);
        obj.put("cause", cause.getName());
        return obj;
    }

    @Override
    public Status loadFromJSON(JSONObject obj) {
        PartiallyCorrupted pc = new PartiallyCorrupted(Global.getCharacterByType(JSONUtils.readString(obj, "cause")));
        pc.counter = JSONUtils.readInteger(obj, "counter");
        return pc;
    }

}
