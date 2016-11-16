package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;

public class Edge extends Skill {

    public Edge(Character self) {
        super("Edge", self);
        addTag(SkillTag.usesHands);
        addTag(SkillTag.pleasure);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.has(Trait.edger);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().reachBottom(getSelf())
                        && target.crotchAvailable()
                        && target.hasDick() && getSelf().canAct()
                        && (!c.getStance().inserted(target) || c.getStance().en == Stance.anal);
    }

    @Override
    public float priorityMod(Combat c) {
        float mod = 3.f;
        if (getSelf().has(Trait.dexterous) || getSelf().has(Trait.defthands) ||
                        getSelf().has(Trait.limbTraining1)) {
            mod += 2.f;
        }
        if (c.getOpponent(getSelf()).getArousal().percent() >= 100 
                        && c.getOpponent(getSelf()).getArousal().percent() < 300) {
            mod *= 2;
        }
        if (getSelf().getArousal().percent() >= 80) {
            mod /= 3;
        }
        return mod;
    }
    
    @Override
    public String describe(Combat c) {
        return "Get your opponent close to the edge, without pushing them over.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean hit = !target.canAct() || c.getStance().dom(getSelf())
                        || target.roll(getSelf(), c, 80);
        if (!hit) {
            c.write(getSelf(), Global.format("{self:NAME-POSSESSIVE} hands descend towards"
                            + "{other:name-possessive} {other:body-part:cock}, but "
                            + "{other:pronoun} succeeds in keeping them well away.", getSelf(), target));
            return false;
        } else if (target.getArousal().percent() < 100) {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:jerk|jerks} {other:name-possessive}"
                            + " {other:body-part:cock} slowly yet deliberately with both hands.", getSelf(), target));
        } else {
            c.write(getSelf(), Global.format("{other:SUBJECT-ACITON:are|is} already so close to cumming, but"
                            + " {self:name-possessive} hands make such careful, calculated movements all over"
                            + " {other:possessive} {other:body-part:cock} that {other:pronoun} stays"
                            + " <i>just</i> away from that impending peak. "
                            + "{other:PRONOUN-ACTION:<i>do</i>|<i>does</i>} thrash around alot, trying desperately"
                            + " to get that little bit of extra stimulation, and it's draining"
                            + " {other:possessive} energy quite rapidly.", getSelf(), target));
            target.weaken(c, Math.min(30, Global.random((target.getArousal().percent() - 100) / 10)));
        }
        target.tempt(c, getSelf(), getSelf().body.getRandom("hands"), 20 + Global.random(8));
        target.emote(Emotion.horny, 30);
        getSelf().emote(Emotion.confident, 15);
        getSelf().emote(Emotion.dominant, 15);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Edge(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

}
