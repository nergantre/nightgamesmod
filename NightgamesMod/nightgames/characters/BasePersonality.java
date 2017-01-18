package nightgames.characters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import nightgames.actions.Action;
import nightgames.actions.Movement;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.custom.AiModifiers;
import nightgames.characters.custom.CharacterLine;
import nightgames.characters.custom.CommentSituation;
import nightgames.characters.custom.RecruitmentData;
import nightgames.combat.Combat;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.skills.Skill;
import nightgames.start.NpcConfiguration;

public abstract class BasePersonality implements Personality {
    /**
     *
     */
    private static final long serialVersionUID = 2279220186754458082L;
    private String type;
    public NPC character;
    protected List<PreferredAttribute> preferredAttributes;
    protected CockMod preferredCockMod;
    protected AiModifiers mods;

    protected BasePersonality(String name, boolean isStartCharacter) {
        // Make the built-in character
        type = getClass().getSimpleName();
        character = new NPC(name, 1, this);
        character.isStartCharacter = isStartCharacter;
        preferredCockMod = CockMod.error;
        preferredAttributes = new ArrayList<PreferredAttribute>();
    }

    public BasePersonality(String name, Optional<NpcConfiguration> charConfig,
                    Optional<NpcConfiguration> commonConfig, boolean isStartCharacter) {
        this(name, isStartCharacter);
        setupCharacter(charConfig, commonConfig);
    }

    protected void setupCharacter(Optional<NpcConfiguration> charConfig, Optional<NpcConfiguration> commonConfig) {
        setGrowth();
        applyBasicStats(character);
        applyStrategy(character);

        // Apply config changes
        Optional<NpcConfiguration> mergedConfig = NpcConfiguration.mergeOptionalNpcConfigs(charConfig, commonConfig);
        mergedConfig.ifPresent(cfg -> cfg.apply(character));

        if (Global.checkFlag("FutaTime") && character.initialGender == CharacterSex.female) {
            character.initialGender = CharacterSex.herm;
        }
        character.body.makeGenitalOrgans(character.initialGender);
        character.body.finishBody(character.initialGender);
        for (int i = 1; i < character.getLevel(); i++) {
            character.getGrowth().levelUp(character);
        }
        character.distributePoints(preferredAttributes);
        character.getGrowth().addOrRemoveTraits(character);
    }

    public void setCharacter(NPC c) {
        this.character = c;
    }

    abstract public void setGrowth();

    @Override
    public void rest(int time) {
        if (preferredCockMod != CockMod.error && character.rank > 0) {
            Optional<BodyPart> optDick = character.body.get("cock")
                                                       .stream()
                                                       .filter(part -> part.moddedPartCountsAs(character, preferredCockMod))
                                                       .findAny();
            if (optDick.isPresent()) {
                CockPart part = (CockPart) optDick.get();
                character.body.remove(part);
                character.body.add(part.applyMod(preferredCockMod));
            }
        }
    }

    public void buyUpTo(Item item, int number) {
        while (character.money > item.getPrice() && character.count(item) < number) {
            character.money -= item.getPrice();
            character.gain(item);
        }
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Skill act(HashSet<Skill> available, Combat c) {
        HashSet<Skill> tactic;
        Skill chosen;
        ArrayList<WeightedSkill> priority = Decider.parseSkills(available, c, character);
        if (!Global.checkFlag(Flag.dumbmode)) {
            chosen = Decider.prioritizeNew(character, priority, c);
        } else {
            chosen = character.prioritize(priority);
        }
        if (chosen == null) {
            tactic = available;
            Skill[] actions = tactic.toArray(new Skill[tactic.size()]);
            return actions[Global.random(actions.length)];
        } else {
            return chosen;
        }
    }

    @Override
    public Action move(Collection<Action> available, Collection<Movement> radar) {
        Action proposed = Decider.parseMoves(available, radar, character);
        return proposed;
    }

    @Override
    public void pickFeat() {
        ArrayList<Trait> available = new ArrayList<Trait>();
        for (Trait feat : Global.getFeats(character)) {
            if (!character.has(feat)) {
                available.add(feat);
            }
        }
        if (available.size() == 0) {
            return;
        }
        character.add((Trait) available.toArray()[Global.random(available.size())]);
    }

    @Override
    public String image(Combat c) {
        String fname = getType()
                                .toLowerCase()
                        + "_" + character.mood.name() + ".jpg";
        return fname;
    }

    public String defaultImage(Combat c) {
        return character.getTrueName()
                        .toLowerCase() + "_confident.jpg";
    }

    @Override
    public void ding(Character self) {
        self.getGrowth().levelUp(self);
        onLevelUp(self);
        self.distributePoints(preferredAttributes);
    }

    @Override
    public List<PreferredAttribute> getPreferredAttributes() {
        return preferredAttributes;
    }

    protected void onLevelUp(Character self) {
        // NOP
    }

    @Override
    public String describeAll(Combat c, Character self) {
        StringBuilder b = new StringBuilder();
        b.append(self.getRandomLineFor(CharacterLine.DESCRIBE_LINER, c));
        b.append("<br/><br/>");
        self.body.describe(b, c.getOpponent(self), " ");
        b.append("<br/>");
        for (Trait t : self.getTraits()) {
            t.describe(self, b);
            b.append(' ');
        }
        b.append("<br/>");
        return b.toString();
    }

    @Override
    public NPC getCharacter() {
        return character;
    }

    @Override
    public RecruitmentData getRecruitmentData() {
        return null;
    }

    @Override
    public AiModifiers getAiModifiers() {
        if (mods == null)
            resetAiModifiers();
        return mods;
    }
    
    @Override
    public void setAiModifiers(AiModifiers mods) {
        this.mods = mods;
    }
    
    @Override
    public void resetAiModifiers() {
        mods = AiModifiers.getDefaultModifiers(getType());
    }
    
    @Override
    public String resist3p(Combat c, Character target, Character assist) {
        return null;
    }

    @Override
    public Map<CommentSituation, String> getComments(Combat c) {
        Map<CommentSituation, String> all = CommentSituation.getDefaultComments(getType());
        Map<CommentSituation, String> applicable = new HashMap<>();
        all.entrySet()
           .stream()
           .filter(e -> e.getKey()
                         .isApplicable(c, character, c.getOpponent(character)))
           .forEach(e -> applicable.put(e.getKey(), e.getValue()));
        return applicable;
    }
}
