package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import screen.GameScreen;
import screen.HighScoreScreen;
import screen.ScoreScreen;
import screen.Screen;
import screen.TitleScreen;
import screen.SettingScreen;
import screen.StoreScreen;

/**
 * Implements core game logic.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class Core {

	/** Width of current screen. */
	private static final int WIDTH = 448;
	/** Height of current screen. */
	private static final int HEIGHT = 520;
	/** Max fps of current screen. */
	private static final int FPS = 60;

	/** Max lives. */
	private static final int MAX_LIVES = 3;
	/** Levels between extra life. */
	private static final int EXTRA_LIFE_FRECUENCY = 3;
	/** Total number of levels. */
	private static final int NUM_LEVELS = 5;

	/** Difficulty settings for level 1. */
	private static final GameSettings SETTINGS_LEVEL_1_E =
			new GameSettings(5, 4, 60, 2000);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2_E =
			new GameSettings(5, 5, 50, 2500);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3_E =
			new GameSettings(6, 5, 40, 1500);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4_E =
			new GameSettings(6, 6, 30, 1500);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5_E =
			new GameSettings(7, 6, 20, 1000);
	private static final GameSettings SETTINGS_LEVEL_1_N =
			new GameSettings(6, 4, 60, 2000);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2_N =
			new GameSettings(6, 5, 50, 2500);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3_N =
			new GameSettings(7, 5, 40, 1500);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4_N =
			new GameSettings(8, 6, 30, 1500);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5_N =
			new GameSettings(9, 6, 20, 1000);
	private static final GameSettings SETTINGS_LEVEL_1_H =
			new GameSettings(6, 5, 60, 2000);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2_H =
			new GameSettings(7, 5, 50, 2500);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3_H =
			new GameSettings(8, 5, 40, 1500);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4_H =
			new GameSettings(9, 6, 30, 1500);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5_H =
			new GameSettings(10, 6, 20, 1000);

	/** Frame to draw the screen on. */
	private static Frame frame;
	/** Screen currently shown. */
	private static Screen currentScreen;
	/** Difficulty settings list. */
	private static List<GameSettings> gameSettings;
	/** Application logger. */
	private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());
	/** Logger handler for printing to disk. */
	private static Handler fileHandler;
	/** Logger handler for printing to console. */
	private static ConsoleHandler consoleHandler;


	/**
	 * Test implementation.
	 *
	 * @param args
	 *            Program args, ignored.
	 */
	public static void main(final String[] args) {
		try {
			LOGGER.setUseParentHandlers(false);

			fileHandler = new FileHandler("log");
			fileHandler.setFormatter(new MinimalFormatter());

			consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new MinimalFormatter());

			LOGGER.addHandler(fileHandler);
			LOGGER.addHandler(consoleHandler);
			LOGGER.setLevel(Level.ALL);

		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}

		frame = new Frame(WIDTH, HEIGHT);
		DrawManager.getInstance().setFrame(frame);
		int width = frame.getWidth();
		int height = frame.getHeight();

		gameSettings = new ArrayList<GameSettings>();
		gameSettings.add(SETTINGS_LEVEL_1_E);
		gameSettings.add(SETTINGS_LEVEL_2_E);
		gameSettings.add(SETTINGS_LEVEL_3_E);
		gameSettings.add(SETTINGS_LEVEL_4_E);
		gameSettings.add(SETTINGS_LEVEL_5_E);
		gameSettings.add(SETTINGS_LEVEL_1_N);
		gameSettings.add(SETTINGS_LEVEL_2_N);
		gameSettings.add(SETTINGS_LEVEL_3_N);
		gameSettings.add(SETTINGS_LEVEL_4_N);
		gameSettings.add(SETTINGS_LEVEL_5_N);
		gameSettings.add(SETTINGS_LEVEL_1_H);
		gameSettings.add(SETTINGS_LEVEL_2_H);
		gameSettings.add(SETTINGS_LEVEL_3_H);
		gameSettings.add(SETTINGS_LEVEL_4_H);
		gameSettings.add(SETTINGS_LEVEL_5_H);

		GameState gameState;

		int returnCode = 1;
		do {
			gameState = new GameState(1, 0, MAX_LIVES, 0, 0);

			switch (returnCode) {
				case 1:
					// Main menu.
					currentScreen = new TitleScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " title screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing title screen.");
					break;
				case 2:
					// Game & score
					LOGGER.info("\n--------------------------------");
					LOGGER.info("Select Difficulty!");
					LOGGER.info("1 : EASY");
					LOGGER.info("2 : NORMAL");
					LOGGER.info("3 : HARD");
					Scanner sc = new Scanner(System.in);
					int difficulty = sc.nextInt(); // initialize
					while (difficulty < 0 || difficulty > 3) {
						LOGGER.info("Please write valid level number...");
						difficulty = sc.nextInt();
					}
					LOGGER.info("--------------------------------\n");
					LOGGER.info("Start '" + difficulty + "' level\n");
					do {
						// One extra live every few levels.
						boolean bonusLife = gameState.getLevel()
								% EXTRA_LIFE_FRECUENCY == 0
								&& gameState.getLivesRemaining() < MAX_LIVES;

						currentScreen = new GameScreen(gameState,
								gameSettings.get(gameState.getLevel() - 1),
								bonusLife, width, height, FPS);
						LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
								+ " game screen at " + FPS + " fps.");
						frame.setScreen(currentScreen);
						LOGGER.info("Closing game screen.");

						gameState = ((GameScreen) currentScreen).getGameState();

						gameState = new GameState(gameState.getLevel() + 1,
								gameState.getScore(),
								gameState.getLivesRemaining(),
								gameState.getBulletsShot(),
								gameState.getShipsDestroyed());

					} while (gameState.getLivesRemaining() > 0
							&& gameState.getLevel() <= NUM_LEVELS);

					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " score screen at " + FPS + " fps, with a score of "
							+ gameState.getScore() + ", "
							+ gameState.getLivesRemaining() + " lives remaining, "
							+ gameState.getBulletsShot() + " bullets shot and "
							+ gameState.getShipsDestroyed() + " ships destroyed.");
					currentScreen = new ScoreScreen(width, height, FPS, gameState);
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing score screen.");
					break;
				case 3:
					// High scores.
					currentScreen = new HighScoreScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " high score screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing high score screen.");
					break;
				case 4:
					//Setting.
					currentScreen = new SettingScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " setting screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing setting screen.");
					break;
				case 5:
					//Store.
					currentScreen = new StoreScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " store screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing store screen.");
					break;
				default:
					break;
			}

		} while (returnCode != 0);

		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}

	/**
	 * Constructor, not called.
	 */
	private Core() {

	}

	/**
	 * Controls access to the logger.
	 *
	 * @return Application logger.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Controls access to the drawing manager.
	 *
	 * @return Application draw manager.
	 */
	public static DrawManager getDrawManager() {
		return DrawManager.getInstance();
	}

	/**
	 * Controls access to the input manager.
	 *
	 * @return Application input manager.
	 */
	public static InputManager getInputManager() {
		return InputManager.getInstance();
	}

	/**
	 * Controls access to the file manager.
	 *
	 * @return Application file manager.
	 */
	public static FileManager getFileManager() {
		return FileManager.getInstance();
	}

	/**
	 * Controls creation of new cooldowns.
	 *
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @return A new cooldown.
	 */
	public static Cooldown getCooldown(final int milliseconds) {
		return new Cooldown(milliseconds);
	}

	/**
	 * Controls creation of new cooldowns with variance.
	 *
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @param variance
	 *            Variation in the cooldown duration.
	 * @return A new cooldown with variance.
	 */
	public static Cooldown getVariableCooldown(final int milliseconds,
											   final int variance) {
		return new Cooldown(milliseconds, variance);
	}
}