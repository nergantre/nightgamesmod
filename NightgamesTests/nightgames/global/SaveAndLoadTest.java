package nightgames.global;

import nightgames.characters.Character;
import org.hamcrest.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Tests for saving and loading game data.
 */
public class SaveAndLoadTest {
    private Path savePath = new File("NightGamesTests/nightgames/global/test_save.ngs").toPath();

    @Before public void setUp() throws Exception {
        new TestGlobal();
    }

    @Test public void testLoadAndSave() throws Exception {
        Global.load(savePath.toFile());
        SaveData firstLoadData = Global.saveData();
        Path tempSave = Files.createTempFile("", "");
        Global.save(tempSave.toFile());
        Global.load(tempSave.toFile());
        SaveData reloadedData = Global.saveData();
        assertThat(reloadedData.players, equalTo(firstLoadData.players));
        for (Character player : firstLoadData.players) {
            Character reloaded = reloadedData.players.stream().filter(p -> p.equals(player)).findFirst()
                            .orElseThrow(AssertionError::new);
            assertThat(reloaded, CharacterStatMatcher.statsMatch(player));
        }
        assertThat(reloadedData, equalTo(firstLoadData));
    }

    private static class CharacterStatMatcher extends TypeSafeMatcher<Character> {
        private Character me;

        CharacterStatMatcher(Character me) {
            this.me = me;
        }

        @Override public boolean matchesSafely(Character other) {
            return me.hasSameStats(other);
        }

        @Override public void describeMismatchSafely(Character other, Description description) {
            description.appendText("was").appendValue(other.printStats());
        }

        @Override public void describeTo(Description description) {
            description.appendText(me.printStats());
        }

        @Factory static CharacterStatMatcher statsMatch(Character me) {
            return new CharacterStatMatcher(me);
        }
    }
}
