import javax.swing.*;
public class Test {
    public static void main(String[] args) {
        Tour tour_noir = new Tour( 2);
        JFrame frame = new JFrame("Test Image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(tour_noir.get_icon());
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }
}

