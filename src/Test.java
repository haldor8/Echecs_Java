import javax.swing.*;
public class Test {
    public static void main(String[] args) {
        Reine roi_noir = new Reine(0, 2, 2);
        JFrame frame = new JFrame("Test Image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(roi_noir.get_icon());
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }
}

