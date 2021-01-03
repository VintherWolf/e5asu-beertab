package beertab.controllers;

import beertab.entities.CustomerTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class BeertabController implements Initializable  {

    private static final int NUMBER_OF_TABLES = 24;
    // BeerTabTable:
    @FXML
    private TableView<CustomerTable> beerTabTable;

    @FXML
    private TableColumn<CustomerTable, Integer> idCol;

    @FXML
    private TableColumn<CustomerTable, Integer> tableCol;

    @FXML
    private TableColumn<CustomerTable, String> customerCol;

    @FXML
    private TableColumn<CustomerTable, String> beverageCol;

    @FXML
    private TableColumn<CustomerTable, Integer> quantityCol;

    @FXML
    private TableColumn<CustomerTable, Integer> costCol;
    // End of BeerTabTable

    private int customerId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.customerId = 1;
        // Set up columns

        idCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, Integer>("customerId"));
        tableCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, Integer>("Table"));
        customerCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, String>("Customer"));
        beverageCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, String>("Beverage"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, Integer>("quantity"));
        costCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, Integer>("Cost"));

        // load database table:




        CustomerTable row = new CustomerTable(50);
        beerTabTable.getItems().add(row);

        // Making the table editable:
        beerTabTable.setEditable(true);
        idCol.setCellFactory(TextFieldTableCell.<CustomerTable, Integer>forTableColumn(new IntegerStringConverter()));
        tableCol.setCellFactory(TextFieldTableCell.<CustomerTable, Integer>forTableColumn(new IntegerStringConverter()));
        customerCol.setCellFactory(TextFieldTableCell.<CustomerTable>forTableColumn());
        beverageCol.setCellFactory(TextFieldTableCell.<CustomerTable>forTableColumn());
        quantityCol.setCellFactory(TextFieldTableCell.<CustomerTable, Integer>forTableColumn(new IntegerStringConverter()));
        costCol.setCellFactory(TextFieldTableCell.<CustomerTable, Integer>forTableColumn(new IntegerStringConverter()));

        tempCellValue = 0;

    }

    public ObservableList<CustomerTable> getCustomer() {

        ObservableList<CustomerTable> CustomerTable = FXCollections.observableArrayList();
        CustomerTable.add(new CustomerTable());
        return CustomerTable;
    }

    @FXML
    private Slider slider;

    private int tempCellValue;

    @FXML
    public void adjustQuantity(MouseEvent event) {
        /**
         * @brief This method will increment og decrement Qty value on click
         */
        CustomerTable customerSelected = beerTabTable.getSelectionModel().getSelectedItem();

        if (customerSelected != null) {

            tempCellValue = 0;
            tempCellValue = customerSelected.getQuantity();

            // Button 1, Left Button
            if (event.isPrimaryButtonDown()) {
                tempCellValue++;
            }

            // Button 2, Right Button
            if (event.isSecondaryButtonDown()) {
                if (tempCellValue > 0) {
                    tempCellValue--;
                } else {
                    tempCellValue = 0;
                }
            }
            customerSelected.setQuantity(tempCellValue);
            beerTabTable.refresh();
        }

    }



    // Slider to Delete the selected row
    @FXML
    public void deleteRow(MouseEvent event) {

        if (slider.getValue() == 100)
        {
            beerTabTable.getItems().removeAll(beerTabTable.getSelectionModel().getSelectedItem());
        }
        slider.setValue(0);
    }


    // Edit Table Cell
    private int tableNumber = -1;
    @FXML
    public void newTable(TableColumn.CellEditEvent<CustomerTable, Integer> cellEdited) {

        CustomerTable selectedTable = beerTabTable.getSelectionModel().getSelectedItem();
        try {

            tableNumber = cellEdited.getNewValue();

            if (tableNumber > 0 && tableNumber <= NUMBER_OF_TABLES) {
                selectedTable.setTable(tableNumber);
            } else {
                selectedTable.setTable(-1);
            }
        }
            catch (Exception e) {

            e.printStackTrace();
            selectedTable.setTable(0);
            }
        beerTabTable.refresh();
    }

    // Edit Customer Cell
    @FXML
    public void newCustomer(TableColumn.CellEditEvent<CustomerTable, String> cellEdited)
    {
        CustomerTable selectedTable = beerTabTable.getSelectionModel().getSelectedItem();
        selectedTable.setCustomer(cellEdited.getNewValue());
    }

    // Edit Beverage Cell
    @FXML
    public void addBeverage(TableColumn.CellEditEvent<CustomerTable, String> CellEdited)
    {

        CustomerTable selectedTable = beerTabTable.getSelectionModel().getSelectedItem();
        selectedTable.setBeverage(CellEdited.getNewValue());
    }

    @FXML
    // Add Row with empty fields to add new customer
    public void addRow(MouseEvent event)
    {
        CustomerTable customerSelected = beerTabTable.getSelectionModel().getSelectedItem();

        if (event.isSecondaryButtonDown())
        {
            beerTabTable.getSelectionModel().clearSelection();
        }

        if (customerSelected == null && event.isPrimaryButtonDown())
        {
            CustomerTable row = new CustomerTable(customerId++);
            beerTabTable.getItems().add(row);
        }

    }

}
