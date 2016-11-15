package nightgames.pet;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.Outfit;

public class PlayerPetCharacter extends PetCharacter {
    private Player prototype;

    public PlayerPetCharacter(String name, Pet self, Player prototypeCharacter, int level) throws CloneNotSupportedException {
        super(self, name, prototypeCharacter.getType() + "Pet", prototypeCharacter.getGrowth(), 1);
        prototype = (Player) prototypeCharacter.clone();
        prototype.applyBasicStats(this);
        for (int i = 1; i < level; i++) {
            getGrowth().levelUp(this);
            prototype.getLevelUpFor(i).apply(this);;
        }
        this.att = new HashMap<>(prototype.att);
        this.traits = new CopyOnWriteArrayList<>(prototype.traits);
        this.getSkills().clear();
        this.body = prototypeCharacter.body.clone(this);
        this.outfit = new Outfit(prototypeCharacter.outfit);
        this.mojo.empty();
        this.arousal.empty();
        this.stamina.fill();
        Global.learnSkills(this);
    }

    @Override
    public String bbLiner(Combat c, Character target) {
        return "";
    }

    @Override
    public String nakedLiner(Combat c, Character target) {
        return "";
    }

    @Override
    public String stunLiner(Combat c, Character target) {
        return "";
    }

    @Override
    public String taunt(Combat c, Character target) {
        return "";
    }

    @Override
    public String temptLiner(Combat c, Character target) {
        return "";
    }

    @Override
    public String challenge(Character other) {
        return "";
    }

    @Override
    public String getPortrait(Combat c) {
        return "";
    }
}