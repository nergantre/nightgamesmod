package nightgames.characters;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nightgames.actions.Move;
import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.areas.NinjaStash;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TentaclePart;
import nightgames.characters.custom.AiModifiers;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.combat.Result;
import nightgames.ftc.FTCMatch;
import nightgames.global.Challenge;
import nightgames.global.DebugFlags;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.global.Match;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.items.clothing.Outfit;
import nightgames.json.JsonUtils;
import nightgames.pet.PetCharacter;
import nightgames.skills.Command;
import nightgames.skills.AssFuck;
import nightgames.skills.Nothing;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.skills.damage.DamageType;
import nightgames.stance.Position;
import nightgames.stance.Stance;
import nightgames.status.Abuff;
import nightgames.status.Alluring;
import nightgames.status.BodyFetish;
import nightgames.status.DivineCharge;
import nightgames.status.DivineRecoil;
import nightgames.status.Enthralled;
import nightgames.status.Falling;
import nightgames.status.Feral;
import nightgames.status.Frenzied;
import nightgames.status.Resistance;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.status.Trance;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;
import nightgames.status.addiction.Dominance;
import nightgames.status.addiction.MindControl;
import nightgames.trap.Trap;

public abstract class Character extends Observable implements Cloneable {
    public String name;
    public CharacterSex initialGender;
    public int level;
    public int xp;
    public int rank;
    public int money;
    public Map<Attribute, Integer> att;
    protected Meter stamina;
    protected Meter arousal;
    protected Meter mojo;
    protected Meter willpower;
    public Outfit outfit;
    public List<Clothing> outfitPlan;
    protected Area location;
    private CopyOnWriteArrayList<Skill> skills;
    public Set<Status> status;
    public Set<Stsflag> statusFlags;
    public CopyOnWriteArrayList<Trait> traits;
    protected Map<Trait, Integer> temporaryAddedTraits;
    protected Map<Trait, Integer> temporaryRemovedTraits;
    public Set<Status> removelist;
    public Set<Status> addlist;
    public Map<String, Integer> cooldowns;
    private CopyOnWriteArrayList<String> mercy;
    protected Map<Item, Integer> inventory;
    private Map<String, Integer> flags;
    protected Item trophy;
    public State state;
    protected int busy;
    protected Map<String, Integer> attractions;
    protected Map<String, Integer> affections;
    public HashSet<Clothing> closet;
    public List<Challenge> challenges;
    public Body body;
    public int availableAttributePoints;
    public boolean orgasmed;
    public boolean custom;
    private boolean pleasured;
    public int orgasms;
    public int cloned;
    private Map<Integer, LevelUpData> levelPlan;
    private Growth growth;

    public Character(String name, int level) {
        this.name = name;
        this.level = level;
        this.growth = new Growth();
        cloned = 0;
        custom = false;
        body = new Body(this);
        att = new HashMap<>();
        cooldowns = new HashMap<>();
        flags = new HashMap<>();
        levelPlan = new HashMap<>();
        att.put(Attribute.Power, 5);
        att.put(Attribute.Cunning, 5);
        att.put(Attribute.Seduction, 5);
        att.put(Attribute.Perception, 5);
        att.put(Attribute.Speed, 5);
        money = 0;
        stamina = new Meter(22 + 3 * level);
        stamina.fill();
        arousal = new Meter(90 + 10 * level);
        mojo = new Meter(100);
        willpower = new Meter(40);
        orgasmed = false;
        pleasured = false;

        outfit = new Outfit();
        outfitPlan = new ArrayList<>();

        closet = new HashSet<>();
        skills = (new CopyOnWriteArrayList<>());
        status = new HashSet<>();
        statusFlags = EnumSet.noneOf(Stsflag.class);
        traits = new CopyOnWriteArrayList<>();
        temporaryAddedTraits = new HashMap<>();
        temporaryRemovedTraits = new HashMap<>();
        removelist = new HashSet<>();
        addlist = new HashSet<>();
        mercy = new CopyOnWriteArrayList<>();
        inventory = new HashMap<>();
        attractions = new HashMap<>(2);
        affections = new HashMap<>(2);
        challenges = new ArrayList<>();
        location = new Area("", "", null);
        state = State.ready;
        busy = 0;
        setRank(0);

        Global.learnSkills(this);
    }

    @Override
    public Character clone() throws CloneNotSupportedException {
        Character c = (Character) super.clone();
        c.att = new HashMap<>(att);
        c.stamina = stamina.clone();
        c.cloned = cloned + 1;
        c.arousal = arousal.clone();
        c.mojo = mojo.clone();
        c.willpower = willpower.clone();
        c.outfitPlan = new ArrayList<>(outfitPlan);
        c.outfit = new Outfit(outfit);
        c.flags = new HashMap<>(flags);
        c.status = status; // Will be deep-copied in finishClone()
        c.traits = new CopyOnWriteArrayList<>(traits);
        c.temporaryAddedTraits = new HashMap<>(temporaryAddedTraits);
        c.temporaryRemovedTraits = new HashMap<>(temporaryRemovedTraits);

        // TODO! We should NEVER modify the growth in a combat sim. If this is not true, this needs to be revisited and deepcloned.
        c.growth = (Growth) growth.clone();

        c.removelist = new HashSet<>(removelist);
        c.addlist = new HashSet<>(addlist);
        c.mercy = new CopyOnWriteArrayList<>(mercy);
        c.inventory = new HashMap<>(inventory);
        c.attractions = new HashMap<>(attractions);
        c.affections = new HashMap<>(affections);
        c.skills = (new CopyOnWriteArrayList<>(getSkills()));
        c.body = body.clone();
        c.body.character = c;
        c.orgasmed = orgasmed;
        c.statusFlags = EnumSet.copyOf(statusFlags);
        c.levelPlan = new HashMap<>();
        for (Entry<Integer, LevelUpData> entry : levelPlan.entrySet()) {
            levelPlan.put(entry.getKey(), (LevelUpData)entry.getValue().clone());
        }
        return c;
    }

    public void finishClone(Character other) {
        Set<Status> oldstatus = status;
        status = new HashSet<>();
        for (Status s : oldstatus) {
            status.add(s.instance(this, other));
        }
    }

    public String name() {
        return name;
    }

    public List<Resistance> getResistances() {
        return traits.stream().map(Trait::getResistance).collect(Collectors.toList());
    }

    public int getXPReqToNextLevel() {
        return Math.min(45 + 5 * getLevel(), 100);
    }

    public int get(Attribute a) {
        if (a == Attribute.Slime && !has(Trait.slime)) {
            // always return 0 if there's no trait for it.
            return 0;
        }
        int total = getPure(a);
        for (Status s : getStatuses()) {
            total += s.mod(a);
        }
        total += body.mod(a, total);
        switch (a) {
            case Arcane:
                if (outfit.has(ClothingTrait.mystic)) {
                    total += 2;
                }
                break;
            case Dark:
                if (outfit.has(ClothingTrait.broody)) {
                    total += 2;
                }
                break;
            case Ki:
                if (outfit.has(ClothingTrait.martial)) {
                    total += 2;
                }
                break;
            case Fetish:
                if (outfit.has(ClothingTrait.kinky)) {
                    total += 2;
                }
                break;
            case Power:
                if (has(Trait.testosterone) && hasDick()) {
                    total += Math.min(20, 10 + getLevel() / 4);
                }
                break;
            case Science:
                if (has(ClothingTrait.geeky)) {
                    total += 2;
                }
                break;
            case Speed:
                if (has(ClothingTrait.bulky)) {
                    total -= 1;
                }
                if (has(ClothingTrait.shoes)) {
                    total += 1;
                }
                if (has(ClothingTrait.heels) && !has(Trait.proheels)) {
                    total -= 2;
                }
                if (has(ClothingTrait.highheels) && !has(Trait.proheels)) {
                    total -= 1;
                }
                if (has(ClothingTrait.higherheels) && !has(Trait.proheels)) {
                    total -= 1;
                }
                break;
            case Seduction:
                if (has(Trait.repressed)) {
                    total /= 2;
                }
                break;
            default:
                break;
        }
        return total;
    }

    public boolean has(ClothingTrait attribute) {
        return outfit.has(attribute);
    }

    public int getPure(Attribute a) {
        int total = 0;
        if (att.containsKey(a) && !a.equals(Attribute.Willpower)) {
            total = att.get(a);
        }
        return total;
    }

    public boolean check(Attribute a, int dc) {
        int rand = Global.random(20);
        if (Global.isDebugOn(DebugFlags.DEBUG_DAMAGE)) {
            System.out.println("Checked " + a + " = " + get(a) + " against " + dc + ", rolled " + rand);
        }
        if (rand == 0) {
            // critical hit, don't check
            return true;
        }
        return get(a) != 0 && get(a) + rand >= dc;
    }

    public int getLevel() {
        return level;
    }

    public void gainXP(int i) {
        assert i >= 0;
        double rate = 1.0;
        if (has(Trait.fastLearner)) {
            rate += .2;
        }
        rate *= Global.xpRate;
        xp += Math.round(i * rate);
        update();
    }

