package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.pet.FGoblin;
import nightgames.pet.Ptype;

public class SpawnFGoblin extends Skill {

    private final Ptype gender;
    
    public SpawnFGoblin(Character user, Ptype gender) {
        super("Spawn Fetish Goblin", user);
        this.gender = gender;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().getPure(Attribute.Fetish) >= 9;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance()
                                      .mobile(getSelf())
                        && !c.getStance().prone(getSelf()) && getSelf().getArousal().get() >= 25
                             && c.getPetsFor(getSelf()).size() < getSelf().getPetLimit();
    }

    @Override
    public String describe(Combat c) {
        return "Summons a hermaphroditic goblin embodying multiple fetishes: Arousal at least 25";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int power = 3 + getSelf().get(Attribute.Fetish) / 10;
        int ac = 2 + getSelf().get(Attribute.Fetish) / 10;
        if (getSelf().has(Trait.leadership))
            power += 2;
        if (getSelf().has(Trait.tactician))
            ac += 2;
        writeOutput(c, Result.normal, target);
        c.addPet(new FGoblin(getSelf(), power, ac).getSelf());
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new SpawnFGoblin(user, gender);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.summoning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "You channel all the fetishes in your twisted libido into a single form. The creature is about 4 feet tall and has a shapely female body covered "
                                        + "with bandage gear. Her face is completely obscured by a latex mask, but her big tits and her crotch are completely exposed. She has a large cock, "
                                        + "which looks ready to burst if it wasn't tightly bound at the base. Past her heavy sack, you can see sex toys sticking out of both her pussy and ass.");
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format(
                        "%s shivers and moans as %s sinks into %s darkest fantasies. Something dangerous is coming. Sure enough a short feminine figure in bondage gear appears "
                                        + "before %s. Her face is completely obscured by a latex mask, but her big tits and her crotch are completely exposed. She has a large cock, "
                                        + "which looks ready to burst if it wasn't tightly bound at the base. Past her heavy sack, %s can see sex toys sticking out of both her pussy and ass.",
                        getSelf().name(), getSelf().pronoun(), getSelf().possessivePronoun(),
                        target.nameDirectObject(), target.subject());
    }

}
