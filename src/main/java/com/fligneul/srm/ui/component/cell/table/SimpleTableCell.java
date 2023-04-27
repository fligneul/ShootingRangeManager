package com.fligneul.srm.ui.component.cell.table;

import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;

import java.util.function.Function;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * TableCell used to display a simple String representation of an object
 *
 * @param <S>
 *         type of data in the table view
 * @param <T>
 *         the object to display
 */
public class SimpleTableCell<S, T> extends TableCell<S, T> {
    private final Function<T, String> textConverter;

    /**
     * Create a table cell with text
     *
     * @param textConverter
     *         function used to convert the item to its string description
     */
    public SimpleTableCell(final Function<T, String> textConverter) {
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
            setTooltip(null);
        } else {
            setText(textConverter.apply(item));
            setTooltip(new Tooltip(textConverter.apply(item)));
        }
    }
}


