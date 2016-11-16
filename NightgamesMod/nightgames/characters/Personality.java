package nightgames.characters;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nightgames.actions.Action;
import nightgames.actions.Movement;
import nightgames.characters.body.BodyPart;
import nightgames.characters.custom.AiModifiers;
import nightgames.characters.custom.CommentSituation;
import nightgames.characters.custom.RecruitmentData;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.skills.Skill;

public interface Personality extends Serializable {
    Skill act(HashSet<Skill> available, Combat c);

    Action move(HashSet<Action> available, HashSet<Movement> radar);

    NPC getCharacter();

    void rest(int time);

    String bbLiner(Combat c, Character other);

    String nakedLiner(Combat c, Character opponent);

    String stunLiner(Combat c, Character opponent);

    String taunt(Combat c, Character opponent);

    String victory(Combat c, Result flag);

    String defeat(Combat c, Result flag);

    String victory3p(Combat c, Character target, Character assist);

    String intervene3p(Combat c, Character target, Character assist);

    String describe(Combat c);

    String draw(Combat c, Result flag);

    boolean fightFlight(Character opponent);

    boolean attack(Character opponent);

    void ding();

    String startBattle(Character other);

    boolean fit();

    String night();

    boolean checkMood(Combat c, Emotion mood, int value);

    String image(Combat c);

    void pickFeat();

    String describeAll(Combat c);

    String temptLiner(Combat c, Character opponent);

    String orgasmLiner(Combat c);

    String makeOrgasmLiner(Combat c);

    String getType();

    RecruitmentData getRecruitmentData();

    AiModifiers getAiModifiers();

    void setAiModifiers(AiModifiers mods);

    void resetAiModifiers();

    String resist3p(Combat combat, Character target, Character assist);
    List<PreferredAttribute> getPreferredAttributes();

    Map<CommentSituation, String> getComments(Combat c);

    default void resolveOrgasm(Combat c, Character opponent, BodyPart selfPart, BodyPart opponentPart, int times,
                    int totalTimes) {
        // no op
    }

    default void eot(Combat c, Character opponent, Skill last) {
        // noop
    }

    void setCharacter(NPC npc);

    void applyBasicStats(Character self);
    void applyStrategy(NPC self);
}
