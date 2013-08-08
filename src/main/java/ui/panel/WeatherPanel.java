package ui.panel;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import net.miginfocom.swing.MigLayout;
import ui.LocalizedText;
import ui.filechooser.MediaFileChooser;
import ui.form.PlaceHolder;
import ui.form.UnitJSpinner;

public class WeatherPanel extends TabContentPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JTextField txtLocation;
	private final JCheckBox cbNational;
	private final JCheckBox cbRegional;
	private final JCheckBox cbCity;
	private final JCheckBox cbCurrentDay;
	private final JCheckBox cbTheNextDay;

	private JLabel lblNameBackgroundImageFile;
	
	private File backgroundImageFile;
	
	public WeatherPanel() {
		super(new MigLayout("ins 10", "[150:150:150]10[]", ""), LocalizedText.weather_settings);
		
		Font title = new Font(UIManager.getDefaults().getFont("Panel.font").getFamily(), Font.BOLD, 12);
		
		//Location
		JLabel lblLocationTitle = new JLabel(LocalizedText.location);
		lblLocationTitle.setFont(title);
		add(lblLocationTitle, "wrap");
		{
			cbNational = new JCheckBox(LocalizedText.national_weather);
			add(cbNational);
			
			cbRegional = new JCheckBox(LocalizedText.regional_weather);
			add(cbRegional, "wrap");
			
			cbCity = new JCheckBox(LocalizedText.city_weather);
			add(cbCity, "wrap");
		}
		
		//Displayed days
		JLabel lblDayDisplayTitle = new JLabel(LocalizedText.displayed_days);
		lblDayDisplayTitle.setFont(title);
		add(lblDayDisplayTitle, "wrap");
		{
			cbCurrentDay = new JCheckBox(LocalizedText.current_day);
			add(cbCurrentDay);
			
			cbTheNextDay = new JCheckBox(LocalizedText.the_next_day);
			add(cbTheNextDay, "wrap");
		}
		
		//Other
		JLabel lblOtherTitle = new JLabel(LocalizedText.other);
		lblOtherTitle.setFont(title);
		add(lblOtherTitle, "wrap");
		{
			//Location
			add(new JLabel(LocalizedText.location+" :"), "alignx right");
			txtLocation = new JTextField();
			PlaceHolder placeHolder = new PlaceHolder(LocalizedText.city_zip_code_state, txtLocation, 0.5f);
			placeHolder.changeStyle(Font.ITALIC);
			add(txtLocation, "wrap, wmin 180px");
			
			//Display time
			add(new JLabel(LocalizedText.display_time+" :"), "alignx right");
			add(new UnitJSpinner("sec", 0, null), "alignx left, aligny center, wrap");
			
			//Background image select
			add(new JLabel(LocalizedText.background_image+" :"), "alignx right");
			
			lblNameBackgroundImageFile = new JLabel(LocalizedText.no_file_selected);
			update(backgroundImageFile);
			add(lblNameBackgroundImageFile, "wrap, gap 10 10");

			final JButton btnSelectImage = new JButton(LocalizedText.choose_an_image);
			btnSelectImage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MediaFileChooser fileDialog = new MediaFileChooser(LocalizedText.choose_an_image, MediaFileChooser.Type.IMAGE);
					
					if(fileDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
						update(fileDialog.getSelectedFile());						
				}
			});
			add(btnSelectImage, "skip 1, wrap");
		}
	}
		
	public File getBackgroundImageFile() {
		return backgroundImageFile;
	}

	public void update(File backgroundImageFile) {
		this.backgroundImageFile = backgroundImageFile;
		if(backgroundImageFile != null) {
			FileSystemView view = FileSystemView.getFileSystemView();
			lblNameBackgroundImageFile.setText(backgroundImageFile.getName());
			lblNameBackgroundImageFile.setIcon(view.getSystemIcon(backgroundImageFile)); 
			lblNameBackgroundImageFile.setToolTipText(backgroundImageFile.getAbsolutePath());
			repaint();
		}
	}

	@Override
	public Component add(Component comp) {
		if(comp instanceof JComponent) ((JComponent) comp).setOpaque(false);
		return super.add(comp);
	}
	
	@Override
	public void add(Component comp, Object constraints) {
		if(comp instanceof JComponent) ((JComponent) comp).setOpaque(false);
		super.add(comp, constraints);
	}
	
	
	@Override
	public void add(Component comp, Object constraints, int index) {
		if(comp instanceof JComponent) ((JComponent) comp).setOpaque(false);
		super.add(comp, constraints, index);
	}
}
