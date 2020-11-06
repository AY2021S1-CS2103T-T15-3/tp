package seedu.stock.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.stock.commons.core.Messages.MESSAGE_DUPLICATE_HEADER_FIELD;
import static seedu.stock.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_SERIAL_NUMBER;

import java.util.stream.Stream;

import seedu.stock.logic.commands.StockViewCommand;
import seedu.stock.logic.parser.exceptions.ParseException;
import seedu.stock.model.stock.SerialNumber;

public class StockViewCommandParser implements Parser<StockViewCommand> {

    private static final Prefix[] allPossiblePrefixes = CliSyntax.getAllPossiblePrefixesAsArray();
    private static final Prefix[] validPrefixesForNoteView = { PREFIX_SERIAL_NUMBER };

    @Override
    public StockViewCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, allPossiblePrefixes);

        // Check if command format is correct
        if (!areAllPrefixesPresent(argMultimap, validPrefixesForNoteView)
                || ParserUtil.isInvalidPrefixPresent(argMultimap, validPrefixesForNoteView)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    StockViewCommand.MESSAGE_USAGE));
        }

        if (isDuplicatePrefixPresent(argMultimap, validPrefixesForNoteView)) {
            throw new ParseException(String.format(MESSAGE_DUPLICATE_HEADER_FIELD,
                    StockViewCommand.MESSAGE_USAGE));
        }

        String serialNumberInput = argMultimap.getValue(PREFIX_SERIAL_NUMBER).get();
        SerialNumber serialNumber = ParserUtil.parseSerialNumber(serialNumberInput);

        return new StockViewCommand(serialNumber);
    }

    /**
     * Returns true if all prefixes specified does not contain an empty {@code Optional} value
     * in the given {@code ArgumentMultimap}.
     * @param argumentMultimap map of prefix to keywords entered by user
     * @param prefixes prefixes to parse
     * @return boolean true if all prefixes specified is present
     */
    private static boolean areAllPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns true if duplicate prefixes are present when parsing command.
     * @param argumentMultimap map of prefix to keywords entered by user
     * @param prefixes prefixes to parse
     * @return boolean true if duplicate prefix is present
     */
    private static boolean isDuplicatePrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {

        // Check for duplicate prefixes
        for (Prefix prefix: prefixes) {
            if (argumentMultimap.getAllValues(prefix).size() >= 2) {
                return true;
            }
        }
        return false;
    }
}
