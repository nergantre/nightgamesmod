package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Winded extends DurationStatus {
    public Winded(Character affected) {
        this(affected, 3);
        flag(Stsflag.disabling);
    }

    public Winded(Character affected, int duration) {
        super("Winded", affected, duration);
        flag(Stsflag.stunned);
        flag(Stsflag.purgable);
        flag(Stsflag.debuff);
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "You need a moment to catch your breath";
        } else {
            return affected.getName() + " is panting and trying to recover";
        }
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return String.format("%s now winded.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public float fitnessModifier() {
        return -.3f;
    }

    @Override
    public int mod(Attribute a) {
        if (a == Attribute.Power || a == Attribute.Speed) {
            return -2;
        } else {
            return 0;
        }
    }

    @Override
    public void onRemove(Combat c, Character other) {
        if (c.getStance().mobile(affected)) {
            if (affected.get(Attribute.Divinity) > 0) {
                affected.addlist.add(new BastionOfFaith(affected));
            } else {
                affected.addlist.add(new Braced(affected));
            }
        }
        affected.addlist.add(new Wary(affected, 3));
        affected.heal(c, affected.getStamina().max(), " (Recovered)");
    }

    @Override
    public int regen(Combat c) {
        super.regen(c);
        affected.emote(Emotion.nervous, 15);
        affected.emote(Emotion.angry, 10);
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        Global.writeIfCombat(c, affected, Global.format("Since {self:subject-action:are} already down, there's not much more that can be done.", affected, affected));
        return -x;
    }

    @Override
    public int weakened(Combat c, int x) {
        Global.writeIfCombat(c, affected, Global.format("Since {self:subject-action:are} already down, there's not much more that can be done.", affected, affected));
        return -x;
    }

    @Override
    public int drained(Combat c, int x) {
        Global.writeIfCombat(c, affected, Global.format("Since {self:subject-action:are} already down, there's not much to take.", affected, affected));
        return -x;
    }

    @Override
    public int tempted(Combat c, int x) {
        Global.writeIfCombat(c, affected, Global.format("%s, {self:subject-action:are} already unconscious.", affected, affected, affected.human() ? "Fortunately" : "Unfortunately"));
        return -x;
    }

    @Override
    public int evade() {
        return -10;
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
        return -10;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Winded(newAffected);
    }

    @Override public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        //Winded constructor can't handle nulls
        throw new UnsupportedOperationException();
        //return new Winded(null);
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }
}
