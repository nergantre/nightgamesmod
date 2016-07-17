package nightgames.requirements;

import com.google.gson.JsonObject;
import nightgames.characters.Attribute;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.combat.Result;
import nightgames.items.Item;
import nightgames.items.ItemAmount;
import nightgames.json.JsonUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.List;

import static nightgames.requirements.RequirementShortcuts.*;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.*;

/**
 * Tests for loading Requirements from Json.
 */
public class JsonRequirementLoaderTest {
    private File reqsFile = new File("NightGamesTests/nightgames/requirements/test_requirements.json");
    private JsonObject reqsJson;

    @Before public void setUp() throws Exception {
        reqsJson = JsonUtils.rootJson(reqsFile.toPath()).getAsJsonObject();
    }

    @Rule public ExpectedException thrown = ExpectedException.none();

    @Test public void loadGoodRequirements() throws Exception {
        List<Requirement> requirements = new JsonRequirementLoader().loadRequirements(reqsJson.getAsJsonObject("good"));
        assertThat(requirements, hasItem(anal()));
        assertThat(requirements, hasItem(and(bodypart("hands"), not(or(rev(prone()), orgasms(12))),
                        attribute(Attribute.Power, 3))));
        assertThat(requirements, hasItem(attribute(Attribute.Seduction, 50)));
        assertThat(requirements, hasItem(bodypart("tail")));
        assertThat(requirements, hasItem(dom()));
        assertThat(requirements, hasItem(duration(15)));
        assertThat(requirements, hasItem(inserted()));
        assertThat(requirements, hasItem(item(new ItemAmount(Item.Dildo2, 1))));
        assertThat(requirements, hasItem(level(20)));
        assertThat(requirements, hasItem(mood(Emotion.nervous)));
        assertThat(requirements, hasItem(none()));
        assertThat(requirements, hasItem(or(mood(Emotion.horny), orgasms(0))));
        assertThat(requirements, hasItem(prone()));
        assertThat(requirements, hasItem(random(0.75f)));
        assertThat(requirements, hasItem(result(Result.upgrade)));
        assertThat(requirements, hasItem(position("FlyingCarry")));
        assertThat(requirements, hasItem(status("wary")));
        assertThat(requirements, hasItem(sub()));
        assertThat(requirements, hasItem(trait(Trait.slime)));
        assertThat(requirements, hasItem(winning()));
    }

    @Test public void loadBadRequirements() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        new JsonRequirementLoader().loadRequirements(reqsJson.getAsJsonObject("bad"));
    }
}
