package xyz.growsome.growsome.Gastos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aang on 4/11/17.
 */

public abstract class Gastos {

    private long userid;
    private long tipoid;
    private long catid;
    private String desc;
    private String nombre;
    private double costo;
    private Date fecha;
    private int cantidad;

    public Gastos(long userid, long catid, String desc, String nombre, double costo, Date fecha)
    {
        this.userid = userid;
        this.catid = catid;
        this.desc = desc;
        this.nombre = nombre;
        this.costo = costo;
        this.fecha = fecha;
    }

    public Gastos(long userid, long catid, String desc, String nombre, double costo, int cantidad, Date fecha)
    {
        this.userid = userid;
        this.catid = catid;
        this.desc = desc;
        this.nombre = nombre;
        this.costo = costo;
        this.fecha = fecha;
        setCantidad(cantidad);
    }
    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getTipoid() {
        return tipoid;
    }

    public void setTipoid(long tipoid) {
        this.tipoid = tipoid;
    }

    public long getCatid() {
        return catid;
    }

    public void setCatid(long catid) {
        this.catid = catid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Date getFecha()
    {
        return this.fecha;
    }

    public String getFecha(String format)
    {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(this.fecha);
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        if (cantidad <= 0)
            cantidad = 1;

        return cantidad;
    }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
