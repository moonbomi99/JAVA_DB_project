// 이 클래스는 출력이 이루어질 단위(문자열)를 나타내는 클래스 임
public class PrintObject {
	private String str;
	private int maxWidth;
	
	// Constructor. length indicates the maximum number of characters str can have.
	// We use this information when we actually allocate space on pages. Or, we may
	// adjust the font-size to make the str fit to the space allocated.
	PrintObject (String str, int maxWidth) {
		this.str = str;
		this.maxWidth=maxWidth;
	}
	public String getStr() {
		return str;
	}
	public int getMaxWidth() {
		return maxWidth;
	}
}
