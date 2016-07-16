package nightgames.requirement;

import org.junit.Test;

import java.util.Arrays;

/**
 *
 */
public class RequirementTest {

    @Test public void orTest() throws Exception {
        Requirement req = new OrRequirement(Arrays.<Requirement>asList(
                        new Requirement[] {new TrueRequirement(), new FalseRequirement(), new FalseRequirement()}));
    }
}
