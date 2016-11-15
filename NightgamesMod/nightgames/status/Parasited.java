package nightgames.status;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.FootWorship;
import nightgames.skills.Masturbate;
import nightgames.stance.Engulfed;
import nightgames.stance.Kneeling;

public class Parasited extends Status {
    private Character other;
    private double time;
    private int stage;

    public Parasited(Character affected, Character other) {
        super("parasited", affected);
        this.other = other;
        this.stage = 0;
        this.time = 0;
        flag(Stsflag.parasited);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return Global.format(
                        "{other:SUBJECT-ACTION:have|has} planted a part of {other:reflective} in {self:name-possessive} head!\n", affected, other);
    }

    @Override
    public String describe(Combat c) {
        return String.format("%s a part of %s inside of %s head.", affected.subjectAction("have", "has"),
                        other.nameOrPossessivePronoun(), affected.possessivePronoun());
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
        if (time >= 3) {
            if (stage < 3) {
                stage = 3;
                Global.gui().message(c, other,
                                Global.format("Suddenly, {self:pronoun-action:hear|hears} a disembodied but familiar voice. \"Testing... testing... Ah good, looks like it has worked.\"",
                                affected, other));
                Global.gui().message(c, affected,
                                Global.format("{self:SUBJECT}... {self:action:seem|seems} to be hearing {other:name-possessive} voice inside {self:possessive} head. That's not good.",
                                affected, other));
                Global.gui().message(c, other,
                                Global.format("{other:NAME} gives {self:name-do} a satisfied smile and {other:possessive} disembodied voice echoes again inside {self:possessive} head, \"{self:NAME}, don't worry... I have connected myself with your brain... We will have so much fun together...\"",
                                affected, other));
                
            }
            switch(Global.random(8)) {
                case 0:
                    Global.gui().message(c, other,
                                    Global.format("\"...You will cum for me...\"",
                                    affected, other));
                    Global.gui().message(c, affected,
                                    Global.format("With absolutely no warning, {self:subject-action:feel|feels} an incredible orgasm rip through {self:possessive} body.",
                                    affected, other));
                    BodyPart part = Global.pickRandom(c.getStance().partsFor(c, affected)).orElse(affected.body.getRandomGenital());
                    BodyPart otherPart = Global.pickRandom(c.getStance().partsFor(c, other)).orElse(other.body.getRandom("skin"));
                    affected.doOrgasm(c, other, part, otherPart);
                    break;
                case 1:
                    Global.gui().message(c, other,
                                    Global.format("\"...Give yourself to me...\"",
                                    affected, other));
                    Global.gui().message(c, affected,
                                    Global.format("With no input from {self:possessive} consciousness, {self:name-possessive} body mechanically walks up to {self:name-possessive} body and presses itself into {other:possessive} slime. While immobilized by {self:possessive} inability to send signals through {self:possessive} locomotive nerves, {self:name-possessive} body slowly sinks into {other:name-possessive} crystal blue body.",
                                    affected, other));
                    c.setStance(new Engulfed(other, affected));
                    break;
                case 2:
                case 3:
                    Global.gui().message(c, other,
                                    Global.format("\"...You will please me...\"",
                                    affected, other));
                    Global.gui().message(c, affected,
                                    Global.format("{self:SUBJECT-ACTION:feel|feels} an immense need to service {self:NAME}!",
                                    affected, other));
                    c.getRandomWorshipSkill(affected, other).orElse(new FootWorship(affected)).resolve(c, other);
                    break;
                case 4:
                case 5:
                    if (!c.getStance().dom(affected) && !c.getStance().prone(affected)) {
                        Global.gui().message(c, other,
                                        Global.format("\"...You will kneel for me...\"",
                                        affected, other));
                        c.setStance(new Kneeling(other, affected));
                        break;
                    }
                case 6:
                case 7:
                default:
                    Global.gui().message(c, other,
                                    Global.format("\"...You will pleasure yourself...\"",
                                    affected, other));
                    Global.gui().message(c, affected,
                                    Global.format("{self:name-possessive} hands involunarily reach into {self:possessive} crotch and start masturbating!",
                                    affected, other));
                    (new Masturbate(affected)).resolve(c, other);
            }
            affected.add(c, new Frenzied(affected, 1000));
        } else if (time >= 2) {
            if (stage < 2) {
                stage = 2;
                if (!c.shouldAutoresolve())
                Global.gui().message(c, affected,
                                Global.format("The parasite inside {self:subject} starts moving again. After a long journey, it has somehow reached inside {self:possessive} skull. Even though that part of {self:possessive} body should have no nerves, {self:pronoun-action:swear|swears} {self:pronoun} can feel its cold pseudopods integrating themselves with {self:possessive} brain.",
                                affected, other));
            }
            if (!c.shouldAutoresolve())
                Global.gui().message(c, affected,
                                Global.format("{self:NAME-POSSESSIVE} thoughts slow down even further. It's becoming difficult to remember why {self:pronoun-action:are|is} even fighting in the first place.",
                                                affected, other));
            affected.loseWillpower(c, 2);
        } else if (time >= 1) {
            if (stage < 1) {
                stage = 1;
                if (!c.shouldAutoresolve())
                Global.gui().message(c, affected,
                                Global.format("The slimey parasite inside {self:name-possessive} starts moving again. {self:PRONOUN} can feel it crawling through {self:possessive} head.",
                                                affected, other));
            }
            if (!c.shouldAutoresolve())
            Global.gui().message(c, affected,
                            Global.format("{self:NAME-POSSESSIVE} thoughts slow down. Somehow the parasite is sapping {self:possessive} will to fight.",
                                            affected, other));
            affected.loseWillpower(c, 1);
        } else {
            if (!c.shouldAutoresolve())
            Global.gui().message(c, affected, Global.format("A part of {other:name-possessive} slime is lodged inside {self:name-possessive} head. It doesn't feel uncomfortable, but {self:pronoun-action:are|is} scared of the implications.",
                            affected, other));
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
        return "Parasited";
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Parasited(newAffected, newOther);
    }

     public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        return obj;
    }

    public Status loadFromJson(JsonObject obj) {
        return new Parasited(null, null);
    }

    @Override
    public int regen(Combat c) {
        return 0;
    }
}
