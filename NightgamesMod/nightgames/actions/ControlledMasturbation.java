package nightgames.actions;

import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.global.Global;
import nightgames.status.Stsflag;

public class ControlledMasturbation extends Action {

    public ControlledMasturbation() {
        super("Be Controlled");
    }

    @Override
    public boolean usable(Character user) {
        return user.is(Stsflag.trance);
    }

    @Override
    public Movement execute(Character user) {
        
        String mast;
        BodyPart part;
        if (user.hasDick()) {
            mast = "stroking {self:possessive} {self:body-part:cock}";
            part = user.body.getRandomCock();
        } else if (user.hasPussy()) {
            mast = "rubbing {self:possessive} {self:body-part:pussy}";
            part = user.body.getRandomPussy();
        } else {
            mast = "fingering {self:possessive} ass";
            part = user.body.getRandomAss();
        }
         
        if (user.human()) {
            Global.gui().message(Global.format("Your limbs are still not your own, and your hand"
                            + " continues %s against your will.", user, Global.noneCharacter(), mast));
        } else {
            Global.gui().message(Global.format("You see {self:name} furiously %s. This would normally be"
                            + " very exciting to watch, but the glazed look in {self:possessive} eyes give"
                            + " you pause. Just what is going on here?", user, Global.noneCharacter(), mast));
        }
        
        user.tempt(40 + Global.random(20));
        
        return Movement.wait;
    }

    @Override
    public Movement consider() {
        return Movement.wait;
    }

}
