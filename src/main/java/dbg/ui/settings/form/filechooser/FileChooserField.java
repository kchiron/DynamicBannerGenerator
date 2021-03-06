package dbg.ui.settings.form.filechooser;

import dbg.ui.LocalizedText;
import dbg.ui.util.UiUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * A panel grouping a button that launches a JFileChooser and a label that displays the current selected file.
 *
 * @author gcornut
 */
public class FileChooserField extends JPanel {

	private static final long serialVersionUID = 1L;

	private final BorderLayout layout;
	private final JFileChooser fileChooser;
	private final JLabel lblFileName;

	private final String noSelectionMessage;

	private final ArrayList<ActionListener> customListeners;
	private final JLabel errorIcon;

	/**
	 * Constructs a file chooser field panel
	 *
	 * @param currentDirectory   the directory opened in the file chooser
	 * @param fileChooser        the file chooser to use with the field
	 * @param selectMessage      the message displayed on the button that launches the file chooser
	 * @param noSelectionMessage the message displayed in the label when no file/directory is selected
	 */
	public FileChooserField(File currentDirectory, JFileChooser fileChooser, String selectMessage, String noSelectionMessage) {
		super();

		this.customListeners = new ArrayList<>(1);
		this.noSelectionMessage = noSelectionMessage;

		setLayout(layout = new BorderLayout());
		setOpaque(false);

		lblFileName = new JLabel(noSelectionMessage);
		lblFileName.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		add(lblFileName, BorderLayout.NORTH);

		JButton btnChooseFile = new JButton(selectMessage);
		add(btnChooseFile, BorderLayout.CENTER);

		errorIcon = UiUtils.createErrorIcon();

		this.fileChooser = fileChooser;
		this.fileChooser.setCurrentDirectory(currentDirectory);
		btnChooseFile.addActionListener(new FileChooserListener(this));
	}

	private void updateFile(File selectedFile, boolean externalModification) {
		if (selectedFile == null) {
			lblFileName.setText(noSelectionMessage);
			lblFileName.setIcon(null);
			lblFileName.setToolTipText(null);

		} else {
			boolean adapted = false;
			if (selectedFile.isDirectory() && selectedFile.getName().equals("")) {
				adapted = true;
				selectedFile = new File(selectedFile.getAbsolutePath());
			}

			FileSystemView view = FileSystemView.getFileSystemView();
			lblFileName.setText(selectedFile.getName());
			lblFileName.setIcon(view.getSystemIcon(selectedFile));
			lblFileName.setToolTipText(selectedFile.getAbsolutePath());

			if(externalModification || adapted) {
				fileChooser.setSelectedFile(selectedFile);
				fileChooser.setCurrentDirectory(selectedFile);
			}
		}
		checkError();
	}

	public void checkError() {
		Component layoutComponent = layout.getLayoutComponent(BorderLayout.EAST);
		if (layoutComponent != null) remove(layoutComponent);
		if (fileChooser.getSelectedFile() == null) {
			errorIcon.setToolTipText(LocalizedText.get("please_choose_background_image"));
			add(errorIcon, BorderLayout.EAST);
		}
		else if (!fileChooser.getSelectedFile().exists()) {
			errorIcon.setToolTipText(LocalizedText.get("error.message.file_not_found"));
			add(errorIcon, BorderLayout.EAST);
		}
		repaint();
	}

	/**
	 * Adds custom action listener to the file chooser field that triggers when a file/directory have been selected in the file chooser dialog
	 *
	 * @param listener
	 */
	public void addActionListener(ActionListener listener) {
		customListeners.add(listener);
	}

	/**
	 * Gets the current selected file of the file chooser field
	 *
	 * @return
	 */
	public File getFile() {
		return fileChooser.getSelectedFile();
	}

	/**
	 * Sets the file selected in the file chooser field
	 *
	 * @param file
	 */
	public void setFile(File file) {
		fileChooser.setSelectedFile(file);
		updateFile(file, true);
	}

	private class FileChooserListener implements ActionListener {
		private final FileChooserField parent;

		public FileChooserListener(FileChooserField parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent paramActionEvent) {
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				updateFile(fileChooser.getSelectedFile(), false);

				for (ActionListener listener : customListeners)
					listener.actionPerformed(new ActionEvent(parent, ActionEvent.ACTION_PERFORMED, "file changed"));
			}
		}
	}

}
