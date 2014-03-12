package dbg.ui.settings;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.ffmpeg.FileExtended;
import dbg.ffmpeg.SupportedFileFormat;
import dbg.ui.LocalizedText;
import dbg.ui.form.UnitJSpinner;
import dbg.ui.form.filechooser.MediaFileChooser;
import dbg.ui.settings.panel.SequencePanel;
import net.miginfocom.swing.MigLayout;

/**
 * Setting window used to edit or create an <code>ImportedMediaElement</code>
 * @author gcornut
 */
public class AddModifyMediaElementWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private ImportedMediaElement oldInportedMediaElement;
	private ImportedMediaElement importedMediaElement;
	
	private JLabel lblInportedFileName;
	private JLabel lblDisplayTime;
	
	private JTextField txtTitle;
	private UnitJSpinner unitDisplayTime;
	
	private JButton btnOk;
	private JButton btnCancel;

	private JButton btnSelectInportedFile;
	
	/**
	 * Constructs default layout window used to create new <code>ImportedMediaElement</code>
	 */
	public AddModifyMediaElementWindow(JFrame parent, final SequencePanel sequencePanel) {
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		
		setTitle(LocalizedText.new_media);

		getContentPane().setLayout(new MigLayout("ins 10, hidemode 2", "[130]10[250]", ""));

		{//Element title
			getContentPane().add(new JLabel(LocalizedText.title+" :"), "alignx right");
			
			txtTitle = new JTextField();
			getContentPane().add(txtTitle, "wrap, wmax 180, growx");
		}

		{//Element file
			getContentPane().add(new JLabel(LocalizedText.video_or_image_file+" :"), "alignx right");
			
			lblInportedFileName = new JLabel(LocalizedText.no_file_selected);
			getContentPane().add(lblInportedFileName, "wrap, growx, wmax 200, gap 10 10");

			btnSelectInportedFile = new JButton(LocalizedText.choose_video_or_image_file);
			getContentPane().add(btnSelectInportedFile, "skip 1, wrap");
		}

		{//Display time (for ImageElement only)
			lblDisplayTime = new JLabel(LocalizedText.display_time+" :");
			lblDisplayTime.setVisible(false);
			getContentPane().add(lblDisplayTime, "alignx right");
			
			unitDisplayTime = new UnitJSpinner("sec", 0, null);
			unitDisplayTime.setVisible(false);
			getContentPane().add(unitDisplayTime, "wrap");
		}
		
		{//Cancel and OK buttons
			getContentPane().add(new JLabel());
			
			btnCancel = new JButton(LocalizedText.cancel);
			getContentPane().add(btnCancel, "alignx right, split");

			btnOk = new JButton(LocalizedText.ok);
			getContentPane().add(btnOk);
		}
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		
		getRootPane().setDefaultButton(btnOk);
		
		//SelectInportedFile button listener
		final MediaFileChooser fileChooser = new MediaFileChooser(LocalizedText.choose_video_or_image_file, MediaFileChooser.Type.IMAGE, MediaFileChooser.Type.VIDEO);
		btnSelectInportedFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					fileChooser.setCurrentDirectory(importedMediaElement.getFile().getParentFile());
				} catch(NullPointerException e1) {}
				
				if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						FileExtended file = new FileExtended(fileChooser.getSelectedFile());
						String extension = file.getExtension().toLowerCase();

						if(SupportedFileFormat.getImageFormatsList().contains(extension)){
							updateData(ImageElement.class, null, file, 0);
							swithToImageElement();
						}
						else if(SupportedFileFormat.getVideoFormatsList().contains(extension)) {
							updateData(VideoElement.class, null, file, 0);
							swithToVideoElement();
						}
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, LocalizedText.error_file_not_found, LocalizedText.file_not_found, JOptionPane.ERROR_MESSAGE); 
						e1.printStackTrace();
					}
				}
			}
		});
		
		final JDialog dialog = this;
		//Ok button listener
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> errors = new ArrayList<String>(); 
				
				String title = txtTitle.getText();
				if(title.equals(""))
					errors.add(LocalizedText.error_empty_title);
				else {
					Class<? extends ImportedMediaElement> elementClass = 
						(importedMediaElement == null) ? VideoElement.class : importedMediaElement.getClass();
					
					updateData(elementClass, title, null, 0);
				}
					
				
				if(importedMediaElement == null || importedMediaElement.getFile() == null)
					errors.add(LocalizedText.error_no_file_selected);
				
				if(importedMediaElement != null && importedMediaElement instanceof ImageElement) {
					int value = unitDisplayTime.getValue();
					if(value <= 0)
						errors.add(LocalizedText.error_display_time_zero);
					else
						updateData(importedMediaElement.getClass(), null, null, value);	
				}
				
				if(errors.size() > 0) {
					String message = LocalizedText.error_please_correct+"\n";
					
					for(String error: errors)
						message += "- "+error+"\n";
					
					JOptionPane.showMessageDialog(dialog, message, LocalizedText.missing_information, JOptionPane.ERROR_MESSAGE);
				}
				else {
					if(oldInportedMediaElement != null)
						sequencePanel.updateElement(oldInportedMediaElement, importedMediaElement);
					else 
						sequencePanel.addElement(importedMediaElement);
					dispose();
				}
			}
		});

		//Cancel button listener
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	public AddModifyMediaElementWindow(JFrame parent, SequencePanel sequencePanel, ImportedMediaElement importedMediaElement) {
		this(parent, sequencePanel);
		setTitle(LocalizedText.modify_media);
		this.oldInportedMediaElement = importedMediaElement;
		updateData(importedMediaElement);
		
		txtTitle.setText(importedMediaElement.getTitle());
		
		if(importedMediaElement instanceof ImageElement) {
			unitDisplayTime.setValue(((ImageElement)importedMediaElement).getDuration());
			swithToImageElement();
		}
		else if(importedMediaElement instanceof VideoElement)
			swithToVideoElement();
	}
	
	private void swithToImageElement() {
		lblDisplayTime.setVisible(true);
		unitDisplayTime.setVisible(true);
		pack();
	}
	
	private void swithToVideoElement() {
		lblDisplayTime.setVisible(false);
		unitDisplayTime.setVisible(false);
		pack();
	}

	private void updateData(Class<? extends ImportedMediaElement> elementClass, String title, FileExtended inportedFile, int displayTime) {
		if(elementClass == ImageElement.class && importedMediaElement == null) {
			importedMediaElement = new ImageElement(
				title != null ? title : "", 
				inportedFile != null ? inportedFile : null, 
				displayTime > 0 ? displayTime : 1
			);
		}
		else if(elementClass == VideoElement.class && importedMediaElement == null) {
			importedMediaElement = new VideoElement(
				title != null ? title : "", 
				inportedFile != null ? inportedFile : null
			);
		}
		else if(importedMediaElement != null) {
			if(importedMediaElement.getClass() != elementClass) {
				if(elementClass == ImageElement.class) {
					importedMediaElement = new ImageElement(
						title != null ? title :importedMediaElement.getTitle(), 
						inportedFile != null ? inportedFile : importedMediaElement.getFile(), 
						displayTime > 0 ? displayTime : 1
					);
				}
				else if(elementClass == VideoElement.class) {
					importedMediaElement = new VideoElement(
						title != null ? title :importedMediaElement.getTitle(), 
						inportedFile != null ? inportedFile : importedMediaElement.getFile()
					);
				}
			}
		}

		if(title != null && importedMediaElement != null)
			importedMediaElement.setTitle(title);
		
		if(inportedFile != null && importedMediaElement != null)
			importedMediaElement.setFile(inportedFile);
		
		if(displayTime > 0 && importedMediaElement instanceof ImageElement)
			((ImageElement)importedMediaElement).setDuration(displayTime);
		
		refreshUi();
	}
	
	private void updateData(ImportedMediaElement importedMediaElement) {
		this.importedMediaElement = importedMediaElement;
		refreshUi();
	}
	
	private void refreshUi() {
		try {
			File file = importedMediaElement.getFile();
			
			FileSystemView view = FileSystemView.getFileSystemView();
			lblInportedFileName.setText(file.getName());
			lblInportedFileName.setIcon(view.getSystemIcon(file)); 
			lblInportedFileName.setToolTipText(file.getAbsolutePath());
		} catch(NullPointerException e) {
			lblInportedFileName.setText(LocalizedText.no_file_selected);
			lblInportedFileName.setIcon(null);
			lblInportedFileName.setToolTipText("");
		}
		lblInportedFileName.revalidate();
	}

	public ImportedMediaElement getInportedMediaElement() {
		return importedMediaElement;
	}
}
