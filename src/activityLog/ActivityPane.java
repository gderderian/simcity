package activityLog;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ActivityPane extends JScrollPane {
	
	List<activity> activities = Collections.synchronizedList(new ArrayList<activity>());
	List<activity> newActivities = Collections.synchronizedList(new ArrayList<activity>());
	private JTextPane textPane;
	Style defaultStyle;
	StyledDocument styledDoc;
	
	public ActivityPane(){
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textPane = new JTextPane();
		textPane.setEditable(false);
		this.setViewportView(textPane);
		styledDoc = textPane.getStyledDocument();
		defaultStyle = styledDoc.addStyle("DefaultStyle", null);
		StyleConstants.setForeground(defaultStyle, Color.black);
		ActivityLog.setPane(this);
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
					textPane.getStyledDocument().insertString(endPosition, a.getName() + ": " + a.getMessage() + "\n", defaultStyle);
				}
				catch (BadLocationException e){
					e.printStackTrace();
				}
			}
		}
		newActivities.clear();
	}
	
	
}
