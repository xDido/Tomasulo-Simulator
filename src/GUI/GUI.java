package GUI;

import Memory.Instruction;
import Process.Main;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GUI {
    private final JFrame frame;
    private final Main main;
    private final JLabel clockCycleLabel;
    private final JLabel fetchQueueLabel;
    private final JLabel issueQueueLabel;
    private final JLabel executeQueueLabel;
    private final JLabel writeQueueLabel;

    // Tables
    private final JTable mulTable;
    private final JTable addTable;
    private final JTable loadTable;
    private final JTable iterationsTable;
    private final JTable registerFileTable;
    private final JTable storeTable;
    private final JTable memoryTable;

    public GUI(int[] configuration) {
        this.main = new Main(configuration);

        // Initialize labels
        this.clockCycleLabel = createLabel("Clock Cycle: 0", SwingConstants.CENTER, Font.PLAIN);
        this.fetchQueueLabel = createQueueLabel("Fetch Queue: ");
        this.issueQueueLabel = createQueueLabel("Issue Queue: ");
        this.executeQueueLabel = createQueueLabel("Execute Queue: ");
        this.writeQueueLabel = createQueueLabel("Write Queue: ");

        // Initialize tables
        this.mulTable = createTable("Tag", "Op", "Busy", "Vj", "Vq", "Qj", "Qk", "A");
        this.addTable = createTable("Tag", "Op", "Busy", "Vj", "Vq", "Qj", "Qk", "A");
        this.loadTable = createTable("Tag", "Busy", "Address");
        this.iterationsTable = createTable("Iteration#", "Instruction", "Operand", "j", "k", "Issue", "Execution Complete", "Write Result");
        this.registerFileTable = createTable("Tag", "Qi", "Content");
        this.storeTable = createTable("Tag", "Busy", "V", "Q", "Address");
        this.memoryTable = createTable("Address", "Content");

        // Center align all tables
        centerAlignTables(mulTable, addTable, loadTable, iterationsTable, registerFileTable, storeTable, memoryTable);

        // Create and setup the main frame
        this.frame = setupMainFrame();
    }

    private JLabel createLabel(String text, int alignment, int fontStyle) {
        JLabel label = new JLabel(text, alignment);
        label.setFont(new Font("Arial", fontStyle, 14));
        return label;
    }

    private JLabel createQueueLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        return label;
    }

    private JFrame setupMainFrame() {
        JFrame frame = new JFrame("Tomasulo Algorithm Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup main layout
        frame.add(clockCycleLabel, BorderLayout.NORTH);
        frame.add(createMainPanel(), BorderLayout.CENTER);
        frame.add(createRegisterFilePanel(), BorderLayout.EAST);
        frame.add(createNextButton(), BorderLayout.SOUTH);

        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = (int) (screenWidth * 0.8);
        int frameHeight = (int) (screenHeight * 0.8);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private JPanel createRegisterFilePanel() {
        return createTablePanel(registerFileTable, "Register File");
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(4, 2));

        // Add reservation station panels
        mainPanel.add(createTablePanel(mulTable, "Mul Stations"));
        mainPanel.add(createTablePanel(addTable, "Add Stations"));
        mainPanel.add(createTablePanel(loadTable, "Load Stations"));
        mainPanel.add(createTablePanel(storeTable, "Store Stations"));

        // Add memory and instructions panels
        mainPanel.add(createTablePanel(memoryTable, "Memory"));
        mainPanel.add(createTablePanel(iterationsTable, "Instructions Table"));

        // Add queue status panels
        mainPanel.add(createQueueStatusPanel(fetchQueueLabel, issueQueueLabel));
        mainPanel.add(createQueueStatusPanel(executeQueueLabel, writeQueueLabel));

        return mainPanel;
    }

    private JPanel createQueueStatusPanel(JLabel... labels) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 1));
        for (JLabel label : labels) {
            panel.add(label);
        }
        return panel;
    }

    private JButton createNextButton() {
        JButton nextButton = new JButton("NEXT");
        nextButton.setFont(new Font("", Font.PLAIN, 14));
        nextButton.setPreferredSize(new Dimension(150, 50));
        nextButton.addActionListener(e -> updateSimulation());
        return nextButton;
    }

    private void updateSimulation() {
        main.runOne();
        updateClockCycle();
        updateQueuesStatus();
        updateTables();
    }

    private void updateClockCycle() {
        clockCycleLabel.setText("Clock Cycle: " + main.getClock());
    }

    private void updateQueuesStatus() {
        fetchQueueLabel.setText("Fetch Queue: " + main.getFetchQueue());
        issueQueueLabel.setText("Issue Queue: " + main.getIssueQueue());
        executeQueueLabel.setText("Execute Queue: " + main.getExecuteQueue());
        writeQueueLabel.setText("Write Queue: " + main.getWriteQueue());
    }

    private void updateTables() {
        updateInstructionsTable();
        updateReservationStations();
        updateMemoryTable();
        updateRegisterFile();
    }

    private void updateInstructionsTable() {
        ArrayList<Instruction> instructions = main.getInstructionTable();
        DefaultTableModel model = (DefaultTableModel) iterationsTable.getModel();

        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            Object[] row = {
                    instruction.getIteration(),
                    instruction.getInstruction(),
                    instruction.getDestinationRegister(),
                    instruction.getJ(),
                    instruction.getK(),
                    instruction.getIssue(),
                    instruction.getExecutionComplete(),
                    instruction.getWriteResult()
            };

            if (model.getRowCount() <= i) {
                model.addRow(row);
            } else {
                for (int j = 0; j < row.length; j++) {
                    model.setValueAt(row[j], i, j);
                }
            }
        }
    }

    private void updateMemoryTable() {
        DefaultTableModel model = (DefaultTableModel) memoryTable.getModel();
        Object[] memory = main.getMainMemory().getMemory();

        for (int i = 0; i < memory.length; i++) {
            if (main.getMainMemory().getChanged()[i] == 1) {
                Object[] row = {i, memory[i]};
                int rowIndex = findRowIndex(model, i);

                if (rowIndex == -1) {
                    model.addRow(row);
                } else {
                    model.setValueAt(memory[i], rowIndex, 1);
                }
            }
        }
    }

    private void updateRegisterFile() {
        DefaultTableModel model = (DefaultTableModel) registerFileTable.getModel();
        String[] tags = main.getRegisterFile().getTag();
        String[] qs = main.getRegisterFile().getQ();
        int[] contents = main.getRegisterFile().getContent();

        for (int i = 0; i < tags.length; i++) {
            Object[] row = {tags[i], qs[i], contents[i]};

            if (model.getRowCount() <= i) {
                model.addRow(row);
            } else {
                for (int j = 0; j < row.length; j++) {
                    model.setValueAt(row[j], i, j);
                }
            }
        }
    }

    private void updateReservationStations() {
        // Update MUL stations
        updateTableRows(mulTable,
                main.getReservationStations().getTagmul(),
                main.getReservationStations().getOpmul(),
                main.getReservationStations().getBusymul(),
                main.getReservationStations().getVjmul(),
                main.getReservationStations().getVkmul(),
                main.getReservationStations().getQjmul(),
                main.getReservationStations().getQkmul()
        );

        // Update ADD stations
        updateTableRows(addTable,
                main.getReservationStations().getTagAdd(),
                main.getReservationStations().getOpadd(),
                main.getReservationStations().getBusyadd(),
                main.getReservationStations().getVjadd(),
                main.getReservationStations().getVkadd(),
                main.getReservationStations().getQjadd(),
                main.getReservationStations().getQkadd()
        );

        // Update LOAD stations
        updateTableRows(loadTable,
                main.getReservationStations().getTagload(),
                main.getReservationStations().getBusyload(),
                main.getReservationStations().getAddressload()
        );

        // Update STORE stations
        updateTableRows(storeTable,
                main.getReservationStations().getTagstore(),
                main.getReservationStations().getBusystore(),
                main.getReservationStations().getVstore(),
                main.getReservationStations().getQstore(),
                main.getReservationStations().getAddressstore()
        );
    }

    private void updateTableRows(JTable table, Object... data) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = ((String[]) data[0]).length;

        for (int i = 0; i < rowCount; i++) {
            Object[] row = new Object[data.length];
            for (int j = 0; j < data.length; j++) {
                if (data[j] instanceof String[]) {
                    row[j] = ((String[]) data[j])[i];
                } else if (data[j] instanceof int[]) {
                    row[j] = ((int[]) data[j])[i];
                }
            }

            if (model.getRowCount() <= i) {
                model.addRow(row);
            } else {
                for (int j = 0; j < row.length; j++) {
                    model.setValueAt(row[j], i, j);
                }
            }
        }
    }

    private int findRowIndex(DefaultTableModel model, int address) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if ((int) model.getValueAt(row, 0) == address) {
                return row;
            }
        }
        return -1;
    }

    // Helper methods
    private JPanel createTablePanel(JTable table, String title) {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(title));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        return tablePanel;
    }

    private JTable createTable(String... columnNames) {
        DefaultTableModel model = new DefaultTableModel();
        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }
        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        return table;
    }

    private void centerAlignTables(JTable... tables) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (JTable table : tables) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        int[] defaultConfiguration = {5, 5, 5, 5, 5, 5, 5, 5};
        int[] configuration = getConfiguration(defaultConfiguration);

        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI(configuration);
            gui.show();
        });
    }

    private static int[] getConfiguration(int[] defaultValues) {
        String[] labels = {
                "Size of MUL Reservation Station:",
                "Size of ADD Reservation Station:",
                "Size of LOAD Reservation Station:",
                "Size of STORE Reservation Station:",
                "Number of MUL Cycles:",
                "Number of ADD Cycles:",
                "Number of LOAD Cycles:",
                "Number of STORE Cycles:"
        };

        JTextField[] fields = new JTextField[labels.length];
        JPanel panel = new JPanel(new GridLayout(labels.length, 2));

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            fields[i] = new JTextField(String.valueOf(defaultValues[i]));
            panel.add(fields[i]);
        }

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter Configuration", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            return parseConfiguration(fields, defaultValues);
        }

        JOptionPane.showMessageDialog(null,
                "Operation cancelled. Default configuration will be used (5 for all).");
        return defaultValues;
    }

    private static int[] parseConfiguration(JTextField[] fields, int[] defaultValues) {
        int[] values = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            try {
                values[i] = Integer.parseInt(fields[i].getText());
            } catch (NumberFormatException e) {
                values[i] = defaultValues[i];
            }
        }
        return values;
    }
}