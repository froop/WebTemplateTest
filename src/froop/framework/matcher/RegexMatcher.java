package froop.framework.matcher;

import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * http://hakobera.hatenablog.com/entry/20101225/1293296275
 */
public class RegexMatcher extends TypeSafeMatcher<String> {

	public static RegexMatcher matches(String criteria) {
		return new RegexMatcher(criteria);
	}

	private final String criteria;
	private final Pattern pattern;

	public RegexMatcher(String criteria) {
		this.criteria = criteria;
		this.pattern = Pattern.compile(criteria);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is matched /").appendValue(criteria)
				.appendText("/");
	}

	@Override
	public boolean matchesSafely(String object) {
		return null != object && null != criteria
				&& pattern.matcher(object).matches();
	}
}
