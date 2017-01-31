package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Abuff extends DurationStatus {
    private Attribute modded;
    private int value;
    private Character other;

    public static void drain(Combat c, Character drainer, Character drained, 
                    Attribute att, int value, int duration, boolean write) {
        if (drainer.has(Trait.WillingSacrifice) && drained.is(Stsflag.charmed)) {
            value *= 1.5;
        }
        if (drainer.has(Trait.Greedy)) {
            duration *= 1.5;
        }
        int realValue = Math.min(drained.getPure(att) - 
                        (Attribute.isBasic(drained, att) ? 3 : 0), value);
        drainer.add(c, new Abuff(drainer, drained, att, realValue, duration));
        drained.add(c, new Abuff(drained, drainer, att, -realValue, duration));
        if (drainer.has(Trait.RaptorMentis)) {
            drained.drainMojo(c, drainer, Math.max(5, realValue));
        }
        if (write) {
            if (drainer.has(Trait.WillingSacrifice) && drained.is(Stsflag.charmed)) {
                c.write(drainer, Global.format("With {other:name-possessive} mental defences lowered as they are,"
                                + " {self:subject-action:are|is} able to draw in more of {other:possessive} %s than"
                                + " normal."
                                , drainer, drained, att.toString()));
            }
            if (drainer.has(Trait.Greedy)) {
                c.write(drainer, Global.format("{self:SUBJECT-ACTION:suck|sucks} {other:name-possessive} %s"
                                + " deeply into {self:reflective}, holding onto it for longer than usual."
                                , drainer, drained, att.toString()));
            }
            if (drainer.has(Trait.RaptorMentis)) {
                c.write(drainer, Global.format("Additionally, the draining leaves a profound emptiness in its"
                                + " wake, sapping {other:name-possessive} confidence.", drainer, drained));
            }
        }
    }

    public Abuff(Character affected, Character other, Attribute att, int value, int duration) {
        super(String.format("%s %+d", att.toString(), value), affected, duration);
        flag(Stsflag.purgable);
        if (value < 0) {
            flag(Stsflag.debuff);
        }
        this.modded = att;
        this.value = value;
        this.other = other;
    }
    
    public Abuff(Character affected, Attribute att, int value, int duration) {
        this(affected, null, att, value, duration);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        int newValue;
        if (replacement.isPresent()) {
            newValue = ((Abuff)replacement.get()).value;
        } else {
            newValue = this.value;
        }
        if (newValue < 0) {
            return Global.format("{self:pronoun-action:feel|seems} %s{self:if-human: than before}", affected, affected, modded.getLowerPhrase());
        } else {
            return Global.format("{self:pronoun-action:feel|seems} %s{self:if-human: than before}", affected, affected, modded.getRaisePhrase());
        }
    }

    @Override
    public float fitnessModifier() {
        return value / (2.0f * Math.min(1.0f, Math.max(1, affected.getPure(modded)) / 10.0f));
    }

    @Override
    public String describe(Combat c) {
        String person, adjective, modification;

        if (affected.human()) {
            person = "You feel your";
        } else {
            person = affected.getName() + "'s";
        }
        if (Math.abs(value) > 5) {
            adjective = "greatly";
        } else {
            adjective = "";
        }
        if (value > 0) {
            modification = "augmented.";
        } else {
            modification = "sapped.";
        }

        return String.format("%s %s is %s %s\n", person, modded, adjective, modification);
    }

    @Override
    public int mod(Attribute a) {
        if (a == modded) {
            return value;
        }
        return 0;
    }

    public Attribute getModdedAttribute() {
        return modded;
    }

    @Override
    public String getVariant() {
        return "ABUFF:" + modded.toString();
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }

    @Override
    public void replace(Status s) {
        assert s instanceof Abuff;
        Abuff other = (Abuff) s;
        assert other.modded == modded;
        setDuration(Math.max(other.getDuration(), getDuration()));
        value += other.value;
        name = String.format("%s %+d", modded.toString(), value);
    }

    @Override
    public int regen(Combat c) {
        super.regen(c);
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
        return other != null && other.has(Trait.SpecificSapping) && value < 0 ? Math.max(-10, -value) : 0;
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
    public boolean lingering() {
        return true;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Abuff(newAffected, newOther, modded, value, getDuration());
    }

    @Override
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("modded", modded.name());
        obj.addProperty("value", value);
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return new Abuff(null, Attribute.valueOf(obj.get("modded")
                                                    .getAsString()),
                        obj.get("value")
                           .getAsInt(),
                        obj.get("duration")
                           .getAsInt());
    }
}
