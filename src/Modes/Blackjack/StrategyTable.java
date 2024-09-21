package Modes.Blackjack;

import Menus.Palette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class StrategyTable extends JTable {
    private JTable table = this;
    private StrategyTableModel model = new StrategyTableModel();
    public StrategyTable() {

        setModel(model);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setCellSelectionEnabled(true);

        TableCellEditor cellEditor;
        for(int i = 0; i < model.getColumnCount(); i++){
            TableColumn column = getColumn(model.getColumnName(i));
            cellEditor = new DefaultCellEditor(new JComboBox<>(StrategyTableModel.Action.values()));
            cellEditor.addCellEditorListener(new CellEditorListener() {
                @Override
                public void editingStopped(ChangeEvent e) {
                    int[] selectedRows = table.getSelectedRows();
                    int[] selectedColumns = table.getSelectedColumns();
                    int editedRow = table.getEditingRow();
                    int editedColumn = table.getEditingColumn();
                    System.out.println(editedRow + ", " + editedColumn);
                    if(editedRow != -1 && editedColumn != -1) {
                        Object newValue = table.getValueAt(editedRow, editedColumn);
                        for (int row : selectedRows) {
                            for (int col : selectedColumns) {
                                if (row != editedRow || col != editedColumn) {
                                    if (table.isCellSelected(row, col))
                                        table.setValueAt(newValue, row, col);
                                }
                            }
                        }
                    }
                }

                @Override
                public void editingCanceled(ChangeEvent e) {
                    System.out.println("cancel");

                }
            });
            column.setCellEditor(cellEditor);
            table.getTableHeader().setBackground(Palette.HIGHLIGHT_COLOR);
            table.getTableHeader().setForeground(Palette.DEFAULT_FONT_COLOR);
            table.getTableHeader().setFont(Palette.HEADER_FONT);

            TableCellRenderer defaultRenderer = table.getTableHeader().getDefaultRenderer();
            table.getTableHeader().setDefaultRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    if(column == 0) {

                        JPanel panel = new JPanel(new BorderLayout());
                        panel.setBackground(Palette.HIGHLIGHT_COLOR);
                        Component component = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        component.setPreferredSize(new Dimension(50, 50));
                        panel.add(component, BorderLayout.LINE_END);
                        return panel;

                    } else {
                        return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    }
                }
            });
            column.setCellRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel valueLabel = new JLabel();
                    valueLabel.setFont(new Font("Verdana", Font.BOLD, 16));
                    valueLabel.setBackground((row + column) % 2 == 0 ? Palette.BACKGROUND_COLOR : Palette.ALT_BACKGROUND_COLOR);
                    valueLabel.setOpaque(true);
                    valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    switch ((StrategyTableModel.Action) value){
                        case Stand:
                            valueLabel.setText("S");
                            valueLabel.setForeground(Palette.NEGATIVE_FONT_COLOR);
                            break;
                        case Hit:
                            valueLabel.setText("H");
                            valueLabel.setForeground(Palette.DEFAULT_FONT_COLOR);
                            break;
                        default:
                            valueLabel.setText("D");
                            valueLabel.setForeground(Palette.POSITIVE_FONT_COLOR);
                    }

                    if(column == 0){
                        JPanel panel = new JPanel(new GridLayout(1, 2));
                        JLabel headerLabel = new JLabel(model.getRowName(row));
                        headerLabel.setFont(Palette.HEADER_FONT);
                        headerLabel.setBackground(Palette.HIGHLIGHT_COLOR);
                        headerLabel.setForeground(Palette.DEFAULT_FONT_COLOR);
                        headerLabel.setOpaque(true);
                        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        panel.add(headerLabel);
                        panel.add(valueLabel);
                        return panel;
                    }
                    return valueLabel;
                }
            });

        }

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                setRowHeight(50);
                TableColumn column = getColumnModel().getColumn(0);
                column.setPreferredWidth(100);
                column.setMinWidth(100);
                column.setMaxWidth(100);
                for(int i = 1; i < getColumnModel().getColumnCount(); i++){
                    column = getColumnModel().getColumn(i);
                    column.setPreferredWidth(50);
                    column.setMinWidth(50);
                    column.setMaxWidth(50);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }

    @Override
    public StrategyTableModel getModel() {
        return model;
    }
}
