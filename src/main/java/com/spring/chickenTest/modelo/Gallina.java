package com.spring.chickenTest.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gallina3")
public class Gallina {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "ID_GALLINA")
		private int idGallina;
		
		@Column(name = "IS_HUEVO")
		private boolean isHuevo;
		
		@Column(name = "DINERO")
		private double dinero;
		//adjuntar fecha creacion
		
		@Column(name = "CREACION")
		private int creacion;
		
		@Column(name = "PASAR_DIA")
		private int pasarDia;
		//private Date fechaCreacion;
		//private Date pasarDeDia;
		
		//constructor
		public Gallina() {
			
		}
		public Gallina(boolean b) {
			this.isHuevo = b;
			this.dinero = 100;
			//this.fechaCreacion = new Date();
			//this.pasarDeDia = java.sql.Date.valueOf(LocalDate.now());
			this.creacion=0;
			this.pasarDia=0;
		}
		public int getIdGallina() {
			return idGallina;
		}
		public void setIdGallina(int idGallina) {
			this.idGallina = idGallina;
		}
		 
		public boolean isHuevo() {
			return isHuevo;
		}
		public void setHuevo(boolean isHuevo) {
			this.isHuevo = isHuevo;
		}
		public double getDinero() {
			return dinero;
		}
		public void setDinero(double dinero) {
			this.dinero = dinero;
		}
		
		public int getCreacion() {
			return creacion;
		}
		public void setCreacion(int creacion) {
			this.creacion = creacion;
		}
		
		public int getPasarDia() {
			return pasarDia;
		}
		public void setPasarDia(int pasarDia) {
			this.pasarDia = pasarDia;
		}
		//adjuntar fecha creacion
//		public Date getFechaCreacion() {
//			return fechaCreacion;
//		}
//		public void setFechaCreacion(Date fechaCreacion) {
//			this.fechaCreacion = fechaCreacion;
//		}
						
}
