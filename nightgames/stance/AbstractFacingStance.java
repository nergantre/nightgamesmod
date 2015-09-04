package nightgames.stance;


import java.util.ArrayList;
import java.util.List;

import nightgames.characters.Character;
import nightgames.global.Global;

public abstract class AbstractFacingStance extends Position {
	public AbstractFacingStance(Character top, Character bottom, Stance stance) {
		super(top, bottom, stance);
	}

	@Override
	public Position insertRandomDom(Character dom) {
		List<Position> possibleResults = new ArrayList<>();
		Character sub = getOther(dom);
		if (dom.hasDick() && sub.hasPussy()) {
			Position newPos = insert(dom, dom);
			if (newPos != this)
				possibleResults.add(newPos);
		}
		if (dom.hasPussy() && sub.hasDick()){
			Position newPos = insert(sub, dom);
			if (newPos != this)
				possibleResults.add(newPos);
		}
		if (possibleResults.isEmpty()) {
			return this;
		} else {
			return possibleResults.get(Global.random(possibleResults.size()));
		}
	}

	@Override
	public Position insert(Character pitcher, Character dom) {
		Character catcher = getOther(pitcher);
		Character sub = getOther(pitcher);
		if (pitcher.body.getRandomInsertable() == null || !catcher.hasPussy()) {
			// invalid
			return this;
		}
		if (pitcher == dom && pitcher == top) {
			// guy is holding girl down, and is the dominant one in the new stance
			return new Missionary(pitcher, catcher);
		}
		if (pitcher == sub && pitcher == top) {
			// guy is holding girl down, and is the submissive one in the new stance
			return new CoiledSex(catcher, pitcher);
		}
		if (pitcher == dom && pitcher == bottom) {
			// girl is holding guy down, and is the submissive one in the new stance
			return new Missionary(pitcher, catcher);			
		}
		if (pitcher == sub && pitcher == bottom) {
			// girl is holding guy down, and is the dominant one in the new stance
			return new Cowgirl(catcher, pitcher);
		}
		return this;
	}
}
