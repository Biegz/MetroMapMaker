package map;

/**
 * This class provides the properties that are needed to be loaded for
 * setting up goLogoLo workspace controls including language-dependent
 * text.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public enum mapPropertyType {
    SELECT,
    REMOVE,
    ADD_RECTANGLE,
    ADD_ELLIPSE,
    ADD_IMAGE,
    ADD_TEXT,
    BOLD_TEXT,
    ITALICIZE_TEXT,
    MOVE_TO_BACK,
    MOVE_TO_FRONT,
    SNAPSHOT,
    
    /* goLogoLo WORKSPACE ICONS */
    SELECT_ICON,    
    REMOVE_ICON,    
    ADD_RECTANGLE_ICON,    
    ADD_ELLIPSE_ICON,
    ADD_IMAGE_ICON,    
    ADD_TEXT_ICON, 
    BOLD_TEXT_ICON,    
    ITALICIZE_TEXT_ICON,    
    MOVE_TO_BACK_ICON,
    MOVE_TO_FRONT_ICON,
    SNAPSHOT_ICON,

    /* goLogoLo WORKSPACE TOOLTIPS */
    SELECT_TOOLTIP,
    REMOVE_TOOLTIP,
    ADD_RECTANGLE_TOOLTIP,
    ADD_ELLIPSE_TOOLTIP,
    ADD_IMAGE_TOOLTIP,
    ADD_TEXT_TOOLTIP,
    BOLD_TEXT_TOOLTIP,
    ITALICIZE_TEXT_TOOLTIP,
    MOVE_TO_BACK_TOOLTIP,
    MOVE_TO_FRONT_TOOLTIP,
    BACKGROUND_TOOLTIP,
    FILL_TOOLTIP,
    OUTLINE_TOOLTIP,
    SNAPSHOT_TOOLTIP,

    /* THESE ARE LABELS THAT WILL NEED LANGUAGE SPECIFIC CONTENT */
    BACKGROUND_COLOR,
    FILL_COLOR,
    OUTLINE_COLOR,
    OUTLINE_THICKNESS,
    BACKGROUND_COLOR_TEXT,
    FILL_COLOR_TEXT,
    OUTLINE_COLOR_TEXT,
    OUTLINE_THICKNESS_TEXT,

    /* goLogoLo WORKSPACE OPTIONS */
    FONT_FAMILY_COMBO_BOX_OPTIONS,
    FONT_SIZE_COMBO_BOX_OPTIONS,
    
    /* DEFAULT INITIAL POSITIONS WHEN ADDING IMAGES AND TEXT */
    DEFAULT_NODE_X,
    DEFAULT_NODE_Y
    
}