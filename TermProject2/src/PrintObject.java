// �� Ŭ������ ����� �̷���� ����(���ڿ�)�� ��Ÿ���� Ŭ���� ��
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