    public void setXP(int i) {
        xp = i;
        update();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void rankup() {
        rank++;
    }

    public abstract void ding();

    public String dong() {
        getLevelUpFor(getLevel()).unapply(this);;
        getGrowth().levelDown(this);
        levelPlan.remove(getLevel());
        level--;
        return Global.gainSkills(this);
    }

    public int getXP() {
        return xp;
    }


    public double modifyDamage(DamageType type, Character other, double baseDamage) {
        // so for each damage type, one level from the attacker should result in about 10% increased damage, while a point in defense should reduce damage by around 5% per level.
        // this differential should be max capped to (3 * (100 + attacker's level * 5))%
        // this differential should be min capped to (.5 * (100 + attacker's level * 5))%
        
        double maxDamage = baseDamage * 3 * (1 + .05 * getLevel());
        double minDamage = baseDamage * .5 * (1 + .05 * getLevel());
        double multiplier = (1 + .1 * getOffensivePower(type) - .05 * other.getDefensivePower(type));
        if (Global.isDebugOn(DebugFlags.DEBUG_DAMAGE)) {
            System.out.println(baseDamage + " from " + getName() + " has multiplier " + multiplier + " against " + other.getName() + "["+ getOffensivePower(type) +", " + other.getDefensivePower(type) + "].");
        }
        double damage = baseDamage * multiplier;
        return Math.min(Math.max(minDamage, damage), maxDamage);
    }

    private double getDefensivePower(DamageType type){
        switch (type) {
            case arcane:
                return get(Attribute.Arcane) + get(Attribute.Dark) / 2 + get(Attribute.Divinity) / 2 + get(Attribute.Ki) / 2;
            case biological:
                return get(Attribute.Animism) + get(Attribute.Bio) + get(Attribute.Medicine) / 2 + get(Attribute.Science) / 2 + get(Attribute.Cunning) / 2;
            case pleasure:
                return get(Attribute.Seduction);
            case temptation:
                return (get(Attribute.Seduction) * 2 + get(Attribute.Submissive) * 2 + get(Attribute.Cunning)) / 2.0;
            case technique:
                return get(Attribute.Cunning);
            case physical:
                return (get(Attribute.Power) * 2 + get(Attribute.Cunning)) / 2.0;
            case gadgets:
                return get(Attribute.Cunning);
            case drain:
                return (get(Attribute.Dark) * 2 + get(Attribute.Arcane)) / 2.0;
            case stance:
                return (get(Attribute.Cunning) * 2 + get(Attribute.Power)) / 2.0;
            case weaken:
                return (get(Attribute.Dark) * 2 + get(Attribute.Divinity)) / 2.0;
            case willpower:
                return (get(Attribute.Dark) + get(Attribute.Fetish) + get(Attribute.Divinity) * 2 + getLevel()) / 2.0;
            default:
                return 0;
        }
    }

    private double getOffensivePower(DamageType type){
        switch (type) {
            case biological:
                return get(Attribute.Animism) + get(Attribute.Bio) + get(Attribute.Medicine) + get(Attribute.Science);
            case gadgets:
                double power = (get(Attribute.Science) * 2 + get(Attribute.Cunning)) / 3.0;
                if (has(Trait.toymaster)) {
                    power += 20;
                }
                return power;
            case pleasure:
                return get(Attribute.Seduction);
            case arcane:
                return get(Attribute.Arcane);
            case temptation:
                return (get(Attribute.Seduction) * 2 + get(Attribute.Cunning)) / 3.0;
            case technique:
                return get(Attribute.Cunning);
            case physical:
                return (get(Attribute.Power) * 2 + get(Attribute.Cunning) + get(Attribute.Ki) * 2) / 3.0;
            case drain:
                return (get(Attribute.Dark) * 2 + get(Attribute.Arcane)) / 3.0;
            case stance:
                return (get(Attribute.Cunning) * 2 + get(Attribute.Power)) / 3.0;
            case weaken:
                return (get(Attribute.Dark) * 2 + get(Attribute.Divinity) + get(Attribute.Ki)) / 3.0;
            case willpower:
                return (get(Attribute.Dark) + get(Attribute.Fetish) + get(Attribute.Divinity) * 2 + getLevel()) / 3.0;
            default:
                return 0;
        }
    }

    public void pain(Combat c, Character other, int i) {
        pain(c, other, i, true, true);
    }

    public void pain(Combat c, Character other, int i, boolean primary, boolean physical) {
        int pain = i;
        int bonus = 0;
        if (is(Stsflag.rewired) && physical) {
            String message = String.format("%s pleasured for <font color='rgb(255,50,200)'>%d<font color='white'>\n",
                            Global.capitalizeFirstLetter(subjectWas()), pain);
            if (c != null) {
                c.writeSystemMessage(message);
            }
            arouse(pain, c);
            return;
        }
        if (has(Trait.slime)) {
            bonus -= pain / 2;
            if (c != null) {
                c.write(this, "The blow glances off " + nameOrPossessivePronoun() + " slimy body.");
            }
        }
        if (c != null) {
            if (has(Trait.cute) && other != null && primary && physical) {
                bonus -= Math.min(get(Attribute.Seduction), 50) * pain / 100;
                c.write(this, Global.format(
                                "{self:NAME-POSSESSIVE} innocent appearance throws {other:direct-object} off and {other:subject-action:use|uses} much less strength than intended.",
                                this, other));
            }
            if (other != null && other.has(Trait.dirtyfighter) && (c.getStance().prone(other)
                            || c.getStance()
                                .sub(other))
                            && physical) {
                bonus += 10;
                c.write(this, Global.format(
                                "{other:SUBJECT-ACTION:know|knows} how to fight dirty, and {other:action:manage|manages} to give {self:direct-object} a lot more trouble than {self:subject} expected despite being in a compromised position.",
                                this, other));
            }

            for (Status s : getStatuses()) {
                bonus += s.damage(c, pain);
            }
        }
        pain += bonus;
        pain = Math.max(1, pain);
        emote(Emotion.angry, pain / 3);

        // threshold at which pain calms you down
        int painAllowance = Math.max(10, getStamina().max() / 6);
        if (other != null && other.has(Trait.wrassler)) {
            painAllowance *= 1.5;
        }
        int difference = pain - painAllowance;
        // if the pain exceeds the threshold and you aren't a masochist
        // calm down by the overflow

        if (c != null) {
            c.writeSystemMessage(String.format("%s hurt for <font color='rgb(250,10,10)'>%d<font color='white'>",
                            subjectWas(), pain));
        }
        if (difference > 0 && !is(Stsflag.masochism)) {
            if (other != null && other.has(Trait.wrassler)) {
                calm(c, difference / 2);
            } else {
                calm(c, difference);
            }
        }
        // if you are a masochist, arouse by pain up to the threshold.
        if (is(Stsflag.masochism) && physical) {
            this.arouse(Math.max(i, painAllowance), c);
        }
        if (other != null && other.has(Trait.disablingblows) && Global.random(5) == 0) {
            int mag = Global.random(3) + 1;
            c.write(other, Global.format("Something about the way {other:subject-action:hit|hits}"
                            + " {self:name-do} seems to strip away {self:possessive} strength.", this, other));
            add(new Abuff(this, Attribute.Power, -mag, 10));
        }
        stamina.reduce(pain);
    }

    public void drain(Combat c, Character drainer, int i) {
        int drained = i;
        int bonus = 0;

        for (Status s : getStatuses()) {
            bonus += s.drained(drained);
        }
        drained += bonus;
        if (drained >= stamina.get()) {
            drained = stamina.get();
        }
        drained = Math.max(drained, i);
        if (c != null) {
            c.writeSystemMessage(
                            String.format("%s drained of <font color='rgb(200,200,200)'>%d<font color='white'> stamina by %s",
                                            subjectWas(), drained, drainer.subject()));
        }
        stamina.reduce(drained);
        drainer.stamina.restore(drained);
    }

    public void weaken(Combat c, int i) {
        int weak = i;
        int bonus = 0;
        for (Status s : getStatuses()) {
            bonus += s.weakened(i);
        }
        weak += bonus;
        if (weak >= stamina.get()) {
            weak = stamina.get();
        }
        i = Math.max(1, i);
        if (c != null) {
            c.writeSystemMessage(String.format("%s weakened by <font color='rgb(200,200,200)'>%d<font color='white'>",
                            subjectWas(), i));
        }
        stamina.reduce(weak);
    }

    public void heal(Combat c, int i) {
        i = Math.max(1, i);
        if (c != null) {
            c.writeSystemMessage(String.format("%s healed for <font color='rgb(100,240,30)'>%d<font color='white'>",
                            subjectWas(), i));
        }
        stamina.restore(i);
    }

    public String subject() {
        return name();
    }

    public void pleasure(int i, Combat c, Character source) {
        resolvePleasure(i, c, source, Body.nonePart, Body.nonePart);
    }

    public void resolvePleasure(int i, Combat c, Character source, BodyPart selfPart, BodyPart opponentPart) {
        int pleasure = i;

        emote(Emotion.horny, i / 4 + 1);
        if (pleasure < 1) {
            pleasure = 1;
        }
        pleasured = true;
        // pleasure = 0;
        arousal.restoreNoLimit(pleasure);
        if (checkOrgasm()) {
            doOrgasm(c, source, selfPart, opponentPart);
        }
    }

    public void tempt(Combat c, int i) {
        tempt(c, null, i);
    }

    public void tempt(Combat c, Character tempter, int i) {
        tempt(c, tempter, null, i);
    }

    public void tempt(Combat c, Character tempter, BodyPart with, int i) {
        if (tempter != null) {
            if (with != null) {
                // triple multiplier for the body part
                double temptMultiplier = tempter.body.getCharismaBonus(this) + with.getHotness(tempter, this) * 2;
                int dmg = (int) Math.round(i * temptMultiplier);
                tempt(dmg);
                String message = String.format(
                                "%s tempted by %s %s for <font color='rgb(240,100,100)'>%d<font color='white'> (base:%d, charisma:%.1f)\n",
                                Global.capitalizeFirstLetter(subjectWas()), tempter.nameOrPossessivePronoun(),
                                with.describe(tempter), dmg, i, temptMultiplier);
                if (Global.isDebugOn(DebugFlags.DEBUG_DAMAGE)) {
                    System.out.printf(message);
                }
                if (c != null) {
                    c.writeSystemMessage(message);
                }
            } else {
                double temptMultiplier = tempter.body.getCharismaBonus(this);
                if (c != null && tempter.has(Trait.obsequiousAppeal) && c.getStance()
                                                                         .sub(tempter)) {
                    temptMultiplier *= 2;
                }
                int dmg = (int) Math.round(i * temptMultiplier);
                tempt(dmg);
                String message = String.format(
                                "%s tempted %s for <font color='rgb(240,100,100)'>%d<font color='white'> (base:%d, charisma:%.1f)\n",
                                Global.capitalizeFirstLetter(tempter.subject()),
                                tempter == this ? reflectivePronoun() : directObject(), dmg, i, temptMultiplier);
                if (Global.isDebugOn(DebugFlags.DEBUG_DAMAGE)) {
                    System.out.printf(message);
                }
                if (c != null) {
                    c.writeSystemMessage(message);
                }
            }
        } else {
            if (c != null) {
                c.writeSystemMessage(
                                String.format("%s tempted for <font color='rgb(240,100,100)'>%d<font color='white'>\n",
                                                subjectWas(), i));
            }
            tempt(i);
        }
    }

    public void arouse(int i, Combat c) {
        arouse(i, c, "");
    }

    public void arouse(int i, Combat c, String source) {
        String message = String.format("%s aroused for <font color='rgb(240,100,100)'>%d<font color='white'> %s\n",
                        Global.capitalizeFirstLetter(subjectWas()), i, source);
        if (c != null) {
            c.writeSystemMessage(message);
        }
        tempt(i);
    }

    public String subjectAction(String verb, String pluralverb) {
        return subject() + " " + pluralverb;
    }
    
    public String subjectAction(String verb) {
        return subjectAction(verb, verb + "s");
    }

    public String subjectWas() {
        return subject() + " was";
    }

    public void tempt(int i) {
        int temptation = i;
        int bonus = 0;

        for (Status s : getStatuses()) {
            bonus += s.tempted(i);
        }
        temptation += bonus;
        if (has(Trait.desensitized2)) {
            temptation /= 2;
        }
        emote(Emotion.horny, i / 4);
        arousal.restoreNoLimit(temptation);
    }

    public void calm(Combat c, int i) {
        if (c != null) {
            String message = String.format("%s calmed down by <font color='rgb(80,145,200)'>%d<font color='white'>\n",
                            Global.capitalizeFirstLetter(subjectAction("have", "has")), i);
            c.writeSystemMessage(message);
        }
        arousal.reduce(i);
    }

    public Meter getStamina() {
        return stamina;
    }

    public Meter getArousal() {
        return arousal;
    }

    public Meter getMojo() {
        return mojo;
    }

    public Meter getWillpower() {
        return willpower;
    }

    public void buildMojo(Combat c, int percent) {
        buildMojo(c, percent, "");
    }

    public void buildMojo(Combat c, int percent, String source) {
        if (human() && Dominance.mojoIsBlocked(c)) {
            c.write(c.getOpponent(this), 
                            String.format("Enraptured by %s display of dominance, you build no mojo.", 
                                            c.getOpponent(this).nameOrPossessivePronoun()));
            return;
        }
        
        int x = percent * Math.min(mojo.max(), 200) / 100;
        int bonus = 0;
        for (Status s : getStatuses()) {
            bonus += s.gainmojo(x);
        }
        x += bonus;
        if (x > 0) {
            mojo.restore(x);
            if (c != null) {
                c.writeSystemMessage(Global.capitalizeFirstLetter(
                                String.format("%s <font color='rgb(100,200,255)'>%d<font color='white'> mojo%s.",
                                                subjectAction("built", "built"), x, source)));
            }
        } else if (x < 0) {
            loseMojo(c, x);
        }
    }

    public void spendMojo(Combat c, int i) {
        spendMojo(c, i, "");
    }

    public void spendMojo(Combat c, int i, String source) {
        int cost = i;
        int bonus = 0;
        for (Status s : getStatuses()) {
            bonus += s.spendmojo(i);
        }
        cost += bonus;
        mojo.reduce(cost);
        if (mojo.get() < 0) {
            mojo.set(0);
        }
        if (c != null && i != 0) {
            c.writeSystemMessage(Global.capitalizeFirstLetter(
                            String.format("%s <font color='rgb(150,150,250)'>%d<font color='white'> mojo%s.",
                                            subjectAction("spent", "spent"), cost, source)));
        }
    }

    public int loseMojo(Combat c, int i) {
        return loseMojo(c, i, "");
    }

    public int loseMojo(Combat c, int i, String source) {
        int amt = Math.min(mojo.get(), i);
        mojo.reduce(amt);
        if (mojo.get() < 0) {
            mojo.set(0);
        }
        if (c != null) {
            c.writeSystemMessage(Global.capitalizeFirstLetter(
                            String.format("%s <font color='rgb(150,150,250)'>%d<font color='white'> mojo%s.",
                                            subjectAction("lost", "lost"), amt, source)));
        }
        return amt;
    }

    public Area location() {
        return location;
    }

    public int init() {
        return att.get(Attribute.Speed) + Global.random(10);
    }

    public boolean reallyNude() {
        return topless() && pantsless();
    }

    public boolean torsoNude() {
        return topless() && pantsless();
    }

    public boolean mostlyNude() {
        return breastsAvailable() && crotchAvailable();
    }

    public boolean breastsAvailable() {
        return outfit.slotOpen(ClothingSlot.top);
    }

    public boolean crotchAvailable() {
        return outfit.slotOpen(ClothingSlot.bottom);
    }

    public void dress(Combat c) {
        outfit.dress(c.getCombatantData(this).getClothespile());
    }

    public void change() {
        outfit.undress();
        outfit.dress(outfitPlan);
        if (Global.getMatch() != null) {
            Global.getMatch().condition.handleOutfit(this);
        }
    }

    public String getName() {
        return name;
    }

    /* undress without any modifiers */
    public void undress(Combat c) {
        if (!breastsAvailable() || !crotchAvailable()) {
            // first time only strips down to what blocks fucking
            outfit.strip().forEach(article -> c.getCombatantData(this).addToClothesPile(article));
        } else {
            // second time strips down everything
            outfit.undress().forEach(article -> c.getCombatantData(this).addToClothesPile(article));
        }
    }

    /* undress non indestructibles */
    public boolean nudify() {
        if (!breastsAvailable() || !crotchAvailable()) {
            // first time only strips down to what blocks fucking
            outfit.forcedstrip();
        } else {
            // second time strips down everything
            outfit.undressOnly(c -> !c.is(ClothingTrait.indestructible));
        }
        return mostlyNude();
    }

    public Clothing strip(Clothing article, Combat c) {
        if (article == null) {
            return null;
        }
        Clothing res = outfit.unequip(article);
        c.getCombatantData(this).addToClothesPile(res);
        return res;
    }

    public Clothing strip(ClothingSlot slot, Combat c) {
        return strip(outfit.getTopOfSlot(slot), c);
    }

    public Clothing stripRandom(Combat c) {
        return stripRandom(c, false);
    }

    public void gainTrophy(Combat c, Character target) {
        Optional<Clothing> underwear = target.outfitPlan.stream()
                        .filter(article -> article.getSlots().contains(ClothingSlot.bottom) && article.getLayer() == 0)
                        .findFirst();
        if (!underwear.isPresent() || c.getCombatantData(target).getClothespile().contains(underwear.get())) {
            this.gain(target.getTrophy());
        }
    }

    public Clothing shredRandom() {
        ClothingSlot slot = outfit.getRandomShreddableSlot();
        if (slot != null) {
            return shred(slot);
        }
        return null;
    }

    public boolean topless() {
        return outfit.slotEmpty(ClothingSlot.top);
    }

    public boolean pantsless() {
        return outfit.slotEmpty(ClothingSlot.bottom);
    }

    public Clothing stripRandom(Combat c, boolean force) {
        return strip(force ? outfit.getRandomEquippedSlot() : outfit.getRandomNakedSlot(), c);
    }

    public Clothing getRandomStrippable() {
        ClothingSlot slot = getOutfit().getRandomEquippedSlot();
        return slot == null ? null : getOutfit().getTopOfSlot(slot);
    }

    public Clothing shred(ClothingSlot slot) {
        Clothing article = outfit.getTopOfSlot(slot);
        if (article == null || article.is(ClothingTrait.indestructible)) {
            System.err.println("Tried to shred clothing that doesn't exist at slot " + slot.name() + " at clone "
                            + cloned);
            System.err.println(outfit.toString());
            Thread.dumpStack();
            return null;
        } else {
            // don't add it to the pile
            return outfit.unequip(article);
        }
    }

    private void countdown(Map<Trait, Integer> counters) {
        Iterator<Map.Entry<Trait, Integer>> it = counters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Trait, Integer> ent = it.next();
            int remaining = ent.getValue() - 1;
            if (remaining > 0) {
                ent.setValue(remaining);
            } else {
                it.remove();
            }
        }
    }

