package map.data;

/**
 * This enum has the various possible states of the logo maker app
 * during the editing process which helps us determine which controls
 * are usable or not and what specific user actions should affect.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public enum mapState {
    SELECTING_NODE,
    DRAGGING_NODE,
    STARTING_RECTANGLE,
    STARTING_LINE,
    STARTING_ELLIPSE,
    SIZING_SHAPE,
    DRAGGING_NOTHING,
    SIZING_NOTHING,
    ADDING_STATION_TO_LINE,
    REMOVING_STATION_FROM_LINE
}
