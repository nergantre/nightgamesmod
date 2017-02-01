package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;

public class Enthralled extends DurationStatus {
    private int timesRefreshed;
    private boolean makesCynical;
    public Character master;
    public Enthralled(Character self, Character master, int duration) {
        this(self, master, duration, duration > 1);
    }

    public Enthralled(Character self, Character master, int duration, boolean makesCynical) {
        super("Enthralled", self, duration);
        timesRefreshed = 0;
        if (master.isPet()) {
            master = ((PetCharacter) master).getSelf().owner();
        }
        this.master = master;
        flag(Stsflag.enthralled);
        flag(Stsflag.debuff);
        flag(Stsflag.disabling);
        flag(Stsflag.purgable);
        this.makesCynical = makesCynical;
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        if (replacement.isPresent()) {
            return String.format("%s %s control of %s.\n", master.subjectAction("reinforce", "reinforces"),
                            master.possessiveAdjective(), affected.nameDirectObject());
        } else {
            return String.format("%s now enthralled by %s.\n", affected.subjectAction("are", "is"), master.subject());
        }
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "You feel a constant pull on your mind, forcing you to obey " + master.possessiveAdjective()
                            + " every command.";
        } else {
            return affected.subject() + " looks dazed and compliant, ready to follow "
                                +c.getOpponent(affected).nameOrPossessivePronoun()+" orders.";
        }
    }

    @Override
    public String getVariant() {
        return "enthralled by " + master.getTrueName();
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }

    @Override
    public void replace(Status s) {
        assert s instanceof Enthralled;
        Enthralled other = (Enthralled) s;
        setDuration(Math.max(getDuration() + 1, other.getDuration() - 2 * (timesRefreshed + 1)));
        timesRefreshed += 1;
    }

    @Override
    public boolean mindgames() {
        return true;
    }

    @Override
    public float fitnessModifier() {
        return -getDuration() * 5;
    }

    @Override
    public int mod(Attribute a) {
        if (a == Attribute.Perception) {
            return -5;
        }
        return -2;
    }

    @Override
    public void onRemove(Combat c, Character other) {
        if (makesCynical) {
            affected.addlist.add(new Cynical(affected));
        }
        if (c != null && affected.human()) {
            c.write(affected,
                            "Everything around you suddenly seems much clearer,"
                                            + " like a lens snapped into focus. You don't really remember why"
                                            + " you were heading in the direction you were...");
        } else if (affected.human()) {
            Global.gui().message("Everything around you suddenly seems much clearer,"
                            + " like a lens snapped into focus. You don't really remember why"
                            + " you were heading in the direction you were...");
        }
    }

    @Override
    public int regen(Combat c) {
        super.regen(c);
        return 0;
    }

    @Override
    public void tick(Combat c) {
        if (affected.check(Attribute.Cunning, master.get(Attribute.Seduction) / 2 + master.get(Attribute.Arcane) / 2
                        + master.get(Attribute.Dark) / 2 + 10 + 10 * (getDuration() - timesRefreshed))) {
            if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
                System.out.println("Escaped from Enthralled");
            }
            setDuration(0);
        }
        affected.loseMojo(c, 5, " (Enthralled)");
        affected.loseWillpower(c, 1, 0, false, " (Enthralled)");
        affected.emote(Emotion.horny, 15);
    }

    @Override
    public int damage(Combat c, int paramInt) {
        return 0;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double paramInt) {
        return paramInt / 4;
    }

    @Override
    public int weakened(Combat c, int paramInt) {
        return 0;
    }

    @Override
    public int tempted(Combat c, int paramInt) {
        return paramInt / 4;
    }

    @Override
    public int evade() {
        return -20;
    }

    @Override
    public int escape() {
        return -20;
    }

    @Override
    public int gainmojo(int paramInt) {
        return -paramInt;
    }

    @Override
    public int spendmojo(int paramInt) {
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
        return new Enthralled(newAffected, newOther, getDuration(), makesCynical);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("duration", getDuration());
        obj.addProperty("makesCynical", makesCynical);
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Enthralled(null, null, obj.get("duration").getAsInt(), obj.get("makesCynical").getAsBoolean());
    }
}
