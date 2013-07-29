package ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import net.miginfocom.swing.MigLayout;
import ui.LocalizedText;
import ui.custom.TabContentPanel;
import ui.custom.listview.ListView;
import ui.custom.listview.Sequence;

public class SequencePanel extends TabContentPanel {
	
	private static final long serialVersionUID = 1L;

	public SequencePanel() {
		super(new MigLayout("ins 0, gap 0px 2px", "[grow][]", "[grow][]"), LocalizedText.sequence_settings);
		setMinimumSize(new Dimension(170, (int)getMinimumSize().getHeight()));
		
		final Border fancy = new CompoundBorder(
			// Outside border 1px bottom light color
			new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 255, 255)),
			// Border all around panel 1px dark grey 
			new LineBorder(new Color(154, 154, 154), 1)
		);
		
		List<Sequence> elements = new ArrayList<Sequence>();
		elements.add(new Sequence("Météo ville", "Périgueux, France", Sequence.Type.weather));
		elements.add(new Sequence("Horoscope", "poisson, bélier", Sequence.Type.horoscope));
		elements.add(new Sequence("Publicité", "sous-titre", Sequence.Type.video));
		elements.add(new Sequence("Horoscope", "tarreau, sagitaire", Sequence.Type.horoscope));
		elements.add(new Sequence("Météo région", "Aquitaine, France", Sequence.Type.weather));
		
		ListView listView = new ListView(elements);
		listView.setDragEnabled(true);
		
		
		JScrollPane scrollPane = new JScrollPane(listView);
		scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(Color.white);
		scrollPane.setBorder(fancy);
		
		add(scrollPane, "cell 0 0 2 1,grow");
		
		{ // Plus, minus button
			final Dimension butSize = new Dimension(40, 40);
			
			JButton plus = new JButton("+");
			plus.setMaximumSize(butSize);
			plus.setHorizontalTextPosition(SwingConstants.CENTER);
			plus.setVerticalAlignment(SwingConstants.CENTER);
			plus.putClientProperty("JComponent.sizeVariant", "small");
			
			JButton minus = new JButton("-");
			minus.setMaximumSize(butSize);
			minus.setHorizontalTextPosition(SwingConstants.CENTER);
			minus.setVerticalAlignment(SwingConstants.CENTER);
			minus.setEnabled(false);
			minus.putClientProperty("JComponent.sizeVariant", "small");
			
			add(plus, "cell 0 2, alignx right");
			add(minus, "cell 1 2, alignx right");
		}
	}
}
