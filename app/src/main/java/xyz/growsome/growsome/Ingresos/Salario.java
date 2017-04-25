package xyz.growsome.growsome.Ingresos;

import java.util.Date;

/**
 * Created by aang on 4/11/17.
 */

public class Salario extends Ingresos {

    public Salario(long userid, long catid, String desc, String nombre, double monto, Date fecha)
    {
        super(userid, catid, desc, nombre, monto, fecha);
        setTipoid(1);
    }

}
