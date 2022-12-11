import java.awt.print.*;
import java.awt.*;
import java.util.*;

import javax.print.attribute.HashPrintRequestAttributeSet;

public class Printer {
	private final int PPI = 72;					// 1 ��ġ �� �Ƚ��� ��
	private final int LEFT_MARGIN = PPI;		// ���� ���� 	(1��ġ)
	private final int TOP_MARGIN = PPI;			// ���� ���� 	(1��ġ)
	private final int WIDTH = (int)(6.5*PPI);	// ��	   	(6.5��ġ)
	private final int LENGTH = 9*PPI;			// ����		(9��ġ)
	private final int rowsPerPage = 30;			// ���������� �� �ڷ� ���� ��
	private final int linesPerPage = rowsPerPage + 10;	// ���������� �� �ؽ�Ʈ ���� ��
	
	private PrintObject title;
	private RowObjects header;
	private ArrayList<RowObjects> entireList;

	private boolean pageOn;
	private Book b;
	private PageFormat fmt;

	// �������� ����, Į�����, ��ü ���� ����Ʈ, ��������ȣ�� ������� ���θ� ����
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
		PrinterJob pj = PrinterJob.getPrinterJob();	// PrinterJob�� �����
		pj.setPageable(b);							// PrinterJob�� �� Book�� ���
		if (pj.printDialog()) {
			try {
				pj.print();
			} catch (PrinterException pex) {
				System.out.println("������ �� ���� �߻� " + pex.getMessage());
			}
		}
	}

	private void preparePages() {
		Paper p = new Paper();							// ���� ���̿� ���� ������ �����ϰ�
		p.setImageableArea(LEFT_MARGIN, TOP_MARGIN, WIDTH, LENGTH);

		PageFormat fmt = new PageFormat();				// ������ ���信 ���� ���
		fmt.setPaper(p);

		b= new Book();									// ���� �������� �� Book�� ����
		
		// strList�� ������ rowPerPage ������ �߶� ����� �� �ִ� �������� ����� �� �ʿ�
		ArrayList<RowObjects> choppedList = new ArrayList<RowObjects>();
		int i;
		for (i=0; i < entireList.size(); i++) {
			choppedList.add(entireList.get(i));
			if (i%(rowsPerPage-1) == 0 && i !=0) { 		// �� �������� rowPerPage �ٸ� ����ϱ�. ��ü�� 40��
				b.append(new PrinterPage(linesPerPage, title, header, choppedList, pageOn), fmt);
				choppedList = new ArrayList<RowObjects>();
			}
		}
		if (choppedList.size() != 0) {					// �� ���� ���� �������� ������ ��������
			b.append(new PrinterPage(linesPerPage, title, header, choppedList, pageOn), fmt);
		}
	}
}
