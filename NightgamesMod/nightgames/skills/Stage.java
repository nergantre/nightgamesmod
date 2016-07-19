package nightgames.skills;

import nightgames.characters.Character;

public enum Stage {
    FOREPLAY(.006), // 30% bonus at 0 arousal, 30% malus at 100% arousal
    FINISHER(-.006), // 30% bonus at 100% arousal, 30% malus at 0 arousal
    REGULAR(0); // No change

    public static final double AROUSAL_PIVOT = 50; // %

    private final double arousalFactor;

    private Stage(double af) {
        arousalFactor = af;
    }

    public static Stage stageOf(Character ch) {
        /*
         * Foreplay: Arousal is at most (fraction of Willpower / 2)% 
         *           At full Willpower: 50% 
         *           At half Willpower: 25% 
         *           At no Willpower: 0% (no foreplay anymore) 
         * Finisher: Arousal is at least (fraction of Willpower / 3 + 0.5)% 
         *           At full Willpower: ~83% 
         *           At half Willpower: ~67% 
         *           At no Willpower: 50%
         */
        double will = ch.getWillpower().percent();
        double arousal = ch.getArousal().percent();

        if (arousal <= will / 2)
            return FOREPLAY;
        if (arousal >= will / 3 + 50)
            return FINISHER;
        return REGULAR;
    }

    public double multiplierFor(Character ch) {
        if (this == REGULAR)
            return 0.0; // Regular skills have no special effects

        Stage target = stageOf(ch);

        if (target == REGULAR)
            return 0.0;

        return arousalFactor * (AROUSAL_PIVOT - ch.getArousal()
                                                  .percent());
    }

    public static String describe(Character ch) {
        Stage stage = stageOf(ch);
        if (ch.human()) {
            if (stage == FOREPLAY) {
                return "You don't feel quite ready to get all hot and heavy yet.<br>";
            }
            if (stage == FINISHER) {
                return "You are pumped, ready to finish this!<br>";
            }
            return "";
        } else {
            if (stage == FOREPLAY) {
                return ch.pronoun() + " does not seem particularly passionate yet.<br>";
            }
            if (stage == FINISHER) {
                return ch.pronoun() + " is completely absorbed in the fight, giving it " + ch.possessivePronoun()
                                + " all.<br>";
            }
            return "";
        }
    }
}
