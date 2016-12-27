package nightgames.creator.req;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Attribute;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Result;
import nightgames.items.ItemAmount;
import nightgames.requirements.*;
import nightgames.status.Stsflag;

public enum RequirementType {
	ANAL(false, "The combatants are fucking anally."),
	AND(false, "The following are all true:", RequirementArgument.REQUIREMENTS),
	ATTRIBUTE("has at least this value for this attribute:", RequirementArgument.ATTRIBUTE, RequirementArgument.NUMBER),
	BODY("has this body part:", RequirementArgument.BODY),
	DOM("is dominant in this position."),
	INSERTED("is penetrating their opponent."),
	ITEM("has this item:", RequirementArgument.ITEM),
	LEVEL("is at least this level:", RequirementArgument.NUMBER),
	MOOD("has this mood:", RequirementArgument.MOOD),
	NOT(false, "The following is NOT true:", RequirementArgument.REQUIREMENT),
	ORGASM("has had at least this many orgasms:", RequirementArgument.NUMBER),
	OR(false, "At least one of the following is true:", RequirementArgument.REQUIREMENTS),
	POSITION(false, "The combatants are in this position:", RequirementArgument.POSITION),
	PRONE("is prone on the ground."),
	RANDOM(false, "A random chance of ... %) succeeds:", RequirementArgument.NUMBER),
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
	
	public String getTag() {
		return this == ORGASM ? "orgasms" : name().toLowerCase();
	}
	
	public static RequirementType parse(String str) {
		return valueOf(str.toUpperCase());
	}
	
	public static RequirementType typeOf(Requirement req) {
		if (req instanceof BodyPartRequirement) {
			return BODY;
		}
		return valueOf(((BaseRequirement) req).getName().toUpperCase());
	}
	
	public Requirement build(Object... args) {
		switch (this) {
		case ANAL:
			return new AnalRequirement();
		case AND:
			return new AndRequirement(asList(args));
		case ATTRIBUTE:
			return new AttributeRequirement((Attribute) args[0], (int) args[1]);
		case BODY:
			return new BodyPartRequirement((String) args[0]);
		case DOM:
			return new DomRequirement();
		case INSERTED:
			return new InsertedRequirement();
		case ITEM:
			return new ItemRequirement((ItemAmount) args[0]);
		case LEVEL:
			return new LevelRequirement((int) args[0]);
		case MOOD:
			return new MoodRequirement((Emotion) args[0]);
		case NOT:
			return new NotRequirement((Requirement) args[0]);
		case OR:
			return new OrRequirement(asList(args));
		case ORGASM:
			return new OrgasmRequirement((int) args[0]);
		case POSITION:
			return new PositionRequirement((String) args[0]);
		case PRONE:
			return new ProneRequirement();
		case RANDOM:
			return new RandomRequirement((int) args[0]);
		case RESULT:
			return new ResultRequirement((Result) args[0]);
		case STATUS:
			return new StatusRequirement((Stsflag) args[0]);
		case SUB:
			return new SubRequirement();
		case TRAIT:
			return new TraitRequirement((Trait) args[0]);
		case WINNING:
			return new WinningRequirement();
		default:
			throw new Error("Incomplete switch in RequirementType#build");
		}
	}
	
	private List<Requirement> asList(Object... args) {
		return Arrays.stream(args).map(o -> (Requirement) o).collect(Collectors.toList());
	}
}
