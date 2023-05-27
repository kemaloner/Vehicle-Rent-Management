package at.ac.tuwien.sepm.assignment.individual.universe.ui;


import at.ac.tuwien.sepm.assignment.individual.universe.entities.LicenseCategory;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.universe.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.universe.service.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.universe.service.VehicleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditVehicleController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final VehicleService vehicleService;
    private final BookingService bookingService;
    private final MainFrameController mainFrameController;
    public ImageView iv_foto;
    private Vehicle vehicle;
    private String imageUrl;

    @FXML
    public Button bt_exit;
    @FXML
    public TextField tf_title;
    @FXML
    public TextField tf_scap;
    @FXML
    public Button bt_savechanges;
    @FXML
    public TextField tf_buildyear;
    @FXML
    public TextField tf_price;
    @FXML
    public TextField tf_description;
    @FXML
    public RadioButton vFmotirized;
    @FXML
    public Label lb_createday;
    @FXML
    public Label lb_lastupdate;
    @FXML
    public Button bt_deleteVehicle;
    @FXML
    public Button bt_bookitnow;
    @FXML
    public RadioButton rb_A;
    @FXML
    public RadioButton rb_B;
    @FXML
    public RadioButton rb_C;
    @FXML
    public RadioButton rb_Brwan;
    @FXML
    public Label lb_drivinglicense;
    @FXML
    public TextField tf_power;
    @FXML
    public TextField tf_licenseplate;
    @FXML
    public Label lb_power;
    @FXML
    public Label lb_licenseplate;
    @FXML
    public RadioButton rb_motorizedid;
    @FXML
    public RadioButton rb_Brwanid;

    public EditVehicleController(VehicleService vehicleService, MainFrameController mainFrameController, Vehicle vehicle, BookingService bookingService) {
        this.vehicleService = vehicleService;
        this.mainFrameController = mainFrameController;
        this.vehicle = vehicle;
        this.bookingService =bookingService;
    }

    public void OnClickedSaveChanges(ActionEvent actionEvent) {


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
        }else if (rb_motorizedid.isSelected() && (!rb_A.isSelected() & !rb_B.isSelected() & !rb_C.isSelected())) {
            Alert alert = new Alert((Alert.AlertType.WARNING));
            alert.setTitle("WARNING!");
            alert.setContentText("The Driveing Licenses of Vehicle can not be empty!");
            alert.showAndWait();
        }else if (rb_motorizedid.isSelected() && tf_power.getText().isEmpty() && tf_licenseplate.getText().isEmpty()) {
            Alert alert = new Alert((Alert.AlertType.WARNING));
            alert.setTitle("WARNING!");
            alert.setContentText("The Power in KW and The License Plate of Vehicle can not be empty!");
            alert.showAndWait();
        }else {

            if(rb_motorizedid.isSelected()){

                try {
                    Integer.parseInt(tf_power.getText());
                } catch (Exception e) {
                    Alert alert = new Alert((Alert.AlertType.WARNING));
                    alert.setTitle("Warning!");
                    alert.setContentText("The power must not contain letters / characters");
                    alert.showAndWait();
                    return;
                }
            }

            try {
                Double.parseDouble(tf_price.getText());
            } catch (Exception e) {
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setTitle("Warning!");
                alert.setContentText("The price may not contain any letters / characters");
                alert.showAndWait();
                return;
            }

            if(!tf_scap.getText().isEmpty()){
                try {
                    Integer.parseInt(tf_scap.getText());
                } catch (Exception e) {
                    Alert alert = new Alert((Alert.AlertType.WARNING));
                    alert.setTitle("Warning!");
                    alert.setContentText("The seating capacity can not contain letters / characters");
                    alert.showAndWait();
                    return;
                }}
            try {
                Integer.parseInt(tf_buildyear.getText());
            } catch (Exception e) {
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setTitle("Warning!");
                alert.setContentText("The buildyear can not contain letters / characters");
                alert.showAndWait();
                return;
            }

            try{

                String pw = "Brawn";
                ArrayList<LicenseCategory> lblist = new ArrayList<LicenseCategory>();


                if(rb_motorizedid.isSelected()){
                    pw = "Motorized";
                    if(rb_A.isSelected()) lblist.add(LicenseCategory.A);
                    if(rb_B.isSelected()) lblist.add(LicenseCategory.B);
                    if(rb_C.isSelected()) lblist.add(LicenseCategory.C);
                    vehicle.setLicenseCategories(lblist);
                    vehicle.setPower(tf_power.getText() == null ? 0 : Integer.parseInt(tf_power.getText()));
                    vehicle.setLicensePlate(tf_licenseplate.getText() == null ? "" : tf_licenseplate.getText());
                    vehicle.setPowerUnit("Motorized");
                }else{
                    lblist.add(LicenseCategory.NONE);
                    vehicle.setLicenseCategories(lblist);
                    vehicle.setPower(0);
                    vehicle.setLicensePlate(null);
                    vehicle.setPowerUnit(pw);
                }

                vehicle.setTitle(tf_title.getText());
                vehicle.setBuildyear(Integer.parseInt(tf_buildyear.getText()));
                vehicle.setDescription(tf_description.getText().isEmpty() ? null : tf_description.getText());
                vehicle.setSeatingCapacity(tf_scap.getText().isEmpty() ? 1 : Integer.parseInt(tf_scap.getText()));
                vehicle.setPrice(Double.parseDouble(tf_price.getText()));
                vehicle.setImageUrl(imageUrl);


                vehicleService.update(vehicle);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Vehicle successfully updated!");
                alert.showAndWait();
                mainFrameController.initializeVehicleList();
            } catch (ServiceException e) {
                e.printStackTrace();
                LOG.error("Error while Updateing the new Vehicle."+e);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error while Updateing the new Vehicle.");
            }
        }
    }

    public void OnClickedUplaodFoto(ActionEvent actionEvent) {

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
                        iv_foto.setImage(image);
                        imageUrl = file.getAbsolutePath();
                        vehicleService.copyFile(file);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "New Foto successfully updated");
                        alert.showAndWait();
                    }
                } catch (ServiceException e) {
                    e.printStackTrace();
                    LOG.error("Can't copy Image" + e);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Can't copy Image");
                    alert.showAndWait();
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

    public void OnClickedBook(ActionEvent actionEvent) {

        LOG.info("on Clicked Book it now in EditVehicleController!");

        ArrayList<Vehicle> listv = new ArrayList<Vehicle>();
        listv.add(vehicle);
        BookingVehicleController bookingVehicleController = new BookingVehicleController(listv, bookingService , vehicleService);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BookingVehicle.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(bookingVehicleController) ? bookingVehicleController : null);

        Parent root= null;
        try {
            root = fxmlLoader.load();
            LOG.info("BookingVehicle succesfully opened");

            Scene scene = new Scene(root);
            Stage stage=new Stage();
            stage.setTitle("New Booking");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Can't open a new Frame for new Booking", e);
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LOG.info("initialize Details Frame!");

        lb_createday.setText(vehicle.getCreateDate().toString());
        lb_lastupdate.setText(vehicle.getUpdatedate() != null ? vehicle.getUpdatedate().toString() : "");
        tf_title.setText(vehicle.getTitle());
        tf_description.setText(vehicle.getDescription()!=null ? vehicle.getDescription() : "");
        tf_scap.setText(String.valueOf(vehicle.getSeatingCapacity()));
        tf_buildyear.setText(String.valueOf(vehicle.getBuildyear()));
        tf_price.setText(String.valueOf(vehicle.getPrice()));

        InputStream is = null;
        try {
            is = new FileInputStream(vehicle.getImageUrl() == null ? "src/main/resources/img/default.png" : vehicle.getImageUrl());
            iv_foto.setImage(new Image(is));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOG.warn("Image url could not fould!");
        }

        if(vehicle.getPowerUnit().equals("Motorized")){
            rb_motorizedid.setSelected(true);

            for (LicenseCategory i: vehicle.getLicenseCategories()) {
                if(i == LicenseCategory.A){rb_A.setSelected(true);}else if(i == LicenseCategory.B){rb_B.setSelected(true);}else if(i == LicenseCategory.C){rb_C.setSelected(true);}
            }

            tf_licenseplate.setText(vehicle.getLicensePlate());
            tf_power.setText(String.valueOf(vehicle.getPower()));

        }else{
            rb_Brwanid.setSelected(true);
            setButtons(false);
        }

    }

    public void OnClickedExit(ActionEvent actionEvent) {
        LOG.info("Exit on AddVehicle.fxml clicked!");
        exit();
    }

    public void onClickedDeleteVehicle(ActionEvent actionEvent) {
        LOG.info("Delete Vehicle button Clicked!");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the vehicle?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            try {
                    vehicleService.delete(vehicle);
                    exit();
                } catch (ServiceException e) {
                    e.printStackTrace();
            }
        }
    }

    public void rb_motorized(ActionEvent actionEvent) {
        setButtons(true);
    }

    public void rb_brawn(ActionEvent actionEvent) {
        setButtons(false);
    }

    private void setButtons(boolean setbuttons){
        rb_A.setVisible(setbuttons);
        rb_B.setVisible(setbuttons);
        rb_C.setVisible(setbuttons);
        lb_licenseplate.setVisible(setbuttons);
        lb_power.setVisible(setbuttons);
        tf_licenseplate.setVisible(setbuttons);
        tf_power.setVisible(setbuttons);
        lb_drivinglicense.setVisible(setbuttons);
    }

    public void exit (){
        bt_exit.getScene().getWindow().hide();
    }

}
