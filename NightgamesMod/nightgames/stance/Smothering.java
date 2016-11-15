package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class Smothering extends FaceSitting {
    public Smothering(Character top, Character bottom) {
        super(top, bottom, Stance.smothering);
    }

    @Override
    public String describe(Combat c) {
        return Global.format("{self:SUBJECT-ACTION:sit|sits} on {other:name-posssesive} face, with {self:possessive} round ass fully encompassing {other:possessive} view. {other:SUBJECT} cannot even breathe except for the short pauses when {self:subject-action:allow|allows} {other:direct-object} to by lifting {self:possessive} ass.", top, bottom);
    }
}
