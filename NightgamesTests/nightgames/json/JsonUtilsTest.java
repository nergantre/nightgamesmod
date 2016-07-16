package nightgames.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import nightgames.characters.Attribute;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingTrait;
import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.number.IsCloseTo;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.*;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Tests for JsonUtils behavior.
 */
public class JsonUtilsTest {
    @BeforeClass public static void setUpClass() throws Exception {
        Clothing.buildClothingTable();
    }

    @Rule public ExpectedException thrown = ExpectedException.none();

    private JsonObject sampleJSON;
    private static JsonParser parser = new JsonParser();

    @Before public void setUp() throws Exception {
        String sampleText = "{ " + "\"int\": 10," + "\"float\": 5.0," + "\"string\": \"wow\"," + "\"bool\": false,"
                        + "\"manystrings\": [\"foo\", \"bar\", \"baz\"],"
                        + "\"enums\": [\"broody\", \"open\", \"kinky\"]," + "\"extraenums\": [\"female\", \"nodick\"],"
                        + "\"map\": {\"Power\": 5, \"Seduction\": 15}"
                        + " }";
        sampleJSON = parser.parse(sampleText).getAsJsonObject();
    }

    @Test public void readInteger() throws Exception {
        assertThat(sampleJSON.get("int").getAsInt(), equalTo(10));
        assertThat(sampleJSON.get("float").getAsInt(), equalTo(5));
        thrown.expect(NumberFormatException.class);
        sampleJSON.get("string").getAsInt();
    }

    @Test public void readFloat() throws Exception {
        assertThat(sampleJSON.get("float").getAsDouble(), IsCloseTo.closeTo(5.0, 1e-6));
        assertThat(sampleJSON.get("int").getAsDouble(), IsCloseTo.closeTo(10, 1e-6));
        thrown.expect(NumberFormatException.class);
        sampleJSON.get("string").getAsFloat();
    }

    @Test public void readString() throws Exception {
        assertThat(sampleJSON.get("string").getAsString(), equalTo("wow"));
        thrown.expect(IllegalStateException.class);
        sampleJSON.get("enums").getAsString();
    }

    @Test public void readBoolean() throws Exception {
        assertThat(sampleJSON.get("bool").getAsBoolean(), equalTo(false));
        assertThat(sampleJSON.get("int").getAsJsonPrimitive().isBoolean(), equalTo(false));
    }

    @Test public void readOptional() throws Exception {
        assertThat(JsonUtils.getOptional(sampleJSON, "nope"), equalTo(Optional.empty()));
        assertThat(JsonUtils.getOptional(sampleJSON, "int").map(JsonElement::getAsInt).orElse(0), equalTo(10));
    }

    @Test public void loadStringsFromArr() throws Exception {
        assertThat(JsonUtils.stringsFromJson(sampleJSON.getAsJsonArray("manystrings")),
                        IsCollectionContaining.hasItems("foo", "bar", "baz"));
    }

    @Test public void testCollectionFromJson() throws Exception {
        assertThat(JsonUtils.collectionFromJson(sampleJSON.getAsJsonArray("enums"), ClothingTrait.class),
                        IsCollectionContaining.hasItems(ClothingTrait.broody, ClothingTrait.open, ClothingTrait.kinky));
    }

    @Test public void testMapFromJson() throws Exception {
        assertThat(JsonUtils.mapFromJson(sampleJSON.getAsJsonObject("map"), Attribute.class, Integer.class),
                        allOf(IsMapContaining.hasEntry(Attribute.Power, 5),
                                        IsMapContaining.hasEntry(Attribute.Seduction, 15)));
    }

    @Test public void rootFromFile() throws Exception {
        JsonElement tinyJSON =
                        JsonUtils.rootJson(new File("NightgamesTests/nightgames/global/tiny_sample.json").toPath());
        JsonElement expectedJSON = parser.parse("{ \"text\": \"I'm tiny!\" }");
        assertThat(tinyJSON, equalTo(expectedJSON));
    }
}
