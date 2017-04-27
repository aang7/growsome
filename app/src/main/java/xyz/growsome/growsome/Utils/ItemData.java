package xyz.growsome.growsome.Utils;

/**
 * Created by aang on 4/24/17.
 */

public class ItemData {

    String name;
    String monto;
    String cat;
    long id;

    /** Params:
     *          _catcolor: category color **/
    public ItemData(long _id, String _name, String _monto, String _catcolor)
    {
        setMonto(_monto);
        setName(_name);
        setCatColor(_catcolor);
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

    public String getCatColor() {
        return cat;
    }

    public void setCatColor(String cat) {
        this.cat = cat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
