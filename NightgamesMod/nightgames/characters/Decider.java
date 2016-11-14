package nightgames.characters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.actions.Action;
import nightgames.actions.Move;
import nightgames.actions.Movement;
import nightgames.characters.custom.effect.CustomEffect;
import nightgames.combat.Combat;
import nightgames.daytime.Daytime;
import nightgames.global.DebugFlags;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.pet.PetCharacter;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.skills.Wait;

public class Decider {
    private static void addAllSkillsWithPriority(ArrayList<WeightedSkill> priority, HashSet<Skill> skills,
                    float weight) {
        for (Skill s : skills) {
            priority.add(new WeightedSkill(weight, s));
        }
    }

    public static ArrayList<WeightedSkill> parseSkills(HashSet<Skill> available, Combat c, NPC character) {
        HashSet<Skill> damage = new HashSet<Skill>();
        HashSet<Skill> pleasure = new HashSet<Skill>();
        HashSet<Skill> fucking = new HashSet<Skill>();
        HashSet<Skill> position = new HashSet<Skill>();
        HashSet<Skill> debuff = new HashSet<Skill>();
        HashSet<Skill> recovery = new HashSet<Skill>();
        HashSet<Skill> calming = new HashSet<Skill>();
        HashSet<Skill> summoning = new HashSet<Skill>();
        HashSet<Skill> stripping = new HashSet<Skill>();
        HashSet<Skill> misc = new HashSet<Skill>();
        ArrayList<WeightedSkill> priority = new ArrayList<WeightedSkill>();
        for (Skill a : available) {
            if (a.type(c) == Tactics.damage) {
                damage.add(a);
            } else if (a.type(c) == Tactics.pleasure) {
                pleasure.add(a);
            } else if (a.type(c) == Tactics.fucking) {
                fucking.add(a);
            } else if (a.type(c) == Tactics.positioning) {
                position.add(a);
            } else if (a.type(c) == Tactics.debuff) {
                debuff.add(a);
            } else if (a.type(c) == Tactics.recovery) {
                recovery.add(a);
            } else if (a.type(c) == Tactics.calming) {
                calming.add(a);
            } else if (a.type(c) == Tactics.summoning) {
                summoning.add(a);
            } else if (a.type(c) == Tactics.stripping) {
                stripping.add(a);
            } else if (a.type(c) == Tactics.misc) {
                misc.add(a);
            }
        }
        switch (character.mood) {
            case confident:
                // i can do whatever i want
                addAllSkillsWithPriority(priority, position, 1.0f);
                addAllSkillsWithPriority(priority, stripping, 1.0f);
                addAllSkillsWithPriority(priority, debuff, 1.0f);
                addAllSkillsWithPriority(priority, pleasure, 1.0f);
                addAllSkillsWithPriority(priority, fucking, 1.0f);
                addAllSkillsWithPriority(priority, damage, 1.0f);
                addAllSkillsWithPriority(priority, summoning, .5f);
                addAllSkillsWithPriority(priority, misc, 1f);
                break;
            case angry:
                addAllSkillsWithPriority(priority, damage, 2.5f);
                addAllSkillsWithPriority(priority, position, 2.0f);
                addAllSkillsWithPriority(priority, debuff, 2.0f);
                addAllSkillsWithPriority(priority, stripping, 1.0f);
                addAllSkillsWithPriority(priority, pleasure, 0.0f);
                addAllSkillsWithPriority(priority, misc, 1f);
                addAllSkillsWithPriority(priority, summoning, 0f);
                break;
            case nervous:
                addAllSkillsWithPriority(priority, summoning, 2.0f);
                addAllSkillsWithPriority(priority, debuff, 2.0f);
                addAllSkillsWithPriority(priority, calming, 2.0f);
                addAllSkillsWithPriority(priority, recovery, 2.0f);
                addAllSkillsWithPriority(priority, position, 1.0f);
                addAllSkillsWithPriority(priority, damage, .5f);
                addAllSkillsWithPriority(priority, pleasure, 0.0f);
                addAllSkillsWithPriority(priority, misc, 1f);
                break;
            case desperate:
                // and probably a bit confused
                addAllSkillsWithPriority(priority, calming, 4.0f);
                addAllSkillsWithPriority(priority, recovery, 4.0f);
                addAllSkillsWithPriority(priority, debuff, 4.0f);
                addAllSkillsWithPriority(priority, misc, 3.0f);
                addAllSkillsWithPriority(priority, position, 2.0f);
                addAllSkillsWithPriority(priority, damage, 2.0f);
                addAllSkillsWithPriority(priority, pleasure, 1.0f);
                addAllSkillsWithPriority(priority, fucking, 1.0f);
                break;
            case horny:
                addAllSkillsWithPriority(priority, fucking, 5.0f);
                addAllSkillsWithPriority(priority, stripping, 1.0f);
                addAllSkillsWithPriority(priority, pleasure, 1.0f);
                addAllSkillsWithPriority(priority, position, 1.0f);
                addAllSkillsWithPriority(priority, debuff, 0f);
                addAllSkillsWithPriority(priority, misc, 1f);
                break;
            case dominant:
                addAllSkillsWithPriority(priority, position, 3.0f);
                addAllSkillsWithPriority(priority, fucking, 2.0f);
                addAllSkillsWithPriority(priority, stripping, 2.0f);
                addAllSkillsWithPriority(priority, pleasure, 2.0f);
                addAllSkillsWithPriority(priority, debuff, 2.0f);
                addAllSkillsWithPriority(priority, damage, 2.0f);
                addAllSkillsWithPriority(priority, summoning, 1.0f);
                addAllSkillsWithPriority(priority, misc, 1f);
                break;
        }
        /*
         * if(character.getArousal().percent()>85||character.getStamina().percent()<10){ priority.add(recovery); priority.add(damage); priority.add(pleasure); } if((c.stance.penetration(character)&&c.stance.dom(character))||c.stance.enumerate()==Stance.sixnine||(c.stance.dom(character)&&c.stance.enumerate()==Stance.behind)){ priority.add(pleasure); priority.add(pleasure); priority.add(damage); priority.add(recovery); } if(!target.canAct()){ priority.add(stripping); priority.add(pleasure); priority.add(summoning); priority.add(position); } else if(!target.nude()&&(target.getArousal().percent()>60||target.getStamina().percent()<50)){ priority.add(stripping); priority.add(pleasure); priority.add(damage); priority.add(position); } else if(c.stance.dom(character)){ priority.add(pleasure); priority.add(stripping); priority.add(summoning); priority.add(damage); priority.add(position); priority.add(debuff); } else{ priority.add(summoning); priority.add(pleasure); priority.add(debuff); priority.add(damage); priority.add(position); priority.add(stripping); priority.add(recovery); }
         */ return priority;
    }

