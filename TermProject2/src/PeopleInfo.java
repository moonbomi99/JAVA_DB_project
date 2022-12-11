import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;

import se.datadosen.component.RiverLayout;

public class PeopleInfo {
	// DB 관련 변수들
	Connection conn;  // DB 연결 Connection 객체참조변수
	// 최상위 프레임
	JFrame frame;
	String frameTitle = "감귤도서관";
	// 텍스트 박스들
	JTextField name;
	JTextField birthday;
	JTextField phone;
	JTextField book;
	JTextField borrowdate;
	JTextField returndate;
	// 색인을 위한 박스
	JTextField search;  // 색인을 위한  필드
	// 라디오 버튼들
	JRadioButton borrowbook=new JRadioButton("대출 가능");
	JRadioButton noreturn=new JRadioButton("연 체");
	// 버튼들
	JButton bSearch;
	JButton save;
	JButton delete;
	JButton bNew;
	JButton bPrint;
	JButton preview;
	JButton borrow;
	JButton bReturn;
	JButton newsave;
	// 리스트
	JList names = new JList(); 
	Choice books=new Choice();
	Choice mybook=new Choice();
	public static void main(String[] args) {
		PeopleInfo client = new PeopleInfo();
		client.setUpGUI();
		client.dbConnectionInit();
	}
	private void setUpGUI()  {
		// build GUI
		frame = new JFrame(frameTitle);
		JPanel leftTopPanel = new JPanel(new RiverLayout());
		JScrollPane scroller = new JScrollPane(names);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		names.setVisibleRowCount(10);
		names.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		names.setFixedCellWidth(150);
		leftTopPanel.add("br center", new JLabel("감 귤 도 서 관  회 원 리 스 트"));
		leftTopPanel.add("p center", scroller);
		leftTopPanel.add("p br center",new JLabel("<<도  서  목  록>>"));
		leftTopPanel.add("p center",books);
		// 입력 창들과 라벨 (오른쪽 상단 패널)
		JPanel righttopPanel = new JPanel(new RiverLayout());
		name=new JTextField(25);
		birthday=new JTextField(25);
		phone=new JTextField(25);
		book=new JTextField(25);
		borrowdate=new JTextField(6);
		returndate=new JTextField(6);
		borrow=new JButton("대출");
		bReturn=new JButton("반납");
		newsave=new JButton("저장");
		ButtonGroup is_return=new ButtonGroup();
		is_return.add(borrowbook);
		is_return.add(noreturn);
		//표식을 위한 라벨들
		
		righttopPanel.add("br center",new JLabel("회 원 정 보"));
		righttopPanel.add("p left",new JLabel("이  름"));
		righttopPanel.add("tap",name);
		righttopPanel.add("br",new JLabel("생 년 월 일"));
		righttopPanel.add("tap",birthday);
		righttopPanel.add("br",new JLabel("전 화 번 호"));
		righttopPanel.add("tap",phone);
		righttopPanel.add("br",new JLabel("대 출 도 서"));
		righttopPanel.add("tap",mybook);
		
		righttopPanel.add("br",new JLabel("대 출 일"));
		righttopPanel.add(borrowdate);
		righttopPanel.add(new JLabel("반 납 일"));
		righttopPanel.add(returndate);
		
		righttopPanel.add("tap",borrowbook);
		righttopPanel.add("tab", noreturn);
		// 왼쪽 하단 패널
		JPanel leftBottomPanel = new JPanel(new RiverLayout());
		
		JPanel tmpPanel = new JPanel(new RiverLayout());
		JPanel tmpPanel1 = new JPanel(new RiverLayout());
		JPanel tmpPanel2 = new JPanel(new RiverLayout());
		search = new JTextField(20);
		bSearch = new JButton("검색");
		bPrint = new JButton("출력");
		preview = new JButton("미리보기");
		tmpPanel1.add("tap",search);
		tmpPanel2.add(bSearch);
		tmpPanel2.add(bPrint);
		tmpPanel2.add(preview);
		tmpPanel.add("center",tmpPanel1);
		tmpPanel.add("br cneter",tmpPanel2);
		leftBottomPanel.add("center", tmpPanel);
		// 오른쪽 하단 패널
		JPanel rightBottomPanel = new JPanel(new RiverLayout());
		
		tmpPanel = new JPanel(new RiverLayout());
		save = new JButton("수정");
		delete = new JButton("삭제");
		bNew = new JButton("신규");
		tmpPanel.add(borrow);
		tmpPanel.add("tab",bReturn);
		tmpPanel.add("tab",save);
		tmpPanel.add("tap", delete);
		tmpPanel.add("tab", bNew);
		tmpPanel.add("tab",newsave);
		rightBottomPanel.add("center", tmpPanel);
		rightBottomPanel.add("br", Box.createRigidArea(new Dimension(0,20)));
		// GUI 배치
		JPanel topPanel = new JPanel(new GridLayout(1,2));
		topPanel.add(leftTopPanel);
		topPanel.add(righttopPanel);
		JPanel bottomPanel = new JPanel(new GridLayout(1,2));
		bottomPanel.add(leftBottomPanel);
		bottomPanel.add(rightBottomPanel);
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(topPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		names.addListSelectionListener(new nameListListener());
		mySearchListener l=new mySearchListener();
		search.addActionListener(l);
		bSearch.addActionListener(l);
		save.addActionListener(new saveListener());
		delete.addActionListener(new deleteListener());
		bNew.addActionListener(new newListener());
		bPrint.addActionListener(new displayListener());
		preview.addActionListener(new displayListener());
		borrow.addActionListener(new borrowListener());
		bReturn.addActionListener(new returnbuttonListener());
		newsave.addActionListener(new newsaveListener());

		// 클라이언드 프레임 창 조정
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.setSize(750,500);
		frame.setLocation(500,0);
		frame.setVisible(true);
	}
	// DB를 연결하는 메소드
	private void dbConnectionInit() {
		try {
			Class.forName("com.mysql.jdbc.Driver");     // JDBC드라이버를 JVM영역으로 가져오기
			conn =  DriverManager.getConnection("jdbc:mysql://localhost/project", "root", "mite");// DB 연결하기
			prepareList();
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println("JDBC 드라이버 클래스를 찾을 수 없습니다 : " + cnfe.getMessage());
		}
		catch (Exception ex) {
			System.out.println("DB 연결 에러 : " + ex.getMessage());
		}
	}
	// DB에 있는 전체 레코드를 불러와서 리스트에 뿌려주는 메소
	public void prepareList() {
		try {
			Statement stmt = conn.createStatement();   // SQL 문을 작성을 위한  Statement 객체 생성
			// 현재 DB에 있는 내용 추출해서 강아지 목록을 names 리스트에 출력하기
			ResultSet rs = stmt.executeQuery("SELECT * FROM member");
			Vector<String> list = new Vector<String>();
			while (rs.next()) {
				list.add(rs.getString("name"));  
			}
			
			stmt.close();
			stmt = conn.createStatement();
			rs=stmt.executeQuery("SELECT * FROM books");

			while(rs.next()) {

				books.add(rs.getString("title"));

			}
			
			stmt.close();          // statement는 사용후 닫는 습관
			Collections.sort(list);        // 우선 정렬하자
			names.setListData(list);       // names의 각종 속성은 그대로 두고 내용물만 바꾼다
			if (!list.isEmpty())        // 리스트가 바뀌고 나면 항상 첫번째 아이텀을 가리키게 
				names.setSelectedIndex(0);

		} catch (SQLException sqlex) {
			System.out.println("SQL 에러 : " + sqlex.getMessage());
			sqlex.printStackTrace();
		}
	}
	public class nameListListener implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting() && !names.isSelectionEmpty()) {  // 현재 선택이 다 끝난 경우에 처리
				try {
					Statement stmt = conn.createStatement();    // SQL 문장 만들기 위한 Statement 객체

					ResultSet rs = stmt.executeQuery("SELECT * FROM member WHERE name = '" +
							(String)names.getSelectedValue() + "'");

					rs.next();            // 여러개가 리턴되어도 첫번째 것으로 사용 

					name.setText(rs.getString("name"));   // DB에서 리턴 된 값을 가지고 택스트 박스 채움
					birthday.setText(rs.getDate("birthday").toString()); 
					phone.setText(rs.getString("phone")); 
					borrowdate.setText(rs.getDate("borrowdate").toString());
					returndate.setText(rs.getDate("returndate").toString());

					if (rs.getString("is_return").equals("Y"))   
						borrowbook.setSelected(true);
					else
						noreturn.setSelected(true);

					rs=stmt.executeQuery("SELECT * FROM books WHERE book_id IN (SELECT book_id FROM member_book WHERE member_id=(SELECT member_id FROM member WHERE name = '"+
							(String)names.getSelectedValue()+"'))");
					mybook.removeAll();
					while(rs.next()) {
						mybook.add(rs.getString("title"));
					}

					stmt.close();
				} catch (SQLException sqlex) {
					System.out.println("SQL 에러 : " + sqlex.getMessage());
					sqlex.printStackTrace();
				} catch (Exception ex) {
					System.out.println("DB Handling 에러(리스트 리스너) : " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}
	public class mySearchListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			int index = names.getNextMatch(search.getText().trim(), 0, Position.Bias.Forward);
			if (index != -1) {
				names.setSelectedIndex(index);
			}
			search.setText("");
		}
	}
	public class deleteListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			try {
				Statement stmt = conn.createStatement();    // SQL 문을 작성을 위한  Statement 객체 생성
				stmt.executeUpdate("DELETE FROM member_book WHERE member_id=(SELECT member_id FROM member WHERE name='"+(String)names.getSelectedValue()+"')");
				stmt.close();
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM member WHERE name = '" + (String)names.getSelectedValue() + "'");   
				stmt.close();
				prepareList();           // 리스트 박스 새 리스트로 다시 채움 
			} catch (SQLException sqlex) {
				System.out.println("SQL 에러 : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling 에러(DELETE 리스너) : " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
	public class saveListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			try {
				Statement stmt = conn.createStatement();
				String is_return;
				if (borrowbook.isSelected())
					is_return="Y";
				else
					is_return = "N";
				
				stmt.executeUpdate("UPDATE member SET name='"+name.getText().trim() + "',"
						+ "birthday='" + birthday.getText().trim() + "',"
						+ "phone= '" + phone.getText().trim() + "',"
						+ "is_return='" + is_return + "',"
						+ "borrowdate= '" +borrowdate.getText().trim() + "',"
						+ "returndate= '" +returndate.getText().trim() 
						+ "' WHERE name= '" +(String)names.getSelectedValue() + "'");   
				

				stmt.close();
				prepareList();           // 다시 뿌려 

				int index = names.getNextMatch(name.getText().trim(), 0, Position.Bias.Forward);
				names.setSelectedIndex(index);
			} catch (SQLException sqlex) {
				System.out.println("SQL 에러 : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling 에러(SAVE 리스너) : " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
	public class newListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			name.setText("");
			birthday.setText("");
			phone.setText("");
			mybook.removeAll();
			borrowdate.setText("");
			returndate.setText("");
			borrowbook.setSelected(true);
			noreturn.setSelected(false);
			names.clearSelection();
		}
	}
	public class newsaveListener implements ActionListener {
		public void actionPerformed (ActionEvent e) { 
			try {
				Statement stmt = conn.createStatement();    // SQL 문을 작성을 위한  Statement 객체 생성


				String is_return, borrowd,returnd;
				is_return="Y";
				borrowd="2000-01-01";
				returnd="2000-01-01";

				stmt.executeUpdate("INSERT INTO member (name, birthday, phone,is_return,borrowdate,returndate) VALUES ('" +   // 새 레코드로 변경
						name.getText().trim() + "', '" +
						birthday.getText().trim() + "', '" +
						phone.getText().trim() + "', '" +
						is_return + "', '" +
						borrowd + "', '" +
						returnd + "')");

				stmt.close();
				prepareList();           // 다시 뿌려 
				int index = names.getNextMatch(name.getText().trim(), 0, Position.Bias.Forward);
				names.setSelectedIndex(index);
			} catch (SQLException sqlex) {
				System.out.println("SQL 에러 : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling 에러(SAVE 리스너) : " + ex.getMessage());
				ex.printStackTrace();
			}

		}
	}
	public class borrowListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			try {
				Statement stmt = conn.createStatement();    // SQL 문장 만들기 위한 Statement 객체
				ResultSet rs = stmt.executeQuery("SELECT * FROM member NATURAL JOIN member_book NATURAL JOIN books WHERE name = '" +
						(String)names.getSelectedValue() + "'");

				rs.next();            // 여러개가 리턴되어도 첫번째 것으로 사용 
				if(noreturn.isSelected()) {
					JOptionPane.showMessageDialog(null, "대출이 불가능합니다","ERROR", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					mybook.add(books.getSelectedItem());
					stmt.close();
					stmt = conn.createStatement();
					stmt.executeUpdate("INSERT INTO member_book  VALUES ((SELECT member_id FROM member WHERE name='" + (String)names.getSelectedValue()+"'),"
							+ "(SELECT book_id FROM books WHERE title='"+books.getSelectedItem()+"'))");

				}
				stmt.close();

			} catch (SQLException sqlex) {
				System.out.println("SQL 에러 : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling 에러(리스트 리스너) : " + ex.getMessage());
				ex.printStackTrace();
			}

		}

	}
	public class returnbuttonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			try {
				Statement stmt = conn.createStatement();    // SQL 문장 만들기 위한 Statement 객체
				ResultSet rs = stmt.executeQuery("SELECT * FROM member NATURAL JOIN member_book NATURAL JOIN books WHERE name = '" +
						(String)names.getSelectedValue() + "'");

				rs.next();            // 여러개가 리턴되어도 첫번째 것으로 사용 
				mybook.remove(mybook.getSelectedItem());
				stmt.executeUpdate("DELETE FROM member_book WHERE member_id=(SELECT member_id FROM member WHERE name='"+(String)names.getSelectedValue()+"') "
						+ "AND book_id= (SELECT book_id FROM books WHERE title='"+mybook.getSelectedItem()+"')");


				stmt.close();
			} catch (SQLException sqlex) {
				System.out.println("SQL 에러 : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling 에러(리스트 리스너) : " + ex.getMessage());
				ex.printStackTrace();
			}

		}
	}
	// 출력을 위한 액션이 발생하면 처리하는 리스너 (print와 preview)
	public class displayListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			// DB에서 가져오는 데이터를 rowObjects의 형태로 저장하고 이들의 리스트를 Printer 또는 Preview로 보내 줌
			ArrayList<RowObjects> rowList = new ArrayList<RowObjects>(); // 행들의 리스트
			RowObjects line;            // 하나의 행
			PrintObject word;            // 하나의 단어
			try {
				Statement stmt = conn.createStatement();     // SQL 문장 만들기 위한 Statement 객체
				ResultSet rs = stmt.executeQuery("SELECT * FROM member NATURAL JOIN member_book NATURAL JOIN books NATURAL JOIN writers");
				while(rs.next()) {
					line = new RowObjects();        // 5개의 단어가 1줄
					line.add(new PrintObject(rs.getString("name"), 15));
					line.add(new PrintObject(rs.getString("phone"), 20));
					line.add(new PrintObject(rs.getString("title"), 30));
					line.add(new PrintObject(rs.getString("writer"), 20));
					line.add(new PrintObject(rs.getString("is_return"), 5));
					rowList.add(line);          // 출력해야 될 전체 리스트를 만듬         
				}
				stmt.close();

				// 각 페이지의 칼럼 헤더를 위해 한 줄 만들음
				line = new RowObjects();         // 5개의 단어가 1줄
				line.add(new PrintObject("이름", 18));
				line.add(new PrintObject("전화번호", 18));
				line.add(new PrintObject("대출 도서", 20));
				line.add(new PrintObject("작가", 13));
				line.add(new PrintObject("연체", 6));
				if (e.getSource() == bPrint) {
					Printer prt = new Printer(new PrintObject("감귤 도서관 도서 대출 리스트", 20), line, rowList, true);
					prt.print();
				}
				else {
					Preview prv = new Preview(new PrintObject("감귤 도서관 도서 대출 리스트", 20), line, rowList, true);
					prv.preview();
				}

			} catch (SQLException sqlex) {
				System.out.println("SQL 에러 : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling 에러(리스트 리스너) : " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}