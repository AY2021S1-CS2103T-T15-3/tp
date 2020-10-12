package seedu.stock.logic.parser;

import static seedu.stock.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_SERIAL_NUMBER;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_SOURCE;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.stock.logic.commands.FindExactCommand;
import seedu.stock.logic.parser.exceptions.ParseException;
import seedu.stock.model.stock.Stock;
import seedu.stock.model.stock.predicates.LocationContainsKeywordsPredicate;
import seedu.stock.model.stock.predicates.NameContainsKeywordsPredicate;
import seedu.stock.model.stock.predicates.SerialNumberContainsKeywordsPredicate;
import seedu.stock.model.stock.predicates.SourceContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindExactCommand object
 */
public class FindExactCommandParser implements Parser<FindExactCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindExactCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_SOURCE, PREFIX_SERIAL_NUMBER, PREFIX_LOCATION);

        // Check if command format is correct and there is a prefix present
        if (!isAnyPrefixPresent(argMultimap, PREFIX_NAME, PREFIX_LOCATION, PREFIX_SOURCE, PREFIX_SERIAL_NUMBER)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindExactCommand.MESSAGE_USAGE));
        }

        List<Prefix> prefixes = CliSyntax.getAllPossiblePrefixes();
        // Check for duplicate prefixes
        for (Prefix prefix: prefixes) {
            if (argMultimap.getAllValues(prefix).size() >= 2) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindExactCommand.MESSAGE_USAGE));
            }
        }

        // Get the predicates to test to find the stocks that match
        List<Predicate<Stock>> predicatesToTest =
                parsePrefixAndKeywords(argMultimap, PREFIX_NAME, PREFIX_LOCATION, PREFIX_SOURCE, PREFIX_SERIAL_NUMBER);

        return new FindExactCommand(predicatesToTest);
    }

    /**
     * Returns true if any one of the prefixes does not contain an empty {@code Optional} value
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean isAnyPrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns a list of predicates to filter stocks based on user's search fields and terms.
     * @param argumentMultimap map of prefix to keywords entered by user
     * @param prefixes prefixes to parse
     * @return list of predicates to filter stocks
     */
    private static List<Predicate<Stock>> parsePrefixAndKeywords(ArgumentMultimap argumentMultimap,
                                                                 Prefix... prefixes) {
        return Stream.of(prefixes)
                .filter(prefix -> argumentMultimap.getValue(prefix).isPresent())
                .map(prefix -> getPredicate(prefix, argumentMultimap.getValue(prefix).get()))
                .collect(Collectors.toList());
    }

    /**
     * Returns a field predicate to test whether a {@code Stock}'s {@code field} matches or contains
     * any of the keywords given.
     * @param prefix prefix for field
     * @param keywordsToFind keywords to match with the stock's field
     * @return predicate filter stocks based on field
     */
    private static Predicate<Stock> getPredicate(Prefix prefix, String keywordsToFind) {
        final Predicate<Stock> fieldContainsKeywordsPredicate;
        String trimmedKeywordsToFind = keywordsToFind.trim();
        String[] keywords = trimmedKeywordsToFind.split("\\s+");

        switch(prefix.getPrefix()) {
        case "n/":
            fieldContainsKeywordsPredicate =
                    new NameContainsKeywordsPredicate(Arrays.asList(keywords));
            break;

        case "sn/":
            fieldContainsKeywordsPredicate =
                    new SerialNumberContainsKeywordsPredicate(Arrays.asList(keywords));
            break;

        case "s/":
            fieldContainsKeywordsPredicate =
                    new SourceContainsKeywordsPredicate(Arrays.asList(keywords));
            break;

        case "l/":
            fieldContainsKeywordsPredicate =
                    new LocationContainsKeywordsPredicate(Arrays.asList(keywords));
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + prefix);
        }

        return fieldContainsKeywordsPredicate;
    }

}
