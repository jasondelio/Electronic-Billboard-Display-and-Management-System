import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeUI extends JFrame implements ActionListener {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    private Color initialColor = Color.WHITE;

    private String hexColour;

    public HomeUI(){
        super("Billboard Control Panel");
        setSize(WIDTH, HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);
        JTabbedPane pane = new JTabbedPane();
        JPanel panelHome = new JPanel(new GridLayout());
        JLabel lblWelcome = new JLabel("Welcome to Billboard Control Panel !");
        lblWelcome.setFont (lblWelcome.getFont().deriveFont (24.0f));


        panelHome.add(lblWelcome);
        JButton btnLogout = new JButton("Logout");




        JPanel panelListBillboards = new JPanel();
        panelListBillboards.add(new JLabel("List Billboards"));

        JPanel panelCreateBillboards = new JPanel();
        JButton btnColour = new JButton("Colour");
        btnColour.addActionListener(this);
        panelCreateBillboards.add(btnColour);

        JScrollPane panelEditUsers = new JScrollPane();
        panelEditUsers.add(new JLabel("Edit Users .."));

        JPanel panelScheduleBillboards = new JPanel();
        panelScheduleBillboards.add(new JLabel("Schedule Billboards.."));

        pane.add("Home", panelHome);
        pane.add("List Billboards", panelListBillboards);
        pane.add("Create Billboards", panelCreateBillboards);
        pane.add("Edit Users", panelEditUsers);
        pane.add("Schedule Billboards", panelScheduleBillboards);

        this.add(pane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int r;
        int g;
        int b;
        Color color = JColorChooser.showDialog(this,
                "Select a color", initialColor);
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();
        hexColour = String.format("#%02X%02X%02X", r, g, b);
        initialColor = new Color(r, g, b);
        System.out.println(initialColor);
    }
}
