package seedu.stock.logic.commands;

import seedu.stock.model.Model;

public class SuggestionCommand extends Command {

    private String toBeDisplayed;

    /**
     * Constructs a new suggestion command.
     *
     * @param toBeDisplayed The suggestion to be displayed to the user.
     */
    public SuggestionCommand(String toBeDisplayed) {
        this.toBeDisplayed = toBeDisplayed;
    }

    /**
     * Executes current suggestion command.
     *
     * @param model {@code Model} which the command should operate on.
     * @return A command result to be passed to the user.
     */
    @Override
    public CommandResult execute(Model model) {
        model.updateFilteredStockList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(toBeDisplayed);
    }
}
