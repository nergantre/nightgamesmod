package nightgames.cmatch.prepare;

import java.util.List;

import nightgames.characters.Character;
import nightgames.characters.body.Body;
import nightgames.cmatch.CustomMatch;

/**
 * Wrapper to facilitate body changes.
 */
public abstract class AbstractBodyChange implements PrematchChange {

	private Body original;

	/**
	 * Optionally perform the required changes. Return value indicates whether or not
	 * anything changed.
	 */
	protected abstract boolean doChange(CustomMatch match, Character ch);
	
	@Override
	public final void apply(CustomMatch match, Character ch, List<PrematchChange> changeList) {
		try {
			original = ch.body.clone();
			if (doChange(match, ch))
				changeList.add(this);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void revert(CustomMatch match, Character ch) {
		ch.body = original;
	}

}
