import java.io.Serializable;

public class Deplacement implements Serializable {
    private int x_origine, y_origine, x_dest, y_dest;

    public Deplacement(int x1, int y1, int x2, int y2){
        this.x_origine = x1;
        this.y_origine = y1;
        this.x_dest = x2;
        this.y_dest = y2;
    }

    public int[] get_deplacement(){
        return new int[]{x_origine, y_origine, x_dest, y_dest};
    }
}
