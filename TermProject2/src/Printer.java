import java.awt.print.*;
import java.awt.*;
import java.util.*;

import javax.print.attribute.HashPrintRequestAttributeSet;

public class Printer {
	private final int PPI = 72;					// 1 인치 당 픽슬의 수
	private final int LEFT_MARGIN = PPI;		// 왼쪽 마진 	(1인치)
	private final int TOP_MARGIN = PPI;			// 위쪽 마진 	(1인치)
	private final int WIDTH = (int)(6.5*PPI);	// 폭	   	(6.5인치)
	private final int LENGTH = 9*PPI;			// 길이		(9인치)
	private final int rowsPerPage = 30;			// 한페이지에 들어갈 자료 행의 수
	private final int linesPerPage = rowsPerPage + 10;	// 한페이지에 들어갈 텍스트 줄의 수
	
	private PrintObject title;
	private RowObjects header;
	private ArrayList<RowObjects> entireList;

	private boolean pageOn;
	private Book b;
	private PageFormat fmt;

	// 페이지의 제목, 칼럼헤딩, 전체 행의 리스트, 페이지번호를 출력할지 여부를 제공
	public Printer(PrintObject title, RowObjects header, ArrayList<RowObjects> entireList, boolean pageOn) {
		this.title = title;
		this.header = header;
		this.entireList = entireList;
		this.pageOn = pageOn;
		preparePages();
	}

	public void setPagePrinting(boolean pageOn) {
		this.pageOn = pageOn;
	}

	public void print() {
		PrinterJob pj = PrinterJob.getPrinterJob();	// PrinterJob을 만들고
		pj.setPageable(b);							// PrinterJob에 이 Book을 등록
		if (pj.printDialog()) {
			try {
				pj.print();
			} catch (PrinterException pex) {
				System.out.println("프린팅 중 에러 발생 " + pex.getMessage());
			}
		}
	}

	private void preparePages() {
		Paper p = new Paper();							// 먼저 종이에 관한 정보를 설정하고
		p.setImageableArea(LEFT_MARGIN, TOP_MARGIN, WIDTH, LENGTH);

		PageFormat fmt = new PageFormat();				// 페이지 포멧에 종이 등록
		fmt.setPaper(p);

		b= new Book();									// 여러 페이지가 들어갈 Book을 만듬
		
		// strList의 내용을 rowPerPage 단위로 잘라서 출력할 수 있는 페이지를 만드는 논리 필요
		ArrayList<RowObjects> choppedList = new ArrayList<RowObjects>();
		int i;
		for (i=0; i < entireList.size(); i++) {
			choppedList.add(entireList.get(i));
			if (i%(rowsPerPage-1) == 0 && i !=0) { 		// 한 페이지에 rowPerPage 줄만 허용하기. 전체는 40줄
				b.append(new PrinterPage(linesPerPage, title, header, choppedList, pageOn), fmt);
				choppedList = new ArrayList<RowObjects>();
			}
		}
		if (choppedList.size() != 0) {					// 다 차지 않은 페이지도 마지막 페이지로
			b.append(new PrinterPage(linesPerPage, title, header, choppedList, pageOn), fmt);
		}
	}
}
