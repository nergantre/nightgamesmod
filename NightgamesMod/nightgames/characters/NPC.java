package nightgames.characters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.actions.Action;
import nightgames.actions.Leap;
import nightgames.actions.Move;
import nightgames.actions.Movement;
import nightgames.actions.Resupply;
import nightgames.actions.Shortcut;
import nightgames.areas.Area;
import nightgames.characters.body.BodyPart;
import nightgames.characters.custom.CharacterLine;
import nightgames.characters.custom.CommentSituation;
import nightgames.characters.custom.RecruitmentData;
import nightgames.combat.Combat;
import nightgames.combat.CombatScene;
import nightgames.combat.IEncounter;
import nightgames.combat.Result;
import nightgames.ftc.FTCMatch;
import nightgames.global.DebugFlags;
import nightgames.global.Encs;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.pet.arms.RoboArmManager;
import nightgames.skills.Nothing;
import nightgames.skills.Skill;
import nightgames.skills.Stage;
import nightgames.skills.Tactics;
import nightgames.skills.damage.DamageType;
import nightgames.skills.strategy.CombatStrategy;
import nightgames.skills.strategy.DefaultStrategy;
import nightgames.stance.Behind;
import nightgames.stance.Neutral;
import nightgames.stance.Position;
import nightgames.status.Disguised;
import nightgames.status.Enthralled;
import nightgames.status.Pheromones;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.trap.Trap;

public class NPC extends Character {
    public Personality ai;
    public HashMap<Emotion, Integer> emotes;
    public Emotion mood;
    public Plan plan;
    private boolean fakeHuman;
    public boolean isStartCharacter = false;
    private List<CombatStrategy> personalStrategies;
    private List<CombatScene> postCombatScenes;
    private Map<String, List<CharacterLine>> lines;

    public NPC(String name, int level, Personality ai) {
        super(name, level);
        this.ai = ai;
        this.lines = new HashMap<>();
        fakeHuman = false;
        emotes = new HashMap<>();
        for (Emotion e : Emotion.values()) {
            emotes.put(e, 0);
        }
        mood = Emotion.confident;
        initialGender = CharacterSex.female;
        personalStrategies = new ArrayList<>();
        postCombatScenes = new ArrayList<>();
    }

    protected void addPersonalStrategy(CombatStrategy strategy) {
        personalStrategies.add(strategy);
    }

    protected void addCombatScene(CombatScene scene) {
        postCombatScenes.add(scene);
    }
    
    public List<CombatScene> getPostCombatScenes() {
        return Collections.unmodifiableList(postCombatScenes);
    }

    @Override
    public String describe(int per, Combat c) {
        String description = ai.describeAll(c, this);
        for (Status s : status) {
            description = description + "<br/>" + s.describe(c);
        }
        description = description + "<br/><br/>";
        description = description + outfit.describe(this);
        description = description + observe(per);
        if (has(Trait.octo)) {
            description += "<p>You can see " + RoboArmManager.getManagerFor(this).describeArms() + " strapped behind "
                                + possessiveAdjective() + " back.<br/>";
        }
        return description;
    }

