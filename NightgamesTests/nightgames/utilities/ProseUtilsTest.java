package nightgames.utilities;

import org.junit.Assert;
import org.junit.Test;

public class ProseUtilsTest {
    @Test
    public void testBaseCases() {
        Assert.assertEquals("makes", ProseUtils.getThirdPersonFromFirstPerson("make"));
        Assert.assertEquals("eats", ProseUtils.getThirdPersonFromFirstPerson("eat"));
        Assert.assertEquals("runs", ProseUtils.getThirdPersonFromFirstPerson("run"));
        Assert.assertEquals("finds", ProseUtils.getThirdPersonFromFirstPerson("find"));
    }

    @Test
    public void testEsEndings() {
        Assert.assertEquals("echoes", ProseUtils.getThirdPersonFromFirstPerson("echo"));
        Assert.assertEquals("retches", ProseUtils.getThirdPersonFromFirstPerson("retch"));
        Assert.assertEquals("caresses", ProseUtils.getThirdPersonFromFirstPerson("caress"));
        Assert.assertEquals("fixes", ProseUtils.getThirdPersonFromFirstPerson("fix"));
        Assert.assertEquals("pushes", ProseUtils.getThirdPersonFromFirstPerson("push"));
    }

    @Test
    public void testIesEndings() {
        Assert.assertEquals("tries", ProseUtils.getThirdPersonFromFirstPerson("try"));
        Assert.assertEquals("parries", ProseUtils.getThirdPersonFromFirstPerson("parry"));
        Assert.assertEquals("carries", ProseUtils.getThirdPersonFromFirstPerson("carry"));
    }

    @Test
    public void testSpecialCases() {
        Assert.assertEquals("is", ProseUtils.getThirdPersonFromFirstPerson("are"));
        Assert.assertEquals("has", ProseUtils.getThirdPersonFromFirstPerson("have"));
        Assert.assertEquals("should", ProseUtils.getThirdPersonFromFirstPerson("should"));
    }
}
