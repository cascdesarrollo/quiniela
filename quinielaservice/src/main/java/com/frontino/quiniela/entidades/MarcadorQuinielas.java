/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frontino.quiniela.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author casc
 */
@Entity
@Table(name = "marcador_quinielas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MarcadorQuinielas.findAll", query = "SELECT m FROM MarcadorQuinielas m"),
    @NamedQuery(name = "MarcadorQuinielas.findById", query = "SELECT m FROM MarcadorQuinielas m WHERE m.id = :id"),
    @NamedQuery(name = "MarcadorQuinielas.findByGolEquipo1", query = "SELECT m FROM MarcadorQuinielas m WHERE m.golEquipo1 = :golEquipo1"),
    @NamedQuery(name = "MarcadorQuinielas.findByGolEquipo2", query = "SELECT m FROM MarcadorQuinielas m WHERE m.golEquipo2 = :golEquipo2"),
    @NamedQuery(name = "MarcadorQuinielas.findByPuntos", query = "SELECT m FROM MarcadorQuinielas m WHERE m.puntos = :puntos"),
    @NamedQuery(name = "MarcadorQuinielas.findByAcumulado", query = "SELECT m FROM MarcadorQuinielas m WHERE m.acumulado = :acumulado")})
public class MarcadorQuinielas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "gol_equipo1")
    private Integer golEquipo1;
    @Column(name = "gol_equipo2")
    private Integer golEquipo2;
    @Column(name = "puntos")
    private Integer puntos;
    @Column(name = "acumulado")
    private Integer acumulado;
    @JoinColumn(name = "id_partido", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Partidos idPartido;
    @JoinColumn(name = "id_quiniela", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Quinielas idQuiniela;

    public MarcadorQuinielas() {
    }

    public MarcadorQuinielas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGolEquipo1() {
        return golEquipo1;
    }

    public void setGolEquipo1(Integer golEquipo1) {
        this.golEquipo1 = golEquipo1;
    }

    public Integer getGolEquipo2() {
        return golEquipo2;
    }

    public void setGolEquipo2(Integer golEquipo2) {
        this.golEquipo2 = golEquipo2;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Integer getAcumulado() {
        return acumulado;
    }

    public void setAcumulado(Integer acumulado) {
        this.acumulado = acumulado;
    }

    public Partidos getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Partidos idPartido) {
        this.idPartido = idPartido;
    }

    public Quinielas getIdQuiniela() {
        return idQuiniela;
    }

    public void setIdQuiniela(Quinielas idQuiniela) {
        this.idQuiniela = idQuiniela;
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
        if (!(object instanceof MarcadorQuinielas)) {
            return false;
        }
        MarcadorQuinielas other = (MarcadorQuinielas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.frontino.quiniela.entidades.MarcadorQuinielas[ id=" + id + " ]";
    }
    
}
