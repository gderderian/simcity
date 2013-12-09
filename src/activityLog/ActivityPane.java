package activityLog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
	Style personNameStyle;
	Style personCommentStyle;
	StyledDocument styledDoc;
	JPanel filterPanel = new JPanel();
	JButton filterButton = new JButton("Filter by Role, Agent, or Building");
	JButton clearRolesButton = new JButton("Clear all selections");
	JButton selectAllRolesButton = new JButton("Select all roles");
	JFrame optionWindow = new JFrame();
	JPanel optionPanel = new JPanel();
	JCheckBox person = new JCheckBox("Person");
	JCheckBox rest1customer = new JCheckBox("Restaurant1Customer");
	JCheckBox rest1waiter = new JCheckBox("Restaurant1Waiter");
	JCheckBox rest1host = new JCheckBox("Restaurant1Host");
	JCheckBox rest1cook = new JCheckBox("Restaurant1Cook");
	JCheckBox rest1cashier = new JCheckBox("Restaurant1Cashier");
	JCheckBox rest2customer = new JCheckBox("Restaurant2Customer");
	JCheckBox rest2waiter = new JCheckBox("Restaurant2Waiter");
	JCheckBox rest2host = new JCheckBox("Restaurant2Host");
	JCheckBox rest2cook = new JCheckBox("Restaurant2Cook");
	JCheckBox rest2cashier = new JCheckBox("Restaurant2Cashier");
	JCheckBox rest3customer = new JCheckBox("Restaurant3Customer");
	JCheckBox rest3waiter = new JCheckBox("Restaurant3Waiter");
	JCheckBox rest3host = new JCheckBox("Restaurant3Host");
	JCheckBox rest3cook = new JCheckBox("Restaurant3Cook");
	JCheckBox rest3cashier = new JCheckBox("Restaurant3Cashier");
	JCheckBox rest4customer = new JCheckBox("Restaurant4Customer");
	JCheckBox rest4waiter = new JCheckBox("Restaurant4Waiter");
	JCheckBox rest4host = new JCheckBox("Restaurant4Host");
	JCheckBox rest4cook = new JCheckBox("Restaurant4Cook");
	JCheckBox rest4cashier = new JCheckBox("Restaurant4Cashier");
	JCheckBox rest5customer = new JCheckBox("Restaurant5Customer");
	JCheckBox rest5waiter = new JCheckBox("Restaurant5Waiter");
	JCheckBox rest5host = new JCheckBox("Restaurant5Host");
	JCheckBox rest5cook = new JCheckBox("Restaurant5Cook");
	JCheckBox rest5cashier = new JCheckBox("Restaurant5Cashier");
	JCheckBox bankManager = new JCheckBox("BankManager");
	JCheckBox bankTeller = new JCheckBox("BankTeller");
	JCheckBox bankCustomer = new JCheckBox("BankCustomer");
	JCheckBox marketCustomer = new JCheckBox("MarketCustomer");
	JCheckBox landlord = new JCheckBox("Landlord");
	JCheckBox marketManager = new JCheckBox("MarketManager");
	JCheckBox marketWorker = new JCheckBox("MarketWorker");
	JCheckBox bus = new JCheckBox("Bus");
	JCheckBox busStop = new JCheckBox("BusStop");
	JCheckBox car = new JCheckBox("Car");
	JCheckBox truck = new JCheckBox("Truck");
	
	List<JCheckBox> roleFilters = new ArrayList<JCheckBox>();

	Dimension scrollPaneDim = new Dimension(350, 580);
	Dimension menuDim = new Dimension(350, 20);
	Dimension optionWindowDim = new Dimension(550, 400);
	
    private String[] buildings = {"[Filter roles by building]", "Restaurant1", "Restaurant2", "Restaurant3",
    		"Restaurant4", "Restaurant5", "Bank", "Market", "Transportation", "House"
    };
    private JComboBox buildingSelect = new JComboBox(buildings);
	
	List<ActivityTag> visibleTags = new ArrayList<ActivityTag>();
	
	public ActivityPane(){
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(scrollPaneDim);
		textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		styledDoc = textPane.getStyledDocument();
		commentStyle = styledDoc.addStyle("CommentStyle", null);
		StyleConstants.setForeground(commentStyle, Color.blue);
		nameStyle = styledDoc.addStyle("NameStyle", null);
		StyleConstants.setForeground(nameStyle, Color.blue);
		StyleConstants.setBold(nameStyle, true);
		personNameStyle = styledDoc.addStyle("PersonNameStyle", null);
		StyleConstants.setForeground(personNameStyle, Color.black);
		StyleConstants.setBold(personNameStyle, true);
		personCommentStyle = styledDoc.addStyle("PersonCommentStyle", null);
		StyleConstants.setForeground(personCommentStyle, Color.black);
		
		ActivityLog.setPane(this);
		
		//adding all the check boxes to a list
		roleFilters.add(person);
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
		roleFilters.add(bankManager);
		roleFilters.add(bankTeller);
		roleFilters.add(bankCustomer);
		roleFilters.add(marketCustomer);
		roleFilters.add(landlord);
		roleFilters.add(marketManager);
		roleFilters.add(marketWorker);
		roleFilters.add(bus);
		roleFilters.add(busStop);
		roleFilters.add(car);
		roleFilters.add(truck);
		
		for(JCheckBox j : roleFilters){
			j.addActionListener(this);
		}
		
		buildingSelect.addActionListener(this);
		
		filterPanel.setPreferredSize(new Dimension(350, 25));
		filterButton.setPreferredSize(new Dimension(350, 20));
		filterButton.addActionListener(this);
		filterPanel.add(filterButton);
		add(filterPanel);
		
		for(ActivityTag t : ActivityTag.values()){
			visibleTags.add(t);
		}
		
		//options window setup
		setupOptionWindow();

		add(scrollPane);
	}
	
	private void setupOptionWindow(){
		optionPanel.add(clearRolesButton);
		optionPanel.add(selectAllRolesButton);
		optionPanel.add(buildingSelect);
		optionPanel.add(Box.createVerticalStrut(10));
		clearRolesButton.addActionListener(this);
		selectAllRolesButton.addActionListener(this);
		optionPanel.setPreferredSize(optionWindowDim);
		optionWindow.setPreferredSize(optionWindowDim);
		optionWindow.setMaximumSize(optionWindowDim);
		optionWindow.setMinimumSize(optionWindowDim);
		optionWindow.setLocation(300, 150);
		optionWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		int count = 0;
		for(JCheckBox j : roleFilters){
			j.setSelected(true);
			optionPanel.add(j);
			if(count % 3 == 0){
				j.setAlignmentX(LEFT_ALIGNMENT);
			}
			else if(count % 3 == 1){
				j.setAlignmentX(CENTER_ALIGNMENT);
			}
			else if(count % 3 == 2){
				j.setAlignmentX(RIGHT_ALIGNMENT);
			}
			count ++;
		}
		
		optionWindow.add(optionPanel);
	}
	
	public void addActivity(activity a){
		if(visibleTags.contains(a.type)){
			newActivities.add(a);
			updatePane();
		}
	}
	
	private void updatePane(){
		synchronized(newActivities){
			for(activity a : newActivities){
				try{
					if(a.person_notRole){
						int endPosition = textPane.getDocument().getEndPosition().getOffset();
						textPane.getStyledDocument().insertString(endPosition, a.getName() + ": ", personNameStyle);
						endPosition = textPane.getDocument().getEndPosition().getOffset();
						textPane.getStyledDocument().insertString(endPosition, a.getMessage() + "\n", personCommentStyle);
					}
					else{
						int endPosition = textPane.getDocument().getEndPosition().getOffset();
						textPane.getStyledDocument().insertString(endPosition, a.getName() + ": ", nameStyle);
						endPosition = textPane.getDocument().getEndPosition().getOffset();
						textPane.getStyledDocument().insertString(endPosition, a.getMessage() + "\n", commentStyle);
					}
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
		if(e.getSource() == clearRolesButton){
			for(JCheckBox j : roleFilters){
				j.setSelected(false);
				removeTagFromFilter(ActivityTag.valueOf(j.getText().toUpperCase()));
			}
		}
		else if(e.getSource() == selectAllRolesButton){
			for(JCheckBox j : roleFilters){
				j.setSelected(true);
				addTagToFilter(ActivityTag.valueOf(j.getText().toUpperCase()));
			}
		}
		else if(e.getSource() == filterButton){
			optionWindow.setVisible(true);
		}
		else if(e.getSource() == buildingSelect){
			visibleTags.clear();
			if(buildingSelect.getSelectedItem().equals("Transportation")){
				for(JCheckBox j : roleFilters){
					if(j.getText().contains("Bus") || j.getText().contains("Car") || j.getText().contains("Truck") || j.getText().contains("BusStop")){
						j.setSelected(true);
					}
					else{
						j.setSelected(false);
					}
				}
				for(ActivityTag t : ActivityTag.values()){
					if(t.toString().contains("CAR") || t.toString().contains("BUS") || t.toString().contains("BUSSTOP") || t.toString().contains("TRUCK")){
						addTagToFilter(t);
					}
				}
			}
			else if(buildingSelect.getSelectedItem().equals("House")){
				for(JCheckBox j : roleFilters){
					if(j.getText().contains("Landlord") || j.getText().contains("Person")){
						j.setSelected(true);
					}
					else{
						j.setSelected(false);
					}
				}
				for(ActivityTag t : ActivityTag.values()){
					if(t.toString().contains("LANDLORD") || t.toString().contains("PERSON")){
						addTagToFilter(t);
					}
				}
			}
			else{
				for(JCheckBox j : roleFilters){
					if(j.getText().contains((String)buildingSelect.getSelectedItem()) || j.getText().contains("Person")){
						j.setSelected(true);
					}
					else{
						j.setSelected(false);
					}
				}
				for(ActivityTag t : ActivityTag.values()){
					if(t.toString().contains(((String) buildingSelect.getSelectedItem()).toUpperCase()) || t.toString().contains("PERSON")){
						addTagToFilter(t);
					}
				}
			}
		}
		else{
			for(JCheckBox j : roleFilters){
				if(e.getSource() == j){
					if(j.isSelected()){
						addTagToFilter(ActivityTag.valueOf(j.getText().toUpperCase()));
					}
					else{
						removeTagFromFilter(ActivityTag.valueOf(j.getText().toUpperCase()));
					}
				}
			}
		}
	}
	
	private void addTagToFilter(ActivityTag t){
		if(!visibleTags.contains(t)){
			visibleTags.add(t);
			filterTracePanel();
		}
	}
	
	private void removeTagFromFilter(ActivityTag t){
		if(visibleTags.contains(t)){
			visibleTags.remove(t);
			filterTracePanel();
		}
	}
	
	private void filterTracePanel() {
		try {
			textPane.getStyledDocument().remove(0, textPane.getStyledDocument().getLength());  //Removes the whole document
			//textPane.getStyledDocument().remove()
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		newActivities.clear();
		
		List<activity> alerts = ActivityLog.getInstance().getLog();  //Get all the alerts from the log
		Collections.sort(alerts);                  //Sort them (they end up sorted by timestamp)
		for(activity a : alerts) {
			if(visibleTags.contains(a.type)) {
				newActivities.add(a);
			}
		}
		updatePane();  //update the panel to now reflect the correct alerts
	}
	
	
}
