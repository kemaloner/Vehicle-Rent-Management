package at.ac.tuwien.sepm.assignment.individual.universe.ui;

import at.ac.tuwien.sepm.assignment.individual.universe.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Status;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.universe.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.universe.service.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.universe.service.VehicleService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainFrameController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final VehicleService vehicleService;
    private final BookingService bookingService;

    //Booking Vehicle
    @FXML
    public TableColumn<Vehicle,String> tcTitle;
    @FXML
    public TableColumn<Vehicle,String> tcDrivingLicence;
    @FXML
    public TableColumn<Vehicle,String> tcPowerUnit;
    @FXML
    public TableColumn<Vehicle,Double> tcPrice;
    @FXML
    public TableView<Vehicle> tv_vehiclelist;
    @FXML
    public TextField tf_model;
    @FXML
    public TextField tf_seatcap;
    @FXML
    public TextField tf_min;
    @FXML
    public TextField tf_max;
    @FXML
    public DatePicker dp_datefrom;
    @FXML
    public RadioButton rb_a;
    @FXML
    public RadioButton rb_b;
    @FXML
    public RadioButton rb_c;
    @FXML
    public TextField tf_hourfrom;
    @FXML
    public DatePicker dp_dateto;
    @FXML
    public TextField tf_hourto;
    @FXML
    private Label mFDLtitle;
    @FXML
    private RadioButton mFmotorized;
    @FXML
    private RadioButton mFwithBrawn;

    private List<Vehicle> vehicleList = new ArrayList<>();

    //Booking

    @FXML
    public TableColumn<Booking,String> tce_cname;
    @FXML
    public TableColumn<Booking,Status> tce_status;
    @FXML
    public TableColumn<Booking,String> tce_bookingper;
    @FXML
    public TableColumn<Booking,Double> tce_preis;
    @FXML
    public Button bt_editbook;
    @FXML
    public Label lb_invoice;
    @FXML
    public TableView tv_editbox;

    //Statistic

    @FXML
    public DatePicker dp_statfrom;
    @FXML
    public DatePicker dp_statto;
    @FXML
    public CheckBox cb_a;
    @FXML
    public CheckBox cb_b;
    @FXML
    public CheckBox cb_none;
    @FXML
    public CheckBox cb_c;
    @FXML
    public LineChart lc_chart;
    @FXML
    public BarChart<String, Integer> bc_chart;


    public MainFrameController(VehicleService vehicleService, BookingService bookingService) {
        this.vehicleService = vehicleService;
        this.bookingService = bookingService;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.info("MainFrameController initialize");
        initializeVehicleList();
        initializeBookingList();
        tv_vehiclelist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tf_max.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()){
                    float value=0;
                }else {
                    if (newValue.matches("((\\d*)|(\\d+))")) {
                        float value = Float.parseFloat(newValue);
                    } else {
                        tf_max.setText(oldValue);
                    }
                }
            }
        });
        tf_min.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()){
                    float value=0;
                }else {
                    if (newValue.matches("((\\d*)|(\\d+))")) {
                        float value = Float.parseFloat(newValue);
                    } else {
                        tf_min.setText(oldValue);
                    }
                }
            }
        });
        tf_seatcap.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()){
                    float value=0;
                }else {
                    if (newValue.matches("((\\d*)|(\\d+))")) {
                        float value = Float.parseFloat(newValue);
                    } else {
                        tf_seatcap.setText(oldValue);
                    }
                }
            }
        });

    }

    public void initializeVehicleList(){
        LOG.info("Initialize Vehicle List!");

        tcDrivingLicence.setCellValueFactory(new PropertyValueFactory<>("print"));
        tcPowerUnit.setCellValueFactory(new PropertyValueFactory<>("powerUnit"));
        tcTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(vehicleService.getAllVehicleList());
            tv_vehiclelist.setItems(vehicles);
            LOG.info("Vehicle list succesfully created!");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Can't connect to database.",e.getMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,"Can't connect to database. No Vehicle found.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void onClickedSeeDetails(ActionEvent actionEvent) {

        LOG.info("on Clicked Edit Vehicle!");

        try {
            Vehicle vehicle = (Vehicle) tv_vehiclelist.getSelectionModel().getSelectedItem();

            if(tv_vehiclelist.getSelectionModel().getSelectedItems().size() > 1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please select only one Vehicle to edit or see details!");
                alert.showAndWait();
            }else if (vehicle == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Please select!");
                alert.setContentText("Please select a Vehicle to edit or see details!");
                alert.showAndWait();
            }else {

                EditVehicleController editVehicleController = new EditVehicleController(vehicleService, this,vehicle,bookingService);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EditVehicle.fxml"));
                fxmlLoader.setControllerFactory(param -> param.isInstance(editVehicleController) ? editVehicleController : null);

                Parent root = null;

                    root = fxmlLoader.load();
                    LOG.info("Edit VehicleFrame succesfully opened");

                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setTitle("Update Vehicle");
                    stage.setScene(scene);
                    stage.show();
            }
        }catch (IOException e) {
                e.printStackTrace();
                LOG.error("Can't open a new Frame for Update Vehicle", e);
            }
    }

    public void onActionMotorized(ActionEvent actionEvent) {

        rb_a.setVisible(true);
        rb_b.setVisible(true);
        rb_c.setVisible(true);
        mFDLtitle.setVisible(true);

    }

    public void onActionWithBrawn(ActionEvent actionEvent) {

        rb_a.setVisible(false);
        rb_b.setVisible(false);
        rb_c.setVisible(false);
        mFDLtitle.setVisible(false);

    }

    public void onMouseEnteredMainFrame(MouseEvent mouseEvent) {
        initializeBookingList();
    }

    public void onClickedAddnewVehicle(ActionEvent actionEvent) {

        LOG.info("on Clicked Add new Vehicle!");

        AddVehicleController addVehicleController= new AddVehicleController(vehicleService,this);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddVehicle.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(addVehicleController) ? addVehicleController : null);

        Parent root= null;
        try {
            root = fxmlLoader.load();
            LOG.info("AddVehicleFrame succesfully opened");

            Scene scene = new Scene(root);
            Stage stage=new Stage();
            stage.setTitle("New Vehicle");
            stage.setScene(scene);
            stage.show();
            initializeVehicleList();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Can't open a new Frame for Add Vehicle", e);
        }

    }

    public void onClickedBookItNow(ActionEvent actionEvent) {

        LOG.info("on Clicked Book it now!");

        ArrayList<Vehicle> listv = new ArrayList<Vehicle>();

        if(tv_vehiclelist.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.WARNING,"No Vehicle selected.",ButtonType.OK);
            alert.showAndWait();
            return;
        }else {
            listv.addAll(tv_vehiclelist.getSelectionModel().getSelectedItems());
        }

        BookingVehicleController bookingVehicleController = new BookingVehicleController(listv, bookingService, vehicleService);

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
            initializeVehicleList();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Can't open a new Frame for new Booking", e);
        }

    }

    public void onClickeddeleteVehicle(ActionEvent actionEvent) {

        if(tv_vehiclelist.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.WARNING,"No Vehicle selected.",ButtonType.OK);
            alert.showAndWait();
        }else {

            String alllist = "";
            List<Vehicle> list = new ArrayList<>();

            for (Vehicle p : tv_vehiclelist.getSelectionModel().getSelectedItems()){
                alllist += p.getTitle()+"\n";
                list.add(p);
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete these Vehicles? \n"+alllist);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                try {
                    for (Vehicle p : list){
                        LOG.info("delete vehicles");
                        vehicleService.delete(p);
                    }
                    initializeVehicleList();
                } catch (ServiceException e) {
                    e.printStackTrace();
                    LOG.warn("Can't Delete Vehicle!"+e);
                }
            }
        }
    }

    public void OnclickedSearchVehicle(ActionEvent actionEvent) {

        LOG.debug("OnClickedSearchVehicle");

        String model ="";
        Double min = 0.0;
        Double max = 0.0;
        int seatcap = 0;
        String punit = "";
        int category = 0;


        if(!tf_model.getText().isEmpty()){
            model = tf_model.getText();
        }

        if(!tf_max.getText().isEmpty()){
            max = Double.valueOf(tf_max.getText());
        }

        if(!tf_min.getText().isEmpty()){
            min = Double.valueOf(tf_min.getText());
        }

        if(!tf_seatcap.getText().isEmpty()){
            seatcap = Integer.parseInt(tf_seatcap.getText());
        }

        if(mFmotorized.isSelected()){
            punit = "Motorized";
            if(rb_a.isSelected()){
                category = 1;
            }else if(rb_b.isSelected()){
                category = 2;
            }else if(rb_c.isSelected()){
                category = 3;
            }
        }else if(mFwithBrawn.isSelected()){
            punit = "Brawn";
        }

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

        try {
            vehicleList = vehicleService.find(model,preis,seatcap,category,punit,from,to,"");
            initializeFilteredVehicleList();
        } catch (ServiceException e) {
            e.printStackTrace();
            LOG.error("Can't create filtered Vehicle list");
        }

    }

    private void initializeFilteredVehicleList(){
        LOG.info("Initialize Vehicle List!");

        tcDrivingLicence.setCellValueFactory(new PropertyValueFactory<>("print"));
        tcPowerUnit.setCellValueFactory(new PropertyValueFactory<>("powerUnit"));
        tcTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(vehicleList);
            tv_vehiclelist.setItems(vehicles);
            LOG.info("Vehicle list succesfully created!");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Can't connect to database.",e.getMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,"Can't connect to database. No Vehicle found.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void OnClickedClear(ActionEvent actionEvent) {

        LOG.debug("OnClickedClearButton");

        tf_model.setText("");
        tf_seatcap.setText("");
        tf_hourfrom.setText("");
        tf_hourto.setText("");
        tf_min.setText("");
        tf_max.setText("");
        mFwithBrawn.setSelected(false);
        mFmotorized.setSelected(false);
        rb_a.setSelected(false);
        rb_b.setSelected(false);
        rb_c.setSelected(false);
        dp_datefrom.setValue(null);
        dp_dateto.setValue(null);

        initializeVehicleList();
    }



    /////////////////////////////////booking/////////////////////////////////

    public void OnClickedTvbooking(MouseEvent tableViewSortEvent) {

        LOG.debug("OnclickedTVBooking to see Detalied Invoice!");

        try {

            Booking booking = (Booking) tv_editbox.getSelectionModel().getSelectedItem();

            if(tv_vehiclelist.getSelectionModel().getSelectedItems().size() > 1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please select only one Invoice to see Details");
                alert.showAndWait();
            }else if (booking == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Please select!");
                alert.setContentText("Please select a Invoice to see Details");
                alert.showAndWait();
            }else if(booking.getStatus() == Status.open){
                lb_invoice.setText("");
            }else {

                for (int i: booking.getVehiclesid()) {
                    booking.setVehicles(vehicleService.getVehicleById(i));
                }

                lb_invoice.setText(booking.toString());
            }
        }catch (ServiceException e) {
            e.printStackTrace();
            Alert alert=new Alert(Alert.AlertType.ERROR,"Can't see this Invoice. Status of this Booking can be open.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void OnClickedEditBookingButton(ActionEvent actionEvent) {

        LOG.debug("onClickedEditBooking");

        try {
            Booking booking = (Booking) tv_editbox.getSelectionModel().getSelectedItem();

            if(tv_vehiclelist.getSelectionModel().getSelectedItems().size() > 1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please select only one Booking to edit or see details!");
                alert.showAndWait();
            }else if (booking == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Please select!");
                alert.setContentText("Please select a Booking to edit or see details!");
                alert.showAndWait();
            }else if(booking.getStatus() == Status.completed || booking.getStatus() == Status.canceled){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("you are unable to edit invoices.");
                alert.showAndWait();
            }else {

                if(booking.getVehiclesid() != null){
                    for (int i : booking.getVehiclesid()) {
                        Vehicle vehicle = new Vehicle();
                        try {
                            vehicle = vehicleService.getVehicleById(i);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                        booking.setVehicles(vehicle);
                    }}

        BookingVehicleController bookingVehicleController = new BookingVehicleController(booking, bookingService, vehicleService);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BookingVehicle.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(bookingVehicleController) ? bookingVehicleController : null);

        Parent root= null;
        root = fxmlLoader.load();
        LOG.info("BookingVehicle succesfully opened");

            Scene scene = new Scene(root);
            Stage stage=new Stage();
            stage.setTitle("New Booking");
            stage.setScene(scene);
            stage.show();

        }} catch (IOException e) {
            e.printStackTrace();
            LOG.error("Can't open a new Frame for new Booking", e);
        }
    }

    private void initializeBookingList(){
        LOG.info("Initialize Booking List!");

        tce_cname.setCellValueFactory(new PropertyValueFactory<>("cname"));
        tce_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tce_bookingper.setCellValueFactory(new PropertyValueFactory<>("bookingper"));
        tce_preis.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            ObservableList<Booking> bookings = FXCollections.observableArrayList(bookingService.getAllBookingsList());
            tv_editbox.setItems(bookings);
            LOG.info("Booking list succesfully created!");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Can't connect to database.",e.getMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,"Can't connect to database. No Vehicle found.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void OnClickedMakeInvoice(ActionEvent actionEvent) {

        LOG.debug("Onclickedmakeinvoice");

        try {

            Booking booking = (Booking) tv_editbox.getSelectionModel().getSelectedItem();

            if(tv_vehiclelist.getSelectionModel().getSelectedItems().size() > 1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please select only one Booking to make it Invoice!");
                alert.showAndWait();
            }else if (booking == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Please select!");
                alert.setContentText("Please select a Booking to make it Invoice!");
                alert.showAndWait();
            }else if(booking.getStatus() == Status.completed || booking.getStatus() == Status.canceled){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("This is already Invioce.");
                alert.showAndWait();
            }else if(booking.getEndDate().getTime() <= new Timestamp(System.currentTimeMillis()).getTime()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("This Booking is Open! You can cancel it to make Invoice");
                alert.showAndWait();
            }else {

                    bookingService.cancel(booking);
                    Alert alert=new Alert(Alert.AlertType.ERROR,"Invoice succesfully created", ButtonType.OK);
                    alert.showAndWait();
                    initializeBookingList();
        }
    }catch (ServiceException e) {
            e.printStackTrace();
            Alert alert=new Alert(Alert.AlertType.ERROR,"Can't make an Invoice. It can be already canceled or completed.", ButtonType.OK);
            alert.showAndWait();
        }
}

    ///////////////////////////statistics/////////////////////////////////

    public void OnClickedStatsClear(ActionEvent actionEvent) {
        dp_statfrom.setValue(null);
        dp_statto.setValue(null);
        cb_a.setSelected(false);
        cb_b.setSelected(false);
        cb_c.setSelected(false);
        cb_none.setSelected(false);
    }

    public void OnClickedApply(ActionEvent actionEvent) {

        LOG.debug("check dates and selections");

        Timestamp from=null;
        Timestamp to;

        if(dp_statfrom.getValue()!=null) {
            from = new Timestamp(Timestamp.from(dp_statfrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        }else{
            from = new Timestamp(System.currentTimeMillis());
        }

        if(dp_statto.getValue()!=null) {
            to = new Timestamp(Date.from(dp_statto.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        }else{
            to = new Timestamp(System.currentTimeMillis());
        }

        if(from.after(to)){
            Alert info = new Alert(Alert.AlertType.INFORMATION,"Start-Date is after End-Date! \nResult will always be empty.");
            info.showAndWait();
        }
        if(!cb_a.isSelected() && !cb_b.isSelected() && !cb_c.isSelected() && !cb_none.isSelected()){
            Alert info =new Alert(Alert.AlertType.INFORMATION,"No Category selected. All Products will be included.");
            info.showAndWait();
        }


        fillBarChart(from,to);
        fillLineChart(from,to);

    }

    private void fillBarChart(Timestamp from, Timestamp to){

        LOG.info("filling barChart");

        List<Booking> bookings = null;

        try {

            bc_chart.getData().clear();

            if(cb_a.isSelected()){
                bookings = bookingService.find(from,to,4);

                int salesMon = bookingService.profitOfDay(bookings, DayOfWeek.MONDAY);
                int salesTue = bookingService.profitOfDay(bookings, DayOfWeek.TUESDAY);
                int salesWed = bookingService.profitOfDay(bookings, DayOfWeek.WEDNESDAY);
                int salesThu = bookingService.profitOfDay(bookings, DayOfWeek.THURSDAY);
                int salesFri = bookingService.profitOfDay(bookings, DayOfWeek.FRIDAY);
                int salesSat = bookingService.profitOfDay(bookings, DayOfWeek.SATURDAY);
                int salesSun = bookingService.profitOfDay(bookings, DayOfWeek.SUNDAY);

                XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
                series.setName("A");
                series.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.MONDAY.toString(), salesMon));
                series.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.TUESDAY.toString(), salesTue));
                series.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.WEDNESDAY.toString(), salesWed));
                series.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.THURSDAY.toString(), salesThu));
                series.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.FRIDAY.toString(), salesFri));
                series.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.SATURDAY.toString(), salesSat));
                series.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.SUNDAY.toString(), salesSun));

                bc_chart.getData().addAll(series);

            }

            if(cb_b.isSelected()){
                bookings = bookingService.find(from,to,2);

                int salesMon = bookingService.profitOfDay(bookings, DayOfWeek.MONDAY);
                int salesTue = bookingService.profitOfDay(bookings, DayOfWeek.TUESDAY);
                int salesWed = bookingService.profitOfDay(bookings, DayOfWeek.WEDNESDAY);
                int salesThu = bookingService.profitOfDay(bookings, DayOfWeek.THURSDAY);
                int salesFri = bookingService.profitOfDay(bookings, DayOfWeek.FRIDAY);
                int salesSat = bookingService.profitOfDay(bookings, DayOfWeek.SATURDAY);
                int salesSun = bookingService.profitOfDay(bookings, DayOfWeek.SUNDAY);

                XYChart.Series<String, Integer> series2 = new XYChart.Series<String, Integer>();
                series2.setName("B");
                series2.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.MONDAY.toString(), salesMon));
                series2.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.TUESDAY.toString(), salesTue));
                series2.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.WEDNESDAY.toString(), salesWed));
                series2.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.THURSDAY.toString(), salesThu));
                series2.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.FRIDAY.toString(), salesFri));
                series2.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.SATURDAY.toString(), salesSat));
                series2.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.SUNDAY.toString(), salesSun));

                bc_chart.getData().addAll(series2);
            }

            if(cb_c.isSelected()){
                bookings = bookingService.find(from,to,3);

                int salesMon = bookingService.profitOfDay(bookings, DayOfWeek.MONDAY);
                int salesTue = bookingService.profitOfDay(bookings, DayOfWeek.TUESDAY);
                int salesWed = bookingService.profitOfDay(bookings, DayOfWeek.WEDNESDAY);
                int salesThu = bookingService.profitOfDay(bookings, DayOfWeek.THURSDAY);
                int salesFri = bookingService.profitOfDay(bookings, DayOfWeek.FRIDAY);
                int salesSat = bookingService.profitOfDay(bookings, DayOfWeek.SATURDAY);
                int salesSun = bookingService.profitOfDay(bookings, DayOfWeek.SUNDAY);

                XYChart.Series<String, Integer> series3 = new XYChart.Series<String, Integer>();
                series3.setName("C");
                series3.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.MONDAY.toString(), salesMon));
                series3.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.TUESDAY.toString(), salesTue));
                series3.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.WEDNESDAY.toString(), salesWed));
                series3.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.THURSDAY.toString(), salesThu));
                series3.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.FRIDAY.toString(), salesFri));
                series3.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.SATURDAY.toString(), salesSat));
                series3.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.SUNDAY.toString(), salesSun));

                bc_chart.getData().addAll(series3);
            }

            if(cb_none.isSelected()){
                bookings = bookingService.find(from,to,4);

                int salesMon = bookingService.profitOfDay(bookings, DayOfWeek.MONDAY);
                int salesTue = bookingService.profitOfDay(bookings, DayOfWeek.TUESDAY);
                int salesWed = bookingService.profitOfDay(bookings, DayOfWeek.WEDNESDAY);
                int salesThu = bookingService.profitOfDay(bookings, DayOfWeek.THURSDAY);
                int salesFri = bookingService.profitOfDay(bookings, DayOfWeek.FRIDAY);
                int salesSat = bookingService.profitOfDay(bookings, DayOfWeek.SATURDAY);
                int salesSun = bookingService.profitOfDay(bookings, DayOfWeek.SUNDAY);

                XYChart.Series<String, Integer> series4 = new XYChart.Series<String, Integer>();
                series4.setName("None");
                series4.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.MONDAY.toString(), salesMon));
                series4.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.TUESDAY.toString(), salesTue));
                series4.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.WEDNESDAY.toString(), salesWed));
                series4.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.THURSDAY.toString(), salesThu));
                series4.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.FRIDAY.toString(), salesFri));
                series4.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.SATURDAY.toString(), salesSat));
                series4.getData().add(new XYChart.Data<String, Integer>(DayOfWeek.SUNDAY.toString(), salesSun));

                bc_chart.getData().addAll(series4);
            }

            lc_chart.getYAxis().setLabel("Day");
        } catch (ServiceException e) {
            LOG.error(e.getMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,"failed to load filtered Booking. Check database connection.");
            alert.showAndWait();
        }
    }

    private void fillLineChart(Timestamp from, Timestamp to) {

        LOG.info("filling lineChart");

        List<Booking> bookings = null;

        try {

            lc_chart.getData().clear();

            if(cb_a.isSelected()){
                bookings = bookingService.find(from,to,1);

                XYChart.Series<String,Long> seriesA = new XYChart.Series<String,Long>();
                seriesA.setName("A");

                seriesA.getData().add(new XYChart.Data<String,Long>((new java.sql.Date(java.sql.Date.from(from.toInstant()).getTime())).toString(),bookingService.revenuesOfDay(bookings)));
                seriesA.getData().add(new XYChart.Data<String,Long>((new java.sql.Date(java.sql.Date.from(to.toInstant()).getTime())).toString(),bookingService.revenuesOfDay(bookings)));

                lc_chart.getData().add(seriesA);

            }

            if(cb_b.isSelected()){
                bookings = bookingService.find(from,to,2);

                XYChart.Series<String,Long> seriesB = new XYChart.Series<String,Long>();
                seriesB.setName("B");

                seriesB.getData().add(new XYChart.Data<String,Long>((new java.sql.Date(java.sql.Date.from(from.toInstant()).getTime())).toString(),bookingService.revenuesOfDay(bookings)));
                seriesB.getData().add(new XYChart.Data<String,Long>((new java.sql.Date(java.sql.Date.from(to.toInstant()).getTime())).toString(),bookingService.revenuesOfDay(bookings)));

                lc_chart.getData().add(seriesB);

            }

            if(cb_c.isSelected()){
                bookings = bookingService.find(from,to,3);

                XYChart.Series<String,Long> seriesC = new XYChart.Series<String,Long>();
                seriesC.setName("A");

                seriesC.getData().add(new XYChart.Data<String,Long>((new java.sql.Date(java.sql.Date.from(from.toInstant()).getTime())).toString(),bookingService.revenuesOfDay(bookings)));
                seriesC.getData().add(new XYChart.Data<String,Long>((new java.sql.Date(java.sql.Date.from(to.toInstant()).getTime())).toString(),bookingService.revenuesOfDay(bookings)));

                lc_chart.getData().add(seriesC);

            }

            if(cb_none.isSelected()){
                bookings = bookingService.find(from,to,4);

                XYChart.Series<String,Long> seriesD = new XYChart.Series<String, Long>();
                seriesD.setName("None");

                seriesD.getData().add(new XYChart.Data<String,Long>((new java.sql.Date(java.sql.Date.from(from.toInstant()).getTime())).toString(),bookingService.revenuesOfDay(bookings)));
                seriesD.getData().add(new XYChart.Data<String,Long>((new java.sql.Date(java.sql.Date.from(to.toInstant()).getTime())).toString(),bookingService.revenuesOfDay(bookings)));

                lc_chart.getData().add(seriesD);

            }

            lc_chart.getYAxis().setLabel("Euro(\u20AC)");

        } catch (ServiceException e) {
            LOG.error(e.getMessage());
            Alert alert=new Alert(Alert.AlertType.ERROR,"failed to load filtered Booking. Check database connection.");
            alert.showAndWait();
        }
    }

}
