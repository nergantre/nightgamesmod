package nightgames.combat;

import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.characters.BlankPersonality;
import nightgames.characters.CharacterSex;
import nightgames.characters.NPC;
import nightgames.characters.Trait;
import nightgames.global.Global;
import nightgames.global.TestGlobal;
import nightgames.items.clothing.Clothing;
import nightgames.stance.Stance;
import nightgames.stance.TestPosition;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * TODO: Write class-level documentation.
 */
public class CombatTest {
    @BeforeClass public static void setUpClass() throws Exception {
        Clothing.buildClothingTable();
        new TestGlobal();
        Global.newGame("TestPlayer", Optional.empty(), new ArrayList<>(), CharacterSex.asexual, new HashMap<>());
    }

    private NPC self;
    private NPC other;
    private Combat combat;

    @Before public void setUp() throws Exception {
        self = new BlankPersonality("SelfTestNPC", 1).character;
        other = new BlankPersonality("OtherTestNPC", 1).character;
        Area area = new Area("TestArea", "TestArea description", Movement.beer);
        combat = new Combat(self, other, area);
    }

    @Test public void getDominanceOfStanceNoTraits() throws Exception {
        // Neutral position. No dominance involved, so neither character should lose willpower.
        combat.setStance(new TestPosition(self, other, Stance.neutral, 0));
        assertThat(combat.getDominanceOfStance(self), equalTo(0));
        assertThat(combat.getDominanceOfStance(other), equalTo(0));

        // Self is dominant. Other should lose willpower but self should not.
        combat.setStance(new TestPosition(self, other, Stance.engulfed, 5));
        assertThat(combat.getDominanceOfStance(self), equalTo(5));
        assertThat(combat.getDominanceOfStance(other), equalTo(0));

        // Negative position dominance. Not a valid dominance value, but we'll accept it and treat it like a neutral position.
        combat.setStance(new TestPosition(self, other, Stance.coiled, -5));
        assertThat(combat.getDominanceOfStance(self), equalTo(0));
        assertThat(combat.getDominanceOfStance(other), equalTo(0));
    }


    @Test public void getDominanceOfStanceSmqueen() throws Exception {
        self.add(Trait.smqueen);
        // Neutral position. No dominance involved, so neither character should lose willpower, regardless of traits.
        combat.setStance(new TestPosition(self, other, Stance.neutral, 0));
        assertThat(combat.getDominanceOfStance(self), equalTo(0));
        assertThat(combat.getDominanceOfStance(other), equalTo(0));

        // Self is dominant. Other should lose willpower but self should not. Trait increases effective stance dominance.
        combat.setStance(new TestPosition(self, other, Stance.engulfed, 5));
        assertThat(combat.getDominanceOfStance(self), equalTo(8));
        assertThat(combat.getDominanceOfStance(other), equalTo(0));

        // Negative position dominance. Not a valid dominance value, but we'll accept it and treat it like a neutral position.
        combat.setStance(new TestPosition(self, other, Stance.coiled, -5));
        assertThat(combat.getDominanceOfStance(self), equalTo(0));
        assertThat(combat.getDominanceOfStance(other), equalTo(0));
    }


    @Test public void getDominanceOfStanceSubmissive() throws Exception {
        self.add(Trait.submissive);
        // Neutral position. No dominance involved, so neither character should lose willpower, regardless of traits.
        combat.setStance(new TestPosition(self, other, Stance.neutral, 0));
        assertThat(combat.getDominanceOfStance(self), equalTo(0));
        assertThat(combat.getDominanceOfStance(other), equalTo(0));

        // Self is dominant. Other should lose willpower but self should not. Trait decreases effective stance dominance.
        combat.setStance(new TestPosition(self, other, Stance.engulfed, 5));
        assertThat(combat.getDominanceOfStance(self), equalTo(3));
        assertThat(combat.getDominanceOfStance(other), equalTo(0));

        // Negative position dominance. Not a valid dominance value, but we'll accept it and treat it like a neutral position.
        combat.setStance(new TestPosition(self, other, Stance.coiled, -5));
        assertThat(combat.getDominanceOfStance(self), equalTo(0));
        assertThat(combat.getDominanceOfStance(other), equalTo(0));
    }
}
