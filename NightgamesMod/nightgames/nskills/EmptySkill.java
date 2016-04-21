package nightgames.nskills;

public class EmptySkill extends GenericSkill {
    static class Holder {
        private final static EmptySkill INSTANCE = new EmptySkill();
    }

    public EmptySkill() {
        super("EMPTY");
    }

    public static SkillInterface getInstance() {
        return Holder.INSTANCE;
    }
}
