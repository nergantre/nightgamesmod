package nightgames.cmatch.prepare;

import java.util.List;
import java.util.function.Function;

import nightgames.characters.Character;
import nightgames.cmatch.CustomMatch;
import nightgames.status.Status;

/**
 * Change to start a character off with a status.
 */
public class GiveStatusChange implements PrematchChange {

	private Function<Character, Status> getStat;

	/**
	 * Just pass a status to be added.
	 */
	public GiveStatusChange(Status status) {
		getStat = c -> status;
	}

	/**
	 * statusFunc should be a function that calls the constructor of a status,
	 * passing to it all arguments except the afflicted character. Example:<br>
	 * <code>
	 * GiveStatusChange(c -> new Abuff(c, Attribute.Power, 5, 9999));<br>
	 * GiveStatusChange(StoneStance::new); //StoneStance only takes a character.</code>
	 */
	public GiveStatusChange(Function<Character, Status> statusFunc) {
		getStat = statusFunc;
	}

	@Override
	public void apply(CustomMatch match, Character ch, List<PrematchChange> changeList) {
		ch.add(getStat.apply(ch));
		changeList.add(this);
	}

	@Override
	public void revert(CustomMatch match, Character ch) {
		// statuses are always removed at the end of the match, no need to do so
		// explicitly.
	}

	@Override
	public PrematchChange copy() {
		return new GiveStatusChange(getStat);
	}

}
