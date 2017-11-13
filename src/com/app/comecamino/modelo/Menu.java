package com.app.comecamino.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Menu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ID;
	private String autor;
	private String guid;
	
	private String restaurante;
	private String nombre;
	private String fecha;
	private String precio;
	private String incluye;
	private String tipoMenu;
	private String caracteristicas;
	private String platos;
	
	private String entrantes;
	private String primeros;
	private String segundos;
	private String postres;
	private ArrayList<Plato> lista_todos_platos;
	
	//TIPO DATO DESAYUNO
	private String tipoComanda; //para saber si es desayuno o menu
	private String descripcion;
	private String horario;
	private String tipos_desayuno;
	
	
	
	public Menu(String ID)
	{
		this.setID(ID);
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getIncluye() {
		return incluye;
	}

	public void setIncluye(String incluye) {
		this.incluye = incluye;
	}

	public String getTipoMenu() {
		return tipoMenu;
	}

	public void setTipoMenu(String tipoMenu) {
		this.tipoMenu = tipoMenu;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public String getPlatos() {
		return platos;
	}

	public void setPlatos(String platos) {
		this.platos = platos;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(String restaurante) {
		this.restaurante = restaurante;
	}

	public String getEntrantes() {
		return entrantes;
	}

	public void setEntrantes(String entrantes) {
		this.entrantes = entrantes;
	}

	public String getPrimeros() {
		return primeros;
	}

	public void setPrimeros(String primeros) {
		this.primeros = primeros;
	}

	public String getSegundos() {
		return segundos;
	}

	public void setSegundos(String segundos) {
		this.segundos = segundos;
	}

	public String getPostres() {
		return postres;
	}

	public void setPostres(String postres) {
		this.postres = postres;
	}

	public ArrayList<Plato> getLista_todos_platos() {
		return lista_todos_platos;
	}

	public void setLista_todos_platos(ArrayList<Plato> lista_todos_platos) {
		this.lista_todos_platos = lista_todos_platos;
	}

	public String getTipoComanda() {
		return tipoComanda;
	}

	public void setTipoComanda(String tipoComanda) {
		this.tipoComanda = tipoComanda;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getTipos_desayuno() {
		return tipos_desayuno;
	}

	public void setTipos_desayuno(String tipos_desayuno) {
		this.tipos_desayuno = tipos_desayuno;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
}
