package com.tiendatextil.model;

public enum TipoAlmacen {
    TIENDA, ALMACEN;


    // Metodo para obtener un enum a partir de una cadena en mayúsculas
    public static TipoAlmacen fromString(String tipo) {
        if (tipo != null) {
            try {
                return TipoAlmacen.valueOf(tipo.toUpperCase()); 
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor no válido para TipoAlmacen: " + tipo);
            }
        }
        return null;
    }
}