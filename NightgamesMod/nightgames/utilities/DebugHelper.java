package nightgames.utilities;

public class DebugHelper {
    public static void printStackFrame(int nNumberOfNGCalls, int nSkipped) {
        nSkipped += 2;
        for (StackTraceElement stackTraceElement: Thread.currentThread().getStackTrace()) {
            if (nSkipped > 0) {
                nSkipped -= 1;
                continue;
            }
            if (stackTraceElement.getClassName().startsWith("nightgames")) {
                nNumberOfNGCalls -= 1;
                System.out.println(stackTraceElement.toString());
            }
            if (nNumberOfNGCalls <= 0) {
                break;
            }
        }
    }
}
