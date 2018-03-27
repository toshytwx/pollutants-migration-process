import javax.swing.*;
import java.awt.event.*;

public class Form extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner xOthers;
    private JSpinner x6;
    private JSpinner x7;
    private JSpinner x8;
    private JSpinner x4;
    private JSpinner x5;
    private JButton calculate;

    public Form() {
//        setTitle("© 2018 TM-51 Antonkin Dmytro All Rights Reserved");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

//        tuneSpinners();
//        tuneOtherInfluencers();
        calculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCalculate();
            }
        });
    }

    private void onCalculate() {
        APTable dialog = new APTable((double) xOthers.getValue(),(double)  x4.getValue(),(double)  x6.getValue(),(double)  x7.getValue(),(double)  x8.getValue());
        SwingUtilities.updateComponentTreeUI(dialog);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void tuneOtherInfluencers() {
        double lowerLimit = -5.09 + 14.1 + 0.0 - 4.5 - 60.1 + 0.31;
        double higherLimit = -1.71 + 20.0 + 4.0 - 5.0 - 70.0 + 1.10;

        SpinnerNumberModel spinnerNumberModelXOthers = new SpinnerNumberModel(lowerLimit, lowerLimit, higherLimit, 0.01);
        xOthers.setModel(spinnerNumberModelXOthers);
    }

    private void tuneSpinners() {
        SpinnerNumberModel spinnerNumberModelX4 = new SpinnerNumberModel(14.1, 14.1, 20.0, 0.1);
        x4.setModel(spinnerNumberModelX4);
        SpinnerNumberModel spinnerNumberModelX5 = new SpinnerNumberModel(0, 0, 4.0, 0.1);
        x5.setModel(spinnerNumberModelX5);
        SpinnerNumberModel spinnerNumberModelX6 = new SpinnerNumberModel(4.5, 4.5, 5.0, 0.1);
        x6.setModel(spinnerNumberModelX6);
        SpinnerNumberModel spinnerNumberModelX7 = new SpinnerNumberModel(60.1, 60.1, 70.0, 0.1);
        x7.setModel(spinnerNumberModelX7);
        SpinnerNumberModel spinnerNumberModelX8 = new SpinnerNumberModel(0.31, 0.31, 1.10, 0.01);
        x8.setModel(spinnerNumberModelX8);
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        Form dialog = new Form();
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(dialog);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
