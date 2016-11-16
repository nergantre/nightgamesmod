package nightgames.skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;

public class LeechEnergy extends Skill {
    String lastPart;

    public LeechEnergy(Character self) {
        super("Leech Energy", self, 2);
        lastPart = "none";
        addTag(SkillTag.drain);
        addTag(SkillTag.staminaDamage);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && getSelf().body.has("tentacles");
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 0;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(getSelf(), c, accuracy(c))) {
            BodyPart part = null;
            BodyPart selfPart = getSelf().body.getRandom("tentacles");
            List<String> targets = new ArrayList<String>(
                            Arrays.asList("hands", "feet", "skin", "mouth", "cock", "pussy", "balls"));
            while (!targets.isEmpty()) {
                String type = targets.remove(Global.random(targets.size()));
                part = target.body.getRandom(type);
                if (part != null) {
                    lastPart = type;
                    break;
                }
            }
            if (part == null) {
                c.write(getSelf(), "<b>ERROR: Could not pick part in LeechEnergy!</b>");
                return false;
            }
            String partString = selfPart.describe(getSelf());
            String partStringSingular = partString.substring(0, partString.length() - 1);
            if (part.isType("hands")) {
                c.write(getSelf(),
                                Global.format("{self:name-possessive} numerous " + selfPart.describe(getSelf())
                                                + " latch onto {other:name-possessive} hands and swallow up {other:possessive} fingers. While the "
                                                + selfPart.describe(getSelf())
                                                + " are lasciviously licking {other:possessive} digits, "
                                                + "{other:subject-action:start|starts} feeling weak as {other:possessive} energy is being drained.",
                                getSelf(), target));
            } else if (part.isType("feet")) {
                c.write(getSelf(),
                                Global.format("{self:name-possessive} numerous " + selfPart.describe(getSelf())
                                                + " latch onto {other:name-possessive} legs and swallow up {other:possessive} feet. While the numerous bumps and ridges inside the "
                                                + selfPart.describe(getSelf())
                                                + " are squeezing and pulling on {other:possessive} ankles, "
                                                + "{other:subject-action:start|starts} feeling weak as {other:possessive} energy is being drained through your toes.",
                                getSelf(), target));
            } else if (part.isType("skin")) {
                c.write(getSelf(),
                                Global.format("{self:name-possessive} numerous " + selfPart.describe(getSelf())
                                                + " latch onto {other:name-possessive} body and coils around {other:possessive} waist. The numerous tips on the "
                                                + selfPart.describe(getSelf())
                                                + " feel like tiny mouths nibbling on your skin as they suck the energy from {other:possessive} body.",
                                getSelf(), target));
            } else if (part.isType("mouth")) {
                c.write(getSelf(),
                                Global.format("A thick " + partStringSingular
                                                + " latches onto {other:name-possessive} mouth and violates {other:possessive} oral cavity. {other:NAME-POSSESSIVE} mouth feels as if the "
                                                + partStringSingular
                                                + " is deep kissing {other:direct-object} as {other:possessive} energy flows through the connection.",
                                getSelf(), target));
            } else if (part.isType("cock")) {
                c.write(getSelf(),
                                Global.format("A particularly thick " + partStringSingular
                                                + " latches onto {other:name-possessive} cock and swallows it whole. {other:SUBJECT-ACTION:gasp|gasps} in pleasure as the "
                                                + partStringSingular
                                                + "-pussy churns against {other:possessive} cock relentlessly, sucking out both precum and {other:possessive} precious energy.",
                                getSelf(), target));
            } else if (part.isType("balls")) {
                c.write(getSelf(),
                                Global.format("A particularly thick " + partStringSingular
                                                + " latches onto {other:name-possessive} balls and swallows it whole. {other:SUBJECT-ACTION:gasp|gasps} in pleasure as the "
                                                + partStringSingular
                                                + "-mouth sucks and chews on {other:possessive} balls relentlessly, sucking out what little fight {other:subject-action:have|has}.",
                                getSelf(), target));
            } else if (part.isType("pussy")) {
                c.write(getSelf(),
                                Global.format("A particularly thick " + partStringSingular
                                                + " latches onto {other:name-possessive} pussy and plunges inside. {other:SUBJECT-ACTION:gasp|gasps} in pleasure as the "
                                                + partStringSingular
                                                + "-cock thrusts in and out of {other:direct-object} relentlessly, draining {other:direct-object} of energy and replacing it with "
                                                + selfPart.getFluids(getSelf()) + ".", getSelf(), target));
            } else {
                c.write(getSelf(), "Wtf happened");
            }
            target.drainStaminaAsMojo(c, getSelf(), 10 + Global.random(20), 1.5f);
            target.body.pleasure(getSelf(), selfPart, part, 10 + Global.random(20), c, this);
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().get(Attribute.Bio) >= 10;
    }

    @Override
    public Skill copy(Character user) {
        return new LeechEnergy(user);
    }

    @Override
    public int speed() {
        return 5;
    }

    @Override
    public int accuracy(Combat c) {
        return 80;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String getLabel(Combat c) {
        if (getSelf().get(Attribute.Dark) >= 1) {
            return "Drain energy";
        } else {
            return getName(c);
        }
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            BodyPart selfPart = getSelf().body.getRandom("tentacles");
            return "You try to drain energy with your " + selfPart.describe(getSelf()) + ", but " + target.name()
                            + " dodges out of the way.";
        }
        return "";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        BodyPart selfPart = getSelf().body.getRandom("tentacles");

        if (modifier == Result.miss) {
            return String.format("%s tries to drain energy with %s %s, but %s out of the way.",
                            getSelf().subject(), getSelf().possessivePronoun(),
                            selfPart.describe(getSelf()), target.subjectAction("dodge"));
        }
        return "";
    }

    @Override
    public String describe(Combat c) {
        return "Drains your opponent of energy with your tentacles";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
