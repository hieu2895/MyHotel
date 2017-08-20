package com.example.hieu.myhotel.Modules;

import java.io.Serializable;

/**
 *
 */

public class booking implements Serializable{
    String idUser;
    String idBookÌnfor;

    public booking() {
    }

    public booking(String idUser, String idBookÌnfor) {
        this.idUser = idUser;
        this.idBookÌnfor = idBookÌnfor;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdBookÌnfor() {
        return idBookÌnfor;
    }

    public void setIdBookÌnfor(String idBookÌnfor) {
        this.idBookÌnfor = idBookÌnfor;
    }
}
