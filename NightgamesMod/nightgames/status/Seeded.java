package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.GenericBodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Seeded extends Status {
    private String target;
    private Character other;
    private double time;
    private int stage;

    public Seeded(Character affected, Character other, String hole) {
        super("seeded", affected);
        this.target = hole;
        this.other = other;
        this.stage = 0;
        this.time = 0;
        flag(hole.equals("ass") ? Stsflag.pegged : Stsflag.fucked);
        flag(Stsflag.seeded);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        BodyPart hole = affected.body.getRandom(target);
        if (hole == null) {
            return "";
        }
        return Global.capitalizeFirstLetter(
                        String.format("%s planted a seed in %s %s\n", other.subjectAction("have", "has"),
                                        affected.nameOrPossessivePronoun(), hole.describe(affected)));
    }

    @Override
    public String describe(Combat c) {
        BodyPart hole = affected.body.getRandom(target);
        if (affected.human()) {
            if (stage > 4) {
                return Global.capitalizeFirstLetter(
                                String.format("A large white lilly grows from your %s\n", hole.describe(affected)));
            } else if (stage > 3) {
                return Global.capitalizeFirstLetter(
                                String.format("A small green bud peeks out from your %s\n", hole.describe(affected)));
            }
            return Global.capitalizeFirstLetter(
                            String.format("A lemon-sized seed is lodged firmly in your %s\n", hole.describe(affected)));
        } else {
            if (stage > 4) {
                return Global.capitalizeFirstLetter(String.format(
                                "A large white lilly grows from " + affected.possessivePronoun() + " %s\n",
                                hole.describe(affected)));
            } else if (stage > 3) {
                return Global.capitalizeFirstLetter(String.format(
                                "A small green bud peeks out from " + affected.possessivePronoun() + " %s\n",
                                hole.describe(affected)));
            }
            return Global.capitalizeFirstLetter(String.format(
                            "A lemon-sized seed is lodged firmly in " + affected.possessivePronoun() + " %s\n",
                            hole.describe(affected)));
        }
    }

    @Override
    public float fitnessModifier() {
        return -10;
    }

    @Override
    public int mod(Attribute a) {
        return 0;
    }

    @Override
    public void tick(Combat c) {
        BodyPart hole = affected.body.getRandom(target);
        GenericBodyPart seed = new GenericBodyPart("seedling", 1.0, 1.0, 1.0, "seedling", "a");
        if (hole == null) {
            affected.removelist.add(this);
            return;
        }

        if (time >= 3) {
            if (stage < 3) {
                stage = 3;
                if (!c.shouldAutoresolve())
                Global.gui().message(c, affected,
                                Global.format("{other:name-possessive} seedling has finally flowered. A brilliant white lilly now covers {self:name-possessive} %s, displaying {self:possessive} verdant submission for everyone to see. "
                                                + "While the little seedling has finally stopped sapping your vitality, the now-matured root network has somehow integrated with your nervous system and bloodsteam. As pulses of chemical and electrical obedience wrack {self:possessive} body, "
                                                + "{self:subject-action:know|knows} that {self:pronoun} {self:action:have|has} lost this fight.",
                                affected, other, hole.describe(affected), hole.describe(affected)));
            }
            if (!c.shouldAutoresolve())
            Global.gui().message(c, affected,
                            Global.format("The seedling churns against {self:possessive} inner walls, while sending a chemical cocktail of aphrodisiacs and narcotics directly into {self:possessive} bloodstream. "
                                            + "{self:possessive} mind blanks out as every thought is replaced with a feral need to mate.",
                            affected, other, hole.describe(affected)));
            affected.heal(c, 100);
            affected.arouse(Math.max(Global.random(50, 100), affected.getArousal().max() / 4), c,
                            other.nameOrPossessivePronoun() + " seedling");
            affected.body.pleasure(other, seed, hole, Global.random(10, 20) + other.get(Attribute.Bio) / 2, c);
            affected.add(c, new Frenzied(other, 1000));
        } else if (time >= 2) {
            if (stage < 2) {
                stage = 2;
                if (!c.shouldAutoresolve())
                Global.gui().message(c, affected,
                                Global.format("Having drained enough of {self:name-possessive} essence, the seed shows yet more changes. "
                                                + "The roots growth thicker and more active, now constantly grinding against {self:possessive} walls. "
                                                + "On the other side, a small green bud has poked its head out from inside {self:possessive} %s. "
                                                + "{self:SUBJECT-ACTION:worry|worries} about its implications, but the constant piston motion from your %s is making it hard to concentrate.",
                                affected, other, hole.describe(affected), hole.describe(affected)));
            }
            if (!c.shouldAutoresolve())
            Global.gui().message(c, affected,
                            Global.format("The thick tuber-like roots inside {self:direct-object} constantly shift and scrape against {self:possessive} %s, leaving {self:direct-object} both horny and lenthargic at the same time.",
                                            affected, other, hole.describe(affected)));
            affected.drainStaminaAsMojo(c, other, Global.random(5, 11), 1.0f);
            affected.body.pleasure(other, seed, hole, Global.random(10, 20) + other.get(Attribute.Bio) / 2, c);
        } else if (time >= 1) {
            if (stage < 1) {
                stage = 1;
                if (!c.shouldAutoresolve())
                Global.gui().message(c, affected,
                                Global.format("With a quiet rumble, the seed burried inside {self:name-possessive} %s sprouts thin spindly roots that reach into {self:possessive} innards.",
                                                affected, other, hole.describe(affected)));
            }
            if (!c.shouldAutoresolve())
            Global.gui().message(c, affected,
                            Global.format("{self:SUBJECT-ACTION:feel|feels} slow as the thin threadlike roots latch onto your inner walls and seem to leech your vigor.",
                                            affected, other, hole.describe(affected)));
            affected.drainStaminaAsMojo(c, other, Global.random(2, 6), 1.0f);
        } else {
            if (!c.shouldAutoresolve())
            Global.gui().message(c, affected, Global.format("The seed sits uncomfortably in {self:possessive} %s.",
                            affected, other, hole.describe(affected)));
            affected.pain(c, 1, false, false);
        }

        affected.emote(Emotion.desperate, 10);
        affected.emote(Emotion.nervous, 10);
        time += .25;
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
        return -5;
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

    public String toString() {
        return "Seeded";
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Seeded(newAffected, newOther, target);
    }

     public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("target", target);
        return obj;
    }

    public Status loadFromJson(JsonObject obj) {
        return new Seeded(null, null, obj.get("target").getAsString());
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }
}
