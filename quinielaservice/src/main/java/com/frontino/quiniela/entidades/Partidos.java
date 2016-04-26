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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author casc
 */
@Entity
@Table(name = "partidos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partidos.findAll", query = "SELECT p FROM Partidos p"),
    @NamedQuery(name = "Partidos.findById", query = "SELECT p FROM Partidos p WHERE p.id = :id"),
    @NamedQuery(name = "Partidos.findByGolEquipo1", query = "SELECT p FROM Partidos p WHERE p.golEquipo1 = :golEquipo1"),
    @NamedQuery(name = "Partidos.findByGolEquipo2", query = "SELECT p FROM Partidos p WHERE p.golEquipo2 = :golEquipo2"),
    @NamedQuery(name = "Partidos.findByFecha", query = "SELECT p FROM Partidos p WHERE p.fecha = :fecha")})
public class Partidos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "gol_equipo1")
    private int golEquipo1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "gol_equipo2")
    private int golEquipo2;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private Character status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPartido")
    private Collection<MarcadorQuinielas> marcadorQuinielasCollection;
    @JoinColumn(name = "id_equipo1", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Equipos idEquipo1;
    @JoinColumn(name = "id_equipo2", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Equipos idEquipo2;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id")
    @ManyToOne
    private Grupos idGrupo;

    public Partidos() {
    }

    public Partidos(Integer id) {
        this.id = id;
    }

    public Partidos(Integer id, int golEquipo1, int golEquipo2) {
        this.id = id;
        this.golEquipo1 = golEquipo1;
        this.golEquipo2 = golEquipo2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getGolEquipo1() {
        return golEquipo1;
    }

    public void setGolEquipo1(int golEquipo1) {
        this.golEquipo1 = golEquipo1;
    }

    public int getGolEquipo2() {
        return golEquipo2;
    }

    public void setGolEquipo2(int golEquipo2) {
        this.golEquipo2 = golEquipo2;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
    
    @XmlTransient
    public Collection<MarcadorQuinielas> getMarcadorQuinielasCollection() {
        return marcadorQuinielasCollection;
    }

    public void setMarcadorQuinielasCollection(Collection<MarcadorQuinielas> marcadorQuinielasCollection) {
        this.marcadorQuinielasCollection = marcadorQuinielasCollection;
    }

    public Equipos getIdEquipo1() {
        return idEquipo1;
    }

    public void setIdEquipo1(Equipos idEquipo1) {
        this.idEquipo1 = idEquipo1;
    }

    public Equipos getIdEquipo2() {
        return idEquipo2;
    }

    public void setIdEquipo2(Equipos idEquipo2) {
        this.idEquipo2 = idEquipo2;
    }

    public Grupos getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Grupos idGrupo) {
        this.idGrupo = idGrupo;
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
        if (!(object instanceof Partidos)) {
            return false;
        }
        Partidos other = (Partidos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.frontino.quiniela.entidades.Partidos[ id=" + id + " ]";
    }
    
}
