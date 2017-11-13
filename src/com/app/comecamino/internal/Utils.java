package com.app.comecamino.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.app.comecamino.modelo.Restaurante;

import android.util.Log;

public class Utils {
	
	public static String obtenerLista(String cadenaEntrada, int tipoEntrada )
	{
		String devolver ="";
		Log.i("tipoEntrada",String.valueOf(tipoEntrada));
		List<String> lista_devolver =new ArrayList<String>();
		String[] arrayDatosEntrada = cadenaEntrada.split(":");
		int numeroElementos = Integer.parseInt(arrayDatosEntrada[1]);
		Log.i("numeroElementos: ",arrayDatosEntrada[1]);
		
		if(numeroElementos!=0)
		{
			switch (tipoEntrada) {
			
			case Constants.cocina:
				
				for (int i = 0; i < arrayDatosEntrada.length; i++) {
					Log.i("elemento cocina: ", arrayDatosEntrada[i]);
					if(arrayDatosEntrada[i].contains(Constants.COCINA1)) devolver = devolver+Constants.COCINA1 +",";
					if(arrayDatosEntrada[i].contains(Constants.COCINA2)) devolver = devolver+Constants.COCINA2+",";
					if(arrayDatosEntrada[i].contains(Constants.COCINA3)) devolver = devolver+Constants.COCINA3+",";
					if(arrayDatosEntrada[i].contains(Constants.COCINA4)) devolver = devolver+Constants.COCINA4;
										
				}
				break;
				
			case Constants.cheque:
				
				
				for (int i = 0; i < arrayDatosEntrada.length; i++) {
					if(arrayDatosEntrada[i].contains(Constants.CHEQUE1)) devolver = devolver + Constants.CHEQUE1 + ",";
					if(arrayDatosEntrada[i].contains(Constants.CHEQUE2)) devolver = devolver + Constants.CHEQUE2;
					 		
				}
				break;
			case Constants.tipoRest:
				
				for (int i = 0; i < arrayDatosEntrada.length; i++) {
					Log.i("elemento tipoRest: ", arrayDatosEntrada[i]);
					if(arrayDatosEntrada[i].contains(Constants.TREST1)) devolver = devolver + Constants.TREST1 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TREST2)) devolver = devolver + Constants.TREST2 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TREST3)) devolver = devolver + Constants.TREST3 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TREST4)) devolver = devolver + Constants.TREST4 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TREST5)) devolver = devolver + Constants.TREST5 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TREST6)) devolver = devolver + Constants.TREST6 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TREST7)) devolver = devolver + Constants.TREST7;
					  	
				}
				break;
				
			case Constants.caracMenu:
				
				for (int i = 0; i < arrayDatosEntrada.length; i++) {
					
					if(arrayDatosEntrada[i].contains(Constants.CMENU1)) devolver = devolver + Constants.CMENU1 + ",";
					if(arrayDatosEntrada[i].contains(Constants.CMENU2)) devolver = devolver + Constants.CMENU2 + ",";
					if(arrayDatosEntrada[i].contains(Constants.CMENU3)) devolver = devolver + Constants.CMENU3;
										
				}
				break;
				
			case Constants.tipoMenu:
				
				for (int i = 0; i < arrayDatosEntrada.length; i++) {
					if(arrayDatosEntrada[i].contains(Constants.TMENU1)) devolver = devolver + Constants.TMENU1 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TMENU2)) devolver = devolver + Constants.TMENU2 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TMENU3)) devolver = devolver + Constants.TMENU3;
					   					
				}
				break;
				
			case Constants.tipoPlato:
				
				
				for (int i = 0; i < arrayDatosEntrada.length; i++) {
					if(arrayDatosEntrada[i].contains(Constants.TPLATO1)) devolver = devolver + Constants.TPLATO1 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TPLATO2)) devolver = devolver + Constants.TPLATO2 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TPLATO3)) devolver = devolver + Constants.TPLATO3 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TPLATO4)) devolver = devolver + Constants.TPLATO4 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TPLATO5)) devolver = devolver + Constants.TPLATO5 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TPLATO6)) devolver = devolver + Constants.TPLATO6 ;
					   				
				}
				break;
				
			case Constants.tipoDesayuno:
				
				
				for (int i = 0; i < arrayDatosEntrada.length; i++) {
					if(arrayDatosEntrada[i].contains(Constants.TDESAYUNO1)) devolver = devolver + Constants.TDESAYUNO1 + ",";
					if(arrayDatosEntrada[i].contains(Constants.TDESAYUNO2)) devolver = devolver + Constants.TDESAYUNO2;
					
					   				
				}
				break;
			default:
				break;
			}
			
		}
		if(!devolver.equals(""))
		{
			char ult = devolver.charAt(devolver.length()-1);
			if(String.valueOf(ult).equals(",")) devolver = devolver.substring(0, devolver.length()-1);
		}
		Log.i("devolver: ", devolver);
		return devolver;
		
	}
	
	public static ArrayList<String> obtenerIdsListaEntrada(String listaEntrada)
	
	{
		ArrayList <String> lista_ids = new ArrayList<String>();
		String[] arrayDatos = listaEntrada.split("\"");
		for (int i = 0; i < arrayDatos.length; i++) {
			//Log.i("dato id: ",arrayDatos[i] );
			if(isNumeric(arrayDatos[i])) 
				{
				
				Log.i("esnumerico:", "Si");
				lista_ids.add(arrayDatos[i]);
				}
			
		}
		return lista_ids;
	}
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	public static ArrayList<Restaurante> listaFinalFiltrada(List<Restaurante> lista_cercanos, String filtro)
	{
		//Log.i("lista_cercanos dentro de listaFinal: ", String.valueOf(lista_cercanos.size()));
		//Log.i("filtro en listaFinalFiltrada: ", filtro);
		ArrayList<Restaurante> m_restaurantesFinal = new ArrayList<Restaurante>();
		for(int i=0; i<lista_cercanos.size(); i++)
		{
			//Log.i("nombre restaurante: ",lista_cercanos.get(i).getName());
			
			if(lista_cercanos.get(i).getLista_cocina()!=null)
			{
				//Log.i("lista_cercanos.get(i).getLista_cocina(): ",lista_cercanos.get(i).getLista_cocina());
				int pos = lista_cercanos.get(i).getLista_cocina().lastIndexOf(filtro);
				//Log.i("pppoooooooooooos: ", String.valueOf(pos));
				if(pos!=-1)
					{
					//Log.i("esDistinto de -1", "esDistinto de -1");
					m_restaurantesFinal.add(lista_cercanos.get(i));
					}
			}
			
			//if(lista_cercanos.get(i).getLista_cocina().contains(filtro)) m_restaurantesFinal.add(lista_cercanos.get(i));
		}
		//Log.i("m_restaurantesFinal size: ", String.valueOf(m_restaurantesFinal.size()));
		return m_restaurantesFinal;
		
	}

}
