package nightgames.pet.arms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.mods.ArcaneMod;
import nightgames.characters.body.mods.CyberneticMod;
import nightgames.characters.body.mods.DivineMod;
import nightgames.characters.body.mods.FeralMod;
import nightgames.characters.body.mods.FieryMod;
import nightgames.characters.body.mods.GooeyMod;
import nightgames.characters.body.mods.PlantMod;
import nightgames.characters.body.mods.DemonicMod;
import nightgames.characters.body.mods.TentacledMod;
import nightgames.characters.body.mods.PartMod;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.pet.PetCharacter;
import nightgames.pet.arms.skills.ArmSkill;
import nightgames.pet.arms.skills.DoubleGrab;
import nightgames.pet.arms.skills.Idle;
import nightgames.pet.arms.skills.MultiArmMove;

public class ArmManager {
    private static final List<MultiArmMove> MULTI_MOVES = Arrays.asList(new DoubleGrab());

    private List<Arm> arms;

    public ArmManager() {
        arms = new ArrayList<>();
    }

    public ArmManager instance() {
        ArmManager newManager = new ArmManager();
        arms.forEach(arm -> newManager.arms.add(arm.instance()));
        return newManager;
    }

    public void selectArms(Character owner) {
        arms.clear();
        if (owner.has(Trait.octo)) {
            if (owner.level < 30) {
                if (Global.randomdouble() < .5) {
                    arms.add(new Grabber(this));
                    arms.add(new Grabber(this));
                    arms.add(new ToyArm(this));
                } else {
                    arms.add(new Stripper(this));
                    arms.add(new Stripper(this));
                    arms.add(new ToyArm(this));
                }
            } else {
                double r = Global.randomdouble();
                if (r > .75) {
                    arms.add(new Grabber(this));
                    arms.add(new Grabber(this));
                    arms.add(new HeatCannon(this));
                    arms.add(new Stripper(this));
                    arms.add(new ToyArm(this));
                } else if (r > .5) {
                    arms.add(new Grabber(this));
                    arms.add(new Grabber(this));
                    arms.add(new Stabilizer(this));
                    arms.add(new Stabilizer(this));
                    arms.add(new ToyArm(this));
                } else if (r > .25) {
                    arms.add(new HealCannon(this));
                    arms.add(new Stripper(this));
                    arms.add(new DefabCannon(this));
                    arms.add(new HeatCannon(this));
                    arms.add(new ToyArm(this));
                } else {
                    arms.add(new Stabilizer(this));
                    arms.add(new Stabilizer(this));
                    arms.add(new HeatCannon(this));
                    arms.add(new DefabCannon(this));
                    arms.add(new ToyArm(this));
                }
            }
        }
        if (owner.has(Trait.Pseudopod) && owner.has(Trait.slime)) {
            addArm(new TentacleClinger(this));
            if (owner.level >= 58 && owner.has(Trait.Imposter)) {
                addArm(new TentacleImpaler(this, Global.pickRandom(IMPALER_MODS)));
                addArm(new TentacleSucker(this, Global.pickRandom(SUCKER_MODS)));
            } else if (owner.level >= 28) {
                addArm(new TentacleImpaler(this, Optional.empty()));
                addArm(new TentacleSucker(this, Optional.empty()));
            }
            if (owner.level >= 48) {
                addArm(new TentacleInjector(this));
            }
            if (owner.level >= 58 && owner.has(Trait.VolatileSubstrate)) {
                addArm(new TentacleSquirter(this));
            }
        }
    }
    
    private static final List<? extends PartMod> IMPALER_MODS = Collections.unmodifiableList(CockMod.ALL_MODS);
    private static final List<? extends PartMod> SUCKER_MODS = Arrays.asList(
                    new ArcaneMod(), new CyberneticMod(),
                    new DivineMod(), new FeralMod(),
                    new FieryMod(), new GooeyMod(),
                    new PlantMod(), new DemonicMod(),
                    new TentacledMod());

    public void addArm(Arm arm) {
        arms.add(arm);
    }

    public int armCount() {
        return arms.size();
    }

    public List<Arm> getActiveArms() {
        return new ArrayList<>(arms);
    }

    private String describeArms(List<? extends Arm> arms) {
        Map<ArmType, List<Arm>> grouped = arms.stream()
                                                  .collect(Collectors.groupingBy(Arm::getType));
        int counter = 0;
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<ArmType, List<Arm>> e : grouped.entrySet()) {
            int amt = e.getValue()
                       .size();
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

    public String describe(Character owner) {
        List<RoboArm> roboArms = arms.stream().filter(arm -> arm instanceof RoboArm).map(arm -> (RoboArm)arm).collect(Collectors.toList());
        List<TentacleArm> tentacleArms = arms.stream().filter(arm -> arm instanceof TentacleArm).map(arm -> (TentacleArm)arm).collect(Collectors.toList());
        String msg = "";
        if (!roboArms.isEmpty()) {
            msg += "<b>You can see " + describeArms(roboArms) + " strapped behind "
                            + owner.possessiveAdjective() + " back.</b><br/>";
        }
        if (!tentacleArms.isEmpty()) {
            msg += "You can see " + tentacleArms.size() + " tentacles attached to " + owner.possessiveAdjective() + " back.<br/>";
            msg += tentacleArms.stream().map(arm -> arm.describe()).collect(Collectors.joining("<br/>"));
            msg += "<br/>";
        }
        return msg;
    }

    private List<Arm> handleMultiArmMoves(Combat c, Character owner, Character target) {
        List<Arm> remaining = arms;
        Collections.shuffle(MULTI_MOVES);
        for (MultiArmMove mam : MULTI_MOVES) {
            if (mam.shouldExecute()) {
                Optional<List<Arm>> used = mam.getInvolvedArms(c, owner, target, remaining);
                if (used.isPresent()) {
                    remaining.removeAll(used.get());
                    mam.execute(c, owner, target, used.get());
                }
            }
        }
        return remaining;
    }

    private void doArmAction(Arm arm, Combat c, Character owner, Character target) {
        if (arm.attackOdds(c, owner, target) > Global.random(100)) {
            Optional<ArmSkill> skill = Global.pickRandom(arm.getSkills(c, owner, target)
                                                  .stream()
                                                  .filter(s -> s.usable(c, arm, owner, target))
                                                  .toArray(ArmSkill[]::new));
            if (skill.isPresent()) {
                c.write(PetCharacter.DUMMY, String.format("<b>%s %s uses %s</b>", owner.nameOrPossessivePronoun(),
                                arm.getName(), skill.get().getName()));
                skill.get().resolve(c, arm, owner, target);
                return;
            }
        }
        new Idle().resolve(c, arm, owner, target);
    }

    public void act(Combat c, Character owner, Character target) {
        if (arms.isEmpty()) {
            return;
        }
        List<Arm> available = handleMultiArmMoves(c, owner, target);
        available.forEach(a -> doArmAction(a, c, owner, target));
    }
}