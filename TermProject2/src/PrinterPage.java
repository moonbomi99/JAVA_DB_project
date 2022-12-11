import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.*;
import java.util.ArrayList;

public class PrinterPage implements Printable {
	private int PPL;				// 1라인당 픽슬 수
	private int LEFT_MARGIN;		// 왼쪽 마진
	private int TOP_MARGIN;			// 위쪽 마진
	private int WIDTH;				// 폭
	private int LENGTH;				// 길이

	private int lines;
	private PrintObject title;
	private RowObjects header;
	private ArrayList<RowObjects> list;
	private boolean pageOn;

	private PageFormat fmt;

	public PrinterPage(int lines, PrintObject title, RowObjects header, ArrayList<RowObjects> list, boolean pageOn) {
		this.lines = lines;
		this.title = title;
		this.header = header;
		this.list = list;
		this.pageOn = pageOn;
	}

	public int print(Graphics g, PageFormat pf, int pageIndex) {
		LEFT_MARGIN = (int)pf.getImageableX();
		TOP_MARGIN = (int)pf.getImageableY();
		WIDTH = (int)pf.getImageableWidth();
		LENGTH = (int)pf.getImageableHeight();
		PPL = LENGTH / lines;
		g.translate(LEFT_MARGIN, TOP_MARGIN);	// 좌표체계를 용지에 맞게
		
		// 타이틀을 집어 넣는 곳
		Font savedFont = g.getFont();									// 현재 폰드는 기억해 두고
		Font nF = new Font("굴림", Font.BOLD, g.getFont().getSize()* 2);	// 굴림으로 두배 크기
		g.setFont(nF);													// 새로운 폰트가동
		int strLength = nF.getSize() * title.getStr().length();			// title의 길이를 픽슬로 알아내고
		g.drawString(title.getStr(), (WIDTH-strLength)/2, yCoordOfLine(3));	// 중앙 정렬해서 title을 출력
		g.setFont(savedFont);											// 원래 폰트 복구
			
		// 윗 분리선 : 타이틀을 위해 라인 4에 줄  그음
		g.drawLine(0, yCoordOfLine(4), WIDTH, yCoordOfLine(4));
		// 윗 분리선 : 타이틀을 위해 라인 6에 줄  그음
		g.drawLine(0, yCoordOfLine(6), WIDTH, yCoordOfLine(6));
		
		g.drawLine(0, yCoordOfLine(18),WIDTH, yCoordOfLine(18));

		if (pageOn)
			g.drawString("" + (pageIndex + 1), WIDTH/2, yCoordOfLine(20));

		// 칼럼 헤더의 출력
		RowObjects row = header;
		row.reset();
		PrintObject po;
		int yCoord, totalLength, runningLength=0;						// runningLength는현재까지 필드들의 시작 마진을 이야기 함
		totalLength = row.getTotalLength();
		while (row.hasNext()) {
			po = row.next();
			g.drawString(po.getStr(),(int)(WIDTH*(runningLength/(double) totalLength)), yCoordOfLine(5));
			runningLength += po.getMaxWidth();
		}

		for (int i=0; i< list.size(); i++) {
			row = list.get(i);
			row.reset();												// 위치를 iterator의 맨 처음으로
			yCoord = yCoordOfLine(i+7);	 // 헤더가 차지하는 행수를 뺀 것인데, 좀더 portable하게 할 필요
			totalLength = row.getTotalLength();
			runningLength = 0;
			while (row.hasNext()) {
				po = row.next();
				g.drawString(po.getStr(),(int)(WIDTH*(runningLength/(double) totalLength)), yCoord);
				runningLength += po.getMaxWidth();
			}
		}
		return Printable.PAGE_EXISTS;
	}
	
	// returns the yCoordinate of the line (lineNum)
	private int yCoordOfLine(int lineNum) {
		return PPL * (lineNum - 1);
	}
}