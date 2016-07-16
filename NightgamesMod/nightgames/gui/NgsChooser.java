package nightgames.gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Optional;

/**
 * Save file dialog specialized for .ngs files.
 */
public class NgsChooser extends JFileChooser {
    private static final long serialVersionUID = 1726133121559035386L;
    private Component parent;

    public NgsChooser(GUI gui) {
        super("./");
        FileFilter savesFilter = new FileNameExtensionFilter("Nightgame Saves", "ngs");
        addChoosableFileFilter(savesFilter);
        setFileFilter(savesFilter);
        setMultiSelectionEnabled(false);

        this.parent = gui;
    }

    public Optional<File> askForSaveFile() {
        int rv = showSaveDialog(parent);

        if (rv != JFileChooser.APPROVE_OPTION) {
            return Optional.empty();
        }
        File saveFile = getSelectedFile();
        String fname = saveFile.getAbsolutePath();
        if (!fname.endsWith(".ngs")) {
            fname += ".ngs";
        }
        return Optional.of(new File(fname));
    }
}