    private String observe(int per) {
        String visible = "";
        for (Status s : status) {
            if (s.flags().contains(Stsflag.unreadable)) {
                return visible;
            }
        }
        if (per >= 9) {
            visible = visible + "Her arousal is at " + arousal.percent() + "%<br/>";
        }
        if (per >= 8) {
            visible = visible + "Her stamina is at " + stamina.percent() + "%<br/>";
        }
        if (per >= 9) {
            visible = visible + "Her willpower is at " + willpower.percent() + "%<br/>";
        }
        if (per >= 7) {
            visible = visible + "She looks " + mood.name() + "<br/>";
        }
        if (per >= 7 && per < 9) {
            if (arousal.percent() >= 75) {
                visible = visible
                                + "She's dripping with arousal and breathing heavily. She's at least 3/4 of the way to orgasm<br/>";
            } else if (arousal.percent() >= 50) {
                visible = visible + "She's showing signs of arousal. She's at least halfway to orgasm<br/>";
            } else if (arousal.percent() >= 25) {
                visible = visible + "She's starting to look noticeably arousal, maybe a quarter of her limit<br/>";
            }
            if (willpower.percent() <= 75) {
                visible = visible + "She still seems ready to fight.<br/>";
            } else if (willpower.percent() <= 50) {
                visible = visible + "She seems a bit unsettled, but she still has some spirit left in her.<br/>";
            } else if (willpower.percent() <= 25) {
                visible = visible + "Her eyes seem glazed over and ready to give in.<br/>";
            }
        }
        if (per >= 6 && per < 8) {
            if (stamina.percent() <= 33) {
                visible = visible + "She looks a bit unsteady on her feet<br/>";
            } else if (stamina.percent() <= 66) {
                visible = visible + "She's starting to look tired<br/>";
            }
        }
        if (per >= 3 && per < 7) {
            if (arousal.percent() >= 50) {
                visible = visible + "She's showing clear sign of arousal. You're definitely getting to her.<br/>";
            }
            if (willpower.percent() <= 50) {
                visible = visible + "She seems a bit distracted and unable to look you in the eye.<br/>";
            }
        }
        if (per >= 4 && per < 6) {
            if (stamina.percent() <= 50) {
                visible = visible + "She looks pretty tired<br/>";
            }
        }

        if (per >= 5) {
            visible += Stage.describe(this);
        }
        
        if (per >= 6 && status.size() > 0) {
            visible += "List of statuses:<br/><i>";
            visible += status.stream().filter(s -> !s.flags().contains(Stsflag.disguised) || per >= 9).map(Status::toString).collect(Collectors.joining(", "));
            visible += "</i><br/>";
        }
        
        return visible;
    }

    @Override
    public void victory(Combat c, Result flag) {
        Character target;
        if (c.p1 == this) {
            target = c.p2;
        } else {
            target = c.p1;
        }
        if (!has(Trait.leveldrainer))
            gainXP(getVictoryXP(target));
        if (!target.has(Trait.leveldrainer))
            target.gainXP(getDefeatXP(this));
        target.arousal.empty();
        if (target.has(Trait.insatiable)) {
            target.arousal.restore((int) (arousal.max() * .2));
        }
        dress(c);
        target.undress(c);
        gainTrophy(c, target);

        target.defeated(this);
        c.write(ai.victory(c, flag));
        gainAttraction(target, 1);
        target.gainAttraction(this, 2);
    }

    @Override
    public void defeat(Combat c, Result flag) {
        Character target;
        if (c.p1 == this) {
            target = c.p2;
        } else {
            target = c.p1;
        }
        if (!has(Trait.leveldrainer))
            gainXP(getDefeatXP(target));
        if (!target.has(Trait.leveldrainer))
            target.gainXP(getVictoryXP(this));
        arousal.empty();
        if (!target.human() || !Global.getMatch().condition.name().equals("norecovery")) {
            target.arousal.empty();
        }
        if (this.has(Trait.insatiable)) {
            arousal.restore((int) (arousal.max() * .2));
        }
        if (target.has(Trait.insatiable)) {
            target.arousal.restore((int) (arousal.max() * .2));
        }
        target.dress(c);
        undress(c);
        target.gainTrophy(c, this);
        defeated(target);
        c.write(ai.defeat(c, flag));
        gainAttraction(target, 2);
        target.gainAttraction(this, 1);
    }

    @Override
    public void intervene3p(Combat c, Character target, Character assist) {
        gainXP(getAssistXP(target));
        target.defeated(this);
        c.write(ai.intervene3p(c, target, assist));
        assist.gainAttraction(this, 1);
    }

    @Override
    public void victory3p(Combat c, Character target, Character assist) {
        gainXP(getVictoryXP(target));
        target.gainXP(getDefeatXP(this));
        target.arousal.empty();
        if (target.has(Trait.insatiable)) {
            target.arousal.restore((int) (arousal.max() * .2));
        }
        dress(c);
        target.undress(c);
        gainTrophy(c, target);
        target.defeated(this);
        c.write(ai.victory3p(c, target, assist));
        gainAttraction(target, 1);
    }

