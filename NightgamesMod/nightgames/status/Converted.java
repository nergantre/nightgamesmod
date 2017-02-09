package nightgames.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Converted extends Abuff {
    private Attribute converted;

    public Converted(Character affected, Attribute att, Attribute converted, int value, int duration) {
        super(String.format("%s->%s (%d)", converted.name(), att.name(), value), affected, att, value, duration);
        this.converted = converted;
        unflag(Stsflag.purgable);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        int newValue;
        if (replacement.isPresent()) {
            newValue = ((Converted)replacement.get()).value;
        } else {
            newValue = this.value;
        }
        if (newValue < 0) {
            return "";
        } else {
            String message = "";
            List<String> synonyms = Arrays.asList("converted", "transformed", "changed", "altered", "transmuted");
            if (newValue <= 2) {
                // small
                message = "A bit of {self:name-possessive} %s has been %s into %s.";
            } else if (newValue <= 4) {
                // medium
                message = "Some of {self:name-possessive} %s has been %s into %s.";
            } else {
                // large
                message = "Much of {self:name-possessive} %s has been %s into %s.";
            }
            return Global.format(message, affected, affected, converted.getDrainedDO(), Global.pickRandom(synonyms).get(), modded.getDrainerDO());
        }
    }

    @Override
    public String describe(Combat c) {
        String message = "";
        List<String> synonyms = Arrays.asList("converted", "transformed", "changed", "altered", "transmuted");
        if (value <= affected.getPure(converted) / 5) {
            // small
            message = "A bit of {self:name-possessive} %s has been %s into %s.";
        } else if (value <= affected.getPure(converted) / 3) {
            // medium
            message = "Some of {self:name-possessive} %s has been %s into %s.";
        } else if (value <= affected.getPure(converted) * 2 / 3) {
            // large
            message = "Much of {self:name-possessive} %s has been %s into %s.";
        } else {
            // large
            message = "Almost all of {self:name-possessive} %s has been %s into %s.";
        }
        return Global.format(message, affected, affected, converted.getDrainedDO(), Global.pickRandom(synonyms).get(), modded.getDrainerDO());
    }

    @Override
    public int mod(Attribute a) {
        int val = 0;
        if (a == converted) {
            val -= value;
        }
        return val + super.mod(a);
    }

    @Override
    public String getVariant() {
        return "CONVERTED:+" + modded.toString()+ ":-" + converted.toString();
    }

    @Override
    public boolean overrides(Status s) {
        return false;
    }

    @Override
    public void replace(Status s) {
        assert s instanceof Converted;
        Converted other = (Converted) s;
        assert other.modded == modded;
        setDuration(Math.max(other.getDuration(), getDuration()));
        value += other.value;
        name = String.format("%s->%s (%d)", converted.name(), modded.name(), value);
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Converted(newAffected, modded, converted, value, getDuration());
    }

    @Override
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("modded", modded.name());
        obj.addProperty("converted", converted.name());
        obj.addProperty("value", value);
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return new Converted(null, Attribute.valueOf(obj.get("modded")
                        .getAsString()),
                        Attribute.valueOf(obj.get("converted")
                                        .getAsString()),
                        obj.get("value")
                           .getAsInt(),
                        obj.get("duration")
                           .getAsInt());
    }
}
