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
	// DB ���� ������
	Connection conn;  // DB ���� Connection ��ü��������
	// �ֻ��� ������
	JFrame frame;
	String frameTitle = "���ֵ�����";
	// �ؽ�Ʈ �ڽ���
	JTextField name;
	JTextField birthday;
	JTextField phone;
	JTextField book;
	JTextField borrowdate;
	JTextField returndate;
	// ������ ���� �ڽ�
	JTextField search;  // ������ ����  �ʵ�
	// ���� ��ư��
	JRadioButton borrowbook=new JRadioButton("���� ����");
	JRadioButton noreturn=new JRadioButton("�� ü");
	// ��ư��
	JButton bSearch;
	JButton save;
	JButton delete;
	JButton bNew;
	JButton bPrint;
	JButton preview;
	JButton borrow;
	JButton bReturn;
	JButton newsave;
	// ����Ʈ
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
		leftTopPanel.add("br center", new JLabel("�� �� �� �� ��  ȸ �� �� �� Ʈ"));
		leftTopPanel.add("p center", scroller);
		leftTopPanel.add("p br center",new JLabel("<<��  ��  ��  ��>>"));
		leftTopPanel.add("p center",books);
		// �Է� â��� �� (������ ��� �г�)
		JPanel righttopPanel = new JPanel(new RiverLayout());
		name=new JTextField(25);
		birthday=new JTextField(25);
		phone=new JTextField(25);
		book=new JTextField(25);
		borrowdate=new JTextField(6);
		returndate=new JTextField(6);
		borrow=new JButton("����");
		bReturn=new JButton("�ݳ�");
		newsave=new JButton("����");
		ButtonGroup is_return=new ButtonGroup();
		is_return.add(borrowbook);
		is_return.add(noreturn);
		//ǥ���� ���� �󺧵�
		
		righttopPanel.add("br center",new JLabel("ȸ �� �� ��"));
		righttopPanel.add("p left",new JLabel("��  ��"));
		righttopPanel.add("tap",name);
		righttopPanel.add("br",new JLabel("�� �� �� ��"));
		righttopPanel.add("tap",birthday);
		righttopPanel.add("br",new JLabel("�� ȭ �� ȣ"));
		righttopPanel.add("tap",phone);
		righttopPanel.add("br",new JLabel("�� �� �� ��"));
		righttopPanel.add("tap",mybook);
		
		righttopPanel.add("br",new JLabel("�� �� ��"));
		righttopPanel.add(borrowdate);
		righttopPanel.add(new JLabel("�� �� ��"));
		righttopPanel.add(returndate);
		
		righttopPanel.add("tap",borrowbook);
		righttopPanel.add("tab", noreturn);
		// ���� �ϴ� �г�
		JPanel leftBottomPanel = new JPanel(new RiverLayout());
		
		JPanel tmpPanel = new JPanel(new RiverLayout());
		JPanel tmpPanel1 = new JPanel(new RiverLayout());
		JPanel tmpPanel2 = new JPanel(new RiverLayout());
		search = new JTextField(20);
		bSearch = new JButton("�˻�");
		bPrint = new JButton("���");
		preview = new JButton("�̸�����");
		tmpPanel1.add("tap",search);
		tmpPanel2.add(bSearch);
		tmpPanel2.add(bPrint);
		tmpPanel2.add(preview);
		tmpPanel.add("center",tmpPanel1);
		tmpPanel.add("br cneter",tmpPanel2);
		leftBottomPanel.add("center", tmpPanel);
		// ������ �ϴ� �г�
		JPanel rightBottomPanel = new JPanel(new RiverLayout());
		
		tmpPanel = new JPanel(new RiverLayout());
		save = new JButton("����");
		delete = new JButton("����");
		bNew = new JButton("�ű�");
		tmpPanel.add(borrow);
		tmpPanel.add("tab",bReturn);
		tmpPanel.add("tab",save);
		tmpPanel.add("tap", delete);
		tmpPanel.add("tab", bNew);
		tmpPanel.add("tab",newsave);
		rightBottomPanel.add("center", tmpPanel);
		rightBottomPanel.add("br", Box.createRigidArea(new Dimension(0,20)));
		// GUI ��ġ
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

		// Ŭ���̾�� ������ â ����
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.setSize(750,500);
		frame.setLocation(500,0);
		frame.setVisible(true);
	}
	// DB�� �����ϴ� �޼ҵ�
	private void dbConnectionInit() {
		try {
			Class.forName("com.mysql.jdbc.Driver");     // JDBC����̹��� JVM�������� ��������
			conn =  DriverManager.getConnection("jdbc:mysql://localhost/project", "root", "mite");// DB �����ϱ�
			prepareList();
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println("JDBC ����̹� Ŭ������ ã�� �� �����ϴ� : " + cnfe.getMessage());
		}
		catch (Exception ex) {
			System.out.println("DB ���� ���� : " + ex.getMessage());
		}
	}
	// DB�� �ִ� ��ü ���ڵ带 �ҷ��ͼ� ����Ʈ�� �ѷ��ִ� �޼�
	public void prepareList() {
		try {
			Statement stmt = conn.createStatement();   // SQL ���� �ۼ��� ����  Statement ��ü ����
			// ���� DB�� �ִ� ���� �����ؼ� ������ ����� names ����Ʈ�� ����ϱ�
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
			
			stmt.close();          // statement�� ����� �ݴ� ����
			Collections.sort(list);        // �켱 ��������
			names.setListData(list);       // names�� ���� �Ӽ��� �״�� �ΰ� ���빰�� �ٲ۴�
			if (!list.isEmpty())        // ����Ʈ�� �ٲ�� ���� �׻� ù��° �������� ����Ű�� 
				names.setSelectedIndex(0);

		} catch (SQLException sqlex) {
			System.out.println("SQL ���� : " + sqlex.getMessage());
			sqlex.printStackTrace();
		}
	}
	public class nameListListener implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting() && !names.isSelectionEmpty()) {  // ���� ������ �� ���� ��쿡 ó��
				try {
					Statement stmt = conn.createStatement();    // SQL ���� ����� ���� Statement ��ü

					ResultSet rs = stmt.executeQuery("SELECT * FROM member WHERE name = '" +
							(String)names.getSelectedValue() + "'");

					rs.next();            // �������� ���ϵǾ ù��° ������ ��� 

					name.setText(rs.getString("name"));   // DB���� ���� �� ���� ������ �ý�Ʈ �ڽ� ä��
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
					System.out.println("SQL ���� : " + sqlex.getMessage());
					sqlex.printStackTrace();
				} catch (Exception ex) {
					System.out.println("DB Handling ����(����Ʈ ������) : " + ex.getMessage());
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
				Statement stmt = conn.createStatement();    // SQL ���� �ۼ��� ����  Statement ��ü ����
				stmt.executeUpdate("DELETE FROM member_book WHERE member_id=(SELECT member_id FROM member WHERE name='"+(String)names.getSelectedValue()+"')");
				stmt.close();
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM member WHERE name = '" + (String)names.getSelectedValue() + "'");   
				stmt.close();
				prepareList();           // ����Ʈ �ڽ� �� ����Ʈ�� �ٽ� ä�� 
			} catch (SQLException sqlex) {
				System.out.println("SQL ���� : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling ����(DELETE ������) : " + ex.getMessage());
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
				prepareList();           // �ٽ� �ѷ� 

				int index = names.getNextMatch(name.getText().trim(), 0, Position.Bias.Forward);
				names.setSelectedIndex(index);
			} catch (SQLException sqlex) {
				System.out.println("SQL ���� : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling ����(SAVE ������) : " + ex.getMessage());
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
				Statement stmt = conn.createStatement();    // SQL ���� �ۼ��� ����  Statement ��ü ����


				String is_return, borrowd,returnd;
				is_return="Y";
				borrowd="2000-01-01";
				returnd="2000-01-01";

				stmt.executeUpdate("INSERT INTO member (name, birthday, phone,is_return,borrowdate,returndate) VALUES ('" +   // �� ���ڵ�� ����
						name.getText().trim() + "', '" +
						birthday.getText().trim() + "', '" +
						phone.getText().trim() + "', '" +
						is_return + "', '" +
						borrowd + "', '" +
						returnd + "')");

				stmt.close();
				prepareList();           // �ٽ� �ѷ� 
				int index = names.getNextMatch(name.getText().trim(), 0, Position.Bias.Forward);
				names.setSelectedIndex(index);
			} catch (SQLException sqlex) {
				System.out.println("SQL ���� : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling ����(SAVE ������) : " + ex.getMessage());
				ex.printStackTrace();
			}

		}
	}
	public class borrowListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			try {
				Statement stmt = conn.createStatement();    // SQL ���� ����� ���� Statement ��ü
				ResultSet rs = stmt.executeQuery("SELECT * FROM member NATURAL JOIN member_book NATURAL JOIN books WHERE name = '" +
						(String)names.getSelectedValue() + "'");

				rs.next();            // �������� ���ϵǾ ù��° ������ ��� 
				if(noreturn.isSelected()) {
					JOptionPane.showMessageDialog(null, "������ �Ұ����մϴ�","ERROR", JOptionPane.PLAIN_MESSAGE);
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
				System.out.println("SQL ���� : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling ����(����Ʈ ������) : " + ex.getMessage());
				ex.printStackTrace();
			}

		}

	}
	public class returnbuttonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			try {
				Statement stmt = conn.createStatement();    // SQL ���� ����� ���� Statement ��ü
				ResultSet rs = stmt.executeQuery("SELECT * FROM member NATURAL JOIN member_book NATURAL JOIN books WHERE name = '" +
						(String)names.getSelectedValue() + "'");

				rs.next();            // �������� ���ϵǾ ù��° ������ ��� 
				mybook.remove(mybook.getSelectedItem());
				stmt.executeUpdate("DELETE FROM member_book WHERE member_id=(SELECT member_id FROM member WHERE name='"+(String)names.getSelectedValue()+"') "
						+ "AND book_id= (SELECT book_id FROM books WHERE title='"+mybook.getSelectedItem()+"')");


				stmt.close();
			} catch (SQLException sqlex) {
				System.out.println("SQL ���� : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling ����(����Ʈ ������) : " + ex.getMessage());
				ex.printStackTrace();
			}

		}
	}
	// ����� ���� �׼��� �߻��ϸ� ó���ϴ� ������ (print�� preview)
	public class displayListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			// DB���� �������� �����͸� rowObjects�� ���·� �����ϰ� �̵��� ����Ʈ�� Printer �Ǵ� Preview�� ���� ��
			ArrayList<RowObjects> rowList = new ArrayList<RowObjects>(); // ����� ����Ʈ
			RowObjects line;            // �ϳ��� ��
			PrintObject word;            // �ϳ��� �ܾ�
			try {
				Statement stmt = conn.createStatement();     // SQL ���� ����� ���� Statement ��ü
				ResultSet rs = stmt.executeQuery("SELECT * FROM member NATURAL JOIN member_book NATURAL JOIN books NATURAL JOIN writers");
				while(rs.next()) {
					line = new RowObjects();        // 5���� �ܾ 1��
					line.add(new PrintObject(rs.getString("name"), 15));
					line.add(new PrintObject(rs.getString("phone"), 20));
					line.add(new PrintObject(rs.getString("title"), 30));
					line.add(new PrintObject(rs.getString("writer"), 20));
					line.add(new PrintObject(rs.getString("is_return"), 5));
					rowList.add(line);          // ����ؾ� �� ��ü ����Ʈ�� ����         
				}
				stmt.close();

				// �� �������� Į�� ����� ���� �� �� ������
				line = new RowObjects();         // 5���� �ܾ 1��
				line.add(new PrintObject("�̸�", 18));
				line.add(new PrintObject("��ȭ��ȣ", 18));
				line.add(new PrintObject("���� ����", 20));
				line.add(new PrintObject("�۰�", 13));
				line.add(new PrintObject("��ü", 6));
				if (e.getSource() == bPrint) {
					Printer prt = new Printer(new PrintObject("���� ������ ���� ���� ����Ʈ", 20), line, rowList, true);
					prt.print();
				}
				else {
					Preview prv = new Preview(new PrintObject("���� ������ ���� ���� ����Ʈ", 20), line, rowList, true);
					prv.preview();
				}

			} catch (SQLException sqlex) {
				System.out.println("SQL ���� : " + sqlex.getMessage());
				sqlex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("DB Handling ����(����Ʈ ������) : " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}