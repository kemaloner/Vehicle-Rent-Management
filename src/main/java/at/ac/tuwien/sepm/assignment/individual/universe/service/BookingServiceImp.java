package at.ac.tuwien.sepm.assignment.individual.universe.service;

import at.ac.tuwien.sepm.assignment.individual.universe.DAO.BookingDAO;
import at.ac.tuwien.sepm.assignment.individual.universe.DAO.BookingDAOJDBC;
import at.ac.tuwien.sepm.assignment.individual.universe.DAO.DAOException;
import at.ac.tuwien.sepm.assignment.individual.universe.DAO.VehicleDAOJDBC;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Booking;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Driver;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Status;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BookingServiceImp implements BookingService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private BookingDAO bookingDAO;


    public BookingServiceImp() {

        this.bookingDAO = new BookingDAOJDBC();
    }

    @Override
    public Booking create(Booking booking) throws ServiceException {

        LOG.debug("Create BookingserviceImp");

        try {
            return bookingDAO.create(booking);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("can't create Booking");
        }
    }

    @Override
    public Booking update(Booking booking) throws ServiceException {
        LOG.debug("Update BookingserviceImp");
        try {
            return bookingDAO.update(booking);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("can't update Booking");
        }
    }

    @Override
    public void cancel(Booking booking) throws ServiceException {

        LOG.debug("Cancel BookingserviceImp");

        Timestamp currentday = new Timestamp(System.currentTimeMillis());

        if(booking.getStatus() == Status.open) {
            if (checkTime(booking.getStartDate(),7).getTime() <= currentday.getTime()) {
                try {
                    bookingDAO.cancel(booking);
                } catch (DAOException e) {
                    e.printStackTrace();
                    LOG.error("Booking can't delete");
                    throw new ServiceException("Eror bei delete Booking#1");
                }
            } else {

                System.out.println(checkTime(booking.getStartDate(),3));

                if(checkTime(currentday,3).getTime() <= booking.getStartDate().getTime()){
                    Double preis = booking.getPrice();
                    booking.setPrice(preis*0.6);
                    booking.setStatus(Status.canceled);
                    booking.setEndDate(currentday);
                }else if(checkTime(currentday,1).getTime() <= booking.getStartDate().getTime()){
                    Double preis = booking.getPrice();
                    booking.setPrice(preis*0.25);
                    booking.setStatus(Status.canceled);
                    booking.setEndDate(currentday);
                }else {
                    booking.setEndDate(currentday);
                    booking.setStatus(Status.completed);
                }

                try {
                    bookingDAO.cancel(booking);
                } catch (DAOException e) {
                    e.printStackTrace();
                    throw new ServiceException("Eror cancel booking");
                }

            }
        }else{
            LOG.error("can't cancel book: "+booking.getBid());
        }
    }

    @Override
    public List<Booking> getAllBookingsList() throws ServiceException {

        LOG.debug("getallbookinglist BookingserviceImp");

        try {
            return bookingDAO.getAllBookingList();
        } catch (DAOException e) {
            e.printStackTrace();
        }throw new ServiceException("Booking List ist null");
    }

    @Override
    public Booking addDriver(Booking booking, Vehicle vehicle, Driver driver) throws ServiceException {
        LOG.debug("addDriver BookingserviceImp");

        try {
            return bookingDAO.addDriver(booking,vehicle,driver);
        } catch (DAOException e) {
            e.printStackTrace();
        }throw new ServiceException("can't add New driver to vehicle for booking");
    }

    @Override
    public Booking getBookingById(long id) throws ServiceException {
        try {
            return bookingDAO.getBookingById(id);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("can't get Booking with id:"+id);
        }
    }

    @Override
    public List<Booking> find(Timestamp startDate, Timestamp endDate, int category) throws ServiceException {
        try {
            return bookingDAO.find(startDate,endDate,category);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("Can't create filtered list"+e);
        }
    }

    @Override
    public int profitOfDay( List<Booking> bookings, DayOfWeek dayOfWeek ) {

        int sum=0;

        if(!bookings.isEmpty()){
            for(Booking booking:bookings){
                Calendar cal = new GregorianCalendar();

                Timestamp tmp = booking.getStartDate();

                while (tmp.getTime() <= booking.getEndDate().getTime()){

                    cal.setTime(tmp);


                    if(dayOfWeek.getValue() == cal.get(Calendar.DAY_OF_WEEK)){
                        sum += booking.getVehiclesid().size();
                    }
                    tmp = checkTime(tmp,1);
                }
            }
        }

        return sum;
    }

    @Override
    public long revenuesOfDay( List<Booking> bookings) {

        int sum=0;

        if(!bookings.isEmpty()){
            for(Booking booking:bookings){
                sum += booking.getPrice();
        }
        }
        return sum;
    }


    private Timestamp checkTime(Timestamp timestamp, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.DATE, day);
        timestamp = new Timestamp(cal.getTime().getTime());

        return timestamp;
    }

}
