package xyz.growsome.growsome.Gastos;

import java.util.Date;

/**
 * Created by aang on 4/11/17.
 */

public class Servicio extends Gastos {

    public Servicio(long userid, long catid, String desc, String nombre, double costo, Date fecha) {
        super(userid, catid, desc, nombre, costo, fecha);
    }
    public Servicio(long userid, long catid, String desc, String nombre, double costo,int cantidad, Date fecha) {
        super(userid, catid, desc, nombre, costo, cantidad, fecha);
    }
}
