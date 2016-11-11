package nightgames.status;

import java.util.function.Supplier;

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

    public DarkChaos() {
        super("Dark Chaos", Global.getPlayer());
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
        float odds = Global.getPlayer().getAddiction(AddictionType.CORRUPTION).map(Addiction::getMagnitude).orElse(0f)
                        / 4;
        if (odds > Math.random()) {
            Effect e = Effect.pick(c);
            e.execute(c);
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
        return new DarkChaos();
    }

     @Override public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new DarkChaos();
    }

    private enum Effect {
        HORNY(() -> new Horny(Global.getPlayer(), 3.f, 5, "Reyka's Corruption"),
                        "The corruption settles into your genitals, inflaming your lusts."),
        HYPER(() -> new Hypersensitive(Global.getPlayer()),
                        "The corruption flows across your skin, leaving it much more sensitive."),
        CHARMED(() -> new Charmed(Global.getPlayer()),
                        "The blackness subverts your mind, making it unthinkable for you to harm your opponent."),
        SHAMED(() -> new Shamed(Global.getPlayer()), "The blackness plants a deep sense of shame in your mind."),
        FALLING(() -> new Falling(Global.getPlayer()),
                        "The darkness interferes with your balance, sending you falling to the ground."),
        FLATFOOTED(() -> new Flatfooted(Global.getPlayer(), 1),
                        "The darkness clouds your mind, distracting you from the fight."),
        FRENZIED(() -> new Frenzied(Global.getPlayer(), 3), "The corruption senses what you're doing, and is compelling"
                        + " you to fuck as hard as you can.");

        private final Supplier<? extends Status> supplier;
        private final String message;

        private Effect(Supplier<? extends Status> supplier, String message) {
            this.supplier = supplier;
            this.message = message;
        }

        boolean possible(Combat c) {
            if (this == FALLING)
                return c.getStance().en == Stance.neutral;
            return true;
        }

        void execute(Combat c) {
            if (this == FALLING)
                c.setStance(new StandingOver(c.getOpponent(Global.getPlayer()), Global.getPlayer()));
            else
                Global.getPlayer()
                      .addlist.add(supplier.get());
            c.write(Global.getPlayer(), message);
        }

        static Effect pick(Combat c) {
            if (c.getStance().inserted(Global.getPlayer()))
                return FRENZIED;
            Effect picked;
            do {
                picked = Global.pickRandom(values());
            } while (!picked.possible(c));
            return picked;
        }
    }
}
