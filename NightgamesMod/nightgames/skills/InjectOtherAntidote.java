package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;

public class InjectOtherAntidote extends Skill {
    public InjectOtherAntidote(Character self) {
        super("Inject Antidote (Other)", self);
    }

    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Medicine) >= 7;
    }

    public boolean usable(Combat c, Character target) {
        return (c.getStance().mobile(this.getSelf())) && (this.getSelf().canAct())
                        && getSelf().has(Item.MedicalSupplies, 1)
                        && (!c.getStance().mobile(this.getSelf()) || !target.canAct());
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    public boolean resolve(Combat c, Character target) {
        if (this.getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else {
            c.write(getSelf(), receive(c, 0, Result.normal, this.getSelf()));
        }
        target.calm(c, target.getArousal().max() / 10);
        target.purge(c);
        getSelf().consume(Item.MedicalSupplies, 1);

        return true;
    }

    public Skill copy(Character user) {
        return new InjectOtherAntidote(user);
    }

    public Tactics type(Combat c) {
        return Tactics.debuff;
    }

    public String deal(Combat c, int damage, Result modifier, Character target) {
        return Global.format(
                        "Moving quickly you inject {other:name-do} with an antidote, removing any buffs or debuffs {other:possessive} had.",
                        getSelf(), target);
    }

    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format(
                        "{self:SUBJECT} quickly manages to stick {other:name-do} with a hypodermic needle. As the contents flood into {other:possessive} body, {other:pronoun-action:feel|feels} any temporary buffs or debuffs leave {other:direct-object}.",
                        getSelf(), target);
    }

    public String describe(Combat c) {
        return "Injects yourself with an pancea";
    }
}
