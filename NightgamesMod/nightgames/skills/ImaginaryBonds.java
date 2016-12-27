package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.status.Bound;

public class ImaginaryBonds extends Skill {

    public ImaginaryBonds(Character self) {
        super("Binding", self, 4);
        addTag(SkillTag.positioning);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Hypnosis) >= 9;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return target.isHypnotized();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    @Override
    public String describe(Combat c) {
        return "Trap your opponent with mental bondage.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        writeOutput(c, Result.normal, target);
        target.add(c, new Bound(target, Math.min(30 + 3 * getSelf().get(Attribute.Hypnosis), 80), "imaginary bindings"));
        target.emote(Emotion.nervous, 5);
        getSelf().emote(Emotion.confident, 20);
        getSelf().emote(Emotion.dominant, 10);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ImaginaryBonds(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return Global.format("You lean close to {other:name-do} and tell her that {other:pronoun} cannot move {other:possessive} body. "
                        + "{other:NAME-POSSESSIVE} eyes widen as your hypnotic suggestion rings true in {other:possessive} mind.", getSelf(), target);
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format("{self:SUBJECT} leans close to you and helpfully informs {other:name-do} that {other:pronoun} cannot move your body. "
                        + "Of course! why didn't {other:pronoun} notice this earlier? ", getSelf(), target);
    }

}
