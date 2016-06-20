package nightgames.requirement;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Ryplinn on 6/17/2016.
 */
public class RequirementTest {

    @Test public void orTest() throws Exception {
        Requirement req = new OrRequirement(Arrays.<Requirement>asList(
                        new Requirement[] {new TrueRequirement(), new FalseRequirement(), new FalseRequirement()}));
    }
}
