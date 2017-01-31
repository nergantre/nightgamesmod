package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class PheromoneBombed extends DurationStatus {

    public PheromoneBombed(Character affected) {
        super("Pheromone Bombed", affected, 4);
        flag(Stsflag.bombed);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return Global.format("{self:SUBJECT} now {self:action:have|has} a %s sticking"
                        + " onto {self:possessive} chest.", affected, c.getOpponent(affected),
                        c.getOpponent(affected).human() ? "primed pheromone bomb" : "creepy-looking sphere");
    }

    @Override
    public String describe(Combat c) {
        switch (getDuration()) {
            case 3:
                return Global.format("{other:NAME-POSSESSIVE} spherical device is sticking to {self:subject}, "
                                + "producing slow but insistent beeps.", affected, c.getOpponent(affected));
            case 2:
                return Global.format("The sphere's beeping is accelerating, and some lights are beginning to flash."
                                + " %s...", affected, c.getOpponent(affected),
                                c.getOpponent(affected).human() ? "Excellent" : "This might be bad");
            case 1:
                return Global.format("A high-pitched whirring sound joins the cacophony of beeps coming"
                                + " from the sphere on {self:name-possessive} chest.", affected, 
                                c.getOpponent(affected));
            case 0:
                return Global.format("<b>A tube-like protrusion extends from the sphere sticking to"
                                + " {self:name-possessive} chest. %s!</b>", affected, c.getOpponent(affected),
                                c.getOpponent(affected).human() ? "Just a few more seconds" :
                                    "If {self:pronoun-action:are|is} planning to remove it, it had best be now");
            default:
                return "<b><<ERROR>> Invalid state for PheromoneBombed!</b>";
        }
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }
    
    @Override
    public void tick(Combat c) {
        if (getDuration() <= 1) {
            affected.removelist.add(this);
            c.write(Global.format("<b>With a last, loud beep, the device on {self:name-possessive} chest"
                            + " releases a pink cloud right into {self:possessive} face. It keeps"
                            + " spewing the cloying substance for several seconds, and "
                            + "{self:pronoun-action:have|has} no choice but to breathe it in. The cloud"
                            + " smells <i>distinctly</i> of {other:subject}, and it is currently"
                            + " turbocharging every nerve in {self:name-possessive} body!</b>", affected,
                            c.getOpponent(affected)));
            affected.arouse(affected.getArousal().max() / 4, c, "(Pheromone Bomb)");
            affected.addlist.add(new Frenzied(affected, 10));
            affected.addlist.add(new Hypersensitive(affected, 10));
        }
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
        return -5;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new PheromoneBombed(newAffected);
    }

    @Override
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return new PheromoneBombed(Global.noneCharacter());
    }

}
