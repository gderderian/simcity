package city.gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface Gui {

    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();
    public void setPresent(boolean t);
    
    Map<Object, Dimension> tableLocations = new HashMap<Object, Dimension>();
}
