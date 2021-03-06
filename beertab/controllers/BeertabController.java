package beertab.controllers;

import beertab.Beertab;
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
import javafx.scene.layout.Pane;
import javafx.util.converter.IntegerStringConverter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    private Pane pn_top;
    public static Pane ballpane;

    public void setTables(List<String> list) throws ParseException {

        List<String> retData = list;

        if(retData.size() != 0)
        {
            int customerId = 0;
            int table = 0;
            String customer = "";
            String beverage = "";
            int quantity = 0;
            int cost = 0;

            JSONParser parser = new JSONParser();

            // Send data for each row (JSON Formatted string)
            for(String str : retData)
            {
                System.out.println(str);
                parser.parse(str);
                Object obj = JSONValue.parse(str);
                JSONObject jsonobj = (JSONObject) obj;
                System.out.println(jsonobj);
                long lcustomerId = (long) jsonobj.get("customerId");
                long ltable = (long) jsonobj.get("table");
                customer = (String) jsonobj.get("customer");
                beverage = (String) jsonobj.get("beverage");
                long lquantity = (long) jsonobj.get("quantity");
                long lcost = (long) jsonobj.get("cost");

                // Cast long int to std int
                customerId = (int) lcustomerId;
                table = (int) ltable;
                quantity = (int) lquantity;
                cost = (int) lcost;

                CustomerTable row = new CustomerTable(customerId, table, customer, beverage, quantity, cost);
                beerTabTable.getItems().add(row);
            }
            System.out.println("Beertab Ctrl: Tables set from database");

        }
        else
        {
            System.out.println("Beertab Ctrl: No tables to set from database");
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        idCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, Integer>("customerId"));
        tableCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, Integer>("Table"));
        customerCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, String>("Customer"));
        beverageCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, String>("Beverage"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, Integer>("quantity"));
        costCol.setCellValueFactory(new PropertyValueFactory<CustomerTable, Integer>("Cost"));

        // load database table:
        try {
            setTables(Beertab.retData);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Beertab Ctrl: Unable to set tables from database");
        }


        // Making the table editable:
        beerTabTable.setEditable(true);
        idCol.setCellFactory(TextFieldTableCell.<CustomerTable, Integer>forTableColumn(new IntegerStringConverter()));
        tableCol.setCellFactory(TextFieldTableCell.<CustomerTable, Integer>forTableColumn(new IntegerStringConverter()));
        customerCol.setCellFactory(TextFieldTableCell.<CustomerTable>forTableColumn());
        beverageCol.setCellFactory(TextFieldTableCell.<CustomerTable>forTableColumn());
        quantityCol.setCellFactory(TextFieldTableCell.<CustomerTable, Integer>forTableColumn(new IntegerStringConverter()));
        costCol.setCellFactory(TextFieldTableCell.<CustomerTable, Integer>forTableColumn(new IntegerStringConverter()));

        tempCellValue = 0;

        ballpane = this.pn_top;

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
            CustomerTable row = new CustomerTable();
            beerTabTable.getItems().add(row);
        }

    }

}
