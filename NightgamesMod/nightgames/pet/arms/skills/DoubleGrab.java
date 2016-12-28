package nightgames.pet.arms.skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.arms.ArmType;
import nightgames.pet.arms.RoboArm;
import nightgames.status.Bound;
import nightgames.status.Stsflag;

public class DoubleGrab extends MultiArmMove {
    
    public DoubleGrab() {
        super("Double Grab");
    }

    @Override
    public Optional<List<RoboArm>> getInvolvedArms(Combat c, Character owner, Character target,
                    List<RoboArm> available) {
        long grabberCount = available.stream().filter(a -> a.getType() == ArmType.GRABBER).count();
        if (grabberCount < 2 || !c.getStance().prone(target) || target.is(Stsflag.bound)
                        || c.getCombatantData(target).getIntegerFlag(Grab.FLAG) > 0) {
            return Optional.empty();
        }
        List<RoboArm> arms = new ArrayList<>(available);
        arms.removeIf(a -> a.getType() != ArmType.GRABBER);
        while (arms.size() > 2) {
            arms.remove(0);
        }
        return Optional.of(arms);
    }

    @Override
    public void execute(Combat c, Character owner, Character target, List<RoboArm> arms) {
        c.write(owner, Global.format("Two of {self:name-possessive} Grabbers fly out towards"
                        + " {other:name-possessive} prone body, seizing a wrist each. The two"
                        + " arms lock together behind {other:possessive} back, completely immobilizing"
                        + " {other:possessive} arms.", owner, target));
        c.getCombatantData(target).setIntegerFlag(Grab.FLAG, 2);
        target.add(c, new Bound(target, 75, owner.nameOrPossessivePronoun() + " Grabbers"));
    }
}