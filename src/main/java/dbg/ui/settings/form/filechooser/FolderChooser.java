package dbg.ui.settings.form.filechooser;

import java.io.File;

import javax.swing.JFileChooser;

public class FolderChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	public FolderChooser(String title) {
		super(new File("."));
	    setDialogTitle(title);
	    setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    setAcceptAllFileFilterUsed(false);
	}

}
