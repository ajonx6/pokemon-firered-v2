package firered;

public class Maths {
	// Interpolates between start and end
	public static double linearInterpolate(double start, double end, double ratio) {
		return clamp(start + (end - start) * ratio, start, end);
	}

	// Clamps value v between the 2 bounds
	public static double clamp(double value, double bound1, double bound2) {
		double bottom = Math.min(bound1, bound2);
		double top = Math.max(bound1, bound2);
		return Math.max(bottom, Math.min(value, top));
	}
}