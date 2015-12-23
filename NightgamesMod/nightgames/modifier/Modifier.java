package nightgames.modifier;

import nightgames.characters.Character;
import nightgames.global.Match;
import nightgames.modifier.skill.SkillModifier;

public interface Modifier {

	/**
	 * Ensure that the character has an appropriate outfit
	 */
	void handleOutfit(Character c);

	/**
	 * Ensure that the character has a legal inventory
	 */
	void handleItems(Character c);

	/**
	 * Apply any required statuses
	 */
	void handleStatus(Character c);

	/**
	 * Get a SkillModifier specific to the current Match
	 */
	SkillModifier getSkillModifier();
	
	/**
	 * Process non-combat turn
	 */
	void handleTurn(Character c, Match m);

	/**
	 * Undo all changes to the character's inventory made by handleItems 
	 */
	void undoItems(Character c);
	
	int bonus();
	String name();
	String intro();
	String acceptance();
}
