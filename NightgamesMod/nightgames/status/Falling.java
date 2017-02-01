package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.stance.StandingOver;

public class Falling extends Status {
    public Falling(Character affected) {
        super("Falling", affected);
        flag(Stsflag.falling);
        flag(Stsflag.debuff);
    }

    @Override
    public String describe(Combat c) {
        return "";
    }

    @Override
    public float fitnessModifier() {
        return -20;
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return String.format("%s knocked off balance.\n", affected.subjectAction("are", "is"));
    }

    @Override
    public int regen(Combat c) {
        affected.removelist.add(this);
        if (c.getStance().havingSex(c) && c.getStance().dom(affected) && c.getStance().reversable(c)) {
            c.write(c.getOpponent(affected), Global.format("{other:SUBJECT-ACTION:take|takes} the chance to shift into a more dominant position.", affected, c.getOpponent(affected)));
            c.setStance(c.getStance().reverse(c, true));
        } else if (!c.getStance().prone(affected)) {
            c.setStance(new StandingOver(c.getOpponent(affected), affected));
        }
        if (affected.has(Trait.NimbleRecovery)) {
            c.write(Global.format("{self:NAME-POSSESSIVE} nimble body expertly breaks the fall.", affected, c.getOpponent(affected)));
            affected.add(c, new Stunned(affected, 0, true));
        } else if (affected.has(Trait.Unwavering)) {
            c.write(Global.format("{self:SUBJECT-ACTION:go|goes} down but the fall seems to hardly affect {self:direct-object}.", affected, c.getOpponent(affected)));
        } else {
            affected.add(c, new Stunned(affected));            
        }
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
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Falling(newAffected);
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Falling(null);
    }
}
