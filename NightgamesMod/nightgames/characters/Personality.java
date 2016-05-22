package nightgames.characters;

import java.io.Serializable;
import java.util.HashSet;
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
    public Skill act(HashSet<Skill> available, Combat c);

    public Action move(HashSet<Action> available, HashSet<Movement> radar);

    public NPC getCharacter();

    public void rest(int time);

    public String bbLiner(Combat c);

    public String nakedLiner(Combat c);

    public String stunLiner(Combat c);

    public String taunt(Combat c);

    public String victory(Combat c, Result flag);

    public String defeat(Combat c, Result flag);

    public String victory3p(Combat c, Character target, Character assist);

    public String intervene3p(Combat c, Character target, Character assist);

    public String describe(Combat c);

    public String draw(Combat c, Result flag);

    public boolean fightFlight(Character opponent);

    public boolean attack(Character opponent);

    public void ding();

    public String startBattle(Character other);

    public boolean fit();

    public String night();

    public boolean checkMood(Combat c, Emotion mood, int value);

    public String image(Combat c);

    public void pickFeat();

    public String describeAll(Combat c);

    public String temptLiner(Combat c);

    public String orgasmLiner(Combat c);

    public String makeOrgasmLiner(Combat c);

    public String getType();

    public RecruitmentData getRecruitmentData();

    public AiModifiers getAiModifiers();
    
    public void setAiModifiers(AiModifiers mods);
    
    public void resetAiModifiers();

    public String resist3p(Combat combat, Character target, Character assist);

    public Map<CommentSituation, String> getComments(Combat c);

    public default void resolveOrgasm(Combat c, Character opponent, BodyPart selfPart, BodyPart opponentPart, int times,
                    int totalTimes) {
        // no op
    }
}
