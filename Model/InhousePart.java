package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author tylergoodfred
 */
public class InhousePart extends Part {

    private final IntegerProperty machineID;

    public InhousePart() {
        super();
        machineID = new SimpleIntegerProperty();
    }

    public void setPartMachineID(int machineID) {
        this.machineID.set(machineID);
    }

    public int getPartMachineID() {
        return this.machineID.get();
    }
}
