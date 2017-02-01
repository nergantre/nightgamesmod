package nightgames.status;

import java.util.Optional;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;

// Just for the description, really
public class PlayerSlimeDummy extends Status {

    public PlayerSlimeDummy(Player affected) {
        super("Player Slime Dummy", affected);
    }

    @Override
    public String initialMessage(Combat c, Optional<Status> replacement) {
        return ""; // handled in Player#resolveOrgasm
    }

    @Override
    public String describe(Combat c) {
        return "You have switched into your slime form due to the intense situation.";
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
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new PlayerSlimeDummy((Player)newAffected);
    }

    @Override
    public JsonObject saveToJson() {
        // should never be saved?
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        return new PlayerSlimeDummy(null);
    }

}
