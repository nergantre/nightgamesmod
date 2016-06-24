package nightgames.characters.custom.requirement;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;

public class MoodRequirement implements CustomRequirement {
    private Emotion mood;

    public MoodRequirement(Emotion mood) {
        this.mood = mood;
    }

    @Override
    public boolean meets(Combat c, Character self, Character other) {
        return self.getMood() == mood;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MoodRequirement that = (MoodRequirement) o;

        return mood == that.mood;

    }

    @Override public int hashCode() {
        return mood.hashCode();
    }
}
