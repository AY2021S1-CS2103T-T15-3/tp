package seedu.stock.ui;

import java.util.Map;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.stock.commons.core.GuiSettings;
import seedu.stock.commons.core.LogsCenter;
import seedu.stock.logic.Logic;
import seedu.stock.logic.commands.CommandResult;
import seedu.stock.logic.commands.SourceStatisticsCommand;
import seedu.stock.logic.commands.exceptions.CommandException;
import seedu.stock.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private StockListPanel stockListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private SourceStatisticsWindow sourceStatisticsWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private StackPane stockListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        helpWindow = new HelpWindow();
        //TODO add all the stats window here
        sourceStatisticsWindow = new SourceStatisticsWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }



    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {

        stockListPanel = new StockListPanel(logic.getFilteredStockList());
        stockListPanelPlaceholder.getChildren().add(stockListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getStockBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    /**
     * Opens the SourceStatisticsWindow or focuses on it if it's already opened.
     */
    @FXML
    public void handleSourceStatistics(Map<String, Integer> statisticsData) {
        if (!sourceStatisticsWindow.isShowing()) {
            sourceStatisticsWindow.show(statisticsData);
        } else {
            sourceStatisticsWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        sourceStatisticsWindow.hide();
        primaryStage.hide();
    }

    public StockListPanel getStockListPanel() {
        return stockListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.stock.logic.Logic#execute(String)
     */
    @FXML
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isShowStatistics()) {
                String type = commandResult.getStatisticsType();
                Map<String, Integer> statisticsData = commandResult.getStatisticsData();

                switch (type) {

                case SourceStatisticsCommand.STATISTICS_TYPE:
                    handleSourceStatistics(statisticsData);
                    break;

                default:
                    throw new CommandException("Unsupported statistics type!");
                }
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

}

