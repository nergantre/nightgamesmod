package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.Primed;

public class AttireShift extends Skill {

    public AttireShift(Character self) {
        super("Attire Shift", self);
        addTag(SkillTag.stripping);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.getPure(Attribute.Temporal) >= 6;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf()) && !target.outfit.isNude()
                        && Primed.isPrimed(getSelf(), 2);
    }

    @Override
    public String describe(Combat c) {
        return "Seperate your opponent from her clothes: 2 charges";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().add(c, new Primed(getSelf(),-2));
        target.nudify();
        writeOutput(c, Result.normal, target);
        getSelf().emote(Emotion.dominant, 15);
        target.emote(Emotion.nervous, 10);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new AttireShift(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format("You trigger a small temporal disturbance, sending %s's clothes a couple seconds back in time. "
                        + "Due to the speed and rotation of the Earth, they probably ended up somewhere over the Pacific Ocean.", target.name());
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s triggers a device on her arm and %s clothes suddenly vanish. "
                        + "What the fuck did %s just do?",getSelf().name(), target.nameOrPossessivePronoun(),
                        getSelf().pronoun());
    }

}
