package nightgames.characters.body;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.status.Abuff;
import nightgames.status.BodyFetish;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public class Body implements Cloneable {
    static class PartReplacement {
        public Set<BodyPart> added;
        public Set<BodyPart> removed;
        public int duration;

        public PartReplacement(int duration) {
            added = new LinkedHashSet<BodyPart>(2);
            removed = new LinkedHashSet<BodyPart>(2);
            this.duration = duration;
        }

        public PartReplacement(PartReplacement original) {
            added = new LinkedHashSet<BodyPart>(original.added);
            removed = new LinkedHashSet<BodyPart>(original.removed);
            duration = original.duration;
        }
    }

    static private Map<String, BodyPart> prototypes;

    static {
        prototypes = new HashMap<String, BodyPart>();
        prototypes.put(PussyPart.class.getCanonicalName(), PussyPart.normal);
        prototypes.put(BreastsPart.class.getCanonicalName(), BreastsPart.c);
        prototypes.put(BasicCockPart.class.getCanonicalName(), BasicCockPart.average);
        // for compatibility with < v1.8.1
        prototypes.put(CockPart.class.getCanonicalName(), BasicCockPart.average);
        prototypes.put(ModdedCockPart.class.getCanonicalName(),
                        new ModdedCockPart(BasicCockPart.average, CockMod.bionic));
        prototypes.put(WingsPart.class.getCanonicalName(), WingsPart.demonic);
        prototypes.put(TailPart.class.getCanonicalName(), TailPart.cat);
        prototypes.put(EarPart.class.getCanonicalName(), EarPart.normal);
        prototypes.put(StraponPart.class.getCanonicalName(), StraponPart.generic);
        prototypes.put(TentaclePart.class.getCanonicalName(), new TentaclePart("tentacles", "back", "semen", 0, 1, 1));
        prototypes.put(AssPart.class.getCanonicalName(), new AssPart("ass", 0, 1, 1));
        prototypes.put(MouthPart.class.getCanonicalName(), new MouthPart("mouth", 0, 1, 1));
        prototypes.put(AnalPussyPart.class.getCanonicalName(), new AnalPussyPart());
        prototypes.put(MouthPussyPart.class.getCanonicalName(), new MouthPussyPart());
        prototypes.put(GenericBodyPart.class.getCanonicalName(), new GenericBodyPart("", 0, 1, 1, "none", "none"));
        prototypes.put(FacePart.class.getCanonicalName(), new FacePart(.1, 2.3));
    }

    // yeah i know :(
    public static BodyPart nonePart = new GenericBodyPart("none", 0, 1, 1, "none", "");
    public static Set<String> pluralParts =
                    new HashSet<String>(Arrays.asList("hands", "feet", "wings", "breasts", "balls"));
    final static BodyPart requiredParts[] = {new GenericBodyPart("hands", 0, 1, 1, "hands", ""),
                    new GenericBodyPart("feet", 0, 1, 1, "feet", ""), new GenericBodyPart("skin", 0, 1, 1, "skin", ""),
                    AssPart.generic, MouthPart.generic, BreastsPart.flat, EarPart.normal};
    final static String fetishParts[] = {"ass", "feet", "cock", "wings", "tail", "tentacles", "breasts"};

    LinkedHashSet<BodyPart> bodyParts;
    public double hotness;
    transient Collection<PartReplacement> replacements;
    transient Collection<BodyPart> currentParts;
    transient public Character character;
    transient public BodyPart lastPleasuredBy;
    transient public BodyPart lastPleasured;
    public double baseFemininity;


    public Body() {
        bodyParts = new LinkedHashSet<BodyPart>();
        currentParts = Collections.emptySet();
        replacements = new ArrayList<PartReplacement>();
        lastPleasuredBy = nonePart;
        lastPleasured = nonePart;
        hotness = 1.0;
    }

    public Body(Character character) {
        this(character, 1);
    }

    public Body(Character character, double hotness) {
        this();
        this.character = character;
        this.hotness = hotness;

    }

    public Collection<BodyPart> getCurrentParts() {
        return currentParts;
    }

    private void updateCurrentParts() {
        LinkedHashSet<BodyPart> parts = new LinkedHashSet<BodyPart>(bodyParts);
        for (PartReplacement r : replacements) {
            parts.removeAll(r.removed);
            parts.addAll(r.added);
        }
        currentParts = parts;
    }

    public void temporaryAddPart(BodyPart part, int duration) {
        PartReplacement replacement = new PartReplacement(duration);
        replacement.added.add(part);
        replacements.add(replacement);
        updateCurrentParts();
        if (character != null) {
            updateCharacter();
        }
    }

    public void temporaryRemovePart(BodyPart part, int duration) {
        PartReplacement replacement = new PartReplacement(duration);
        replacement.removed.add(part);
        replacements.add(replacement);
        updateCurrentParts();
        if (character != null) {
            updateCharacter();
        }
    }

    public void temporaryAddOrReplacePartWithType(BodyPart part, int duration) {
        temporaryAddOrReplacePartWithType(part, getRandom(part.getType()), duration);
    }

    private BodyPart getPartIn(String type, Collection<BodyPart> parts) {
        for (BodyPart p : parts) {
            if (p.isType(type)) {
                return p;
            }
        }
        return null;
    }

    public boolean temporaryAddOrReplacePartWithType(BodyPart part, BodyPart removed, int duration) {
        PartReplacement replacement = null;
        if (removed != null)
            for (PartReplacement r : replacements) {
                BodyPart other = null;
                if (r.added.contains(removed)) {
                    other = removed;
                } else {
                    other = getPartIn(removed.getType(), r.added);
                }
                if (other != null) {
                    replacement = r;
                    r.added.remove(other);
                    r.added.add(part);
                    replacement.duration = Math.max(duration, replacement.duration);
                    break;
                }
            }
        if (replacement == null) {
            replacement = new PartReplacement(duration);
            replacement.removed.add(removed);
            replacement.added.add(part);
            replacements.add(replacement);
        }
        updateCurrentParts();
        if (character != null) {
            updateCharacter();
        }
        return true;
    }

    public void describe(StringBuilder b, Character c, String delimiter) {
        describe(b, c, delimiter, true);
    }
    
    public void describe(StringBuilder b, Character c, String delimiter, boolean hideInvisible) {
        for (BodyPart part : getCurrentParts()) {
            if ((!hideInvisible || part.isVisible(c)) && part.isNotable()) {
                int prevLength = b.length();
                part.describeLong(b, c);
                if (prevLength != b.length()) {
                    b.append(delimiter);
                }
            }
        }
    }

    public void describeBodyText(StringBuilder b, Character c, boolean notableOnly) {
        b.append(Global.format("{self:POSSESSIVE} body has ", c, null));
        BodyPart previous = null;
        for (BodyPart part : getCurrentParts()) {
            if (!notableOnly || part.isNotable()) {
                if (previous != null) {
                    b.append(Global.prependPrefix(previous.prefix(), previous.fullDescribe(c)));
                    b.append(", ");
                }
                previous = part;
            }
        }
        if (previous == null) {
            b.append("nothing notable.<br>");
        } else {
            b.append("and ");
            b.append(Global.prependPrefix(previous.prefix(), previous.fullDescribe(c)));
            b.append(".<br>");
        }
    }

    public void add(BodyPart part) {
        assert part != null;
        bodyParts.add(part);
        updateCurrentParts();
        updateCharacter();
    }

    public void updateCharacter() {
        if (character != null) {
            character.update();
        }
    }

    public boolean contains(BodyPart part) {
        return getCurrentParts().contains(part);
    }

    public List<BodyPart> get(String type) {
        return currentParts.stream()
                           .filter(p -> p.isType(type))
                           .collect(Collectors.toList());
    }

    public PussyPart getRandomPussy() {
        return (PussyPart) getRandom("pussy");

    }

    public WingsPart getRandomWings() {
        return (WingsPart) getRandom("wings");

    }

    public AssPart getRandomAss() {
        return (AssPart) getRandom("ass");
    }

    public BreastsPart getRandomBreasts() {
        return (BreastsPart) getRandom("breasts");
    }

    public BreastsPart getLargestBreasts() {
        List<BodyPart> parts = get("breasts");
        BreastsPart breasts = BreastsPart.flat;
        for (BodyPart part : parts) {
            BreastsPart b = (BreastsPart) part;
            if (b.size > breasts.size) {
                breasts = b;
            }
        }
        return breasts;
    }

    public CockPart getLargestCock() {
        List<BodyPart> parts = get("cock");
        if (parts.size() == 0) {
            return null;
        }
        CockPart largest = BasicCockPart.tiny;
        for (BodyPart part : parts) {
            CockPart cock = (CockPart) part;
            largest = cock.getSize() >= largest.getSize() ? cock : largest;
        }
        return largest;
    }

    public CockPart getCockBelow(double size) {
        List<BodyPart> parts = get("cock");
        List<CockPart> upgradable = new ArrayList<CockPart>();
        for (BodyPart part : parts) {
            CockPart cock = (CockPart) part;
            if (cock.getSize() < size) {
                upgradable.add(cock);
            }
        }
        if (upgradable.size() == 0) {
            return null;
        }

        return upgradable.get(Global.random(upgradable.size()));
    }

    public CockPart getCockAbove(double size) {
        List<BodyPart> parts = get("cock");
        List<CockPart> upgradable = new ArrayList<CockPart>();
        for (BodyPart part : parts) {
            CockPart b = (CockPart) part;
            if (b.getSize() > size) {
                upgradable.add(b);
            }
        }
        if (upgradable.size() == 0) {
            return null;
        }

        return upgradable.get(Global.random(upgradable.size()));
    }

    public BreastsPart getBreastsBelow(double size) {
        List<BodyPart> parts = get("breasts");
        List<BreastsPart> upgradable = new ArrayList<BreastsPart>();
        for (BodyPart part : parts) {
            BreastsPart b = (BreastsPart) part;
            if (b.size < size) {
                upgradable.add(b);
            }
        }
        if (upgradable.size() == 0) {
            return null;
        }

        return upgradable.get(Global.random(upgradable.size()));
    }

    public BreastsPart getBreastsAbove(double size) {
        List<BodyPart> parts = get("breasts");
        List<BreastsPart> upgradable = new ArrayList<BreastsPart>();
        for (BodyPart part : parts) {
            BreastsPart b = (BreastsPart) part;
            if (b.size > size) {
                upgradable.add(b);
            }
        }
        if (upgradable.size() == 0) {
            return null;
        }

        return upgradable.get(Global.random(upgradable.size()));
    }

    public Optional<BodyFetish> getFetish(String part) {
        Optional<Status> fs = character.status.stream()
                                              .filter(status -> {
                                                  if (status.flags()
                                                            .contains(Stsflag.bodyfetish)) {
                                                      BodyFetish fetish = (BodyFetish) status;
                                                      if (fetish.part.equalsIgnoreCase(part)) {
                                                          return true;
                                                      }
                                                  }
                                                  return false;
                                              })
                                              .findFirst();
        if (fs.isPresent()) {
            return Optional.of((BodyFetish) fs.get());
        } else {
            return Optional.empty();
        }
    }

    public double getHotness(Character self, Character opponent) {
        // represents tempt damage
        double retval = hotness;
        for (BodyPart part : getCurrentParts()) {
            retval += part.getHotness(self, opponent) * (getFetish(part.getType()).isPresent() ? 2 : 1);
        }
        retval += self.getOutfit()
                      .getHotness();
        int seductionDiff = Math.max(0, self.get(Attribute.Seduction) - opponent.get(Attribute.Seduction));
        retval += seductionDiff / 10.0;
        retval *= self.getExposure();
        if (self.is(Stsflag.alluring)) {
            retval *= 1.5;
        }
        return retval;
    }

    public void remove(BodyPart part) {
        bodyParts.remove(part);

        updateCurrentParts();
        if (character != null) {
            updateCharacter();
        }
    }

    public void removeOne(String type) {
        BodyPart removed = null;
        for (BodyPart part : bodyParts) {
            if (part.isType(type)) {
                removed = part;
                break;
            }
        }
        if (removed != null) {
            bodyParts.remove(removed);
            updateCurrentParts();
            if (character != null) {
                updateCharacter();
            }
        }
    }

    // returns how many are removed
    public int removeAll(String type) {
        List<BodyPart> removed = new ArrayList<BodyPart>();
        for (BodyPart part : bodyParts) {
            assert part != null;
            if (part.isType(type)) {
                removed.add(part);
            }
        }
        for (BodyPart part : removed) {
            bodyParts.remove(part);
        }
        updateCurrentParts();

        if (character != null) {
            updateCharacter();
        }
        return removed.size();
    }

    public void removeTemporaryParts(String type) {
        replacements.removeIf(rep -> rep.added.stream()
                                              .anyMatch(part -> part.getType()
                                                                    .equals(type)));
        updateCurrentParts();
    }

    public CockPart getRandomCock() {
        return (CockPart) getRandom("cock");
    }

    public BodyPart getRandomInsertable() {
        BodyPart part = getRandomCock();
        if (part == null && character.has(Trait.strapped)) {
            part = StraponPart.generic;
        }
        return part;
    }

    public boolean has(String type) {
        return get(type).size() > 0;
    }

    public BodyPart getRandom(String type) {
        List<BodyPart> parts = get(type);
        BodyPart part = null;
        if (parts.size() > 0) {
            part = parts.get(Global.random(parts.size()));
        }
        return part;
    }

    public int pleasure(Character opponent, BodyPart with, BodyPart target, double magnitude, Combat c) {
        return pleasure(opponent, with, target, magnitude, 0, c, false, null);
    }

    public int pleasure(Character opponent, BodyPart with, BodyPart target, double magnitude, Combat c, Skill skill) {
        return pleasure(opponent, with, target, magnitude, 0, c, false, skill);
    }
    
    public int pleasure(Character opponent, BodyPart with, BodyPart target, double magnitude, int bonus, Combat c,
                    boolean sub, Skill skill) {
        if (target == null) {
            target = nonePart;
        }
        if (with == null) {
            with = nonePart;
        }
        if (target.getType()
                  .equals("strapon")) {
            return 0;
        }

        double sensitivity = target.getSensitivity(with);
        if (character.has(Trait.desensitized)) {
            sensitivity -= .5;
        }
        if (character.has(Trait.desensitized2)) {
            sensitivity -= .5;
        }
        if (target.isErogenous() && character.has(Trait.hairtrigger)) {
            sensitivity += 1;
        }

        final double moddedSensitivity = sensitivity;
        sensitivity += character.status.stream()
                                       .mapToDouble(status -> status.sensitivity(moddedSensitivity))
                                       .sum();

        double pleasure = 1;
        if (!with.isType("none")) {
            pleasure = with.getPleasure(opponent, target);
        }
        double perceptionBonus = 1.0;
        if (opponent != null) {
            perceptionBonus *= getCharismaBonus(opponent);
        }
        double bonusDamage = bonus;
        if (opponent != null) {
            bonusDamage += with.applyBonuses(opponent, character, target, magnitude, c);
            bonusDamage += target.applyReceiveBonuses(character, opponent, with, magnitude, c);
            if (!sub) {
                for (BodyPart p : opponent.body.getCurrentParts()) {
                    bonusDamage += p.applySubBonuses(opponent, character, with, target, magnitude, c);
                }
            }
            // double the base damage if the opponent is submissive and in a
            // submissive stance
            if (c.getStance()
                 .sub(opponent) && opponent.has(Trait.submissive) && target.isErogenous()) {
                bonusDamage += bonusDamage + magnitude;
            } else if (c.getStance()
                        .dom(opponent) && opponent.has(Trait.submissive) && target.isErogenous()) {
                bonusDamage -= (bonusDamage + magnitude) * 2. / 3.;
            }
        }
        Optional<BodyFetish> fetish = getFetish(with.getType());
        if (fetish.isPresent()) {
            bonusDamage += magnitude * (1 + fetish.get().magnitude);
            character.add(c, new BodyFetish(character, opponent, with.getType(), .05));
        }
        double origBase = bonusDamage + magnitude;

        for (Status s : character.status) {
            bonusDamage += s.pleasure(c, with, target, origBase);
        }
        bonusDamage = Math.max(0, bonusDamage);
        double base = (magnitude + bonusDamage);
        double multiplier = Math.max(0, 1 + ((sensitivity - 1) + (pleasure - 1) + (perceptionBonus - 1)));
        
        if (skill != null) {
            multiplier = Math.max(0, multiplier + skill.multiplierForStage(character));
        }
        
        double damage = base * multiplier;
        double perceptionlessDamage = base * (multiplier - (perceptionBonus - 1));

        int result = (int) Math.round(damage);
        if (character.is(Stsflag.rewired)) {
            character.pain(c, result, false, false);
            return 0;
        }
        if (opponent != null) {
            String pleasuredBy = opponent.nameOrPossessivePronoun() + " " + with.describe(opponent);
            if (with == nonePart) {
                pleasuredBy = opponent.subject();
            }
            String firstColor =
                            character.human() ? "<font color='rgb(150,150,255)'>" : "<font color='rgb(255,150,150)'>";
            String secondColor =
                            opponent.human() ? "<font color='rgb(150,150,255)'>" : "<font color='rgb(255,150,150)'>";
            String bonusString = bonusDamage > 0
                            ? String.format(" + <font color='rgb(255,100,50)'>%.1f<font color='white'>", bonusDamage)
                            : "";
            String stageString = skill == null ? "" : String.format(" + stage:%.2f", skill.multiplierForStage(character));
            String battleString = String.format(
                            "%s%s %s<font color='white'> was pleasured by %s%s<font color='white'> for <font color='rgb(255,50,200)'>%d<font color='white'> "
                                            + "base:%.1f (%.1f%s) x multiplier: %.2f (1 + sen:%.1f + ple:%.1f + per:%.1f %s)\n",
                            firstColor, Global.capitalizeFirstLetter(character.nameOrPossessivePronoun()),
                            target.describe(character), secondColor, pleasuredBy, result, base, magnitude, bonusString,
                            multiplier, sensitivity - 1, pleasure - 1, perceptionBonus - 1, stageString);
            if (c != null) {
                c.writeSystemMessage(battleString);
            }
            Optional<BodyFetish> otherFetish = opponent.body.getFetish(target.getType());
            if (otherFetish.isPresent()) {
                opponent.tempt(c, character, target, (int) Math.round(perceptionlessDamage));
            }
        } else {
            String firstColor =
                            character.human() ? "<font color='rgb(150,150,255)'>" : "<font color='rgb(255,150,150)'>";
            String bonusString = bonusDamage > 0
                            ? String.format(" + <font color='rgb(255,100,50)'>%.1f<font color='white'>", bonusDamage)
                            : "";
            String battleString = String.format(
                            "%s%s %s<font color='white'> was pleasured for <font color='rgb(255,50,200)'>%d<font color='white'> "
                                            + "base:%.1f (%.2f%s) x multiplier: %.2f (sen:%.1f + ple:%.1f + per:%.1f)\n",
                            firstColor, Global.capitalizeFirstLetter(character.nameOrPossessivePronoun()),
                            target.describe(character), result, base, magnitude, bonusString, multiplier,
                            sensitivity - 1, pleasure - 1, perceptionBonus - 1);
            if (c != null) {
                c.writeSystemMessage(battleString);
            }
        }
        character.resolvePleasure(result, c, opponent, target, with);

        if (opponent != null && Arrays.asList(fetishParts)
                                      .contains(with.getType())) {
            if (opponent.has(Trait.fetishTrainer)
                            && Global.random(100) < Math.min(opponent.get(Attribute.Fetish), 25)) {
                c.write(character, character.subjectAction("now have", "now has") + " a new fetish, courtesy of "
                                + opponent.directObject() + ".");
                character.add(c, new BodyFetish(character, opponent, with.getType(), .25));
            }
        }
        lastPleasuredBy = with;
        lastPleasured = target;
        return result;
    }

    public double getCharismaBonus(Character opponent) {
        // you don't get turned on by yourself
        if (opponent == character) {
            return 1.0;
        } else {
            double perceptionBonus = Math.sqrt(opponent.body.getHotness(opponent, character)
                            * (1.0 + (Math.max(0, character.get(Attribute.Perception)) - 5) / 10.0));
            if (character.is(Stsflag.lovestruck)) {
                perceptionBonus += 1;
            }
            return perceptionBonus;
        }
    }

    public void addReplace(BodyPart part, int max) {
        int n = Math.min(Math.max(1, removeAll(part.getType())), max);
        for (int i = 0; i < n; i++) {
            add(part);
        }
    }

    public double getFemininity() {
        double femininity = baseFemininity;
        femininity += getLargestBreasts().size / ((double) BreastsPart.maximumSize().size);
        femininity += getCurrentParts().stream()
                                       .mapToDouble(part -> part.getFemininity(character))
                                       .sum();
        return femininity;
    }

    public void finishBody(CharacterSex sex) {
        switch (sex) {
            case female:
                baseFemininity += 2;
                if (!has("face")) {
                    add(new FacePart(0, 2));
                }
                if (get("breasts").size() == 0) {
                    add(BreastsPart.b);
                }
                break;
            case male:
                baseFemininity -= 2;
                if (!has("face")) {
                    add(new FacePart(0, -2));
                }
                if (!has("balls")) {
                    add(new GenericBodyPart("balls", 0, 1.0, 1.5, "balls", ""));
                }
                break;
            case herm:
                baseFemininity += 1;
                if (!has("face")) {
                    add(new FacePart(0, 0));
                }
                if (get("breasts").size() == 0) {
                    add(BreastsPart.b);
                }
                break;
            case asexual:
                baseFemininity += 0;
                if (!has("face")) {
                    add(new FacePart(0, 0));
                }
                break;
        }
        for (BodyPart part : requiredParts) {
            if (!has(part.getType())) {
                add(part);
            }
        }
    }

    CharacterSex getEffectiveSex() {
        boolean hasCock = has("cock");
        boolean hasPussy = has("pussy");
        if (hasCock && hasPussy) {
            return CharacterSex.herm;
        } else if (hasCock) {
            return CharacterSex.male;
        } else if (hasPussy) {
            return CharacterSex.female;
        } else {
            return CharacterSex.asexual;
        }
    }

    public void makeGenitalOrgans(CharacterSex sex) {
        if (sex.hasPussy()) {
            if (!has("pussy")) {
                add(PussyPart.normal);
            }

        }
        if (sex.hasCock()) {
            if (!has("cock")) {
                add(BasicCockPart.average);
            }
        }
        if (sex == CharacterSex.male) {

        }

    }

    @Override
    public Body clone() throws CloneNotSupportedException {
        Body newBody = (Body) super.clone();
        newBody.replacements = new ArrayList<PartReplacement>();
        replacements.forEach(rep -> newBody.replacements.add(new PartReplacement(rep)));
        newBody.bodyParts = new LinkedHashSet<>(bodyParts);
        newBody.currentParts = currentParts;
        return newBody;
    }

    @SuppressWarnings("unchecked")
    public JSONObject save() {
        JSONObject bodyObj = new JSONObject();
        bodyObj.put("hotness", hotness);
        bodyObj.put("femininity", baseFemininity);
        JSONArray partsArr = new JSONArray();
        for (BodyPart part : bodyParts) {
            JSONObject obj = part.save();
            obj.put("class", part.getClass()
                                 .getCanonicalName());
            partsArr.add(obj);
        }
        bodyObj.put("parts", partsArr);
        return bodyObj;
    }

    public static BodyPart loadPart(JSONObject obj) {
        String classType = (String) obj.get("class");
        return prototypes.get(classType)
                         .load(obj);
    }

    public void loadParts(JSONArray partsArr) {
        for (Object part : partsArr) {
            JSONObject obj = (JSONObject) part;
            this.add(loadPart(obj));
        }
    }

    @SuppressWarnings("unchecked")
    public static Body load(JSONObject bodyObj, Character character) {
        double hotness = ((Number) bodyObj.get("hotness")).doubleValue();
        Body body = new Body(character, hotness);
        body.loadParts((JSONArray) bodyObj.get("parts"));
        double defaultFemininity = 0;
        if (body.has("pussy")) {
            defaultFemininity += 2;
        }
        if (body.has("cock")) {
            defaultFemininity -= 2;
        }
        double femininity = ((Number) bodyObj.getOrDefault("femininity", defaultFemininity)).doubleValue();
        body.baseFemininity = femininity;
        body.updateCurrentParts();
        return body;
    }

    private void advancedTemporaryParts(Combat c) {
        ArrayList<PartReplacement> expired = new ArrayList<Body.PartReplacement>();
        for (PartReplacement r : replacements) {
            r.duration -= 1;
            if (r.duration <= 0) {
                expired.add(r);
            }
        }
        for (PartReplacement r : expired) {
            replacements.remove(r);
            updateCurrentParts();
            StringBuilder sb = new StringBuilder();
            LinkedList<BodyPart> added = new LinkedList<>(r.added);
            LinkedList<BodyPart> removed = new LinkedList<>(r.removed);
            if (added.size() > 0 && removed.size() == 0) {
                sb.append(Global.format("{self:NAME-POSSESSIVE} ", character, character));
                for (BodyPart p : added.subList(0, added.size() - 1)) {
                    sb.append(p.fullDescribe(character))
                      .append(", ");
                }
                if (added.size() > 1) {
                    sb.append(" and ");
                }
                sb.append(added.get(added.size() - 1)
                               .fullDescribe(character));
                sb.append(" disappeared.");
            } else if (removed.size() > 0 && added.size() == 0) {
                sb.append(Global.format("{self:NAME-POSSESSIVE} ", character, character));
                for (BodyPart p : removed.subList(0, removed.size() - 1)) {
                    sb.append(p.fullDescribe(character))
                      .append(", ");
                }
                if (removed.size() > 1) {
                    sb.append(" and ");
                }
                sb.append(removed.get(removed.size() - 1)
                                 .fullDescribe(character));
                sb.append(" reappeared.");
            } else if (removed.size() > 0 && added.size() > 0) {
                sb.append(Global.format("{self:NAME-POSSESSIVE} ", character, character));
                for (BodyPart p : added.subList(0, added.size() - 1)) {
                    sb.append(p.fullDescribe(character))
                      .append(", ");
                }
                if (added.size() > 1) {
                    sb.append(" and ");
                }
                sb.append(added.get(added.size() - 1)
                               .fullDescribe(character));
                sb.append(" turned back into ");
                for (BodyPart p : removed.subList(0, removed.size() - 1)) {
                    sb.append(Global.prependPrefix(p.prefix(), p.fullDescribe(character)))
                      .append(", ");
                }
                if (removed.size() > 1) {
                    sb.append(" and ");
                }
                BodyPart last = removed.get(removed.size() - 1);
                if (last != null)
                sb.append(Global.prependPrefix(last.prefix(), last.fullDescribe(character)));
                sb.append('.');
            }
            if (c != null) {
                c.writeSystemMessage(character, sb.toString());
            } else if (character.human()) {
                Global.gui()
                      .message(sb.toString());
            }
        }
    }

    public void tick(Combat c) {
        advancedTemporaryParts(c);
        if (character != null) {
            updateCharacter();
        }
    }

    public BodyPart getRandomHole() {
        BodyPart part = getRandomPussy();
        if (part == null) {
            part = getRandom("ass");
        }
        return part;
    }

    public void clearReplacements() {
        replacements.clear();
        updateCurrentParts();
        if (character != null) {
            updateCharacter();
        }
    }

    public int mod(Attribute a, int total) {
        int res = 0;
        for (BodyPart p : getCurrentParts()) {
            total += p.mod(a, total);
        }
        return res;
    }

    public void receiveCum(Combat c, Character opponent, BodyPart part) {
        if (part == null) {
            part = character.body.getRandom("skin");
        }
        if (character.has(Trait.spiritphage)) {
            c.write(Global.capitalizeFirstLetter("<br><b>" + character.subjectAction("glow", "glows")
                            + " with power as the cum is absorbed by " + character.possessivePronoun() + " "
                            + part.describe(character) + ".</b>"));
            character.add(c, new Abuff(character, Attribute.Power, 5, 10));
            character.add(c, new Abuff(character, Attribute.Seduction, 10, 10));
            character.add(c, new Abuff(character, Attribute.Cunning, 5, 10));
            character.buildMojo(c, 100);
        }
        if (opponent.has(Trait.hypnoticsemen)) {
            c.write(Global.format(
                            "<br><b>{other:NAME-POSSESSIVE} hypnotic semen takes its toll on {self:name-possessive} willpower, rendering {self:direct-object} doe-eyed and compliant.</b>",
                            character, opponent));
            character.loseWillpower(c, 10 + Global.random(10));
        }
    }

    public void tickHolding(Combat c, Character opponent, BodyPart selfOrgan, BodyPart otherOrgan) {
        if (selfOrgan != null && otherOrgan != null) {
            selfOrgan.tickHolding(c, character, opponent, otherOrgan);
            if (character.checkOrgasm()) {
                character.doOrgasm(c, opponent, selfOrgan, otherOrgan);
            }
            if (opponent.checkOrgasm()) {
                opponent.doOrgasm(c, character, otherOrgan, selfOrgan);
            }
        }
    }

    public float penetrationFitnessModifier(Character self, Character other, boolean pitcher, boolean anal) {
        int totalCounterValue = 0;

        if (anal) {
            if (!pitcher) {
                totalCounterValue += get("ass").stream()
                                               .flatMapToInt(ass -> other.body.get("cock")
                                                                         .stream()
                                                                         .mapToInt(cock -> ass.counterValue(cock, self, other)))
                                               .sum();
            } else {
                totalCounterValue += get("cock").stream()
                                                .flatMapToInt(cock -> other.body.get("ass")
                                                                           .stream()
                                                                           .mapToInt(ass -> cock.counterValue(ass, self, other)))
                                                .sum();
            }
        } else {
            if (!pitcher) {
                totalCounterValue += get("pussy").stream()
                                                 .flatMapToInt(pussy -> other.body.get("cock")
                                                                             .stream()
                                                                             .mapToInt(cock -> pussy.counterValue(
                                                                                             cock, self, other)))
                                                 .sum();
            } else {
                totalCounterValue += get("cock").stream()
                                                .flatMapToInt(cock -> other.body.get("pussy")
                                                                           .stream()
                                                                           .mapToInt(pussy -> cock.counterValue(pussy, self, other)))
                                                .sum();
            }
        }
        return 20 * totalCounterValue;
    }

    public Body clone(Character other) throws CloneNotSupportedException {
        Body res = clone();
        res.character = other;
        return res;
    }

    public void purge(Combat c) {
        for (PartReplacement r : replacements) {
            r.duration = 0;
        }
        advancedTemporaryParts(c);
    }

    public BodyPart getRandomGenital() {
        List<BodyPart> parts = new ArrayList<>();
        BodyPart pussy = getRandomPussy();
        BodyPart cock = getRandomCock();
        if (pussy != null) {
            parts.add(pussy);
        }
        if (cock != null) {
            parts.add(cock);
        }
        Collections.shuffle(parts);
        if (parts.size() > 1) {
            return parts.get(0);
        } else {
            return getRandomBreasts();
        }
    }
}
