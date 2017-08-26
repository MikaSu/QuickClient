package org.quickclient.classes;

/**
 *
 * @author Administrator
 */
public class QListItem {
    private String rObjectId;
    private String name;

    public String getRObjectId() {
        return rObjectId;
    }

    public void setRObjectId(String rObjectId) {
        this.rObjectId = rObjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }
    
    @Override
    public String toString() {
       return name; 
    }
    
}