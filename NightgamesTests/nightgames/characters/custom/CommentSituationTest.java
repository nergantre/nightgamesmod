package nightgames.characters.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nightgames.json.JsonUtils;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThat;

/**
 * Tests for the CommentSituation class.
 */
public class CommentSituationTest {
    private JsonArray commentsJSON;

    @Rule public ExpectedException thrown = ExpectedException.none();

    @Before public void setUp() throws Exception {
        Path file = new File("NightGamesTests/nightgames/characters/custom/test_comments.json").toPath();
        commentsJSON = JsonUtils.rootJson(file).getAsJsonObject().getAsJsonArray("allcomments");
    }

    @Test public void testParseValidComments() throws Exception {
        JsonObject successJSON = commentsJSON.get(0).getAsJsonObject();
        assertThat(successJSON.get("character").getAsString(), IsEqual.equalTo("Success"));
        JsonArray successCommentsJSON = successJSON.getAsJsonArray("comments");
        Map<CommentSituation, String> successComments = new HashMap<>();
        for (JsonElement commentJSON : successCommentsJSON) {
            CommentSituation.parseComment(commentJSON.getAsJsonObject(), successComments);
        }

        Map<CommentSituation, String> expectedComments = new HashMap<>();
        expectedComments.put(CommentSituation.SELF_HORNY, "I need you! Now! Get over here!");
        expectedComments.put(CommentSituation.OTHER_HORNY,
                        "You're a little hot for me aren't you? I can help with that.");

        assertThat(successComments.entrySet(), IsEqual.equalTo(expectedComments.entrySet()));
    }

    @Test public void testParseBadSituation() throws Exception {
        JsonObject failureJSON = commentsJSON.get(1).getAsJsonObject();
        assertThat(failureJSON.get("character").getAsString(), IsEqual.equalTo("Failure"));
        JsonArray failureCommentsJSON = failureJSON.getAsJsonArray("comments");
        Map<CommentSituation, String> failureComments = new HashMap<>();

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No enum constant");

        for (Object commentJSON : failureCommentsJSON) {
            CommentSituation.parseComment((JsonObject) commentJSON, failureComments);
        }
    }
}