    @Override
    public boolean resist3p(Combat combat, Character intruder, Character assist) {
        if (has(Trait.cursed)) {
            Global.gui().message(ai.resist3p(combat, intruder, assist));
            return true;
        }
        return false;
    }

    @Override
    public void act(Combat c) {
        act(c, c.getOpponent(this));
    }

    public void act(Combat c, Character target) {
        if (target.human() && Global.isDebugOn(DebugFlags.DEBUG_SKILL_CHOICES)) {
            pickSkillsWithGUI(c, target);
        } else {
            // if there's no strategy, try getting a new one.
            if (!c.getCombatantData(this).getStrategy().isPresent()) {
                c.getCombatantData(this).setStrategy(c, this, pickStrategy(c));
            }
            // if the strategy is out of moves, try getting a new one.
            Collection<Skill> possibleSkills = c.getCombatantData(this).getStrategy().get().nextSkills(c, this);
            if (possibleSkills.isEmpty()) {
                if (Global.isDebugOn(DebugFlags.DEBUG_STRATEGIES)) {
                    System.out.printf("%s has no moves available for strategy %s, picking a new one\n", this.getTrueName(), c.getCombatantData(this).getStrategy().get().getClass().getSimpleName());
                }
                c.getCombatantData(this).setStrategy(c, this, pickStrategy(c));
                possibleSkills = c.getCombatantData(this).getStrategy().get().nextSkills(c, this);
            }
            if (Global.isDebugOn(DebugFlags.DEBUG_STRATEGIES)) {
                System.out.println("next skills: " +  possibleSkills);
            }
            // if there are still no moves, just use all available skills for this turn and try again next turn.
            if (possibleSkills.isEmpty()) {
                if (Global.isDebugOn(DebugFlags.DEBUG_STRATEGIES)) {
                    System.out.printf("%s has no moves available for strategy %s\n", this.getTrueName(), c.getCombatantData(this).getStrategy().get().getClass().getSimpleName());
                }
                possibleSkills = getSkills();
            } else {
                if (Global.isDebugOn(DebugFlags.DEBUG_STRATEGIES)) {
                    System.out.printf("%s is using strategy %s\n", this.getTrueName(), c.getCombatantData(this).getStrategy().get().getClass().getSimpleName());
                }
            }
            HashSet<Skill> available = new HashSet<>();
            for (Skill act : possibleSkills) {
                if (Skill.skillIsUsable(c, act) && cooldownAvailable(act)) {
                    available.add(act);
                }
            }
            Skill.filterAllowedSkills(c, available, this, target);
            if (available.size() == 0) {
                available.add(new Nothing(this));
            }
            c.act(this, ai.act(available, c), "");
        }
    }

    private CombatStrategy pickStrategy(Combat c) {
        if (Global.random(100) < 60 ) {
            if (Global.isDebugOn(DebugFlags.DEBUG_STRATEGIES)) {
                System.out.println("Using default strategy");
            }
            // most of the time don't bother using a strategy.
            return new DefaultStrategy();
        }

        Map<Double, CombatStrategy> stratsWithCumulativeWeights = new HashMap<>();
        DefaultStrategy defaultStrat = new DefaultStrategy();
        double lastWeight = defaultStrat.weight(c, this);
        stratsWithCumulativeWeights.put(lastWeight, defaultStrat);
        List<CombatStrategy> allStrategies = new ArrayList<>(CombatStrategy.availableStrategies);
        allStrategies.addAll(personalStrategies);
        for (CombatStrategy strat: allStrategies) {
            if (strat.weight(c, this) < .01 || strat.nextSkills(c, this).isEmpty()) {
                continue;
            }
            lastWeight += strat.weight(c, this);
            stratsWithCumulativeWeights.put(lastWeight, strat);
        }
        if (Global.isDebugOn(DebugFlags.DEBUG_STRATEGIES)) {
            System.out.println("Available strategies: "+ stratsWithCumulativeWeights);
        }
        double random = Global.randomdouble() * lastWeight;
        for (Map.Entry<Double, CombatStrategy> entry: stratsWithCumulativeWeights.entrySet()) {
            if (random < entry.getKey()) {
                return entry.getValue();
            }
        }
        // we should have picked something, but w/e just return the default if we need to
        return defaultStrat;
    }