    public static Action parseMoves(HashSet<Action> available, HashSet<Movement> radar, NPC character) {
        HashSet<Action> enemy = new HashSet<Action>();
        HashSet<Action> safe = new HashSet<Action>();
        HashSet<Action> utility = new HashSet<Action>();
        HashSet<Action> tactic = new HashSet<Action>();
        if (character.mostlyNude()) {
            for (Action act : available) {
                if (act.consider() == Movement.resupply) {
                    return act;
                } else if (act.getClass() == Move.class) {
                    Move movement = (Move) act;
                    if (movement.consider() == Movement.union && !radar.contains(Movement.union)
                                    || movement.consider() == Movement.dorm && !radar.contains(Movement.dorm)) {
                        return act;
                    }
                }
            }
        }
        if (character.getArousal().percent() >= 40 && !character.location().humanPresent() && radar.isEmpty()) {
            for (Action act : available) {
                if (act.consider() == Movement.masturbate) {
                    return act;
                }
            }
        }
        if (character.getStamina().percent() <= 60 || character.getArousal().percent() >= 30) {
            for (Action act : available) {
                if (act.consider() == Movement.bathe) {
                    return act;
                } else if (act.getClass() == Move.class) {
                    Move movement = (Move) act;
                    if (movement.consider() == Movement.pool || movement.consider() == Movement.shower) {
                        return act;
                    }
                }
            }
        }
        if (character.get(Attribute.Science) >= 1 && !character.has(Item.Battery, 10)) {
            for (Action act : available) {
                if (act.consider() == Movement.recharge) {
                    return act;
                }
            }
            String workshop = Global.checkFlag(Flag.FTC) ? "Cabin" : "Workshop";
            Move path = character.findPath(Global.getMatch().gps(workshop));
            if (path != null) {
                return path;
            }
        }
        for (Action act : available) {
            if (radar.contains(act.consider())) {
                enemy.add(act);
            } else if (act.consider() == Movement.bathe || act.consider() == Movement.craft
                            || act.consider() == Movement.scavenge || act.consider() == Movement.hide
                            || act.consider() == Movement.trap || act.consider() == Movement.wait
                            || act.consider() == Movement.engineering || act.consider() == Movement.dining) {
                utility.add(act);
            } else {
                safe.add(act);
            }
        }
        if (!character.location().humanPresent()) {
            tactic.addAll(utility);
        }
        if (character.plan == Plan.hunting && !enemy.isEmpty()) {
            tactic.addAll(enemy);
        }

        else {
            tactic.addAll(safe);
        }
        if (tactic.isEmpty()) {
            tactic.addAll(available);
        }
        Action[] actions = tactic.toArray(new Action[tactic.size()]);
        return actions[Global.random(actions.length)];
    }

