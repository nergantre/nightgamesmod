package nightgames.gui;

import nightgames.characters.Attribute;
import nightgames.characters.Trait;
import nightgames.global.Global;
import nightgames.global.TestGlobal;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

/**
 * TODO: Write class-level documentation.
 */
public class CreationGUITest {
    @Before public void setUp() throws Exception {
        new TestGlobal();
    }

    @Test public void testSelectPlayerStats() throws Exception {
        CreationGUI creationGUI = Global.gui().creation;
        creationGUI.namefield.setText("TestPlayer");
        creationGUI.StrengthBox.setSelectedItem(Trait.romantic);
        creationGUI.WeaknessBox.setSelectedItem(Trait.insatiable);
        creationGUI.power = 5;
        creationGUI.seduction = 11;
        creationGUI.cunning = 9;
        creationGUI.makeGame(Optional.empty());
        assertThat(Global.human.att, allOf(hasEntry(Attribute.Power, 5), hasEntry(Attribute.Seduction, 11),
                        hasEntry(Attribute.Cunning, 9)));
        assertThat(Global.human.getTraits(), IsCollectionContaining.hasItems(Trait.romantic, Trait.insatiable));
    }
}