    public Skill actFast(Combat c) {
        HashSet<Skill> available = new HashSet<>();
        Character target;
        if (c.p1 == this) {
            target = c.p2;
        } else {
            target = c.p1;
        }
        for (Skill act : getSkills()) {
            if (Skill.skillIsUsable(c, act) && cooldownAvailable(act)) {
                available.add(act);
            }
        }
        Skill.filterAllowedSkills(c, available, this, target);
        if (available.size() == 0) {
            available.add(new Nothing(this));
        }
        return ai.act(available, c);
    }

    @Override
    public boolean human() {
        return fakeHuman;
    }

    public void setFakeHuman(boolean val) {
        fakeHuman = val;
    }

    @Override
    public void draw(Combat c, Result flag) {
        Character target;
        if (c.p1 == this) {
            target = c.p2;
        } else {
            target = c.p1;
        }
        if (!has(Trait.leveldrainer))
            gainXP(getVictoryXP(target));
        if (!target.has(Trait.leveldrainer))
            target.gainXP(getVictoryXP(this));
        arousal.empty();
        target.arousal.empty();
        if (this.has(Trait.insatiable)) {
            arousal.restore((int) (arousal.max() * .2));
        }
        if (target.has(Trait.insatiable)) {
            target.arousal.restore((int) (arousal.max() * .2));
        }
        target.undress(c);
        undress(c);
        target.gainTrophy(c, this);
        gainTrophy(c, target);
        target.defeated(this);
        defeated(target);
        c.write(ai.draw(c, flag));
        gainAttraction(target, 4);
        target.gainAttraction(this, 4);
        if (getAffection(target) > 0) {
            gainAffection(target, 1);
            target.gainAffection(this, 1);
        }
    }

    public String getRandomLineFor(String lineType, Combat c, Character other) {
        Map<String, List<CharacterLine>> lines = this.lines;
        Disguised disguised = (Disguised) getStatus(Stsflag.disguised);
        if (disguised != null) {
            lines = disguised.getTarget().getLines();
        }
        return Global.format(Global.pickRandom(lines.get(lineType)).orElse((cb, sf, ot) -> "").getLine(c, this, other), this, other);
    }

    @Override
    public String challenge(Character other) {
        return getRandomLineFor(CharacterLine.CHALLENGE, null, other);
    }

    @Override
    public String orgasmLiner(Combat c) {
        return getRandomLineFor(CharacterLine.ORGASM_LINER, c);
    }

