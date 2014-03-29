package dbg.ui.settings.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import net.miginfocom.swing.MigLayout;
import dbg.data.media.MediaSequence;
import dbg.data.media.element.MediaElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.data.property.PropertyManager;
import dbg.ui.LocalizedText;
import dbg.ui.form.IconButton;
import dbg.ui.settings.AddModifyMediaElementWindow;
import dbg.ui.settings.listview.ListView;
import dbg.ui.settings.listview.ListViewModel;

public class SequencePanel extends TabContentPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JButton up;
	private final JButton down;
	private final JButton minus;
	private final ListView listView;
	private final JFrame parentFrame;

	private final JLabel sequenceInfo;

	public SequencePanel(final JFrame parent) {
		super(new MigLayout("ins 0, gap 0px 2px", "[][][grow][][]", "[][grow][]"), LocalizedText.sequence_settings);
		setMinimumSize(new Dimension(170, (int)getMinimumSize().getHeight()));

		this.parentFrame = parent;
		
		add(new JLabel(LocalizedText.playlist), "cell 0 0 5 1, grow");
		
		final Border fancy = new CompoundBorder(
			// Outside border 1px bottom light color
			new MatteBorder(0, 0, 1, 0, new Color(255, 255, 255)),
			// Border all around panel 1px dark grey 
			new LineBorder(new Color(154, 154, 154), 1)
		);
		
		listView = new ListView(PropertyManager.getSequence(), this);
		
		JScrollPane scrollPane = new JScrollPane(listView);
		scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(Color.white);
		scrollPane.setBorder(fancy);
		
		add(scrollPane, "cell 0 1 5 1, grow");
		
		final Dimension butSize = new Dimension(28, 21);
		{ // Up, Down button
			up = createMiniIconButton("up.png", "^", butSize);
			up.setEnabled(false);

			down = createMiniIconButton("down.png", "v", butSize);
			down.setEnabled(false);
			
			add(up, "cell 0 2, alignx right");
			add(down, "cell 1 2, alignx right");
		}
		
		{ // Sequence list info 
			sequenceInfo = new JLabel();
			Font f = sequenceInfo.getFont();
			sequenceInfo.setFont(new Font(f.getFamily(), f.getStyle(), 9));
			add(sequenceInfo, "cell 2 2, alignx center");
		}

		JButton plus;
		{ // Plus, minus button
			plus = createMiniIconButton("plus.png", "+", butSize);
			
			minus = createMiniIconButton("minus.png", "-", butSize);
			minus.setEnabled(false);
			
			add(plus, "cell 3 2, alignx right");
			add(minus, "cell 4 2, alignx right");
		}

		//Minus listener
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				listView.removeSelectedRow();
			}
		});
		
		
		//Plus listener
		final SequencePanel sequencePanel = this;
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				(new AddModifyMediaElementWindow(parent, sequencePanel)).setVisible(true);
			}
		});
		
		//Down listener
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				listView.swapSelectedRow(ListView.SwapDirection.DOWN);
			}
		});
		
		//Up listener
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				listView.swapSelectedRow(ListView.SwapDirection.UP);
			}
		});

	}
	
	public void setSequence(MediaSequence sequence) {
		listView.setSequence(sequence);
		updateSequenceInfo();
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
	
	private void updateSequenceInfo() {
		sequenceInfo.setText(this.getSequence().size() + " " + LocalizedText.elements);
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
	
	public void addElement(MediaElement element) {
		listView.addRow(element);
		//updateSequenceInfo();
	}
	 
	public void updateElement(MediaElement oldElement, MediaElement newElement) {
		listView.replaceElement(oldElement, newElement);
		//updateSequenceInfo();
	}
	
	public void modifyRow(ImportedMediaElement element) {
		(new AddModifyMediaElementWindow(parentFrame, this, element)).setVisible(true);
		//updateSequenceInfo();
	}

	public void refreshList() {
		((ListViewModel)listView.getModel()).fireTableStructureChanged();
		updateSequenceInfo();
	}
}
