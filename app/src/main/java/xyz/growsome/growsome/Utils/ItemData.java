package xyz.growsome.growsome.Utils;

/**
 * Created by aang on 4/24/17.
 */

public class ItemData {

    String name;
    String monto;
    String cat;
    long id;


    public ItemData(long _id, String _name, String _monto, String _cat)
    {
        setMonto(_monto);
        setName(_name);
        setCat(_cat);
        setId(_id);

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
