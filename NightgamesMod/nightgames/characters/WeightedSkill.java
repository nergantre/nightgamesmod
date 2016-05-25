package nightgames.characters;

import nightgames.skills.Skill;

public class WeightedSkill implements Comparable<WeightedSkill> {
    public double weight;
    public double rating;
    public double raw_rating;
    public Skill skill;

    public WeightedSkill(double sum, double raw_rating2, double rating2, Skill skill) {
        this.weight = sum;
        this.skill = skill;
        this.rating = rating2;
        this.raw_rating = raw_rating2;
    }

    public WeightedSkill(double weight, Skill skill) {
        this(weight, 0., 0., skill);
    }

    @Override
    public int compareTo(WeightedSkill obj) {
        if (weight < obj.weight) {
            return -1;
        } else if (weight > obj.weight) {
            return 1;
        } else {
            return 0;
        }
    }
}
