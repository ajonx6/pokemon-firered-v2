package firered.entity;

public enum Direction {
	UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);
	
	public int dx, dy;
	
	Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public static Direction getByInitial(String c) {
		c = c.toLowerCase();
		if (c.equals("u")) return UP;
		if (c.equals("d")) return DOWN;
		if (c.equals("l")) return LEFT;
		if (c.equals("r")) return RIGHT;
		return null;
	}
	
	public Direction flip() {
		if (this == UP) return DOWN;
		if (this == DOWN) return UP;
		if (this == LEFT) return RIGHT;
		if (this == RIGHT) return LEFT;
		return null;
	}
}