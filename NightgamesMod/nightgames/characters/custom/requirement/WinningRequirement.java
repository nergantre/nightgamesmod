package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class WinningRequirement implements CustomRequirement {

	private static final int INITIAL_WINNING_SCORE = 10;

	@Override
	public boolean meets(Combat c, Character self, Character other) {
		if (c == null)
			return false;
		int score = INITIAL_WINNING_SCORE;
		score -= other.getWillpower().get();
		score += (other.getArousal().percent() - 50) / 2;
		score += self.getFitness(c) - other.getFitness(c);
		if (c.getStance().dom(self))
			score += 10;
		if (c.getStance().mobile(self))
			score += 5;
		if (c.getStance().mobile(other))
			score -= 5;
		return score >= 0;
	}

}
