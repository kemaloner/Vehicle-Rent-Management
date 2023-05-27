package at.ac.tuwien.sepm.assignment.individual.universe.service;

import at.ac.tuwien.sepm.assignment.individual.universe.DAO.*;
import at.ac.tuwien.sepm.assignment.individual.universe.entities.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class VehicleServiceImp implements VehicleService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private VehicleDAO vehicleDAO;

    public VehicleServiceImp() throws SQLException, DAOException {
        this.vehicleDAO = new VehicleDAOJDBC();
    }


    @Override
    public Vehicle create(Vehicle vehicle) throws ServiceException {

        LOG.info("Create Vehicle in Service has been called!");

        try {
            if (vehicle.getPrice() < 1) {
                throw new IllegalArgumentException("The Vehicle price must not be negative and not 0!");
            } else if (vehicle.getPower() < 0) {
                throw new IllegalArgumentException("The Power of Vehicle must not be negative");
            } else if (vehicle.getBuildyear() > 2018 && vehicle.getBuildyear() < 1900) {
                throw new IllegalArgumentException("Build Year should be beetwen in the year 1900 and 2018");
            } else return vehicleDAO.create(vehicle);
        } catch (DAOException e) {
            e.printStackTrace();
            LOG.error("Can't create a vehicle. There must be missing entities" + e);
        }
        throw new ServiceException("Vehicle ist null");
    }

    @Override
    public Vehicle update(Vehicle vehicle) throws ServiceException {
        LOG.info("Create Vehicle in Service has been called!");

        try {
            if (vehicle.getPrice() < 1) {
                throw new IllegalArgumentException("The Vehicle price must not be negative and not 0!");
            } else if (vehicle.getPower() < 0) {
                throw new IllegalArgumentException("The Power of Vehicle must not be negative");
            } else if (vehicle.getBuildyear() > 2018 && vehicle.getBuildyear() < 1900) {
                throw new IllegalArgumentException("Build Year should be beetwen in the year 1900 and 2018");
            } else return vehicleDAO.update(vehicle);
        } catch (DAOException e) {
            e.printStackTrace();
            LOG.error("Can't update a vehicle. There must be missing entities" + e);
        }
        throw new ServiceException("Vehicle ist null");
    }

    @Override
    public Vehicle getVehicleById(int id) throws ServiceException {

        try {
            return vehicleDAO.getVehicleById(id);
        } catch (DAOException e) {
            e.printStackTrace();
        }
        try {
            throw new DAOException("ca");
        } catch (DAOException e) {
            e.printStackTrace();
        }throw new ServiceException("Vehicle ist null");

    }

    @Override
    public List<Vehicle> getAllVehicleList() throws ServiceException {

        try {

            return vehicleDAO.getAllVehicleList();

        } catch (DAOException e) {
            LOG.debug("Exception in find");
        }
        throw new ServiceException("Vehicle ist null");

    }

    @Override
    public List<Vehicle> getdeletedVehicleList() throws ServiceException {
        return null;
    }

    @Override
    public void delete(Vehicle vehicle) throws ServiceException {
        try {
            vehicleDAO.delete(vehicle);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("Can't delete Vehicle");
        }
    }

    @Override
    public List<Vehicle> find(String model, List<Double> price, int seatcap, int category, String punit, Timestamp startDate, Timestamp endDate, String ignore) throws ServiceException {
        try {
            return vehicleDAO.find(model,price,seatcap,category,punit,startDate,endDate,ignore);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException("Vehicle is null");
        }
    }

    @Override
    public void copyFile(File sourceFile) throws ServiceException {

        File destFile = new File(
            "src/main/resources/img/"+sourceFile.getName());

        FileChannel source = null;
        FileChannel destination = null;

        try {

            source = new FileInputStream(sourceFile).getChannel();

            destination = new FileOutputStream(destFile).getChannel();

            if (source != null) {
                destination.transferFrom(source, 0, source.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("File not Found for Image Copy"+e);
            throw new ServiceException("File not Found for Image Copy");
        }

        finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ServiceException("Source File is null");
                }
            }
            if (destination != null) {
                try {
                    destination.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ServiceException("Destination File is null");
                }
            }
        }

    }

}