    public static void visit(Character self) {
        if (Global.checkCharacterDisabledFlag(self)) {
            return;
        }
        int max = 0;
        Character bff = null;
        if (!self.attractions.isEmpty()) {
            for (String key : self.attractions.keySet()) {
                Character friend = Global.getCharacterByType(key);
                if (self.getAttraction(friend) > max && !friend.human()) {
                    max = self.getAttraction(friend);
                    bff = friend;
                }
            }
            if (bff != null) {
                self.gainAffection(bff, Global.random(3) + 1);
                bff.gainAffection(self, Global.random(3) + 1);
                switch (Global.random(3)) {
                    case 0:
                        Daytime.train(self, bff, Attribute.Power);
                    case 1:
                        Daytime.train(self, bff, Attribute.Cunning);
                    default:
                        Daytime.train(self, bff, Attribute.Seduction);
                }
            }
        }
    }

    public static WeightedSkill prioritizePet(PetCharacter self, Character target, List<Skill> plist, Combat c) {
        List<WeightedSkill> weightedList = plist.stream().map(skill -> new WeightedSkill(1.0, skill)).collect(Collectors.toList());
        return prioritizePetWithWeights(self, target, weightedList, c);
    }

    public static WeightedSkill prioritizePetWithWeights(PetCharacter self, Character target, List<WeightedSkill> plist, Combat c) {
        if (plist.isEmpty()) {
            return new WeightedSkill(1.0, new Wait(self));
        }
        // The higher, the better the AI will plan for "rare" events better
        final int RUN_COUNT = 3;
        // Decrease to get an "easier" AI. Make negative to get a suicidal AI.
        final double RATING_FACTOR = 0.02f;

        // Starting fitness
        Character master = self.getSelf().owner();
        Character other = c.getOpponent(self);
        double masterFit = master.getFitness(c);
        double otherFit = master.getOtherFitness(c, other);

        // Now simulate the result of all actions
        ArrayList<WeightedSkill> moveList = new ArrayList<>();
        double sum = 0;

        for (WeightedSkill wskill : plist) {
            // Run it a couple of times
            double rating, raw_rating = 0;
            if (wskill.skill.type(c) == Tactics.damage && self.has(Trait.sadist)) {
                wskill.weight += 1.0;
            }
            for (int j = 0; j < RUN_COUNT; j++) {
                raw_rating += ratePetMove(self, wskill.skill, target, c, masterFit, otherFit);
            }

            // Sum up rating, add to map
            rating = (double) Math.pow(2, RATING_FACTOR * raw_rating + wskill.weight + wskill.skill.priorityMod(c)
                            + Global.getMatch().condition.getSkillModifier().encouragement(wskill.skill, c, self));
            sum += rating;
            moveList.add(new WeightedSkill(sum, raw_rating, rating, wskill.skill));
        }
        if (sum == 0 || moveList.size() == 0) {
            return null;
        }
        // Debug
        if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
            String s = "Pet choices: ";
            for (WeightedSkill entry : moveList) {
                s += String.format("\n(%.1f\t\t%.1f\t\tculm: %.1f\t\t/ %.1f)\t\t-> %s", entry.raw_rating, entry.rating,
                                entry.weight, entry.rating * 100.0f / sum, entry.skill.getLabel(c));
            }
            System.out.println(s);
        }
        // Select
        double s = Global.randomdouble() * sum;
        for (WeightedSkill entry : moveList) {
            if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
                System.out.printf("%.1f/%.1f %s\n", entry.weight, s, entry.skill.toString());
            }
            if (entry.weight > s) {
                return entry;
            }
        }
        return moveList.get(moveList.size() - 1);
    }

    public static Skill prioritizeNew(Character self, List<WeightedSkill> plist, Combat c) {
        if (plist.isEmpty()) {
            return null;
        }
        // The higher, the better the AI will plan for "rare" events better
        final int RUN_COUNT = 5;
        // Decrease to get an "easier" AI. Make negative to get a suicidal AI.
        final double RATING_FACTOR = 0.02f;

        // Starting fitness
        Character other = c.getOpponent(self);
        double selfFit = self.getFitness(c);
        double otherFit = self.getOtherFitness(c, other);

        // Now simulate the result of all actions
        ArrayList<WeightedSkill> moveList = new ArrayList<>();
        double sum = 0;
        for (WeightedSkill wskill : plist) {
            // Run it a couple of times
            double rating, raw_rating = 0;
            if (wskill.skill.type(c) == Tactics.fucking && self.has(Trait.experienced)) {
                wskill.weight += 1.0;
            }
            if (wskill.skill.type(c) == Tactics.damage && self.has(Trait.sadist)) {
                wskill.weight += 1.0;
            }
            for (int j = 0; j < RUN_COUNT; j++) {
                raw_rating += rateMove(self, wskill.skill, c, selfFit, otherFit);
            }

            if (self instanceof NPC) {
                wskill.weight += ((NPC)self).ai.getAiModifiers().modAttack(wskill.skill.getClass());
            }
            // Sum up rating, add to map
            rating = (double) Math.pow(2, RATING_FACTOR * raw_rating + wskill.weight + wskill.skill.priorityMod(c)
                            + Global.getMatch().condition.getSkillModifier().encouragement(wskill.skill, c, self));
            sum += rating;
            moveList.add(new WeightedSkill(sum, raw_rating, rating, wskill.skill));
        }
        if (sum == 0 || moveList.size() == 0) {
            return null;
        }
        // Debug
        if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
            String s = "AI choices: ";
            for (WeightedSkill entry : moveList) {
                s += String.format("\n(%.1f\t\t%.1f\t\tculm: %.1f\t\t/ %.1f)\t\t-> %s", entry.raw_rating, entry.rating,
                                entry.weight, entry.rating * 100.0f / sum, entry.skill.getLabel(c));
            }
            System.out.println(s);
        }
        // Select
        double s = Global.randomdouble() * sum;
        for (WeightedSkill entry : moveList) {
            if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
                System.out.printf("%.1f/%.1f %s\n", entry.weight, s, entry.skill.toString());
            }
            if (entry.weight > s) {
                return entry.skill;
            }
        }
        return moveList.get(moveList.size() - 1).skill;
    }

    private static double ratePetMove(PetCharacter self, Skill skill, Character target, Combat c, double masterFit, double otherFit) {
        // Clone ourselves a new combat... This should clone our characters, too
        if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS_RATING) && (c.p1.human() || c.p2.human())) {
            System.out.println("===> Rating " + skill);
            System.out.println("Before:\n" + c.debugMessage());
        }
        return rateActionWithObserver(self, self.getSelf().owner(), target, c, masterFit, otherFit, (combat, selfCopy, other) -> {
            skill.setSelf(selfCopy);
            skill.resolve(combat, other);
            skill.setSelf(self);
            return true;
        });
    }

    private static double rateMove(Character self, Skill skill, Combat c, double selfFit, double otherFit) {
        // Clone ourselves a new combat... This should clone our characters, too
        if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS_RATING) && (c.p1.human() || c.p2.human())) {
            System.out.println("===> Rating " + skill);
            System.out.println("Before:\n" + c.debugMessage());
        }
        return rateAction(self, c, selfFit, otherFit, (combat, selfCopy, other) -> {
            skill.setSelf(selfCopy);
            skill.resolve(combat, other);
            skill.setSelf(self);
            return true;
        });
    }

    private static Character getCopyFromCombat(Combat c, Combat clonedCombat, Character self) {
        if (c.p1 == self) {
            return clonedCombat.p1;
        } else if (c.p2 == self) {
            return clonedCombat.p2;
        } else if (c.getOtherCombatants().contains(self)) {
            return clonedCombat.getOtherCombatants().stream().filter(other -> other.equals(self)).findAny().get();
        } else {
            throw new IllegalArgumentException("Tried to use a badly cloned combat");
        }
    }

    public static double rateAction(Character skillUser, Combat c, double selfFit, double otherFit, CustomEffect effect) {
        return rateActionWithObserver(skillUser, skillUser, c.getOpponent(skillUser), c, selfFit, otherFit, effect);
    }

    public static double rateActionWithObserver(Character skillUser, Character fitnessObserver, Character target,
                    Combat c, double selfFit, double otherFit, CustomEffect effect) {
        // Clone ourselves a new combat... This should clone our characters, too
        Combat c2;
        try {
            c2 = c.clone();
        } catch (CloneNotSupportedException e) {
            return 0;
        }

        Global.debugSimulation += 1;
        Character newSkillUser = getCopyFromCombat(c, c2, skillUser);
        Character newObserver = getCopyFromCombat(c, c2, fitnessObserver);
        Character newOpponent = c2.getOpponent(newObserver);
        Character newTarget = getCopyFromCombat(c, c2, target);

        effect.execute(c2, newSkillUser, newTarget);
        Global.debugSimulation -= 1;
        double selfFitnessDelta = newObserver.getFitness(c) - selfFit;
        double otherFitnessDelta = newObserver.getOtherFitness(c, newOpponent) - otherFit;
        if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS_RATING) && (c2.p1.human() || c2.p2.human())) {
            System.out.println("After:\n" + c2.debugMessage());
        }
        return selfFitnessDelta - otherFitnessDelta;
    }
}