package nightgames.characters.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nightgames.characters.Attribute;
import nightgames.characters.Growth;
import nightgames.characters.MaxAttribute;
import nightgames.characters.PreferredAttribute;
import nightgames.characters.custom.effect.CustomEffect;
import nightgames.characters.custom.effect.MoneyModEffect;
import nightgames.json.JsonUtils;
import nightgames.items.Item;
import nightgames.items.ItemAmount;
import nightgames.items.clothing.Clothing;
import nightgames.requirements.*;
import nightgames.skills.Skill;
import nightgames.skills.ThrowDraft;
import nightgames.stance.Stance;
import nightgames.status.Stsflag;
import org.hamcrest.beans.SamePropertyValuesAs;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nightgames.characters.custom.JsonSourceNPCDataLoader.load;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests for loading NPC data from JSON files.
 */
public class JsonSourceNPCDataLoaderTest {
    JsonObject npcJSON;

    @BeforeClass public static void setUpJSONSourceNPCDataLoaderTest() throws Exception {
        Clothing.buildClothingTable();
    }

    @Before public void setUp() throws Exception {
        Path file = new File("NightgamesTests/nightgames/characters/custom/test_custom_npc.json").toPath();
        npcJSON = JsonUtils.rootJson(file).getAsJsonObject();
    }

    @Test public void testLoadBasicNPCData() throws Exception {
        NPCData data = load(npcJSON);
        assertEquals("NPCTestSamantha", data.getType());
        assertEquals("TestSamantha", data.getName());
        assertEquals("SamanthaTrophy", data.getTrophy().getID());
    }

    @Test public void testLoadResources() throws Exception {
        Stats stats = new Stats();
        JsonObject statsJSON = npcJSON.getAsJsonObject("stats");
        JsonObject baseJSON = statsJSON.getAsJsonObject("base");
        JsonObject resourcesJSON = baseJSON.getAsJsonObject("resources");
        JsonSourceNPCDataLoader.loadResources(resourcesJSON, stats);

        Stats expectedStats = new Stats();
        expectedStats.stamina = 60;
        expectedStats.arousal = 200;
        expectedStats.mojo = 100;
        expectedStats.willpower = 60;

        assertThat(stats, SamePropertyValuesAs.samePropertyValuesAs(expectedStats));
    }

    @Test public void testLoadRecruitment() throws Exception {
        JsonObject recruitJSON = npcJSON.getAsJsonObject("recruitment");
        RecruitmentData recruitment = new RecruitmentData();
        JsonSourceNPCDataLoader.loadRecruitment(recruitJSON, recruitment);

        assertThat(recruitment.action, equalTo("Samantha:$1500"));
        assertThat(recruitment.introduction, equalTo("So, I know this girl..."));
        assertThat(recruitment.confirm, equalTo("Awesome"));

        List<Requirement> expectedReqs = new ArrayList<>();
        expectedReqs.add(new LevelRequirement(10));
        List<Requirement> orList = new ArrayList<>();
        orList.add(new BodyPartRequirement("cock"));
        List<Requirement> andList = new ArrayList<>();
        andList.add(new BodyPartRequirement("pussy"));
        andList.add(new NotRequirement(new BodyPartRequirement("balls")));
        orList.add(new AndRequirement(andList));
        expectedReqs.add(new OrRequirement(orList));

        assertThat(recruitment.requirement,
                        IsCollectionContaining.hasItems(expectedReqs.toArray(new Requirement[] {})));
    }

    @Test public void testLoadEffects() throws Exception {
        JsonObject recruitJSON = npcJSON.getAsJsonObject("recruitment");
        JsonArray effectJSON = recruitJSON.getAsJsonArray("cost");
        List<CustomEffect> effects = new ArrayList<>();
        JsonSourceNPCDataLoader.loadEffects(effectJSON, effects);

        assertThat(effects, IsCollectionContaining.hasItems(new MoneyModEffect(-1500)));
    }

    @Test public void testReadLine() throws Exception {
        JsonObject linesJSON = npcJSON.getAsJsonObject("lines");
        JsonArray nakedLines = linesJSON.getAsJsonArray("naked");
        CustomStringEntry line = JsonSourceNPCDataLoader.readLine(nakedLines.get(0).getAsJsonObject());

        assertThat(line.line, is("IM NEKKID!"));
    }

