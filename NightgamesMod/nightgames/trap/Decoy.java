package nightgames.trap;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.global.Global;
import nightgames.items.Item;

public class Decoy extends Trap {

    public Decoy() {
        this(null);
    }
    
    public Decoy(Character owner) {
        super("Decoy", owner);
    }
    
    @Override
    public void trigger(Character target) {
        if (target.human()) {
            Global.gui().message(
                            "You follow the noise you've been hearing for a while, which turns out to be coming from a disposable cell phone. Seems like someone "
                                            + "is playing a trick and you fell for it. You shut off the phone and toss it aside.");
        } else if (target.location().humanPresent()) {
            Global.gui().message(target.getName() + " finds the decoy phone and deactivates it.");
        }
        target.location().remove(this);
    }

    @Override
    public boolean decoy() {
        return true;
    }

    @Override
    public boolean recipe(Character owner) {
        return owner.has(Item.Phone);
    }

    @Override
    public boolean requirements(Character owner) {
        return owner.get(Attribute.Cunning) >= 6 && !owner.has(Trait.direct);
    }

    @Override
    public String setup(Character owner) {
        this.owner = owner;
        owner.consume(Item.Phone, 1);
        if (owner.human()) {
            return "Your program a phone to play a prerecorded audio track five minutes from now. It should be noticable from a reasonable distance until someone switches it "
                            + "off.";
        } else {
            return null;
        }
    }

}
