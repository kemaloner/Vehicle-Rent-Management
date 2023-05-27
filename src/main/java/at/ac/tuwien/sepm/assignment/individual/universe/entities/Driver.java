package at.ac.tuwien.sepm.assignment.individual.universe.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Driver {

    private int did;
    private Timestamp ldate;

    public void setDid(int did) {
        this.did = did;
    }

    public void setLdate(Timestamp ldate) {
        this.ldate = ldate;
    }

    public int getDid() {
        return did;
    }

    public Timestamp getLdate() {
        return ldate;
    }
}
