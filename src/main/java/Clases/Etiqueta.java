package Clases;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Etiqueta implements Serializable {
    @Id
    private long id;
    private String etiqueta;

    public Etiqueta(){

    }

    public Etiqueta(String etiqueta){
        this.id = Controladora.getInstance().getMisEtiquetas().size()+1;
        this.etiqueta = etiqueta;
    }

    public long getId() { return id; }
    public String getEtiqueta() { return etiqueta; }


    public void setId(long id) { this.id = id; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }

}
