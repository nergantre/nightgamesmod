package nightgames.characters.custom;

import nightgames.characters.custom.requirement.*;
import nightgames.global.JSONUtils;
import nightgames.items.clothing.Clothing;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static nightgames.characters.custom.JSONSourceNPCDataLoader.load;
import static org.junit.Assert.*;

/**
 * Created by Ryplinn on 6/23/2016.
 */
public class JSONSourceNPCDataLoaderTest {
    JSONObject npcJSON;

    @BeforeClass public static void setUpJSONSourceNPCDataLoaderTest() throws Exception {
        Clothing.buildClothingTable();
    }

    @Before public void setUp() throws Exception {
        Path file = new File("NightgamesTests/nightgames/characters/custom/test_custom_npc.json").toPath();
        npcJSON = JSONUtils.rootFromFile(file);

    }

    // Mostly just make sure loading doesn't fail
    @Test public void testLoadNPCData() throws Exception {
        NPCData data = load(npcJSON);
        assertEquals("NPCTestSamantha", data.getType());
        assertEquals("TestSamantha", data.getName());
    }

    @Test public void testLoadRequirement() throws Exception {
        List<CustomRequirement> reqs = new ArrayList<>();
        JSONObject recruitmentJSON = (JSONObject) npcJSON.get("recruitment");
        JSONObject reqsJSON = (JSONObject) recruitmentJSON.get("requirements");
        JSONSourceNPCDataLoader.loadRequirement(reqsJSON, reqs);

        List<CustomRequirement> expectedReqs = new ArrayList<>();
        expectedReqs.add(new LevelRequirement(10));
        List<CustomRequirement> orList = new ArrayList<>();
        orList.add(new BodyPartRequirement("cock"));
        List<CustomRequirement> andList = new ArrayList<>();
        andList.add(new BodyPartRequirement("pussy"));
        List<CustomRequirement> notList = new ArrayList<>();
        notList.add(new BodyPartRequirement("balls"));
        andList.add(new NotRequirement(notList));
        orList.add(new AndRequirement(andList));
        expectedReqs.add(new OrRequirement(orList));

        assertEquals(new HashSet<>(expectedReqs), new HashSet<>(reqs));
    }
}
