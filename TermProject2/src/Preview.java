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
   
   // Preview 창의 초기 크기. 이 값은 paint 메소드에서 zoom값이 1.0으로
   // 세팅되었을 경우에 제대로 윤곽이 맞게 설정 된 것임
   private Dimension preferredSize = new Dimension (630,500);

   private int pageIndex = 0;
   private double zoom = 1.0;
   private Color color = Color.black;
   private Font currentFont = new Font("굴림", Font.PLAIN, 12);

   private PrintObject title;
   private RowObjects header;
   private ArrayList<RowObjects> entireList;

   private boolean pageOn;
   private Book b;
   private PageFormat fmt;

   // 구성자
   // 페이지의 제목, 칼럼헤딩, 전체 행의 리스트, 페이지번호를 출력할지 여부를 제공
   public Preview(PrintObject title, RowObjects header, ArrayList<RowObjects> entireList, boolean pageOn) {
	   super();
	   this.title = title;
	   this.header = header;
	   this.entireList = entireList;
	   this.pageOn = pageOn;
	   preparePages();
   }

   private void preparePages() {
	   Paper p = new Paper();							// 먼저 종이를 설정하고

	   PageFormat fmt = new PageFormat();				// 페이지 포멧에 종이 등록
	   fmt.setPaper(p);

	   b= new Book();									// 어러 페이지가 들어갈 Book을 만듬
		
	   // strList의 내용을 30줄 단위로 잘라서 출력할 수 있는 페이지를 만드는 논리 필요
	   ArrayList<RowObjects> choppedList = new ArrayList<RowObjects>();
	   int i;
	   for (i=0; i < entireList.size(); i++) {
		   choppedList.add(entireList.get(i));
		   if (i%10 == 0 && i !=0) { 					// 한페이지에 10줄만 허용하기 (우선은)
			   b.append(new PrinterPage(40, title, header, choppedList, pageOn), fmt);
			   choppedList = new ArrayList<RowObjects>();
		   }
	   }
	   if (choppedList.size() != 0) {					// 다 차지 않은 페이지도 마지막 페이지로
		   b.append(new PrinterPage(40, title, header, choppedList, pageOn), fmt);
	   }
   }

   public void zoomIn () {
	   if (zoom < 1.4)					// 두배 이상은 크게 안함
		   zoom += 0.1;					// 원래 크기의 10% 씩 크게 함
	   renderPage();
   }

   public void zoomOut () {
	   if (zoom > 0.7)					// 1/2 이상은 작게  안함
		   zoom -= 0.1;					// 원래 크기의 10% 씩 작게 함
	   renderPage();
   }

   public void font() {
	   JFontChooser fc = new JFontChooser(currentFont);
	   if (fc.showDialog(this, "폰트선택자") == JFontChooser.ACCEPT_OPTION)
		   currentFont = fc.getSelectedFont();
	   renderPage();

   }
	
   public void color () {
	   JFrame frame = new JFrame("색상선택");
	   color=JColorChooser.showDialog(frame, "색상선택", Color.black);
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
			   System.out.println("프린팅 중 에러 발생 " + pex.getMessage());
		   }
	   }
   }
	
   // 이 메소드에서 현재 페이지를 출력해 주면 됨
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

      /* 아래 와 같이 스크롤 패인을 사용할 수도 있으나, 글자의 크기를 직접바꾸어 주지 않는 방식을
       * paint()에서 사용하므로, 그냥 창을 늘리게 하는 것이 나을 것 같다.
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
	
      // 해당되는 페이지를 지정하고 repaint()를 호출하여 출력되게 함 (paint()가 호출됨)
      public void renderPage (Printable parPage) {
         page = (PrinterPage)parPage;
         repaint ();
      }
	
      public void paint (Graphics parG) {
         super.paint (parG);
	
         Dimension size = this.getSize ();
         BufferedImage doubleBuffer = new BufferedImage (size.width, size.height, BufferedImage.TYPE_INT_RGB);
         Graphics2D g2d = (Graphics2D) doubleBuffer.getGraphics ();
         g2d.setColor (Color.white);					// 배경색
         g2d.fillRect (0, 0, size.width, size.height);  // 흰색 배경을 만들고
         g2d.scale (zoom, zoom);						// zoom 비율에 따라 출력크기 조정
         g2d.setColor (color);							// 출력색
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
    	  // 툴바에 사용할 버튼들의 설정
         firstPage.setText("첫페이지");
         firstPage.setActionCommand ("firstPage");
         firstPage.addActionListener (this);
	
         previousPage.setText("이전페이지");
         previousPage.setActionCommand ("previousPage");
         previousPage.addActionListener (this);
	
         nextPage.setText("다음페이지");
         nextPage.setActionCommand ("nextPage");
         nextPage.addActionListener (this);
	
         lastPage.setText("마지막페이지");
         lastPage.setActionCommand ("lastPage");
         lastPage.addActionListener (this);

         zoomIn.setText("페이지확대");
         zoomIn.setActionCommand ("zoomIn");
         zoomIn.addActionListener (this);
	
         zoomOut.setText("페이지축소");
         zoomOut.setActionCommand ("zoomOut");
         zoomOut.addActionListener (this);

         font.setText("폰트");
         font.setActionCommand ("font");
         font.addActionListener (this);

         color.setText("색상");
         color.setActionCommand ("color");
         color.addActionListener (this);

         print.setText("출력");
         print.setActionCommand ("print");
         print.addActionListener (this);
	
         // toolbar의 초기화
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