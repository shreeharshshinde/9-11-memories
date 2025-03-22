
import javax.print.attribute.standard.Media;
import javax.swing.JFrame;



public class App {
    public static void main(String[] args) throws Exception {
        int boardwidth = 360;
        int boardheight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setVisible(true);
        frame.setSize(boardwidth, boardheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappybird = new FlappyBird();
        frame.add(flappybird);
        frame.pack();
        flappybird.requestFocus();
        frame.setVisible(true);



    }
}