    public void tick(Combat c) {
        body.tick(c);
        status.stream().filter(s -> c != null || s.lingering()).forEach(s -> s.tick(c));
        countdown(temporaryAddedTraits);
        countdown(temporaryRemovedTraits);
    }

    public Collection<Trait> getTraits() {
        Collection<Trait> allTraits = new HashSet<>();
        allTraits.addAll(traits);
        allTraits.addAll(temporaryAddedTraits.keySet());
        allTraits.removeAll(temporaryRemovedTraits.keySet());
        return allTraits;
    }

    public boolean addTemporaryTrait(Trait t, int duration) {
        if (!getTraits().contains(t)) {
            temporaryAddedTraits.put(t, duration);
            return true;
        } else if (temporaryAddedTraits.containsKey(t)) {
            temporaryAddedTraits.put(t, Math.max(duration, temporaryAddedTraits.get(t)));
            return true;
        }
        return false;
    }

    public boolean removeTemporaryTrait(Trait t, int duration) {
        if (temporaryRemovedTraits.containsKey(t)) {
            temporaryRemovedTraits.put(t, Math.max(duration, temporaryRemovedTraits.get(t)));
            return true;
        } else if (traits.contains(t)) {
            temporaryRemovedTraits.put(t, duration);
            return true;
        }
        return false;
    }

    public LevelUpData getLevelUpFor(int level) {
        levelPlan.putIfAbsent(level, new LevelUpData());
        return levelPlan.get(level);
    }

    public void modAttributeDontSaveData(Attribute a, int i) {
        if (human() && i != 0) {
            Global.gui().message("You have " + (i > 0 ? "gained" : "lost") + " " + i + " " + a.name());
        }
        if (a.equals(Attribute.Willpower)) {
            getWillpower().gain(i * 2);
        } else {
            att.put(a, att.getOrDefault(a, 0) + i);
        }
    }

    public void mod(Attribute a, int i) {
        modAttributeDontSaveData(a, i);
        getLevelUpFor(getLevel()).modAttribute(a, i);
    }

    public boolean add(Trait t) {
        if (traits.addIfAbsent(t)) {
            getLevelUpFor(getLevel()).addTrait(t);
            return true;
        }
        return false;
    }

    public boolean remove(Trait t) {
        if (traits.remove(t)) {
            getLevelUpFor(getLevel()).removeTrait(t);
            return true;
        }
        return false;
    }

    public boolean hasPure(Trait t) {
        return getTraits().contains(t);
    }

    public boolean has(Trait t) {
        boolean hasTrait = false;
        if (t.parent != null) {
            hasTrait = getTraits().contains(t.parent);
        }
        if (outfit.has(t)) {
            return true;
        }
        hasTrait = hasTrait || hasPure(t);
        return hasTrait;
    }

    public boolean hasDick() {
        return body.get("cock").size() > 0;
    }

    public boolean hasBalls() {
        return body.get("balls").size() > 0;
    }

    public boolean hasPussy() {
        return body.get("pussy").size() > 0;
    }

    public boolean hasBreasts() {
        return body.get("breasts").size() > 0;
    }

    public int countFeats() {
        int count = 0;
        for (Trait t : traits) {
            if (t.isFeat()) {
                count++;
            }
        }
        return count;
    }

    public void regen() {
        regen(null, false);
    }

    public void regen(Combat c) {
        regen(c, true);
    }

    public void regen(Combat c, boolean combat) {
        int regen = 1;
        // TODO can't find the concurrent modification error, just use a copy
        // for now I guess...
        for (Status s : new HashSet<>(getStatuses())) {
            regen += s.regen(c);
        }
        if (has(Trait.BoundlessEnergy)) {
            regen += 1;
        }
        if (regen > 0) {
            heal(c, regen);
        } else {
            weaken(c, -regen);
        }
        if (combat) {
            if (has(Trait.exhibitionist) && mostlyNude()) {
                buildMojo(c, 5);
            }
            if (outfit.has(ClothingTrait.stylish)) {
                buildMojo(c, 1);
            }
            if (has(Trait.SexualGroove)) {
                buildMojo(c, 2);
            }
            if (outfit.has(ClothingTrait.lame)) {
                buildMojo(c, -1);
            }
        }
    }

    public void preturnUpkeep() {
        orgasmed = false;
    }

    public void add(Status status) {
        add(null, status);
    }

    public boolean has(Status status) {
        return this.status.stream().anyMatch(s -> s.flags().containsAll(status.flags()) && status.flags()
                        .containsAll(status.flags()) && s.getVariant().equals(status.getVariant()));
    }

