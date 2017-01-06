package nightgames.status;

import java.util.Collection;
import java.util.Collections;

import com.google.gson.JsonObject;

import nightgames.actions.Action;
import nightgames.actions.ControlledMasturbation;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Masturbate;
import nightgames.skills.Skill;

public class RemoteMasturbation extends DurationStatus {

    private final Character controller;

    public RemoteMasturbation(Character affected, Character controller) {
        super("Remote Masturbation", affected, 10);
        flag(Stsflag.trance);
        flag(Stsflag.purgable);
        this.controller = controller;
    }

    @Override
    public Collection<Action> allowedActions() {
        return Collections.singleton(new ControlledMasturbation());
    }

    @Override
    public Collection<Skill> allowedSkills(Combat c) {
        return Collections.singleton(new Masturbate(affected));
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return ""; // handled in RemoteControl trap
    }

    @Override
    public String describe(Combat c) {
        return controller.subject() + " is remotely directing " + affected.nameOrPossessivePronoun()
                        + " hands to masturbate fiercely.";
    }
    
    @Override
    public boolean mindgames() {
        return true;
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        return 0;
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return c == null ? -x / 2 * 3 : 0; // We don't want masturbation to end to quickly.
                                           // It would be more fun if the victim were discovered
                                           // while helpless.
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
        return -8;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new RemoteMasturbation(newAffected, newOther);
    }

    @Override
    public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override
    public Status loadFromJson(JsonObject obj) {
        int duration = obj.get("duration").getAsInt();
        RemoteMasturbation instance = new RemoteMasturbation(Global.noneCharacter(), Global.noneCharacter());
        instance.setDuration(duration);
        return instance;
    }

}
