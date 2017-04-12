package xyz.growsome.growsome.Ingresos;

/**
 * Created by aang on 4/11/17.
 */

public abstract class Ingresos {

    private String descripcion;
    private String nombre;
    private double monto;

   /* public Ingresos(String name, String desc, double cost)
    {
        descripcion = desc;
        nombre = name;
        monto = cost;
    }
    */
    public void setDescripcion(String descripc)
    {
        descripcion = descripc;
    }

    public void setNombre(String name)
    {
        nombre = name;
    }

    public void setMonto(double price)
    {
        monto = price;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public double getMonto()
    {
        return monto;
    }

    /* tambien debo poder seleccionar el tipo de ingreso al agregar Ingreso (o bien Gasto) */

}