    public void add(Combat c, Status status) {
        boolean cynical = false;
        String message = "";
        boolean done = false;
        Status effectiveStatus = status;
        for (Status s : getStatuses()) {
            if (s.flags().contains(Stsflag.cynical)) {
                cynical = true;
            }
        }
        if (cynical && status.mindgames()) {
            message = subjectAction("resist", "resists") + " " + status.name + " (Cynical).";
            done = true;
        } else {
            for (Resistance r : getResistances()) {
                String resistReason = "";
                resistReason = r.resisted(this, status);
                if (!resistReason.isEmpty()) {
                    message = subjectAction("resist", "resists") + " " + status.name + " (" + resistReason + ").";
                    done = true;
                    break;
                }
            }
        }
        if (!done) {
            boolean unique = true;
            for (Status s : this.status) {
                if (s.getVariant().equals(status.getVariant())) {
                    s.replace(status);
                    message = s.initialMessage(c, false);
                    done = true;
                    effectiveStatus = s;
                    break;
                }
                if (s.overrides(status)) {
                    unique = false;
                }
            }
            if (!done && unique) {
                this.status.add(status);
                message = status.initialMessage(c, false);
            }
        }
        if (done) {
            if (!message.isEmpty()) {
                message = Global.capitalizeFirstLetter(message);
                if (c != null) {
                    if (!c.getOpponent(this).human() || !c.getOpponent(this).is(Stsflag.blinded)) {
                        c.write(this, "<b>" + message + "</b>");
                    } effectiveStatus.onApply(c, c.getOpponent(this));
                } else if (human() || location() != null && location().humanPresent()) {
                    Global.gui().message("<b>" + message + "</b>");
                    effectiveStatus.onApply(null, null);
                }
            }
            if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
                System.out.println(message);
            }
        }
    }

    private double getPheromonesChance(Combat c) {
        double baseChance = .1 + getExposure() / 2 + (arousal.getOverflow() + arousal.get()) / (float) arousal.max();
        double mod = c.getStance().pheromoneMod(this);
        return Math.min(1, baseChance * mod);
    }

    public boolean rollPheromones(Combat c) {
        double chance = getPheromonesChance(c);
        double roll = Global.randomdouble();
        // System.out.println("Pheromones: rolled " + Global.formatDecimal(roll)
        // + " vs " + chance + ".");
        return roll < chance;
    }

    public int getPheromonePower() {
        return 1 + (get(Attribute.Animism) + get(Attribute.Bio)) / 10;
    }

    public void dropStatus(Combat c, Character opponent) {
        Set<Status> removedStatuses = status.stream().filter(s -> !s.meetsRequirements(c, this, opponent))
                        .collect(Collectors.toSet());
        removedStatuses.addAll(removelist);
        removedStatuses.forEach(s -> {
            if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
                System.out.println(s.name + " removed from " + name());
            }
            s.onRemove(c, opponent);
        });
        status.removeAll(removedStatuses);
        for (Status s : addlist) {
            add(c, s);
        }
        removelist.clear();
        addlist.clear();
    }

    public void removeStatusNoSideEffects() {
        status.removeAll(removelist);
        removelist.clear();
    }

    public boolean is(Stsflag sts) {
        if (statusFlags.contains(sts))
            return true;
        for (Status s : getStatuses()) {
            if (s.flags().contains(sts)) {
                return true;
            }
        }
        return false;
    }

    public boolean is(Stsflag sts, String variant) {
        for (Status s : getStatuses()) {
            if (s.flags().contains(sts) && s.getVariant().equals(variant)) {
                return true;
            }
        }
        return false;
    }

    public boolean stunned() {
        for (Status s : getStatuses()) {
            if (s.flags().contains(Stsflag.stunned)) {
                return true;
            }
        }
        return false;
    }

    public boolean distracted() {
        for (Status s : getStatuses()) {
            if (s.flags().contains(Stsflag.distracted) || s.flags().contains(Stsflag.trance)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasStatus(Stsflag flag) {
        for (Status s : getStatuses()) {
            if (s.flags().contains(flag)) {
                return true;
            }
        }
        return false;
    }

    public void removeStatus(Stsflag flag) {
        for (Status s : getStatuses()) {
            if (s.flags().contains(flag)) {
                removelist.add(s);
            }
        }
    }

    public boolean bound() {
        for (Status s : getStatuses()) {
            if (s.flags().contains(Stsflag.bound)) {
                return true;
            }
        }
        return false;
    }

    public void free() {
        for (Status s : getStatuses()) {
            if (s.flags().contains(Stsflag.bound)) {
                removelist.add(s);
            }
        }
    }

    public void struggle() {
        for (Status s : getStatuses()) {
            s.struggle(this);
        }
    }

    public int getEscape(Combat c, Character from) {
        int total = 0;
        for (Status s : getStatuses()) {
            total += s.escape();
        }
        if (has(Trait.freeSpirit)) {
            total += 5;
        }
        if (from.has(Trait.Clingy)) {
            total -= 5;
        }
        int stanceMod = c.getStance().escape(c, this);
        if (stanceMod < 0) {
            total += stanceMod;
        }
        return total;
    }

    public int escape(Combat c, Character from) {
        int total = getEscape(c, from);
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.println("Escape: " + total);
        }
        if (c.getStance().escape(c, this) < 0) {
            c.getStance().struggle();
        }
        return total;
    }

    public boolean canMasturbate() {
        return !(stunned() || bound() || is(Stsflag.distracted) || is(Stsflag.enthralled));
    }

    public boolean canAct() {
        return !(stunned() || distracted() || bound() || is(Stsflag.enthralled));
    }

    public boolean canRespond() {
        return !(stunned() || distracted() || is(Stsflag.enthralled));
    }

    public abstract void detect();

    public abstract void faceOff(Character opponent, IEncounter enc);

    public abstract void spy(Character opponent, IEncounter enc);

    public abstract String describe(int per, Combat c);

    public abstract void victory(Combat c, Result flag);

    public abstract void defeat(Combat c, Result flag);

    public abstract void intervene3p(Combat c, Character target, Character assist);

    public abstract void victory3p(Combat c, Character target, Character assist);

    public abstract boolean resist3p(Combat c, Character target, Character assist);

    public abstract void act(Combat c);

    public abstract void move();

    public abstract void draw(Combat c, Result flag);

    public abstract boolean human();

    public abstract String bbLiner(Combat c, Character target);

    public abstract String nakedLiner(Combat c, Character target);

    public abstract String stunLiner(Combat c, Character target);

    public abstract String taunt(Combat c, Character target);

    public abstract void intervene(IEncounter fight, Character p1, Character p2);

    public abstract void showerScene(Character target, IEncounter encounter);

    public boolean humanControlled(Combat c) {
        return human() || Global.isDebugOn(DebugFlags.DEBUG_SKILL_CHOICES) && c.getOpponent(this).human();
    }

    public JsonObject save() {
        JsonObject saveObj = new JsonObject();
        saveObj.addProperty("name", name);
        saveObj.addProperty("type", getType());
        saveObj.addProperty("level", level);
        saveObj.addProperty("rank", getRank());
        saveObj.addProperty("xp", xp);
        saveObj.addProperty("money", money);
        {
            JsonObject resources = new JsonObject();
            resources.addProperty("stamina", stamina.trueMax());
            resources.addProperty("arousal", arousal.trueMax());
            resources.addProperty("mojo", mojo.trueMax());
            resources.addProperty("willpower", willpower.trueMax());
            saveObj.add("resources", resources);
        }
        saveObj.add("affections", JsonUtils.JsonFromMap(affections));
        saveObj.add("attractions", JsonUtils.JsonFromMap(attractions));
        saveObj.add("attributes", JsonUtils.JsonFromMap(att));
        saveObj.add("outfit", JsonUtils.jsonFromCollection(outfitPlan));
        saveObj.add("closet", JsonUtils.jsonFromCollection(closet));
        saveObj.add("traits", JsonUtils.jsonFromCollection(traits));
        saveObj.add("body", body.save());
        saveObj.add("inventory", JsonUtils.JsonFromMap(inventory));
        saveObj.addProperty("human", human());
        saveObj.add("flags", JsonUtils.JsonFromMap(flags));
        saveObj.add("levelUps", JsonUtils.JsonFromMap(levelPlan));
        saveObj.add("growth", JsonUtils.gson.toJsonTree(growth));
        saveInternal(saveObj);
        return saveObj;
    }

    protected void saveInternal(JsonObject obj) {

    }

    public abstract String getType();

    public void load(JsonObject object) {
        name = object.get("name").getAsString();
        level = object.get("level").getAsInt();
        rank = object.get("rank").getAsInt();
        xp = object.get("xp").getAsInt();
        if (object.has("growth")) {
            growth = JsonUtils.gson.fromJson(object.get("growth"), Growth.class);
        }
        money = object.get("money").getAsInt();
        {
            JsonObject resources = object.getAsJsonObject("resources");
            stamina.setMax(resources.get("stamina").getAsFloat());
            arousal.setMax(resources.get("arousal").getAsFloat());
            mojo.setMax(resources.get("mojo").getAsFloat());
            willpower.setMax(resources.get("willpower").getAsFloat());
        }

        affections = JsonUtils.mapFromJson(object.getAsJsonObject("affections"), String.class, Integer.class);
        attractions = JsonUtils.mapFromJson(object.getAsJsonObject("attractions"), String.class, Integer.class);

        {
            outfitPlan.clear();
            JsonUtils.getOptionalArray(object, "outfit").ifPresent(this::addClothes);
        }

        {
            traits = new CopyOnWriteArrayList<>(
                            JsonUtils.collectionFromJson(object.getAsJsonArray("traits"), Trait.class).stream()
                            .filter(trait -> trait != null).collect(Collectors.toList()));
            if (getType().equals("Airi"))
                traits.remove(Trait.slime);
        }

        body = Body.load(object.getAsJsonObject("body"), this);
        att = JsonUtils.mapFromJson(object.getAsJsonObject("attributes"), Attribute.class, Integer.class);

        inventory = JsonUtils.mapFromJson(object.getAsJsonObject("inventory"), Item.class, Integer.class);

        flags.clear();
        JsonUtils.getOptionalObject(object, "flags")
                        .ifPresent(obj -> flags.putAll(JsonUtils.mapFromJson(obj, String.class, Integer.class)));
        if (object.has("levelUps")) {
            levelPlan = JsonUtils.mapFromJson(object.getAsJsonObject("levelUps"), Integer.class, LevelUpData.class);
        } else {
            levelPlan = new HashMap<>();
        }
        loadInternal(object);
        change();
        Global.gainSkills(this);
        Global.learnSkills(this);
    }

    private void addClothes(JsonArray array) {
        outfitPlan.addAll(
                        JsonUtils.stringsFromJson(array).stream().map(Clothing::getByID).collect(Collectors.toList()));
    }

    protected void loadInternal(JsonObject obj) {

    }

    public abstract void afterParty();

    public boolean checkOrgasm() {
        return getArousal().isFull() && !is(Stsflag.orgasmseal) && pleasured;
    }

    public CharacterSex getSex() {
        // it's oversimplified I know. So sue me :(
        if (hasDick() && hasPussy()) {
            return CharacterSex.herm;
        }
        if (hasDick()) {
            return CharacterSex.male;
        }
        if (hasPussy()) {
            return CharacterSex.female;
        }
        return CharacterSex.asexual;
    }

    public void doOrgasm(Combat c, Character opponent, BodyPart selfPart, BodyPart opponentPart) {
        int total = this != opponent && opponent != null && opponent.has(Trait.carnalvirtuoso) ? 2 : 1;
        for (int i = 1; i <= total; i++) {
            resolveOrgasm(c, opponent, selfPart, opponentPart, i, total);
        }
    }

    protected void resolveOrgasm(Combat c, Character opponent, BodyPart selfPart, BodyPart opponentPart, int times,
                    int totalTimes) {
        String orgasmLiner = "<b>" + orgasmLiner(c) + "</b>";
        String opponentOrgasmLiner = opponent == null || opponent == this ? "" : 
            "<b>" + opponent.makeOrgasmLiner(c) + "</b>";
        orgasmed = true;
        if (times == 1) {
            c.write(this, "<br>");
        }
        if (opponent == this) {
            resolvePreOrgasmForSolo(c, opponent, selfPart, times);
        } else {
            resolvePreOrgasmForOpponent(c, opponent, selfPart, opponentPart, times, totalTimes);
        }
        int overflow = arousal.getOverflow();
        c.write(this, String.format("<font color='rgb(255,50,200)'>%s<font color='white'> arousal overflow", overflow));
        if (this != opponent) {
            resolvePostOrgasmForOpponent(c, opponent, selfPart, opponentPart);
        }
        getArousal().empty();
        if (has(Trait.insatiable)) {
            arousal.restore((int) (arousal.max() * .2));
        }
        if (is(Stsflag.feral)) {
            arousal.restore(arousal.max() / 2);
        }
        float extra = 25.0f * overflow / (arousal.max() / 2.0f);

        loseWillpower(c, getOrgasmWillpowerLoss(), Math.round(extra), true, "");
        if (has(Trait.nymphomania) && !getWillpower().isEmpty() && times == totalTimes) {
            if (human()) {
                c.write("Cumming actually made you feel kind of refreshed, albeit with a burning desire for more.");
            } else {
                c.write(Global.format(
                                "After {self:subject} comes down from {self:possessive} orgasmic high, {self:pronoun} doesn't look satisfied at all. There's a mad glint in {self:possessive} eye that seems to be endlessly asking for more.",
                                this, opponent));
            }
            restoreWillpower(c, 5 + Math.min((get(Attribute.Animism) + get(Attribute.Nymphomania)) / 5, 15));
        }
        if (this != opponent && times == totalTimes) {
            c.write(this, orgasmLiner);
            c.write(opponent, opponentOrgasmLiner);
        }
        // TODO: If NPCs are able to acquire long term addictions, replace this check with a call to human().
        if (this instanceof Player) {
            Player p = (Player) this;

            if (p.checkAddiction(AddictionType.CORRUPTION, opponent) && selfPart != null && opponentPart != null 
                            && opponentPart.isType("pussy") && selfPart
                            .isType("cock") && c.getCombatantData(this).getIntegerFlag("ChoseToFuck") == 1) {
                c.write(this, "Your willing sacrifice to " + opponent.getName() + " greatly reinforces"
                                + " the corruption inside of you.");
                p.addict(AddictionType.CORRUPTION, opponent, Addiction.HIGH_INCREASE);
            }
            if (p.checkAddiction(AddictionType.ZEAL, opponent) && selfPart != null && opponentPart != null 
                            && opponentPart.isType("pussy") && selfPart
                            .isType("cock")) {
                c.write(this, "Experiencing so much pleasure inside of " + opponent + " reinforces" + " your faith.");
                p.addict(AddictionType.ZEAL, opponent, Addiction.MED_INCREASE);
            }
            if (p.checkAddiction(AddictionType.BREEDER)) {
                // Clear combat addiction
                p.unaddictCombat(AddictionType.BREEDER, opponent, 1.f, c);
            }
        }
        orgasms += 1;
    }

    private void resolvePreOrgasmForSolo(Combat c, Character opponent, BodyPart selfPart, int times) {
        if (selfPart != null && selfPart.isType("cock")) {
            if (times == 1) {
                c.write(this, Global.format(
                                "<b>{self:NAME-POSSESSIVE} back arches as thick ropes of jizz fire from {self:possessive} dick and land on {self:reflective}.</b>",
                                this, opponent));
            } else {
                c.write(this, Global.format(
                                "<b>{other:SUBJECT-ACTION:expertly coax|expertly coaxes} yet another orgasm from {self:name-do}, leaving {self:direct-object} completely spent.</b>",
                                this, opponent));
            }
        } else {
            if (times == 1) {
                c.write(this, Global.format(
                                "<b>{self:SUBJECT-ACTION:shudder|shudders} as {self:pronoun} {self:action:bring|brings} {self:reflective} to a toe-curling climax.</b>",
                                this, opponent));
            } else {
                c.write(this, Global.format(
                                "<b>{other:SUBJECT-ACTION:expertly coax|expertly coaxes} yet another orgasm from {self:name-do}, leaving {self:direct-object} completely spent.</b>",
                                this, opponent));
            }
        }
    }

    private void resolvePreOrgasmForOpponent(Combat c, Character opponent, BodyPart selfPart, BodyPart opponentPart,
                    int times, int total) {
        if (c.getStance().inserted(this) && !has(Trait.strapped)) {
            if (times == 1) {
                c.write(this, Global.format(
                                "<b>{self:SUBJECT-ACTION:tense|tenses} up as {self:possessive} hips wildly buck against {other:direct-object}. In no time, {self:possessive} hot seed spills into {other:possessive} pulsing hole.</b>",
                                this, opponent));
            } else {
                c.write(this, Global.format(
                                "<b>{other:NAME-POSSESSIVE} devilish orfice does not let up, and {other:possessive} intense actions somehow force {self:name-do} to cum again instantly.</b>",
                                this, opponent));
            }
            if (c.getStance().en == Stance.anal) {
                opponent.body.receiveCum(c, this, opponent.body.getRandom("ass"));
            } else {
                opponent.body.receiveCum(c, this, opponent.body.getRandom("pussy"));
            }
        } else if (selfPart != null && selfPart.isType("cock") && opponentPart != null
                        && !opponentPart.isType("none")) {
            if (times == 1) {
                c.write(this, Global.format(
                                "<b>{self:NAME-POSSESSIVE} back arches as thick ropes of jizz fire from {self:possessive} dick and land on {other:name-possessive} "
                                                + opponentPart.describe(opponent) + ".</b>",
                                this, opponent));
            } else {
                c.write(this, Global.format(
                                "<b>{other:SUBJECT-ACTION:expertly coax|expertly coaxes} yet another orgasm from {self:name-do}, leaving {self:direct-object} completely spent.</b>",
                                this, opponent));
            }
            opponent.body.receiveCum(c, this, opponentPart);
        } else {
            if (times == 1) {
                c.write(this, Global.format(
                                "<b>{self:SUBJECT-ACTION:shudder|shudders} as {other:subject-action:bring|brings} {self:direct-object} to a toe-curling climax.</b>",
                                this, opponent));
            } else {
                c.write(this, Global.format(
                                "<b>{other:SUBJECT-ACTION:expertly coax|expertly coaxes} yet another orgasm from {self:name-do}, leaving {self:direct-object} completely spent.</b>",
                                this, opponent));
            }
        }
        if (human() && opponent.has(Trait.mindcontroller) && cloned == 0) {
            MindControl.Result res = new MindControl.Result(opponent, c.getStance());
            String message = res.getDescription();
            if (res.hasSucceeded()) {
                message += "While your senses are overwhelmed by your violent orgasm, the deep pools of Mara's eyes"
                                + " swirl and dance. You helplessly stare at the intricate movements and feel a strong"
                                + " pressure on your mind as you do. When your orgasm dies down, so do the dancing patterns."
                                + " With a satisfied smirk, Mara tells you to lift an arm. Before you have even processed"
                                + " her words, you discover that your right arm is sticking straight up into the air. This"
                                + " is probably really bad.";
                Global.getPlayer()
                      .addict(AddictionType.MIND_CONTROL, opponent, Addiction.MED_INCREASE);
            }
            c.write(this, message);
        }
    }

    private void resolvePostOrgasmForOpponent(Combat c, Character opponent, BodyPart selfPart, BodyPart opponentPart) {
        if (selfPart != null && opponentPart != null) {
            selfPart.onOrgasmWith(c, this, opponent, opponentPart, true);
            opponentPart.onOrgasmWith(c, opponent, this, selfPart, false);
        } else if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.printf("Could not process %s's orgasm against %s: self=%s, opp=%s, pos=%s", this, opponent,
                            selfPart, opponentPart, c.getStance());
        }
        body.getCurrentParts().forEach(part -> part.onOrgasm(c, this, opponent));

        if (opponent.has(Trait.erophage)) {
            c.write(Global.capitalizeFirstLetter("<b>" + opponent.subjectAction("flush", "flushes")
                            + " as the feedback from " + nameOrPossessivePronoun() + " orgasm feeds "
                            + opponent.possessivePronoun() + " divine power.</b>"));
            opponent.add(c, new Alluring(opponent, 5));
            opponent.buildMojo(c, 100);
            if (c.getStance().inserted(this) && opponent.has(Trait.divinity)) {
                opponent.add(c, new DivineCharge(opponent, 1));
            }
        }
        if (opponent.has(Trait.sexualmomentum)) {
            c.write(Global.capitalizeFirstLetter(
                            "<b>" + opponent.subjectAction("are more composed", "seems more composed") + " as "
                                            + nameOrPossessivePronoun() + " forced orgasm goes straight to "
                                            + opponent.possessivePronoun() + " ego.</b>"));
            opponent.restoreWillpower(c, 10 + Global.random(10));
        }
        if (opponent.has(Trait.leveldrainer) && ((c.getStance()
                                                  .penetratedBy(c, opponent, this)
                        && !has(Trait.strapped)) || c.getStance().en == Stance.trib)) {
            if (Global.random(10) < 8 && getLevel() > 1 && getLevel() <= opponent.getLevel()
                            && !c.getCombatantData(opponent).getBooleanFlag("has_drained")) {
                c.getCombatantData(opponent).toggleFlagOn("has_drained", true);
                if (c.getStance().en != Stance.trib)
                    c.write(opponent, Global.capitalizeFirstLetter(String.format("%s %s contracts around %s %s, reinforcing"
                            + " %s orgasm and drawing upon %s very strength and experience. Once it's over, %s"
                                                    + " left considerably more powerful, at %s expense.",
                                    opponent.nameOrPossessivePronoun(),
                                    c.getStance().insertablePartFor(c, opponent).describe(opponent),
                                    nameOrPossessivePronoun(),
                            c.getStance().insertedPartFor(c, this).describe(this), possessivePronoun(), possessivePronoun(),
                            opponent.subjectAction("are", "is"), nameOrPossessivePronoun())));
                else
                    c.write(opponent, Global.capitalizeFirstLetter(String.format("%s greedy %s sucks itself tightly to"
                                + " %s %s, drawing in %s strength and experience along with the pleasure of %s orgasm.",
                                opponent.nameOrPossessivePronoun(), opponent.body.getRandomPussy().describe(opponent),
                                nameOrPossessivePronoun(), body.getRandomPussy().describe(this), possessivePronoun(),
                                possessivePronoun())));
                int xpStolen = getXP();
                c.write(dong());
                opponent.gainXP(Math.min(opponent.getXPReqToNextLevel(), xpStolen));
            } else {
                c.write(opponent, Global.capitalizeFirstLetter(String.format("%s %s pulses, but fails to"
                                                + " draw in %s experience.", opponent.nameOrPossessivePronoun(),
                                opponent.body.getRandomPussy().describe(opponent),
                                nameOrPossessivePronoun())));
            }
        }
    }

    public void loseWillpower(Combat c, int i) {
        loseWillpower(c, i, 0, false, "");
    }

    public void loseWillpower(Combat c, int i, boolean primary) {
        loseWillpower(c, i, 0, primary, "");
    }

    public void loseWillpower(Combat c, int i, int extra, boolean primary, String source) {
        int amt = i + extra;
        String reduced = "";
        if (has(Trait.strongwilled) && primary) {
            amt = amt * 2 / 3 + 1;
            reduced = " (Strong-willed)";
        }
        if (is(Stsflag.feral) && primary) {
            amt = amt / 3;
            reduced = " (Feral)";
        }
        int old = willpower.get();
        willpower.reduce(amt);
        if (c != null) {
            c.writeSystemMessage(String.format(
                            "%s lost <font color='rgb(220,130,40)'>%s<font color='white'> willpower" + reduced + "%s.",
                            subject(), extra == 0 ? Integer.toString(amt) : i + "+" + extra + " (" + amt + ")",
                            source));
        } else {
            Global.gui().systemMessage(String
                            .format("%s lost <font color='rgb(220,130,40)'>%d<font color='white'> willpower" + reduced
                                            + "%s.", subject(), amt, source));
        }
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.printf("will power reduced from %d to %d\n", old, willpower.get());
        }
    }

    public void restoreWillpower(Combat c, int i) {
        willpower.restore(i);
        c.writeSystemMessage(String.format(
                        "%s regained <font color='rgb(181,230,30)'>%d<font color='white'> willpower.", subject(), i));
    }

    public void eot(Combat c, Character opponent, Skill last) {
        dropStatus(c, opponent);
        tick(c);
        List<String> removed = new ArrayList<>();
        for (String s : cooldowns.keySet()) {
            if (cooldowns.get(s) <= 1) {
                removed.add(s);
            } else {
                cooldowns.put(s, cooldowns.get(s) - 1);
            }
        }
        for (String s : removed) {
            cooldowns.remove(s);
        }
        if (c.getStance().inserted()) {
            BodyPart selfOrgan;
            BodyPart otherOrgan;
            if (c.getStance().inserted(this)) {
                selfOrgan = body.getRandomCock();
                if (c.getStance().en == Stance.anal) {
                    otherOrgan = opponent.body.getRandom("ass");
                } else {
                    otherOrgan = opponent.body.getRandomPussy();
                }
            } else {
                otherOrgan = opponent.body.getRandomCock();
                if (c.getStance().en == Stance.anal) {
                    selfOrgan = body.getRandom("ass");
                } else {
                    selfOrgan = body.getRandomPussy();
                }
            }
            if (has(Trait.energydrain) && selfOrgan != null && otherOrgan != null) {
                c.write(this, Global.format(
                                "{self:NAME-POSSESSIVE} body glows purple as {other:subject-action:feel|feels} {other:possessive} very spirit drained into {self:possessive} "
                                                + selfOrgan.describe(this) + " through your connection.",
                                this, opponent));
                int m = Global.random(5) + 5;
                opponent.drain(c, this, (int) this.modifyDamage(DamageType.drain, opponent, m));
            }
            // TODO this works weirdly when both have both organs.
            body.tickHolding(c, opponent, selfOrgan, otherOrgan);
        }
        if (outfit.has(ClothingTrait.tentacleSuit)) {
            c.write(this, Global.format("The tentacle suit squirms against {self:name-possessive} body.", this,
                            opponent));
            if (hasBreasts()) {
                TentaclePart.pleasureWithTentacles(c, this, 5, body.getRandomBreasts());
            }
            TentaclePart.pleasureWithTentacles(c, this, 5, body.getRandom("skin"));
        }
        if (outfit.has(ClothingTrait.tentacleUnderwear)) {
            String undieName = "underwear";
            if (hasPussy()) {
                undieName = "panties";
            }
            c.write(this, Global.format("The tentacle " + undieName + " squirms against {self:name-possessive} crotch.",
                            this, opponent));
            if (hasDick()) {
                TentaclePart.pleasureWithTentacles(c, this, 5, body.getRandomCock());
                body.pleasure(null, null, body.getRandom("cock"), 5, c);
            }
            if (hasBalls()) {
                TentaclePart.pleasureWithTentacles(c, this, 5, body.getRandom("balls"));
            }
            if (hasPussy()) {
                TentaclePart.pleasureWithTentacles(c, this, 5, body.getRandomPussy());
            }
            TentaclePart.pleasureWithTentacles(c, this, 5, body.getRandomAss());
        }
        if (checkOrgasm()) {
            doOrgasm(c, opponent, null, null);
        }
        if (opponent.checkOrgasm()) {
            opponent.doOrgasm(c, this, null, null);
        }
        if (opponent.has(Trait.magicEyeEnthrall) && getArousal().percent() >= 50 && c.getStance().facing(this, opponent)
                        && Global.random(20) == 0) {
            c.write(opponent,
                            Global.format("<br>{other:NAME-POSSESSIVE} eyes start glowing and captures both {self:name-possessive} gaze and consciousness.",
                                            this, opponent));
            add(c, new Enthralled(this, opponent, 2));
        }
        if (opponent.has(Trait.magicEyeTrance) && getArousal().percent() >= 50 && c.getStance().facing(this, opponent)
                        && Global.random(10) == 0) {
            c.write(opponent,
                            Global.format("<br>{other:NAME-POSSESSIVE} eyes start glowing and send {self:subject} straight into a trance.",
                                            this, opponent));
            add(c, new Trance(this));
        }

        if (opponent.has(Trait.magicEyeFrenzy) && getArousal().percent() >= 50 && c.getStance().facing(this, opponent)
                        && Global.random(10) == 0) {
            c.write(opponent,
                            Global.format("<br>{other:NAME-POSSESSIVE} eyes start glowing and send {self:subject} into a frenzy.",
                                            this, opponent));
            add(c, new Frenzied(this, 3));
        }

        if (opponent.has(Trait.magicEyeArousal) && getArousal().percent() >= 50 && c.getStance().facing(this, opponent)
                        && Global.random(5) == 0) {
            c.write(opponent,
                            Global.format("<br>{other:NAME-POSSESSIVE} eyes start glowing and {self:subject-action:feel|feels} a strong pleasure wherever {other:possessive} gaze lands. {self:SUBJECT-ACTION:are|is} literally being raped by {other:name-possessive} eyes!",
                                            this, opponent));
            tempt(c, opponent, opponent.get(Attribute.Seduction) / 2);
        }

        if (opponent.has(Trait.enchantingVoice)) {
            int voiceCount = c.getCombatantData(opponent).getIntegerFlag("enchantingvoice-count");
            if (voiceCount >= 1) {
                if (!opponent.human()) {
                    c.write(opponent,
                                    Global.format("{other:SUBJECT} winks at you and verbalizes a few choice words that pass straight through your mental barriers.",
                                                    this, opponent));
                } else {
                    c.write(opponent,
                                    Global.format("Sensing a moment of distraction, you use the power in your voice to force {self:subject} to your will.",
                                                    this, opponent));
                }
                (new Command(opponent)).resolve(c, this);
                int cooldown = Math.max(1, 6 - (opponent.getLevel() - getLevel() / 5));
                c.getCombatantData(opponent).setIntegerFlag("enchantingvoice-count", -cooldown);
            } else {
                c.getCombatantData(opponent).setIntegerFlag("enchantingvoice-count", voiceCount + 1);
            }
        }
        if (getPure(Attribute.Animism) >= 4 && getArousal().percent() >= 50 && !is(Stsflag.feral)) {
            add(c, new Feral(this));
        }
        
        if (opponent.has(Trait.temptingass) && !is(Stsflag.frenzied)) {
            AssFuck fuck = new AssFuck(this);
            if (fuck.requirements(c, opponent) && fuck.usable(c, opponent)) {
                int chance = 20;
                chance += Math.max(0, Math.min(15, opponent.get(Attribute.Seduction) - get(Attribute.Seduction)));
                if (is(Stsflag.feral))
                    chance += 10;
                if (is(Stsflag.charmed) || opponent.is(Stsflag.alluring))
                    chance += 5;
                if (has(Trait.assmaster) || has(Trait.analFanatic))
                    chance += 5;
                Optional<BodyFetish> fetish = body.getFetish("ass");
                if (fetish.isPresent() && opponent.has(Trait.bewitchingbottom)) {
                    chance += 20 * fetish.get().magnitude;
                }
                if (chance >= Global.random(100)) {
                    c.write(opponent, Global.format("<b>The look of {other:name-possessive} ass,"
                                    + " so easily within {self:possessive} reach, causes"
                                    + " {self:subject} to involuntarily switch to autopilot."
                                    + " {self:SUBJECT} simply {self:action:NEED|NEEDS} that ass.</b>", this, opponent));
                    add(new Frenzied(this, 1));
                }
            }
        }
        
        pleasured = false;
        Optional<PetCharacter> randomOpponentPetOptional = Global.pickRandom(c.getPetsFor(opponent));
        if (randomOpponentPetOptional.isPresent()) {
            PetCharacter pet = randomOpponentPetOptional.get();
            boolean weakenBetter = modifyDamage(DamageType.physical, pet, 100) / pet.getStamina().remaining() 
                            > modifyDamage(DamageType.pleasure, pet, 100) / pet.getStamina().remaining();
            if (canAct() && pet.roll(this, c, 20)) {
                if (weakenBetter) {
                    c.write(Global.format("{self:SUBJECT-ACTION:focus|focuses} {self:possessive} attentions on {other:name-do}, "
                                    + "thoroughly exhausting {other:direct-object} in a game of cat and mouse.", this, pet));
                    pet.weaken(c, (int) modifyDamage(DamageType.physical, pet, Global.random(10, 20)));
                } else {
                    c.write(Global.format("{self:SUBJECT-ACTION:focus|focuses} {self:possessive} attentions on {other:name-do}, "
                                    + "harassing and toying with {other:possessive} body as much as {self:pronoun} can.", this, pet));
                    pet.body.pleasure(this, body.getRandom("hands"), pet.body.getRandomGenital(), modifyDamage(DamageType.pleasure, pet, Global.random(10, 20)), c);
                }
            }
        }
    }

    public String orgasmLiner(Combat c) {
        return "";
    }

    public String makeOrgasmLiner(Combat c) {
        return "";
    }

    private int getOrgasmWillpowerLoss() {
        return 25;
    }

    public abstract void emote(Emotion emo, int amt);

    public void learn(Skill copy) {
        skills.addIfAbsent(copy.copy(this));
    }

    public void forget(Skill copy) {
        getSkills().remove(copy);
    }

    public boolean stealthCheck(int perception) {
        return check(Attribute.Cunning, Global.random(20) + perception) || state == State.hidden;
    }

    public boolean spotCheck(int perception) {
        if (state == State.hidden) {
            int dc = perception + 10;
            if (has(Trait.Sneaky)) {
                dc -= 5;
            }
            return check(Attribute.Cunning, dc);
        } else {
            return true;
        }
    }

    public void travel(Area dest) {
        state = State.ready;
        location.exit(this);
        location = dest;
        dest.enter(this);
        if (dest.name.isEmpty()) {
            throw new RuntimeException("empty location");
        }
    }

    public void flee(Area location2) {
        Area[] adjacent = location2.adjacent.toArray(new Area[location2.adjacent.size()]);
        travel(adjacent[Global.random(adjacent.length)]);
        location2.endEncounter();
    }

    public void upkeep() {
        status.removeAll(status.stream().filter(s -> !s.lingering()).collect(Collectors.toSet()));
        getTraits().forEach(trait -> {
            if (trait.status != null) {
                Status newStatus = trait.status.instance(this, null);
                if (!has(newStatus)) {
                    add(newStatus);
                }
            }
        });
        regen();
        tick(null);
        if (has(Trait.Confident)) {
            willpower.restore(10);
            mojo.reduce(5);
        } else {
            willpower.restore(5);
            mojo.reduce(10);
        }
        if (has(Trait.exhibitionist) && mostlyNude()) {
            mojo.restore(2);
        }
        if (bound()) {
            free();
        }
        dropStatus(null, null);
        if (has(Trait.QuickRecovery)) {
            heal(null, Global.random(1, 3));
        }
        update();
        notifyObservers();
    }
    
    public String debugMessage(Combat c, Position p) {
        String mood;
        if (this instanceof NPC) { // useOfInstanceOfWithThis
            mood = "mood: " + ((NPC) this).mood.toString();
        } else {
            mood = "";
        }
        return String.format("[%s] %s s: %d/%d a: %d/%d m: %d/%d w: %d/%d c:%d f:%f", name, mood, stamina.getReal(),
                        stamina.max(), arousal.getReal(), arousal.max(), mojo.getReal(), mojo.max(),
                        willpower.getReal(), willpower.max(), outfit.getEquipped().size(), getFitness(c));
    }

    public void gain(Item item) {
        gain(item, 1);
    }

    public void remove(Item item) {
        gain(item, -1);
    }

    public void gain(Clothing item) {
        closet.add(item);
        setChanged();
    }

    public void gain(Item item, int q) {
        int amt = 0;
        if (inventory.containsKey(item)) {
            amt = count(item);
        }
        inventory.put(item, Math.max(0, amt + q));
        setChanged();
    }

    public boolean has(Item item) {
        return has(item, 1);
    }

    public boolean has(Item item, int quantity) {
        return inventory.containsKey(item) && inventory.get(item) >= quantity;
    }

    public void unequipAllClothing() {
        closet.addAll(outfitPlan);
        outfitPlan.clear();
        change();
    }

    public boolean has(Clothing item) {
        return closet.contains(item) || outfit.getEquipped().contains(item);
    }

    public void consume(Item item, int quantity) {
        consume(item, quantity, true);
    }

    public void consume(Item item, int quantity, boolean canBeResourceful) {
        if (canBeResourceful && has(Trait.resourceful) && Global.random(5) == 0) {
            quantity--;
        }
        if (inventory.containsKey(item)) {
            gain(item, -quantity);
        }
    }

    public int count(Item item) {
        if (inventory.containsKey(item)) {
            return inventory.get(item);
        }
        return 0;
    }

    public void chargeBattery() {
        int power = count(Item.Battery);
        if (power < 20) {
            gain(Item.Battery, 20 - power);
        }
    }

    public void defeated(Character victor) {
        mercy.addIfAbsent(victor.getType());
    }

    public void resupply() {
        for (String victorType : mercy) {
            Character victor = Global.getCharacterByType(victorType);
            victor.bounty(has(Trait.event) ? 5 : 1, victor);
        }
        mercy.clear();
        change();
        state = State.ready;
        getWillpower().fill();
        if (location().present.size() > 1) {
            if (location().id() == Movement.dorm) {
                if (Global.getMatch().gps("Quad").present.isEmpty()) {
                    if (human()) {
                        Global.gui()
                                        .message("You hear your opponents searching around the dorm, so once you finish changing, you hop out the window and head to the quad.");
                    }
                    travel(Global.getMatch().gps("Quad"));
                } else {
                    if (human()) {
                        Global.gui()
                                        .message("You hear your opponents searching around the dorm, so once you finish changing, you quietly move downstairs to the laundry room.");
                    }
                    travel(Global.getMatch().gps("Laundry"));
                }
            }
            if (location().id() == Movement.union) {
                if (Global.getMatch().gps("Quad").present.isEmpty()) {
                    if (human()) {
                        Global.gui()
                                        .message("You don't want to be ambushed leaving the student union, so once you finish changing, you hop out the window and head to the quad.");
                    }
                    travel(Global.getMatch().gps("Quad"));
                } else {
                    if (human()) {
                        Global.gui()
                                        .message("You don't want to be ambushed leaving the student union, so once you finish changing, you sneak out the back door and head to the pool.");
                    }
                    travel(Global.getMatch().gps("Pool"));
                }
            }
        }
    }

    public void finishMatch() {
        for (String victorType : mercy) {
            Character victor = Global.getCharacterByType(victorType);
            victor.bounty(has(Trait.event) ? 5 : 1, victor);
        }
        Global.gui().clearImage();
        mercy.clear();
        change();
        clearStatus();
        temporaryAddedTraits.clear();
        temporaryRemovedTraits.clear();
        body.clearReplacements();
        getStamina().fill();
        getArousal().empty();
        getMojo().empty();
    }

    public void place(Area loc) {
        location = loc;
        loc.present.add(this);
        if (loc.name.isEmpty()) {
            throw new RuntimeException("empty location");
        }
    }

    public void bounty(int points, Character victor) {
        int score = points;
        if (Global.checkFlag(Flag.FTC) && points == 1) {
            FTCMatch match = (FTCMatch) Global.getMatch();
            if (match.isPrey(this)) {
                score = 3;
            } else if (!match.isPrey(victor)) {
                score = 2;
            } else {
                score = 0; // Hunter beating prey gets no points, only for flag.
            }
        }
        Global.getMatch().score(this, score);
    }

    public boolean eligible(Character p2) {
        boolean ftc = true;
        if (Global.checkFlag(Flag.FTC)) {
            FTCMatch match = (FTCMatch) Global.getMatch();
            ftc = !match.inGracePeriod() || (!match.isPrey(this) && !match.isPrey(p2));
        }
        return ftc && !mercy.contains(p2.getType()) && state != State.resupplying;
    }

    public void setTrophy(Item trophy) {
        this.trophy = trophy;
    }

    public Item getTrophy() {
        return trophy;
    }

    public void bathe() {
        status.clear();
        stamina.fill();
        state = State.ready;
        setChanged();
    }

    public void masturbate() {
        arousal.empty();
        state = State.ready;
        setChanged();
    }

    public void craft() {
        int roll = Global.random(15);
        if (check(Attribute.Cunning, 25)) {
            if (roll == 9) {
                gain(Item.Aphrodisiac);
                gain(Item.DisSol);
            } else if (roll >= 5) {
                gain(Item.Aphrodisiac);
            } else {
                gain(Item.Lubricant);
                gain(Item.Sedative);
            }
        } else if (check(Attribute.Cunning, 20)) {
            if (roll == 9) {
                gain(Item.Aphrodisiac);
            } else if (roll >= 7) {
                gain(Item.DisSol);
            } else if (roll >= 5) {
                gain(Item.Lubricant);
            } else if (roll >= 3) {
                gain(Item.Sedative);
            } else {
                gain(Item.EnergyDrink);
            }
        } else if (check(Attribute.Cunning, 15)) {
            if (roll == 9) {
                gain(Item.Aphrodisiac);
            } else if (roll >= 8) {
                gain(Item.DisSol);
            } else if (roll >= 7) {
                gain(Item.Lubricant);
            } else if (roll >= 6) {
                gain(Item.EnergyDrink);
            }
        } else {
            if (roll >= 7) {
                gain(Item.Lubricant);
            } else if (roll >= 5) {
                gain(Item.Sedative);
            }
        }
        state = State.ready;
        setChanged();
    }

    public void search() {
        int roll = Global.random(15);
        switch (roll) {
            case 9:
                gain(Item.Tripwire);
                gain(Item.Tripwire);
                break;
            case 8:
                gain(Item.ZipTie);
                gain(Item.ZipTie);
                gain(Item.ZipTie);
                break;
            case 7:
                gain(Item.Phone);
                break;
            case 6:
                gain(Item.Rope);
                break;
            case 5:
                gain(Item.Spring);
        }
        state = State.ready;

    }

    public abstract String challenge(Character other);

    public void delay(int i) {
        busy += i;
    }

    public abstract void promptTrap(IEncounter fight, Character target, Trap trap);

    public int lvlBonus(Character opponent) {
        if (opponent.getLevel() > getLevel()) {
            return 10 * (opponent.getLevel() - getLevel());
        } else {
            return 0;
        }
    }

    public int getVictoryXP(Character opponent) {
        return 15 + lvlBonus(opponent);
    }

    public int getAssistXP(Character opponent) {
        return 10 + lvlBonus(opponent);
    }

    public int getDefeatXP(Character opponent) {
        return 10 + lvlBonus(opponent);
    }

    public int getAttraction(Character other) {
        if (other == null) {
            System.err.println("Other is null");
            Thread.dumpStack();
            return 0;
        }
        if (attractions.containsKey(other.getType())) {
            return attractions.get(other.getType());
        } else {
            return 0;
        }
    }

    public void gainAttraction(Character other, int x) {
        if (other == null) {
            System.err.println("Other is null");
            Thread.dumpStack();
            return;
        }
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.printf("%s gained attraction for %s\n", name(), other.name());
        }
        if (attractions.containsKey(other.getType())) {
            attractions.put(other.getType(), attractions.get(other.getType()) + x);
        } else {
            attractions.put(other.getType(), x);
        }
    }

    public Map<String, Integer> getAffections() {
        return Collections.unmodifiableMap(affections);
    }

    public int getAffection(Character other) {
        if (other == null) {
            System.err.println("Other is null");
            Thread.dumpStack();
            return 0;
        }

        if (affections.containsKey(other.getType())) {
            return affections.get(other.getType());
        } else {
            return 0;
        }
    }

    public void gainAffection(Character other, int x) {
        if (other == null) {
            System.err.println("Other is null");
            Thread.dumpStack();
            return;
        }
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.printf("%s gained %d affection for %s\n", name(), x, other.name());
        }
        if (affections.containsKey(other.getType())) {
            affections.put(other.getType(), affections.get(other.getType()) + x);
        } else {
            affections.put(other.getType(), x);
        }
    }

    public int evasionBonus() {
        int ac = 0;
        for (Status s : getStatuses()) {
            ac += s.evade();
        }
        if (has(Trait.clairvoyance)) {
            ac += 5;
        }
        return ac;
    }

    private Collection<Status> getStatuses() {
        return status;
    }

    public int counterChance(Combat c, Character opponent, Skill skill) {
        int counter = 3;
        // subtract some counter chance if the opponent is more cunning than you.
        // 1% decreased counter chance per 5 points of cunning over you.
        counter += Math.min(0, get(Attribute.Cunning) - opponent.get(Attribute.Cunning)) / 5;
        // increase counter chance by perception difference
        counter += get(Attribute.Perception) - opponent.get(Attribute.Perception);
        // 1% increased counter chance per 2 speed over your opponent.
        counter += getSpeedDifference(opponent) / 2;
        for (Status s : getStatuses()) {
            counter += s.counter();
        }
        if (has(Trait.clairvoyance)) {
            counter += 3;
        }
        if (has(Trait.aikidoNovice)) {
            counter += 3;
        }
        if (has(Trait.fakeout)) {
            counter += 3;
        }
        if (opponent.is(Stsflag.countered)) {
            counter -= 10;
        }
        // Maximum counter chance is 3 + 5 + 2 + 3 + 3 + 3 = 19, which is super hard to achieve.
        // I guess you also get some more counter with certain statuses effects like water form.
        // Counters should be pretty rare.
        return Math.max(0, counter);
    }

    private int getSpeedDifference(Character opponent) {
        return Math.min(Math.max(get(Attribute.Speed) - opponent.get(Attribute.Speed), -5), 5);
    }

    public boolean roll(Character attacker, Combat c, int accuracy) {
        int hitDiff = attacker.getSpeedDifference(this) + (attacker.get(Attribute.Perception) - get(
                        Attribute.Perception));
        int levelDiff = Math.min(attacker.level - level, 5);
        levelDiff = Math.max(attacker.level - level, -5);
        int attackroll = Global.random(100);

        // with no level or hit differences and an default accuracy of 80, 80%
        // hit rate
        // each level the attacker is below the target will reduce this by 2%,
        // to a maximum of 10%
        // each point in accuracy of skill affects changes the hit chance by 1%
        // each point in speed and perception will increase hit by 5%
        int chanceToHit = 2 * levelDiff + accuracy + 5 * (hitDiff - evasionBonus());
        if (has(Trait.hawkeye)) {
            chanceToHit += 5;
        }
        if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
            System.out.printf("Rolled %s against %s, base accuracy: %s, hit difference: %s, level difference: %s\n",
                            attackroll, chanceToHit, accuracy, hitDiff, levelDiff);
        }

        return attackroll < chanceToHit;
    }

    public int knockdownDC() {
        int dc = 10 + getStamina().get() / 2;
        if (is(Stsflag.braced)) {
            dc += getStatus(Stsflag.braced).value();
        }
        if (has(ClothingTrait.heels) && !has(Trait.proheels)) {
            dc -= 7;
        }
        if (has(ClothingTrait.highheels) && !has(Trait.proheels)) {
            dc -= 8;
        }
        if (has(ClothingTrait.higherheels) && !has(Trait.proheels)) {
            dc -= 10;
        }
        return dc;
    }

    public abstract void counterattack(Character target, Tactics type, Combat c);

    public void clearStatus() {
        status.clear();
    }

    public Status getStatus(Stsflag flag) {
        for (Status s : getStatuses()) {
            if (s.flags().contains(flag)) {
                return s;
            }
        }
        return null;
    }

    public Integer prize() {
        if (getRank() >= 2) {
            return 500;
        } else if (getRank() == 1) {
            return 200;
        } else {
            return 50;
        }
    }

    public Move findPath(Area target) {
        if (location.name.equals(target.name)) {
            return null;
        }
        ArrayDeque<Area> queue = new ArrayDeque<>();
        Vector<Area> vector = new Vector<>();
        HashMap<Area, Area> parents = new HashMap<>();
        queue.push(location);
        vector.add(location);
        Area last = null;
        while (!queue.isEmpty()) {
            Area t = queue.pop();
            parents.put(t, last);
            if (t.name.equals(target.name)) {
                while (!location.adjacent.contains(t)) {
                    t = parents.get(t);
                }
                return new Move(t);
            }
            for (Area area : t.adjacent) {
                if (!vector.contains(area)) {
                    vector.add(area);
                    queue.push(area);
                }
            }
            last = t;
        }
        return null;
    }

    public boolean knows(Skill skill) {
        for (Skill s : getSkills()) {
            if (s.equals(skill)) {
                return true;
            }
        }
        return false;
    }

    public void endofbattle() {
        for (Status s : status) {
            if (!s.lingering()) {
                removelist.add(s);
            }
        }
        cooldowns.clear();
        dropStatus(null, null);
        orgasms = 0;
        setChanged();
        if (has(ClothingTrait.heels)) {
            setFlag("heelsTraining", getFlag("heelsTraining") + 1);
        }
        if (has(ClothingTrait.highheels)) {
            setFlag("heelsTraining", getFlag("heelsTraining") + 1);
        }
        if (has(ClothingTrait.higherheels)) {
            setFlag("heelsTraining", getFlag("heelsTraining") + 1);
        }
    }

    public void setFlag(String string, int i) {
        flags.put(string, i);
    }

    public int getFlag(String string) {
        if (flags.containsKey(string)) {
            return flags.get(string);
        }
        return 0;
    }

    public boolean canSpend(int mojo) {
        int cost = mojo;
        for (Status s : getStatuses()) {
            cost += s.spendmojo(mojo);
        }
        return getMojo().get() >= cost;
    }

    public Map<Item, Integer> getInventory() {
        return inventory;
    }

    public ArrayList<String> listStatus() {
        ArrayList<String> result = new ArrayList<>();
        for (Status s : getStatuses()) {
            result.add(s.toString());
        }
        return result;
    }

    public String dumpstats(boolean notableOnly) {
        StringBuilder b = new StringBuilder();
        b.append("<b>");
        b.append(name() + ": Level " + getLevel() + "; ");
        for (Attribute a : att.keySet()) {
            b.append(a.name() + " " + att.get(a) + ", ");
        }
        b.append("</b>");
        b.append("<br>Max Stamina " + stamina.max() + ", Max Arousal " + arousal.max() + ", Max Mojo " + mojo.max()
                        + ", Max Willpower " + willpower.max() + ".");
        b.append("<br>");
        body.describeBodyText(b, this, notableOnly);
        if (getTraits().size() > 0) {
            b.append("<br>Traits:<br>");
            List<Trait> traits = new ArrayList<>(getTraits());
            traits.sort((first, second) -> first.toString().compareTo(second.toString()));
            for (Trait t : traits) {
                b.append(t + ": " + t.getDesc());
                b.append("<br>");
            }
        }
        b.append("</p>");

        return b.toString();
    }

    public void accept(Challenge c) {
        challenges.add(c);
    }

    public void evalChallenges(Combat c, Character victor) {
        for (Challenge chal : challenges) {
            chal.check(c, victor);
        }
    }

    public String toString() {
        return getType();
    }

    public boolean taintedFluids() {
        return Global.random(get(Attribute.Dark) / 4 + 5) >= 4;
    }

    protected void pickSkillsWithGUI(Combat c, Character target) {
        if (Global.isDebugOn(DebugFlags.DEBUG_SKILL_CHOICES)) {
            c.write(this, nameOrPossessivePronoun() + " turn...");
            c.updateAndClearMessage();
        }
        HashSet<Skill> available = new HashSet<>();
        HashSet<Skill> cds = new HashSet<>();
        for (Skill a : getSkills()) {
            if (Skill.skillIsUsable(c, a)) {
                if (cooldownAvailable(a)) {
                    available.add(a);
                } else {
                    cds.add(a);
                }
            }
        }
        HashSet<Skill> damage = new HashSet<>();
        HashSet<Skill> pleasure = new HashSet<>();
        HashSet<Skill> fucking = new HashSet<>();
        HashSet<Skill> position = new HashSet<>();
        HashSet<Skill> debuff = new HashSet<>();
        HashSet<Skill> recovery = new HashSet<>();
        HashSet<Skill> summoning = new HashSet<>();
        HashSet<Skill> stripping = new HashSet<>();
        HashSet<Skill> misc = new HashSet<>();
        Skill.filterAllowedSkills(c, available, this, target);
        if (available.size() == 0) {
            available.add(new Nothing(this));
        }
        available.addAll(cds);
        Global.gui().clearCommand();
        Skill lastUsed = null;
        for (Skill a : available) {
            if (a.getName().equals(c.getCombatantData(this).getLastUsedSkillName())) {
                lastUsed = a;
            } else if (a.type(c) == Tactics.damage) {
                damage.add(a);
            } else if (a.type(c) == Tactics.pleasure) {
                pleasure.add(a);
            } else if (a.type(c) == Tactics.fucking) {
                fucking.add(a);
            } else if (a.type(c) == Tactics.positioning) {
                position.add(a);
            } else if (a.type(c) == Tactics.debuff) {
                debuff.add(a);
            } else if (a.type(c) == Tactics.recovery || a.type(c) == Tactics.calming) {
                recovery.add(a);
            } else if (a.type(c) == Tactics.summoning) {
                summoning.add(a);
            } else if (a.type(c) == Tactics.stripping) {
                stripping.add(a);
            } else {
                misc.add(a);
            }
        }
        if (lastUsed != null) {
            Global.gui().addSkill(lastUsed, c);
        }
        for (Skill a : stripping) {
            Global.gui().addSkill(a, c);
        }
        for (Skill a : position) {
            Global.gui().addSkill(a, c);
        }
        for (Skill a : fucking) {
            Global.gui().addSkill(a, c);
        }
        for (Skill a : pleasure) {
            Global.gui().addSkill(a, c);
        }
        for (Skill a : damage) {
            Global.gui().addSkill(a, c);
        }
        for (Skill a : debuff) {
            Global.gui().addSkill(a, c);
        }
        for (Skill a : summoning) {
            Global.gui().addSkill(a, c);
        }
        for (Skill a : recovery) {
            Global.gui().addSkill(a, c);
        }
        for (Skill a : misc) {
            Global.gui().addSkill(a, c);
        }
        Global.gui().showSkills(0);
    }

    public float getOtherFitness(Combat c, Character other) {
        float fit = 0;
        // Urgency marks
        float arousalMod = 1.0f;
        float staminaMod = 1.0f;
        float mojoMod = 1.0f;
        float usum = arousalMod + staminaMod + mojoMod;
        int escape = other.getEscape(c, this);
        if (escape > 1) {
            fit += 8 * Math.log(escape);
        } else if (escape < -1) {
            fit += -8 * Math.log(-escape);
        }
        int totalAtts = 0;
        for (Attribute attribute : att.keySet()) {
            totalAtts += att.get(attribute);
        }
        fit += Math.sqrt(totalAtts) * 5;

        // what an average piece of clothing should be worth in fitness
        double topFitness = 4.0;
        double bottomFitness = 4.0;
        // If I'm horny, I want the other guy's clothing off, so I put more
        // fitness in them
        if (getMood() == Emotion.horny) {
            topFitness = 8;
            topFitness = 6;
            // If I'm horny, I want to make the opponent cum asap, put more
            // emphasis on arousal
            arousalMod = 2.0f;
        }

        // check body parts based on my preferences
        if (other.hasDick()) {
            fit -= (dickPreference() - 3) * 4;
        }
        if (other.hasPussy()) {
            fit -= (pussyPreference() - 3) * 4;
        }

        fit += other.outfit.getFitness(c, bottomFitness, topFitness);
        fit += other.body.getCharismaBonus(this);
        // Extreme situations
        if (other.arousal.isFull()) {
            fit -= 50;
        }
        // will power empty is a loss waiting to happen
        if (other.willpower.isEmpty()) {
            fit -= 100;
        }
        if (other.stamina.isEmpty()) {
            fit -= staminaMod * 3;
        }
        fit += other.getWillpower().getReal() * 5.33f;
        // Short-term: Arousal
        fit += arousalMod / usum * 100.0f * (other.getArousal().max() - other.getArousal().get()) / Math
                        .min(100, other.getArousal().max());
        // Mid-term: Stamina
        fit += staminaMod / usum * 50.0f * (1 - Math
                        .exp(-((float) other.getStamina().get()) / Math.min(other.getStamina().max(), 100.0f)));
        // Long term: Mojo
        fit += mojoMod / usum * 50.0f * (1 - Math
                        .exp(-((float) other.getMojo().get()) / Math.min(other.getMojo().max(), 40.0f)));
        for (Status status : other.getStatuses()) {
            fit += status.fitnessModifier();
        }
        // hack to make the AI favor making the opponent cum
        fit -= 100 * other.orgasms;
        // special case where if you lost, you are super super unfit.
        if (other.orgasmed && other.getWillpower().isEmpty()) {
            fit -= 1000;
        }
        return fit;
    }

    public float getFitness(Combat c) {

        float fit = 0;
        // Urgency marks
        float arousalMod = 1.0f;
        float staminaMod = 2.0f;
        float mojoMod = 1.0f;
        float usum = arousalMod + staminaMod + mojoMod;
        Character other = c.getOpponent(this);

        int totalAtts = 0;
        for (Attribute attribute : att.keySet()) {
            totalAtts += att.get(attribute);
        }
        fit += Math.sqrt(totalAtts) * 5;
        // Always important: Position
        fit += (c.getStance().priorityMod(this) + c.getStance().getDominanceOfStance(this)) * 4;

        int escape = getEscape(c, other);
        if (escape > 1) {
            fit += 8 * Math.log(escape);
        } else if (escape < -1) {
            fit += -8 * Math.log(-escape);
        }
        // what an average piece of clothing should be worth in fitness
        double topFitness = 4.0;
        double bottomFitness = 4.0;
        // If I'm horny, I don't care about my clothing, so I put more less
        // fitness in them
        if (getMood() == Emotion.horny) {
            topFitness = 1;
            topFitness = 1;
            // If I'm horny, I put less importance on my own arousal
            arousalMod = .7f;
        }
        fit += outfit.getFitness(c, bottomFitness, topFitness);
        fit += body.getCharismaBonus(other);
        if (c.getStance().inserted()) { // If we are fucking...
            // ...we need to see if that's beneficial to us.
            fit += body.penetrationFitnessModifier(this, other, c.getStance().inserted(this),
                            c.getStance().anallyPenetrated(c));
        }
        if (hasDick()) {
            fit += (dickPreference() - 3) * 4;
        }

        if (hasPussy()) {
            fit += (pussyPreference() - 3) * 4;
        }
        if (has(Trait.pheromones)) {
            fit += 5 * getPheromonePower();
            fit += 15 * getPheromonesChance(c) * (2 + getPheromonePower());
        }

        // Also somewhat of a factor: Inventory (so we don't
        // just use it without thinking)
        for (Item item : inventory.keySet()) {
            fit += (float) item.getPrice() / 10;
        }
        // Extreme situations
        if (arousal.isFull()) {
            fit -= 100;
        }
        if (stamina.isEmpty()) {
            fit -= staminaMod * 3;
        }
        fit += getWillpower().getReal() * 5.3f;
        // Short-term: Arousal
        fit += arousalMod / usum * 100.0f * (getArousal().max() - getArousal().get())
                        / Math.min(100, getArousal().max());
        // Mid-term: Stamina
        fit += staminaMod / usum * 50.0f
                        * (1 - Math.exp(-((float) getStamina().get()) / Math.min(getStamina().max(), 100.0f)));
        // Long term: Mojo
        fit += mojoMod / usum * 50.0f * (1 - Math.exp(-((float) getMojo().get()) / Math.min(getMojo().max(), 40.0f)));
        for (Status status : getStatuses()) {
            fit += status.fitnessModifier();
        }

        if (this instanceof NPC) {
            NPC me = (NPC) this;
            AiModifiers mods = me.ai.getAiModifiers();
            fit += mods.modPosition(c.getStance().enumerate()) * 6;
            fit += status.stream().flatMap(s -> s.flags().stream()).mapToDouble(mods::modSelfStatus).sum();
            fit += c.getOpponent(this).status.stream().flatMap(s -> s.flags().stream())
                            .mapToDouble(mods::modOpponentStatus).sum();
        }
        // hack to make the AI favor making the opponent cum
        fit -= 100 * orgasms;
        // special case where if you lost, you are super super unfit.
        if (orgasmed && getWillpower().isEmpty()) {
            fit -= 1000;
        }
        return fit;
    }

    public String nameOrPossessivePronoun() {
        return name + "'s";
    }

    public double getExposure(ClothingSlot slot) {
        return outfit.getExposure(slot);
    }

    public double getExposure() {
        return outfit.getExposure();
    }

    public abstract String getPortrait(Combat c);

    public void modMoney(int i) {
        setMoney((int) (money + Math.round(i * Global.moneyRate)));
    }

    public void setMoney(int i) {
        money = i;
        update();
    }

    public void loseXP(int i) {
        assert i >= 0;
        xp -= i;
        update();
    }

    public String pronoun() {
        if (useFemalePronouns()) {
            return "she";
        } else {
            return "he";
        }
    }

    public Emotion getMood() {
        return Emotion.confident;
    }

    public String possessivePronoun() {
        if (useFemalePronouns()) {
            return "her";
        } else {
            return "his";
        }
    }

    public String directObject() {
        if (useFemalePronouns()) {
            return "her";
        } else {
            return "him";
        }
    }

    public boolean useFemalePronouns() {
        return hasPussy() 
                        || !hasDick() 
                        || (body.getLargestBreasts().size > BreastsPart.flat.size && body.getFace().getFemininity(this) > 0) 
                        || (body.getFace().getFemininity(this) >= 1.5) 
                        || Global.checkFlag(Flag.FemalePronounsOnly);
    }

    public String nameDirectObject() {
        return name();
    }

    public String reflectivePronoun() {
        return possessivePronoun() + "self";
    }

    public boolean clothingFuckable(BodyPart part) {
        if (part.isType("strapon")) {
            return true;
        }
        if (part.isType("cock")) {
            return outfit.slotEmptyOrMeetsCondition(ClothingSlot.bottom,
                            (article) -> (!article.is(ClothingTrait.armored) && !article.is(ClothingTrait.bulky)));
        } else if (part.isType("pussy") || part.isType("ass")) {
            return outfit.slotEmptyOrMeetsCondition(ClothingSlot.bottom, (article) -> {
                return article.is(ClothingTrait.skimpy) || article.is(ClothingTrait.open)
                                || article.is(ClothingTrait.flexible);
            });
        } else {
            return false;
        }
    }

    public double pussyPreference() {
        return 11 - Global.getValue(Flag.malePref);
    }

    public double dickPreference() {
        return Global.getValue(Flag.malePref);
    }

    public boolean wary() {
        return hasStatus(Stsflag.wary);
    }

    public void gain(Combat c, Item item) {
        if (c != null) {
            c.write(Global.format("<b>{self:subject-action:have|has} gained " + item.pre() + item.getName() + "</b>",
                            this, this));
        }
        gain(item, 1);
    }

    public String temptLiner(Combat c, Character target) {
        return Global.format("{self:SUBJECT-ACTION:tempt|tempts} {other:direct-object}.", this, target);
    }

    public String action(String firstPerson, String thirdPerson) {
        return thirdPerson;
    }
    
    public String action(String verb) {
        return action(verb, verb + "s");
    }

    public void addCooldown(Skill skill) {
        if (skill.getCooldown() <= 0) {
            return;
        }
        if (cooldowns.containsKey(skill.toString())) {
            cooldowns.put(skill.toString(), cooldowns.get(skill.toString()) + skill.getCooldown());
        } else {
            cooldowns.put(skill.toString(), skill.getCooldown());
        }
    }

    public boolean cooldownAvailable(Skill s) {
        boolean cooledDown = true;
        if (cooldowns.containsKey(s.toString()) && cooldowns.get(s.toString()) > 0) {
            cooledDown = false;
        }
        return cooledDown;
    }

    public Integer getCooldown(Skill s) {
        if (cooldowns.containsKey(s.toString()) && cooldowns.get(s.toString()) > 0) {
            return cooldowns.get(s.toString());
        } else {
            return 0;
        }
    }

    public boolean checkLoss(Combat c) {
        return (orgasmed || c.getTimer() > 150) && willpower.isEmpty();
    }

    public boolean isCustomNPC() {
        return custom;
    }

    public String recruitLiner() {
        return "";
    }

    public int stripDifficulty(Character other) {
        if (outfit.has(ClothingTrait.tentacleSuit) || outfit.has(ClothingTrait.tentacleUnderwear)) {
            return other.get(Attribute.Science) + 20;
        }
        return 0;
    }

    public void startBattle(Combat combat) {
        orgasms = 0;
    }

    public void drainStaminaAsMojo(Combat c, Character drainer, int i, float efficiency) {
        int drained = i;
        int bonus = 0;

        for (Status s : getStatuses()) {
            bonus += s.drained(drained);
        }
        drained += bonus;
        if (drained >= stamina.get()) {
            drained = stamina.get();
        }
        drained = Math.max(1, drained);
        int restored = Math.round(drained * efficiency);
        if (c != null) {
            c.writeSystemMessage(
                            String.format("%s drained of <font color='rgb(240,162,100)'>%d<font color='white'> stamina as <font color='rgb(100,162,240)'>%d<font color='white'> mojo by %s",
                                            subjectWas(), drained, restored, drainer.subject()));
        }
        stamina.reduce(drained);
        drainer.mojo.restore(restored);
    }

    public void drainMojo(Combat c, Character drainer, int i) {
        int drained = i;
        int bonus = 0;

        for (Status s : getStatuses()) {
            bonus += s.drained(drained);
        }
        drained += bonus;
        if (drained >= mojo.get()) {
            drained = mojo.get();
        }
        drained = Math.max(1, drained);
        if (c != null) {
            c.writeSystemMessage(
                            String.format("%s drained of <font color='rgb(0,162,240)'>%d<font color='white'> mojo by %s",
                                            subjectWas(), drained, drainer.subject()));
        }
        mojo.reduce(drained);
        drainer.mojo.restore(drained);
    }

    public void update() {
        setChanged();
        notifyObservers();
    }

    public Outfit getOutfit() {
        return outfit;
    }

    public boolean footAvailable() {
        Clothing article = outfit.getTopOfSlot(ClothingSlot.feet);
        return article == null || article.getLayer() < 2;
    }

    public boolean hasInsertable() {
        return hasDick() || has(Trait.strapped);
    }

    public String guyOrGirl() {
        return useFemalePronouns() ? "girl" : "guy";
    }

    public String boyOrGirl() {
        return useFemalePronouns() ? "girl" : "boy";
    }

    public boolean isDemonic() {
        return has(Trait.succubus) || body.get("pussy").stream()
                        .anyMatch(part -> part.moddedPartCountsAs(this, PussyPart.succubus)) || body.get("cock")
                        .stream().anyMatch(part -> part.moddedPartCountsAs(this, CockMod.incubus));
    }

    public int baseDisarm() {
        int disarm = 0;
        if (has(Trait.cautious)) {
            disarm += 5;
        }
        return disarm;
    }

    public float modRecoilPleasure(Combat c, float mt) {
        float total = mt;
        if (c.getStance().sub(this)) {
            total += get(Attribute.Submissive) / 2;
        }
        if (has(Trait.responsive)) {
            total += total / 2;
        }
        return total;
    }

    public boolean isPartProtected(BodyPart target) {
        return target.isType("hands") && has(ClothingTrait.nursegloves);
    }

    public void purge(Combat c) {
        temporaryAddedTraits.clear();
        temporaryRemovedTraits.clear();
        body.purge(c);
        status = new HashSet<>(status.stream().filter(s -> !s.flags().contains(Stsflag.purgable))
                        .collect(Collectors.toSet()));
    }

    /**
     * applies bonuses and penalties for using an attribute.
     */
    public void usedAttribute(Attribute att, Combat c, double baseChance) {
        // divine recoil applies at 20% per magnitude
        if (att == Attribute.Divinity && Global.randomdouble() < baseChance) {
            add(c, new DivineRecoil(this, 1));
        }
    }

    /**
     * Attempts to knock down this character
     */
    public void knockdown(Combat c, Character other, Set<Attribute> attributes, int strength, int roll) {
        if (canKnockDown(c, other, attributes, strength, roll)) {
            add(c, new Falling(this));
        }
    }

    public int knockdownBonus() {
        return 0;
    }

    public boolean canKnockDown(Combat c, Character other, Set<Attribute> attributes, int strength, double roll) {
        return knockdownDC() < strength + (roll * 100) + attributes.stream().mapToInt(other::get).sum() + other
                        .knockdownBonus();
    }

    public boolean checkResists(ResistType type, Character other, double value, double roll) {
        switch (type) {
            case mental:
                return value < roll * 100;
            default:
                return false;
        }
    }

    /**
     * If true, count insertions by this character as voluntary
     */
    public boolean canMakeOwnDecision() {
        return !is(Stsflag.charmed) && !is(Stsflag.lovestruck) && !is(Stsflag.frenzied);
    }

    public String printStats() {
        return "Character{" + "name='" + name + '\'' + ", type=" + getType() + ", level=" + level + ", xp=" + xp
                        + ", rank=" + rank + ", money=" + money + ", att=" + att + ", stamina=" + stamina.max()
                        + ", arousal=" + arousal.max() + ", mojo=" + mojo.max() + ", willpower=" + willpower.max()
                        + ", outfit=" + outfit + ", traits=" + traits + ", inventory=" + inventory + ", flags=" + flags
                        + ", trophy=" + trophy + ", closet=" + closet + ", body=" + body + ", availableAttributePoints="
                        + availableAttributePoints + '}';
    }

    public int getMaxWillpowerPossible() {
        return Integer.MAX_VALUE;
    }

    public boolean levelUpIfPossible() {
        int req;
        boolean dinged = false;
        while (xp > (req = getXPReqToNextLevel())) {
            xp -= req;
            ding();
            dinged = true;
        }
        return dinged;
    }

    public void matchPrep(Match m) {
        if(getPure(Attribute.Ninjutsu)>=9){
            Global.gainSkills(this);
            placeNinjaStash(m);
        }

    }

    private void placeNinjaStash(Match m) {
        String location;
        switch(Global.random(6)){
        case 0:
            location = "Library";
            break;
        case 1:
            location = "Dining";
            break;
        case 2:
            location = "Lab";
            break;
        case 3:
            location = "Workshop";
            break;
        case 4:
            location = "Storage";
            break;
        default:
            location = "Liberal Arts";
            break;
        }
        m.gps(location).place(new NinjaStash(this));
        if(human()){
            Global.gui().message("<b>You've arranged for a hidden stash to be placed in the "+m.gps(location).name+".</b>");
        }
    }

    public boolean hasSameStats(Character character) {
        if (!name.equals(character.name)) {
            return false;
        }
        if (!getType().equals(character.getType())) {
            return false;
        }
        if (!(level == character.level)) {
            return false;
        }
        if (!(xp == character.xp)) {
            return false;
        }
        if (!(rank == character.rank)) {
            return false;
        }
        if (!(money == character.money)) {
            return false;
        }
        if (!att.equals(character.att)) {
            return false;
        }
        if (!(stamina.max() == character.stamina.max())) {
            return false;
        }
        if (!(arousal.max() == character.arousal.max())) {
            return false;
        }
        if (!(mojo.max() == character.mojo.max())) {
            return false;
        }
        if (!(willpower.max() == character.willpower.max())) {
            return false;
        }
        if (!outfit.equals(character.outfit)) {
            return false;
        }
        if (!(new HashSet<>(traits).equals(new HashSet<>(character.traits)))) {
            return false;
        }
        if (!inventory.equals(character.inventory)) {
            return false;
        }
        if (!flags.equals(character.flags)) {
            return false;
        }
        if (!trophy.equals(character.trophy)) {
            return false;
        }
        if (!closet.equals(character.closet)) {
            return false;
        }
        if (!body.equals(character.body)) {
            return false;
        }
        return availableAttributePoints == character.availableAttributePoints;

    }

    public void flagStatus(Stsflag flag) {
        statusFlags.add(flag);
    }
    
    public void unflagStatus(Stsflag flag) {
        statusFlags.remove(flag);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Character character = (Character) o;

        return getType().equals(character.getType()) && name.equals(character.name);

    }

    @Override public int hashCode() {
        int result = getType().hashCode();
        return result * 31 + name.hashCode();
    }

    public Growth getGrowth() {
        return growth;
    }

    public void setGrowth(Growth growth) {
        this.growth = growth;
    }
    public Collection<Skill> getSkills() {
        return skills;
    }

    public void distributePoints(List<PreferredAttribute> preferredAttributes) {
        if (availableAttributePoints <= 0) {
            return;
        }
        ArrayList<Attribute> avail = new ArrayList<Attribute>();
        Deque<PreferredAttribute> preferred = new ArrayDeque<PreferredAttribute>(preferredAttributes);
        for (Attribute a : att.keySet()) {
            if (Attribute.isTrainable(a, this) && (getPure(a) > 0 || Attribute.isBasic(a))) {
                avail.add(a);
            }
        }
        if (avail.size() == 0) {
            avail.add(Attribute.Cunning);
            avail.add(Attribute.Power);
            avail.add(Attribute.Seduction);
        }
        int noPrefAdded = 2;
        for (; availableAttributePoints > 0; availableAttributePoints--) {
            Attribute selected = null;
            // remove all the attributes that isn't in avail
            preferred = new ArrayDeque<>(preferred.stream()
                                                  .filter(p -> {
                                                      Optional<Attribute> att = p.getPreferred(this);
                                                      return att.isPresent() && avail.contains(att.get());
                                                  })
                                                  .collect(Collectors.toList()));
            if (preferred.size() > 0) {
                if (noPrefAdded > 1) {
                    noPrefAdded = 0;
                    Optional<Attribute> pref = preferred.removeFirst()
                                                        .getPreferred(this);
                    if (pref.isPresent()) {
                        selected = pref.get();
                    }
                } else {
                    noPrefAdded += 1;
                }
            }

            if (selected == null) {
                selected = avail.get(Global.random(avail.size()));
            }
            mod(selected, 1);
            selected = null;
        }
    }

    public boolean isPetOf(Character other) {
        return false;
    }
    
    public boolean isPet() {
        return false;
    }

    public int getPetLimit() {
        return has(Trait.congregation) ? 2 : 1;
    }
}
