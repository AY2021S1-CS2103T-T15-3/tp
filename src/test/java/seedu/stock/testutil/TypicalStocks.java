package seedu.stock.testutil;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.stock.model.SerialNumberSetsBook;
import seedu.stock.model.StockBook;
import seedu.stock.model.stock.AccumulatedQuantity;
import seedu.stock.model.stock.SerialNumber;
import seedu.stock.model.stock.SerialNumberSet;
import seedu.stock.model.stock.Stock;

/**
 * A utility class containing a list of {@code Stock} objects to be used in tests.
 */
public class TypicalStocks {
    public static final Stock APPLE = new StockBuilder().withName("Apple Juice")
            .withSerialNumber("Ntuc1").withSource("Ntuc")
            .withQuantity("2000").withLocation("Fruit Section, Subsection C")
            .build();
    public static final Stock BANANA = new StockBuilder().withName("Banana Cake")
            .withSerialNumber("Fairprice1").withSource("Fairprice")
            .withQuantity("2103").withLocation("Fruits section, Subsection B")
            .build();
    public static final Stock PINEAPPLE = new StockBuilder().withName("Pineapple Tart")
            .withSerialNumber("BengawanSolo1").withSource("BengawanSolo")
            .withQuantity("123").withLocation("Tart Section, Subsection A")
            .build();
    public static final SerialNumber SERIAL_NUMBER_FIRST_STOCK = APPLE.getSerialNumber();
    public static final SerialNumber SERIAL_NUMBER_SECOND_STOCK = BANANA.getSerialNumber();

    private TypicalStocks() {} // prevents instantiation

    /**
     * Returns an {@code StockBook} with all the typical stocks.
     */
    public static StockBook getTypicalStockBook() {
        StockBook inventory = new StockBook();
        for (Stock stock : getTypicalStocks()) {
            inventory.addStock(stock);
        }
        return inventory;
    }

    public static List<Stock> getTypicalStocks() {
        return new ArrayList<>(Arrays.asList(APPLE, BANANA));
    }

    public static SerialNumberSetsBook getTypicalSerialNumberSetsBook() {
        SerialNumberSetsBook serialNumbers = new SerialNumberSetsBook();
        List<SerialNumberSet> serialNumberSets = new ArrayList<>();
        for (Stock stock: getTypicalStocks()) {
            serialNumberSets.add(new SerialNumberSet(stock.getSource(), new AccumulatedQuantity("1")));
        }
        serialNumbers.setSerialNumberSets(serialNumberSets);
        return serialNumbers;
    }

    /**
     * Displays the list of stocks in a clearer view, with each subsequent stock moved to the next line.
     *
     * @param stockList The list of stocks to convert to String.
     * @return The String depicting each stock in the list.
     */
    public static String stocksAsString(List<Stock> stockList) {
        String stocksAsString = "";
        for (int i = 0; i < stockList.size(); i++) {
            stocksAsString += "\n" + stockList.get(i).toString();
        }
        return stockList.size() == 0 ? "No stocks deleted" : stocksAsString;
    }

    /**
     * Displays the list of serial numbers in a clearer view, with each subsequent serial number moved
     * to the next line.
     *
     * @param serialNumberList The list of serial numbers to convert to String.
     * @return The String depicting each serial number in the list.
     */
    public static String serialNumberListAsString(List<SerialNumber> serialNumberList) {
        String serialNumbersAsString = "";
        for (int i = 0; i < serialNumberList.size(); i++) {
            serialNumbersAsString += "\n" + serialNumberList.get(i).toString();
        }
        return serialNumbersAsString;
    }
}
