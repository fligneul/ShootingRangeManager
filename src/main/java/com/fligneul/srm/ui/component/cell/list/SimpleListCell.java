package com.fligneul.srm.ui.component.cell.list;

import javafx.scene.control.ListCell;

import java.util.function.Function;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * ListCell used to displaya simple String representation of an object
 *
 * @param <T>
 *         the object to display
 */
public class SimpleListCell<T> extends ListCell<T> {
    private final Function<T, String> textConverter;

    /**
     * Create a list cell with text
     *
     * @param textConverter
     *         function used to convert the item to its string description
     */
    public SimpleListCell(final Function<T, String> textConverter) {
        this.textConverter = textConverter;
    }

    /**
     * Update the table cell with the current item
     *
     * @param item
     *         the cell item
     * @param empty
     *         {@code true} is the cell is currently empty, {@code false} otherwise
     */
    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(EMPTY);
        } else {
            setText(textConverter.apply(item));
        }
    }
}


