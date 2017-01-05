package nightgames.pet;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.characters.custom.CharacterLine;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.Outfit;

public class NPCPetCharacter extends PetCharacter {
    private NPC prototype;
    private Map<String, List<CharacterLine>> lines;

    public NPCPetCharacter(String name, Pet self, NPC prototypeCharacter, int level) throws CloneNotSupportedException {
        super(self, name, prototypeCharacter.getType() + "Pet", prototypeCharacter.getGrowth(), 1);
        prototype = prototypeCharacter.clone();
        prototype.ai.applyBasicStats(this);
        for (int i = 1; i < level; i++) {
            this.level += 1;
            getGrowth().levelUp(this);
        }
        distributePoints(prototypeCharacter.ai.getPreferredAttributes());
        this.getSkills().clear();
        this.body = prototypeCharacter.body.clone(this);
        this.outfit = new Outfit(prototypeCharacter.outfit);
        this.mojo.empty();
        this.arousal.empty();
        this.stamina.fill();
        this.lines = Collections.unmodifiableMap(prototype.getLines());
        Global.learnSkills(this);
    }

    @Override
    public PetCharacter cloneWithOwner(Character owner) throws CloneNotSupportedException {
        NPCPetCharacter clone = (NPCPetCharacter) super.cloneWithOwner(owner);
        clone.prototype = prototype.clone();
        return clone;
    }

    public String getRandomLineFor(String lineType, Combat c, Character other) {
        return Global.pickRandom(lines.get(lineType)).orElse((cb, sf, ot) -> "").getLine(c, this, other);
    }

    public String getRandomLineFor(String lineType, Combat c) {
        Character other = c == null ? null : c.getOpponent(this);
        return getRandomLineFor(lineType, c, other);
    }

    @Override
    public String challenge(Character other) {
        return getRandomLineFor(CharacterLine.CHALLENGE, null, other);
    }

    @Override
    public String orgasmLiner(Combat c) {
        return getRandomLineFor(CharacterLine.ORGASM_LINER, c);
    }

    @Override
    public String makeOrgasmLiner(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.MAKE_ORGASM_LINER, c);
    }

    @Override
    public String bbLiner(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.BB_LINER, c);
    }

    @Override
    public String nakedLiner(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.NAKED_LINER, c);
    }

    @Override
    public String stunLiner(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.STUNNED_LINER, c);
    }

    @Override
    public String taunt(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.TAUNT_LINER, c);
    }

    @Override
    public String temptLiner(Combat c, Character target) {
        return getRandomLineFor(CharacterLine.TEMPT_LINER, c);
    }

    @Override
    public String getPortrait(Combat c) {
        return prototype.ai.image(c);
    }
}