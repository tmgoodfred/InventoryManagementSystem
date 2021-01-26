package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.InhousePart;
import Model.OutsourcedPart;
import Model.Inventory;
import Model.Part;

/**
 * FXML Controller class
 *
 * @author tylergoodfred
 */
public class AddPartsController implements Initializable {

    @FXML
    private Label DynAddPartLabel;
    @FXML
    private TextField AddPartsIDField;
    @FXML
    private TextField AddPartsNameField;
    @FXML
    private TextField AddPartsInvField;
    @FXML
    private TextField AddPartsPriceField;
    @FXML
    private TextField AddPartsMinField;
    @FXML
    private TextField AddPartsDynField;
    @FXML
    private TextField AddPartsMaxField;

    private boolean isOutsourced;
    private String exceptionMessage = new String();
    private int partID;

    @FXML
    void AddPartsInHouseRadio(ActionEvent event) {      //if the part is coming from our company
        isOutsourced = false;
        DynAddPartLabel.setText("Machine ID");          //allows you to choose which machine in our company makes it
    }

    @FXML
    void AddPartsOutsourcedRadio(ActionEvent event) {   //if the part is coming from a different company
        isOutsourced = true;
        DynAddPartLabel.setText("Company Name");        //adds the option to get a company name 
    }

    @FXML
    void AddPartsSaveClicked(ActionEvent event) throws IOException {    //saves the selected part and all the data fields
        String partName = AddPartsNameField.getText();
        String partInv = AddPartsInvField.getText();
        String partPrice = AddPartsPriceField.getText();
        String partMin = AddPartsMinField.getText();
        String partMax = AddPartsMaxField.getText();
        String partDyn = AddPartsDynField.getText();

        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);    //error if the data is not input correctly
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error Adding Part");
                alert.setHeaderText("Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                if (isOutsourced == false) {        //allows the exception to throw if the part is in house, needed because in house parts won't have a company name
                    InhousePart iPart = new InhousePart();

                    iPart.setPartID(partID);
                    iPart.setPartName(partName);
                    iPart.setPartPrice(Double.parseDouble(partPrice));
                    iPart.setPartInStock(Integer.parseInt(partInv));
                    iPart.setPartMin(Integer.parseInt(partMin));
                    iPart.setPartMax(Integer.parseInt(partMax));
                    iPart.setPartMachineID(Integer.parseInt(partDyn));
                    Inventory.addPart(iPart);
                } else {                            //allows the exception to be thrown if the part is from a different company since it won't have a machine ID
                    OutsourcedPart oPart = new OutsourcedPart();

                    oPart.setPartID(partID);
                    oPart.setPartName(partName);
                    oPart.setPartPrice(Double.parseDouble(partPrice));
                    oPart.setPartInStock(Integer.parseInt(partInv));
                    oPart.setPartMin(Integer.parseInt(partMin));
                    oPart.setPartMax(Integer.parseInt(partMax));
                    oPart.setPartCompanyName(partDyn);
                    Inventory.addPart(oPart);
                }

                Parent partsSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(partsSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {     //will dissallow blank fields
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error Adding Part");
            alert.setHeaderText("Error");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    void AddPartsCancelClicked(ActionEvent event) throws IOException {      //confirms delete upon cancellation of adding part

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to cancel adding part " + AddPartsNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent partsCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(partsCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("Cancel has been clicked.");
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.getPartIDCount();
        AddPartsIDField.setText("AUTO GEN: " + partID);
    }
}
