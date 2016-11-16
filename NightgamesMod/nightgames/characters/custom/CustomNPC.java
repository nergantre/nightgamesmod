package nightgames.characters.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import nightgames.characters.BasePersonality;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.NPC;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.ItemAmount;
import nightgames.start.NpcConfiguration;

public class CustomNPC extends BasePersonality {
    private final NPCData data;
    private static final long serialVersionUID = -8169646189131720872L;

    public static final String TYPE_PREFIX = "CUSTOM_";

    public CustomNPC(NPCData data){
        this(data, Optional.empty(), Optional.empty());
    }

    public CustomNPC(NPCData data, Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        super(data.getName(), data.getStats().level, data.isStartCharacter());
        this.data = data;
        setupCharacter(charConfig, commonConfig);
    }

    @Override
    public void applyStrategy(NPC self) {
        self.isStartCharacter = data.isStartCharacter();
        self.plan = data.getPlan();
        self.mood = Emotion.confident;
    }

    @Override
    public void applyBasicStats(Character self) {
        preferredAttributes = new ArrayList<>(data.getPreferredAttributes());

        self.outfitPlan.addAll(data.getTopOutfit());
        self.outfitPlan.addAll(data.getBottomOutfit());
        self.closet.addAll(self.outfitPlan);
        self.change();
        self.att = new HashMap<>(data.getStats().attributes);
        self.traits = new CopyOnWriteArrayList<>(data.getStats().traits);
        self.getArousal().setMax(data.getStats().arousal);
        self.getStamina().setMax(data.getStats().stamina);
        self.getMojo().setMax(data.getStats().mojo);
        self.getWillpower().setMax(data.getStats().willpower);
        self.setTrophy(data.getTrophy());
        self.custom = true;

        try {
            self.body = data.getBody().clone(self);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        self.initialGender = data.getSex();

        for (ItemAmount i : data.getStartingItems()) {
            self.gain(i.item, i.amount);
        }

        Global.gainSkills(self);
    }

    public void setGrowth() {
        character.setGrowth(data.getGrowth());
    }

    @Override
    public void rest(int time) {
        for (ItemAmount i : data.getPurchasedItems()) {
            buyUpTo(i.item, i.amount);
        }
    }

    @Override
    public String bbLiner(Combat c, Character other) {
        return data.getLine("hurt", c, character, c.getOpponent(character));
    }

    @Override
    public String nakedLiner(Combat c, Character opponent) {
        return data.getLine("naked", c, character, c.getOpponent(character));
    }

    @Override
    public String stunLiner(Combat c, Character opponent) {
        return data.getLine("stunned", c, character, c.getOpponent(character));
    }

    @Override
    public String taunt(Combat c, Character opponent) {
        return data.getLine("taunt", c, character, c.getOpponent(character));
    }

    @Override
    public String temptLiner(Combat c, Character opponent) {
        return data.getLine("tempt", c, character, c.getOpponent(character));
    }

    @Override
    public String victory(Combat c, Result flag) {
        character.getArousal().empty();
        return data.getLine("victory", c, character, c.getOpponent(character));
    }

    @Override
    public String defeat(Combat c, Result flag) {
        return data.getLine("defeat", c, character, c.getOpponent(character));
    }

    @Override
    public String describe(Combat c) {
        return data.getLine("describe", c, character, c.getOpponent(character));
    }

    @Override
    public String draw(Combat c, Result flag) {
        return data.getLine("draw", c, character, c.getOpponent(character));
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
        return data.getLine("orgasm", c, character, c.getOpponent(character));
    }

    @Override
    public String makeOrgasmLiner(Combat c) {
        return data.getLine("makeOrgasm", c, character, c.getOpponent(character));
    }

    @Override
    public String getType() {
        return TYPE_PREFIX + data.getType();
    }

    @Override
    public String image(Combat c) {
        Character other = c.getOpponent(character);
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
        all.entrySet().stream().filter(e -> e.getKey().isApplicable(c, character, c.getOpponent(character)))
                        .forEach(e -> applicable.put(e.getKey(), e.getValue()));
        return applicable;
    }
}
