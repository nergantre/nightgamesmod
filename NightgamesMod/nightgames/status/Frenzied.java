package nightgames.status;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.AssFuck;
import nightgames.skills.Carry;
import nightgames.skills.Fly;
import nightgames.skills.Fuck;
import nightgames.skills.Invitation;
import nightgames.skills.LegLock;
import nightgames.skills.ReverseAssFuck;
import nightgames.skills.ReverseCarry;
import nightgames.skills.ReverseFly;
import nightgames.skills.ReverseFuck;
import nightgames.skills.Shove;
import nightgames.skills.Skill;
import nightgames.skills.Straddle;
import nightgames.skills.SubmissiveHold;
import nightgames.skills.Tackle;
import nightgames.skills.Tear;
import nightgames.skills.ToggleKnot;
import nightgames.skills.Undress;
import nightgames.skills.WildThrust;

public class Frenzied extends DurationStatus {

    private static final Collection<Skill> FUCK_SKILLS = new HashSet<>();

    static {
        // Skills that either lead to penetration, or can be used during it.
        Character p = Global.noneCharacter();
        FUCK_SKILLS.add(new AssFuck(p));
        FUCK_SKILLS.add(new Carry(p));
        FUCK_SKILLS.add(new Shove(p));
        FUCK_SKILLS.add(new Tackle(p));
        FUCK_SKILLS.add(new Straddle(p));
        FUCK_SKILLS.add(new Tear(p));
        FUCK_SKILLS.add(new Undress(p));
        FUCK_SKILLS.add(new Fly(p));
        FUCK_SKILLS.add(new Fuck(p));
        FUCK_SKILLS.add(new Invitation(p));
        FUCK_SKILLS.add(new WildThrust(p));
        FUCK_SKILLS.add(new ReverseAssFuck(p));
        FUCK_SKILLS.add(new ReverseCarry(p));
        FUCK_SKILLS.add(new ReverseFly(p));
        FUCK_SKILLS.add(new ReverseFuck(p));
        FUCK_SKILLS.add(new SubmissiveHold(p));
        FUCK_SKILLS.add(new ToggleKnot(p));
    }

    public Frenzied(Character affected, int duration) {
        super("Frenzied", affected, duration);
        flag(Stsflag.frenzied);
        flag(Stsflag.purgable);
    }

    @Override
    public String initialMessage(Combat c, boolean replaced) {
        return String.format("%s mind blanks, leaving only the bestial need to breed.",
                        affected.nameOrPossessivePronoun());
    }

    @Override
    public String describe(Combat c) {
        if (affected.human()) {
            return "You cannot think about anything other than fucking all those around.";
        } else {
            return String.format("%s has a frenzied look in %s eyes, interested in nothing but raw, hard sex.",
                            affected.name(), affected.possessivePronoun());
        }
    }

    @Override
    public int mod(Attribute a) {
        if (a == Attribute.Cunning) {
            return -5;
        }
        if (a == Attribute.Power) {
            return 8;
        }
        if (a == Attribute.Animism) {
            return 8;
        }
        return 0;
    }

    @Override
    public void onRemove(Combat c, Character other) {
        affected.addlist.add(new Cynical(affected));
    }

    @Override
    public boolean mindgames() {
        return true;
    }

    @Override
    public int regen(Combat c) {
        super.regen(c);
        affected.buildMojo(c, 25);
        affected.emote(Emotion.horny, 15);
        return 0;
    }

    @Override
    public int damage(Combat c, int x) {
        return (int) (-x * 0.2);
    }

    @Override
    public double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x) {
        return 0;
    }

    @Override
    public int weakened(int x) {
        return (int) (-x * 0.2);
    }

    @Override
    public int tempted(int x) {
        return (int) (x * 0.2);
    }

    @Override
    public int evade() {
        return -10;
    }

    @Override
    public int escape() {
        return -10;
    }

    @Override
    public int gainmojo(int x) {
        return (int) (x * 1.25);
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
        return 0;
    }

    @Override
    public void tick(Combat c) {
        if (c == null) {
            affected.removelist.add(this);
            affected.removeStatusNoSideEffects();
        } else if (!c.getStance().inserted(affected)) {
            affected.removelist.add(this);
        } else {
            setDuration(getDuration() + 2);
        }
    }

    @Override
    public Status instance(Character newAffected, Character newOther) {
        return new Frenzied(newAffected, getDuration());
    }

    @Override
    public Collection<Skill> allowedSkills(Combat c) {
        // Gather the preferred skills for which the character meets the
        // requirements
        return FUCK_SKILLS.stream().filter(s -> s.requirements(c, affected, c.getOpponent(affected)))
                        .map(s -> s.copy(affected)).collect(Collectors.toSet());
    }

    @Override  public JsonObject saveToJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getClass().getSimpleName());
        obj.addProperty("duration", getDuration());
        return obj;
    }

    @Override public Status loadFromJson(JsonObject obj) {
        return new Frenzied(null, obj.get("duration").getAsInt());
    }
}
