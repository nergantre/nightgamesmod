package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.Bound;

public class Binding extends Skill {

    public Binding(Character self) {
        super("Binding", self, 4);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Arcane) >= 9;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return !target.wary() && !c.getStance().sub(getSelf()) && !c.getStance().prone(getSelf())
                        && !c.getStance().prone(target) && getSelf().canAct();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 20;
    }

    @Override
    public String describe(Combat c) {
        return "Bind your opponent's hands with a magic seal.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        target.add(c, new Bound(target, Math.min(10 + 3 * getSelf().get(Attribute.Arcane), 70), "seal"));
        target.emote(Emotion.nervous, 5);
        getSelf().emote(Emotion.confident, 20);
        getSelf().emote(Emotion.dominant, 10);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Binding(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You cast a binding spell on " + target.getName() + ", holding her in place.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().getName()
                        + " gestures at "+target.nameDirectObject()+" and casts a spell. A ribbon of light wraps around "+target.possessiveAdjective()+" wrists and holds them in place.";
    }

}