    @Test public void testReadItem() throws Exception {
        JsonObject itemsJSON = npcJSON.getAsJsonObject("items");
        JsonArray initialJSON = itemsJSON.getAsJsonArray("initial");
        ItemAmount item = JsonSourceNPCDataLoader.readItem(initialJSON.get(0).getAsJsonObject());

        assertThat(item, equalTo(new ItemAmount(Item.Strapon, 1)));

    }

    @Test public void testLoadGrowthResources() throws Exception {
        JsonObject statsJSON = npcJSON.getAsJsonObject("stats");
        JsonObject growthJSON = statsJSON.getAsJsonObject("growth");
        JsonObject resourcesJSON = growthJSON.getAsJsonObject("resources");
        Growth growth = new Growth();
        JsonSourceNPCDataLoader.loadGrowthResources(resourcesJSON, growth);

        Growth expectedGrowth = new Growth();
        expectedGrowth.stamina = 1;
        expectedGrowth.bonusStamina = 2;
        expectedGrowth.arousal = 3;
        expectedGrowth.bonusArousal = 4;
        expectedGrowth.willpower = 7.5f;
        expectedGrowth.bonusWillpower = 8.5f;
        expectedGrowth.attributes = new int[] {9, 10, 11, 11};
        expectedGrowth.bonusAttributes = 12;

        assertThat(growth, SamePropertyValuesAs.samePropertyValuesAs(expectedGrowth));
    }

    @Test public void testLoadPreferredAttributes() throws Exception {
        JsonObject statsJSON = npcJSON.getAsJsonObject("stats");
        JsonObject growthJSON = statsJSON.getAsJsonObject("growth");
        JsonArray prefAttJSON = growthJSON.getAsJsonArray("preferredAttributes");
        List<PreferredAttribute> preferred = new ArrayList<>();
        JsonSourceNPCDataLoader.loadPreferredAttributes(prefAttJSON, preferred);

        List<PreferredAttribute> expectedPreferred = new ArrayList<>();
        expectedPreferred.add(new MaxAttribute(Attribute.Technique, 30));
        expectedPreferred.add(new MaxAttribute(Attribute.Seduction));

        assertThat(preferred, IsCollectionContaining.hasItems(expectedPreferred.toArray(new PreferredAttribute[] {})));
    }

    @Test public void testLoadAiModifiers() throws Exception {
        JsonArray modsJSON = npcJSON.getAsJsonArray("ai-modifiers");
        AiModifiers aiMods = new AiModifiers();
        JsonSourceNPCDataLoader.loadAiModifiers(modsJSON, aiMods);

        AiModifiers expectedMods = new AiModifiers();
        Map<Class<? extends Skill>, Double> expectedAttackMods = expectedMods.getAttackMods();
        expectedAttackMods.put(ThrowDraft.class, -10.0);
        Map<Stance, Double> expectedPositionMods = expectedMods.getPositionMods();
        expectedPositionMods.put(Stance.cowgirl, 5.0);
        Map<Stsflag, Double> expectedSelfStatusMods = expectedMods.getSelfStatusMods();
        expectedSelfStatusMods.put(Stsflag.firedup, 1.5);
        Map<Stsflag, Double> expectedOppStatusMods = expectedMods.getOppStatusMods();
        expectedOppStatusMods.put(Stsflag.charmed, 1.0);
        expectedOppStatusMods.put(Stsflag.bodyfetish, 1.0);

        assertThat(aiMods.getAttackMods().entrySet(), equalTo(expectedMods.getAttackMods().entrySet()));
        assertThat(aiMods.getPositionMods().entrySet(), equalTo(expectedMods.getPositionMods().entrySet()));
        assertThat(aiMods.getSelfStatusMods().entrySet(), equalTo(expectedMods.getSelfStatusMods().entrySet()));
        assertThat(aiMods.getOppStatusMods().entrySet(), equalTo(expectedMods.getOppStatusMods().entrySet()));

    }
}
