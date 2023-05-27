package at.ac.tuwien.sepm.assignment.individual.universe.ui;

import at.ac.tuwien.sepm.assignment.individual.universe.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Driver;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.LicenseCategory;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.universe.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.universe.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import jdk.jfr.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class AddDriverController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    BookingService bookingService;

    @FXML
    public Button bt_save;
    @FXML
    public Label tf_title;
    @FXML
    public Label lb_id;
    @FXML
    public Label lb_cdate;
    @FXML
    public TextField tf_id;
    @FXML
    public DatePicker dp_cdate;
    public RadioButton rb_B;
    public RadioButton rb_C;
    public RadioButton rb_A;

    Booking booking;
    Vehicle vehicle;

    public AddDriverController(BookingService bookingService, BookingVehicleController bookingVehicleController, Booking booking, Vehicle vehicle) {
        this.booking=booking;
        this.vehicle=vehicle;
        this.bookingService=bookingService;
    }

    public void OnClickedSaveButton(ActionEvent actionEvent) {

        LOG.info("OnClickedSaveButton clicked!");

        if (tf_id.getText().isEmpty()) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "ID should not be empty!");
            warning.showAndWait();
        } else if (dp_cdate.getValue() == null) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "Licence creation date should not be empty!");
            warning.showAndWait();
        } else if (!rb_A.isSelected() && !rb_B.isSelected() && !rb_C.isSelected()) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "License typ should not be empty!");
            warning.showAndWait();
        } else {

            if (!tf_id.getText().isEmpty()) {
                try {
                    Integer.parseInt(tf_id.getText());
                } catch (Exception e) {
                    Alert alert = new Alert((Alert.AlertType.WARNING));
                    alert.setTitle("Warning!");
                    alert.setContentText("The License ID can not contain letters / characters");
                    alert.showAndWait();
                }
            }


            Timestamp tmp = new Timestamp(Timestamp.from(dp_cdate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());

            if(vehicle.getLicenseCategories().size() > 0 ){

                for (LicenseCategory l : vehicle.getLicenseCategories()) {
                    if(l == LicenseCategory.A || l == LicenseCategory.C){

                        Timestamp today = new Timestamp(System.currentTimeMillis());

                        if (today.getTime()<tmp.getTime()) {
                            Alert warning = new Alert(Alert.AlertType.WARNING, "Create ID of Driver License not valid!");
                            warning.showAndWait();
                            return;
                        }else if(rb_A.isSelected() || rb_C.isSelected()){
                            tmp = AddTime(tmp, 3);
                            if(today.getTime() < tmp.getTime()){
                                Alert alert = new Alert((Alert.AlertType.WARNING));
                                alert.setTitle("Warning!");
                                alert.setContentText("to get this Vehicle you have to get 3 year old Driver License");
                                alert.showAndWait();
                                return;
                            }
                        }
                    }
                }

            }

            Driver driver = new Driver();
            driver.setDid(Integer.parseInt(tf_id.getText()));
            driver.setLdate(tmp);

                booking.setBookedVehiclewithDriver(vehicle.getId(), driver);

            LOG.info("Exit on AddDriverController.fxml clicked!");
            bt_save.getScene().getWindow().hide();

        }
        LOG.info("Driver succesfully added!");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Driver Successfully added to selected Vehicle");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LOG.info("initialize Details add Driver!");

        tf_id.setText(booking.getBookedVehiclewithDriver().get(vehicle.getId()) != null ? String.valueOf(booking.getBookedVehiclewithDriver().get(vehicle.getId()).getDid()) : "");

        if(vehicle.getLicenseCategories().size() > 0 ){

            for (LicenseCategory l : vehicle.getLicenseCategories()) {
                if(l == LicenseCategory.A){
                    rb_A.setVisible(true);
                }else if(l == LicenseCategory.B){
                    rb_B.setVisible(true);
                }else if(l == LicenseCategory.C){
                    rb_C.setVisible(true);}
            }
        }
        if(booking != null && booking.getBid() != 0 && booking.getBookedVehiclewithDriver().get(vehicle.getId()) != null) {
            dp_cdate.setValue(booking.getDate(booking.getBookedVehiclewithDriver().get(vehicle.getId()).getLdate()));
        }
        // TODO: 02.04.18 fix here for edit Driver
        //dp_cdate.setValue(booking.getBookedVehiclewithDriver().get(vehicle.getId()).getLdate() != null ? LocalDate.ofEpochDay(booking.getBookedVehiclewithDriver().get(vehicle.getId()).getLdate().getTime()) : null);

    }

    private Timestamp AddTime(Timestamp timestamp,int year) {

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp.getTime());

            //cal.add(Calendar.MINUTE, minute);
            //cal.add(Calendar.HOUR, hour);
            cal.add(Calendar.YEAR,year);
            timestamp = new Timestamp(cal.getTime().getTime());

            return timestamp;

    }


}
