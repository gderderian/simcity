package restaurant1.gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface Restaurant1Gui {

    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();
    
    Map<Object, Dimension> tableLocations = new HashMap<Object, Dimension>();    
}
