package xyz.growsome.growsome.Categorias;

import java.util.Date;

/**
 * Created by cheko on 24/04/2017.
 */

public class Categoria
{

    private long id;
    private long userid;
    private String name;
    private String color;
    private String desc;

    public Categoria(long id, long userid, String name, String color, String desc)
    {
        setId(id);
        setName(name);
        setUserid(userid);
        setColor(color);
        setDesc(desc);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
