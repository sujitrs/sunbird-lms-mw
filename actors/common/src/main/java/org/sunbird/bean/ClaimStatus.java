package org.sunbird.bean;


/**
 * this class will have user action performed on the shadow user record.
 * @author anmolgupta
 *
 */
public enum ClaimStatus {


    CLAIMED(0),
    UNCLAIMED(1),
    REJECTED(2),
    FAILED(3),
    MULTIMATCH(4);

    private int value;
    ClaimStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
