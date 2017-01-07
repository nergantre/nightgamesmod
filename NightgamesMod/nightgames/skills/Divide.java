package nightgames.skills;

import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.pet.CharacterPet;
import nightgames.pet.Pet;

public class Divide extends Skill {
    public Divide(Character self) {
        super("Divide", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().has(Trait.SlimeRoyalty) && getSelf().has(Trait.slime);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        // ignore pet limit
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && !target.isPet();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 15;
    }

    @Override
    public String describe(Combat c) {
        return "Divides your body into two.";
    }

    public static Pet makeClone(Combat c, Character self) {
        int power = Math.max(1, self.getLevel() / 2);
        int ac = 4 + power / 3;
        CharacterPet pet = null;
        String clonePrefix = String.format("%s clone", self.nameOrPossessivePronoun());
        Set<String> existingNames = c.getOtherCombatants()
                                      .stream().map(Character::getTrueName)
                                      .filter(name -> name.contains(clonePrefix))
                                      .collect(Collectors.toSet());
        String cloneName = clonePrefix + " ?";
        for (char letter : "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
            String testName = clonePrefix + " " + String.valueOf(letter).toUpperCase();
            if (!existingNames.contains(testName)) {
                cloneName = testName;
                break;
            }
        }
        if (self instanceof Player) {
            pet = new CharacterPet(cloneName, self, (Player)self, power, ac);
        } else if (self instanceof NPC) {
            pet = new CharacterPet(cloneName, self, (NPC)self, power, ac);
        } else {
            c.write(self, "Something fucked up happened in Divide.");
            return pet;
        }
        pet.getSelf().add(Trait.MindlessClone);
        return pet;

    }
    
    @Override
    public boolean resolve(Combat c, Character target) {
        Pet pet = makeClone(c, getSelf());
        if (pet == null) {
            return false;
        }
        c.write(getSelf(), formatMessage(Result.normal, target));
        c.addPet(getSelf(), pet.getSelf());

        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Divide(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.summoning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "unused";
    }
    
    private String formatMessage(Result modifier, Character target) {
        if (getSelf().human()) {
            return Global.format("You focus your attention on your slimey consitution and force yourself apart. "
                            + "The force of effort almost makes you black out, but when you finally raise your head, you are face to face with your own clone!", getSelf(), target);
        } else {
            return Global.format("Airi's slimey body bubbles as if boiling over. Worried, you step closer to make sure she's not in any kind of trouble. "
                            + "Suddenly, her viscous body splits apart, making you jump in surprise. Somehow, she managed to divide her body in half, and now you're another copy of her!",
                            getSelf(), target);
        }
    }
    
    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return "unused";
    }
}
