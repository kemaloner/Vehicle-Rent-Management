package at.ac.tuwien.sepm.assignment.individual.universe.DAO;

import at.ac.tuwien.sepm.assignment.individual.universe.entities.Driver;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.LicenseCategory;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public interface VehicleDAO {

    /**create new vehicle
     * @param vehicle
     * @return returns vehicle with generated id
     * @throws DAOException
     */
    Vehicle create(Vehicle vehicle) throws DAOException;

    /**update veheicle: set isOld=true and create new veheicle
     * @param vehicle
     * @return returns created vehicle with generated id
     * @throws DAOException
     */
    Vehicle update(Vehicle vehicle) throws DAOException;

    /**delete vehicle: set isdelete=true
     * @param vehicle
     * @throws DAOException
     */
    void delete(Vehicle vehicle) throws DAOException;

    /**loading list of vehicles
     * @return returns all vehicles from db
     * @throws DAOException
     */
    List<Vehicle> getAllVehicleList() throws DAOException;

    /**find Vehicle with this id in database
     * @param id Vehicle id
     * @return vehicle
     * @throws DAOException if there is no Vehicle to find
     */
    Vehicle getVehicleById(int id) throws DAOException;

    /**
     * searches for the corresponding Vehicle in the database
     *
     * @param model Model name of Vehicle
     * @param price Price range of Vehicle
     * @param seatcap Seat Capacity of Vehicle
     * @param category if motorized, License Category of Vehicle
     * @param punit Power Unit of Vehicle
     * @param startDate Start date for booking for available Vehicle in this time
     * @param endDate end date for booking for available Vehicle in this time
     * @param ignore ignore this vehicle
     * @return the Vehicles that was found
     * @throws DAOException if no Vehicle is in List
     */
    List<Vehicle> find(String model, List<Double> price, int seatcap, int category, String punit, Timestamp startDate, Timestamp endDate,String ignore) throws DAOException;


}
