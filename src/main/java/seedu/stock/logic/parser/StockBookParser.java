package seedu.stock.logic.parser;

import static seedu.stock.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.stock.logic.commands.AddCommand;
import seedu.stock.logic.commands.Command;
import seedu.stock.logic.commands.DeleteCommand;
import seedu.stock.logic.commands.ExitCommand;
import seedu.stock.logic.commands.FindCommand;
import seedu.stock.logic.commands.FindExactCommand;
import seedu.stock.logic.commands.HelpCommand;
import seedu.stock.logic.commands.ListCommand;
import seedu.stock.logic.commands.UpdateCommand;
import seedu.stock.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class StockBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        String trimmedUserInput = userInput.trim();
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(trimmedUserInput);
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            try {
                return new AddCommandParser().parse(arguments);
            } catch (ParseException ex) {
                return new SuggestionCommandParser(commandWord, ex.getMessage()).parse(arguments);
            }

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommandParser().parse(arguments);

        case UpdateCommand.COMMAND_WORD:
            return new UpdateCommandParser().parse(arguments);

        //        case ClearCommand.COMMAND_WORD:
        //            return new ClearCommand();

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case FindCommand.COMMAND_WORD:
            try {
                return new FindCommandParser().parse(arguments);
            } catch (ParseException ex) {
                return new SuggestionCommandParser(commandWord, ex.getMessage()).parse(arguments);
            }

        case FindExactCommand.COMMAND_WORD:
            return new FindExactCommandParser().parse(arguments);

        default:
            return new SuggestionCommandParser(commandWord).parse(arguments);
        }
    }

}
