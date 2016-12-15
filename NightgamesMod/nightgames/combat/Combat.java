package nightgames.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.areas.Area;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.NPC;
import nightgames.characters.Player;
import nightgames.characters.State;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.PussyPart;
import nightgames.global.DebugFlags;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;
import nightgames.pet.Pet;
import nightgames.pet.PetCharacter;
import nightgames.skills.Anilingus;
import nightgames.skills.BreastWorship;
import nightgames.skills.CockWorship;
import nightgames.skills.Command;
import nightgames.skills.ConcedePosition;
import nightgames.skills.FootWorship;
import nightgames.skills.PetInitiatedThreesome;
import nightgames.skills.PussyWorship;
import nightgames.skills.Reversal;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.skills.WildThrust;
import nightgames.stance.Kneeling;
import nightgames.stance.Neutral;
import nightgames.stance.Pin;
import nightgames.stance.Position;
import nightgames.stance.Stance;
import nightgames.stance.StandingOver;
import nightgames.status.Abuff;
import nightgames.status.Alluring;
import nightgames.status.BodyFetish;
import nightgames.status.Braced;
import nightgames.status.Collared;
import nightgames.status.CounterStatus;
import nightgames.status.DivineCharge;
import nightgames.status.Enthralled;
import nightgames.status.Falling;
import nightgames.status.Flatfooted;
import nightgames.status.Frenzied;
import nightgames.status.SapphicSeduction;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.status.Stunned;
import nightgames.status.Trance;
import nightgames.status.Wary;
import nightgames.status.Winded;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.Addiction.Severity;
import nightgames.status.addiction.AddictionType;

public class Combat extends Observable implements Cloneable {
    
    private enum CombatPhase {
        PRETURN,
        SKILL_SELECTION,
        PET_ACTIONS,
        DETERMINE_SKILL_ORDER,
        DETERMINE_SKILL_ORDER_AUTONEXT,
        P1_ACT_FIRST,
        P2_ACT_FIRST,
        P1_ACT_SECOND,
        P2_ACT_SECOND,
        UPKEEP,
        RESULTS_SCENE,
        FINISHED,
    }
    public Character p1;
    public Character p2;
    public List<PetCharacter> otherCombatants;
    public Map<String, CombatantData> combatantData;
    public Optional<Character> winner;
    public CombatPhase phase;
    protected Skill p1act;
    protected Skill p2act;
    public Area location;
    private String message;
    private Position stance;
    public Character lastTalked;
    protected int timer;
    public Result state;
    private HashMap<String, String> images;
    boolean lastFailed = false;
    private CombatLog log;
    private boolean beingObserved;
    private int postCombatScenesSeen;
    private boolean wroteMessage;
    private boolean cloned;

    String imagePath = "";

    public Combat(Character p1, Character p2, Area loc) {
        this.p1 = p1;
        combatantData = new HashMap<>();
        this.p2 = p2;
        p1.startBattle(this);
        p2.startBattle(this);
        location = loc;
        stance = new Neutral(p1, p2);
        message = "";
        timer = 0;
        images = new HashMap<String, String>();
        p1.state = State.combat;
        p2.state = State.combat;
        postCombatScenesSeen = 0;
        otherCombatants = new ArrayList<>();
        wroteMessage = false;
        winner = Optional.empty();
        phase = CombatPhase.PRETURN;
        cloned = false;
        if (doExtendedLog()) {
            log = new CombatLog(this);
        }
    }

    public Combat(Character p1, Character p2, Area loc, Position starting) {
        this(p1, p2, loc);
        stance = starting;
    }

    public Combat(Character p1, Character p2, Area loc, int code) {
        this(p1, p2, loc);
        stance = new Neutral(p1, p2);
        message = "";
        timer = 0;
        switch (code) {
            case 1:
                p2.undress(this);
                p1.emote(Emotion.dominant, 50);
                p2.emote(Emotion.nervous, 50);
            default:
        }
        p1.state = State.combat;
        p2.state = State.combat;
    }

    private void applyCombatStatuses(Character self, Character other) {
        if (self.human()) {
            Player p = (Player) self;
            for (Addiction a : p.getAddictions()) {
                if (a.isActive()) {
                    Optional<Status> status = a.startCombat(this, other);
                    if (status.isPresent()) {
                        self.add(this, status.get());
                    }
                }
            }
        } else if (other.human() && self.has(Trait.zealinspiring) && other instanceof Player && ((Player)other).getAddiction(AddictionType.ZEAL)
                        .map(Addiction::isInWithdrawal).orElse(false)) {
            self.add(this, new DivineCharge(self, .3));
        }
        if (self.has(Trait.suave) && !other.hasDick()) {
            self.add(this, new SapphicSeduction(self));
        }

        
        if (self.has(Trait.footfetishist)) {
            applyFetish(self, other, "feet");
        } 
        if(self.has(Trait.breastobsessed) && other.hasBreasts()) {
            applyFetish(self, other, "breasts");
        }
        if(self.has(Trait.assaddict)) {
            applyFetish(self, other, "ass");
        }
        if(self.has(Trait.pussywhipped ) && other.hasPussy()) {
            applyFetish(self, other, "pussy");
        }
        if(self.has(Trait.cockcraver)&& other.hasDick()) {
            applyFetish(self, other, "cock");
        }
        
    }
    
    
    public void applyFetish(Character self, Character other, String FetishType) {
        
        if ( !other.body.get(FetishType).isEmpty() && !self.body.getFetish(FetishType).isPresent()) {
            if (self.human()) {
                write(self, "As your first battle of the night begins, you can't help but think about " + FetishType + ".");
            } 
            self.add(this, new BodyFetish(self, null, FetishType, .25));
        }
    
    }

    public void go() {
        if (p1.mostlyNude() && !p2.mostlyNude()) {
            p1.emote(Emotion.nervous, 20);
        }
        if (p2.mostlyNude() && !p1.mostlyNude()) {
            p2.emote(Emotion.nervous, 20);
        }
        applyCombatStatuses(p1, p2);
        applyCombatStatuses(p2, p1);

        updateMessage();
        if (doExtendedLog()) {
            log.logHeader("\n");
        }
        next();
    }

    public CombatantData getCombatantData(Character character) {
        if (!combatantData.containsKey(character.getName())) {
            combatantData.put(character.getName(), new CombatantData());
        }
        return combatantData.get(character.getName());
    }

    private boolean checkBottleCollection(Character victor, Character loser, PussyPart mod) {
        return victor.has(Item.EmptyBottle, 1) && loser.body.get("pussy")
                                                            .stream()
                                                            .anyMatch(part -> part.getMod(loser) == mod);
    }