    @Override
    public String makeOrgasmLiner(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.MAKE_ORGASM_LINER, c);
    }

    @Override
    public String bbLiner(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.BB_LINER, c);
    }

    @Override
    public String nakedLiner(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.NAKED_LINER, c);
    }

    @Override
    public String stunLiner(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.STUNNED_LINER, c);
    }

    @Override
    public String taunt(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.TAUNT_LINER, c);
    }

    @Override
    public String temptLiner(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.TEMPT_LINER, c);
    }

    @Override
    public void detect() {
    }

    @Override
    public void move() {
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.println(getTrueName() + " is moving with state " + state);
        }
        if (state == State.combat) {
            if (location != null && location.fight != null) {
                if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
                    System.out.println(getTrueName() + " is battling in the " + location.name);
                }
                location.fight.battle();
            } else {
                if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
                    System.out.println(getTrueName() + " is done battling in the " + location.name);
                }
            }
        } else if (busy > 0) {
            busy--;
        } else if (this.is(Stsflag.enthralled) && !has(Trait.immobile)) {
            Character master;
            master = ((Enthralled) getStatus(Stsflag.enthralled)).master;
            Move compelled = findPath(master.location);
            if (compelled != null) {
                compelled.execute(this);
                return;
            }
        } else if (state == State.shower || state == State.lostclothes) {
            bathe();
        } else if (state == State.crafting) {
            craft();
        } else if (state == State.searching) {
            search();
        } else if (state == State.resupplying) {
            resupply();
        } else if (state == State.webbed) {
            state = State.ready;
        } else if (state == State.masturbating) {
            masturbate();
        } else {
            if (!location.encounter(this)) {
                
                HashSet<Action> moves = new HashSet<>();
                HashSet<Movement> radar = new HashSet<>();
                FTCMatch match;
                if (Global.checkFlag(Flag.FTC) && allowedActions().isEmpty()) {
                    match = (FTCMatch) Global.getMatch();
                    if (match.isPrey(this) && match.getFlagHolder() == null) {
                        moves.add(findPath(match.gps("Central Camp")));
                        if (Global.isDebugOn(DebugFlags.DEBUG_FTC))
                            System.out.println(getTrueName() + " moving to get flag (prey)");
                    } else if (!match.isPrey(this) && has(Item.Flag) && !match.isBase(this, location)) {
                        moves.add(findPath(match.getBase(this)));
                        if (Global.isDebugOn(DebugFlags.DEBUG_FTC))
                            System.out.println(getTrueName() + " moving to deliver flag (hunter)");
                    } else if (!match.isPrey(this) && has(Item.Flag) && match.isBase(this, location)) {
                        if (Global.isDebugOn(DebugFlags.DEBUG_FTC))
                            System.out.println(getTrueName() + " delivering flag (hunter)");
                        new Resupply().execute(this);
                        return;
                    }
                }
                if (!has(Trait.immobile) && moves.isEmpty()) {
                    for (Area path : location.adjacent) {
                        moves.add(new Move(path));
                        if (path.ping(get(Attribute.Perception))) {
                            radar.add(path.id());
                        }
                    }
                    if (getPure(Attribute.Cunning) >= 28) {
                        for (Area path : location.shortcut) {
                            moves.add(new Shortcut(path));
                        }
                    }
                    if(getPure(Attribute.Ninjutsu)>=5){
                        for(Area path:location.jump){
                            moves.add(new Leap(path));
                        }
                    }
                }
                
                pickAndDoAction(allowedActions(), moves, radar);
            }
        }
    }
    
    private void pickAndDoAction(Collection<Action> available, Collection<Action> moves, Collection<Movement> radar) {
        if (available.isEmpty()) {
            available.addAll(Global.getActions());
            available.addAll(moves);
        }
        available.removeIf(a -> a == null || !a.usable(this));
        if (location.humanPresent()) {
            Global.gui().message("You notice " + getName() + ai.move(available, radar).execute(this).describe());
        } else {
            ai.move(available, radar).execute(this);
        }
    }

    @Override
    public void faceOff(Character opponent, IEncounter enc) {
        Encs encType;
        if (ai.fightFlight(opponent)) {
            encType = Encs.fight;
        } else if (has(Item.SmokeBomb)) {
            encType = Encs.smoke;
            remove(Item.SmokeBomb);
        } else {
            encType = Encs.flee;
        }
        enc.parse(encType, this, opponent);
    }

    @Override
    public void spy(Character opponent, IEncounter enc) {
        if (ai.attack(opponent)) {
            // enc.ambush(this, opponent);
            enc.parse(Encs.ambush, this, opponent);
        } else {
            location.endEncounter();
        }
    }

    @Override
    public void ding() {
        level++;
        ai.ding();
        String message = Global.gainSkills(this);
        if (human()) {
            Global.gui().message(message);
        }
    }

    @Override
    public void showerScene(Character target, IEncounter encounter) {
        Encs response;
        if (this.has(Item.Aphrodisiac)) {
            // encounter.aphrodisiactrick(this, target);
            response = Encs.aphrodisiactrick;
        } else if (!target.mostlyNude() && Global.random(3) >= 2) {
            // encounter.steal(this, target);
            response = Encs.stealclothes;
        } else {
            // encounter.showerambush(this, target);
            response = Encs.showerattack;
        }
        encounter.parse(response, this, target);
    }

    @Override
    public void intervene(IEncounter enc, Character p1, Character p2) {
        if (Global.random(20) + getAffection(p1) + (p1.has(Trait.sympathetic) ? 10 : 0) >= Global.random(20)
                        + getAffection(p2) + (p2.has(Trait.sympathetic) ? 10 : 0)) {
            enc.intrude(this, p1);
        } else {
            enc.intrude(this, p2);
        }
    }

    @Override
    public void promptTrap(IEncounter enc, Character target, Trap trap) {
        if (ai.attack(target) && (!target.human() || !Global.isDebugOn(DebugFlags.DEBUG_SPECTATE))) {
            enc.trap(this, target, trap);
        } else {
            location.endEncounter();
        }
    }

    public void addLine(String lineType, CharacterLine line) {
        lines.computeIfAbsent(lineType, type -> new ArrayList<>());
        lines.get(lineType).add(line);
    }

    @Override
    public void afterParty() {
        Global.gui().message(getRandomLineFor(CharacterLine.NIGHT_LINER, null, Global.getPlayer()));
    }

    public void daytime(int time) {
        ai.rest(time);
    }

    @Override
    public Emotion getMood() {
        return mood;
    }

    @Override
    public void counterattack(Character target, Tactics type, Combat c) {
        switch (type) {
            case damage:
                c.write(this, getName() + " avoids your clumsy attack and swings her fist into your nuts.");
                target.pain(c, target, 4 + Math.min(Global.random(get(Attribute.Power)), 20));
                break;
            case pleasure:
                if (target.hasDick()) {
                    if (target.crotchAvailable()) {
                        c.write(this, getName() + " catches you by the penis and rubs your sensitive glans.");
                        target.body.pleasure(this, body.getRandom("hands"), target.body.getRandom("cock"),
                                        4 + Math.min(Global.random(get(Attribute.Seduction)), 20), c);
                    } else {
                        c.write(this, getName() + " catches you as you approach and grinds her knee into the tent in your "
                                        + target.getOutfit().getTopOfSlot(ClothingSlot.bottom) +".");
                        target.body.pleasure(this, body.getRandom("legs"), target.body.getRandom("cock"),
                                        4 + Math.min(Global.random(get(Attribute.Seduction)), 20), c);
                    }
                } else {
                    c.write(this, getName()
                                    + " pulls you off balance and licks your sensitive ear. You tremble as she nibbles on your earlobe.");
                    target.body.pleasure(this, body.getRandom("tongue"), target.body.getRandom("ears"),
                                    4 + Math.min(Global.random(get(Attribute.Seduction)), 20), c);
                }
                break;
            case fucking:
                if (c.getStance().sub(this)) {
                    Position reverse = c.getStance().reverse(c, true);
                    if (reverse != c.getStance() && !BodyPart.hasOnlyType(reverse.bottomParts(), "strapon")) {
                        c.setStance(reverse, this, false);
                    } else {
                        c.write(this, Global.format(
                                        "{self:NAME-POSSESSIVE} quick wits find a gap in {other:name-possessive} hold and {self:action:slip|slips} away.",
                                        this, target));
                        c.setStance(new Neutral(this, target), this, true);
                    }
                } else {
                    target.body.pleasure(this, body.getRandom("hands"), target.body.getRandomBreasts(),
                                    4 + Math.min(Global.random(get(Attribute.Seduction)), 20), c);
                    c.write(this, Global.format(
                                    "{self:SUBJECT-ACTION:pinch|pinches} {other:possessive} nipples with {self:possessive} hands as {other:subject-action:try|tries} to fuck {self:direct-object}. "
                                                    + "While {other:subject-action:yelp|yelps} with surprise, {self:subject-action:take|takes} the chance to pleasure {other:possessive} body",
                                    this, target));
                }

                break;
            case stripping:
                Clothing clothes = target.stripRandom(c);
                if (clothes != null) {
                    c.write(this, getName()
                                    + " manages to catch you groping her clothing, and in a swift motion strips off your "
                                    + clothes.getName() + ".");
                } else {
                    c.write(this, getName()
                                    + " manages to dodge your groping hands and gives a retaliating slap in return.");
                    target.pain(c, target, 4 + Math.min(Global.random(get(Attribute.Power)), 20));
                }
                break;
            case positioning:
                if (c.getStance().dom(this)) {
                    c.write(this, getName() + " outmanuevers you and you're exhausted from the struggle.");
                    target.weaken(c, (int) this.modifyDamage(DamageType.stance, target, 15));
                } else {
                    c.write(this, getName() + " outmanuevers you and catches you from behind when you stumble.");
                    c.setStance(new Behind(this, target));
                }
                break;
            default:
                c.write(this, getName() + " manages to dodge your attack and gives a retaliating slap in return.");
                target.pain(c, target, 4 + Math.min(Global.random(get(Attribute.Power)), 20));
        }
    }

    public Skill prioritize(ArrayList<WeightedSkill> plist) {
        if (plist.isEmpty()) {
            return null;
        }
        double sum = 0;
        ArrayList<WeightedSkill> wlist = new ArrayList<>();
        for (WeightedSkill wskill : plist) {
            sum += wskill.weight;
            wlist.add(new WeightedSkill(sum, wskill.skill));
            if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
                System.out.printf("%.1f %s\n", sum, wskill.skill);
            }
        }

        if (wlist.isEmpty()) {
            return null;
        }
        double s = Global.randomdouble() * sum;
        for (WeightedSkill wskill : wlist) {
            if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
                System.out.printf("%.1f/%.1f %s\n", wskill.weight, s, wskill.skill);
            }
            if (wskill.weight > s) {
                return wskill.skill;
            }
        }
        return plist.get(plist.size() - 1).skill;
    }

    @Override
    public void emote(Emotion emo, int amt) {
        if (Global.isDebugOn(DebugFlags.DEBUG_MOOD)) {
            System.out.printf("%s: %+d %s", getTrueName(), amt, emo.name());
        }
        if (emo == mood) {
            // if already this mood, cut gain by half
            amt = Math.max(1, amt / 2);
        }
        emotes.put(emo, emotes.get(emo) + amt);
    }

    public Emotion moodSwing(Combat c) {
        Emotion current = mood;
        for (Emotion e : emotes.keySet()) {
            if (ai.checkMood(c, e, emotes.get(e))) {
                emotes.put(e, 0);
                // cut all the other emotions by half so that the new mood
                // persists for a bit
                for (Emotion e2 : emotes.keySet()) {
                    emotes.put(e2, emotes.get(e2) / 2);
                }
                mood = e;
                if (Global.isDebugOn(DebugFlags.DEBUG_MOOD)) {
                    System.out.printf("Moodswing: %s is now %s\n", getTrueName(), mood.name());
                }
                if (c.p1.human() || c.p2.human()) {
                    Global.gui().loadPortrait(c, c.p1, c.p2);
                }
                return e;
            }
        }
        return current;
    }

    @Override
    public void eot(Combat c, Character opponent, Skill last) {
        super.eot(c, opponent, last);
        ai.eot(c, opponent, last);
        if (opponent.has(Trait.pheromones) && opponent.getArousal().percent() >= 20 && opponent.rollPheromones(c)) {
            c.write(opponent, "<br/>You see " + getName()
                            + " swoon slightly as she gets close to you. Seems like she's starting to feel the effects of your musk.");
            add(c, Pheromones.getWith(opponent, this, opponent.getPheromonePower(), 10));
        }
        if (has(Trait.RawSexuality)) {
            c.write(this, Global.format("{self:NAME-POSSESSIVE} raw sexuality turns both of you on.", this, opponent));
            temptNoSkillNoSource(c, opponent, getArousal().max() / 20);
            opponent.temptNoSkillNoSource(c, this, opponent.getArousal().max() / 20);
        }
        if (c.getStance().dom(this)) {
            emote(Emotion.dominant, 20);
            emote(Emotion.confident, 10);
        } else if (c.getStance().sub(this)) {
            emote(Emotion.nervous, 15);
            emote(Emotion.desperate, 10);
        }
        if (opponent.mostlyNude()) {
            emote(Emotion.horny, 15);
            emote(Emotion.confident, 10);
        }
        if (mostlyNude()) {
            emote(Emotion.nervous, 10);
            if (has(Trait.exhibitionist)) {
                emote(Emotion.horny, 20);
            }
        }
        if (opponent.getArousal().percent() >= 75) {
            emote(Emotion.confident, 20);
        }

        if (getArousal().percent() >= 50) {
            emote(Emotion.horny, getArousal().percent() / 4);
        }

        if (getArousal().percent() >= 50) {
            emote(Emotion.desperate, 10);
        }
        if (getArousal().percent() >= 75) {
            emote(Emotion.desperate, 20);
            emote(Emotion.nervous, 10);
        }
        if (getArousal().percent() >= 90) {
            emote(Emotion.desperate, 20);
        }
        if (!canAct()) {
            emote(Emotion.desperate, 10);
        }
        if (!opponent.canAct()) {
            emote(Emotion.dominant, 20);
        }
        moodSwing(c);
    }

    @Override
    public NPC clone() throws CloneNotSupportedException {
        return (NPC) super.clone();
    }

    @Override
    protected void resolveOrgasm(Combat c, Character opponent, BodyPart selfPart, BodyPart opponentPart, int times,
                    int totalTimes) {
        super.resolveOrgasm(c, opponent, selfPart, opponentPart, times, totalTimes);
        ai.resolveOrgasm(c, opponent, selfPart, opponentPart, times, totalTimes);
    }

    @Override
    public String getPortrait(Combat c) {
        Disguised disguised = (Disguised) getStatus(Stsflag.disguised);
        if (disguised != null) {
            return disguised.getTarget().ai.image(c);
        }
        return ai.image(c);
    }

    @Override
    public String getType() {
        return ai.getType();
    }

    public RecruitmentData getRecruitmentData() {
        return ai.getRecruitmentData();
    }

    @Override
    public double dickPreference() {
        return ai instanceof Eve ? 10.0 : super.dickPreference();
    }

    public Optional<String> getComment(Combat c) {
        Set<CommentSituation> applicable = CommentSituation.getApplicableComments(c, this, c.getOpponent(this));
        Set<CommentSituation> forbidden = EnumSet.allOf(CommentSituation.class);
        forbidden.removeAll(applicable);
        Map<CommentSituation, String> comments = ai.getComments(c);
        forbidden.forEach(comments::remove);
        if (comments.isEmpty() || comments.size() == 1 && comments.containsKey(CommentSituation.NO_COMMENT))
            return Optional.empty();
        return Global.pickRandom(new ArrayList<>(comments.values()));
    }
    
    public Emotion moodSwing() {
        Emotion current = mood;
        int max=emotes.get(current);
        for(Emotion e: emotes.keySet()){
            if(emotes.get(e)>max){
                mood=e;
                max=emotes.get(e);
            }
        }
        return mood;
    }
    
    public void decayMood(){
        for(Emotion e: emotes.keySet()){
            if(mostlyNude()&&!has(Trait.exhibitionist)&&!has(Trait.shameless)&& e==Emotion.nervous){
                emotes.put(e, emotes.get(e)-((emotes.get(e)-50)/2));
            }else if(mostlyNude()&&!has(Trait.exhibitionist)&& e==Emotion.horny){
                emotes.put(e, emotes.get(e)-((emotes.get(e)-50)/2));
            }
            else if(!mostlyNude()&&e==Emotion.confident){
                emotes.put(e, emotes.get(e)-((emotes.get(e)-50)/2));
            }
            else{
                if(emotes.get(e)>=5){
                    emotes.put(e, emotes.get(e)-(emotes.get(e)/2));
                }
            }
        }
    }
    
    @Override
    public void upkeep() {
        super.upkeep();
        moodSwing();
        decayMood();
        update();
        notifyObservers();
    }

    public Map<String, List<CharacterLine>> getLines() {
        return lines;
    }

}
