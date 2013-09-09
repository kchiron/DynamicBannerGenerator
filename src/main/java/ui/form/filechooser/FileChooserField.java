package ui.form.filechooser;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

/**
 * A panel grouping a button that launches a JFileChooser and a label that displays the current selected file.
 * @author gcornut
 */
public class FileChooserField extends JPanel {

	private static final long serialVersionUID = 1L;
	private final JFileChooser fileChooser;
	
	private final JLabel lblFileName;
	private final JButton btnChooseFile;
	
	private final String noSelectionMessage;
	
	private final ArrayList<ActionListener> customListeners;
	
	/**
	 * Constructs a file chooser field panel
	 * @param currentDirectory the directory opened in the file chooser
	 * @param fileChooser the file chooser to use with the field
	 * @param selectMessage the message displayed on the button that launches the file chooser
	 * @param noSelectionMessage the message displayed in the label when no file/directory is selected
	 */
	public FileChooserField(File currentDirectory, JFileChooser fileChooser, String selectMessage, String noSelectionMessage) {
		super();
		
		this.customListeners = new ArrayList<ActionListener>(1);
		this.noSelectionMessage = noSelectionMessage;
		
		setLayout(new BorderLayout());
		setOpaque(false);
		
		lblFileName = new JLabel(noSelectionMessage);
		lblFileName.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		add(lblFileName, BorderLayout.NORTH);
		
		btnChooseFile = new JButton(selectMessage);
		add(btnChooseFile, BorderLayout.CENTER);
		
		this.fileChooser = fileChooser;
		this.fileChooser.setCurrentDirectory(currentDirectory);
		btnChooseFile.addActionListener(new FileChooserListener(this));
	}
	
	private void updateFile(File selectedFile) {
		try {
			FileSystemView view = FileSystemView.getFileSystemView();
			lblFileName.setText(selectedFile.getName());
			lblFileName.setIcon(view.getSystemIcon(selectedFile)); 
			lblFileName.setToolTipText(selectedFile.getAbsolutePath());
		} catch(NullPointerException e) {
			lblFileName.setText(noSelectionMessage);
			lblFileName.setIcon(null);
			lblFileName.setToolTipText(null);
		}
		revalidate();
	}
	
	/**
	 * Adds custom action listener to the file chooser field that triggers when a file/directory have been selected in the file chooser dialog
	 * @param listener
	 */
	public void addActionListener(ActionListener listener) {
		customListeners.add(listener);
	}
	
	/**
	 * Gets the current selected file of the file chooser field
	 * @return
	 */
	public File getFile() {
		return fileChooser.getSelectedFile();
	}
	
	/**
	 * Sets the file selected in the file chooser field
	 * @param file
	 */
	public void setFile(File file) {
		fileChooser.setSelectedFile(file);
		updateFile(file);
	}
	
	private class FileChooserListener implements ActionListener {
		private final FileChooserField parent;
		
		public FileChooserListener(FileChooserField parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent paramActionEvent) {
			if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				updateFile(fileChooser.getSelectedFile());	
				
				for(ActionListener listener: customListeners)
					listener.actionPerformed(new ActionEvent(parent, ActionEvent.ACTION_PERFORMED, "file changed"));
			}
		}
	}
}
