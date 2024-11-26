import javax.swing.*;
public class Test {
    public static void main(String[] args) {
        Fou fou_noir = new Fou(0, 2, 1);
        JFrame frame = new JFrame("Test Image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(fou_noir.get_icon());
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }
}

