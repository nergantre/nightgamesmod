package nightgames.pet;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public abstract class Pet implements Cloneable {
    private String name;
    private PetCharacter self;
    private Ptype type;
    protected Character owner;
    private int power;
    private int ac;

    public Pet(String name, Character owner, Ptype type, int power, int ac) {
        this.owner = owner;
        this.name = name;
        this.type = type;
        this.setPower(power);
        this.setAc(ac);
        buildSelf();
    }

    protected abstract void buildSelf();

    @Override
    public String toString() {
        return name;
    }

    public Character owner() {
        return owner;
    }

    public String own() {
        return Global.capitalizeFirstLetter(owner.nameOrPossessivePronoun());
    }

    public abstract String describe();

    public void act(Combat c, Character target) {
        getSelf().act(c, target);
    }

    public abstract void vanquish(Combat c, Pet opponent);

    public abstract void caught(Combat c, Character captor);

    public Ptype type() {
        return type;
    }

    public int power() {
        return getPower();
    }

    public int ac() {
        return getAc();
    }

    public final boolean hasDick() {
        return getSelf().hasDick();
    }

    public final boolean hasPussy() {
        return getSelf().hasPussy();
    }

    public PetCharacter getSelf() {
        return self;
    }

    public void setSelf(PetCharacter self) {
        this.self = self;
    }

    public String getName() {
        return this.name;
    }

    public Pet cloneWithOwner(Character owner) throws CloneNotSupportedException {
        Pet clone = (Pet) this.clone();
        clone.owner = owner;
        return clone;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }
}
