package at.ac.tuwien.sepm.assignment.individual.universe.DAO;

import at.ac.tuwien.sepm.assignment.individual.universe.entities.*;

import at.ac.tuwien.sepm.assignment.individual.universe.entities.Driver;
import at.ac.tuwien.sepm.assignment.individual.universe.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BookingDAOJDBC implements BookingDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Connection con;

    static {
        try {
            con = DBUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Booking create(Booking booking) throws DAOException {

        LOG.debug("creating the new Booking!");

        if(booking == null){
            LOG.debug("The Vehicle is null!");
            throw new DAOException("The Vehicle is null!");
        }else {

            try {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO BOOKING (CNAME,IBAN,CNUMBER,STARTDATE,ENDDATE,PRICE,STATUS)" +
                        " VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);


                ps.setString(1,booking.getCname());
                ps.setString(2,booking.getIban());
                ps.setString(3,booking.getCnumber());
                ps.setTimestamp(4,booking.getStartDate());
                ps.setTimestamp(5,booking.getEndDate());
                ps.setDouble(6,booking.getPrice());
                ps.setInt(7,booking.getStatus()== Status.open ? 1 : (booking.getStatus() == Status.completed ? 2 : 3));
                ps.execute();

                ResultSet generatedKeys = ps.getGeneratedKeys();

                if(generatedKeys.next())
                    booking.setBid(generatedKeys.getInt(1));



                for(Vehicle v : booking.getVehicles()) {

                    Driver driver = new Driver();
                    if(booking.getBookedVehiclewithDriver().get(v.getId()) == null){
                        driver.setDid(0);
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        driver.setLdate(timestamp);
                    }else {
                        driver = booking.getBookedVehiclewithDriver().get(v.getId());
                    }
                    addDriver(booking,v,driver);
                }
            } catch (SQLException e) {
                LOG.error("Vehicle can not be created.", e.getMessage());
                throw new DAOException("Vehicle can not be created." + e);
            }
        }
        return booking;
    }

    @Override
    public Booking addDriver(Booking booking, Vehicle vehicle, Driver driver) throws DAOException {

        LOG.debug("addingnew Driver to the Booking!");

        if(booking == null || vehicle == null){
            LOG.debug("The Vehicle is null!");
            throw new DAOException("The Vehicle is null!");
        }else {

            try {

                    PreparedStatement pd = con.prepareStatement("INSERT INTO Bookedvehicle (bid,vid,did,dday) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
                    pd.setInt(1, booking.getBid());
                    pd.setInt(2, vehicle.getId());
                    pd.setInt(3, driver.getDid());
                    pd.setTimestamp(4,driver.getLdate());
                    pd.execute();


            } catch (SQLException e) {
                LOG.error("Driver can't add to booking.", e.getMessage());
                throw new DAOException("Driver can't add to booking." + e);
            }
        }
        return booking;
    }

    @Override
    public Booking update(Booking booking) throws DAOException {

        LOG.debug("Update the Booking!");

        if(booking == null){
            LOG.debug("The Booking is null!");
            throw new DAOException("The Booking is null!");
        }else {

            try {
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE BOOKING SET CNAME = ?,IBAN = ?,CNUMBER = ?,STARTDATE = ?,ENDDATE = ?,PRICE = ?,STATUS = ? WHERE BID = ? ");


                ps.setString(1,booking.getCname());
                ps.setString(2,booking.getIban());
                ps.setString(3,booking.getCnumber());
                ps.setTimestamp(4,booking.getStartDate());
                ps.setTimestamp(5,booking.getEndDate());
                ps.setDouble(6,booking.getPrice());
                ps.setInt(7,booking.getStatus()== Status.open ? 1 : (booking.getStatus() == Status.completed ? 2 : 3));
                ps.setInt(8,booking.getBid());
                ps.execute();

                for(Vehicle v : booking.getVehicles()) {

                    PreparedStatement pd = con.prepareStatement("INSERT INTO BOOKEDVEHICLE (BID,VID,DID,DDAY) SELECT ?,?,?,? FROM dual WHERE NOT EXISTS(SELECT 1 FROM BOOKEDVEHICLE WHERE BID = ? AND VID = ?)");
                    pd.setInt(1, booking.getBid());
                    pd.setInt(2, v.getId());
                    pd.setInt(3, booking.getBookedVehiclewithDriver().get(v.getId()) == null ? 0 : booking.getBookedVehiclewithDriver().get(v.getId()).getDid());
                    pd.setTimestamp(4, booking.getBookedVehiclewithDriver().get(v.getId()) == null ? new Timestamp(System.currentTimeMillis()) : booking.getBookedVehiclewithDriver().get(v.getId()).getLdate());
                    pd.setInt(5, booking.getBid());
                    pd.setInt(6, v.getId());
                    pd.execute();

                }
            } catch (SQLException e) {
                LOG.error("Booking can not be updated.", e.getMessage());
                throw new DAOException("Booking can not be updated." + e);
            }
        }
        return booking;
    }

    @Override
    public Booking cancel(Booking booking) throws DAOException {

        LOG.debug("Booking cancel clicked on BookingDAO");

        if (checkTime(booking.getStartDate())) {
            try {
                PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM BOOKEDVEHICLE WHERE BID = "+booking.getBid());
                ps.execute();
            } catch (SQLException e) {
                LOG.debug("Exception in delete Booking!");
                throw new DAOException("Exception in delete Booking!"+e);
            }
            try {
                PreparedStatement pm = con.prepareStatement(
                    "DELETE FROM BOOKING WHERE BID = "+booking.getBid());
                pm.execute();
            } catch (SQLException e) {
                LOG.debug("Exception in delete Booking!");
                throw new DAOException("Exception in delete Booking!"+e);
            }
        }else{

            try {

                PreparedStatement pd = con.prepareStatement(
                    "UPDATE BOOKING SET ENDDATE = ?,PRICE = ?,STATUS = ? WHERE BID = ?" );

                pd.setTimestamp(1,booking.getEndDate());
                pd.setDouble(2,booking.getPrice());
                pd.setInt(3,booking.getStatus()== Status.open ? 1 : (booking.getStatus() == Status.completed ? 2 : 3));
                pd.setInt(4,booking.getBid());
                pd.executeUpdate();

            } catch (SQLException e) {
                LOG.debug("Exception in Update Booking Status!");
                throw new DAOException("Exception in Update Booking Status!"+e);
            }
        }
        return booking;
    }

    @Override
    public List<Booking> getAllBookingList() throws DAOException {

        LOG.debug("get All Bookings");

        List<Booking> bookinglist = new ArrayList<Booking>();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BOOKING ORDER BY STARTDATE");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Booking booking = new Booking();
                booking.setBid(rs.getInt("BID"));
                booking.setCname(rs.getString("cname"));
                int typ=rs.getInt("status");
                booking.setStatus(typ==1 ? Status.open : (typ==2 ? Status.completed : Status.canceled ));
                booking.setIban((rs.getString("iban")));
                booking.setCnumber(rs.getString("cnumber"));
                booking.setStartDate(rs.getTimestamp("startdate"));
                booking.setEndDate(rs.getTimestamp("enddate"));
                booking.setCreateDate(rs.getTimestamp("createdate"));
                booking.setPrice(rs.getDouble("price"));

                PreparedStatement pd = con.prepareStatement("SELECT v.VID, bv.DID, bv.dday " +
                    "FROM BOOKING B JOIN BOOKEDVEHICLE BV ON B.BID = BV.BID JOIN VEHICLE V ON BV.VID = V.VID WHERE b.BID = ?");

                pd.setInt(1,rs.getInt("BID"));
                ResultSet rd = pd.executeQuery();
                ArrayList<Integer> vehicles = new ArrayList<>();

                while (rd.next()) {
                    Driver d = new Driver();
                    d.setDid(rd.getInt("DID"));
                    d.setLdate(rd.getTimestamp("DDAY"));
                    booking.setBookedVehiclewithDriver(rd.getInt("VID"),d);
                    vehicles.add(rd.getInt("VID"));
                }

                booking.setVehiclesid(vehicles);
                bookinglist.add(booking);
            }

            LOG.info("Booking list successufully created.");
        }catch (SQLException e) {
            e.printStackTrace();
            LOG.error("Booking list can't be created!",e);
            throw new DAOException("Booking list can't be created!");
        }
        return bookinglist;
    }

    @Override
    public Booking getBookingById(long id) throws DAOException {
        return null;
    }

    private Boolean checkTime(Timestamp timestamp) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.DATE,7);
        timestamp = new Timestamp(cal.getTime().getTime());


        return timestamp.getTime() <= new Timestamp(System.currentTimeMillis()).getTime();

    }

    @Override
    public List<Booking> find(Timestamp startDate, Timestamp endDate, int category) throws DAOException {

        List<Booking> list = new ArrayList<>();

        LOG.debug("Loading filtered Booking.");


        String tableOK = "CREATE VIEW IF NOT EXISTS FILTERB AS SELECT V.VID,V2.LID,B2.* FROM VEHICLE V LEFT JOIN BOOKEDVEHICLE B ON V.VID = B.VID LEFT JOIN VEHICLELICENCE V2 ON V.VID = V2.VID JOIN BOOKING B2 ON B.BID = B2.BID";

        try {
            PreparedStatement psd = con.prepareStatement(tableOK);
            psd.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String select = "SELECT DISTINCT * FROM FILTERB WHERE ";

        if(startDate != null && endDate != null) {
            select += "STARTDATE >= '"+startDate+"' and  ENDDATE <= '"+endDate+"' ";
        }


        if(category != 0){
            select += "AND LID = "+category;
        }


        try{
            PreparedStatement ps = con.prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            int bevor = 0;

            while (rs.next()) {

                Booking booking = new Booking();
                booking.setStartDate(rs.getTimestamp("startdate"));
                booking.setEndDate(rs.getTimestamp("enddate"));
                booking.setCreateDate(rs.getTimestamp("createdate"));
                booking.setPrice(rs.getDouble("price"));

                PreparedStatement pd = con.prepareStatement("SELECT v.VID, bv.DID, bv.dday " +
                    "FROM BOOKING B JOIN BOOKEDVEHICLE BV ON B.BID = BV.BID JOIN VEHICLE V ON BV.VID = V.VID WHERE b.BID = ?");

                pd.setInt(1,rs.getInt("BID"));
                ResultSet rd = pd.executeQuery();
                ArrayList<Integer> vehicles = new ArrayList<>();

                while (rd.next()) {
                    vehicles.add(rd.getInt("VID"));
                }

                booking.setVehiclesid(vehicles);
                list.add(booking);

            }

            LOG.info("Vehicle successufully created.");
        }catch (SQLException e) {
            e.printStackTrace();
            LOG.error("Vehicle can't be created!",e);
            throw new DAOException("Vehicle list can't be created!");
        }

        return list;
    }

}
