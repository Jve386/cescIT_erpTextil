package com.tiendatextil.model;

public enum TipoAlmacen {
    TIENDA, ALMACEN;


    // Metodo para obtener un enum a partir de una cadena en mayúsculas
    public static TipoAlmacen fromString(String tipo) {
        if (tipo != null) {
            try {
                return TipoAlmacen.valueOf(tipo.toUpperCase()); // Convertir la cadena a mayúsculas antes de buscar el valor en el enum
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor no válido para TipoAlmacen: " + tipo);
            }
        }
        return null; // O lanzar una excepción si prefieres
    }
}