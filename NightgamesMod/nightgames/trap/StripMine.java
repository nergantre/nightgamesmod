package nightgames.trap;

import java.util.stream.IntStream;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.global.Global;
import nightgames.items.Item;

public class StripMine extends Trap {
    
    public StripMine() {
        this(null);
    }
    
    public StripMine(Character owner) {
        super("Strip Mine", owner);
    }

    @Override
    public void trigger(Character target) {
        if (target.human()) {
            if (target.mostlyNude()) {
                Global.gui().message(
                                "You're momentarily blinded by a bright flash of light. A camera flash maybe? Is someone taking naked pictures of you?");
            } else {
                Global.gui().message(
                                "You're suddenly dazzled by a bright flash of light. As you recover from your disorientation, you notice that it feel a bit drafty. "
                                                + "You find you're missing some clothes. You reflect that your clothing expenses have gone up significantly since you joined the Games.");
            }
        } else if (target.location().humanPresent()) {
            Global.gui().message("You're startled by a flash of light not far away. Standing there is a half-naked "
                            + target.name() + ", looking surprised.");
        }
        IntStream.range(0, 2 + Global.random(4)).forEach(i -> target.shredRandom());
        target.location().opportunity(target, this);
    }

    @Override
    public boolean recipe(Character owner) {
        return owner.has(Item.Tripwire) && owner.has(Item.Battery, 3);
    }

    @Override
    public boolean requirements(Character owner) {
        return owner.get(Attribute.Science) >= 4;
    }

    @Override
    public String setup(Character owner) {
        this.owner = owner;
        owner.consume(Item.Tripwire, 1);
        owner.consume(Item.Battery, 3);
        return "Using the techniques Jett showed you, you rig up a one-time-use clothing destruction device.";
    }

    @Override
    public void capitalize(Character attacker, Character victim, IEncounter enc) {
        enc.engage(new Combat(attacker, victim, attacker.location()));
        attacker.location().remove(this);
    }

}