    public void doVictory(Character victor, Character loser) {
        if (loser.hasDick() && victor.has(Trait.succubus)) {
            victor.gain(Item.semen, 3);
            if (loser.human()) {
                write(victor, "<br><b>As she leaves, you see all your scattered semen ooze out and gather into a orb in "
                                + victor.nameOrPossessivePronoun() + " hands. "
                                + "She casually drops your seed in some empty vials that appeared out of nowhere</b>");
            } else if (victor.human()) {
                write(victor, "<br><b>" + loser.nameOrPossessivePronoun()
                                + " scattered semen lazily oozes into a few magically conjured flasks. "
                                + "To speed up the process, you milk " + loser.possessivePronoun()
                                + " out of the last drops " + loser.subject()
                                + " had to offer. Yum, you just got some leftovers.</b>");
            }
        } else if (loser.hasDick() && (victor.human() || victor.has(Trait.madscientist))
                        && victor.has(Item.EmptyBottle, 1)) {
            // for now only the player and mara collects semen
            write(victor, Global.format(
                            "<br><b>{self:SUBJECT-ACTION:manage|manages} to collect some of {other:name-possessive} scattered semen in an empty bottle</b>",
                            victor, loser));
            victor.consume(Item.EmptyBottle, 1, false);
            victor.gain(Item.semen, 1);
        }
        if (checkBottleCollection(victor, loser, PussyPart.divine)) {
            write(victor, Global.format(
                            "<br><b>{other:SUBJECT-ACTION:shoot|shoots} {self:name-do} a dirty look as {self:subject-action:move|moves} to collect some of {other:name-possessive} divine pussy juices in an empty bottle</b>",
                            victor, loser));
            victor.consume(Item.EmptyBottle, 1, false);
            victor.gain(Item.HolyWater, 1);
        }
        if (checkBottleCollection(victor, loser, PussyPart.succubus)) {
            write(victor, Global.format(
                            "<br><b>{other:SUBJECT-ACTION:shoot|shoots} {self:name-do} a dirty look as {self:subject-action:move|moves} to collect some of {other:name-possessive} demonic pussy juices in an empty bottle</b>",
                            victor, loser));
            victor.consume(Item.EmptyBottle, 1, false);
            victor.gain(Item.ExtremeAphrodisiac, 1);
        }
        if (checkBottleCollection(victor, loser, PussyPart.plant)) {
            write(victor, Global.format(
                            "<br><b>{other:SUBJECT-ACTION:shoot|shoots} {self:name-do} a dirty look as {self:subject-action:move|moves} to collect some of {other:possessive} nectar in an empty bottle</b>",
                            victor, loser));
            victor.consume(Item.EmptyBottle, 1, false);
            victor.gain(Item.nectar, 3);
        }
        if (checkBottleCollection(victor, loser, PussyPart.cybernetic)) {
            write(victor, Global.format(
                            "<br><b>{other:SUBJECT-ACTION:shoot|shoots} {self:name-do} a dirty look as {self:subject-action:move|moves} to collect some of {other:possessive} artificial lubricant in an empty bottle</b>",
                            victor, loser));
            victor.consume(Item.EmptyBottle, 1, false);
            victor.gain(Item.LubricatingOils, 1);
        }
        if (checkBottleCollection(victor, loser, PussyPart.arcane)) {
            write(victor, Global.format(
                            "<br><b>{other:SUBJECT-ACTION:shoot|shoots} {self:name-do} a dirty look as {self:subject-action:move|moves} to collect some of the floating mana wisps ejected from {other:possessive} orgasm in an empty bottle</b>",
                            victor, loser));
            victor.consume(Item.EmptyBottle, 1, false);
            victor.gain(Item.RawAether, 1);
        }
        if (checkBottleCollection(victor, loser, PussyPart.feral)) {
            write(victor, Global.format(
                            "<br><b>{other:SUBJECT-ACTION:shoot|shoots} {self:name-do} a dirty look as {self:subject-action:move|moves} to collect some of {other:possessive} musky juices in an empty bottle</b>",
                            victor, loser));
            victor.consume(Item.EmptyBottle, 1, false);
            victor.gain(Item.FeralMusk, 1);
        }
        if (checkBottleCollection(victor, loser, PussyPart.gooey)) {
            write(victor, Global.format(
                            "<br><b>{other:SUBJECT-ACTION:shoot|shoots} {self:name-do} a dirty look as {self:subject-action:move|moves} to collect some of {other:possessive} goo in an empty bottle</b>",
                            victor, loser));
            victor.consume(Item.EmptyBottle, 1, false);
            victor.gain(Item.BioGel, 1);
        }
        if (loser.human() && loser.getWillpower().max() < loser.getMaxWillpowerPossible()) {
            write("<br>Ashamed at your loss, you resolve to win next time.");
            write("<br><b>Gained 1 Willpower</b>.");
            loser.getWillpower()
                 .gain(1);
        }
        victor.getWillpower()
              .fill();
        loser.getWillpower()
             .fill();

        if (Global.checkFlag(Flag.FTC) && loser.has(Item.Flag)) {
            write(victor, Global.format(
                            "<br><b>{self:SUBJECT-ACTION:take|takes} the " + "Flag from {other:subject}!</b>", victor,
                            loser));
            loser.remove(Item.Flag);
            victor.gain(Item.Flag);
        }
    }

    private void draw() {
        state = eval();
        p1.evalChallenges(this, null);
        p2.evalChallenges(this, null);
        p2.draw(this, state);
        updateMessage();
        winner = Optional.of(Global.noneCharacter());
    }

    private void victory(Character won) {
        state = eval();
        p1.evalChallenges(this, won);
        p2.evalChallenges(this, won);
        won.victory(this, state);
        doVictory(won, getOpponent(won));
        winner = Optional.of(won);
        updateMessage();
    }

    private boolean checkLosses() {
        if (cloned) {
            return false;
        }
        if (p1.checkLoss(this) && p2.checkLoss(this)) {
            draw();
            return true;
        }
        if (p1.checkLoss(this)) {
            victory(p2);
            return true;
        }
        if (p2.checkLoss(this)) {
            victory(p1);
            return true;
        }
        return false;
    }

    private void checkForCombatComment() {
        Character other;
        if (p1.human() || p2.human()) {
            other = (NPC) getOpponent(Global.getPlayer());
        } else {
            other = (NPC) (Global.random(2) == 0 ? p1 : p2);
        }
        if (other instanceof NPC) {
            NPC commenter = (NPC) other;
            Optional<String> comment = commenter.getComment(this);
            if (comment.isPresent()) {
                write(commenter, "<i>\"" + Global.format(comment.get(), commenter, Global.getPlayer()) + "\"</i>");
            }
        }
    }

    private void doPreturnUpkeep() {
        timer += 1;
        Character player;
        Character other;
        if (p1.human()) {
            player = p1;
            other = p2;
        } else {
            player = p2;
            other = p1;
        }
        message = describe(player, other);
        if (!shouldAutoresolve() && !Global.checkFlag(Flag.noimage)) {
            Global.gui()
                  .clearImage();
            if (!imagePath.isEmpty()) {
                Global.gui()
                      .displayImage(imagePath, images.get(imagePath));
            }
        }
        p1.preturnUpkeep();
        p2.preturnUpkeep();
        p1act = null;
        p2act = null;
        if (Global.random(3) == 0 && !shouldAutoresolve()) {
            checkForCombatComment();
        }
    }

    private void doEndOfTurnUpkeep() {
        p1.eot(this, p2, p2act);
        p2.eot(this, p1, p1act);
        checkStamina(p1);
        checkStamina(p2);
        doStanceTick(p1);
        doStanceTick(p2);

        List<Character> team1 = new ArrayList<>();
        team1.addAll(getPetsFor(p1));
        team1.add(p1);
        List<Character> team2 = new ArrayList<>();
        team2.addAll(getPetsFor(p2));
        team2.add(p2);
        team1.forEach(self -> doAuraTick(self, team1, team2));
        team2.forEach(self -> doAuraTick(self, team2, team1));

        combatantData.values().forEach(data -> data.tick(this));

        getStance().decay(this);
        getStance().checkOngoing(this);
        p1.regen(this);
        p2.regen(this);
    }

