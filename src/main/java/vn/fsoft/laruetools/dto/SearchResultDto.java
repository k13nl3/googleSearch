package vn.fsoft.laruetools.dto;

public class SearchResultDto {
	private String text;
	private String link;
	private boolean found;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public SearchResultDto(String text, String link, boolean found) {
		super();
		this.text = text;
		this.link = link;
		this.found = found;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}
}
