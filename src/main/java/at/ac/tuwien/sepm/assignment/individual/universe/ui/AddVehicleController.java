package at.ac.tuwien.sepm.assignment.individual.universe.ui;

import at.ac.tuwien.sepm.assignment.individual.universe.entities.LicenseCategory;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.universe.service.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.universe.service.VehicleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddVehicleController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final VehicleService vehicleService;
    private MainFrameController mainFrameController;

    @FXML
    public Button Exit;
    @FXML
    public Label lb_licenseplate;
    @FXML
    public Label lb_powerKW;
    @FXML
    public TextField tf_licensePlate;
    @FXML
    public TextField tf_powerKW;
    @FXML
    public TextField tf_price;
    @FXML
    public TextField tf_title;
    @FXML
    public TextField tf_buildyear;
    @FXML
    public TextField tf_description;
    @FXML
    public TextField tf_scap;
    @FXML
    public RadioButton rb_motirized;
    @FXML
    public RadioButton rb_brawn;
    @FXML
    public RadioButton rb_a;
    @FXML
    public RadioButton rb_b;
    @FXML
    public RadioButton rb_c;
    @FXML
    public ToggleGroup Group1;
    @FXML
    public ToggleGroup Group2;
    @FXML
    public ToggleGroup Group3;
    @FXML
    public ToggleGroup Group4;
    @FXML
    public ImageView Iv_Foto;
    @FXML
    public Label lb_dlicenses;
    @FXML
    public StackPane sp_addvehicle;
    private String imageUrl;


    public AddVehicleController(VehicleService vehicleService, MainFrameController mainFrameController) {
        this.vehicleService = vehicleService;
        this.mainFrameController = mainFrameController;
    }

    public void OnClickedExit(ActionEvent actionEvent) {
        LOG.info("Exit on AddVehicle.fxml clicked!");
        sp_addvehicle.getScene().getWindow().hide();
    }

    public void OnclickedSave(ActionEvent actionEvent) {

        if (tf_title.getText().isEmpty()) {
            Alert warning = new Alert(Alert.AlertType.WARNING,"Title should not be empty!");
            warning.showAndWait();
        }else if (tf_buildyear.getText().isEmpty()) {
            Alert warning = new Alert(Alert.AlertType.WARNING,"Buildyear should not be empty!");
            warning.showAndWait();
        }else if (tf_price.getText().isEmpty()) {
            Alert alert = new Alert((Alert.AlertType.WARNING));
            alert.setTitle("WARNING!");
            alert.setContentText("The Price of Vehicle can not be empty!");
            alert.showAndWait();
        }else if (!rb_motirized.isSelected() && !rb_brawn.isSelected()) {
            Alert alert = new Alert((Alert.AlertType.WARNING));
            alert.setTitle("WARNING!");
            alert.setContentText("The Power Unit of Vehicle can not be empty!");
            alert.showAndWait();
        }else if (rb_motirized.isSelected() && (!rb_a.isSelected() & !rb_b.isSelected() & !rb_c.isSelected())) {
            Alert alert = new Alert((Alert.AlertType.WARNING));
            alert.setTitle("WARNING!");
            alert.setContentText("The Driveing Licenses of Vehicle can not be empty!");
            alert.showAndWait();
        }else if (rb_motirized.isSelected() && tf_powerKW.getText().isEmpty()) {
            Alert alert = new Alert((Alert.AlertType.WARNING));
            alert.setTitle("WARNING!");
            alert.setContentText("The Power in KW of Vehicle can not be empty!");
            alert.showAndWait();
        }else if (rb_motirized.isSelected() && tf_licensePlate.getText().isEmpty()) {
            Alert alert = new Alert((Alert.AlertType.WARNING));
            alert.setTitle("WARNING!");
            alert.setContentText("The License Plate of Vehicle can not be empty!");
            alert.showAndWait();
        }else {

            if(rb_motirized.isSelected()){

                try {
                    Integer.parseInt(tf_powerKW.getText());
                } catch (Exception e) {
                    Alert alert = new Alert((Alert.AlertType.ERROR));
                    alert.setTitle("Error!");
                    alert.setContentText("The power must not contain letters / characters");
                    alert.showAndWait();
                }
            }
            try {
                Double.parseDouble(tf_price.getText());
            } catch (Exception e) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error!");
                alert.setContentText("The price may not contain any letters / characters");
                alert.showAndWait();
            }

                if(!tf_scap.getText().isEmpty()){
            try {
                Integer.parseInt(tf_scap.getText());
            } catch (Exception e) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error!");
                alert.setContentText("The seating capacity can not contain letters / characters");
                alert.showAndWait();
            }}
            try {
                Integer.parseInt(tf_buildyear.getText());
            } catch (Exception e) {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error!");
                alert.setContentText("\"The buildyear can not contain letters / characters\"");
                alert.showAndWait();
            }

            try{

                String pw = "with Brawn";
                ArrayList<LicenseCategory> lblist = new ArrayList<>();

                Vehicle vehicle = new Vehicle();

                if(rb_motirized.isSelected()){
                    pw = "Motorized";
                    if(rb_a.isSelected()) lblist.add(LicenseCategory.A);
                    if(rb_b.isSelected()) lblist.add(LicenseCategory.B);
                    if(rb_c.isSelected()) lblist.add(LicenseCategory.C);
                    vehicle.setLicenseCategories(lblist);
                    vehicle.setPower(Integer.parseInt(tf_powerKW.getText()));
                    vehicle.setLicensePlate(tf_licensePlate.getText());
                    vehicle.setPowerUnit("Motorized");
                }else{
                    lblist.add(LicenseCategory.NONE);
                    vehicle.setLicenseCategories(lblist);
                    vehicle.setPower(0);
                    vehicle.setLicensePlate(null);
                    vehicle.setPowerUnit("Brawn");
                }

                Timestamp t = vehicle.getCreateDate();

                vehicle.setTitle(tf_title.getText());
                vehicle.setBuildyear(tf_buildyear.getText() == null ? 0 : Integer.parseInt(tf_buildyear.getText()));
                vehicle.setDescription(tf_description.getText().isEmpty() ? null : tf_description.getText());
                vehicle.setSeatingCapacity(tf_scap.getText().isEmpty() ? 1 : Integer.parseInt(tf_scap.getText()));
                vehicle.setPrice(Double.parseDouble(tf_price.getText()));
                vehicle.setImageUrl(imageUrl);


                vehicleService.create(vehicle);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Vehicle successfully added!");
                alert.showAndWait();
                mainFrameController.initializeVehicleList();

            } catch (ServiceException e) {
                e.printStackTrace();
                LOG.error("Error creating the new Vehicle."+e);
            }
        }
    }

    public void OnClickedUpload(ActionEvent actionEvent) {

        LOG.info("upload a image for new Vehicle!");

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        InputStream is = null;

        if(file != null) {

            try {
                String imagePath = file.getAbsolutePath();
                is = new FileInputStream(imagePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LOG.error("there is no File" + e);
            }

            if(is!=null){
            try {
                Image image = new Image(is);

                long filesizeB = file.length();
                long filesizeKB = filesizeB / 1024;
                long filesizeMB = filesizeKB / 1024;
                if (image.getHeight() < 500 || image.getWidth() < 500) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Can't save Image! is is too small. \nIt must be at least 500x500.");
                    alert.showAndWait();
                } else if (filesizeMB >= 5) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Can't save Image! is is too big. Maximum allowed size 5MB.");
                    alert.showAndWait();
                } else {
                    Iv_Foto.setImage(image);
                    imageUrl = file.getAbsolutePath();
                    vehicleService.copyFile(file);
                }
                this.mainFrameController.initializeVehicleList();
            } catch (ServiceException e) {
                e.printStackTrace();
                LOG.error("Can't copy Image"+e);
            }
            }}
    }

    private static void configureFileChooser(
        final FileChooser fileChooser) {
        fileChooser.setTitle("Choose a photo for Vehicle");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+"/Desktop"));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickedMotorized(ActionEvent actionEvent) {

        rb_a.setVisible(true);
        rb_b.setVisible(true);
        rb_c.setVisible(true);
        lb_licenseplate.setVisible(true);
        lb_powerKW.setVisible(true);
        tf_powerKW.setVisible(true);
        tf_licensePlate.setVisible(true);
        lb_dlicenses.setVisible(true);
    }

    public void OnClickedBrawn(ActionEvent actionEvent) {
        rb_a.setVisible(false);
        rb_b.setVisible(false);
        rb_c.setVisible(false);
        lb_licenseplate.setVisible(false);
        lb_powerKW.setVisible(false);
        tf_powerKW.setVisible(false);
        tf_licensePlate.setVisible(false);
        lb_dlicenses.setVisible(false);
    }
}
