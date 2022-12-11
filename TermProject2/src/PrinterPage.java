import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.*;
import java.util.ArrayList;

public class PrinterPage implements Printable {
	private int PPL;				// 1���δ� �Ƚ� ��
	private int LEFT_MARGIN;		// ���� ����
	private int TOP_MARGIN;			// ���� ����
	private int WIDTH;				// ��
	private int LENGTH;				// ����

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
		g.translate(LEFT_MARGIN, TOP_MARGIN);	// ��ǥü�踦 ������ �°�
		
		// Ÿ��Ʋ�� ���� �ִ� ��
		Font savedFont = g.getFont();									// ���� ����� ����� �ΰ�
		Font nF = new Font("����", Font.BOLD, g.getFont().getSize()* 2);	// �������� �ι� ũ��
		g.setFont(nF);													// ���ο� ��Ʈ����
		int strLength = nF.getSize() * title.getStr().length();			// title�� ���̸� �Ƚ��� �˾Ƴ���
		g.drawString(title.getStr(), (WIDTH-strLength)/2, yCoordOfLine(3));	// �߾� �����ؼ� title�� ���
		g.setFont(savedFont);											// ���� ��Ʈ ����
			
		// �� �и��� : Ÿ��Ʋ�� ���� ���� 4�� ��  ����
		g.drawLine(0, yCoordOfLine(4), WIDTH, yCoordOfLine(4));
		// �� �и��� : Ÿ��Ʋ�� ���� ���� 6�� ��  ����
		g.drawLine(0, yCoordOfLine(6), WIDTH, yCoordOfLine(6));
		
		g.drawLine(0, yCoordOfLine(18),WIDTH, yCoordOfLine(18));

		if (pageOn)
			g.drawString("" + (pageIndex + 1), WIDTH/2, yCoordOfLine(20));

		// Į�� ����� ���
		RowObjects row = header;
		row.reset();
		PrintObject po;
		int yCoord, totalLength, runningLength=0;						// runningLength��������� �ʵ���� ���� ������ �̾߱� ��
		totalLength = row.getTotalLength();
		while (row.hasNext()) {
			po = row.next();
			g.drawString(po.getStr(),(int)(WIDTH*(runningLength/(double) totalLength)), yCoordOfLine(5));
			runningLength += po.getMaxWidth();
		}

		for (int i=0; i< list.size(); i++) {
			row = list.get(i);
			row.reset();												// ��ġ�� iterator�� �� ó������
			yCoord = yCoordOfLine(i+7);	 // ����� �����ϴ� ����� �� ���ε�, ���� portable�ϰ� �� �ʿ�
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