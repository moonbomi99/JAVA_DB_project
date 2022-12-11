import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import java.awt.Dimension;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import java.awt.print.*;
import java.util.ArrayList;
import com.lamatek.swingextras.JFontChooser;

/**
 * @author Jean-Pierre Dube <jpdube@videotron.ca>
 * @ modified by Dongik Oh <dohdoh@sch.ac.kr>  2009-11-17
 */

public class Preview extends JFrame {
	  //--- Private instances declarations
   //----------------------------------
   private JPanel viewPanel = new JPanel ();
   private PagePanel pagePanel = new PagePanel ();
   private BorderLayout mainLayout = new BorderLayout ();
   private BorderLayout pageLayout = new BorderLayout ();
   private PreviewToolBar toolbar = new PreviewToolBar (this);
   
   // Preview â�� �ʱ� ũ��. �� ���� paint �޼ҵ忡�� zoom���� 1.0����
   // ���õǾ��� ��쿡 ����� ������ �°� ���� �� ����
   private Dimension preferredSize = new Dimension (630,500);

   private int pageIndex = 0;
   private double zoom = 1.0;
   private Color color = Color.black;
   private Font currentFont = new Font("����", Font.PLAIN, 12);

   private PrintObject title;
   private RowObjects header;
   private ArrayList<RowObjects> entireList;

   private boolean pageOn;
   private Book b;
   private PageFormat fmt;

   // ������
   // �������� ����, Į�����, ��ü ���� ����Ʈ, ��������ȣ�� ������� ���θ� ����
   public Preview(PrintObject title, RowObjects header, ArrayList<RowObjects> entireList, boolean pageOn) {
	   super();
	   this.title = title;
	   this.header = header;
	   this.entireList = entireList;
	   this.pageOn = pageOn;
	   preparePages();
   }

   private void preparePages() {
	   Paper p = new Paper();							// ���� ���̸� �����ϰ�

	   PageFormat fmt = new PageFormat();				// ������ ���信 ���� ���
	   fmt.setPaper(p);

	   b= new Book();									// � �������� �� Book�� ����
		
	   // strList�� ������ 30�� ������ �߶� ����� �� �ִ� �������� ����� �� �ʿ�
	   ArrayList<RowObjects> choppedList = new ArrayList<RowObjects>();
	   int i;
	   for (i=0; i < entireList.size(); i++) {
		   choppedList.add(entireList.get(i));
		   if (i%10 == 0 && i !=0) { 					// ���������� 10�ٸ� ����ϱ� (�켱��)
			   b.append(new PrinterPage(40, title, header, choppedList, pageOn), fmt);
			   choppedList = new ArrayList<RowObjects>();
		   }
	   }
	   if (choppedList.size() != 0) {					// �� ���� ���� �������� ������ ��������
		   b.append(new PrinterPage(40, title, header, choppedList, pageOn), fmt);
	   }
   }

   public void zoomIn () {
	   if (zoom < 1.4)					// �ι� �̻��� ũ�� ����
		   zoom += 0.1;					// ���� ũ���� 10% �� ũ�� ��
	   renderPage();
   }

   public void zoomOut () {
	   if (zoom > 0.7)					// 1/2 �̻��� �۰�  ����
		   zoom -= 0.1;					// ���� ũ���� 10% �� �۰� ��
	   renderPage();
   }

   public void font() {
	   JFontChooser fc = new JFontChooser(currentFont);
	   if (fc.showDialog(this, "��Ʈ������") == JFontChooser.ACCEPT_OPTION)
		   currentFont = fc.getSelectedFont();
	   renderPage();

   }
	
   public void color () {
	   JFrame frame = new JFrame("������");
	   color=JColorChooser.showDialog(frame, "������", Color.black);
	   renderPage();
   }
	
   public void nextPage () {
      pageIndex++;
      if (pageIndex > b.getNumberOfPages()-1)
	         pageIndex--;
      renderPage ();
   }

   public void previousPage () {
      pageIndex--;
      if (pageIndex < 0)
         pageIndex = 0;
      renderPage ();
   }

   public void firstPage () {
      pageIndex = 0;
      renderPage ();
   }
	
   public void lastPage () {
      pageIndex = b.getNumberOfPages () - 1;
      renderPage ();
   }
	
   public void print () {
	   PrinterJob pj = PrinterJob.getPrinterJob();
	   pj.setPageable(b);
	   if (pj.printDialog()) {
		   try {
			   pj.print();
		   } catch (PrinterException pex) {
			   System.out.println("������ �� ���� �߻� " + pex.getMessage());
		   }
	   }
   }
	
   // �� �޼ҵ忡�� ���� �������� ����� �ָ� ��
   private void renderPage () {
	   pagePanel.renderPage (b.getPrintable(pageIndex));
   }
	
   public Dimension getPreferredSize () {
      return (preferredSize);
   }
	
