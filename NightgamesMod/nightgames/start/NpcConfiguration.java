package nightgames.start;

import com.sun.javafx.scene.control.behavior.OptionalBoolean;
import nightgames.characters.CharacterSex;
import nightgames.characters.body.Body;
import nightgames.global.JSONUtils;
import org.json.simple.JSONObject;

import nightgames.characters.NPC;

import java.util.Optional;

import static nightgames.start.ConfigurationUtils.mergeOptionals;

public class NpcConfiguration extends CharacterConfiguration {

    // Optional because NpcConfiguration is used for both NPCs and adjustments common to all NPCs
    protected String type;
    public Optional<Boolean> isStartCharacter;

    public NpcConfiguration() {
        isStartCharacter = Optional.empty();
    }

    /** Makes a new NpcConfiguration from merging two others.
     * @param primaryConfig Will override field values from secondaryConfig.
     * @param secondaryConfig Field values will be overridden by primaryConfig.
     */
    public NpcConfiguration(NpcConfiguration primaryConfig, NpcConfiguration secondaryConfig) {
        super(primaryConfig, secondaryConfig);
        isStartCharacter = mergeOptionals(primaryConfig.isStartCharacter, secondaryConfig.isStartCharacter);
        type = primaryConfig.type;
    }

    public static Optional<NpcConfiguration> mergeOptionalNpcConfigs(Optional<NpcConfiguration> primaryConfig,
                    Optional<NpcConfiguration> secondaryConfig) {
        if (primaryConfig.isPresent()) {
            if (secondaryConfig.isPresent()) {
                return Optional.of(new NpcConfiguration(primaryConfig.get(), secondaryConfig.get()));
            } else {
                return primaryConfig;
            }
        } else {
            return secondaryConfig;
        }
    }

    public final void apply(NPC base) {
        if (gender.isPresent()) {
            CharacterSex sex = gender.get();
            base.initialGender = sex;
            // If gender is present in config, make genitals conform to it. This will be overridden if config also supplies genitals.
            if (!sex.hasCock()) {
                base.body.removeAll("cock");
            }
            if (!sex.hasPussy()) {
                base.body.removeAll("pussy");
            }
            base.body.makeGenitalOrgans(sex);
        }
        super.apply(base);
        if (isStartCharacter.isPresent()) {
            base.isStartCharacter = isStartCharacter.get();
        }
    }

    /** Parse fields from the all_npcs section.
     * @param obj The configuration from the JSON config file.
     * @return A new NpcConfiguration as specified in the config file.
     */
    public static NpcConfiguration parseAllNpcs(JSONObject obj) {
        NpcConfiguration config = new NpcConfiguration();
        config.isStartCharacter = JSONUtils.<Boolean>readOptional(obj, "start");
        config.parseCommon(obj);
        return config;
    }

    /** Parse a character-specific NPC config.
     * @param obj The configuration from the JSON config file.
     * @return A new NpcConfiguration as specified in the config file.
     */
    public static NpcConfiguration parse(JSONObject obj) {
        NpcConfiguration config = NpcConfiguration.parseAllNpcs(obj);
        config.type = JSONUtils.readOptional(obj, "type").map(Object::toString)
                        .orElseThrow(() -> new RuntimeException("Tried parsing NPC without a type."));

        return config;
    }
}
