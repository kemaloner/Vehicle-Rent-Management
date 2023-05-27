package at.ac.tuwien.sepm.assignment.individual.universe;

import at.ac.tuwien.sepm.assignment.individual.universe.DAO.DAOException;
import at.ac.tuwien.sepm.assignment.individual.universe.DAO.VehicleDAO;
import at.ac.tuwien.sepm.assignment.individual.universe.DAO.VehicleDAOJDBC;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.LicenseCategory;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VehicleDAOJDBCTest {

    private VehicleDAO vehicleDAO;


    public VehicleDAOJDBCTest() throws DAOException{
        try {
            vehicleDAO = new VehicleDAOJDBC(DBUtilTest.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void create() throws Exception {


        Vehicle vehicle =new Vehicle();
        vehicle.setTitle("Tester");
        vehicle.setBuildyear(2017);
        vehicle.setDescription("");
        vehicle.setSeatingCapacity(2);
        vehicle.setLicensePlate("W12312");
        vehicle.setPower(233);
        vehicle.setPowerUnit("Brawn");
        vehicle.setPrice(12.31);

        vehicle.setLicenseCategories(new ArrayList<LicenseCategory>(Collections.singleton(LicenseCategory.NONE)));

        List<Vehicle> vehicles = new ArrayList<>();

        vehicle = vehicleDAO.create(vehicle);
        vehicles = vehicleDAO.getAllVehicleList();

        Vehicle search = null;

        for (Vehicle v:vehicles) {
            if(v.getId() == vehicle.getId()){
                search=v;
            }
        }

        assertTrue(search!=null);

    }

}
