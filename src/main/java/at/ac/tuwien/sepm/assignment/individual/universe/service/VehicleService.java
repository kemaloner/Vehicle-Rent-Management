package at.ac.tuwien.sepm.assignment.individual.universe.service;

import at.ac.tuwien.sepm.assignment.individual.universe.DAO.DAOException;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;


public interface VehicleService {

    /** a new Vehicle is saved in the database
     *
     * @param  vehicle Vehicle
     * @return the Vehicle that was saved
     * @throws ServiceException if the given Vehicle is null
     */
    Vehicle create(Vehicle vehicle) throws ServiceException;

    /** a new Vehicle is updated in the database
     *
     * @param  vehicle Vehicle
     * @return the Vehicle that was saved
     * @throws ServiceException if the given Vehicle is null
     */
    Vehicle update(Vehicle vehicle) throws ServiceException;

    /** searching Vehicle by id
     *
     * @param  id Vehicle
     * @return the Vehicle that was saved
     * @throws ServiceException if the given Vehicle is null
     */
    Vehicle getVehicleById(int id) throws ServiceException;

    /** returning all vehicles, without deleted ones
     *
     * @return VehicleList
     * @throws ServiceException if the given Vehicle is null
     */
    List<Vehicle> getAllVehicleList() throws ServiceException;

    /**returning old vehicles
     *
     * @return the Vehicle that was saved
     * @throws ServiceException if the given Vehicle is null
     */
    List<Vehicle> getdeletedVehicleList() throws ServiceException;

    /** deleted vehicle liste
     * @param  vehicle Vehicle that will be delete
     * @throws ServiceException if the given Vehicle is null
     */
    void delete(Vehicle vehicle) throws ServiceException;

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
     * @throws ServiceException if no Vehicle is in List
     */
    List<Vehicle> find(String model, List<Double> price, int seatcap, int category, String punit, Timestamp startDate, Timestamp endDate,String ignore) throws ServiceException;


    /** Selected file copy to source
     * @param  sourceFile copy File
     * @throws ServiceException if the given File is null
     */
    void copyFile(File sourceFile) throws ServiceException;


}
