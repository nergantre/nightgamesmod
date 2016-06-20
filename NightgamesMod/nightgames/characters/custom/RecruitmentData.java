package nightgames.characters.custom;

import java.util.ArrayList;
import java.util.List;

import nightgames.characters.custom.effect.CustomEffect;
import nightgames.requirement.Requirement;

public class RecruitmentData {
    public String introduction;
    public String confirm;
    public String action;
    public List<Requirement> requirement;
    public List<CustomEffect> effects;

    public RecruitmentData() {
        introduction = "default introduction";
        confirm = "default confirm";
        action = "default action";
        requirement = new ArrayList<>();
        effects = new ArrayList<>();
    }
}
