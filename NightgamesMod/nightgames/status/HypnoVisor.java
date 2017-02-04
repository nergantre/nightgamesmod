package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class HypnoVisor extends Status {

    private final Character cause;
    
    public HypnoVisor(Character affected, Character cause) {
        super("Wearing Hypno Visor", affected);
        assert affected.human() : "NPC got a hypno visor on them";
        this.cause = cause;
        flag(Stsflag.blinded);
        flag(Stsflag.debuff);
        flag(Stsflag.hypnovisor);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return "The Hypno Visor comes to life, baragging you with images which you can't make sense"
                        + " of, but somehow just seem <i>right</i>.";
    }

    @Override
    public String describe(Combat c) {
        return "The Hypno Visor continues to bombard you with its insidious sights, while"
                        + " preventing you from seeing the real world around you.";
    }

    @Override
    public int mod(Attribute a) {
        if (a == Attribute.Perception) {
            return -3;
        }
        return 0;
    }

    @Override
    public void tick(Combat c) {
        affected.addict(AddictionType.MIND_CONTROL, cause, Addiction.LOW_INCREASE / 2);
        c.write(affected, Global.format("The Hypno Visor is corrupting your mind, rewiring it"
                        + " to follow {other:name-possessive} commands.", affected, cause));
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
        return -25;
    }

    @Override
    public int escape() {
        return -15;
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
        return -20;
    }

    @Override
    public int value() {
        return -10;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new HypnoVisor(newAffected, newOther);
    }

    @Override
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return new HypnoVisor(Global.noneCharacter(), Global.noneCharacter());
    }

}
