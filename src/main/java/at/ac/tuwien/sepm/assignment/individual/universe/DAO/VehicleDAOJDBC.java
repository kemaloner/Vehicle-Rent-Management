package at.ac.tuwien.sepm.assignment.individual.universe.DAO;

import at.ac.tuwien.sepm.assignment.individual.universe.entities.Driver;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.LicenseCategory;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.universe.service.VehicleServiceImp;
import at.ac.tuwien.sepm.assignment.individual.universe.util.DBUtil;
import org.h2.command.dml.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAOJDBC implements VehicleDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Connection con;


    public VehicleDAOJDBC() throws DAOException {
        try {
            con = DBUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Connection to Database failed.");
        }
    }

    public VehicleDAOJDBC(Connection con){
        this.con = con;
    }

    @Override
    public Vehicle create(Vehicle vehicle) throws DAOException {

        LOG.debug("creating new Vehicle!");

        if(vehicle == null){
            LOG.debug("The Vehicle is null!");
            throw new DAOException("The Vehicle is null!");
        }else {

            try {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Vehicle (TITLE,BUILDYEAR,DESCRIPTION,SEATINGCAPACITY,LICENSEPLATE,POWERUNIT,POWER,PRICE,IMAGEURL)" +
                        " VALUES (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

                ps.setString(1,vehicle.getTitle());
                ps.setInt(2,vehicle.getBuildyear());
                ps.setString(3,vehicle.getDescription());
                ps.setInt(4,vehicle.getSeatingCapacity());
                ps.setString(5,vehicle.getLicensePlate());
                ps.setString(6,vehicle.getPowerUnit());
                ps.setInt(7,vehicle.getPower());
                ps.setDouble(8,vehicle.getPrice());
                ps.setString(9,vehicle.getImageUrl());
                ps.execute();

                ResultSet generatedKeys = ps.getGeneratedKeys();

                if(generatedKeys.next())
                    vehicle.setId(generatedKeys.getInt(1));

                LOG.info("Vehicle successfully saved in the database!");

                if(vehicle.getLicenseCategories().size() != 0){
                    for (LicenseCategory i : vehicle.getLicenseCategories()) {
                        PreparedStatement pd = con.prepareStatement("INSERT INTO VehicleLicence VALUES (?,?)");
                        pd.setInt(1, vehicle.getId());
                        pd.setInt(2, i == LicenseCategory.A ? 1 : (i == LicenseCategory.B ? 2 : (i == LicenseCategory.C ? 3 : 4)));
                        pd.execute();
                    }
                }
            } catch (SQLException e) {
                LOG.error("Vehicle can not be created.", e.getMessage());
                throw new DAOException("Vehicle can not be created." + e);
            }
        }
        return vehicle;
    }

    @Override
    public Vehicle update(Vehicle vehicle) throws DAOException {

        if(vehicle == null){
            LOG.debug("The Vehicle is null!");
            throw new DAOException("The Vehicle is null!");
        }else {
            LOG.info("updating the vehicle");

            try {
                PreparedStatement ps= con.prepareStatement(
                    "update Vehicle set isdelete = ? where VID="+vehicle.getId());
                ps.setBoolean(1,true);
                ps.executeUpdate();

            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw  new DAOException("Vehicle not updated.");
            }

            try {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Vehicle (TITLE,BUILDYEAR,DESCRIPTION,SEATINGCAPACITY,LICENSEPLATE,POWERUNIT,POWER,PRICE,IMAGEURL,createDate,updatedate)" +
                        " VALUES (?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

                ps.setString(1,vehicle.getTitle());
                ps.setInt(2,vehicle.getBuildyear());
                ps.setString(3,vehicle.getDescription());
                ps.setInt(4,vehicle.getSeatingCapacity());
                ps.setString(5,vehicle.getLicensePlate());
                ps.setString(6,vehicle.getPowerUnit());
                ps.setInt(7,vehicle.getPower());
                ps.setDouble(8,vehicle.getPrice());
                ps.setString(9,vehicle.getImageUrl());
                ps.setTimestamp(10,vehicle.getCreateDate());
                ps.setTimestamp(11,new Timestamp(System.currentTimeMillis()));
                ps.execute();

                ResultSet generatedKeys = ps.getGeneratedKeys();

                if(generatedKeys.next())
                    vehicle.setId(generatedKeys.getInt(1));

                LOG.info("Vehicle successfully saved in the database!");

                if(vehicle.getLicenseCategories().size() != 0){
                    for (LicenseCategory i : vehicle.getLicenseCategories()) {
                        PreparedStatement pd = con.prepareStatement("INSERT INTO VehicleLicence VALUES (?,?)");
                        pd.setInt(1, vehicle.getId());
                        pd.setInt(2, i == LicenseCategory.A ? 1 : (i == LicenseCategory.B ? 2 : (i == LicenseCategory.C ? 3 : 4)) );
                        pd.execute();
                    }
                }

                /*
                if(vehicle.getDrivingLicense()!=null) {
                    for (int i : vehicle.getDrivingLicense()) {
                        PreparedStatement pd = con.prepareStatement("INSERT INTO VehicleLicence VALUES (?,?)");
                        pd.setInt(1, vehicle.getId());
                        pd.setInt(2, i);
                        pd.execute();
                    }
                }*/
            } catch (SQLException e) {
                LOG.error("Vehicle can not be created.", e.getMessage());
                throw new DAOException("Vehicle can not be created." + e);
            }
        }
        return vehicle;
    }

    @Override
    public void delete(Vehicle vehicle) throws DAOException {

        if(vehicle == null){
            LOG.debug("The Vehicle is null!");
            throw new DAOException("The Vehicle is null!");
        }else {

            try {
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE Vehicle SET isDelete = ? WHERE vid = ?");

                ps.setBoolean(1,true);
                ps.setInt(2,vehicle.getId());
                ps.executeUpdate();

                LOG.info("Vehicle is successfully deleted!");

            } catch (SQLException e) {
                e.printStackTrace();
                LOG.error("Vehicle can not be deleted.",e);
                throw new DAOException("Vehicle can not be deleted."+e);
            }
        }
    }

    @Override
    public List<Vehicle> getAllVehicleList() throws DAOException {
        LOG.debug("get All Vehicle list");

        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM VEHICLE WHERE ISDELETE = FALSE");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Vehicle vehicle = new Vehicle();
                vehicle.setId(rs.getInt("VID"));
                vehicle.setTitle(rs.getString("title"));
                vehicle.setBuildyear(rs.getInt("buildYear"));
                vehicle.setDescription(rs.getString("description"));
                vehicle.setSeatingCapacity(rs.getInt("seatingCapacity"));
                vehicle.setLicensePlate(rs.getString("licensePlate"));
                vehicle.setPowerUnit(rs.getString("powerUnit"));
                vehicle.setPower(rs.getInt("power"));
                vehicle.setPrice(rs.getFloat("price"));
                vehicle.setCreateDate(rs.getTimestamp("createDate"));
                vehicle.setImageUrl(rs.getString("imageUrl"));
                vehicle.setUpdatedate(rs.getTimestamp("updatedate"));
                vehicle.setIsdelete(rs.getBoolean("isDelete"));

                PreparedStatement pd = con.prepareStatement("SELECT L.LID " +
                    "FROM VEHICLE V1 JOIN VEHICLELICENCE V2 ON V1.VID = V2.VID JOIN LICENCECATEGORY L ON V2.LID = L.LID " +
                    "WHERE V1.VID = ?");

                pd.setInt(1,rs.getInt("VID"));
                ResultSet rd = pd.executeQuery();

                ArrayList<LicenseCategory> licenseCategories = new ArrayList<LicenseCategory>();

                while (rd.next()){
                    int id = rd.getInt("LID");
                    licenseCategories.add(id == 1 ? LicenseCategory.A : (id == 2 ? LicenseCategory.B : (id == 3 ? LicenseCategory.C : LicenseCategory.NONE)));
                }

                vehicle.setLicenseCategories(licenseCategories);
                vehicle.printLicenseCategory();
                vehicleList.add(vehicle);
            }

            LOG.info("Vehicle list successufully created.");
        }catch (SQLException e) {
            e.printStackTrace();
            LOG.error("Vehicle list can't be created!",e);
            throw new DAOException("Vehicle list can't be created!");
        }
        return vehicleList;
    }

    @Override
    public Vehicle getVehicleById(int id) throws DAOException {

        LOG.debug("get Vehicle ID: "+id);

        Vehicle vehicle = new Vehicle();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM VEHICLE WHERE VID="+id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                vehicle.setId(rs.getInt("VID"));
                vehicle.setTitle(rs.getString("title"));
                vehicle.setBuildyear(rs.getInt("buildYear"));
                vehicle.setDescription(rs.getString("description"));
                vehicle.setSeatingCapacity(rs.getInt("seatingCapacity"));
                vehicle.setLicensePlate(rs.getString("licensePlate"));
                vehicle.setPowerUnit(rs.getString("powerUnit"));
                vehicle.setPower(rs.getInt("power"));
                vehicle.setPrice(rs.getFloat("price"));
                vehicle.setCreateDate(rs.getTimestamp("createDate"));
                vehicle.setImageUrl(rs.getString("imageUrl"));
                vehicle.setUpdatedate(rs.getTimestamp("updatedate"));
                vehicle.setIsdelete(rs.getBoolean("isDelete"));

                vehicle.setLicenseCategories(getLCategory(rs.getInt("VID")));
                vehicle.printLicenseCategory();
            }

            LOG.info("Vehicle successufully created.");
        }catch (SQLException e) {
            e.printStackTrace();
            LOG.error("Vehicle can't be created!",e);
            throw new DAOException("Vehicle list can't be created!");
        }
        return vehicle;
    }

    @Override
    public List<Vehicle> find(String model, List<Double> price, int seatcap, int category, String punit, Timestamp startDate, Timestamp endDate,String ignore) throws DAOException {

        List<Vehicle> list = new ArrayList<>();

        LOG.debug("Loading filtered Vehicle.");


        String tableOK = "CREATE VIEW IF NOT EXISTS FILTER AS SELECT V.*,V2.LID,B2.STARTDATE,B2.ENDDATE FROM VEHICLE V LEFT JOIN BOOKEDVEHICLE B ON V.VID = B.VID LEFT JOIN VEHICLELICENCE V2 ON V.VID = V2.VID LEFT JOIN BOOKING B2 ON B.BID = B2.BID";

        try {
            PreparedStatement psd = con.prepareStatement(tableOK);
            psd.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String select = "SELECT DISTINCT * FROM FILTER WHERE ISDELETE = FALSE";

        if(!punit.equals("")){
            select += " AND ";
        if(punit.equals("Brawn")){
            select += "LID = 4";
        }else if(punit.equals("Motorized")){
            select += "LID = "+category;
        }}

        if(price.get(0) != 0.0){
            select += " AND ";
            select += "PRICE ";
            select +=">= " + price.get(0);
        }else if(price.get(1) != 0.0){
            select += " AND ";
            select += "PRICE ";
            select += "<= " + price.get(1);
        }

        if(!model.equals("")){
            select += " AND ";
            select += "TITLE ILIKE '%"+model+"%'";
        }

        if(seatcap != 0){
            select += " AND ";
            select += "SEATINGCAPACITY ="+seatcap;
        }

        if(!ignore.equals("")){
            select += " and VID not in "+ignore;
        }


        if(startDate != null && endDate != null) {
            String selectunion = " UNION "+select+" AND STARTDATE IS NULL";
            select += " AND STARTDATE not BETWEEN '"+startDate+"'"+ " AND '"+endDate+"'";
            select += selectunion;
        }

        select += " ORDER BY VID";

        try{
            PreparedStatement ps = con.prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            Vehicle bevor = new Vehicle();
            bevor.setId(0);

            while (rs.next()) {

                Vehicle vehicle = new Vehicle();
                vehicle.setId(rs.getInt("VID"));
                vehicle.setTitle(rs.getString("title"));
                vehicle.setBuildyear(rs.getInt("buildYear"));
                vehicle.setDescription(rs.getString("description"));
                vehicle.setSeatingCapacity(rs.getInt("seatingCapacity"));
                vehicle.setLicensePlate(rs.getString("licensePlate"));
                vehicle.setPowerUnit(rs.getString("powerUnit"));
                vehicle.setPower(rs.getInt("power"));
                vehicle.setPrice(rs.getFloat("price"));
                vehicle.setCreateDate(rs.getTimestamp("createDate"));
                vehicle.setImageUrl(rs.getString("imageUrl"));
                vehicle.setUpdatedate(rs.getTimestamp("updatedate"));
                vehicle.setIsdelete(rs.getBoolean("isDelete"));

                if(bevor.getId() != vehicle.getId()){
                vehicle.setLicenseCategories(getLCategory(rs.getInt("VID")));
                vehicle.printLicenseCategory();
                list.add(vehicle);
                }
                bevor = vehicle;
            }

            LOG.info("Vehicle successufully created.");
        }catch (SQLException e) {
            e.printStackTrace();
            LOG.error("Vehicle can't be created!",e);
            throw new DAOException("Vehicle list can't be created!");
        }

        return list;
    }

    private ArrayList<LicenseCategory> getLCategory(int v){

        PreparedStatement pd = null;
        ArrayList<LicenseCategory> licenseCategories = new ArrayList<LicenseCategory>();

        try {
            pd = con.prepareStatement("SELECT L.LID " +
                "FROM VEHICLE V1 JOIN VEHICLELICENCE V2 ON V1.VID = V2.VID JOIN LICENCECATEGORY L ON V2.LID = L.LID " +
                "WHERE V1.VID = ?");


        pd.setInt(1,v);
        ResultSet rd = pd.executeQuery();

        while (rd.next()){
            int idnr = rd.getInt("LID");
            licenseCategories.add(idnr == 1 ? LicenseCategory.A : (idnr == 2 ? LicenseCategory.B : (idnr == 3 ? LicenseCategory.C : LicenseCategory.NONE)));
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return licenseCategories;
    }

}
