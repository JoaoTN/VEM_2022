package ufc.npi.prontuario.model;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public abstract class PacienteDocumento {
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Documento> documentos;

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void addDocumento(Documento documento) {
        if (this.documentos == null) {
            this.documentos = new ArrayList<>();
        }

        this.documentos.add(documento);
    }

    public void removerDocumento(Documento documento) {
        this.documentos.remove(documento);
    }
}
