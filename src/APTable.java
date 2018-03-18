import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class APTable extends JDialog {
    private JPanel contentPane;
    private JTable dataTable;
    private JButton buttonCount;
    private JButton buttonExport;
    private JFormattedTextField c0paramCesium;
    private JFormattedTextField lambdaCesium;
    private JFormattedTextField c0paramStroncii;
    private JFormattedTextField lambdaStroncii;
    private JButton buttonBack;

    public APTable(double value, double x4Value, double x6Value, double x7Value, double x8Value) {
        setTitle("© 2018 TM-51 Antonkin Dmytro All Rights Reserved");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonBack);

        buttonBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBack();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        DefaultTableModel model = new DefaultTableModel(0, 6);
        Vector colHdrs = new Vector(6);
        colHdrs.addElement("Агроекологічний потенціал:");
        colHdrs.addElement("Рік");
        colHdrs.addElement("См ґрунту");
        colHdrs.addElement("Цезій");
        colHdrs.addElement("Стронцій");
        colHdrs.addElement("(Х5)Сум. вміст радіонуклідів в ґрунті:");
        model.setColumnIdentifiers(colHdrs);
        dataTable.setModel(model);

        c0paramCesium.setValue(0.1);
        lambdaCesium.setValue(0.1);
        c0paramStroncii.setValue(0.1);
        lambdaStroncii.setValue(0.1);
        buttonCount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addData(value, x4Value, x6Value, x7Value, x8Value);
            }
        });
        buttonExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToCSV();
            }
        });
    }

    private void saveToCSV() {
        JFileChooser jFileChooser = new JFileChooser();
        int res = jFileChooser.showDialog(null, "Save file");
        if (res == JFileChooser.APPROVE_OPTION) {
            try (FileWriter excel = new FileWriter(jFileChooser.getSelectedFile())){
                jFileChooser.setSelectedFile(jFileChooser.getSelectedFile());
                TableModel model = dataTable.getModel();

                for (int i = 0; i < model.getColumnCount(); i++) {
                    excel.write(model.getColumnName(i) + "\t");
                }
                excel.write("\n");

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        excel.write(model.getValueAt(i, j).toString() + "\t");
                    }
                    excel.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addData(double value, double x4Value, double x6Value, double x7Value, double x8Value) {
        double C01cesium = (double) c0paramCesium.getValue();
        double S01stroncii = (double) c0paramStroncii.getValue();
        ArrayList<Double> C0 = getElement0for20Years(C01cesium, (double) lambdaCesium.getValue());
        ArrayList<Double> S0 = getElement0for20Years(S01stroncii, (double) lambdaStroncii.getValue());
        for (int year = 1; year <= 20; year++) {
            for (int depth = 1; depth <= 20; depth++) {
                Vector rowData = new Vector(6);
                double cesiumValueCurrentYearAndDepth = C0.get(year) * Math.exp((double) lambdaCesium.getValue() * depth);
                double stronciiValueCurrentYearAndDepth = S0.get(year) * Math.exp((double) lambdaStroncii.getValue() * depth);
                double x5Value = cesiumValueCurrentYearAndDepth + stronciiValueCurrentYearAndDepth;
                double acroEcologicalPotential = value - x4Value + x6Value + x7Value - x8Value - x5Value;
                rowData.addElement(acroEcologicalPotential);
                rowData.addElement(year);
                rowData.addElement(depth);
                rowData.addElement(cesiumValueCurrentYearAndDepth);
                rowData.addElement(stronciiValueCurrentYearAndDepth);
                rowData.addElement(x5Value);
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
                model.addRow(rowData);
                dataTable.setModel(model);
            }
        }
    }

    private ArrayList<Double> getElement0for20Years(double element01Value, double elementLambdaValue) {
        ArrayList<Double> element0for20Years = new ArrayList<>();
        element0for20Years.add(0, element01Value);
        element0for20Years.add(1, element01Value);
        for (int i = 2; i <= 20; i++) {
            double value = element0for20Years.get(i - 1);
            element0for20Years.add(i, value * Math.exp(elementLambdaValue * 0.1));
        }
        return element0for20Years;
    }

    private void onBack() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
