package dbg.ui.settings.listview.contextmenu;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import dbg.data.media.element.MediaElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.ui.LocalizedText;
import dbg.ui.settings.listview.ListViewCell;

public class ListViewContextMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	
	public ListViewContextMenu(final MediaElement element, final ListViewCell parent) {
		if(element instanceof ImportedMediaElement) {
			//Remove option
			JMenuItem remove = new JMenuItem(LocalizedText.remove);
			add(remove);
			
			//Edit option
			JMenuItem edit = new JMenuItem(LocalizedText.edit);
			add(edit);	
			
			//Show in Finder/explorer
			JMenuItem show = new JMenuItem(LocalizedText.show);
			add(show);
			
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
			
			//Show listener 
			show.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Desktop.getDesktop().open(((ImportedMediaElement) element).getFile());
					} catch (IllegalArgumentException e1) {
						JOptionPane.showMessageDialog(null, LocalizedText.error_file_not_found, LocalizedText.error, JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, LocalizedText.error_cant_display, LocalizedText.error, JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
	}
}
