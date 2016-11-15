package nightgames.stance;

import java.util.ArrayList;
import java.util.List;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public abstract class AbstractFacingStance extends Position {
    public AbstractFacingStance(Character top, Character bottom, Stance stance) {
        super(top, bottom, stance);
    }

    @Override
    public Position insertRandomDom(Combat c, Character dom) {
        List<Position> possibleResults = new ArrayList<>();
        Character sub = getPartner(c, dom);
        if (dom.hasInsertable() && sub.hasPussy()) {
            Position newPos = insert(c, dom, dom);
            if (newPos != this) {
                possibleResults.add(newPos);
            }
        }
        if (dom.hasPussy() && sub.hasInsertable()) {
            Position newPos = insert(c, sub, dom);
            if (newPos != this) {
                possibleResults.add(newPos);
            }
        }
        if (possibleResults.isEmpty()) {
            return this;
        } else {
            return possibleResults.get(Global.random(possibleResults.size()));
        }
    }

    @Override
    public Position insert(Combat c, Character pitcher, Character dom) {
        Character catcher = getPartner(c, pitcher);
        Character sub = getPartner(c, dom);
        if (pitcher.body.getRandomInsertable() == null || !catcher.hasPussy()) {
            // invalid
            return this;
        }
        if (pitcher == dom && pitcher == top) {
            // guy is holding girl down, and is the dominant one in the new
            // stance
            return Missionary.similarInstance(pitcher, catcher);
        }
        if (pitcher == sub && pitcher == top) {
            // guy is holding girl down, and is the submissive one in the new
            // stance
            return new CoiledSex(catcher, pitcher);
        }
        if (pitcher == dom && pitcher == bottom) {
            // girl is holding guy down, and is the submissive one in the new
            // stance
            return Missionary.similarInstance(pitcher, catcher);
        }
        if (pitcher == sub && pitcher == bottom) {
            // girl is holding guy down, and is the dominant one in the new
            // stance
            return Cowgirl.similarInstance(catcher, pitcher);
        }
        return this;
    }
}
