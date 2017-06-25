package vn.fsoft.googlesearch.dto;

public class SearchResultDto {
	private String keyword;
	private String text;
	private String link;
	private boolean foundInPage;
	private boolean foundInCache;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public SearchResultDto() {
		super();
	}


	public SearchResultDto(String keyword, String text, String link, boolean foundInPage, boolean foundInCache) {
		super();
		this.keyword = keyword;
		this.text = text;
		this.link = link;
		this.foundInPage = foundInPage;
		this.foundInCache = foundInCache;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}


	public boolean isFoundInPage() {
		return foundInPage;
	}

	public void setFoundInPage(boolean foundInPage) {
		this.foundInPage = foundInPage;
	}

	public boolean isFoundInCache() {
		return foundInCache;
	}

	public void setFoundInCache(boolean foundInCache) {
		this.foundInCache = foundInCache;
	}
}
