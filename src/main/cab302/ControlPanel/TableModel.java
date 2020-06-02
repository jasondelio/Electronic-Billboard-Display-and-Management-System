package cab302.ControlPanel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * This class sets the renderer model of table on weekly schedule
 */
class TableModel extends JLabel implements ListCellRenderer {

    /**
     * @param table
     * @see CalendarGUI
     */
    TableModel(JTable table) {
        JTableHeader header = table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());

    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText((value == null) ? "" : value.toString());
//        setBorder(isSelected? LineBorder.createGrayLineBorder() : EmptyBorder.getInteriorRectangle());
        return this;
    }
}