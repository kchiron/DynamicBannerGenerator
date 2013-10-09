package ui.listview.contextmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import data.media.element.MediaElement;
import data.media.element.imported.ImportedMediaElement;
import ui.LocalizedText;
import ui.listview.ListViewCell;

public class ListViewContextMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	
	public ListViewContextMenu(MediaElement element, final ListViewCell parent) {
		if(element instanceof ImportedMediaElement) {
			//Remove option
			JMenuItem remove = new JMenuItem(LocalizedText.remove);
			add(remove);
			
			//Edit option
			JMenuItem edit = new JMenuItem(LocalizedText.edit);
			add(edit);	
			
			//Edit listener
			edit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					parent.edit();
				}
			});
			
			//Remove listener
			remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					parent.remove();
				}
			});
		}
	}
}
