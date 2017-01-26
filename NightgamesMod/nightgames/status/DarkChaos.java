package nightgames.status;

import java.util.function.Function;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.stance.Stance;
import nightgames.stance.StandingOver;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class DarkChaos extends Status {

    public DarkChaos(Character affected) {
        super("Dark Chaos", affected);
        flag(Stsflag.debuff);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return ""; // explained in withdrawal message
    }

    @Override
    public String describe(Combat c) {
        return "The blackness coursing through your soul is looking for ways to hinder you.";
    }

    @Override
    public void tick(Combat c) {
        if (c == null)
            return;
        float odds = affected.getAddiction(AddictionType.CORRUPTION).map(Addiction::getMagnitude).orElse(0f)
                        / 4;
        if (odds > Math.random()) {
            Effect e = Effect.pick(c, affected);
            e.execute(c, affected);
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
        return -10;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new DarkChaos(newAffected);
    }

     @Override public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new DarkChaos(null);
    }

    private enum Effect {
        HORNY((affected) -> new Horny(affected, 3.f, 5, "Reyka's Corruption"),
                        "The corruption settles into your genitals, inflaming your lusts."),
        HYPER((affected) -> new Hypersensitive(affected),
                        "The corruption flows across your skin, leaving it much more sensitive."),
        CHARMED((affected) -> new Charmed(affected),
                        "The blackness subverts your mind, making it unthinkable for you to harm your opponent."),
        SHAMED((affected) -> new Shamed(affected), "The blackness plants a deep sense of shame in your mind."),
        FALLING((affected) -> new Falling(affected),
                        "The darkness interferes with your balance, sending you falling to the ground."),
        FLATFOOTED((affected) -> new Flatfooted(affected, 1),
                        "The darkness clouds your mind, distracting you from the fight."),
        FRENZIED((affected) -> new Frenzied(affected, 3), "The corruption senses what you're doing, and is compelling"
                        + " you to fuck as hard as you can.");

        private final Function<Character, ? extends Status> effect;
        private final String message;

        private Effect(Function<Character, ? extends Status> supplier, String message) {
            this.effect = supplier;
            this.message = message;
        }

        boolean possible(Combat c) {
            if (this == FALLING)
                return c.getStance().en == Stance.neutral;
            return true;
        }

        void execute(Combat c, Character affected) {
            if (this == FALLING)
                c.setStance(new StandingOver(c.getOpponent(affected), affected));
            else
                affected.addlist.add(effect.apply(affected));
            c.write(affected, message);
        }

        static Effect pick(Combat c, Character affected) {
            if (c.getStance().inserted(affected))
                return FRENZIED;
            Effect picked;
            do {
                picked = Global.pickRandom(values()).get();
            } while (!picked.possible(c));
            return picked;
        }
    }
}
