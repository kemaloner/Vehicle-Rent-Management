package at.ac.tuwien.sepm.assignment.individual.application;

import at.ac.tuwien.sepm.assignment.individual.universe.service.BookingService;
import at.ac.tuwien.sepm.assignment.individual.universe.service.BookingServiceImp;
import at.ac.tuwien.sepm.assignment.individual.universe.service.VehicleService;
import at.ac.tuwien.sepm.assignment.individual.universe.service.VehicleServiceImp;
import at.ac.tuwien.sepm.assignment.individual.universe.ui.MainFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public final class MainApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void start(Stage primaryStage) throws Exception {
        // setup application
        primaryStage.setTitle("Vehicle Menager");
        primaryStage.setWidth(900);
        primaryStage.setHeight(575);
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> LOG.debug("Application shutdown initiated"));

        // initiate service and controller
        VehicleService vehicleService = new VehicleServiceImp();
        BookingService bookingService = new BookingServiceImp();
        MainFrameController mainFrameController = new MainFrameController(vehicleService, bookingService);

        // prepare fxml loader to inject controller
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mainFrame.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(mainFrameController) ? mainFrameController : null);
        primaryStage.setScene(new Scene(fxmlLoader.load()));

        // show application
        primaryStage.show();
        primaryStage.toFront();
        LOG.debug("Application startup complete");
    }

    public static void main(String[] args) {
        LOG.debug("Application starting with arguments={}", (Object) args);
        Application.launch(MainApplication.class, args);
    }

}
