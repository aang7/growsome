package xyz.growsome.growsome.Ingresos;

import java.util.Date;

/**
 * Created by aang on 4/11/17.
 */

public abstract class Ingresos
{
    private int userid;
    private int tipoid;
    private String desc;
    private String nombre;
    private double monto;
    private Date fecha;

    public Ingresos(int userid, String desc, String nombre, double monto, Date fecha)
    {
        this.userid = userid;
        this.desc = desc;
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
    }

    public void setUserid(int userid)
    {
        this.userid = userid;
    }

    public void setTipoid(int tipoid)
    {
        this.tipoid = tipoid;
    }

    public void setDesc(String descripc)
    {
        desc = descripc;
    }

    public void setNombre(String name)
    {
        nombre = name;
    }

    public void setMonto(double price)
    {
        monto = price;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public int getUserid()
    {
        return userid;
    }

    public int getTipoid()
    {
        return tipoid;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getDesc()
    {
        return desc;
    }

    public double getMonto()
    {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }

}