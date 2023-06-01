package pokemon.util;

public class Vector {
    private double x, y;

    public Vector() {
        this(0, 0);
    }

    public Vector(Vector r) {
        this(r.x, r.y);
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public static Vector add(Vector v, double x, double y) {
        return new Vector(v.x + x, v.y + y);
    }

    public static Vector sub(Vector v, double x, double y) {
        return new Vector(v.x - x, v.y - y);
    }

    public static Vector mul(Vector v, double r) {
        return new Vector(v.x * r, v.y * r);
    }

    public static Vector div(Vector v, double r) {
        return new Vector(v.x / r, v.y / r);
    }

    public Vector copy() {
        return new Vector(x, y);
    }

    public int intX() {
        return (int) x;
    }

    public int intY() {
        return (int) y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}