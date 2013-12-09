package activityLog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ActivityPane extends JPanel implements ActionListener {
	
	List<activity> activities = Collections.synchronizedList(new ArrayList<activity>());
	List<activity> newActivities = Collections.synchronizedList(new ArrayList<activity>());
	private JScrollPane scrollPane;
	private JTextPane textPane;
	Style commentStyle;
	Style nameStyle;
	StyledDocument styledDoc;
	JMenuBar menuBar = new JMenuBar();
	JMenu roleOptions = new JMenu("Filer by Role");
	JCheckBoxMenuItem rest1customer = new JCheckBoxMenuItem("Restaurant1 Customer Role");
	JCheckBoxMenuItem rest1waiter = new JCheckBoxMenuItem("Restaurant1 Waiter Role");
	JCheckBoxMenuItem rest1host = new JCheckBoxMenuItem("Restaurant1 Host Role");
	JCheckBoxMenuItem rest1cook = new JCheckBoxMenuItem("Restaurant1 Cook Role");
	JCheckBoxMenuItem rest1cashier = new JCheckBoxMenuItem("Restaurant1 Cashier Role");
	JCheckBoxMenuItem rest2customer = new JCheckBoxMenuItem("Restaurant2 Customer Role");
	JCheckBoxMenuItem rest2waiter = new JCheckBoxMenuItem("Restaurant2 Waiter Role");
	JCheckBoxMenuItem rest2host = new JCheckBoxMenuItem("Restaurant2 Host Role");
	JCheckBoxMenuItem rest2cook = new JCheckBoxMenuItem("Restaurant2 Cook Role");
	JCheckBoxMenuItem rest2cashier = new JCheckBoxMenuItem("Restaurant2 Cashier Role");
	JCheckBoxMenuItem rest3customer = new JCheckBoxMenuItem("Restaurant3 Customer Role");
	JCheckBoxMenuItem rest3waiter = new JCheckBoxMenuItem("Restaurant3 Waiter Role");
	JCheckBoxMenuItem rest3host = new JCheckBoxMenuItem("Restaurant3 Host Role");
	JCheckBoxMenuItem rest3cook = new JCheckBoxMenuItem("Restaurant3 Cook Role");
	JCheckBoxMenuItem rest3cashier = new JCheckBoxMenuItem("Restaurant3 Cashier Role");
	JCheckBoxMenuItem rest4customer = new JCheckBoxMenuItem("Restaurant4 Customer Role");
	JCheckBoxMenuItem rest4waiter = new JCheckBoxMenuItem("Restaurant4 Waiter Role");
	JCheckBoxMenuItem rest4host = new JCheckBoxMenuItem("Restaurant4 Host Role");
	JCheckBoxMenuItem rest4cook = new JCheckBoxMenuItem("Restaurant4 Cook Role");
	JCheckBoxMenuItem rest4cashier = new JCheckBoxMenuItem("Restaurant4 Cashier Role");
	JCheckBoxMenuItem rest5customer = new JCheckBoxMenuItem("Restaurant5 Customer Role");
	JCheckBoxMenuItem rest5waiter = new JCheckBoxMenuItem("Restaurant5 Waiter Role");
	JCheckBoxMenuItem rest5host = new JCheckBoxMenuItem("Restaurant5 Host Role");
	JCheckBoxMenuItem rest5cook = new JCheckBoxMenuItem("Restaurant5 Cook Role");
	JCheckBoxMenuItem rest5cashier = new JCheckBoxMenuItem("Restaurant5 Cashier Role");
	
	List<JCheckBoxMenuItem> roleFilters = new ArrayList<JCheckBoxMenuItem>();

	Dimension scrollPaneDim = new Dimension(350, 580);
	Dimension menuDim = new Dimension(350, 20);
	
	public ActivityPane(){
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(scrollPaneDim);
		textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		styledDoc = textPane.getStyledDocument();
		commentStyle = styledDoc.addStyle("CommentStyle", null);
		nameStyle = styledDoc.addStyle("NameStyle", null);
		StyleConstants.setForeground(commentStyle, Color.black);
		StyleConstants.setForeground(nameStyle, Color.black);
		StyleConstants.setBold(nameStyle, true);
		ActivityLog.setPane(this);
		
		//adding all the check boxes to a list
		roleFilters.add(rest1customer);
		roleFilters.add(rest1waiter);
		roleFilters.add(rest1host);
		roleFilters.add(rest1cashier);
		roleFilters.add(rest1cook);
		roleFilters.add(rest2customer);
		roleFilters.add(rest2waiter);
		roleFilters.add(rest2host);
		roleFilters.add(rest2cashier);
		roleFilters.add(rest2cook);
		roleFilters.add(rest3customer);
		roleFilters.add(rest3waiter);
		roleFilters.add(rest3host);
		roleFilters.add(rest3cashier);
		roleFilters.add(rest3cook);
		roleFilters.add(rest4customer);
		roleFilters.add(rest4waiter);
		roleFilters.add(rest4host);
		roleFilters.add(rest4cashier);
		roleFilters.add(rest4cook);
		roleFilters.add(rest5customer);
		roleFilters.add(rest5waiter);
		roleFilters.add(rest5host);
		roleFilters.add(rest5cashier);
		roleFilters.add(rest5cook);
		
		//options window setup
		setupOptionWindow();
		menuBar.add(roleOptions);
		menuBar.setPreferredSize(menuDim);
		
		add(menuBar);
		add(scrollPane);
	}
	
	private void setupOptionWindow(){
		roleOptions.setAlignmentX(LEFT_ALIGNMENT);
		for(JCheckBoxMenuItem j : roleFilters){
			roleOptions.add(j);
			j.addActionListener(this);
		}
	}
	
	public void addActivity(activity a){
		newActivities.add(a);
		updatePane();
	}
	
	private void updatePane(){
		synchronized(newActivities){
			for(activity a : newActivities){
				try{
					int endPosition = textPane.getDocument().getEndPosition().getOffset();
					textPane.getStyledDocument().insertString(endPosition, a.getName() + ": ", nameStyle);
					endPosition = textPane.getDocument().getEndPosition().getOffset();
					textPane.getStyledDocument().insertString(endPosition, a.getMessage() + "\n", commentStyle);
				}
				catch (BadLocationException e){
					e.printStackTrace();
				}
			}
		}
		textPane.setCaretPosition(textPane.getDocument().getLength());
		newActivities.clear();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(JCheckBoxMenuItem j : roleFilters){
			if(e.getSource() == j){
				System.out.println(j.getText());
			}
		}
	}
	
	
}
