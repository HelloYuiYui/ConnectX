/**
 * The main class of the Connect Four game.
 *
 * @author Enes IA
 */
public final class ConnectFour
{
	/**
	 * The code follows a design pattern called Model-View-Controller (MVC).
	 * The main method instantiates each of these components and then starts the game loop.
	 *
	 * @param args No arguments expected.
	 */
	public static void main(String[] args)
	{
		// Creates an AI agent to handle NPC moves.
		AI agentAI = new AI();

		// Creates a model representing the state of the game.
		Model model = new Model();
		
		// This text-based view is used to communicate with the user.
		// It can print the state of the board and handles user input.
		TextView view = new TextView();
		
		// The controller facilitates communication between model and view.
		// It also contains the main loop that controls the sequence of events.
		Controller controller = new Controller(model, view, agentAI);
		
		// Start a new session.
		controller.startSession();
	}
}
