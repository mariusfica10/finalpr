package domain;

import java.io.Serializable;

public abstract class Entity<ID> implements Serializable{

    private static final long serialVersionUID = 7331115341259248461L;
    protected ID id;

    public ID getID()
    {
        return id;
    }
    public void setID(ID id) {this.id = id;}

    public Entity(ID id)
    {
        this.id = id;
    }

}
