package nightgames.creator.req;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum RequirementType {
	ANAL(false, "The combatants are fucking anally."),
	AND(false, "The following are all true:", RequirementArgument.REQUIREMENTS),
	ATTRIBUTE("has at least this value for this attribute:", RequirementArgument.ATTRIBUTE, RequirementArgument.NUMBER),
	BODY("has this body part:", RequirementArgument.BODY),
	DOM("is dominant in this position."),
	INSERTED("has a cock or strap-on inside their opponent."),
	ITEM("has this item:", RequirementArgument.ITEM),
	LEVEL("is at least this level:", RequirementArgument.NUMBER),
	MOOD("has this mood:", RequirementArgument.MOOD),
	NOT(false, "The following is NOT true:", RequirementArgument.REQUIREMENT),
	ORGASMS("has had at least this many orgasms:", RequirementArgument.NUMBER),
	OR(false, "At least one of the following is true:", RequirementArgument.REQUIREMENTS),
	POSITION(false, "The combatants are in this position:", RequirementArgument.POSITION),
	PRONE("is prone on the ground."),
	RANDOM(false, "A random chance with these odds (in %) succeeds:", RequirementArgument.NUMBER),
	RESULT(false, "The fight is over, and had this result:", RequirementArgument.RESULT),
	STATUS("has this status condition:", RequirementArgument.STATUS),
	SUB("is submissive in this position"),
	TRAIT("has this trait:", RequirementArgument.TRAIT),
	WINNING("is currently winning."),;

	private boolean characterSpecific;
	private String explanation;
	private List<RequirementArgument> args;

	private RequirementType(boolean characterSpecific, String explanation, RequirementArgument... args) {
		this.characterSpecific = characterSpecific;
		this.explanation = explanation;
		this.args = Collections.unmodifiableList(Arrays.asList(args));
	}

	private RequirementType(String explanation, RequirementArgument... args) {
		this(true, explanation, args);
	}

	public boolean isCharacterSpecific() {
		return characterSpecific;
	}

	public String getExplanation() {
		return explanation;
	}

	public List<RequirementArgument> getArguments() {
		return args;
	}

	public boolean canHaveChildren(int count) {
		return !(args.contains(RequirementArgument.REQUIREMENTS) && count <= 0)
				&& !(args.contains(RequirementArgument.REQUIREMENT) && count != 1);
	}

	public String toString() {
		return explanation;
	}
	
	public static RequirementType parse(String str) {
		return valueOf(str.toUpperCase());
	}
}
