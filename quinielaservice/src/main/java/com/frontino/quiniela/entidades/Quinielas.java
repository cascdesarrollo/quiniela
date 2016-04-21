/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frontino.quiniela.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author casc
 */
@Entity
@Table(name = "quinielas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Quinielas.findAll", query = "SELECT q FROM Quinielas q"),
    @NamedQuery(name = "Quinielas.findById", query = "SELECT q FROM Quinielas q WHERE q.id = :id"),
    @NamedQuery(name = "Quinielas.findByAlias", query = "SELECT q FROM Quinielas q WHERE q.alias = :alias"),
    @NamedQuery(name = "Quinielas.findByAcumulado", query = "SELECT q FROM Quinielas q WHERE q.acumulado = :acumulado")})
public class Quinielas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 25)
    @Column(name = "alias")
    private String alias;
    @Column(name = "acumulado")
    private Integer acumulado;
    @Column(name = "status")
    private String status;
    @Column(name = "fecharegistro")
    @Temporal(TemporalType.DATE)
    private Date fecharegistro;
    
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idQuiniela")
    private Collection<MarcadorQuinielas> marcadorQuinielasCollection;

    public Quinielas() {
    }

    public Quinielas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getAcumulado() {
        return acumulado;
    }

    public void setAcumulado(Integer acumulado) {
        this.acumulado = acumulado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(Date fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    
    
    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    @XmlTransient
    public Collection<MarcadorQuinielas> getMarcadorQuinielasCollection() {
        return marcadorQuinielasCollection;
    }

    public void setMarcadorQuinielasCollection(Collection<MarcadorQuinielas> marcadorQuinielasCollection) {
        this.marcadorQuinielasCollection = marcadorQuinielasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Quinielas)) {
            return false;
        }
        Quinielas other = (Quinielas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.frontino.quiniela.entidades.Quinielas[ id=" + id + " ]";
    }
    
}
