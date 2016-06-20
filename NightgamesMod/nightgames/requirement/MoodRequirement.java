package nightgames.requirement;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;

/**
 * Returns true if character self has a certain active mood.
 */
public class MoodRequirement implements Requirement {
    private final Emotion mood;

    public MoodRequirement(Emotion mood) {
        this.mood = mood;
    }

    public MoodRequirement(String mood) {
        this.mood = Emotion.valueOf(mood);
    }

    @Override public String getKey() {
        return "mood";
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.getMood() == mood;
    }
}
