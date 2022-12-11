import java.util.ArrayList;
import java.util.Iterator;

// 출력될 한 줄의 내용을 의미하는 클래스
public class RowObjects implements Iterator<PrintObject> {
	private ArrayList<PrintObject> row;
	private int totalLength;				// 한줄이 가지는 전체 문자열의 길이		
	private int pos;						
	
	RowObjects() {
		row = new ArrayList<PrintObject>();
		this.totalLength = 0;
		pos = -1;
	}
	
	public void reset() {
		pos = -1;
	}
	public void add(PrintObject po) {
		row.add(po);
		totalLength += po.getMaxWidth();
	}

	public boolean hasNext() {
		return pos < row.size()-1;
	}

	public PrintObject next() {
		pos++;
		return row.get(pos);
	}

	public void remove() {
	}
	
	public int getTotalLength() {
		return totalLength;
	}
}
