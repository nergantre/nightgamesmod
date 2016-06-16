package nightgames.characters.custom;

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

public interface NPCData {
    String getName();

    Stats getStats();

    Stack<Clothing> getTopOutfit();

    Stack<Clothing> getBottomOutfit();

    Growth getGrowth();

    List<PreferredAttribute> getPreferredAttributes();

    List<ItemAmount> getStartingItems();

    List<ItemAmount> getPurchasedItems();

    String getLine(String string, Combat c, Character self, Character other);

    Item getTrophy();

    boolean checkMood(Character self, Emotion mood, int value);

    Body getBody();

    CharacterSex getSex();

    String getPortraitName(Combat c, Character self, Character other);

    String getDefaultPortraitName();

    Plan getPlan();

    String getType();

    RecruitmentData getRecruitment();

    AiModifiers getAiModifiers();

    Map<CommentSituation, String> getComments();

    boolean isStartCharacter();
}
