package nightgames.pet.arms.skills;

import java.util.List;
import java.util.Optional;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.arms.Arm;

public abstract class MultiArmMove {
    @SuppressWarnings("unused")
    private final String name;

    MultiArmMove(String name) {
        this.name = name;
    }

    public abstract Optional<List<Arm>> getInvolvedArms(Combat c, Character owner, Character target,
                    List<Arm> available);
    
    public abstract void execute(Combat c, Character owner, Character target, List<Arm> arms);
    
    public boolean shouldExecute() {
        return Global.random(100) < 20;
    }
}
