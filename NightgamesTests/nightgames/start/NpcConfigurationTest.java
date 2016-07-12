package nightgames.start;

import nightgames.characters.Attribute;
import nightgames.characters.CharacterSex;
import nightgames.json.JsonUtils;
import nightgames.items.clothing.Clothing;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import nightgames.characters.TestAngel;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 *
 */
public class NpcConfigurationTest {
    StartConfiguration startConfig;
    NpcConfiguration angelConfig;

    @BeforeClass
    public static void setUpNpcConfigurationTest() {
        Clothing.buildClothingTable();
    }

    @Before public void setUp() throws Exception {
        Path file = new File("NightgamesTests/nightgames/start/TestStartConfig.json").toPath();
        startConfig = StartConfiguration.parse(JsonUtils.rootJson(file).getAsJsonObject());
        angelConfig = startConfig.findNpcConfig("TestAngel")
                        .orElseThrow(() -> new NoSuchElementException("TestAngel not found in test config."));
    }

    @Test public void testConfigMerge() throws Exception {
        NpcConfiguration mergedConfig = new NpcConfiguration(angelConfig, startConfig.npcCommon);
        assertEquals("TestAngel", mergedConfig.type);
        assertEquals(Optional.empty(), mergedConfig.gender);
        Map<Attribute, Integer> expectedAttributes = new HashMap<>();
        expectedAttributes.put(Attribute.Power, 13);
        expectedAttributes.put(Attribute.Seduction, 20);
        expectedAttributes.put(Attribute.Cunning, 15);
        expectedAttributes.put(Attribute.Divinity, 10);
        expectedAttributes.put(Attribute.Arcane, 2);
        assertEquals(expectedAttributes, mergedConfig.attributes);
        assertEquals(BodyConfiguration.Archetype.ANGEL, mergedConfig.body.get().type.get());
        assertEquals(50, mergedConfig.xp.get().intValue());
        assertEquals(5, mergedConfig.level.get().intValue());
        assertEquals(5000, mergedConfig.money.get().intValue());
    }

    @Test public void testNpcCreation() throws Exception {
        TestAngel angel = new TestAngel(Optional.of(angelConfig), Optional.of(startConfig.npcCommon));
        assertEquals("TestAngel", angel.character.getType());
        Map<Attribute, Integer> expectedAttributes = new HashMap<>();
        expectedAttributes.put(Attribute.Power, 13);
        expectedAttributes.put(Attribute.Seduction, 20);
        expectedAttributes.put(Attribute.Cunning, 15);
        expectedAttributes.put(Attribute.Divinity, 10);
        expectedAttributes.put(Attribute.Arcane, 2);
        expectedAttributes.put(Attribute.Perception, 6);
        expectedAttributes.put(Attribute.Speed, 5);
        assertEquals(expectedAttributes, angel.character.att);
        assertEquals(50, angel.character.xp);
        assertEquals(5, angel.character.level);
        assertEquals(5000, angel.character.money);
    }

    @Test public void testBodyMerge() throws Exception {
        TestAngel angel = new TestAngel(Optional.of(angelConfig), Optional.of(startConfig.npcCommon));

        // Starting stats should match config but breasts should be the same as base Angel if not overwritten in config.
        assertEquals(angelConfig.attributes.get(Attribute.Seduction).intValue(),
                        angel.getCharacter().get(Attribute.Seduction));
        assertEquals(TestAngel.baseTestAngelChar.body.getLargestBreasts(), angel.getCharacter().body.getLargestBreasts());
    }
    
    @Test public void testGenderChange() throws Exception {
        angelConfig.gender = Optional.of(CharacterSex.male);
        TestAngel angel = new TestAngel(Optional.of(angelConfig), Optional.of(startConfig.npcCommon));

        assertFalse(angel.character.body.has("pussy"));
        assertTrue(angel.character.body.has("cock"));
        // Changing gender should not change (e.g.) breast size.
        assertEquals(TestAngel.baseTestAngelChar.body.getLargestBreasts(), angel.character.body.getLargestBreasts());
    }

}
