package nightgames.characters;

import nightgames.items.clothing.Clothing;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Tests relating to the Player character class.
 * <br/><br/>
 *
 */
public class PlayerTest {
    @BeforeClass public static void setUpPlayerTest() {
        Clothing.buildClothingTable();
    }

    @Test public void testStartGenitals() throws Exception {
        Map<Attribute, Integer> selectedAttributes = new HashMap<>();
        selectedAttributes.put(Attribute.Power, 5);
        selectedAttributes.put(Attribute.Seduction, 6);
        selectedAttributes.put(Attribute.Cunning, 7);
        Player playerMale =
                        new Player("dude", CharacterSex.male, Optional.empty(), new ArrayList<>(), selectedAttributes);
        Player playerFemale = new Player("chick", CharacterSex.female, Optional.empty(), new ArrayList<>(),
                        selectedAttributes);
        Player playerHerm =
                        new Player("futa", CharacterSex.herm, Optional.empty(), new ArrayList<>(), selectedAttributes);
        Player playerAsexual = new Player("ace", CharacterSex.asexual, Optional.empty(), new ArrayList<>(),
                        selectedAttributes);

        assertTrue("Male player has no cock!", playerMale.body.has("cock"));
        assertTrue("Male player has no balls!", playerMale.body.has("balls"));
        assertFalse("Male player has a pussy!", playerMale.body.has("pussy"));

        assertFalse("Female player has a cock!", playerFemale.body.has("cock"));
        assertFalse("Female player has balls!", playerFemale.body.has("balls"));
        assertTrue("Female player has no pussy!", playerFemale.body.has("pussy"));

        assertTrue("Herm player has no cock!", playerHerm.body.has("cock"));
        assertFalse("Herm player has balls!", playerHerm.body.has("balls"));
        assertTrue("Herm player has no pussy!", playerHerm.body.has("pussy"));

        assertFalse("Asexual player has a cock!", playerAsexual.body.has("cock"));
        assertFalse("Asexual player has balls!", playerAsexual.body.has("balls"));
        assertFalse("Asexual player has a pussy!", playerAsexual.body.has("pussy"));
    }
}
