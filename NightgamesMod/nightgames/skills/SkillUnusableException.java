package nightgames.skills;

/**
 * SkillUnusableException is intended for situations where something goes wrong when resolving the use of a skill,
 * after first checking whether it was usable.
 */
public class SkillUnusableException extends RuntimeException {

    private static final long serialVersionUID = 2449705009333893124L;
    private final Skill skill;

    public SkillUnusableException(Skill skill) {
        this.skill = skill;
    }

    @Override public String getMessage() {
        return "Something went wrong between checking whether " + skill.getName()
                        + " was usable and resolving the skill.";
    }
}
