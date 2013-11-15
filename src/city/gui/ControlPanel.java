package city.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ControlPanel extends JPanel implements ActionListener{
	
	public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    
    private int WINDOWX = 370;
    private int WINDOWY = 750;
    
    private Dimension scrollDim = new Dimension(WINDOWX, WINDOWY/4);

    private JTextField nameField;
    private JPanel enterNames = new JPanel();
    public JCheckBox isHungry;
    public JCheckBox takeBreak;
    
    CityGui cityGui;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ControlPanel() {

        view.setLayout(new FlowLayout());
        setLayout(new BoxLayout((Container) this, BoxLayout.PAGE_AXIS));
        
        pane.setMinimumSize(scrollDim);
        pane.setMaximumSize(scrollDim);
        pane.setPreferredSize(scrollDim);

        setBorder(BorderFactory.createLineBorder(Color.black, 5));
        
        addPersonSection();
                
    }
    
    public void setCityGui(CityGui c){
    	cityGui = c;
    }
    
    private void addPersonSection(){
    	add(new JLabel("<html><br><u>Add People</u><br></html>"));
        
        FlowLayout flow = new FlowLayout();
        
        enterNames.add(new JLabel("Name:"), flow);
        
        nameField = new JTextField();
        nameField.setColumns(16);
        enterNames.add(nameField, flow);
        
        isHungry = new JCheckBox("Hungry?");
        isHungry.setEnabled(false);
        isHungry.addActionListener(this);
        //enterNames.add(isHungry);
        
        nameField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(nameField.getText().length() > 0){
					isHungry.setEnabled(true);
				}
				else{
					isHungry.setEnabled(false);
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if(nameField.getText().length() > 0){
					isHungry.setEnabled(true);
				}
				else{
					isHungry.setEnabled(false);
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if(nameField.getText().length() > 0){
					isHungry.setEnabled(true);
				}
				else{
					isHungry.setEnabled(false);
				}
			}
        	
        });

        addPersonB.addActionListener(this);
       
        enterNames.add(addPersonB, flow);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        this.add(enterNames);
        pane.setViewportView(view);
        add(pane);
    	
    }
    

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
        	if(!nameField.getText().equals("")){
                addPerson(nameField.getText());
            	nameField.setText("");
            	isHungry.setSelected(false);
        	}
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);
            
            cityGui.addPerson(name);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension((paneSize.width - 20),
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            isHungry.setEnabled(false);
            validate();
        }
    }
	
}