package nightgames.cmatch.prepare;

import nightgames.cmatch.CustomMatch;

import java.util.List;

import nightgames.characters.Character;

/**
 * Describes a possible change made at the start of a custom match.
 * This change can be reverted at the end.
 */
public interface PrematchChange {

	/**
	 * Optionally perform some operation on the Character if required. If a change
	 * is performed, add it to the list.
	 */
	void apply(CustomMatch match, Character ch, List<PrematchChange> changeList);
	
	/**
	 * Assuming that the operation given by apply was performed, revert that change.
	 */
	void revert(CustomMatch match, Character ch);
	
	/**
	 * Create a new instance.
	 */
	PrematchChange copy();
}
