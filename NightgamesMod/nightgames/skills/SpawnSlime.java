package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.pet.Slime;

public class SpawnSlime extends Skill {

    public SpawnSlime(Character self) {
        super("Create Slime", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Science) >= 3;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && c.getPetsFor(getSelf()).size() < getSelf().getPetLimit() 
                        && getSelf().has(Item.Battery, 5);
    }

    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    @Override
    public String describe(Combat c) {
        return "Creates a mindless, but living slime to attack your opponent: 5 mojo, 5 Battery";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().consume(Item.Battery, 5);
        int power = 10 + getSelf().get(Attribute.Science) / 2;
        int ac = 3 + getSelf().get(Attribute.Science) / 10;
        if (getSelf().has(Trait.leadership)) {
            power += 5;
        }
        if (getSelf().has(Trait.tactician)) {
            ac += 3;
        }
        writeOutput(c, Result.normal, target);
        c.addPet(new Slime(getSelf(), power, ac).getSelf());
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new SpawnSlime(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.summoning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You dispense blue slime on the floor and send a charge through it to animate it. The slime itself is not technically alive, but an extension of a larger "
                        + "creature kept in Jett's lab.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return String.format("%s points a device at the floor and releases a blob of blue slime. The blob "
                        + "starts to move like a living thing and briefly takes on a vaguely humanoid shape "
                        + "and smiles at %s.", getSelf().subject(), target.nameDirectObject());
    }

}
