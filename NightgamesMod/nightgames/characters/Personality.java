package nightgames.characters;

import java.io.Serializable;
import java.util.Collection;
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

    Action move(Collection<Action> available, Collection<Movement> radar);

    NPC getCharacter();

    void rest(int time);

    String victory(Combat c, Result flag);

    String defeat(Combat c, Result flag);

    String victory3p(Combat c, Character target, Character assist);

    String intervene3p(Combat c, Character target, Character assist);

    String draw(Combat c, Result flag);

    boolean fightFlight(Character opponent);

    boolean attack(Character opponent);

    void ding(Character self);

    boolean fit();

    boolean checkMood(Combat c, Emotion mood, int value);

    String image(Combat c);

    void pickFeat();

    String describeAll(Combat c, Character self);

    String getType();

    RecruitmentData getRecruitmentData();

    AiModifiers getAiModifiers();

    void setAiModifiers(AiModifiers mods);

    void resetAiModifiers();

    String resist3p(Combat combat, Character target, Character assist);
    List<PreferredAttribute> getPreferredAttributes();

    Map<CommentSituation, String> getComments(Combat c);

    default void resolveOrgasm(Combat c, NPC self, Character opponent, BodyPart selfPart, BodyPart opponentPart, int times,
                    int totalTimes) {
        // no op
    }

    default void eot(Combat c, Character opponent) {
        // noop
    }

    void setCharacter(NPC npc);

    void applyBasicStats(Character self);
    void applyStrategy(NPC self);
}
