package com.peyto.athena.engine.entity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.peyto.athena.engine.math.HexUtils;
import com.peyto.athena.engine.math.NormalizedHexagonCoordinates;

public class FieldMap implements Serializable{
	private final int sizeX;
	private final int sizeY;
	
	private final Map<Position, FieldType> allFields;
	
	private FieldMap(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		allFields = new HashMap<Position, FieldType>();
	}
	public static FieldMap getEmptyFieldMap(int sizeX, int sizeY) {
		FieldMap f = new FieldMap(sizeX, sizeY);
		for (int i=0; i<sizeX; i++) {
			for (int j=0; j<sizeY; j++) {
				NormalizedHexagonCoordinates norm = HexUtils.convertToNormalized(i, j);
				f.addField(norm.getX(), norm.getY(), FieldType.grass);
			}
		}
		return f;
	}
	
	public static FieldMap saveMapFromUtilsEditor(HttpServletRequest request) {
		int sizeX = Integer.valueOf(request.getParameter("sizeX"));
		int sizeY = Integer.valueOf(request.getParameter("sizeY"));
		String[] xCoord = request.getParameterValues("x");
		String[] yCoord = request.getParameterValues("y");
		String[] ft = request.getParameterValues("fieldtype");
		
		FieldMap res = new FieldMap(sizeX, sizeY);
		for (int i=0; i<xCoord.length; i++) {
			res.addField(Integer.valueOf(xCoord[i]), Integer.valueOf(yCoord[i]), FieldType.valueOf(ft[i]));
		}
		return res;
	}
	
	public static FieldMap deSerialize(String fileName) {
		try {
			//use buffering
			InputStream file = new FileInputStream(fileName);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			try {
				FieldMap result = (FieldMap) input.readObject();
				return result;
			} finally {
				input.close();
			}
		} catch (ClassNotFoundException ex) {
			System.out.println("Class not fount. Probabily wrong file");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Unsuccess");
			ex.printStackTrace();
		}
		return null;
		
	}
	
	public static void serialize(FieldMap map, String fileName) {
	    try {
	      OutputStream file = new FileOutputStream(fileName);
	      OutputStream buffer = new BufferedOutputStream(file);
	      ObjectOutput output = new ObjectOutputStream(buffer);
	      try {
	    	  output.writeObject(map);
	      } catch (IOException e) {
	    	  throw e;
	      } finally {
	    	  if (output!=null) {
	    		  output.close();
	    	  }
	      }
	    }  
	    catch(IOException ex){
	    	System.out.println("Unsuccess!!");
	    	ex.printStackTrace();
	    }
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}
	
	public FieldType getFieldType(int x, int y) {
		return allFields.get(new Position(x, y));
	}
	
	private void addField(int x, int y, FieldType ft) {
		allFields.put(new Position(x, y), ft);
	}
	
	
}
