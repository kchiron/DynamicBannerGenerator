package ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;

import exception.ZeroOrNegativeNumberException;
import media.MediaSequence;
import media.element.MediaElement;
import media.element.generated.HoroscopeElement;
import media.element.generated.WeatherElement;
import media.element.imported.ImageElement;
import media.element.imported.VideoElement;
import net.miginfocom.swing.MigLayout;
import ui.LocalizedText;
import ui.form.IconButton;
import ui.listview.ListView;

public class SequencePanel extends TabContentPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JButton up;
	private JButton down;
	private JButton plus;
	private JButton minus;
	private final ListView listView;

	public SequencePanel() {
		super(new MigLayout("ins 0, gap 0px 2px", "[][][grow][][]", "[][grow][]"), LocalizedText.sequence_settings);
		setMinimumSize(new Dimension(170, (int)getMinimumSize().getHeight()));
		
		add(new JLabel(LocalizedText.playlist), "cell 0 0 4 1, grow");
		
		final Border fancy = new CompoundBorder(
			// Outside border 1px bottom light color
			new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 255, 255)),
			// Border all around panel 1px dark grey 
			new LineBorder(new Color(154, 154, 154), 1)
		);
		
		MediaSequence elements = new MediaSequence();
		elements.add(new WeatherElement("Périgueux, France", WeatherElement.Type.CITY));
		elements.add(new HoroscopeElement("Horoscope", "poisson, bélier"));
		elements.add(new VideoElement("Publicité", new File("toto.mp4")));
		elements.add(new HoroscopeElement("Horoscope", "tarreau, sagitaire"));
		elements.add(new WeatherElement("Aquitaine, France", WeatherElement.Type.REGIONAL));
		try {
			elements.add(new ImageElement("Pub image", new File("tata.jpg"), 5));
		} catch (ZeroOrNegativeNumberException e) {e.printStackTrace();}
		
		listView = new ListView(elements, this);
		
		JScrollPane scrollPane = new JScrollPane(listView);
		scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(Color.white);
		scrollPane.setBorder(fancy);
		
		add(scrollPane, "cell 0 1 4 1, grow");
		
		final Dimension butSize = new Dimension(28, 21);
		{ // Up, Down button
			up = createMiniIconButton("up.png", "^", butSize);
			up.setEnabled(false);
			up.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent paramActionEvent) {
					listView.swapSelectedRow(ListView.SwapDirection.UP);
				}
			});

			down = createMiniIconButton("down.png", "v", butSize);
			down.setEnabled(false);
			down.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent paramActionEvent) {
					listView.swapSelectedRow(ListView.SwapDirection.DOWN);
				}
			});
			
			add(up, "cell 0 2, alignx right");
			add(down, "cell 1 2, alignx right");
		}
		
		{ // Plus, minus button
			plus = createMiniIconButton("plus.png", "+", butSize);
			
			minus = createMiniIconButton("minus.png", "-", butSize);
			minus.setEnabled(false);
			minus.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent paramActionEvent) {
					listView.removeSelectedRow();
				}
			});
			
			add(plus, "cell 2 2, alignx right");
			add(minus, "cell 3 2, alignx right");
		}
	}
	
	public MediaSequence getSequence() {
		return listView.getSequence();
	}
	
	private JButton createMiniIconButton(String pathIcon, String textFallback, Dimension size) {
		Font f = new Font("Lucida Grande", Font.BOLD, 10);
		IconButton button = new IconButton(getClass().getResource(pathIcon), textFallback, f);
		button.setMaximumSize(size);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.putClientProperty("JComponent.sizeVariant", "small");
		return button;
	}
	
	/**
	 * Updates the up and down buttons according to whether a row is selected and if this row is at the top or at the bottom.
	 * @param selected if a row is selected
	 * @param isTop if a row is selected and is at the top
	 * @param isBottom if a row is selected and is at the bottom
	 * @param isDeletable if a row is deletable
	 */
	public void rowSelected(boolean selected, boolean isTop, boolean isBottom, boolean isDeletable) {
		if(isBottom) down.setEnabled(false);
		else down.setEnabled(selected);
		
		if(isTop) up.setEnabled(false);
		else up.setEnabled(selected);
		
		if(isDeletable) minus.setEnabled(true);
		else minus.setEnabled(false);
	}
	 
}
