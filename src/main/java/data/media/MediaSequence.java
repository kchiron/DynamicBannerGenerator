package data.media;

import java.io.Serializable;
import java.util.ArrayList;

import data.media.element.MediaElement;
import data.media.element.generated.WeatherElement;

/**
 * Data representation of a list of data.media element later merged as a video sequence.
 * @author gcornut
 */
public class MediaSequence extends ArrayList<MediaElement> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public MediaSequence() {}
	
	/**
	 * Gets all elements in the sequence having the given class
	 */
	@SuppressWarnings("unchecked")
	public <T extends MediaElement> ArrayList<T> getElementsByClass(Class<? extends MediaElement> elementClass) {
		final ArrayList<T> elements = new ArrayList<T>();
		
		for(MediaElement element : this)
			if(element.getClass().equals(elementClass))
				elements.add((T)element);
		
		return elements;
	}

	public void removeAllElementsByClass(Class<? extends MediaElement> elementClass) {
		final ArrayList<MediaElement> elementToRemove = new ArrayList<MediaElement>();
		
		for(MediaElement element : this)
			if(element.getClass().equals(elementClass))
				elementToRemove.add(element);
		
		for(MediaElement element : elementToRemove)
			remove(element);
	}
	
}
