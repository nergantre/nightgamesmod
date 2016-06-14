package nightgames.start;

import nightgames.characters.Attribute;
import nightgames.characters.NPC;
import nightgames.characters.body.BreastsPart;
import nightgames.global.JSONUtils;
import nightgames.items.clothing.Clothing;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import nightgames.characters.TestAngel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Ryplinn on 6/11/2016.
 */
public class NpcConfigurationTest {
    StartConfiguration startCfg;

    @BeforeClass
    public static void setUpNpcConfigurationTest() {
        Clothing.buildClothingTable();
    }

    @Before public void setUp() throws Exception {

    }

    @Test public void testBodyMerge() throws Exception {
        Path file = new File("NightgamesTests/nightgames/start/TestStartConfig.json").toPath();
        startCfg = StartConfiguration.parse(JSONUtils.rootFromFile(file));
        NpcConfiguration angelCfg = startCfg.findNpcConfig("TestAngel")
                        .orElseThrow(() -> new NoSuchElementException("TestAngel not found in test config."));

        TestAngel angel = new TestAngel(Optional.of(angelCfg), Optional.of(startCfg.npcCommon));

        // Starting stats should match config but breasts should be the same as base Angel if not overwritten in config.
        assertEquals(angelCfg.attributes.get(Attribute.Seduction).intValue(),
                        angel.getCharacter().get(Attribute.Seduction));
        assertEquals(TestAngel.baseTestAngelChar.body.getLargestBreasts(), angel.getCharacter().body.getLargestBreasts());
    }

}
