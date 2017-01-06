package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.nskills.tags.SkillTag;

public class Defabricator extends Skill {

    public Defabricator(Character self) {
        super("Defabricator", self);
        addTag(SkillTag.stripping);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Science) >= 18;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && !target.mostlyNude() && getSelf().has(Item.Battery, 8);
    }

    @Override
    public String describe(Combat c) {
        return "Does what it says on the tin.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().consume(Item.Battery, 8);
        writeOutput(c, Result.normal, target);
        if (getSelf().human() || c.isBeingObserved())
            c.write(target, target.nakedLiner(c, target));
        target.nudify();
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Defabricator(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You charge up your Defabricator and point it in " + target.getName()
                        + "'s general direction. A bright light engulfs her and her clothes are disintegrated in moment.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s points a device at %s and light shines from it like it's a simple flashlight. "
                        + "The device's function is immediately revealed as %s clothes just vanish "
                        + "in the light. %s left naked in seconds.", getSelf().subject(),
                        target.nameDirectObject(), target.possessiveAdjective(), 
                        Global.capitalizeFirstLetter(target.subjectAction("are", "is")));
    }

}