    private void doAuraTick(Character character, List<Character> allies, List<Character> opponents) {
        if (character.has(Trait.overwhelmingPresence)) {
            write(character, Global.format("{self:NAME-POSSESSIVE} overwhelming presence mentally exhausts {self:possessive} opponents.", character, character));
            opponents.forEach(opponent -> opponent.weaken(this, opponent.getStamina().max() / 10));
        }
        String beguilingbreastCompletedFlag = Trait.beguilingbreasts.name() + "Completed";
        if (character.has(Trait.beguilingbreasts) && character.body.getLargestBreasts().size >= BreastsPart.dd.size && !getCombatantData(character).getBooleanFlag(beguilingbreastCompletedFlag)
                        && character.outfit.slotOpen(ClothingSlot.top)) {
            Character mainOpponent = getOpponent(character);
            write(character, Global.format("The instant {self:subject-action:lay|lays} {self:possessive} eyes on {other:name-possessive} bare breasts, {self:possessive} consciousness flies out of {self:possessive} mind. " +
                            (character.canAct() ? "{other:SUBJECT-ACTION:giggle|giggles} a bit and cups her stupendous tits and gives them a little squeeze to which {self:subject} can only moan." : ""), 
                            character, mainOpponent));
            opponents.forEach(opponent -> opponent.add(this, new Trance(opponent, 50)));
            getCombatantData(character).setBooleanFlag(beguilingbreastCompletedFlag, true);
        }

        
        if (character.has(Trait.footfetishist)) {
            fetishDisadvantageAura(character, allies, opponents, "feet", ClothingSlot.feet);
        }
        if (character.has(Trait.breastobsessed)) {
            fetishDisadvantageAura(character, allies, opponents, "breasts", ClothingSlot.top);
        }
        if(character.has(Trait.assaddict)) {
            fetishDisadvantageAura(character, allies, opponents, "ass", ClothingSlot.bottom);
        }
        if(character.has(Trait.pussywhipped ) )  {
            fetishDisadvantageAura(character, allies, opponents, "pussy", ClothingSlot.bottom);
        }
        if(character.has(Trait.cockcraver)) {
            fetishDisadvantageAura(character, allies, opponents, "cock", ClothingSlot.bottom);
        }
        
        opponents.forEach(opponent -> checkIndividualAuraEffects(character, opponent));
    }
    
    
    private void fetishDisadvantageAura(Character character, List<Character> allies, List<Character> opponents, String fetishType, ClothingSlot clothingType) {
       
        float ifPartNotNull = 0;
       
        
        if(fetishType == "breasts" && opponents.get(0).hasBreasts()){
            ifPartNotNull = 1;
        } else if(fetishType == "pussy" && opponents.get(0).hasPussy()){
            ifPartNotNull = 1;
        } else if(fetishType == "cock" && opponents.get(0).hasDick()){
            ifPartNotNull = 1;
        } else if(fetishType == "ass" ){
            ifPartNotNull = 1;
        } else if(fetishType == "feet" ){
            ifPartNotNull = 1;
        } else{
            ifPartNotNull = 0;
        }      
        
        if(ifPartNotNull == 1)
        {
            Optional<Character> otherWithAura = opponents.stream().filter(other -> !other.body.get(fetishType).isEmpty()).findFirst();
            Clothing clothes = otherWithAura.get().getOutfit().getTopOfSlot(clothingType);
            boolean seeFetish = clothes == null || clothes.getLayer() <= 1 || otherWithAura.get().getOutfit().getExposure() >= .5;
            String partDescrip;
            
        if(fetishType == "breasts"){
             partDescrip = otherWithAura.get().body.getRandomBreasts().describe(otherWithAura.get()) ;
         } else if(fetishType == "ass"){
             partDescrip = otherWithAura.get().body.getRandomAss().describe(otherWithAura.get()) ;
         } else if(fetishType == "pussy"){
             partDescrip = otherWithAura.get().body.getRandomPussy().describe(otherWithAura.get()) ;
         } else if(fetishType == "cock"){
             partDescrip = otherWithAura.get().body.getRandomCock().describe(otherWithAura.get()) ;
         } else{
             partDescrip = fetishType;
         }
        
            if ( otherWithAura.isPresent() && seeFetish && Global.random(5) == 0) {
                if (character.human()) {
                    write(character, "You can't help thinking about " + otherWithAura.get().nameOrPossessivePronoun() + " " + partDescrip + ".");
                }
                character.add(this, new BodyFetish(character, null, fetishType, .05));
            }
        }
        
       
    
    }
    
    private void checkIndividualAuraEffects(Character self, Character other) {
        if (self.has(Trait.magicEyeEnthrall) && other.getArousal().percent() >= 50 && getStance().facing(other, self)
                        && Global.random(20) == 0) {
            write(self,
                            Global.format("<br>{other:NAME-POSSESSIVE} eyes start glowing and captures both {self:name-possessive} gaze and consciousness.",
                                            other, self));
            other.add(this, new Enthralled(other, self, 2));
        }
        if (self.has(Trait.magicEyeTrance) && other.getArousal().percent() >= 50 && getStance().facing(other, self)
                        && Global.random(10) == 0) {
            write(self,
                            Global.format("<br>{other:NAME-POSSESSIVE} eyes start glowing and send {self:subject} straight into a trance.",
                                            other, self));
            other.add(this, new Trance(other));
        }

        if (self.has(Trait.magicEyeFrenzy) && other.getArousal().percent() >= 50 && getStance().facing(other, self)
                        && Global.random(10) == 0) {
            write(self,
                            Global.format("<br>{other:NAME-POSSESSIVE} eyes start glowing and send {self:subject} into a frenzy.",
                                            other, self));
            other.add(this, new Frenzied(other, 3));
        }

        if (self.has(Trait.magicEyeArousal) && other.getArousal().percent() >= 50 && getStance().facing(other, self)
                        && Global.random(5) == 0) {
            write(self,
                            Global.format("<br>{other:NAME-POSSESSIVE} eyes start glowing and {self:subject-action:feel|feels} a strong pleasure wherever {other:possessive} gaze lands. {self:SUBJECT-ACTION:are|is} literally being raped by {other:name-possessive} eyes!",
                                            other, self));
            other.tempt(this, self, self.get(Attribute.Seduction) / 2);
        }

        if (self.has(Trait.enchantingVoice)) {
            int voiceCount = getCombatantData(self).getIntegerFlag("enchantingvoice-count");
            if (voiceCount >= 1) {
                if (!self.human()) {
                    write(self,
                                    Global.format("{other:SUBJECT} winks at you and verbalizes a few choice words that pass straight through your mental barriers.",
                                                    other, self));
                } else {
                    write(self,
                                    Global.format("Sensing a moment of distraction, you use the power in your voice to force {self:subject} to your will.",
                                                    other, self));
                }
                (new Command(self)).resolve(this, other);
                int cooldown = Math.max(1, 6 - (self.getLevel() - other.getLevel() / 5));
                getCombatantData(self).setIntegerFlag("enchantingvoice-count", -cooldown);
            } else {
                getCombatantData(self).setIntegerFlag("enchantingvoice-count", voiceCount + 1);
            }
        }
    }

