package at.ac.tuwien.sepm.assignment.individual.universe.service;

import at.ac.tuwien.sepm.assignment.individual.universe.DAO.DAOException;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Driver;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.util.List;

public interface BookingService {

    /** a new booking is saved in the database
     *
     * @param  booking Booking that will be created
     * @return the booking that was saved
     * @throws ServiceException
     */
    Booking create(Booking booking) throws ServiceException;

    /** update booking is saved in the database
     *
     * @param  booking booking that will ve updated
     * @return the booking that was saved
     * @throws ServiceException
     */
    Booking update(Booking booking) throws ServiceException;

    /** cancel booking
     *
     * @param  booking Booking that will be canceled
     * @return the booking that was saved
     * @throws ServiceException if booking can't cancel or any problem
     */
    void cancel(Booking booking) throws ServiceException;

    /** returning all bookings
     *
     * @return list of booking
     * @throws ServiceException
     */
    List<Booking> getAllBookingsList() throws ServiceException;

    /** a new Driver to booking is saved in the database
     *
     * @param  booking
     * @param vehicle
     * @param driver
     * @return the Vehicle that was saved
     * @throws ServiceException
     */
    Booking addDriver(Booking booking, Vehicle vehicle, Driver driver) throws ServiceException;

    /**find Booking with id nr in database
     * @param id Booking id to find
     * @return booking that find
     * @throws ServiceException if booking null
     */
    Booking getBookingById(long id) throws ServiceException;

    /**
     * searches for the corresponding Booking in the database
     *
     * @param startDate Start date for booking for available Vehicle in this time
     * @param endDate end date for booking for available Vehicle in this time
     * @param category license category
     * @return the Vehicles that was found
     * @throws ServiceException if no Vehicle is in List
     */
    List<Booking> find(Timestamp startDate, Timestamp endDate, int category ) throws ServiceException;


    /**
     *
     *
     * @param bookings All Bookings to search
     * @param dayOfWeek searched day week
     * @return the Vehicles that was found
     */
    public int profitOfDay( List<Booking> bookings, DayOfWeek dayOfWeek );


    /**

     * @param bookings All Bookings to search
     * @return the Vehicles that was found
     */
    public long revenuesOfDay( List<Booking> bookings);

}
