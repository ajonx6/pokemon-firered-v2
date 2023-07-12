package firered.util;

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

	public void set(Vector v) {
		this.x = v.x;
		this.y = v.y;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static double length(Vector v) {
		return Math.sqrt(v.x * v.x + v.y * v.y);
	}

	public static Vector add(Vector v, double x, double y) {
		return new Vector(v.x + x, v.y + y);
	}

	public static Vector sub(Vector v, double x, double y) {
		return new Vector(v.x - x, v.y - y);
	}

	public static Vector sub(Vector v, Vector u) {
		return new Vector(v.x - u.x, v.y - u.y);
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

	public static boolean equals(Vector v1, Vector v2) {
		return v1.x == v2.x && v1.y == v2.y;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}