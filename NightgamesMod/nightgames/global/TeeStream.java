package nightgames.global;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class TeeStream extends OutputStream {
    private ArrayList<OutputStream> streams;

    public TeeStream(OutputStream... args) {
        streams = new ArrayList<OutputStream>();
        streams.addAll(Arrays.asList(args));
    }

    @Override
    public void write(int arg0) throws IOException {
        for (OutputStream stream : streams) {
            stream.write(arg0);
        }
    }

}
