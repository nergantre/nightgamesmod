package nightgames.modifier;

import com.google.gson.JsonObject;
import nightgames.actions.Locate;
import nightgames.characters.Player;
import nightgames.global.Global;
import nightgames.json.JsonUtils;
import nightgames.items.clothing.Clothing;
import nightgames.modifier.status.StatusModifier;
import nightgames.skills.Blowjob;
import nightgames.skills.Kick;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Tests for loading custom modifier setups.
 */
public class CustomModifierLoaderTest {
    private JsonObject modJSON;
    private static Player player;

    @BeforeClass public static void setUpClass() throws Exception {
        Clothing.buildClothingTable();
        Global.buildParser();
        Global.buildModifierPool();
        Global.buildActionPool();
        player = new Player("player");
        Global.buildSkillPool(player);
    }

    @Before public void setUp() throws Exception {
        Path file = new File("NightgamesTests/nightgames/modifier/test_modifier.json").toPath();
        modJSON = JsonUtils.rootJson(file).getAsJsonObject();
    }

    @Test public void testReadModifier() throws Exception {
        BaseModifier mod = (BaseModifier) CustomModifierLoader.readModifier(modJSON);
        // TODO: Modifiers constructed via allOf() and combine() only have the top-most attributes directly accessible. Figure out a way to merge attributes.
        assertThat(mod.clothing.allowedLayers(), IsCollectionContaining.hasItem(0));
        assertThat(mod.clothing.forcedItems(), IsCollectionContaining.hasItem("jacket"));
        assertThat(mod.clothing.toString(), equalTo("underwear-only, Forced:[jacket]"));
        assertThat(mod.status, equalTo(StatusModifier.combiner.nullModifier()));
        assertThat(mod.skills.bannedTactics(), IsCollectionContaining.hasItem(Tactics.fucking));
        assertThat(mod.skills.encouragedSkills(),
                        allOf((Matcher<? super Map<? extends Skill, ? extends Double>>) IsMapContaining
                                                        .hasEntry((Skill) new Blowjob(player), 1.0),
                                        (Matcher<? super Map<? extends Skill, ? extends Double>>) IsMapContaining
                                                        .hasEntry((Skill) new Kick(player), -2.0)));
        assertThat(mod.skills.toString(), equalTo("Banned:[fucking], Encouraged:{Blow=1.0, Kick=-2.0}"));
        assertThat(mod.actions.bannedActions(), IsCollectionContaining.hasItem(new Locate()));
        assertThat(mod.custom, equalTo(BaseModifier.EMPTY_CONSUMER));
    }

}
