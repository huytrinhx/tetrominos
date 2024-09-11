package tetrominos.models;

public class Position {
	//X coordinates corresponds to column index
	//Y coordinates corresponds to row index
    public int x;
	public int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return String.format("Block (%s,%s)\t", this.x, this.y);
	}
}
