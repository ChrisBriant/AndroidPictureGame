package chrisbriant.uk.picturegame.objects;

public class PicPoint {
    private int x;
    private int y;
    private String pos;


    public PicPoint(int x, int y, String pos) {
        this.x = x / 2;
        this.y = y / 2;
        this.pos = pos;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
