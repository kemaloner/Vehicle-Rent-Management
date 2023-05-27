package at.ac.tuwien.sepm.assignment.individual.universe.DAO;

import at.ac.tuwien.sepm.assignment.individual.universe.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Driver;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface BookingDAO {


    /**create new Booking
     * @param booking
     * @return returns booking with generated id
     * @throws DAOException
     */
    Booking create(Booking booking) throws DAOException;

    /**update veheicle: set isOld=true and create new veheicle
     * @param booking
     * @return returns created Booking with generated id
     * @throws DAOException
     */
    Booking update(Booking booking) throws DAOException;

    /** cancel booking
     *
     * @param  booking Booking that will be canceled
     * @return the booking that was saved
     * @throws DAOException if booking can't cancel or any problem
     */
    Booking cancel(Booking booking) throws DAOException;

    /**loading list of booking
     * @return returns all bookings from db
     * @throws DAOException
     */
    List<Booking> getAllBookingList() throws DAOException;

    /**find all Booking in database
     * @param id
     * @return
     * @throws DAOException
     */
    Booking getBookingById(long id) throws DAOException;


    /**add new Driver to Booking
     * @param booking
     * @param vehicle
     * @param driver
     * @return returns booking with generated id
     * @throws DAOException
     */
    Booking addDriver(Booking booking, Vehicle vehicle, Driver driver) throws DAOException;

    /**
     * searches for the corresponding Booking in the database
     *
     * @param startDate Start date for booking for available Vehicle in this time
     * @param endDate end date for booking for available Vehicle in this time
     * @param category category
     * @return the Vehicles that was found
     * @throws DAOException if no Vehicle is in List
     */
    List<Booking> find(Timestamp startDate, Timestamp endDate,int category ) throws DAOException;

}
