// $codepro.audit.disable logExceptions
package nightgames.characters.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import nightgames.characters.BasePersonality.PreferredAttribute;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Emotion;
import nightgames.characters.Growth;
import nightgames.characters.Plan;
import nightgames.characters.body.Body;
import nightgames.combat.Combat;
import nightgames.items.Item;
import nightgames.items.ItemAmount;
import nightgames.items.clothing.Clothing;

public class DataBackedNPCData implements NPCData {
	List<PreferredAttribute>				preferredAttributes;
	List<ItemAmount>						purchasedItems;
	List<ItemAmount>						startingItems;
	List<CustomStringEntry>					portraits;
	Map<Emotion, Integer>					moodThresholds;
	Map<String, List<CustomStringEntry>>	characterLines;
	Stack<Clothing>							top;
	Stack<Clothing>							bottom;
	Stats									stats;
	Growth									growth;
	Item									trophy;
	String									name;
	String									sex;
	String									defaultPortraitName;
	Plan									plan;
	String									type;
	RecruitmentData							recruitment;
	Body									body;
	AiModifiers								aiModifiers;

	public DataBackedNPCData() {
		preferredAttributes = new ArrayList<>();
		purchasedItems = new ArrayList<>();
		startingItems = new ArrayList<>();
		portraits = new ArrayList<>();
		body = new Body();
		moodThresholds = new HashMap<>();
		characterLines = new HashMap<>();
		top = new Stack<>();
		bottom = new Stack<>();
		stats = new Stats();
		growth = new Growth();
		trophy = Item.MiscTrophy;
		name = "Anonymous";
		sex = "female";
		defaultPortraitName = "";
		recruitment = new RecruitmentData();
		aiModifiers = new AiModifiers();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Stats getStats() {
		return stats;
	}

	@Override
	public Stack<Clothing> getTopOutfit() {
		return top;
	}

	@Override
	public Stack<Clothing> getBottomOutfit() {
		return bottom;
	}

	@Override
	public Growth getGrowth() {
		return growth;
	}

	@Override
	public List<PreferredAttribute> getPreferredAttributes() {
		return preferredAttributes;
	}

	@Override
	public List<ItemAmount> getStartingItems() {
		return startingItems;
	}

	@Override
	public List<ItemAmount> getPurchasedItems() {
		return purchasedItems;
	}

	@Override
	public String getLine(String type, Combat c, Character self,
			Character other) {
		if (!characterLines.containsKey(type)) {
			return "";
		}
		List<CustomStringEntry> lines = characterLines.get(type);
		for (CustomStringEntry line : lines) {
			if (line.meetsRequirements(c, self, other)) {
				return line.getLine(c, self, other);
			}
		}
		return "";
	}

	@Override
	public Item getTrophy() {
		return trophy;
	}

	@Override
	public boolean checkMood(Character self, Emotion mood, int value) {
		if (moodThresholds.containsKey(mood)) {
			return value >= moodThresholds.get(mood);
		}
		return value >= 100;
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public CharacterSex getSex() {
		try {
			CharacterSex eSex = CharacterSex.valueOf(sex);
			return eSex;
		} catch (IllegalArgumentException | NullPointerException e) {
			return CharacterSex.asexual;
		}
	}

	@Override
	public String getPortraitName(Combat c, Character self, Character other) {
		for (CustomStringEntry line : portraits) {
			if (line.meetsRequirements(c, self, other)) {
				return line.getLine(c, self, other);
			}
		}
		return "";
	}

	@Override
	public String getDefaultPortraitName() {
		return defaultPortraitName;
	}

	@Override
	public Plan getPlan() {
		return plan;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public RecruitmentData getRecruitment() {
		return recruitment;
	}

	@Override
	public AiModifiers getAiModifiers() {
		return aiModifiers;
	}
}
