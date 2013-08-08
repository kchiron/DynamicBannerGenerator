package ui.settings;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import ui.LocalizedText;
import ui.filechooser.MediaFileChooser;
import ui.form.UnitJSpinner;
import net.miginfocom.swing.MigLayout;
import media.element.imported.ImageElement;
import media.element.imported.InportedMediaElement;
import media.element.imported.VideoElement;

/**
 * Setting window used to edit or create an <code>InportedMediaElement</code>
 * @author gcornut
 */
public class AddModifyMediaElementWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private InportedMediaElement inportedMediaElement;
	
	private JLabel lblInportedFileName;
	private JLabel lblDisplayTime;
	
	private JTextField txtTitle;
	private UnitJSpinner unitDisplayTime;
	
	private JButton btnOk;
	private JButton btnCancel;

	private JButton btnSelectInportedFile;

	/**
	 * Constructs default layout window used to create new <code>InportedMediaElement</code>
	 */
	public AddModifyMediaElementWindow(JFrame parent) {
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		setTitle(LocalizedText.new_media);

		getContentPane().setLayout(new MigLayout("ins 10, hidemode 2", "[130]10[350]", ""));

		{//Element title
			getContentPane().add(new JLabel(LocalizedText.title+" :"), "alignx right");
			
			txtTitle = new JTextField();
			getContentPane().add(txtTitle, "wrap, wmax 180, growx");
		}

		{//Element file
			getContentPane().add(new JLabel(LocalizedText.video_or_image_file+" :"), "alignx right");
			
			lblInportedFileName = new JLabel(LocalizedText.no_file_selected);
			if(inportedMediaElement != null)
				updateData(inportedMediaElement);
			getContentPane().add(lblInportedFileName, "wrap, gap 10 10");

			btnSelectInportedFile = new JButton(LocalizedText.choose_video_or_image_file);
			btnSelectInportedFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

				}
			});
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
		btnSelectInportedFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MediaFileChooser fileChooser = new MediaFileChooser(LocalizedText.choose_video_or_image_file, MediaFileChooser.Type.IMAGE, MediaFileChooser.Type.VIDEO);
				
				if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					
				}
			}
		});
		
		//Ok button listener
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});

		//Cancel button listener
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
	
	public AddModifyMediaElementWindow(JFrame parent, InportedMediaElement inportedMediaElement) {
		this(parent);
		setTitle(LocalizedText.modify_media);
		updateData(inportedMediaElement);
		
		if(inportedMediaElement instanceof ImageElement)
			swithToImageElement();
		else if(inportedMediaElement instanceof VideoElement)
			swithToVideoElement();
	}
	
	private void swithToImageElement() {
		lblDisplayTime.setVisible(true);
		unitDisplayTime.setVisible(true);
		repaint();
	}
	
	private void swithToVideoElement() {
		lblDisplayTime.setVisible(false);
		unitDisplayTime.setVisible(false);
		repaint();
	}

	private void updateData(InportedMediaElement inportedMediaElement) {
		this.inportedMediaElement = inportedMediaElement;
		File file = inportedMediaElement.getInportedFile();
		FileSystemView view = FileSystemView.getFileSystemView();
		lblInportedFileName.setText(file.getName());
		lblInportedFileName.setIcon(view.getSystemIcon(file)); 
		lblInportedFileName.setToolTipText(file.getAbsolutePath());
		repaint();
	}

	public InportedMediaElement getInportedMediaElement() {
		return inportedMediaElement;
	}
}
