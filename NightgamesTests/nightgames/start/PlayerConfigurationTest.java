package nightgames.start;

import nightgames.characters.Attribute;
import nightgames.characters.CharacterSex;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.global.JSONUtils;
import nightgames.items.clothing.Clothing;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests involving starting player configuration.
 *
 */
public class PlayerConfigurationTest {
    StartConfiguration startConfig;
    PlayerConfiguration playerConfig;

    @BeforeClass public static void setUpNpcConfigurationTest() {
        Clothing.buildClothingTable();
    }

    @Before public void setUp() throws Exception {
        Path file = new File("NightgamesTests/nightgames/start/TestStartConfig.json").toPath();
        startConfig = StartConfiguration.parse(JSONUtils.rootFromFile(file));
        playerConfig = startConfig.player;

    }

    @Test public void testPlayerCreation() throws Exception {
        Map<Attribute, Integer> chosenAttributes = new HashMap<>();
        chosenAttributes.put(Attribute.Power, 5);
        chosenAttributes.put(Attribute.Seduction, 6);
        chosenAttributes.put(Attribute.Cunning, 7);
        Player malePlayer = new Player("dude", CharacterSex.male, Optional.of(playerConfig), chosenAttributes);
        assertEquals(5, malePlayer.level);
        assertEquals(15000, malePlayer.money);
        List<Trait> configTraits = playerConfig.traits.get();
        Collections.sort(configTraits);
        Collections.sort(malePlayer.traits);
        assertArrayEquals(configTraits.toArray(), malePlayer.traits.toArray());

    }
}
