package at.ac.tuwien.sepm.assignment.individual.universe.ui;

import at.ac.tuwien.sepm.assignment.individual.universe.entities.*;
import at.ac.tuwien.sepm.assignment.individual.universe.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.universe.service.BookingServiceImp;
import at.ac.tuwien.sepm.assignment.individual.universe.service.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.universe.service.VehicleService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.validator.CreditCardValidator;
import org.iban4j.Iban;
import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;

public class BookingVehicleController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BookingService bookingService;
    private final VehicleService vehicleService;

    @FXML
    public Label tf_header;
    @FXML
    public TextField tf_title;
    @FXML
    public TextField tf_pay;
    @FXML
    public TableView<Vehicle> tv_vehiclelist;
    @FXML
    public MenuButton mb_pay;
    @FXML
    public DatePicker dp_datefrom;
    @FXML
    public TextField tf_hourfrom;
    @FXML
    public DatePicker dp_dateto;
    @FXML
    public TextField tf_hourto;
    @FXML
    public Button bt_exit;
    @FXML
    public Button bt_save;
    @FXML
    public Button bt_cancelbooking;
    @FXML
    public Button bt_adddriver;
    @FXML
    public TableColumn<Vehicle,String> tc_title;
    @FXML
    public TableColumn<Vehicle,String> tc_license;
    @FXML
    public TableColumn<Vehicle,Double> tc_price;
    @FXML
    public Label tf_sum;
    @FXML
    public Label lb_status;
    @FXML
    public RadioButton rb_crcard;
    @FXML
    public RadioButton rb_iban;
    @FXML
    public TableView<Vehicle> tv_allvehiclelist;
    @FXML
    public TableColumn<Vehicle,String> tc_allModel;
    @FXML
    public TableColumn<Vehicle,LicenseCategory> tc_Alldlicenses;
    @FXML
    public TableColumn<Vehicle, String> tc_allpunit;
    @FXML
    public TableColumn<Vehicle, Double> tc_allprice;
    @FXML
    public Button bt_serrvehicles;
    @FXML
    public Label lb_from;
    @FXML
    public Label lb_to;

    private List<Vehicle> allvehiclelisttobook = new ArrayList<>();

    private ArrayList<Vehicle> alllist;
    private Booking booking = new Booking();

    BookingVehicleController(ArrayList<Vehicle> alllist, BookingService bookingService, VehicleService vehicleService) {
        this.bookingService = bookingService;
        this.alllist = alllist;
        this.vehicleService = vehicleService;
    }

    BookingVehicleController(Booking booking, BookingService bookingService, VehicleService vehicleService) {
        this.bookingService = bookingService;
        this.booking = booking;
        this.vehicleService = vehicleService;
    }

    public void OnClickedSave(ActionEvent actionEvent) {

        if (tf_title.getText().isEmpty()) {
            Alert warning = new Alert(Alert.AlertType.WARNING,"Title should not be empty!");
            warning.showAndWait();
        }else if (tf_pay.getText().isEmpty()) {
            Alert warning = new Alert(Alert.AlertType.WARNING,"Paying Methods should not be empty!");
            warning.showAndWait();
        }else if(!rb_crcard.isSelected() && !rb_iban.isSelected()) {
            Alert warning = new Alert(Alert.AlertType.WARNING,"Paying Methods should not be empty!");
            warning.showAndWait();
        }else if(dp_datefrom.getValue()==null || dp_dateto.getValue() == null || tf_hourfrom.getText().isEmpty() || tf_hourto.getText().isEmpty()) {
            Alert warning = new Alert(Alert.AlertType.WARNING,"Booking Period should not be empty!");
            warning.showAndWait();
        }else if(dp_dateto.getValue().isBefore(dp_datefrom.getValue())) {
            Alert warning = new Alert(Alert.AlertType.WARNING,"to schould be after from");
            warning.showAndWait();
        }else {

            if(rb_crcard.isSelected()){
                CreditCardValidator vs = new CreditCardValidator();
                if (vs.isValid(tf_pay.getText())) {
                    booking.setCnumber(tf_pay.getText());
                } else {
                    Alert alert = new Alert((Alert.AlertType.WARNING));
                    alert.setContentText("The Credit Card number is not Valid");
                    alert.showAndWait();
                    return;
                }
            }else if(rb_iban.isSelected()){

                try {
                    String s = tf_pay.getText();
                    IbanUtil.validate(s);
                    booking.setIban(tf_pay.getText());
                } catch (IbanFormatException e) {
                    Alert alert = new Alert((Alert.AlertType.WARNING));
                    alert.setContentText("The IBAN number is not Valid");
                    alert.showAndWait();
                    return;
                }

            }

            String frombs = tf_hourfrom.getText().substring(3,5);
            String tobs = tf_hourto.getText().substring(3,5);

            try {
                String froma = tf_hourfrom.getText().substring(0,2);
                String toa = tf_hourto.getText().substring(0,2);

                Integer.parseInt(froma);
                Integer.parseInt(toa);
                Integer.parseInt(tobs);
                Integer.parseInt(frombs);

            } catch (Exception e) {
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setTitle("Warning!");
                alert.setContentText("Hour Form is not Valid! (Valid form hh:mm) ");
                alert.showAndWait();
            }


            if(booking.getBid() == 0) {
                int i = alllist.size();

                for (Vehicle v : alllist) {

                    if (v.getLicenseCategories().size() == 0 || v.getLicenseCategories().get(0) == LicenseCategory.NONE) {
                        i--;
                    }
                }

                if (booking.getBookedVehiclewithDriver().size() != i) {
                    Alert warning = new Alert(Alert.AlertType.WARNING, "Vehicle which need Driver License need a Driver. Please add Driver for these Vehicles");
                    warning.showAndWait();
                    return;
                }
            }

            try{
                booking.setCname(tf_title.getText());
                Timestamp from = new Timestamp(Timestamp.from(dp_datefrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
                Timestamp to = new Timestamp(Timestamp.from(dp_dateto.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());

                int froma = Integer.parseInt(tf_hourfrom.getText().substring(0,2));
                int fromb = Integer.parseInt(frombs);

                int toa = Integer.parseInt(tf_hourto.getText().substring(0,2));
                int tob = Integer.parseInt(tobs);

                if(froma>23 || froma<0 || toa>23 || toa<0 || fromb> 59 || fromb<0 || tob>59 || tob<0){
                    Alert alert = new Alert((Alert.AlertType.WARNING));
                    alert.setContentText("Please set time between: 00:00 - 23:59");
                    alert.showAndWait();
                    return;
                }

                from = AddTime(from,froma*60+fromb);
                to = AddTime(to,toa*60+tob);

                long l = to.getTime() - from.getTime();
                l = l /(60*60*1000);
                int hour = (int) l;
                if(fromb!=tob){hour++;}
                Double preis = 0.0;

                for (Vehicle v : alllist) {
                    preis += v.getPrice()*hour;
                }

                tf_sum.setText(String.valueOf(preis));

                booking.setStartDate(from);
                booking.setEndDate(to);
                booking.setStatus(Status.open);
                booking.setPrice(preis);
                booking.setVehicles(alllist);

                ArrayList<Integer> vehiclesid = new ArrayList<>();
                for (Vehicle v : alllist){
                    vehiclesid.add(v.getId());
                }

                booking.setVehiclesid(vehiclesid);

                if(AddTime(booking.getStartDate(),1440).getTime() >= new Timestamp(System.currentTimeMillis()).getTime()){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Add this Booking. Booking can't be canceled! \n"+"Total Sum: €"+booking.getPrice());
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get() == ButtonType.CANCEL){
                        return;
                    }
                }


                if(booking.getBid() == 0){
                bookingService.create(booking);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Booking successfully created!");
                    alert.showAndWait();}else{
                    bookingService.update(booking);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Booking successfully updated!");
                    alert.showAndWait();
                }

            } catch (ServiceException e) {
                e.printStackTrace();
                LOG.error("Error creating the new Booking."+e);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Error creating the new Booking");
                alert.showAndWait();
            }
        }
    }

    public void OnClickedExit(ActionEvent actionEvent) {
        LOG.info("Exit on AddVehicle.fxml clicked!");
        bt_exit.getScene().getWindow().hide();
    }

    public void OnClickedAddDriverInfo(ActionEvent actionEvent) {

        LOG.info("on Clicked Add new Driver!");

        if(tv_vehiclelist.getSelectionModel().getSelectedItems().size() > 1){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please select only one Vehicle to edit or add Driver!");
            alert.showAndWait();
            return;
        }

        Vehicle vehicle = (Vehicle) tv_vehiclelist.getSelectionModel().getSelectedItem();
        if (vehicle == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please select!");
            alert.setContentText("Please select a Driver to edit or add Driver!");
            alert.showAndWait();
            return;
        }else if (vehicle.getLicenseCategories().size()==0 || vehicle.getLicenseCategories().get(0) == LicenseCategory.NONE) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText(vehicle.getTitle()+ " no need a Driver");
            alert.showAndWait();
            return;
        }

        AddDriverController addDriverController= new AddDriverController(bookingService,this,booking,vehicle);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddDriver.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(addDriverController) ? addDriverController : null);

        Parent root= null;
        try {
            root = fxmlLoader.load();
            LOG.info("AddDriverFrame succesfully opened");

            Scene scene = new Scene(root);
            Stage stage=new Stage();
            stage.setTitle("New Driver");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Can't open a new Frame for Add Driver", e);
        }

    }

    public void OnClickedCancelBooking(ActionEvent actionEvent) throws ServiceException {

        try {
            bookingService.cancel(booking);
            Alert alert=new Alert(Alert.AlertType.ERROR,"Booking succesfully Canceled", ButtonType.OK);
            alert.showAndWait();
            bt_cancelbooking.getScene().getWindow().hide();
        } catch (ServiceException e) {
            e.printStackTrace();
            Alert alert=new Alert(Alert.AlertType.ERROR,"Can't Cancel this booking. It can be already canceled or completed.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lb_status.setText(booking.getStatus() != null ? booking.getStatus().toString() : "open");
        tf_title.setText(booking.getCname() != null ? booking.getCname() : "");
        if(booking.getIban() == null){
            rb_crcard.setSelected(true);
            tf_pay.setText(String.valueOf(booking.getCnumber()));
        }
        if(booking.getCnumber() == null || booking.getCnumber().isEmpty()){
            rb_iban.setSelected(true);
            tf_pay.setText(booking.getIban());
        }
        tf_sum.setText(String.valueOf(booking.getPrice() == null ? "0" : booking.getPrice()));
        if(booking.getBid() != 0){
            bt_cancelbooking.setVisible(true);
            if(alllist == null){
            alllist = booking.getVehicles();}
            dp_datefrom.setValue(booking.getDate(booking.getStartDate()));
            dp_dateto.setValue(booking.getDate(booking.getEndDate()));
            tf_hourfrom.setText(booking.getHour(booking.getStartDate()));
            tf_hourto.setText(booking.getHour(booking.getEndDate()));
        }

        initializeVehicleList();
        initializeAllVehicleList();

    }

    private void initializeVehicleList(){
        LOG.debug("Initialize already booked Vehicle List!");

        tc_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tc_license.setCellValueFactory(new PropertyValueFactory<>("print"));
        tc_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(alllist/*==null ? booking.getVehicles() : alllist*/);
            tv_vehiclelist.setItems(vehicles);
            LOG.info("Vehicle list succesfully created!");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Can't connect to database.",e.getMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,"Can't connect to database. No Vehicle found.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void OnMauseEntered(MouseEvent mouseEvent) {
    }

    private Timestamp AddTime(Timestamp timestamp,int minute) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());

        cal.add(Calendar.MINUTE, minute);
        timestamp = new Timestamp(cal.getTime().getTime());

        return timestamp;

    }


    public void OnclickedAddVehicle(ActionEvent actionEvent) {


        LOG.debug("onClickedAddVehicle!");

            if(tv_allvehiclelist.getSelectionModel().getSelectedItems().isEmpty()){
                Alert alert=new Alert(Alert.AlertType.WARNING,"No Vehicle selected.",ButtonType.OK);
                alert.showAndWait();
            }else {
                Vehicle v = tv_allvehiclelist.getSelectionModel().getSelectedItem();
                alllist.add(v);
                allvehiclelisttobook.remove(v);
                initializeVehicleList();
                initializeAllVehicleList();
            }

    }

    private void initializeAllVehicleList(){


        LOG.info("Initialize Vehicle List!");

        tc_Alldlicenses.setCellValueFactory(new PropertyValueFactory<>("print"));
        tc_allpunit.setCellValueFactory(new PropertyValueFactory<>("powerUnit"));
        tc_allModel.setCellValueFactory(new PropertyValueFactory<>("title"));
        tc_allprice.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(allvehiclelisttobook);
            tv_allvehiclelist.setItems(vehicles);
            LOG.info("Vehicle list succesfully created for Booking!");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Can't connect to database.",e.getMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,"Can't connect to database. No Vehicle found.", ButtonType.OK);
            alert.showAndWait();
        }

    }



    public void OnClickedSeeAvailableVehicle(ActionEvent actionEvent) {

        LOG.debug("OnClickedSearchVehicle");

        String model ="";
        Double min = 0.0;
        Double max = 0.0;
        int seatcap = 0;
        String punit = "";
        int category = 0;


        String fromas ="";
        String frombs = "";
        String tobs = "";
        String toas ="";
        Timestamp from = null;
        Timestamp to = null;

        if(!tf_hourfrom.getText().isEmpty()){
            frombs = tf_hourfrom.getText().substring(3,5);
            fromas = tf_hourfrom.getText().substring(0,2);
            try {
                Integer.parseInt(fromas);
                Integer.parseInt(frombs);
            } catch (Exception e) {
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setTitle("Warning!");
                alert.setContentText("Hour Form is not Valid! (Valid form hh:mm) ");
                alert.showAndWait();
            }
            from = new Timestamp(Timestamp.from(dp_datefrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            int froma = Integer.parseInt(tf_hourfrom.getText().substring(0,2));
            int fromb = Integer.parseInt(frombs);
            if(froma>23 || froma<0 || fromb> 59 || fromb<0){
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setContentText("Please set time between: 00:00 - 23:59");
                alert.showAndWait();
                return;
            }
        }

        if(!tf_hourto.getText().isEmpty()){
            tobs = tf_hourto.getText().substring(3,5);
            toas = tf_hourto.getText().substring(0,2);
            try {
                Integer.parseInt(toas);
                Integer.parseInt(tobs);
            } catch (Exception e) {
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setTitle("Warning!");
                alert.setContentText("Hour Form is not Valid! (Valid form hh:mm) ");
                alert.showAndWait();
            }
            to = new Timestamp(Timestamp.from(dp_dateto.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
            int toa = Integer.parseInt(tf_hourto.getText().substring(0,2));
            int tob = Integer.parseInt(tobs);
            if(toa>23 || toa<0 || tob>59 || tob<0){
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setContentText("Please set time between: 00:00 - 23:59");
                alert.showAndWait();
                return;
            }
        }

        List<Double> preis = new ArrayList<>();
        preis.add(min);
        preis.add(max);

        String s = "(";

        int size = alllist.size()-1;

        for (Vehicle v:alllist) {
            s += v.getId();
            if(size>0){
                s+=",";
            }else{
                s+=")";
            }
            size--;
        }

        try {
            allvehiclelisttobook = vehicleService.find(model,preis,seatcap,category,punit,from,to,s);
            if(allvehiclelisttobook.size() == 0){
            }
            initializeAllVehicleList();
        } catch (ServiceException e) {
            e.printStackTrace();
            LOG.error("Can't create filtered Vehicle list");
        }


    }


    public void OnClickedSeeTotalSum(ActionEvent actionEvent) {
    }
}
