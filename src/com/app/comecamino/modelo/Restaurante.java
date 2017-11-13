package com.app.comecamino.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Restaurante implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String ID;
	private String contenido;
	private String author;
	private String guid;
	
	private String web;	
	private String tfno;
	private String direccion;	
	private String localidad;
	private String provincia;
	private String cp;
	private String lat;
	private String lon;
	private String lista_cocina;
	private String lista_cheque;
	private String lista_tiporest;
	private String ratings_average;
	private String rating_users;

public Restaurante(String ID)
	{
		this.ID = ID;
		
		
	}
public String getName(){
	return name;
}

public String getID(){
	return ID;
}

public String getAuthor(){
	return author;
}

public String getContenido(){
	return contenido;
}

public void setName(String name)
{
	this.name=name;
}
public void setID(String ID)
{
	this.ID=ID;
}
public void setAuthor(String author)
{
	this.author=author;
}
public void setContenido(String contenido)
{
	this.contenido=contenido;
}
public String getDireccion() {
	return direccion;
}
public void setDireccion(String direccion) {
	this.direccion = direccion;
}
public String getTfno() {
	return tfno;
}
public void setTfno(String tfno) {
	this.tfno = tfno;
}
public String getWeb() {
	return web;
}
public void setWeb(String web) {
	this.web = web;
}
public String getLocalidad() {
	return localidad;
}
public void setLocalidad(String localidad) {
	this.localidad = localidad;
}
public String getLat() {
	return lat;
}
public void setLat(String lat) {
	this.lat = lat;
}
public String getLon() {
	return lon;
}
public void setLon(String lon) {
	this.lon = lon;
}
public String getLista_cocina() {
	return lista_cocina;
}
public void setLista_cocina(String lista_cocina) {
	this.lista_cocina = lista_cocina;
}
public String getLista_cheque() {
	return lista_cheque;
}
public void setLista_cheque(String lista_cheque) {
	this.lista_cheque = lista_cheque;
}
public String getLista_tiporest() {
	return lista_tiporest;
}
public void setLista_tiporest(String lista_tiporest) {
	this.lista_tiporest = lista_tiporest;
}
public String getProvincia() {
	return provincia;
}
public void setProvincia(String provincia) {
	this.provincia = provincia;
}
public String getCp() {
	return cp;
}
public void setCp(String cp) {
	this.cp = cp;
}
public String getGuid() {
	return guid;
}
public void setGuid(String guid) {
	this.guid = guid;
}
public String getRatings_average() {
	return ratings_average;
}
public void setRatings_average(String ratings_average) {
	this.ratings_average = ratings_average;
}
public String getRating_users() {
	return rating_users;
}
public void setRating_users(String rating_users) {
	this.rating_users = rating_users;
}
}
