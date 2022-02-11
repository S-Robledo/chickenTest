package com.spring.chickenTest.modelo;

import java.time.LocalDate;
import java.util.Date;

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
		private int idGallina;
		private boolean isHuevo;
		private double dinero;
		//adjuntar fecha creacion
		
		private Date fechaCreacion;
		private Date pasarDeDia;
		
		//constructor
		public Gallina() {
			
		}
		public Gallina(boolean b) {
			this.isHuevo = b;
			this.dinero = 100;
			this.fechaCreacion = new Date();
			this.pasarDeDia = java.sql.Date.valueOf(LocalDate.now());
			
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
		
		
		//adjuntar fecha creacion
		public Date getFechaCreacion() {
			return fechaCreacion;
		}
		public void setFechaCreacion(Date fechaCreacion) {
			this.fechaCreacion = fechaCreacion;
		}
		
		//pasar de dia
		
		public void pasarDeDia() {
			
		}
		
		
				
}
