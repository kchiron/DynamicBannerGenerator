package dbg.data.media;

import dbg.data.media.element.MediaElement;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Data representation of a list of data.media originalElement later merged as a video sequence.
 * @author gcornut
 */
public class MediaSequence extends ArrayList<MediaElement> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public MediaSequence() {}
	
	/**
	 * Gets all elements in the sequence having the given class
	 */
	@SuppressWarnings("unchecked")
	public <T extends MediaElement> ArrayList<T> getElementsByClass(Class<T> elementClass) {
		final ArrayList<T> elements = new ArrayList<>();

		for(MediaElement element : this)
			if(elementClass.isInstance(element))
				elements.add(elementClass.cast(element));
		
		return elements;
	}

	public void removeAllElementsByClass(Class<? extends MediaElement> elementClass) {
		final ArrayList<MediaElement> elementToRemove = new ArrayList<>();
		
		for(MediaElement element : this)
			if(element.getClass().equals(elementClass))
				elementToRemove.add(element);
		
		for(MediaElement element : elementToRemove)
			remove(element);
	}
	
	public int getDuration() {
		int duration = 0;
		for(MediaElement e: this)
			duration += e.getDuration();
		return duration;
	}
}
