package nightgames.trap;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Hypersensitive;
import nightgames.status.Oiled;

public class TentacleTrap extends Trap {

    public TentacleTrap() {
        this(null);
    }
    
    public TentacleTrap(Character owner) {
        super("Tentacle Trap", owner);
    }

    @Override
    public void trigger(Character target) {
        if (target.mostlyNude()) {
            if (target.human()) {
                Global.gui().message(
                                "An unearthly glow appears from the floor surrounding you and at least a dozen tentacles burst from the floor. Before you can react, you're lifted helpless "
                                                + "into the air. The tentacles assault you front and back, wriggling around you nipples and cock, while one persistant tentacle forces its way into your ass. The overwhelming "
                                                + "sensations and violation keep you from thinking clearly and you can't even begin to mount a reasonable resistance. Just as suddenly as they attacked you, the tentacles "
                                                + "are gone, dumping you unceremoniously to the floor. You're left coated in a slimy liquid that, based on your rock-hard erection, seems to be a powerful aphrodisiac. Holy "
                                                + "fucking hell....");
            } else if (target.location().humanPresent()) {
                Global.gui().message(target.name()
                                + " gets caught in the tentacle trap and is immediately surrounded by penis-shaped tentacles. Before she can escape, they bind her "
                                + "limbs and start probing and caressing her naked body. The tentacles start to ooze out lubricant and two tentacles penetrate her vaginally and anally. A third "
                                + "tentacle slips into her mouth, while the rest frot against her body. They gang-bang her briefly, but thoroughly, before squirting liquid over her and disappearing "
                                + "back into the floor. She'll left shivering, sticky, and unsatisfied. In effect, she's already defeated.");
            }
            target.tempt(target.getArousal().max());
            target.calm(null, 1);
            target.add(new Oiled(target));
            target.add(new Hypersensitive(target));
            target.location().opportunity(target, this);
        } else {
            if (target.human()) {
                Global.gui().message(
                                "Holy hell! A dozen large tentacles shoot out of the floor in front of you and thrash wildly. You freeze, hoping they won't notice you, but "
                                                + "it seems futile. the tentacles approach you from all sides, poking at you tentatively. As suddenly as they appeared, the tentacles vanish back into the floor. "
                                                + "\n...Is that it? You're safe... you guess?");
            } else if (target.location().humanPresent()) {
                Global.gui().message(
                                target.name() + " stumbles into range of the fetish totem. A cage of phallic tentacles appear around her. They apparently aren't interested in her and "
                                                + "they disappear, leaving her slightly bewildered.");
            }
        }
    }

    @Override
    public boolean recipe(Character owner) {
        return owner.has(Item.Totem);
    }

    @Override
    public boolean requirements(Character owner) {
        return owner.getRank() > 0;
    }

    @Override
    public String setup(Character owner) {
        this.owner = owner;
        owner.consume(Item.Totem, 1);
        return "You need to activate this phallic totem before it can be used as a trap. You stroke the small totem with your hand, which is... weird, but effective. You "
                        + "quickly place the totem someplace out of sight and hurriedly get out of range. You're not sure whether this will actually discriminate before attacking.";
    }

    @Override
    public void capitalize(Character attacker, Character victim, IEncounter enc) {
        enc.engage(new Combat(attacker, victim, attacker.location()));
        attacker.location().remove(this);
    }
}
