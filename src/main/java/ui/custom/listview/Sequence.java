package ui.custom.listview;

public class Sequence {

	private String title;
	private String subTitle;
	private Type type;
	
	public Sequence(String title, String subTitle, Type type) {
		super();
		this.title = title;
		this.subTitle = subTitle;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public Type getType() {
		return type;
	}

	public enum Type {
		weather, video, horoscope;
	}
}
