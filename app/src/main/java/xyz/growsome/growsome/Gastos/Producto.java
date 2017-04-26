package xyz.growsome.growsome.Gastos;

import java.util.Date;

/**
 * Created by aang on 4/11/17.
 */

public class Producto extends Gastos
{
    public Producto(long userid, long catid, String desc, String nombre, double costo, int cantidad, Date fecha)
    {
        super(userid, catid, desc, nombre, costo, cantidad, fecha);
        setTipoid(2);
    }
}
