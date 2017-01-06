package nightgames.characters.custom;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public interface CharacterLine {
    public static String ORGASM_LINER = "orgasm";
    public static String MAKE_ORGASM_LINER = "makeOrgasm";
    public static String NIGHT_LINER = "night";
    public static String BB_LINER = "hurt";
    public static String STUNNED_LINER = "stunned";
    public static String NAKED_LINER = "naked";
    public static String TEMPT_LINER = "tempt";
    public static String TAUNT_LINER = "taunt";
    public static String CHALLENGE = "startBattle";
    public static String LEVEL_DRAIN_LINER = "levelDrain";
    public static String DESCRIBE_LINER = "describe";
    public static String ALL_LINES[] = {DESCRIBE_LINER, CHALLENGE, ORGASM_LINER, MAKE_ORGASM_LINER, NIGHT_LINER, BB_LINER, STUNNED_LINER, NAKED_LINER, TEMPT_LINER, TAUNT_LINER, LEVEL_DRAIN_LINER};

    String getLine(Combat c, Character self, Character other);
}