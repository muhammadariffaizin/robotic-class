import java.util.Arrays;
import java.util.List;

public class CheckMazeSolve {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AStarDriver maze = new AStarDriver();
		
		List<Integer> moves = maze.getMove();

		System.out.println(Arrays.toString(moves.toArray()));
	}

}