    private static final List<CombatPhase> SKIPPABLE_PHASES = 
                    Arrays.asList(
                    CombatPhase.PET_ACTIONS,
                    CombatPhase.P1_ACT_FIRST,
                    CombatPhase.P1_ACT_SECOND,
                    CombatPhase.P2_ACT_FIRST,
                    CombatPhase.P1_ACT_SECOND);
    public void turn() {
        if (phase != CombatPhase.FINISHED && phase != CombatPhase.RESULTS_SCENE && checkLosses()) {
            phase = CombatPhase.RESULTS_SCENE;
            next();
            return;
        }
        if ((p1.orgasmed || p2.orgasmed) && phase != CombatPhase.RESULTS_SCENE && SKIPPABLE_PHASES.contains(phase)) {
            phase = CombatPhase.UPKEEP;
        }
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.println("Current phase = " + phase);
        }
        wroteMessage = false;
        switch (phase) {
            case PRETURN:
                doPreturnUpkeep();
                phase = CombatPhase.SKILL_SELECTION;
                turn();
                break;
            case SKILL_SELECTION:
                pickSkills();
                break;
            case PET_ACTIONS:
                phase = doPetActions();
                turn();
                break;
            case DETERMINE_SKILL_ORDER:
                phase = determineSkillOrder();
                next();
                break;
            case DETERMINE_SKILL_ORDER_AUTONEXT:
                phase = determineSkillOrder();
                turn();
                break;
            case P1_ACT_FIRST:
                if (doAction(p1, p1act.getDefaultTarget(this), p1act)) {
                    phase = CombatPhase.UPKEEP;
                } else {
                    phase = CombatPhase.P2_ACT_SECOND;
                }
                next();
                break;
            case P1_ACT_SECOND:
                doAction(p1, p1act.getDefaultTarget(this), p1act);
                phase = CombatPhase.UPKEEP;
                next();
                break;
            case P2_ACT_FIRST:
                if (doAction(p2, p2act.getDefaultTarget(this), p2act)) {
                    phase = CombatPhase.UPKEEP;
                } else {
                    phase = CombatPhase.P1_ACT_SECOND;
                }
                next();
                break;
            case P2_ACT_SECOND:
                doAction(p2, p2act.getDefaultTarget(this), p2act);
                phase = CombatPhase.UPKEEP;
                next();
                break;
            case UPKEEP:
                doEndOfTurnUpkeep();
                phase = CombatPhase.PRETURN;
                if (wroteMessage) {
                    next();
                } else {
                    turn();
                }
                break;
            case RESULTS_SCENE:
                // fall through to the finished case.
                phase = CombatPhase.FINISHED;
            case FINISHED:
            default:
                next();
                return;

        }
        updateAndClearMessage();
    }

    private void pickSkills() {
        if (p1act == null) {
            p1.act(this);
        } else if (p2act == null) {
            p2.act(this);
        } else {
            phase = CombatPhase.PET_ACTIONS;
            turn();
        }
    }

    private String describe(Character player, Character other) {
        if (beingObserved) {
            return Global.capitalizeFirstLetter(getStance().describe(this)) + "<p>"
                            + player.describe(player.get(Attribute.Perception), this) + "<p>"
                            + other.describe(player.get(Attribute.Perception), this) + "<p>";
        } else if (!player.is(Stsflag.blinded)) {
            return other.describe(player.get(Attribute.Perception), this) + "<p>"
                            + Global.capitalizeFirstLetter(getStance().describe(this)) + "<p>"
                            + player.describe(other.get(Attribute.Perception), this) + "<p>";
        } else {
            return "<b>You are blinded, and cannot see what " + other.name() + " is doing!</b><p>"
                            + Global.capitalizeFirstLetter(getStance().describe(this)) + "<p>"
                            + player.describe(other.get(Attribute.Perception), this) + "<p>";
        }
    }

    protected Result eval() {
        if (getStance().bottom.human() && getStance().inserted(getStance().top) && getStance().en == Stance.anal) {
            return Result.anal;
        } else if (getStance().inserted()) {
            return Result.intercourse;
        } else {
            return Result.normal;
        }
    }

    public static List<Skill> WORSHIP_SKILLS = Arrays.asList(new BreastWorship(null), new CockWorship(null), new FootWorship(null),
                    new PussyWorship(null), new Anilingus(null));
    public static final String TEMPT_WORSHIP_BONUS = "TEMPT_WORSHIP_BONUS";
    public boolean combatMessageChanged;

    public Optional<Skill> getRandomWorshipSkill(Character self, Character other) {
        List<Skill> avail = new ArrayList<Skill>(WORSHIP_SKILLS);
        if (other.has(Trait.piety)) {
            avail.add(new ConcedePosition(self));
        }
        Collections.shuffle(avail);
        while (!avail.isEmpty()) {
            Skill skill = avail.remove(avail.size() - 1)
                               .copy(self);
            if (Skill.skillIsUsable(this, skill, other)) {
                write(other, Global.format(
                                "<b>{other:NAME-POSSESSIVE} divine aura forces {self:subject} to forget what {self:pronoun} {self:action:were|was} doing and crawl to {other:direct-object} on {self:possessive} knees.</b>",
                                self, other));
                return Optional.of(skill);
            }
        }
        return Optional.ofNullable(null);
    }

    private boolean rollWorship(Character self, Character other) {
        if (other.has(Trait.objectOfWorship) && (other.breastsAvailable() || other.crotchAvailable())) {
            double chance = Math.min(20, Math.max(5, other.get(Attribute.Divinity) + 10 - self.getLevel()));
            if (other.has(Trait.revered)) {
                chance += 10;
            }
            chance += getCombatantData(self).getDoubleFlag(TEMPT_WORSHIP_BONUS);
            if (Global.random(100) < chance) {
                getCombatantData(self).setDoubleFlag(TEMPT_WORSHIP_BONUS, 0);
                return true;
            }            
        }
        return false;
    }

    private Skill checkWorship(Character self, Character other, Skill def) {
        if (rollWorship(self, other)) {
            return getRandomWorshipSkill(self, other).orElse(def);
        }
        return def;
    }

    public boolean doAction(Character self, Character target, Skill action) {
        if (!shouldAutoresolve()) {
            Global.gui().clearText();
        }

        action = checkWorship(self, target, action);
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.println(self.name() + " uses " + action.getLabel(this));
        }
        /*
         * TODO fix this so it works with the new combat system
        if (doExtendedLog()) {
            log.logTurn(p1act, p2act);
        } else if (Global.isDebugOn(DebugFlags.DEBUG_SPECTATE) && beingObserved) {
            write("<br/>");
            write(log.logTurnToString(p1act, p2act, "<br>"));
        } else {
            useSkills();
        }
        */
        boolean results = resolveSkill(action, target);
        this.write("<br/>");
        updateMessage();
        return results;
    }

    public void act(Character c, Skill action, String choice) {
        if (c == p1) {
            p1act = action;
        }
        if (c == p2) {
            p2act = action;
        }
        action.choice = choice;
        turn();
    }

    private CombatPhase doPetActions() {
        Set<PetCharacter> alreadyBattled = new HashSet<>();
        if (otherCombatants.size() > 0) {
            for (PetCharacter pet : otherCombatants) {
                if (alreadyBattled.contains(pet)) { continue; }
                for (PetCharacter otherPet : otherCombatants) {
                    if (alreadyBattled.contains(otherPet)) { continue; }
                    if (!pet.getSelf().owner().equals(otherPet.getSelf().owner()) && Global.random(2) == 0) {
                        petbattle(pet.getSelf(), otherPet.getSelf());
                        alreadyBattled.add(pet);
                        alreadyBattled.add(otherPet);
                    }
                }
            }

            List<PetCharacter> actingPets = new ArrayList<>(otherCombatants);
            actingPets.stream().filter(pet -> !alreadyBattled.contains(pet)).forEach(pet -> {
                pet.act(this);
                if (pet.getSelf().owner().has(Trait.devoteeFervor) && Global.random(2) == 0) {
                    write(pet, Global.format("{self:SUBJECT} seems to have gained a second wind from {self:possessive} religious fervor!", pet, pet.getSelf().owner()));
                    pet.act(this);
                }
            });
            write("<br/>");
            return CombatPhase.DETERMINE_SKILL_ORDER;
        }
        return CombatPhase.DETERMINE_SKILL_ORDER_AUTONEXT;
    }

    private void doStanceTick(Character self) {
        int stanceDominance = getStance().getDominanceOfStance(self);
        if (!(stanceDominance > 0)) {
            return;
        }

        Character other = getStance().getPartner(this, self);
        if (other.human() && other instanceof Player) {
            Addiction add = ((Player)other).getAddiction(AddictionType.DOMINANCE).orElse(null);
            if (add != null && add.atLeast(Severity.MED) && !add.wasCausedBy(self)) {
                write(self, Global.format("{self:name} does {self:possessive} best to be dominant, but with the "
                            + "way Jewel has been working you over you're completely desensitized." , self, other));
                return;
            }
        }
        
        if (self.has(Trait.smqueen)) {
                write(self,
                            Global.format("{self:NAME-POSSESSIVE} cold gaze in {self:possessive} dominant position"
                                            + " makes {other:direct-object} shiver.",
                                            self, other));
            other.loseWillpower(this, stanceDominance, 0, false, " (SM Queen)");
        } else if (getStance().time % 2 == 0 && getStance().time > 0) {
            if (other.has(Trait.indomitable)) {
                write(self, Global.format("{other:SUBJECT}, typically being the dominant one,"
                                + "{other:action:are|is} simply refusing to acknowledge {self:name-possessive}"
                                + " current dominance.", self, other));
                stanceDominance = Math.max(1, stanceDominance - 3);
            } else {
                write(self, Global.format("{other:NAME-POSSESSIVE} compromising position takes a toll on {other:possessive} willpower.",
                                            self, other));
            }
            other.loseWillpower(this, stanceDominance, 0, false, " (Dominance)");
        }
        
        if (self.has(Trait.confidentdom) && Global.random(2) == 0) {
            Attribute attr;
            String desc;
            if (self.get(Attribute.Ki) > 0 && Global.random(2) == 0) {
                attr = Attribute.Ki;
                desc = "strengthening {self:possessive} focus on martial discipline";
            } else if (Global.random(2) == 0) {
                attr = Attribute.Power;
                desc = "further empowering {self:possessive} muscles";
            } else {
                attr = Attribute.Cunning;
                desc = "granting {self:direct-object} increased mental clarity";
            }
            write(self, Global.format("{self:SUBJECT-ACTION:feel|feels} right at home atop"
                            + " {other:name-do}, %s.", self, other, desc));
            self.add(this, new Abuff(self, attr, Global.random(3) + 1, 10));
        }
        
        if (self.has(Trait.unquestionable) && Global.random(4) == 0) {
            write(self, Global.format("<b><i>\"Stay still, worm!\"</i> {self:subject-action:speak|speaks}"
                            + " with such force that it casues {other:name-do} to temporarily"
                            + " cease resisting.</b>", self, other));
            other.add(this, new Flatfooted(other, 1, false));
        }
        
        if (self.is(Stsflag.collared) && Global.random(10) < 3 && new Reversal(other).usable(this, self)) {
            self.pain(this, null, Global.random(20, 50));
            Position nw = stance.reverse(this, false);
            if (!stance.equals(nw)) {
                stance = nw;
                write(Global.format("Appearantly punishing {self:name-do} for being dominant, the collar"
                                + " around {self:possessive} neck gives {self:direct-object} a painful"
                                + " shock. At the same time, {other:subject-action:grab|grabs}"
                                + " hold of {self:possessive} body and gets {other:reflective}"
                                + " into a more advantegeous position.", self, other));
            } else {
                stance = new Pin(other, self);
                write(Global.format("Distracted by a shock from the collar around {self:possessive}"
                                + " neck, {self:subject-action:have|has} no chance to resist as"
                                + " {other:subject-action:put|puts} {self:direct-object}"
                                + " in a pin.", self, other));
            }
        }
        
        if (getStance().facing(self, other) && other.breastsAvailable() && !self.has(Trait.temptingtits) && other.has(Trait.temptingtits)) {
            write(self, Global.format("{self:SUBJECT-ACTION:can't avert|can't avert} {self:possessive} eyes from {other:NAME-POSSESSIVE} perfectly shaped tits sitting in front of {self:possessive} eyes.",
                                            self, other));
            self.tempt(this, other, other.body.getRandomBreasts(), 10 + Math.max(0, other.get(Attribute.Seduction) / 3 - 7));
        } else if (getOpponent(self).has(Trait.temptingtits) && getStance().behind(other)) {
            write(self, Global.format("{self:SUBJECT-ACTION:feel|feels} a heat in {self:possessive} groin as {other:name-possessive} enticing tits pressing against {self:possessive} back.",
                            self, other));
            double selfTopExposure = self.outfit.getExposure(ClothingSlot.top);
            double otherTopExposure = other.outfit.getExposure(ClothingSlot.top);
            double temptDamage = 20 + Math.max(0, other.get(Attribute.Seduction) / 2 - 12);
            temptDamage = temptDamage * Math.min(1, selfTopExposure + .5) * Math.min(1, otherTopExposure + .5);
            self.tempt(this, other, other.body.getRandomBreasts(), (int) temptDamage);
        }
    }

    private boolean checkCounter(Character attacker, Character target, Skill skill) {
        return !target.has(Trait.submissive) && getStance().mobile(target)
                        && target.counterChance(this, attacker, skill) > Global.random(100);
    }

    private boolean resolveCrossCounter(Skill skill, Character target, int chance) {
        if (target.has(Trait.CrossCounter) && Global.random(100) < chance) {
            if (!target.human()) {
                write(target, Global.format("As {other:SUBJECT-ACTION:move|moves} to counter, {self:subject-action:seem|seems} to disappear from {other:possessive} line of sight. "
                                + "A split second later, {other:pronoun-action:are|is} lying on the ground with a grinning {self:name-do} standing over {other:direct-object}. "
                                + "How did {self:pronoun} do that!?", skill.user(), target));
            } else {
                write(target, Global.format("As {other:subject} moves to counter your assault, you press {other:possessive} arms down with your weight and leverage {other:possessive} "
                                + "forward motion to trip {other:direct-object}, sending the poor {other:girl} crashing onto the floor.", skill.user(), target));
            }
            skill.user().add(this, new Falling(skill.user()));
            return true;
        }
        return false;
    }

    boolean resolveSkill(Skill skill, Character target) {
        boolean orgasmed = false;
        if (Skill.skillIsUsable(this, skill, target)) {
            boolean success;
            if (!target.human() || !target.is(Stsflag.blinded)) {
                write(skill.user()
                           .subjectAction("use ", "uses ") + skill.getLabel(this) + ".");
            }
            if (skill.makesContact() && !getStance().dom(target) && target.canAct()
                            && checkCounter(skill.user(), target, skill)) {
                write("Countered!");
                if (!resolveCrossCounter(skill, target, 25)) {
                    target.counterattack(skill.user(), skill.type(this), this);
                }
                success = false;
            } else if (target.is(Stsflag.counter) && skill.makesContact()) {
                write("Countered!");
                if (!resolveCrossCounter(skill, target, 50)) {
                    CounterStatus s = (CounterStatus) target.getStatus(Stsflag.counter);
                    if (skill.user()
                             .is(Stsflag.wary)) {
                        write(target, s.getCounterSkill()
                                       .getBlockedString(this, skill.user()));
                    } else {
                        s.resolveSkill(this, skill.user());
                    }
                }
                success = false;
            } else {
                success = Skill.resolve(skill, this, target);
            }
            if (success) {
                if (skill.getTags(this).contains(SkillTag.thrusting) && skill.user().has(Trait.Jackhammer) && Global.random(2) == 0) {
                    write(skill.user(), Global.format("{self:NAME-POSSESSIVE} hips don't stop as {self:pronoun-action:continue|continues} to fuck {other:direct-object}.", skill.user(), target));
                    Skill.resolve(new WildThrust(skill.user()), this, target);
                }
                if (skill.getTags(this).contains(SkillTag.thrusting) && skill.user().has(Trait.Piledriver) && Global.random(3) == 0) {
                    write(skill.user(), Global.format("{self:SUBJECT-ACTION:fuck|fucks} {other:name-do} <b>hard</b>, so much so that {other:pronoun-action:are|is} momentarily floored by the stimulation.", skill.user(), target));
                    target.add(this, new Stunned(target, 1, false));
                }
                if (skill.type(this) == Tactics.damage && skill.user().is(Stsflag.collared)) {
                    Collared stat = (Collared) skill.user().getStatus(Stsflag.collared);
                    stat.spendCharges(this, 1);
                    write(Global.format("The training collar around {self:name-possessive}"
                                    + "neck reacts to {self:possessive} aggression by sending"
                                    + " a powerful shock down {self:possessive} spine.", 
                                    skill.user(), target));
                    skill.user().pain(this, null, Global.random(10, 40));
                }
            }
            checkStamina(target);
            checkStamina(skill.user());
            orgasmed = checkOrgasm(skill.user(), target, skill);
            lastFailed = false;
        } else {
            write(skill.user()
                       .possessivePronoun() + " " + skill.getLabel(this) + " failed.");
            lastFailed = true;
        }
        return orgasmed;
    }

    private boolean checkOrgasm(Character user, Character target, Skill skill) {
        return target.orgasmed || user.orgasmed;
    }

    protected CombatPhase determineSkillOrder() {
        if (p1.init() + p1act.speed() >= p2.init() + p2act.speed()) {
            return CombatPhase.P1_ACT_FIRST;
        } else {
            return CombatPhase.P2_ACT_FIRST;
        }
    }

    public void clear() {
        message = "";
        updateMessage();
    }

    public void write(String text) {
        text = Global.capitalizeFirstLetter(text);
        if (text.isEmpty()) {
            return;
        }
        String added = message + "<br>" + text;
        message = added;
        wroteMessage = true;
        lastTalked = null;
    }

    public void updateMessage() {
        combatMessageChanged = true;
        setChanged();
        this.notifyObservers();
        setChanged();
    }

    public void updateAndClearMessage() {
        Global.gui()
              .clearText();
        combatMessageChanged = true;
        setChanged();
        this.notifyObservers();
    }

    public void write(Character user, String text) {
        text = Global.capitalizeFirstLetter(text);
        if (text.length() > 0) {
            if (user.human()) {
                message = message + "<br><font color='rgb(200,200,255)'>" + text + "<font color='white'>";
            } else if (user.isPet() && user.isPetOf(Global.getPlayer())) {
                message = message + "<br><font color='rgb(130,225,200)'>" + text + "<font color='white'>";
            } else if (user.isPet()) {
                message = message + "<br><font color='rgb(210,130,255)'>" + text + "<font color='white'>";
            } else {
                message = message + "<br><font color='rgb(255,200,200)'>" + text + "<font color='white'>";
            }
            lastTalked = user;
        }
        wroteMessage = true;
    }

    public String getMessage() {
        return message;
    }

    public String debugMessage() {
        return "Stance: " + getStance().getClass()
                                       .getName()
                        + "\np1: " + p1.debugMessage(this, getStance()) + "\np2: " + p2.debugMessage(this, getStance());
    }

    public void checkStamina(Character p) {
        if (p.getStamina()
             .isEmpty() && !p.is(Stsflag.stunned)) {
            p.add(this, new Winded(p, 3));
            Character other;
            if (p == p1) {
                other = p2;
            } else {
                other = p1;
            }
            if (!getStance().prone(p)) {
                if (getStance().inserted() && getStance().dom(other)) {
                    if (p.human()) {
                        write("Your legs give out, but " + other.name() + " holds you up.");
                    } else {
                        write(String.format("%s slumps in %s arms, but %s %s %s to keep %s from collapsing.",
                                        p.subject(), other.nameOrPossessivePronoun(),
                                        other.pronoun(), other.action("support"), p.directObject(),
                                        p.directObject()));
                    }
                } else {
                    setStance(new StandingOver(other, p), null, false);
                    if (p.human()) {
                        write("You don't have the strength to stay on your feet. You slump to the floor.");
                    } else {
                        write(p.name() + " drops to the floor, exhausted.");
                    }
                }
                p.loseWillpower(this, Math.min(p.getWillpower()
                                                .max()
                                / 8, 15), true);
            }
            if (p.human() && p instanceof Player && other.has(Trait.dominatrix)) {
                if (((Player)p).hasAddiction(AddictionType.DOMINANCE)) {
                    write(String.format("Being dominated by %s again reinforces your"
                                    + " submissiveness towards %s.", other.getName(),
                                    other.directObject()));
                } else {
                    write(String.format("There's something about the way %s knows just"
                                    + " how and where to hurt you which some part of your"
                                    + " psyche finds strangely appealing. You find yourself"
                                    + " wanting more.", other.getName()));
                }
                ((Player)p).addict(AddictionType.DOMINANCE, other, Addiction.HIGH_INCREASE);
            }
        }
    }

    private void next() {
        if (phase != CombatPhase.FINISHED) {
            if (shouldAutoresolve() || (Global.checkFlag(Flag.AutoNext) && phase != CombatPhase.SKILL_SELECTION && phase != CombatPhase.RESULTS_SCENE && phase != CombatPhase.PRETURN)) {
                turn();
            } else {
                Global.gui().next(this);
            }
        } else {
            end();
        }
    }

    public void intervene(Character intruder, Character assist) {
        Character target;
        if (p1 == assist) {
            target = p2;
        } else {
            target = p1;
        }
        if (target.resist3p(this, intruder, assist)) {
            target.gainXP(20 + target.lvlBonus(intruder));
            intruder.gainXP(10 + intruder.lvlBonus(target));
            intruder.getArousal()
                    .empty();
            if (intruder.has(Trait.insatiable)) {
                intruder.getArousal()
                        .restore((int) (intruder.getArousal()
                                                .max()
                                        * 0.2D));
            }
            target.undress(this);
            intruder.defeated(target);
            intruder.defeated(assist);
        } else {
            intruder.intervene3p(this, target, assist);
            assist.victory3p(this, target, intruder);
            phase = CombatPhase.RESULTS_SCENE;
            if (!(p1.human() || p2.human() || intruder.human())) {
                end();
            } else {
                Global.gui().watchCombat(this);
                next();
            }
        }
        updateMessage();
    }

    /**
     * @return true if it should end the fight, false if there are still more scenes
     */
    public void end() {
        clear();
        boolean hasScene = false;
        if (p1.human() || p2.human()) {
            if (postCombatScenesSeen < 3) {
                if (!p2.human() && p2 instanceof NPC) {
                    hasScene = doPostCombatScenes((NPC)p2);
                } else if (!p1.human() && p1 instanceof NPC) {
                    hasScene = doPostCombatScenes((NPC)p1);
                }
                if (hasScene) {
                    postCombatScenesSeen += 1;
                    return;
                }
            } else {
                Global.gui().next(this);
            }
        }

        p1.state = State.ready;
        p2.state = State.ready;
        p1.endofbattle();
        p2.endofbattle();
        getCombatantData(p1).getRemovedItems().forEach(p1::gain);
        getCombatantData(p2).getRemovedItems().forEach(p2::gain);
        location.endEncounter();
        // it's a little ugly, but we must be mindful of lazy evaluation
        boolean ding = p1.levelUpIfPossible() && p1.human();
        ding = (p2.levelUpIfPossible() && p2.human()) || ding;
        if (doExtendedLog()) {
            log.logEnd(winner);
        }
        if (!ding && !shouldAutoresolve()) {
            Global.gui().endCombat();
        }
    }

    private boolean doPostCombatScenes(NPC npc) {
        List<CombatScene> availableScenes = npc.getPostCombatScenes()
                        .stream()
                        .filter(scene -> scene.meetsRequirements(this, npc))
                        .collect(Collectors.toList());
        Optional<CombatScene> possibleScene = Global.pickRandom(availableScenes);
        if (possibleScene.isPresent()) {
            Global.gui().clearText();
            Global.gui().clearCommand();
            possibleScene.get().visit(this, npc);
            return true;
        } else {
            return false;
        }
    }

    public void petbattle(Pet one, Pet two) {
        int roll1 = Global.random(20) + one.power();
        int roll2 = Global.random(20) + two.power();
        if (one.hasPussy() && two.hasDick()) {
            roll1 += 3;
        } else if (one.hasDick() && two.hasPussy()) {
            roll2 += 3;
        }
        if (roll1 > roll2) {
            one.vanquish(this, two);
        } else if (roll2 > roll1) {
            two.vanquish(this, one);
        } else {
            write(one.getName() + " and " + two.getName()
                            + " engage each other for awhile, but neither can gain the upper hand.");
        }
    }

    @Override
    public Combat clone() throws CloneNotSupportedException {
        Combat c = (Combat) super.clone();
        c.p1 = p1.clone();
        c.p2 = p2.clone();
        c.p1.finishClone(c.p2);
        c.p2.finishClone(c.p1);
        c.combatantData = new HashMap<>();
        combatantData.forEach((name, data) -> c.combatantData.put(name, (CombatantData) data.clone()));
        c.stance = getStance().clone();
        c.state = state;
        if (c.getStance().top == p1) {
            c.getStance().top = c.p1;
        }
        if (c.getStance().top == p2) {
            c.getStance().top = c.p2;
        }
        if (c.getStance().bottom == p1) {
            c.getStance().bottom = c.p1;
        }
        if (c.getStance().bottom == p2) {
            c.getStance().bottom = c.p2;
        }
        c.otherCombatants = new ArrayList<>();
        for (PetCharacter pet : otherCombatants) {
            if (pet.isPetOf(p1)) {
                c.otherCombatants.add(pet.cloneWithOwner(c.p1));
            } else if (pet.isPetOf(p2)) {
                c.otherCombatants.add(pet.cloneWithOwner(c.p2));
            }
        }
        c.getStance().setOtherCombatants(c.otherCombatants);
        c.postCombatScenesSeen = this.postCombatScenesSeen;
        c.cloned = true;
        return c;
    }

    public Skill lastact(Character user) {
        if (user == p1) {
            return p1act;
        } else if (user == p2) {
            return p2act;
        } else {
            return null;
        }
    }

    public void offerImage(String path, String artist) {
        imagePath = path;
        if (!imagePath.isEmpty() && !cloned && isBeingObserved()) {
            Global.gui()
                  .displayImage(imagePath, images.get(imagePath));
        }
    }

    public void forfeit(Character player) {
        end();
    }

    public Position getStance() {
        return stance;
    }

    public void checkStanceStatus(Character c, Position oldStance, Position newStance) {
        if ((oldStance.prone(c) || !oldStance.mobile(c)) && !newStance.prone(c) && newStance.mobile(c)) {
            c.add(this, new Braced(c));
            c.add(this, new Wary(c, 3));
        } else if (!oldStance.mobile(c) && newStance.mobile(c)) {
            c.add(this, new Wary(c, 3));
        }
    }

    public void setStance(Position newStance) {
        setStance(newStance, null, true);
    }

    public void setStance(Position newStance, Character initiator, boolean voluntary) {
        if ((newStance.top != getStance().bottom && newStance.top != getStance().top) || (newStance.bottom != getStance().bottom && newStance.bottom != getStance().top)) {
            if (initiator != null && initiator.isPet() && newStance.top == initiator) {
                PetInitiatedThreesome threesomeSkill = new PetInitiatedThreesome(initiator);
                if (newStance.havingSex(this)) {
                    threesomeSkill.resolve(this, newStance.bottom);
                } else if (!getStance().sub(newStance.bottom)) {
                    write(initiator, Global.format("{self:SUBJECT-ACTION:take|takes} the chance to send {other:name-do} sprawling to the ground", initiator, newStance.bottom));
                    newStance.bottom.add(this, new Falling(newStance.bottom));
                    return;
                }
            } else {
                if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
                    System.out.printf("Tried to chance stance without both players, stopping: %s -> %s\n",
                                    stance.getClass().getName(),
                                    newStance.getClass().getName());
                    Thread.dumpStack();
                }
                return;
            }
        }
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.printf("Stance Change: %s -> %s\n", stance.getClass()
                                                                 .getName(),
                            newStance.getClass()
                                     .getName());
        }
        if (initiator != null) {
            Character otherCharacter = getOpponent(initiator);
            if (voluntary && newStance.en == Stance.neutral && getStance().en != Stance.kneeling && otherCharacter.has(Trait.genuflection) && rollWorship(initiator, otherCharacter)) {
                write(initiator, Global.format("While trying to get back up, {self:name-possessive} eyes accidentally met {other:name-possessive} gaze. "
                                + "Like a deer in headlights, {self:possessive} body involuntarily stops moving and kneels down before {other:direct-object}.", initiator, otherCharacter));
                newStance = new Kneeling(otherCharacter, initiator);
            }
        }
        checkStanceStatus(p1, stance, newStance);
        checkStanceStatus(p2, stance, newStance);

        if (stance.inserted() && !newStance.inserted()) {
            List<BodyPart> parts1 = stance.partsFor(this, p1);
            List<BodyPart> parts2 = stance.partsFor(this, p2);
            parts1.forEach(part -> parts2.forEach(other -> part.onEndPenetration(this, p1, p2, other)));
            parts2.forEach(part -> parts1.forEach(other -> part.onEndPenetration(this, p2, p1, other)));
            getCombatantData(p1).setIntegerFlag("ChoseToFuck", 0);
            getCombatantData(p2).setIntegerFlag("ChoseToFuck", 0);
        } else if (!stance.inserted() && newStance.inserted() && (p1.human() || p2.human())) {
            Player player = null;
            if (p1.human() && p1 instanceof Player) {
                player = (Player) p1;
            } else if (p2.human() && p2 instanceof Player) {
                player = (Player) p2;
            }
            if (player != null) {
                Character opp = getOpponent(player);
                List<BodyPart> parts1 = newStance.partsFor(this, p1);
                List<BodyPart> parts2 = newStance.partsFor(this, p2);
                parts1.forEach(part -> parts2.forEach(other -> part.onStartPenetration(this, p1, p2, other)));
                parts2.forEach(part -> parts1.forEach(other -> part.onStartPenetration(this, p2, p1, other)));
                if (voluntary) {
                    if (initiator != null) {
                        getCombatantData(initiator).setIntegerFlag("ChoseToFuck", 1);
                        getCombatantData(getOpponent(initiator)).setIntegerFlag("ChoseToFuck", -1);
                    }
                    if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
                        System.out.println(initiator + " initiated penetration, voluntary=" + voluntary);
                    }
                }
                if (player.checkAddiction(AddictionType.BREEDER, opp)) {
                    if (voluntary) {
                        write(player, "As you enter Kat, instinct immediately kicks in. It just"
                                        + " feels so right, like this is what you're supposed"
                                        + " to be doing all the time.");
                        player.addict(AddictionType.BREEDER, opp, Addiction.MED_INCREASE);
                    } else {
                        write(initiator, "Something shifts inside of you as Kat fills herself with"
                                        + " you. A haze descends over your mind, clouding all but a desire"
                                        + " to fuck her as hard as you can.");
                        player.addict(AddictionType.BREEDER, opp, Addiction.LOW_INCREASE);
                    }
                }
            }
        }

        if (stance != newStance && initiator != null && initiator.has(Trait.Catwalk)) {
            write(initiator, Global.format("The way {self:subject-action:move|moves} exudes such feline grace that it demands {other:name-possessive} attention.",
                            initiator, getOpponent(initiator)));
            initiator.add(this, new Alluring(initiator, 1));
        }

        stance = newStance;
        offerImage(stance.image(), "");
    }

    public Character getOpponent(Character self) {
        if (self.equals(p1) || self.isPetOf(p1)) {
            return p2;
        }
        if (self.equals(p2) || self.isPetOf(p2)) {
            return p1;
        }
        System.err.println("Tried  to get an opponent for " + self.getName() + " which does not exist in combat.");
        return Global.noneCharacter();
    }

    public void writeSystemMessage(String battleString) {
        if (Global.checkFlag(Flag.systemMessages)) {
            write(battleString);
        }
    }

    public void writeSystemMessage(Character character, String string) {
        if (Global.checkFlag(Flag.systemMessages)) {
            write(character, string);
        }
    }

    public int getTimer() {
        return timer;
    }

    private boolean doExtendedLog() {
        return (p1.human() || p2.human()) && Global.checkFlag(Flag.extendedLogs);
    }

    public boolean isBeingObserved() {
        return beingObserved;
    }

    public void setBeingObserved(boolean beingObserved) {
        this.beingObserved = beingObserved;
        if (beingObserved && log == null && Global.isDebugOn(DebugFlags.DEBUG_SPECTATE)) {
            log = new CombatLog(this);
        }
    }
    
    public boolean shouldPrintReceive(Character ch, Combat c) {
        return beingObserved || (c.p1.human() || c.p2.human());
    }
    
    public boolean shouldAutoresolve() {
        return !(p1.human() || p2.human()) && !beingObserved;
    }

    public String bothDirectObject(Character target) {
        return target.human() ? "you" : "them";
    }
    
    public String bothPossessive(Character target) {
        return target.human() ? "your" :  "their";
    }
    
    public String bothSubject(Character target) {
        return target.human() ? "you" : "they";
    }

    public List<PetCharacter> getPetsFor(Character target) {
        return otherCombatants.stream().filter(c -> c.isPetOf(target)).collect(Collectors.toList());
    }

    public void removePet(PetCharacter self) {
        if (self.has(Trait.resurrection) && !getCombatantData(self).getBooleanFlag("resurrected")) {
            write(self, "Just as " + self.subject() + " was about to disappear, a dazzling light covers " 
            + self.possessivePronoun() + " body. When the light fades, " + self.pronoun() + " looks completely refreshed!");
            getCombatantData(self).setBooleanFlag("resurrected", true);
            self.getArousal().empty();
            self.getMojo().empty();
            self.getWillpower().fill();
            self.getStamina().fill();
            return;
        }
        getCombatantData(self).setBooleanFlag("resurrected", false);
        otherCombatants.remove(self);
    }

    public void addPet(Character master, PetCharacter self) {
        if (master.has(Trait.leadership)) {
            int levelups = Math.max(5, master.getLevel() / 4);
            self.getSelf().setPower(self.getSelf().getPower() + levelups);
            for (int i = 0; i < levelups; i++) {
                self.ding();
            }
        }
        if (master.has(Trait.tactician)) {
            self.getSelf().setAc(self.getSelf().getAc() + 3);
            self.getArousal().setMax(self.getArousal().trueMax() * 1.5f);
            self.getStamina().setMax(self.getStamina().trueMax() * 1.5f);
        }
        self.getArousal().empty();
        self.getStamina().fill();
        writeSystemMessage(self, Global.format("{self:SUBJECT-ACTION:have|has} summoned {other:name-do} (Level %s)",
                                        master, self, self.getLevel()));
        otherCombatants.add(self);
        this.write(self, self.challenge(getOpponent(self)));
    }

    public List<PetCharacter> getOtherCombatants() {
        return otherCombatants;
    }

    public boolean isEnded() {
        return phase == CombatPhase.FINISHED;
    }
}
