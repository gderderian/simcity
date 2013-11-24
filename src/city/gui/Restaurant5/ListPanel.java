package city.gui.Restaurant5;


import javax.swing.*;

import city.gui.Bank.BankPanel;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    FlowLayout internallayout = new FlowLayout(); 
    
    private JButton addPersonB = new JButton("Add");
    
    private JTextField textfield = new JTextField(10);
    private JCheckBox hungry = new JCheckBox("Hungry?");
    private JCheckBox goonbreak = new JCheckBox("On Break?");
    private JPanel internalpanel = new JPanel(new FlowLayout());
    
    private JButton submit = new JButton("submit");
    
    private BankPanel restPanel;
    private String type;
    
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(BankPanel rp, String type) {
        restPanel = rp;
        this.type = type;
        
        goonbreak.setVisible(false);
        internalpanel.setPreferredSize(new Dimension(300,100));
        internalpanel.add(textfield);
        if(type == "Customers")
        internalpanel.add(hungry);
        else if(type == "Waiters")
        internalpanel.add(goonbreak);
        internalpanel.add(addPersonB);
        hungry.setEnabled(false);
        
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        addPersonB.addActionListener(this);
        
        if(type == "Customers")
        hungry.addActionListener(this);
        
        if(type == "Waiters")
        goonbreak.addActionListener(this);
        
        textfield.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(textfield.getText().length() > 0) {
					hungry.setEnabled(true);
					goonbreak.setEnabled(true);
				}
				else {
				   hungry.setEnabled(false);
				   goonbreak.setEnabled(false);				   
				}
			}
        	
        });
        add(internalpanel);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setPreferredSize(new Dimension(300,250));
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
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
            String getname = textfield.getText();
            //System.out.print(getname);
            addPerson(getname, hungry.isSelected());
            //addPerson(getname, goonbreak.isSelected());    
        }
        
        else {
        	// Isn't the second for loop more beautiful?
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
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
    public void addPerson(String name, boolean ishungry) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name,ishungry);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    

    
    
    
    
}
