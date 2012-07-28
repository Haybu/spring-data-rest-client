package at.furti.springrest.client.http.link;

import org.springframework.util.Assert;

/**
 * A Link exposed by the repositoryexposer.
 * 
 * @author Daniel
 * 
 */
public class Link {

	private String rel;
	private String href;

	public Link() {
		super();
	}

	public Link(String rel, String href) {
		super();
		Assert.notNull(rel);
		Assert.notNull(href);

		this.rel = rel;
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public String getHref() {
		return href;
	}

	@Override
	public int hashCode() {
		return rel.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Link && ((Link) obj).getRel().equals(rel);
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public void setHref(String href) {
		this.href = href;
	}
}
