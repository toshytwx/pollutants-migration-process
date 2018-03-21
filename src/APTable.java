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
    private JFormattedTextField c0paramStroncii;
    private JButton buttonBack;
    private JComboBox groundType;

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
        c0paramStroncii.setValue(0.1);
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
        tuneComboBox();
        GroundTypes.fillMap();
    }

    private void tuneComboBox() {
        DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel();
        defaultComboBoxModel.addElement("Густа трава, дернина, підстилка");
        defaultComboBoxModel.addElement("Рідкісна трава без деревини і підстилки");
        defaultComboBoxModel.addElement("Трава середньої гущини, дернина");
        defaultComboBoxModel.addElement("Густа трава, дернина");
        defaultComboBoxModel.addElement("Хвойний ліс");
        defaultComboBoxModel.addElement("Змішаний ліс");
        groundType.setModel(defaultComboBoxModel);
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
        String selectedItem = (String) groundType.getSelectedItem();
        ArrayList<Double> cesiumAndStronciiLamdas = GroundTypes.groundMap.get(selectedItem);
        ArrayList<Double> C0 = getElement0for20Years(C01cesium, cesiumAndStronciiLamdas.get(0));
        ArrayList<Double> S0 = getElement0for20Years(S01stroncii, cesiumAndStronciiLamdas.get(1));
        for (int year = 1; year <= 20; year++) {
            for (int depth = 1; depth <= 20; depth++) {
                Vector rowData = new Vector(6);
                double cesiumValueCurrentYearAndDepth = C0.get(year) * Math.exp((double) cesiumAndStronciiLamdas.get(0) * depth);
                double stronciiValueCurrentYearAndDepth = S0.get(year) * Math.exp((double) cesiumAndStronciiLamdas.get(1) * depth);
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

        showSummary();
    }

    private void showSummary() {
        Double valueAt = (double) dataTable.getModel().getValueAt(380, 0);
        String groundMark = decideGroundMark(valueAt);
        JOptionPane.showMessageDialog(null, "Значення АП через 20 років на 1 см грунту: " + valueAt + "\n" + groundMark);
    }

    private String decideGroundMark(double apValue) {
        if (apValue > 1.71) {
            return "Оцінка грунтів:" + "\n" + "Умовно сприятливі" + "\n" +  "Стратегія екол.-рац. використ. земель:" + "\n" + "Зона економ. доцільного використання земель";
        } else if (apValue <= 1.7 && apValue > -1.7) {
            return "Оцінка грунтів:" + "\n" + "Задовільні" + "\n" +  "Стратегія екол.-рац. використ. земель:" + "\n" + "Зона економ. доцільного використання земель";
        } else if (apValue <= 1.7 && apValue >= -5.09) {
            return "Оцінка грунтів:" + "\n" + "Умовно задовільні" + "\n" +  "Стратегія екол.-рац. використ. земель:" + "\n" + "Зона використання земель у режимі збереження";
        } else if (apValue <= -5.10 && apValue >= -8.49) {
            return "Оцінка грунтів:" + "\n" + "Погіршені" + "\n" +  "Стратегія екол.-рац. використ. земель:" + "\n" + "Зона економ. адаптивного використання земель";
        } else if (apValue <= -8.5) {
            return "Оцінка грунтів:" + "\n" + "Екологічного лиха" + "\n" +  "Стратегія екол.-рац. використ. земель:" + "\n" + "Зона використання земель у режимі відновлення";
        }
        return  "";
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
