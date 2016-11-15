package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Behind;
import nightgames.status.Primed;

public class EmergencyJump extends Skill {

    public EmergencyJump(Character self) {
        super("Emergency Jump", self);
        addTag(SkillTag.positioning);
        addTag(SkillTag.escaping);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.getPure(Attribute.Temporal) >= 4;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return ((c.getStance()
                  .sub(getSelf())
                        && !c.getStance()
                             .mobile(getSelf())
                        && !c.getStance()
                             .penetrated(c, getSelf())
                        && !c.getStance()
                             .penetrated(c, target))
                        || getSelf().bound()) && !getSelf().stunned() && !getSelf().distracted()
                        && Primed.isPrimed(getSelf(), 2);
    }

    @Override
    public String describe(Combat c) {
        return "Escape from a disadvantageous position and/or bind: 2 charges";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().add(new Primed(getSelf(),-2));
        getSelf().free();
        c.setStance(new Behind(getSelf(),target));
        if(getSelf().human()){
            c.write(getSelf(),deal(c,0,Result.normal,target));
        }
        else if(target.human()){
            c.write(getSelf(),receive(c,0,Result.normal,target));
        }
        getSelf().emote(Emotion.confident, 15);
        target.emote(Emotion.nervous, 15);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new EmergencyJump(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "You're in trouble for a moment, so you trigger your temporal manipulator and stop time just long "
                                        + "enough to free yourself.");
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "%s thought %s had %s right where %s wanted %s, but %s seems to vanish completely and escape.",
                        target.subject(), target.pronoun(), getSelf().name(), 
                        target.pronoun(), getSelf().directObject(), getSelf().pronoun());
    }

}
