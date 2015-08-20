package nightgames.characters.custom;

import java.util.List;
import java.util.Stack;

import nightgames.characters.BasePersonality.PreferredAttribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Growth;
import nightgames.characters.Personality;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.items.Clothing;
import nightgames.items.Item;
import nightgames.items.ItemAmount;

public interface NPCData {
	String getName();
	Stats getStats();
	Stack<Clothing> getTopOutfit();
	Stack<Clothing> getBottomOutfit();
	Growth getGrowth();
	List<PreferredAttribute> getPreferredAttributes();
	List<ItemAmount> getStartingItems();
	List<ItemAmount> getPurchasedItems();
	String getLine(String string, Combat c, Character other);
	Item getTrophy();
	boolean checkMood(Personality self, Emotion mood, int value);
	List<BodyPart> getBodyParts();
	String getGender();
	String getPortraitName(Personality self);
	String getDefaultPortraitName();
}
