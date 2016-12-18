package nightgames.global;

/**
 * Indicates whether it is daytime (shopping and talking) or nighttime (sex-fighting).
 */
public enum Time {
    DAY("day"), NIGHT("night");

    final String desc;

    Time(String desc) {
        this.desc = desc;
    }

    static Time fromDesc(String desc) {
        desc = compatibility(desc);
        return Time.valueOf(desc.toUpperCase());
    }

    /**
     * Maps old descriptors to current descriptors to maintain save file compatibility.
     * <br/><br/>
     * Older save files used different descriptors for DAY and NIGHT (and different enum constants).
     *
     * @param oldDesc A description that may or may not be from an old version.
     * @return A current description suitable for use in fromDesc().
     */
    private static String compatibility(String oldDesc) {
        switch (oldDesc) {
            case "dawn":
                return DAY.desc;
            case "dusk":
                return NIGHT.desc;
            default:
                return oldDesc;
        }
    }
}
