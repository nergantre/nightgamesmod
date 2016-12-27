package nightgames.pet.arms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.DoubleGrab;
import nightgames.pet.arms.skills.Idle;
import nightgames.pet.arms.skills.MultiArmMove;

public class RoboArmManager {

    private static final List<MultiArmMove> MULTI_MOVES = Arrays.asList(new DoubleGrab());

    private final Character owner;
    private List<RoboArm> arms;

    public RoboArmManager(Character owner) {
        this.owner = owner;
        arms = new ArrayList<>();
    }

    public void selectArms() {
        arms.clear();
        // TODO
        arms.add(new Grabber(this, owner));
        arms.add(new Grabber(this, owner));
        arms.add(new Stripper(this, owner));
    }

    public int armCount() {
        return arms.size();
    }

    public List<RoboArm> getActiveArms() {
        return new ArrayList<>(arms);
    }
    
    public String describeArms() {
        Map<ArmType, List<RoboArm>> grouped = arms.stream().collect(Collectors.groupingBy(RoboArm::getType));
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        
        for (Map.Entry<ArmType, List<RoboArm>> e : grouped.entrySet()) {
            int amt = e.getValue().size();
            sb.append(amt == 1 ? "a" : amt);
            sb.append(" ").append(e.getKey().getName());
            if (amt > 1) sb.append('s');
            
            counter++;
            if (counter == grouped.size() - 1) {
                sb.append(" and ");
            } else if (counter < grouped.size()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    private List<RoboArm> handleMultiArmMoves(Combat c, Character target) {
        List<RoboArm> remaining = arms;
        Collections.shuffle(MULTI_MOVES);
        for (MultiArmMove mam : MULTI_MOVES) {
            if (mam.shouldExecute()) {
                Optional<List<RoboArm>> used = mam.getInvolvedArms(c, owner, target, remaining);
                if (used.isPresent()) {
                    remaining.removeAll(used.get());
                    mam.execute(c, owner, target, used.get());
                }
            }
        }
        return remaining;
    }

    private void doArmAction(RoboArm arm, Combat c, Character target) {
        if (arm.attackOdds(c, owner, target) > Global.random(100)) {
            ArmSkill skill = Global.pickRandom(arm.getSkills(c, owner, target)
                                                  .stream()
                                                  .filter(s -> s.usable(c, arm, owner, target))
                                                  .toArray(ArmSkill[]::new));
            if (skill != null) {
                c.write(PetCharacter.DUMMY, String.format("<b>%s %s uses %s</b>", owner.nameOrPossessivePronoun(),
                                arm.getName(), skill.getName()));
                skill.resolve(c, arm, owner, target);
                return;
            }
        }
        new Idle().resolve(c, arm, owner, target);
    }

    public void act(Combat c, Character target) {
        List<RoboArm> available = handleMultiArmMoves(c, target);
        available.forEach(a -> doArmAction(a, c, target));
    }
}
