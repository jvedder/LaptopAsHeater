package LaptopAsHeater;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class AppWindow extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private final Border border = BorderFactory.createLoweredBevelBorder();
    private final JTextField totalText, stateText;
    private final JButton startButton, stopButton;

    private WorkerTask workerTask;
    static AppWindow instance = null;

    private JPanel makePanel(int axis)
    {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, axis));
        return p;
    }

    private JTextField makeText()
    {
        JTextField t = new JTextField(20);
        t.setEditable(false);
        t.setHorizontalAlignment(JTextField.RIGHT);
        t.setBorder(border);
        return t;
    }

    private JButton makeButton(String caption)
    {
        JButton b = new JButton(caption);
        b.setActionCommand(caption);
        b.addActionListener(this);
        return b;
    }

    public AppWindow()
    {
        super("Laptop As Heater");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
         * Top Button Panel (horizontal layout)
         */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // margin
        buttonPanel.add(Box.createHorizontalGlue()); // expandable
        // buttonPanel.add(Box.createGlue()); // expandable
        buttonPanel.add(startButton = makeButton("Start"));
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // fixed
        buttonPanel.add(stopButton = makeButton("Stop"));
        stopButton.setEnabled(false);

        /*
         * Bottom Text Panel (vertical layout)
         */
        JPanel textPanel = makePanel(BoxLayout.Y_AXIS);
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // margin
        textPanel.add(totalText = makeText());
        textPanel.add(Box.createRigidArea(new Dimension(0, 10))); // fixed
        textPanel.add(stateText = makeText());
        // textPanel.add(Box.createVerticalGlue()); // expandable
        // textPanel.add(Box.createGlue()); // expandable

        /*
         * Overall Collector Panel (vertical layout)
         */
        JPanel outerPanel = makePanel(BoxLayout.Y_AXIS);

        outerPanel.add(textPanel);
        outerPanel.add(buttonPanel);
        this.getContentPane().add(outerPanel);

        // Display the window.
        pack();
        setVisible(true);
    }

    public static AppWindow getInstance()
    {
        if (instance == null) instance = new AppWindow();
        return instance;
    }

    public void actionPerformed(ActionEvent e)
    {
        if ("Start" == e.getActionCommand())
        {
            if ((workerTask == null) || (workerTask.getWorkerState() == WorkerState.DONE))
            {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                workerTask = new WorkerTask();
                workerTask.execute();
            }
        }
        else if ("Stop" == e.getActionCommand())
        {
            if (workerTask != null)
            {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                workerTask.requestShutdown();
            }
        }

    }

    public void showWorkerStatus(WorkerStatus status)
    {
        totalText.setText(String.format("%d", status.total));
        stateText.setText(status.state.toString());
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                AppWindow.getInstance();
            }
        });
    }
}
