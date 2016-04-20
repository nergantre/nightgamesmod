package nightgames.characters.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import nightgames.characters.Attribute;
import nightgames.characters.BasePersonality;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.Body;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.ItemAmount;

public class CustomNPC extends BasePersonality {
    /**
     *
     */
    private NPCData data;
    private static final long serialVersionUID = -8169646189131720872L;

    public CustomNPC(NPCData data) {
        super(data.getName(), data.getStats().level);
        this.data = data;
        growth = data.getGrowth();
        preferredAttributes = new ArrayList<PreferredAttribute>(data.getPreferredAttributes());

        character.outfitPlan.addAll(data.getTopOutfit());
        character.outfitPlan.addAll(data.getBottomOutfit());
        character.closet.addAll(character.outfitPlan);
        character.change();
        character.att = new HashMap<Attribute, Integer>(data.getStats().attributes);
        character.traits = new HashSet<Trait>(data.getStats().traits);
        character.getArousal().setMax(data.getStats().arousal);
        character.getStamina().setMax(data.getStats().stamina);
        character.getMojo().setMax(data.getStats().mojo);
        character.getWillpower().setMax(data.getStats().willpower);
        character.setTrophy(data.getTrophy());
        character.plan = data.getPlan();
        character.mood = Emotion.confident;
        character.custom = true;
        try {
            character.body = data.getBody().clone(character);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            character.body = new Body(character);
        }
        character.body.finishBody(data.getSex());
        for (ItemAmount i : data.getStartingItems()) {
            character.gain(i.item, i.amount);
        }
        Global.gainSkills(character);
    }

    @Override
    public void rest(int time) {
        for (ItemAmount i : data.getPurchasedItems()) {
            buyUpTo(i.item, i.amount);
        }
    }

    @Override
    public String bbLiner(Combat c) {
        return data.getLine("hurt", c, character, c.getOther(character));
    }

    @Override
    public String nakedLiner(Combat c) {
        return data.getLine("naked", c, character, c.getOther(character));
    }

    @Override
    public String stunLiner(Combat c) {
        return data.getLine("stunned", c, character, c.getOther(character));
    }

    @Override
    public String taunt(Combat c) {
        return data.getLine("taunt", c, character, c.getOther(character));
    }

    @Override
    public String temptLiner(Combat c) {
        return data.getLine("tempt", c, character, c.getOther(character));
    }

    @Override
    public String victory(Combat c, Result flag) {
        character.getArousal().empty();
        return data.getLine("victory", c, character, c.getOther(character));
    }

    @Override
    public String defeat(Combat c, Result flag) {
        return data.getLine("defeat", c, character, c.getOther(character));
    }

    @Override
    public String describe(Combat c) {
        return data.getLine("describe", c, character, c.getOther(character));
    }

    @Override
    public String draw(Combat c, Result flag) {
        return data.getLine("draw", c, character, c.getOther(character));
    }

    @Override
    public boolean fightFlight(Character opponent) {
        return !character.mostlyNude() || opponent.mostlyNude();
    }

    @Override
    public boolean attack(Character opponent) {
        return true;
    }

    @Override
    public String victory3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return data.getLine("victory3p", c, character, assist);
        } else {
            return data.getLine("victory3pAssist", c, character, target);
        }
    }

    @Override
    public String intervene3p(Combat c, Character target, Character assist) {
        if (target.human()) {
            return data.getLine("intervene3p", c, character, assist);
        } else {
            return data.getLine("intervene3pAssist", c, character, target);
        }
    }

    @Override
    public String startBattle(Character other) {
        return data.getLine("startBattle", null, character, other);
    }

    @Override
    public boolean fit() {
        return !character.mostlyNude() && character.getStamina().percent() >= 50;
    }

    @Override
    public String night() {
        return data.getLine("startBattle", null, character, Global.getPlayer());
    }

    @Override
    public boolean checkMood(Combat c, Emotion mood, int value) {
        return data.checkMood(character, mood, value);
    }

    @Override
    public String orgasmLiner(Combat c) {
        return data.getLine("orgasm", c, character, c.getOther(character));
    }

    @Override
    public String makeOrgasmLiner(Combat c) {
        return data.getLine("makeOrgasm", c, character, c.getOther(character));
    }

    @Override
    public String getType() {
        return "CUSTOM_" + data.getType();
    }

    @Override
    public String image(Combat c) {
        Character other = c.getOther(character);
        return data.getPortraitName(c, character, other);
    }

    public String defaultImage() {
        return data.getDefaultPortraitName();
    }

    @Override
    public RecruitmentData getRecruitmentData() {
        return data.getRecruitment();
    }

    @Override
    public AiModifiers getAiModifiers() {
        return data.getAiModifiers();
    }

    @Override
    public Map<CommentSituation, String> getComments(Combat c) {
        Map<CommentSituation, String> all = data.getComments();
        Map<CommentSituation, String> applicable = new HashMap<>();
        all.entrySet().stream().filter(e -> e.getKey().isApplicable(c, character, c.getOther(character)))
                        .forEach(e -> applicable.put(e.getKey(), e.getValue()));
        return applicable;
    }
}
