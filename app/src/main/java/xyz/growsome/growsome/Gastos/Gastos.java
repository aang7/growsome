package xyz.growsome.growsome.Gastos;

/**
 * Created by aang on 4/11/17.
 */

public abstract class Gastos {
    private String descripcion;
    private String nombre;
    private double costo;

    public void setDescripcion(String descripc)
    {
        descripcion = descripc;
    }

    public void setNombre(String name)
    {
        nombre = name;
    }

    public void setCosto(double price)
    {
        costo = price;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public double getCosto()
    {
        return costo;
    }
}