   public void preview () {
	  this.getContentPane ().setLayout (mainLayout);
      this.getContentPane ().add (toolbar, BorderLayout.NORTH);
      this.getContentPane ().add (pagePanel, BorderLayout.CENTER);

      /* �Ʒ� �� ���� ��ũ�� ������ ����� ���� ������, ������ ũ�⸦ �����ٲپ� ���� �ʴ� �����
       * paint()���� ����ϹǷ�, �׳� â�� �ø��� �ϴ� ���� ���� �� ����.
	 	this.getContentPane ().setLayout (mainLayout);
	 	JScrollPane js = new JScrollPane(pagePanel); 
	 	js.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	 	js.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	 	this.getContentPane ().add (toolbar, BorderLayout.NORTH);
     	this.getContentPane ().add (js, BorderLayout.CENTER);
       */

     this.setTitle ("Print preview");
     this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
     this.setVisible(true);
     this.pack ();
	
     //--- Init the pagePanel
     pagePanel.setBackground (Color.white);
     pagePanel.setBorder (BorderFactory.createLineBorder (Color.lightGray, 10));
     pagePanel.setPreferredSize (preferredSize);
	
     renderPage ();
   }

   private class PagePanel extends JPanel {
      private PrinterPage page;

      public PagePanel () {
         super ();
      }
	
      // �ش�Ǵ� �������� �����ϰ� repaint()�� ȣ���Ͽ� ��µǰ� �� (paint()�� ȣ���)
      public void renderPage (Printable parPage) {
         page = (PrinterPage)parPage;
         repaint ();
      }
	
      public void paint (Graphics parG) {
         super.paint (parG);
	
         Dimension size = this.getSize ();
         BufferedImage doubleBuffer = new BufferedImage (size.width, size.height, BufferedImage.TYPE_INT_RGB);
         Graphics2D g2d = (Graphics2D) doubleBuffer.getGraphics ();
         g2d.setColor (Color.white);					// ����
         g2d.fillRect (0, 0, size.width, size.height);  // ��� ����� �����
         g2d.scale (zoom, zoom);						// zoom ������ ���� ���ũ�� ����
         g2d.setColor (color);							// ��»�
         g2d.setFont(currentFont);
	
         if (page != null)
            page.print (g2d, new PageFormat (), 0);
	
         if (doubleBuffer != null)
            parG.drawImage (doubleBuffer, 0, 0, this);
	     }
      }
	
   public class PreviewToolBar extends JToolBar implements ActionListener {
	   //--- Private instances declarations
	   //----------------------------------
	   //--- Buttons
	   private JButton firstPage = new JButton ();
	   private JButton lastPage = new JButton ();
	   private JButton nextPage = new JButton ();
	   private JButton previousPage = new JButton ();
	   private JButton zoomIn = new JButton ();
	   private JButton zoomOut = new JButton ();
	   private JButton font = new JButton ();
	   private JButton color = new JButton ();
	   private JButton print = new JButton ();
	
	   Preview preview;

	   public PreviewToolBar (Preview parPreview) {
         super ();
         preview = parPreview;
         init ();
	   }
	
      private void init () {
    	  // ���ٿ� ����� ��ư���� ����
         firstPage.setText("ù������");
         firstPage.setActionCommand ("firstPage");
         firstPage.addActionListener (this);
	
         previousPage.setText("����������");
         previousPage.setActionCommand ("previousPage");
         previousPage.addActionListener (this);
	
         nextPage.setText("����������");
         nextPage.setActionCommand ("nextPage");
         nextPage.addActionListener (this);
	
         lastPage.setText("������������");
         lastPage.setActionCommand ("lastPage");
         lastPage.addActionListener (this);

         zoomIn.setText("������Ȯ��");
         zoomIn.setActionCommand ("zoomIn");
         zoomIn.addActionListener (this);
	
         zoomOut.setText("���������");
         zoomOut.setActionCommand ("zoomOut");
         zoomOut.addActionListener (this);

         font.setText("��Ʈ");
         font.setActionCommand ("font");
         font.addActionListener (this);

         color.setText("����");
         color.setActionCommand ("color");
         color.addActionListener (this);

         print.setText("���");
         print.setActionCommand ("print");
         print.addActionListener (this);
	
         // toolbar�� �ʱ�ȭ
         this.add (firstPage);
         this.add (previousPage);
         this.add (nextPage);
         this.add (lastPage);
         this.add (zoomIn);
         this.add (zoomOut);
         this.add (font);
         this.add (color);
         this.add (print);
      }

      public void actionPerformed (ActionEvent parEvent) {
         String command = parEvent.getActionCommand ();
         
         if (command.equals ("nextPage"))
        	preview.nextPage ();
         else if (command.equals ("previousPage"))
            preview.previousPage ();
         else if (command.equals ("firstPage"))
            preview.firstPage ();
         else if (command.equals ("lastPage"))
             preview.lastPage ();
         else if (command.equals ("zoomIn"))
             preview.zoomIn();
         else if (command.equals ("zoomOut"))
             preview.zoomOut();
         else if (command.equals ("font"))
             preview.font();
         else if (command.equals ("color"))
             preview.color();
         else if (command.equals ("print"))
            preview.print ();
      }
	
   }
}// Preview