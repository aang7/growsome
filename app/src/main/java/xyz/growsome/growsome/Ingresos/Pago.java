package xyz.growsome.growsome.Ingresos;

import java.util.Date;

/**
 * Created by aang on 4/11/17.
 */

public class Pago extends Ingresos {


    public Pago(int userid, String desc, String nombre, double monto, Date fecha)
    {
        super(userid, desc, nombre, monto, fecha);
        setTipoid(2);
    }
}
